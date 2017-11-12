package storage

import java.net.InetAddress

import helper.EnvironmentHelper
import org.elasticsearch.client.Client
import org.elasticsearch.common.settings.Settings
import org.elasticsearch.common.transport.InetSocketTransportAddress
import org.elasticsearch.common.xcontent.XContentFactory._
import org.elasticsearch.transport.client.PreBuiltTransportClient

import scala.collection.JavaConverters._

object ESClient {
  private val settings = Settings.builder()
      .put("cluster.name", EnvironmentHelper.getConfiguration("ELASTICSEARCH_NAME", "elasticsearch"))
      .build()


  // FIXME
  if (EnvironmentHelper.getConfiguration("WAIT_FOR_ELASTICSEARCH", "false") == "true") {
    Thread.sleep(5000L)
  }

  val client: Client = new PreBuiltTransportClient(settings)
      .addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName(
        EnvironmentHelper.getConfiguration("ELASTICSEARCH_URL", "localhost")), 9300)
      )

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
    if (!client.admin().indices().prepareExists(AppStorage.index).execute().actionGet().isExists) {
      val createResponse = client.admin().indices()
          .prepareCreate(AppStorage.index)
          .addMapping(AppStorage.indexType, jsonBuilder()
              .startObject()
              .field("properties", Map(
                "user" -> Map("type" -> "text", "store" -> "yes").asJava,
                "name" -> Map("type" -> "text", "store" -> "yes").asJava,
                "cpu" -> Map("type" -> "double", "store" -> "yes").asJava,
                "mem" -> Map("type" -> "double", "store" -> "yes").asJava,
                "disk" -> Map("type" -> "double", "store" -> "yes").asJava,
                "port" -> Map("type" -> "long", "store" -> "yes").asJava
              ).asJava)
              .endObject()
          ).execute().actionGet()
    }
  }

  def onStop(): Unit = {
    client.close()
  }
}
