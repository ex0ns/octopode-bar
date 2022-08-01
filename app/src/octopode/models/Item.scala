package octopode
package models

val CAUTION = 2
enum Item(val name: String, val price: BigDecimal):
  case Beer    extends Item("Bière", BigDecimal(4.5 + CAUTION))
  case Soft    extends Item("Soft", BigDecimal(4.3 + CAUTION))
  case Caution extends Item("Retour caution", BigDecimal(-CAUTION))
