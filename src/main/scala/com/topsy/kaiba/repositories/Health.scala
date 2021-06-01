package com.topsy.kaiba.repositories

import zio._

object Health {
  // type alias, to use for other layers
  type HealthEnv = Has[Health.Service]

  // service definition
  trait Service {
    def ok: Task[String]
  }

  // layer - service implementation
  val live: ZLayer[Any, Throwable, HealthEnv] = ZLayer.succeed {
    new Service {
      override def ok: Task[String] = Task {
        // TODO: Replace with email notification later
        "Server is up"
      }
    }
  }

  // accessor
  def ok: ZIO[HealthEnv, Throwable, String] = ZIO.accessM(_.get.ok)
}
