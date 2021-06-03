package com.topsy.kaiba.repositories

import zio._

trait Health {
  def ok: ZIO[Has[Health], Nothing, String]
}

final case class HealthLive() extends Health {
  override def ok: UIO[String] = ZIO.effectTotal("Server is up")
}

object HealthLive {
  val layer: ULayer[Has[Health]] = {
    ZLayer.succeed(HealthLive())
  }
}

object Health {
  def ok: ZIO[Has[Health], Nothing, String] = ZIO.serviceWith[Health](_.ok)
}
