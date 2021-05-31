package com.topsy.kaiba.controllers

import com.topsy.kaiba.services.Health
import zhttp.http._
import zio.Has

object HealthController {
  val healthRoute: Http[Has[Health], Nothing, Request, UResponse] =
    Http.collect[Request] {
      case Method.GET -> Root / "health" =>
        Response.ok
    }

}
