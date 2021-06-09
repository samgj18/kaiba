package com.topsy

import zhttp.http._
import zio._
import com.topsy.kaiba.http._
import com.topsy.kaiba.repositories._

package object kaiba {
  val port: Int = 6025

  val routes
      : Http[Has[Authentication] with ZEnv with Has[Health], Throwable, Request, Response[Has[Authentication] with ZEnv, Throwable]] =
    HealthService.routes +++ AuthenticationService.routes

}
