package octopode
package views

import models._
import com.raquo.laminar.api.L.{ *, given }

class CashbackView(order: Order, appState: Var[ViewState]) {
  private[views] val selected = Var[Cashback](Cashback.empty)
  private[views] val ows      = selected.signal.map(_.rawTotal).map(_ - order.rawTotal).map(_.toDouble)

  def render() =
    div(
      cls := "flex flex-col h-full",
      div(
        div(s"Prix: ${order.total.toString()}"),
        child <-- ows.map {
          case ows if ows > 0  => s"Tu dois ${ows} CHF"
          case ows if ows < 0  => s"On te doit ${-ows} CHF"
          case ows if ows == 0 => "Le compte est bon"
        }
      ),
      div(
        cls := "grid grid-cols-4 gap-4",
        children <--
          Signal.fromValue(Bill.values.toSeq.map(renderBill))
      ),
      currentCustomerBills(),
      confirm()
    )

  private val btnCommonStyle =
    cls := "border border-transparent rounded-md py-3 px-8 flex items-center justify-center text-base font-medium text-white hover:bg-purple-dark focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-indigo-500"

  private def customerBills(bills: Seq[Bill]) =
    li(
      button(
        cls := "my-1 w-full bg-purple-light",
        btnCommonStyle,
        onClick --> (_ => selected.update(_.remove(bills.head))),
        s"${bills.head.value} (${bills.length})"
      )
    )

  private def currentCustomerBills() = div(
    ul(
      cls := "w-full flex flex-col",
      children <-- selected.signal.map(_.grouped.map(_._2).toList.sortBy(_.head.value).map(customerBills))
    )
  )

  private def confirm() = button(
    cls := "self-end w-full mt-auto mb-2 bg-purple-light",
    btnCommonStyle,
    onClick --> (_ => appState.update(_.next())),
    "Ok"
  )

  private def renderBill(bill: Bill) =
    div(
      label(
        cls := "group relative border rounded-md py-3 px-4 flex items-center justify-center text-sm font-medium uppercase hover:bg-gray-50 focus:outline-none sm:flex-1 bg-white shadow-sm text-gray-900 cursor-pointer",
        input(
          tpe := "radio",
          onClick --> (_ => selected.update(_.add(bill))),
          name                        := "size-choice",
          cls                         := "sr-only",
          dataAttr("aria-labelledby") := "size-choice-0-label"
        ),
        span(idAttr := "size-choice-0-label", bill.value.toString()),
        span(cls    := "absolute -inset-px rounded-md pointer-events-none", dataAttr("aria-hidden") := "true")
      )
    )
}
