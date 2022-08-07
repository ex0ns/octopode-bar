package octopode
package models

val CAUTION = 2

enum Item(val name: String, val price: BigDecimal):
  case Beer            extends Item("Bière", BigDecimal(5 + CAUTION))
  case Soft            extends Item("Soft", BigDecimal(3 + CAUTION))
  case AppleJuice      extends Item("Jus de pomme", BigDecimal(4 + CAUTION))
  case Sirup           extends Item("Sirop", BigDecimal(1 + CAUTION))
  case WineGlass       extends Item("Verre de vin", BigDecimal(4 + CAUTION))
  case SmallWineBottle extends Item("Vin (50cl)", BigDecimal(12 + CAUTION))
  case WineBottle      extends Item("Vin (75cl)", BigDecimal(16 + CAUTION))
  case Glass           extends Item("Gobelet", BigDecimal(CAUTION))
  case Caution         extends Item("Retour caution", BigDecimal(-CAUTION))
