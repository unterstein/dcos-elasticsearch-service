package services

import javax.inject._

import play.api.inject.ApplicationLifecycle
import storage.ESClient

import scala.concurrent.Future

@Singleton
class InitService @Inject()(appLifecycle: ApplicationLifecycle) {

  ESClient.onStart()

  appLifecycle.addStopHook { () =>
    ESClient.onStop()
    Future.successful(())
  }
}
