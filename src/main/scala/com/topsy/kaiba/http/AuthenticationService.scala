package com.topsy.kaiba.http

import com.topsy.kaiba.services.Authentication.{ AuthenticationEnv, getUser }
import zhttp.http._
import com.topsy.kaiba.utils.jwt.Tokenizer.{ jwtDecode, jwtEncode }
import pdi.jwt.JwtClaim

object AuthenticationService {
  def routes: Http[AuthenticationEnv, Throwable, Request, Response[AuthenticationEnv, Throwable]] =
    login +++ authenticate(HttpApp.forbidden("error.forbidden.request"), user)

  def login: UHttpApp = Http.collect[Request] {
    case Method.GET -> Root / "login" / username / password =>
      if (password.reverse == username) Response.text(jwtEncode(username))
      else Response.fromHttpError(HttpError.Unauthorized("error.invalid.credentials"))
  }
  def authenticate[R, E](fail: HttpApp[R, E], success: JwtClaim => HttpApp[R, E]): HttpApp[R, E] =
    Http.flatten {
      Http
        .fromFunction[Request] {
          _.getHeader("X-ACCESS-TOKEN")
            .flatMap(header => jwtDecode(header.value.toString))
            .fold[HttpApp[R, E]](fail)(success)
        }
    }

  def user(claim: JwtClaim): Http[AuthenticationEnv, Throwable, Request, UResponse] = Http.fromEffectFunction[Request] {
    case Method.GET -> Root / "user" =>
      for {
        user <- getUser(claim)
      } yield Response.jsonString(s"Your user is: ${claim.content}")
  }
}
