package deployment

import play.api.libs.functional.syntax._
import play.api.libs.json._

sealed trait ServerStatus

object ServerStatusOk extends ServerStatus

object ServerStatusFailed extends ServerStatus

case class DeploymentResult(status: ServerStatus, deploymentId: Option[String])

case class AppStatus(id: String, tasksRunning: Int, tasksHealthy: Int, tasksUnhealthy: Int, instances: Seq[InstanceStatus]) {

  def isHealthy: Boolean = instances.forall(_.isHealthy)
}

case class InstanceStatus(id: String, healthCheckResults: Seq[HealthCheckResult], hostname: String, ports: Seq[Int]) {

  def isHealthy: Boolean = healthCheckResults.forall(_.alive)

  def namedPorts: Map[String, Int] = List("http", "transport").zip(ports).toMap
}

case class HealthCheckResult(alive: Boolean, lastSuccess: String)

object Formats {

  implicit val formatHCResult: Format[HealthCheckResult] = (
    (__ \ "alive").format[Boolean] ~
    (__ \ "lastSuccess").format[String]
  ) (HealthCheckResult.apply, unlift(HealthCheckResult.unapply))

  implicit val formatInstanceStatus: Format[InstanceStatus] = (
    (__ \ "id").format[String] ~
    (__ \ "healthCheckResults").format[Seq[HealthCheckResult]] ~
    (__ \ "host").format[String] ~
    (__ \ "ports").format[Seq[Int]]
  ) (InstanceStatus.apply, unlift(InstanceStatus.unapply))

  implicit val formatAppStatus: Format[AppStatus] = (
    (__ \ "app" \ "id").format[String] ~
    (__ \ "app" \ "tasksRunning").format[Int] ~
    (__ \ "app" \ "tasksHealthy").format[Int] ~
    (__ \ "app" \ "tasksUnhealthy").format[Int]  ~
    (__ \ "app" \ "tasks").format[Seq[InstanceStatus]]
  ) (AppStatus.apply, unlift(AppStatus.unapply))
}