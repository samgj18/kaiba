package com.topsy.kaiba.services

import com.topsy.kaiba.models.User
import pdi.jwt.JwtClaim
import zhttp.http._
import zio._
import com.topsy.kaiba.utils.jwt.Tokenizer._

object Authentication {
  // type alias to use for other layers
  type AuthenticationEnv = Has[Authentication.Service]

  // service definition
  trait Service {
    def getUser(token: JwtClaim): UIO[Option[User]]
  }

  // layer; includes service implementation
  val live: ZLayer[Any, Throwable, AuthenticationEnv] = ZLayer.succeed(new Service {
    // Go to the database and ask for the user
    override def getUser(token: JwtClaim): UIO[Option[User]] =
      ZIO.succeed(Option(new User(user(token), "dummy@dummy.co")))
  })

  // front-facing API, aka "accessor"
  def getUser(token: JwtClaim): ZIO[AuthenticationEnv, Throwable, Option[User]] = ZIO.accessM(_.get.getUser(token))

  def user(claim: JwtClaim): String = claim.content
}
