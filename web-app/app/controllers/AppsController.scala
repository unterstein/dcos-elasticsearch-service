package controllers

import javax.inject._

import deployment.DeploymentHelper
import play.api.data.Forms._
import play.api.data._
import play.api.data.format.Formats._
import play.api.mvc._
import storage.AppStorage

@Singleton
class AppsController @Inject()(cc: ControllerComponents) extends BaseController(cc) {

  def list = AuthenticatedAction {
    implicit request =>
      val apps = AppStorage.getApps(request.user)
      Ok(views.html.apps.appsPage(apps, AppsController.appsForm))
  }

  def create = AuthenticatedAction {
    implicit request =>
      AppsController.appsForm.bindFromRequest.fold(
        formWithErrors => {
          val apps = AppStorage.getApps(request.user)
          BadRequest(views.html.apps.appsPage(apps, formWithErrors))
        },
        appData => {
          val maybeApp = AppStorage.storeApp(request.user, appData.name, appData.cpu, appData.mem, appData.disk, appData.port)
          maybeApp.map {
            app =>
              DeploymentHelper.deploy(request.user, app)
          }
          Redirect(routes.AppsController.list).withSession(USER_ID -> request.user.email)
        }
      )
  }
}

object AppsController {

  case class AppsData(name: String, cpu: Double, mem: Double, disk: Double, port: Int)

  val appsForm = Form(
    mapping(
      "name" -> nonEmptyText,
      "cpu" -> of(doubleFormat),
      "mem" -> of(doubleFormat),
      "disk" -> of(doubleFormat),
      "port" -> of(intFormat)
    )(AppsData.apply)(AppsData.unapply)
  )
}