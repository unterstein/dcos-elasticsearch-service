package controllers

import javax.inject._

import play.api.data.Forms._
import play.api.data._
import play.api.mvc._
import storage.UserStorage

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
          UserStorage.storeUser(userData.email, userData.password) match {
            case None =>
              // TODO add error message that user already exists or error happened
              BadRequest(views.html.login.loginPage(AuthorizationController.loginForm, AuthorizationController.registerForm))
            case Some(user) =>
              Redirect(routes.AppsController.list()).withSession(USER_ID -> user.email)
          }
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
          UserStorage.getUser(userData.email, userData.password) match {
            case None =>
              // TODO add error message that user already exists or error happened
              BadRequest(views.html.login.loginPage(AuthorizationController.loginForm, AuthorizationController.registerForm))
            case Some(user) =>
              Redirect(routes.AppsController.list()).withSession(USER_ID -> user.email)
          }
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
        .verifying("error.userExists", register => UserStorage.getUser(register.email).isEmpty)
  )
}
