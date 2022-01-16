package it.anesin.workout.provider

import io.vertx.core.Vertx
import io.vertx.ext.auth.PubSecKeyOptions
import io.vertx.ext.auth.authorization.RoleBasedAuthorization
import io.vertx.ext.auth.jwt.JWTAuth
import io.vertx.ext.auth.jwt.JWTAuthOptions
import io.vertx.ext.auth.mongo.*
import io.vertx.ext.mongo.MongoClient
import io.vertx.ext.web.handler.AuthorizationHandler
import io.vertx.ext.web.handler.BasicAuthHandler
import io.vertx.ext.web.handler.JWTAuthHandler

interface AuthProvider {
  fun jwtAuthentication(): JWTAuth
  fun jwtAuthenticationHandler(): JWTAuthHandler
  fun basicAuthenticationHandler(): BasicAuthHandler
  fun roleAuthorizationHandler(role: UserRole): AuthorizationHandler
  fun mongoUserUtil() : MongoUserUtil
}

class DefaultAuthProvider(vertx: Vertx, mongoClient: MongoClient, jwtKeys: Pair<String, String>) : AuthProvider {
  private val mongoAuthenticationOptions = MongoAuthenticationOptions().setCollectionName("users")
  private val mongoAuthentication = MongoAuthentication.create(mongoClient, mongoAuthenticationOptions)
  private val mongoAuthorizationOptions = MongoAuthorizationOptions()
  private val mongoAuthorization = MongoAuthorization.create("provider", mongoClient, mongoAuthorizationOptions)
  private val mongoUserUtil = MongoUserUtil.create(mongoClient, mongoAuthenticationOptions, mongoAuthorizationOptions)

  private val firstKey = PubSecKeyOptions().setAlgorithm("RS256").setBuffer(jwtKeys.first)
  private val secondKey = PubSecKeyOptions().setAlgorithm("RS256").setBuffer(jwtKeys.second)
  private val jwtAuthenticationOptions = JWTAuthOptions().addPubSecKey(firstKey).addPubSecKey(secondKey)
  private val jwtAuthentication = JWTAuth.create(vertx, jwtAuthenticationOptions)

  override fun jwtAuthentication() = jwtAuthentication!!
  override fun jwtAuthenticationHandler() = JWTAuthHandler.create(jwtAuthentication)!!
  override fun basicAuthenticationHandler() = BasicAuthHandler.create(mongoAuthentication)!!
  override fun roleAuthorizationHandler(role: UserRole) = AuthorizationHandler.create(RoleBasedAuthorization.create(role.name)).addAuthorizationProvider(mongoAuthorization)!!
  override fun mongoUserUtil(): MongoUserUtil = mongoUserUtil
}

enum class UserRole { ADMIN, TRAINER, TRAINEE }
