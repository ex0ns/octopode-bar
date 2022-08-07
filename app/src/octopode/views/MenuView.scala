package octopode
package views

import models._
import com.raquo.laminar.api.L.{ *, given }

class MenuView(order: Var[Order], appState: Var[ViewState]) {

  def render() =
    div(
      cls := "h-full flex flex-col",
      div(
        cls := "grid gap-4 grid-cols-1 auto-rows-max",
        children <-- Signal.fromValue(Item.values.toSeq.map(renderItem))
      ),
      payButton(),
      debug()
    )

  private val buttonStyle =
    cls := "bg-purple-light active:bg-purple-dark hover:bg-purple-dark text-white h-full w-20 rounded-l cursor-pointer outline-none py-4"

  private def renderItem(item: Item): HtmlElement = div(
    cls := "py-2",
    span(cls := "text-xl font-bold", item.name),
    span(cls := "ml-1 text-sm", s" ${item.price}CHF"),
    div(
      cls := "flex my-2",
      button(
        buttonStyle,
        onClick --> (_ => order.update(_.remove(item))),
        span(
          "-",
          cls := "m-auto text-2xl font-thin"
        )
      ),
      input(
        typ      := "text",
        readOnly := true,
        cls      := "bg-neutral-100 appearance-none outline-none border-none focus:ring-0 focus:outline-none text-center w-full font-semibold text-md hover:text-black focus:text-black md:text-basecursor-default m-0 p-0",
        value <-- order.signal.map(_.grouped.get(item.name).map(_.size).getOrElse(0).toString())
      ),
      button(
        buttonStyle,
        onClick --> (_ => order.update(_.add(item))),
        span(
          "+",
          cls := "m-auto text-2xl font-thin"
        )
      )
    )
  )

  private def payButton() =
    div(
      cls := "self-end w-full mt-auto mb-2",
      button(
        tpe := "submit",
        cls := "group relative w-full py-2 px-4 border border-transparent text-sm font-medium rounded-md text-white bg-purple-light active:bg-purple-dark hover:bg-purple-dark focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-indigo-500",
        span(
          cls := "h-auto absolute left-0 inset-y-0 flex items-center pl-3",
          svg.svg(
            svg.cls     := "h-5 w-5 text-purple-dark group-hover:text-purple-light",
            svg.xmlns   := "http://www.w3.org/2000/svg",
            svg.viewBox := "0 0 20 20",
            svg.fill    := "currentColor",
            svg.path(
              svg.d := "M17 9V7a2 2 0 00-2-2H5a2 2 0 00-2 2v6a2 2 0 002 2h2m2 4h10a2 2 0 002-2v-6a2 2 0 00-2-2H9a2 2 0 00-2 2v6a2 2 0 002 2zm7-5a2 2 0 11-4 0 2 2 0 014 0z"
            )
          )
        ),
        disabled <-- order.signal.map(_.isEmpty),
        child <-- order.signal.map(order => s"Payer ${order.total} CHF"),
        onClick --> (_ => appState.update(_.next()))
      )
    )

  private def debug() = ul(children <-- order.signal.map(_.grouped.map { case (name, items) =>
    val sum = items.map(_.price).sum
    li(s"$name x ${items.size} = $sum")
  }.toSeq))

}
