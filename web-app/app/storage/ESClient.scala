package storage

import java.net.InetAddress

import org.elasticsearch.client.Client
import org.elasticsearch.common.settings.Settings
import org.elasticsearch.common.transport.InetSocketTransportAddress
import org.elasticsearch.common.xcontent.XContentFactory._
import org.elasticsearch.transport.client.PreBuiltTransportClient

import scala.collection.JavaConverters._

object ESClient {
  private val settings = Settings.builder() /*.put("cluster.name", "myClusterName")*/ .build()


  // FIXME make this configurable
  val client: Client = new PreBuiltTransportClient(settings)
      .addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName("localhost"), 9300))

  onStart()

  def onStart(): Unit = {
    if (!client.admin().indices().prepareExists(UserStorage.index).execute().actionGet().isExists) {
      val createResponse = client.admin().indices()
          .prepareCreate(UserStorage.index)
          .addMapping(UserStorage.indexType, jsonBuilder()
              .startObject()
              .field("properties", Map(
                "email" -> Map("type" -> "text", "store" -> "yes").asJava,
                "password" -> Map("type" -> "text", "store" -> "yes").asJava
              ).asJava)
              .endObject()
          ).execute().actionGet()
    }
  }

  // FIXME callme
  def onStop(): Unit = {
    client.close()
  }
}