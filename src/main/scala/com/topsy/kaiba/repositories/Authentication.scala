package com.topsy.kaiba.repositories

import pdi.jwt.JwtClaim
import zio._

import com.topsy.kaiba.models.User

object Authentication {
  // type alias to use for other layers
  type AuthenticationEnv = Has[Authentication.Service]

  // service definition
  trait Service {
    def getUser(token: JwtClaim): UIO[Option[User]]
  }

  // live; includes service implementation
  val live: ZLayer[Any, Throwable, AuthenticationEnv] = ZLayer.succeed(new Service {
    // TODO: Go to the database and ask for the user
    override def getUser(token: JwtClaim): UIO[Option[User]] =
      ZIO.succeed(Option(new User(token.content, "Samuel", "dummy@dummy.co")))
  })

  // front-facing API, aka "accessor"
  def getUser(token: JwtClaim): ZIO[AuthenticationEnv, Throwable, Option[User]] = ZIO.accessM(_.get.getUser(token))

  def user(claim: JwtClaim): String = claim.content
}
