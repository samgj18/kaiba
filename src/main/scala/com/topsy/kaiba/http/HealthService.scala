package com.topsy.kaiba.http

import zhttp.http._
import zio._
import zio.clock.Clock

import com.topsy.kaiba.repositories.Health
import com.topsy.kaiba.repositories.Health.HealthEnv

object HealthService {
  implicit val canFail: CanFail.type = CanFail
  def routes: Http[HealthEnv with Clock, Nothing, Request, UResponse] =
    Http.collectM[Request] {
      case Method.GET -> Root / "health" =>
        for {
          health <- Health
                      .ok
                      .fold(
                        (failure: Throwable) => s"Server down due to: ${failure.getMessage}",
                        (success: String) => success,
                      )
          // .retry(Schedule.recurs(5)) TODO: Tentative, define if needed
        } yield Response.jsonString(health)
    }
}
