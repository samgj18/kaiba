package com.topsy.kaiba

import zhttp.http._
import zio.Has

package object http {
  val routes: Http[Has[Authentication] with Has[Health], Nothing, Request, UResponse] =
    HealthService.routes +++ AuthenticationService.routes
}
