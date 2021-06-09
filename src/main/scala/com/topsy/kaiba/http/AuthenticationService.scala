package com.topsy.kaiba.http

import pdi.jwt.JwtClaim
import zhttp.http._
import zio.json._
import com.topsy.kaiba.models.User
import com.topsy.kaiba.repositories.Authentication
import com.topsy.kaiba.repositories.Authentication.getUser
import com.topsy.kaiba.utils.jwt.Tokenizer._
import zio._

object AuthenticationService {
  def routes: Http[Has[Authentication], Throwable, Request, Response[Has[Authentication], Throwable]] =
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

  def user(claim: JwtClaim): Http[Has[Authentication], Throwable, Request, UResponse] =
    Http.collectM[Request] {
      case Method.GET -> Root / "user" =>
        getUser(claim).map {
          case one :: Nil => Response.jsonString(one.toJson)
          case _          => Response.HttpResponse(Status.NOT_FOUND, Nil, HttpData.Empty)
        }
    }
}
