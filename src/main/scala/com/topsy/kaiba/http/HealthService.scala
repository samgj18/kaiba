package com.topsy.kaiba.http

import zhttp.http._
import zio._

import com.topsy.kaiba.repositories.Health

object HealthService {
  implicit val canFail: CanFail.type = CanFail
  def routes: Http[Has[Health], Nothing, Request, UResponse] =
    Http.collectM[Request] {
      case Method.GET -> Root / "health" =>
        for {
          health <- Health
                      .ok
                      .map((success: String) => success)
          // .retry(Schedule.recurs(5)) TODO: Tentative, define if needed
        } yield Response.jsonString(health)
    }
}
