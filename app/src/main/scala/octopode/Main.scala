package octopode

import scala.scalajs.js
import com.raquo.laminar.api.L.{ *, given }

import org.scalajs.dom

@main
def main(): Unit =
  renderOnDomContentLoaded(dom.document.querySelector("#app"), Main.appElement())

object Main:
  def appElement(): HtmlElement =
    div(
      h1(cls := "text-4xl", "Hello Vite!")
    )

end Main
