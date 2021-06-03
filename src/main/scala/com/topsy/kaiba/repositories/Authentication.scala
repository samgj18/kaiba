package com.topsy.kaiba.repositories

import pdi.jwt.JwtClaim
import zio._
import com.topsy.kaiba.models.User
trait Authentication {
  def getUser(token: JwtClaim): UIO[Option[User]]
  def user(claim: JwtClaim): UIO[String]
}

final case class AuthenticationLive() extends Authentication {
  override def getUser(token: JwtClaim): UIO[Option[User]] = ZIO.succeed {
    Option(new User(token.content, "Samuel", "dummy@dummy.co"))
  }

  override def user(claim: JwtClaim): UIO[String] = ZIO.succeed(claim.content)
}

object AuthenticationLive {
  val layer: ULayer[Has[Authentication]] = {
     ZLayer.succeed(Authentication)
  }
}

object Authentication {
  def getUser(token: JwtClaim): ZIO[Has[Authentication], Nothing, Option[User]] =
    ZIO.serviceWith[Authentication](_.getUser(token))
  def user(claim: JwtClaim): ZIO[Has[Authentication], Nothing, String] = ZIO.serviceWith[Authentication](_.user(claim))
}
