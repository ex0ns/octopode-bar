import mill._
import mill.scalalib._
import mill.scalalib.scalafmt._
import mill.scalajslib._
import mill.scalajslib.api._

import os.Shellable.IterableShellable

import $ivy.`com.lihaoyi::mill-contrib-docker:0.10.5`
import contrib.docker.DockerModule
//import $ivy.`com.goyeau::mill-scalafix::0.2.8`
//import com.goyeau.mill.scalafix.ScalafixModule

object Versions {
  val laminar         = "0.14.2"
  val scalafixImports = "0.6.0"
  val secureRandom    = "1.0.0"
  val scalaTest       = "3.2.13"
  val scalaCheckTest  = "3.2.13.0"
}

object app extends ScalaJSModule with ScalafmtModule with DockerModule /*with ScalafixModule*/ {
  def scalaVersion   = "3.1.3"
  def scalaJSVersion = "1.10.1"
//   def scalafixIvyDeps = Agg(ivy"com.github.liancheng::organize-imports:${Versions.scalafixImports}")
  def scalacOptions = Seq("-deprecation", "-feature")

  def ivyDeps = Agg(
    ivy"com.raquo::laminar::${Versions.laminar}",
    ivy"org.scala-js::scalajs-dom::2.2.0"
  )

  def moduleKind = T(ModuleKind.CommonJSModule)

  def buildWebsite = T {
    val dest = T.dest

    app.fullLinkJS()
    os.proc("yarn", "build").call()
    os.copy(T.workspace / "dist", dest / "dist")
  }

  object test extends Tests with TestModule.ScalaTest {
    def scalacOptions = app.scalacOptions
    def ivyDeps = Agg(
      ivy"org.scalatest::scalatest::${Versions.scalaTest}",
      ivy"org.scalatest::scalatest-flatspec::${Versions.scalaTest}",
      ivy"org.scalatestplus::scalacheck-1-16::${Versions.scalaCheckTest}"
    )
  }

  object docker extends DockerConfig {
    def baseImage     = "alpine:3.16.1"
    def pullBaseImage = true
    def exposedPorts  = Seq(8080)
    def tags          = List("registry.ex0ns.me/ex0ns/octopode-cash/octopode-cash:latest")

    def run = Seq(
      "apk add thttpd",
      "adduser -D static"
    )

    def user = "static"

    // Shamelessly taken from https://github.com/com-lihaoyi/mill/blob/main/contrib/docker/src/DockerModule.scala
    override def dockerfile: T[String] = T {
      val jarName = assembly().path.last
      val labelRhs = labels().map { case (k, v) =>
        val lineBrokenValue = v
          .replace("\r\n", "\\\r\n")
          .replace("\n", "\\\n")
          .replace("\r", "\\\r")
        s""""$k"="$lineBrokenValue""""
      }
        .mkString(" ")

      val lines = List(
        if (labels().isEmpty) "" else s"LABEL $labelRhs",
        if (exposedPorts().isEmpty) ""
        else
          exposedPorts()
            .map(port => s"$port/tcp")
            .mkString("EXPOSE ", " ", ""),
        envVars().map { case (env, value) =>
          s"ENV $env=$value"
        }.mkString("\n"),
        run().map(c => s"RUN $c").mkString("\n"),
        if (user().isEmpty) "" else s"USER ${user()}"
      ).filter(_.nonEmpty).mkString(sys.props("line.separator"))

      s"""
         |FROM ${baseImage()}
         |$lines
         |WORKDIR /home/static
         |COPY ./dist/ /home/static
         |CMD ["thttpd", "-D", "-h", "0.0.0.0", "-p", "8080", "-d", "/home/static", "-u", "static", "-l", "-", "-M", "60"]""".stripMargin
    }

    private def baseImageCacheBuster: T[(Boolean, Double)] = T.input {
      val pull = pullBaseImage()
      if (pull) (pull, Math.random()) else (pull, 0d)
    }

    final def buildCustom = T {
      val dest = T.dest

      app.fullLinkJS()
      os.proc("yarn", "build").call()
      os.copy(T.workspace / "dist", dest / "dist")
      /*
      :>From the section PERMISSIONS in thttpd(8):
       In summary, data files should  be  mode  644  (rw-r--r--),
       directories should be 755 (rwxr-xr-x) if you want to allow
       indexing and 711 (rwx--x--x) to disallow it, and CGI  pro­
       grams should be mode 755 (rwxr-xr-x) or 711 (rwx--x--x).
       */
      os.proc("find", dest, "-type", "d", "-exec", "chmod", "711", "{}", "+").call()
      os.proc("find", dest, "-type", "f", "-exec", "chmod", "644", "{}", "+").call()

      os.write(dest / "Dockerfile", dockerfile())

      val log = T.log

      val tagArgs = tags().flatMap(t => List("-t", t))

      val (pull, _)      = baseImageCacheBuster()
      val pullLatestBase = IterableShellable(if (pull) Some("--pull") else None)

      val result = os
        .proc(executable(), "build", tagArgs, pullLatestBase, dest)
        .call(stdout = os.Inherit, stderr = os.Inherit)

      log.info(s"Docker build completed ${
          if (result.exitCode == 0) "successfully"
          else "unsuccessfully"
        } with ${result.exitCode}")
      tags()
    }

    final def pushCustom() = T.command {
      val tags = buildCustom()
      tags.foreach(t => os.proc(executable(), "push", t).call(stdout = os.Inherit, stderr = os.Inherit))
    }
  }
}
