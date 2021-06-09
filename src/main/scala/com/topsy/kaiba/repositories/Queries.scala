package com.topsy.kaiba.repositories

import com.topsy.kaiba.config.Database
import com.topsy.kaiba.config.Database.ZioCassandraContext.{ query, quote, _ }
import com.topsy.kaiba.models.User
import zio._

trait Queries {
  def getUserQuery(id: String): RIO[ZEnv, List[User]]
  def createUserQuery(user: User): RIO[ZEnv, List[User]]
}

final case class QueriesLive() extends Queries {
  override def getUserQuery(id: String): RIO[ZEnv, List[User]] = run {
    quote {
      query[User].filter(user => user.id == lift(id))
    }
  }.provideCustomLayer(Database.layer)

  override def createUserQuery(user: User): RIO[ZEnv, List[User]] = run {
    query[User].insert(lift(user))
    query[User].filter(_.id == lift(user.id))
  }.provideCustomLayer(Database.layer)
}

object QueriesLive {
  val layer: ULayer[Has[Queries]] =
    ZLayer.succeed(QueriesLive())
}

object Queries {
  def getUserQuery(id: String): ZIO[ZEnv with Has[Queries], Throwable, List[User]] =
    ZIO.service[Queries].flatMap(_.getUserQuery(id))

  def createUserQuery(user: User): ZIO[ZEnv with Has[Queries], Throwable, List[User]] =
    ZIO.service[Queries].flatMap(_.createUserQuery(user))
}
