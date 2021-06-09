package com.topsy.kaiba.config

import io.getquill._
import zio.blocking.Blocking
import zio.{ Has, ZLayer }

object Database {
  object ZioCassandraContext extends CassandraZioContext(Literal)

  lazy val layer: ZLayer[Blocking, Throwable, Has[CassandraZioSession]] =
    CassandraZioSession.fromPrefix("topsy_auth")
}
