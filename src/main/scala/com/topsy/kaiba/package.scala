package com.topsy

import zhttp.http._
import zio._
import zio.blocking.Blocking
import zio.clock.Clock
import zio.logging.Logging
import zio.logging.slf4j.Slf4jLogger

import com.topsy.kaiba.http._
import com.topsy.kaiba.repositories.Authentication.AuthenticationEnv
import com.topsy.kaiba.repositories.Health.HealthEnv
import com.topsy.kaiba.repositories._

package object kaiba {
  val port: Int = 6025

  val routes
      : Http[AuthenticationEnv with HealthEnv with Clock, Throwable, Request, Response[AuthenticationEnv, Throwable]] =
    HealthService.routes +++ AuthenticationService.routes

  type Layer0Env = Logging with Blocking
  type Layer1Env = HealthEnv with AuthenticationEnv with Clock
  type AppEnv    = Layer1Env

  object live {
    val layer0: ZLayer[Blocking, Throwable, Layer0Env]  = Slf4jLogger.make((_, msg) => msg) ++ Blocking.any
    val layer1: ZLayer[Layer0Env, Throwable, Layer1Env] = Health.live ++ Authentication.live ++ Clock.live

    val appLayer: ZLayer[Blocking, Throwable, AppEnv] = layer0 >>> layer1

  }
}
