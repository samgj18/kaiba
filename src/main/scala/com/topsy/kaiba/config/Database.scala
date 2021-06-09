package com.topsy.kaiba.config

import io.getquill.{ CassandraZioContext, _ }
import zio.{ Has, ZLayer }
import zio.blocking.Blocking

object Database {
  object ZioCassandraContext extends CassandraZioContext(Literal)

  lazy val layer: ZLayer[Blocking, Throwable, Has[CassandraZioSession] with Has[Blocking.Service]] =
    CassandraZioSession.fromPrefix("topsy_auth")
}
