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

    hits.filter(hit => hit.getSource.get("user") == user.email).map(hit => App(
      hit.getId,
      hit.getSource().get("name").asInstanceOf[String],
      hit.getSource().get("cpu").asInstanceOf[Double],
      hit.getSource().get("mem").asInstanceOf[Double],
      hit.getSource().get("disk").asInstanceOf[Double],
      hit.getSource().get("port").asInstanceOf[Int],
    )).toList
  }

  def getApp(user: User, name: String): Option[App] = getApps(user).find(app => app.name == name)

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
    getApp(user, name)
  }
}
