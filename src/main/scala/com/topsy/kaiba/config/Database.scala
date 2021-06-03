package com.topsy.kaiba.config

import io.getquill.{ CassandraZioContext, _ }

import com.topsy.kaiba.models.User

object Database {
  object ZioPostgresContext extends CassandraZioContext(Literal)
  import ZioPostgresContext._

  def getUserQuery(id: String): ZioPostgresContext.Quoted[EntityQuery[User]] = quote {
    query[User].filter(user => user.id == id)
  }

  def createUserQuery(user: User): ZioPostgresContext.Quoted[Insert[User]] = quote {
    query[User].insert(user)
  }

}
