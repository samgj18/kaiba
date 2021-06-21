package com.topsy.kaiba.utils.jwt

import java.time.Clock

import pdi.jwt._

import com.topsy.kaiba.models.User

object Tokenizer {
  val SECRET_KEY: String    = "secretKey"
  implicit val clock: Clock = Clock.systemUTC

  /** Encodes user into a JWT token
    * @param User: User
    * @return
    */
  def jwtEncode(user: User): String = {
    val claim = JwtClaim().issuedNow
      .expiresIn(3600)
      .+("id", user.id)
      .+("name", user.name)
      .+("email", user.email)
    // claim + ("user", user.id)
    Jwt.encode(claim, SECRET_KEY, JwtAlgorithm.HS512)
  }

  /** Encodes JWT token into a user
    * @param token: String
    * @return
    */
  def jwtDecode(token: String): Option[JwtClaim] =
    Jwt.decode(token, SECRET_KEY, Seq(JwtAlgorithm.HS512)).toOption
}
