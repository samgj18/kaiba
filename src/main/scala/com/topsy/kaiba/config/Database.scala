package com.topsy.kaiba.config

import io.getquill._
import zio._
import zio.blocking.Blocking

object Database {
  object ZioCassandraContext extends CassandraZioContext(Literal)

  lazy val layer: RLayer[Blocking, Has[CassandraZioSession]] =
    CassandraZioSession.fromPrefix("topsy_auth")
}
