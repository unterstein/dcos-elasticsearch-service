package helper

import java.util.UUID

object UUIDHelper {

  def uuid: String = UUID.randomUUID().toString.replace("-", "")
}
