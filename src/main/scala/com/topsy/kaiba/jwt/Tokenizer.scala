package com.topsy.kaiba.jwt

import java.time.Clock

import pdi.jwt._

object Tokenizer {
  val SECRET_KEY: String    = "secretKey"
  implicit val clock: Clock = Clock.systemUTC

  /** Encodes user into a JWT token
    * @param username: String
    * @return
    */
  def jwtEncode(username: String): String = {
    val json  = s"""{"user": "$username"}"""
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
