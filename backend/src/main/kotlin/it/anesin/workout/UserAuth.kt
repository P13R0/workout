package it.anesin.workout

import io.vertx.core.http.impl.HttpClientConnection
import io.vertx.ext.auth.authentication.UsernamePasswordCredentials
import io.vertx.ext.auth.authorization.RoleBasedAuthorization
import io.vertx.ext.auth.mongo.*
import io.vertx.ext.mongo.MongoClient
import io.vertx.ext.web.handler.BasicAuthHandler

class UserAuth(mongoClient: MongoClient) {
  private val mongoAuthenticationOptions = MongoAuthenticationOptions().setCollectionName("users")
  private val mongoAuthentication = MongoAuthentication.create(mongoClient, mongoAuthenticationOptions)
  private val mongoAuthorizationOptions = MongoAuthorizationOptions()
  private val mongoAuthorization = MongoAuthorization.create("provider", mongoClient, mongoAuthorizationOptions)
  private val mongoUserUtil = MongoUserUtil.create(mongoClient, mongoAuthenticationOptions, mongoAuthorizationOptions)

  fun basicAuthHandler() = BasicAuthHandler.create(mongoAuthentication)!!

  fun addUser(credentials: UsernamePasswordCredentials, role: UserRole) {
    mongoAuthentication.authenticate(credentials)
      .onSuccess { user ->
        mongoAuthorization.getAuthorizations(user)
          .onSuccess {
            if (!RoleBasedAuthorization.create(role.name).match(user)) {
              mongoUserUtil.createUserRolesAndPermissions(user.principal().getString("username"), listOf(role.name), listOf())
                .onSuccess { HttpClientConnection.log.info("Role $role added to user ${credentials.username}") }
                .onFailure { HttpClientConnection.log.error("Failed add role $role to user ${credentials.username}") }
            }
          }
      }
      .onFailure {
        mongoUserUtil.createUser(credentials.username, credentials.password)
          .onSuccess {
            HttpClientConnection.log.info("User ${credentials.username} created")
            mongoAuthentication.authenticate(credentials)
              .onSuccess { user ->
                mongoAuthorization.getAuthorizations(user)
                  .onSuccess {
                    if (!RoleBasedAuthorization.create(role.name).match(user)) {
                      mongoUserUtil.createUserRolesAndPermissions(user.principal().getString("username"), listOf(role.name), listOf())
                        .onSuccess { HttpClientConnection.log.info("Role $role added to user ${credentials.username}") }
                        .onFailure { HttpClientConnection.log.error("Failed add role $role to user ${credentials.username}") }
                    }
                  }
              }
          }
          .onFailure { HttpClientConnection.log.error("Failed add role $role to user ${credentials.username}", it) }
      }
  }
}

enum class UserRole { ADMIN, TRAINER, TRAINEE }
