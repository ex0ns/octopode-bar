package octopode

import com.raquo.laminar.api.L.{ *, given }

import views._
import models._
import org.scalajs.dom

object Main:
  def main(args: Array[String]): Unit =
    renderOnDomContentLoaded(dom.document.querySelector("#app"), Main.appElement())

  private val currentOrder = Var.apply(Order.empty)
  private def resetOrder() = currentOrder.set(Order.empty)

  private val appState = Var(ViewState.Menu)

  def appElement(): HtmlElement =
    div(
      cls := "h-screen flex flex-col bg-neutral-100",
      div(
        cls := "flex items-center justify-center self-center mb-8",
        img(
          cls := "w-32",
          src := "octo.png"
        )
      ),
      child <-- appState.signal.map {
        case ViewState.Menu =>
          resetOrder()
          new MenuView(currentOrder, appState).render()
        case ViewState.Cashback =>
          new CashbackView(currentOrder.now(), appState).render()
      }
    )

end Main
