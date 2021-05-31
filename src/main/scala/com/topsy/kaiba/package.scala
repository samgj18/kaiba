package com.topsy

import com.topsy.kaiba.controllers.AuthenticationController.authenticationRoute
import com.topsy.kaiba.controllers.HealthController.healthRoute
import com.topsy.kaiba.services.{ Authentication, Health }
import zhttp.http._
import zio.{ Has, ZLayer }

package object kaiba {
  val port = 8080
  val routes: Http[Has[Authentication] with Has[Health], Nothing, Request, UResponse] =
    authenticationRoute +++ healthRoute

  val customLayers: ZLayer[Any, Nothing, Has[Health] with Has[Authentication]] = Health.live ++ Authentication.live
}
