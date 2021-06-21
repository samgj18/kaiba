package com.topsy.kaiba

import zhttp.service._
import zio._
import zio.magic._

import com.topsy.kaiba.config.Database
import com.topsy.kaiba.repositories._

object Main extends App {
  val deps: URLayer[ZEnv, Has[Health] with Has[Authentication]] =
    ZLayer
      .fromSomeMagic[ZEnv, Has[Health] with Has[Authentication]](
        HealthLive.layer,
        AuthenticationLive.layer,
        QueriesLive.layer,
        Database.layer,
      )
      .orDie

  override def run(args: List[String]): URIO[ZEnv, ExitCode] =
    Server
      .start(port, routes)
      .exitCode
      .provideCustomLayer(deps)
}
