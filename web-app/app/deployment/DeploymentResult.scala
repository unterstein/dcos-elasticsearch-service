package deployment

sealed trait ServerStatus

object ServerStatusOk extends ServerStatus

object ServerStatusFailed extends ServerStatus

case class DeploymentResult(status: ServerStatus, deploymentId: Option[String])
