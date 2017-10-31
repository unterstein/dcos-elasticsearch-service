import com.google.inject.AbstractModule
import services.InitService

class Module extends AbstractModule {

  override def configure(): Unit = {
    bind(classOf[InitService]).asEagerSingleton()
  }

}
