package octopode
package models

final case class Order(items: List[Item]) {
  val rawTotal: BigDecimal = items.map(_.price).sum
  val total: Double        = rawTotal.toDouble
  val isEmpty: Boolean     = items.isEmpty

  def add(item: Item): Order    = copy(items = item :: items)
  def remove(item: Item): Order = copy(items = items diff List(item))

  val grouped = items.groupBy(_.name)
}

object Order {
  def empty = Order(List.empty)
}
