package deployment

import helper.EnvironmentHelper
import models.{App, User}
import org.apache.commons.io.IOUtils
import org.apache.http.client.methods.{HttpGet, HttpPost}
import org.apache.http.entity.StringEntity
import org.apache.http.impl.client.HttpClients
import play.api.libs.json.Json

object DeploymentHelper {
  private val template = IOUtils.toString(getClass.getClassLoader.getResourceAsStream("marathon-template.json"), "UTF-8")
  private val client = HttpClients.createDefault()

  private val marathonUrl = EnvironmentHelper.getConfiguration("MARATHON_URL", "http://localhost:8080/")

  // TODO introduce cache and use /v2/groups endpoint
  def getAppStatus(user: User, app: App): AppStatus = {
    val get = new HttpGet(s"$marathonUrl/v2/apps/userelasticsearch/${app.id}")
    val response = client.execute(get)
    if (response.getStatusLine.getStatusCode == 200) {
      val input = IOUtils.toString(response.getEntity.getContent, "UTF-8")
      deserializeAppStatus(input)
    } else {
      throw new RuntimeException("o_O") // FIXME
    }
  }

  def deserializeAppStatus(input: String): AppStatus = {
    import Formats._
    Json.fromJson[AppStatus](Json.parse(input)).get
  }

  def deploy(user: User, app: App): DeploymentResult = {
    val post = new HttpPost(s"$marathonUrl/v2/apps")
    post.setHeader("Content-Type", "application/json")
    post.setEntity(new StringEntity(rendeTemplate(user, app)))

    val response = client.execute(post)
    if (response.getStatusLine.getStatusCode == 201) {
      DeploymentResult(ServerStatusOk, Some(IOUtils.toString(response.getEntity.getContent, "UTF-8")))
    } else {
      DeploymentResult(ServerStatusFailed, None)
    }
  }

  def rendeTemplate(user: User, app: App): String = {
    template
        .replace("{{id}}", app.id)
        .replace("{{cpu}}", "" + app.cpu)
        .replace("{{mem}}", "" + app.mem)
        .replace("{{memory}}", "" + (app.mem * 0.75).toInt)
        .replace("{{disk}}", "" + app.disk)
        .replace("{{port}}", "" + app.port)
  }

}
