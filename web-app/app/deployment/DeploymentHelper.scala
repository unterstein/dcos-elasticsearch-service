package deployment

import models.{App, User}
import org.apache.commons.io.IOUtils
import org.apache.http.client.methods.HttpPost
import org.apache.http.entity.StringEntity
import org.apache.http.impl.client.HttpClients

object DeploymentHelper {
  private val template = IOUtils.toString(getClass.getClassLoader.getResourceAsStream("marathon-template.json"), "UTF-8")
  private val client = HttpClients.createDefault()

  def deploy(user: User, app: App): DeploymentResult = {
    // FIXME sys.env()
    val marathonUrl = "http://localhost:8080/"
    val post = new HttpPost(marathonUrl + "/v2/apps")
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
