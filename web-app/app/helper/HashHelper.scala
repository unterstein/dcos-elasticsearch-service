package helper

object HashHelper {

  private val sha256 = java.security.MessageDigest.getInstance("SHA-256")

  def sha256(text: String): String = String.format("%064x", new java.math.BigInteger(1, sha256.digest(text.getBytes("UTF-8"))))

}
