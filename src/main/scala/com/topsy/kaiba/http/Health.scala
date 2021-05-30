package com.topsy.kaiba.http

import zhttp.http._
import zio._

final case class Health()
object Health {
  val live: ULayer[Has[Health]] = ZLayer.succeed(Health())
}

private[http] object HealthService {
  val routes: Http[Has[Health], Nothing, Request, UResponse] =
    Http.collect[Request] {
      case Method.GET -> Root / "health" =>
        Response.ok
    }
}
