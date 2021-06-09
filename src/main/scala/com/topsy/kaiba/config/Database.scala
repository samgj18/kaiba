package com.topsy.kaiba.config

import io.getquill._
import zio.blocking.Blocking
import zio.{ Has, RLayer }

object Database {
  object ZioCassandraContext extends CassandraZioContext(Literal)

  lazy val layer: RLayer[Blocking, Has[CassandraZioSession]] =
    CassandraZioSession.fromPrefix("topsy_auth")
}
