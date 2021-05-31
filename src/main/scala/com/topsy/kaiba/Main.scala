package com.topsy.kaiba

import zhttp.service._
import zio._
import zio.console._
import com.topsy.kaiba.services.{ Authentication, Health }

object Main extends App {

  /** Custom layers are optional and are here just as an example. Has[_] in the Z[R, E, A] is
    * replaceable with the …Any… type and we wouldn't need to provide any custom layers.
    */

  override def run(args: List[String]): URIO[zio.ZEnv, ExitCode] =
    Server
      .start(port, routes.silent)
      .foldM(
        failure = throwable => putStrLn(throwable.getMessage).exitCode,
        success = _ => ZIO.succeed(ExitCode.success),
      )
      .provideCustomLayer(customLayers)

}
