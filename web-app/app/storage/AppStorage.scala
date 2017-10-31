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
      hit.getField("name").getValue[String],
      hit.getField("cpu").getValue[String].toDouble,
      hit.getField("mem").getValue[String].toDouble,
      hit.getField("disk").getValue[String].toDouble)
    ).toList
  }

  def getApp(user: User, name: String): Option[App] = getApps(user).find(app => app.name == name)

  def storeApp(user: User, name: String, cpu: Double, mem: Double, disk: Double): Option[App] = {
    val response = ESClient.client.prepareIndex(index, indexType)
        .setSource(
          jsonBuilder()
              .startObject()
              .field("user", user.email)
              .field("name", name)
              .field("cpu", cpu)
              .field("mem", mem)
              .field("disk", disk)
              .endObject()
        ).setCreate(true).setId(UUIDHelper.uuid).execute().actionGet()
    getApp(user, name)
  }
}
