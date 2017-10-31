package controllers

import javax.inject._

import play.api.data.Forms._
import play.api.data._
import play.api.mvc._

@Singleton
class AuthorizationController @Inject()(cc: ControllerComponents) extends BaseController(cc) {

  def loginPage = BaseAction {
    implicit request =>
      Ok(views.html.login.loginPage(AuthorizationController.loginForm, AuthorizationController.registerForm))
  }

  def register = BaseAction {
    implicit request =>
      AuthorizationController.registerForm.bindFromRequest.fold(
        formWithErrors => {
          BadRequest(views.html.login.loginPage(AuthorizationController.loginForm, formWithErrors))
        },
        userData => {
          //        Redirect(routes.Application.home(id))
          Ok("ok")
        }
      )
  }

  def login = BaseAction {
    implicit request =>
      AuthorizationController.loginForm.bindFromRequest.fold(
        formWithErrors => {
          BadRequest(views.html.login.loginPage(formWithErrors, AuthorizationController.registerForm))
        },
        userData => {
          //        Redirect(routes.Application.home(id))
          Ok("ok")
        }
      )
  }

}

object AuthorizationController {

  case class LoginData(email: String, password: String)

  val loginForm = Form(
    mapping(
      "email" -> email,
      "password" -> nonEmptyText
    )(LoginData.apply)(LoginData.unapply)
  )

  case class RegisterData(email: String, password: String, password2: String)

  val registerForm = Form(
    mapping(
      "email" -> email,
      "password" -> nonEmptyText,
      "password2" -> nonEmptyText
    )(RegisterData.apply)(RegisterData.unapply)
        .verifying("error.passwordNotMatching", register => register.password == register.password2)
  )
}
