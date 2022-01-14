package it.anesin.workout.provider

import io.vertx.core.json.JsonArray
import io.vertx.core.json.JsonObject
import java.io.FileNotFoundException
import java.util.*

interface ConfigProvider {
  fun mongo(): JsonObject
  fun adminUsername(): String
  fun adminPassword(): String
  fun jwtKeys(): Pair<String, String>
}

class PropertiesConfigProvider : ConfigProvider {
  private val prop = Properties()

  init {
    val inputStream = javaClass.classLoader.getResourceAsStream("config.properties")
    if (inputStream != null) prop.load(inputStream)
    else throw FileNotFoundException("Config file not found in the resources")
  }

  override fun mongo(): JsonObject = JsonObject()
    .put("hosts", JsonArray()
      .add(JsonObject().put("host", prop.getProperty("mongo_host_1")).put("port", 27017))
      .add(JsonObject().put("host", prop.getProperty("mongo_host_2")).put("port", 27017))
      .add(JsonObject().put("host", prop.getProperty("mongo_host_3")).put("port", 27017)))
    .put("replicaSet", prop.getProperty("mongo_replica_set"))
    .put("db_name", prop.getProperty("mongo_db_name"))
    .put("username", prop.getProperty("mongo_username"))
    .put("password", prop.getProperty("mongo_password"))
    .put("ssl", true)
    .put("authSource", "admin")

  override fun adminUsername(): String = prop.getProperty("admin_username")
  override fun adminPassword(): String = prop.getProperty("admin_password")
  override fun jwtKeys(): Pair<String, String> = Pair(prop.getProperty("jwt_public_key"), prop.getProperty("jwt_private_key"))
}
