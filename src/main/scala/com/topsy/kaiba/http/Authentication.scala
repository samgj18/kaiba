package com.topsy.kaiba.http

import pdi.jwt.JwtClaim
import zhttp.http.{ Method, _ }
import zio._

import com.topsy.kaiba.jwt.Tokenizer._

final case class Authentication()
object Authentication {
  val live: ULayer[Has[Authentication]] = ZLayer.succeed(Authentication())
}

private[http] object AuthenticationService {
  val routes: Http[Has[Authentication], Nothing, Request, UResponse] =
    login +++ authenticate(HttpApp.forbidden("Not allowed!"), user)

  def authenticate[R, E](fail: HttpApp[R, E], success: JwtClaim => HttpApp[R, E]): HttpApp[R, E] = Http.flatten {
    Http
      .fromFunction[Request] {
        _.getHeader("X-ACCESS-TOKEN")
          .flatMap(header => jwtDecode(header.value.toString))
          .fold[HttpApp[R, E]](fail)(success)
      }
  }

  // Testing only: Login is successful only if the password is the reverse of the username
  def login: UHttpApp = Http.collect[Request] {
    case Method.GET -> Root / "login" / username / password =>
      if (password.reverse == username) Response.text(jwtEncode(username))
      else Response.fromHttpError(HttpError.Unauthorized("Invalid username of password\n"))
  }

  def user(claim: JwtClaim): Http[Any, Nothing, Request, UResponse] = Http.collect[Request] {
    case Method.GET -> Root / "user"                  => Response.text(s"Welcome to the ZIO party! ${claim.content}")
    case Method.GET -> Root / "user" / name / "greet" => Response.text(s"Welcome to the ZIO party! $name")
    case Method.GET -> Root / "user" / "expiration"   => Response.text(s"Expires in: ${claim.expiration.getOrElse(-1L)}")
  }
}
