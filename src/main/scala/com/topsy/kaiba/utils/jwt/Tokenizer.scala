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
    val json  = user.id
    val claim = JwtClaim(json).issuedNow.expiresIn(3600)
    Jwt.encode(claim, SECRET_KEY, JwtAlgorithm.HS512)
  }

  /** Encodes JWT token into a user
    * @param token: String
    * @return
    */
  def jwtDecode(token: String): Option[JwtClaim] =
    Jwt.decode(token, SECRET_KEY, Seq(JwtAlgorithm.HS512)).toOption
}
