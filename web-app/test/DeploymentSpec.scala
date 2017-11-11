import deployment.{AppStatus, DeploymentHelper, HealthCheckResult, InstanceStatus}
import models.{App, User}
import org.apache.commons.io.IOUtils
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
      deploymentDescriptor must include("\"id\":\"/userelasticsearch/adslkadsklasdkjl\"")
      deploymentDescriptor must include("\"cpus\":1.0")
      deploymentDescriptor must include("\"mem\":64.0")
      deploymentDescriptor must include("\"VIP_0\":\"adslkadsklasdkjl:9200\"")
      deploymentDescriptor must include("\"HAPROXY_0_PORT\":\"9200\"")
      // usually there should be a whitespace, but this is trimmed
      deploymentDescriptor must include("\"ES_JAVA_OPTS\":\"-Xmx48m-Xms48m\"")
    }

    "parse rest responeses" in {
      val json = IOUtils.toString(getClass.getResourceAsStream("response3Healthy.json"), "UTF-8")
      val appStatus = DeploymentHelper.deserializeAppStatus(json)
      val expected =
        AppStatus("/test", 3, 3, 0,
          List(
            InstanceStatus("test.e668e579-c6d4-11e7-a712-f2272e7e0262", List(HealthCheckResult(true, "2017-11-11T11:39:30.967Z")), "10.0.3.83", List(25141, 25142)),
            InstanceStatus("test.e6690c8a-c6d4-11e7-a712-f2272e7e0262", List(HealthCheckResult(true, "2017-11-11T11:39:30.967Z")), "10.0.2.130", List(2090, 2091)),
            InstanceStatus("test.e6684938-c6d4-11e7-a712-f2272e7e0262", List(HealthCheckResult(true, "2017-11-11T11:39:30.967Z")), "10.0.3.83", List(29648, 29649))
          )
        )
      expected must equal(appStatus)
    }
  }
}
