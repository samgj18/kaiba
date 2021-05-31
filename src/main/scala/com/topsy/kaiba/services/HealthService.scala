package com.topsy.kaiba.services

import zio._

final case class Health()
object Health {
  val live: ULayer[Has[Health]] = ZLayer.succeed(Health())
}

object HealthService {}
