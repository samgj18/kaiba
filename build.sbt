import Dependencies._

ThisBuild / organization := "com.topsy"
ThisBuild / scalaVersion := "2.13.6"

lazy val `kaiba` =
  project
    .in(file("."))
    .settings(name := "kaiba")
    .settings(commonSettings)
    .settings(dependencies)

lazy val commonSettings = Seq(
  addCompilerPlugin(com.olegpy.`better-monadic-for`),
  addCompilerPlugin(org.augustjune.`context-applied`),
  addCompilerPlugin(org.typelevel.`kind-projector`),
  update / evictionWarningOptions := EvictionWarningOptions.empty,
  Compile / console / scalacOptions := {
    (Compile / console / scalacOptions)
      .value
      .filterNot(_.contains("wartremover"))
      .filterNot(Scalac.Lint.toSet)
      .filterNot(Scalac.FatalWarnings.toSet) :+ "-Wconf:any:silent"
  },
  Test / console / scalacOptions :=
    (Compile / console / scalacOptions).value,
)

lazy val dependencies = Seq(
  libraryDependencies ++= Seq(
    "dev.zio"                 %% "zio"                 % "1.0.5",
    "dev.zio"                 %% "zio-json"            % "0.1.2",
    "dev.zio"                 %% "zio-logging-slf4j"   % "0.5.8",
    "dev.zio"                 %% "zio-logging"         % "0.5.8",
    "dev.zio"                 %% "zio-macros"          % "1.0.5",
    "io.d11"                  %% "zhttp"               % "1.0.0.0-RC16",
    "io.getquill"             %% "quill-cassandra-zio" % "3.7.0",
    "io.github.kitlangton"    %% "zio-magic"           % "0.3.2",
    "com.github.jwt-scala"    %% "jwt-json4s-native"   % "7.1.5",
    "org.apache.logging.log4j" % "log4j-slf4j-impl"    % "2.14.1",
  ),
  libraryDependencies ++= Seq(
    com.github.alexarchambault.`scalacheck-shapeless_1.15`,
    org.scalacheck.scalacheck,
    org.scalatest.scalatest,
    org.scalatestplus.`scalacheck-1-15`,
    org.typelevel.`discipline-scalatest`,
  ).map(_ % Test),
)
