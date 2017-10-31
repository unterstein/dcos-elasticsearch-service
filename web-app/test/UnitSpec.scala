import akka.actor.ActorSystem
import controllers.ApplicationController
import org.scalatestplus.play._
import play.api.test.Helpers._
import play.api.test.FakeRequest

/**
 * Unit tests can run without a full Play application.
 */
class UnitSpec extends PlaySpec {

  "ApplicationController" should {

    "return a valid result with action" in {
      val controller = new ApplicationController(stubControllerComponents())
      val result = controller.index(FakeRequest())
      contentAsString(result) must include("Welcome")
    }
  }

}
