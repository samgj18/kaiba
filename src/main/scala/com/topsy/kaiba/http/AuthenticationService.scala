package com.topsy.kaiba.http

import pdi.jwt.JwtClaim
import zhttp.http._
import zio.json._

import com.topsy.kaiba.models.User
import com.topsy.kaiba.repositories.Authentication._
import com.topsy.kaiba.utils.jwt.Tokenizer._

object AuthenticationService {
  def routes: Http[AuthenticationEnv, Throwable, Request, Response[AuthenticationEnv, Throwable]] =
    login +++ authenticate(HttpApp.forbidden("error.forbidden.request"), user)

  def login: UHttpApp = Http.collect[Request] {
    case Method.GET -> Root / "login" / username / password =>
      if (password.reverse == username) Response.text(jwtEncode(new User("fakeId", username, email = "dummy@dummy.co")))
      else Response.fromHttpError(HttpError.Unauthorized("error.invalid.credentials"))
  }

  def authenticate[R, E](fail: HttpApp[R, E], success: JwtClaim => HttpApp[R, E]): Http[R, E, Request, Response[R, E]] =
    Http.flatten {
      Http
        .fromFunction[Request] {
          _.getHeader("X-ACCESS-TOKEN")
            .flatMap(header => jwtDecode(header.value.toString))
            .fold[HttpApp[R, E]](fail)(success)
        }
    }

  def user(claim: JwtClaim): Http[AuthenticationEnv, Throwable, Request, UResponse] = Http.collectM[Request] {
    case Method.GET -> Root / "user" =>
      for {
        user <- getUser(claim)
      } yield user match {
        case Some(user: User) => Response.jsonString(user.toJsonPretty)
        case _                => Response.text("User not found")
      }
  }
}
