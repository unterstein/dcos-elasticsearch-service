package services

import javax.inject._

import play.api.inject.ApplicationLifecycle
import storage.ESClient

import scala.concurrent.Future

@Singleton
class InitService @Inject()(appLifecycle: ApplicationLifecycle) {

  // FIXME for tests, elasticsearch must be up and running
  ESClient.onStart()

  appLifecycle.addStopHook { () =>
    ESClient.onStop()
    Future.successful(())
  }
}
