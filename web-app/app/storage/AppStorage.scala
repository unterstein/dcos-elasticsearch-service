package storage

import helper.UUIDHelper
import models.{App, User}
import org.elasticsearch.common.xcontent.XContentFactory._
import org.elasticsearch.index.query.QueryBuilders

object AppStorage {

  private[storage] val index = "app"
  private[storage] val indexType = "app"

  def getApps(user: User): List[App] = {
    val hits = ESClient.client.prepareSearch(index).setTypes(indexType)
        .setQuery(QueryBuilders.matchQuery("user", user.email))
        .execute().actionGet().getHits.getHits

    hits.filter(hit => hit.getSource.get("user") == user.email).map(hit => mapToApp(hit.getId, hit.getSource)).toList
  }

  def getApp(id: String): Option[App] = {
    val app = ESClient.client.prepareGet(index, indexType, id).execute().actionGet()
    if (app.isExists) {
      Some(mapToApp(app.getId, app.getSource))
    } else {
      None
    }
  }

  def storeApp(user: User, name: String, cpu: Double, mem: Double, disk: Double, port: Int): Option[App] = {
    val response = ESClient.client.prepareIndex(index, indexType)
        .setSource(
          jsonBuilder()
              .startObject()
              .field("user", user.email)
              .field("name", name)
              .field("cpu", cpu)
              .field("mem", mem)
              .field("disk", disk)
              .field("port", port)
              .endObject()
        ).setCreate(true).setId(UUIDHelper.uuid).execute().actionGet()
    getApp(response.getId)
  }

  private def mapToApp(id: String, input: java.util.Map[String, AnyRef]) = App(
    id,
    input.get("name").asInstanceOf[String],
    input.get("cpu").asInstanceOf[Double],
    input.get("mem").asInstanceOf[Double],
    input.get("disk").asInstanceOf[Double],
    input.get("port").asInstanceOf[Int],
  )
}
