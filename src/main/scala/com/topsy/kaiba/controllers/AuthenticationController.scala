package com.topsy.kaiba.controllers

import com.topsy.kaiba.services.Authentication
import com.topsy.kaiba.services.AuthenticationService.{ authenticate, login, user }
import zhttp.http.{ Http, HttpApp, Request, UResponse }
import zio.Has

object AuthenticationController {
  val authenticationRoute: Http[Has[Authentication], Nothing, Request, UResponse] =
    login +++ authenticate(HttpApp.forbidden("error.forbidden.request"), user)
}
