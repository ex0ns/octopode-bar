import mill._
import mill.scalalib._
import mill.scalalib.scalafmt._
import mill.scalajslib._
import mill.scalajslib.api._
//import $ivy.`com.goyeau::mill-scalafix::0.2.8`
//import com.goyeau.mill.scalafix.ScalafixModule

object Versions {
  val laminar         = "0.14.2"
  val scalafixImports = "0.6.0"
}

object app extends ScalaJSModule with ScalafmtModule /*with ScalafixModule*/ {
  def scalaVersion   = "3.1.3"
  def scalaJSVersion = "1.10.1"
//   def scalafixIvyDeps = Agg(ivy"com.github.liancheng::organize-imports:${Versions.scalafixImports}")
  def scalacOptions = Seq("-deprecation", "-feature")

  def ivyDeps = Agg(
    ivy"com.raquo::laminar::${Versions.laminar}",
    ivy"org.scala-js::scalajs-dom::2.2.0"
  )

  def moduleKind = T(ModuleKind.CommonJSModule)
}
