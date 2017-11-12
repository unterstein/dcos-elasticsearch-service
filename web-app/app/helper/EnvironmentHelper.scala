package helper

object EnvironmentHelper {

  def getConfiguration(key: String, default: String): String = {
    if (sys.env.contains(key)) {
      sys.env(key)
    } else {
      default
    }
  }

}
