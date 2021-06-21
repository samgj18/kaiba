package com.topsy.kaiba.http

import pdi.jwt.JwtClaim
import zhttp.http._
import zio._
import zio.json._
import com.topsy.kaiba.models.{ Credential, User }
import com.topsy.kaiba.repositories.Authentication
import com.topsy.kaiba.repositories.Authentication.getUser
import com.topsy.kaiba.utils.jwt.Tokenizer._

object AuthenticationService {
  def routes: Http[Has[Authentication], Throwable, Request, Response[Has[Authentication], Throwable]] =
    login +++ authenticate(HttpApp.forbidden("error.forbidden.request"), user)

  def login: UHttpApp = Http.collect[Request] {
    case self @ Method.POST -> Root / "login" =>
      self.getBodyAsString match {
        case Some(credentials) =>
          credentials.fromJson[Credential] match {
            case Right(credentials) =>
              Response.text(jwtEncode(new User("fakeId", credentials.username, email = credentials.email)))
            case Left(error) => Response.fromHttpError(HttpError.Unauthorized(error))
          }

        case _ => Response.fromHttpError(HttpError.Unauthorized("error.invalid.credentials"))
      }
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
          case user :: Nil => Response.jsonString(user.toJson)
          case _           => Response.fromHttpError(HttpError.Unauthorized("error.user.not.found"))
        }
    }
}
