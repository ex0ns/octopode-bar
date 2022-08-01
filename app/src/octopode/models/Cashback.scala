package octopode
package models

final case class Cashback(bills: List[Bill]) {
  val rawTotal: BigDecimal = bills.map(_.value).sum
  val total: Double        = rawTotal.toDouble

  def add(bill: Bill): Cashback    = copy(bills = bill :: bills)
  def remove(bill: Bill): Cashback = copy(bills = bills diff List(bill))

  // is it possible in scala 3 to group by the type of the enum ?
  val grouped = bills.groupBy(_.value)
}

object Cashback {
  def empty = Cashback(List.empty)
}
