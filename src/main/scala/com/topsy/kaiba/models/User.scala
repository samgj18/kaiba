package com.topsy.kaiba.models

import zio.json._

final case class User(
    id: String,
    name: String,
    email: String,
  )

object User {
  implicit val encoder: JsonEncoder[User] = DeriveJsonEncoder.gen[User]
  implicit val decoder: JsonDecoder[User] = DeriveJsonDecoder.gen[User]
}
