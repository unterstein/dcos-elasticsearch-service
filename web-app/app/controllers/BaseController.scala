package controllers

import models.User
import play.api.i18n.Lang
import play.api.mvc.Security.AuthenticatedRequest
import play.api.mvc._

class BaseController(cc: ControllerComponents) extends AbstractController(cc) {

  implicit val messages = messagesApi.preferred(Seq(Lang.defaultLang))

  def AuthenticatedAction(f: => AuthenticatedRequest[AnyContent, User] => Result): EssentialAction = Security.Authenticated(userInfo, onUnauthorized) {
    user =>
      BaseAction(request => {
        f(new BaseRequest(request, user))
      })
  }

  def BaseAction(f: Request[AnyContent] => Result): Action[AnyContent] = Action {
    implicit request =>
      f(request)
  }

  private def userInfo(request: RequestHeader): Option[User] = {
    val maybeEmail = request.session.get(USER_ID)
    maybeEmail.map {
      email =>
        // TODO check if mail and password matches
        User(email, email)
    }
  }

  class BaseRequest(request: Request[AnyContent], user: User) extends AuthenticatedRequest[AnyContent, User](user, request)

  val USER_ID = "email"

  private def onUnauthorized(requestHeader: RequestHeader) = Redirect(routes.AuthorizationController.loginPage())
}
