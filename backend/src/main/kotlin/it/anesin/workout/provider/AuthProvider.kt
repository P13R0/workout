package it.anesin.workout.provider

import io.vertx.core.Future
import io.vertx.core.http.impl.HttpClientConnection.log
import io.vertx.ext.auth.User
import io.vertx.ext.auth.authentication.UsernamePasswordCredentials
import io.vertx.ext.auth.authorization.RoleBasedAuthorization
import io.vertx.ext.auth.mongo.*
import io.vertx.ext.mongo.MongoClient
import io.vertx.ext.web.handler.BasicAuthHandler

interface AuthProvider {
  fun addUser(username: String, password: String, role: UserRole): Future<User>
}

class DefaultAuthProvider(mongoClient: MongoClient) : AuthProvider {
  private val mongoAuthenticationOptions = MongoAuthenticationOptions().setCollectionName("users")
  private val mongoAuthentication = MongoAuthentication.create(mongoClient, mongoAuthenticationOptions)
  private val mongoAuthorizationOptions = MongoAuthorizationOptions()
  private val mongoAuthorization = MongoAuthorization.create("provider", mongoClient, mongoAuthorizationOptions)
  private val mongoUserUtil = MongoUserUtil.create(mongoClient, mongoAuthenticationOptions, mongoAuthorizationOptions)

  fun basicAuthHandler() = BasicAuthHandler.create(mongoAuthentication)!!

  override fun addUser(username: String, password: String, role: UserRole): Future<User> {
    val credentials = UsernamePasswordCredentials(username, password)

    return mongoAuthentication.authenticate(credentials)
      .onSuccess { user -> addAuthorization(user, role) }
      .onFailure {
        mongoUserUtil.createUser(credentials.username, credentials.password)
          .onSuccess {
            log.info("User ${credentials.username} created")
            mongoAuthentication.authenticate(credentials)
              .onSuccess { user -> addAuthorization(user, role) }
          }
          .onFailure { log.error("Failed add role $role to user ${credentials.username}", it) }
      }
  }

  private fun addAuthorization(user: User, role: UserRole) {
    mongoAuthorization.getAuthorizations(user)
      .onSuccess {
        if (isRoleAbsent(role, user)) {
          val username = user.principal().getString("username")
          mongoUserUtil.createUserRolesAndPermissions(username, listOf(role.name), listOf())
            .onSuccess { log.info("Role $role added to user $username") }
            .onFailure { log.error("Failed add role $role to user $username") }
        }
      }
  }

  private fun isRoleAbsent(role: UserRole, user: User) = !RoleBasedAuthorization.create(role.name).match(user)
}

enum class UserRole { ADMIN, TRAINER, TRAINEE }
