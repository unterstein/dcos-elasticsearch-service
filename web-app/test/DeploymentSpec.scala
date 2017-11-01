import deployment.DeploymentHelper
import models.{App, User}
import org.scalatestplus.play._

/**
 * Unit tests can run without a full Play application.
 */
class DeploymentSpec extends PlaySpec {

  "DeploymentHelper" should {

    "return a rendered template for given app and user" in {
      val user = User("asd@asd.de", "kjadskjasdlsadh")
      val app = App("adslkadsklasdkjl", "testapp", 1, 64, 1000, 9200)
      val deploymentDescriptor = DeploymentHelper.rendeTemplate(user, app).replace(" ", "").replace("\\n", "")
      deploymentDescriptor must include("\"id\":\"/adslkadsklasdkjl\"")
      deploymentDescriptor must include("\"cpus\":1.0")
      deploymentDescriptor must include("\"mem\":64.0")
      deploymentDescriptor must include("\"VIP_0\":\"adslkadsklasdkjl:9200\"")
      deploymentDescriptor must include("\"HAPROXY_0_PORT\":\"9200\"")
      // usually there should be a whitespace, but this is trimmed
      deploymentDescriptor must include("\"ES_JAVA_OPTS\":\"-Xmx48m-Xms48m\"")
    }
  }

}
