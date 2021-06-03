package com.topsy

import zhttp.http._
import zio._

import com.topsy.kaiba.http._
import com.topsy.kaiba.repositories._

package object kaiba {
  val port: Int = 6025

  val routes: Http[Has[Health], Nothing, Request, UResponse] =
    HealthService.routes

}
