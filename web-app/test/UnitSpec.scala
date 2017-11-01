import controllers.AuthorizationController
import org.scalatestplus.play._
import play.api.test.FakeRequest
import play.api.test.Helpers._
import play.api.test.CSRFTokenHelper._

/**
  * Unit tests can run without a full Play application.
  */
class UnitSpec extends PlaySpec {

  "ApplicationController" should {

    "return a valid result with action" in {
      val controller = new AuthorizationController(stubControllerComponents())
      val result = controller.loginPage(FakeRequest().withCSRFToken)
      contentAsString(result) must include("Elasticsearch as a service web app")
    }
  }

}
