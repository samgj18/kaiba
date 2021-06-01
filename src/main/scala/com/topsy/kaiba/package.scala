package com.topsy

import zhttp.http._
import zio._
import zio.blocking.Blocking
import zio.logging.Logging
import zio.logging.slf4j.Slf4jLogger
import com.topsy.kaiba.http.{ AuthenticationService, HealthService }
import com.topsy.kaiba.services.Authentication.AuthenticationEnv
import com.topsy.kaiba.services.Health.HealthEnv
import com.topsy.kaiba.services._

package object kaiba {
  val port: Int = 6025

  val routes: Http[AuthenticationEnv, Throwable, Request, Response[AuthenticationEnv, Throwable]] =
    HealthService.routes +++ AuthenticationService.routes

  type Layer0Env = Logging with Blocking
  type Layer1Env = HealthEnv with AuthenticationEnv
  type AppEnv    = Layer1Env

  object live {
    val layer0: ZLayer[Blocking, Throwable, Layer0Env]  = Blocking.any ++ Slf4jLogger.make((_, msg) => msg)
    val layer1: ZLayer[Layer0Env, Throwable, Layer1Env] = Health.live ++ Authentication.live

    val appLayer: ZLayer[Blocking, Throwable, AppEnv] = layer0 >>> layer1

  }
}
