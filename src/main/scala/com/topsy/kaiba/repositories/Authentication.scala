package com.topsy.kaiba.repositories

import com.topsy.kaiba.models.User
import pdi.jwt.JwtClaim
import zio._

trait Authentication {
  def getUser(token: JwtClaim): RIO[ZEnv, List[User]]
  def createUser(user: User): RIO[ZEnv, List[User]]
  def user(claim: JwtClaim): UIO[String]
}

final case class AuthenticationLive(queries: Queries) extends Authentication {
  override def getUser(token: JwtClaim): RIO[ZEnv, List[User]] =
    // TODO: token.content must be replaced with actual id
    queries.getUserQuery("dummy_id")

  override def user(claim: JwtClaim): UIO[String] = ZIO.succeed(claim.content)

  override def createUser(user: User): RIO[ZEnv, List[User]] = queries.createUserQuery(user)
}

object AuthenticationLive {
  val layer: ULayer[Has[Queries => AuthenticationLive]] =
    ZLayer.succeed(AuthenticationLive(_))
}

object Authentication {
  def getUser(token: JwtClaim): ZIO[ZEnv with Has[Authentication], Throwable, List[User]] =
    ZIO.service[Authentication].flatMap(_.getUser(token))

  def createUser(user: User): ZIO[ZEnv with Has[Authentication], Throwable, List[User]] =
    ZIO.service[Authentication].flatMap(_.createUser(user))

  def user(claim: JwtClaim): ZIO[Has[Authentication], Nothing, String] = ZIO.serviceWith[Authentication](_.user(claim))
}
