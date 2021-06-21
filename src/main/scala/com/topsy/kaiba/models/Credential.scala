package com.topsy.kaiba.models

import zio.json._

case class Credential(
    username: String,
    password: Option[String],
    email: String,
  )

object Credential {
  implicit val encoder: JsonEncoder[Credential] = DeriveJsonEncoder.gen[Credential]
  implicit val decoder: JsonDecoder[Credential] = DeriveJsonDecoder.gen[Credential]
}
