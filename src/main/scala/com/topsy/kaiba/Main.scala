package com.topsy.kaiba

import com.topsy.kaiba.repositories.{ AuthenticationLive, HealthLive }
import zhttp.service._
import zio._
import zio.magic._

object Main extends App {
  override def run(args: List[String]): URIO[ZEnv, ExitCode] =
    Server
      .start(port, routes)
      .inject(HealthLive.layer, ZEnv.live, AuthenticationLive.layer)
      .exitCode
}
