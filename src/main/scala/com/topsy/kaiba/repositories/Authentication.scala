package com.topsy.kaiba.repositories

import com.topsy.kaiba.models.User
import pdi.jwt.JwtClaim
import zio._

trait Authentication {
  def getUser(token: JwtClaim): Task[List[User]]
  def createUser(user: User): Task[List[User]]
  def user(claim: JwtClaim): Task[String]
}

final case class AuthenticationLive(queries: Queries) extends Authentication {
  override def getUser(token: JwtClaim): Task[List[User]] =
    // TODO: token.content must be replaced with actual id
    queries.getUserQuery("dummy_id")

  override def user(claim: JwtClaim): Task[String] = ZIO.succeed(claim.content)

  override def createUser(user: User): Task[List[User]] = queries.createUserQuery(user)
}

object AuthenticationLive {
  val layer: URLayer[Has[Queries], Has[Authentication]] =
    ZLayer.fromService[Queries, Authentication](AuthenticationLive(_))
}

object Authentication {
  def getUser(token: JwtClaim): RIO[Has[Authentication], List[User]] =
    ZIO.service[Authentication].flatMap(_.getUser(token))

  def createUser(user: User): RIO[Has[Authentication], List[User]] =
    ZIO.service[Authentication].flatMap(_.createUser(user))

  def user(claim: JwtClaim): RIO[Has[Authentication], String] = ZIO.serviceWith[Authentication](_.user(claim))
}
