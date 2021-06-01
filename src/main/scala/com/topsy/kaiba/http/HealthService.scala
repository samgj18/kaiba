package com.topsy.kaiba.http

import zhttp.http._
import zio._
import zio.logging._

import com.topsy.kaiba.services.Health

object HealthService {
  implicit val canFail: CanFail.type = CanFail
  def routes: Http[Any, Nothing, Request, UResponse] =
    Http.collect[Request] {
      case Method.GET -> Root / "health" =>
        Health
          .ok
          .fold(
            (failure: Throwable) => ZIO.succeed(log.error(s"Server down up due to: ${failure.getMessage}")),
            (success: String) => ZIO.fail(log.info(s"Server is up $success")),
          )
          .retry(Schedule.recurs(5))
        Response.jsonString("Server is up")
    }
}
