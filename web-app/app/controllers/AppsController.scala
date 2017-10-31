package controllers

import javax.inject._

import play.api.mvc._

@Singleton
class AppsController @Inject()(cc: ControllerComponents) extends BaseController(cc) {

  def list = AuthenticatedAction {
    implicit request =>
      Ok("ok!!")
  }


}

object AppsController {

}
