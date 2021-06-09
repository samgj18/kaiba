package com.topsy.kaiba.repositories

import com.topsy.kaiba.config.Database.ZioCassandraContext.{ query, quote, _ }
import com.topsy.kaiba.models.User
import io.getquill.CassandraZioContext._
import io.getquill.CassandraZioSession
import zio._
import zio.blocking.Blocking

trait Queries {
  def getUserQuery(id: String): Task[List[User]]
  def createUserQuery(user: User): Task[List[User]]
}

final case class QueriesLive(session: CassandraZioSession, blocking: Blocking.Service) extends Queries {
  override def getUserQuery(id: String): Task[List[User]] = run {
    quote {
      query[User].filter(user => user.id == lift(id))
    }
  }.provideSession(session).provide(Has(blocking))

  override def createUserQuery(user: User): Task[List[User]] = {
    val insert: CIO[Unit]     = run(query[User].insert(lift(user)))
    val find: CIO[List[User]] = run(query[User].filter(_.id == lift(user.id)))

    (insert *> find).provideSession(session).provide(Has(blocking))
  }
}

object QueriesLive {
  val layer: URLayer[Has[CassandraZioSession] with Has[Blocking.Service], Has[Queries]] =
    ZLayer.fromServices[CassandraZioSession, Blocking.Service, Queries](
      (session: CassandraZioSession, blocking: Blocking.Service) => QueriesLive(session, blocking)
    )
}

object Queries {
  def getUserQuery(id: String): ZIO[Has[Queries], Throwable, List[User]] =
    ZIO.serviceWith[Queries](_.getUserQuery(id))

  def createUserQuery(user: User): ZIO[Has[Queries], Throwable, List[User]] =
    ZIO.serviceWith[Queries](_.createUserQuery(user))
}
