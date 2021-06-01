package com.topsy.kaiba

import zhttp.service._
import zio._

object Main extends App {
  override def run(args: List[String]): URIO[zio.ZEnv, ExitCode] =
    Server
      .start(port, routes)
      .provideSomeLayer(live.appLayer)
      .orDie
}
