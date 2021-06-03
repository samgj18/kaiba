package com.topsy.kaiba.repositories

import zio._

trait Health {
  def ok: UIO[String]
}

object Health {
  def ok: ZIO[Has[Health], Nothing, String] = ZIO.serviceWith[Health](_.ok)
}

object HealthLive {
  val layer: ULayer[Has[Health]] = ZLayer.succeed {
    new Health {
      override def ok: UIO[String] =
        // TODO: Replace with email notification later
        ZIO.effectTotal("Server is up")
    }
  }
}
