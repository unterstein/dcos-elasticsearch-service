package storage

import helper.HashHelper
import models.User
import org.elasticsearch.common.xcontent.XContentFactory._


object UserStorage {

  private[storage] val index = "user"
  private[storage] val indexType = "user"

  def getUser(email: String): Option[User] = {
    val maybeUser = ESClient.client.prepareGet(index, indexType, email).execute().actionGet()

    maybeUser match {
      case user if user.isExists && email == "" + user.getSource.get("email") => Some(User("" + user.getSource().get("email"), "" + user.getSource().get("password")))
      case _ => None
    }
  }


  def getUser(email: String, password: String): Option[User] = getUser(email).filter(user => user.password == HashHelper.sha256(password))

  def storeUser(email: String, password: String): Option[User] = {
    val hashedPassword = HashHelper.sha256(password)
    val response = ESClient.client.prepareIndex(index, indexType)
        .setSource(
          jsonBuilder()
              .startObject()
              .field("email", email)
              .field("password", hashedPassword)
              .endObject()
        ).setCreate(true).setId(email).execute().actionGet()

    println(response)
    getUser(email, password)
  }
}
