package octopode
package models

object Returnable:
  val GLASS  = 2
  val BOTTLE = 5

enum Item(val name: String, val price: BigDecimal):
  // Drinks
  case Drink       extends Item("Bière / Panaché / Jus de pomme / Vin", BigDecimal(5 + Returnable.GLASS))
  case BeerPitcher extends Item("Pichet / Bouteille de vin (75cl)", BigDecimal(25 + Returnable.BOTTLE))
  case Soft        extends Item("Soft", BigDecimal(3 + Returnable.GLASS))
  case Sirup       extends Item("Sirop", BigDecimal(1 + Returnable.GLASS))
  // Consumable
  case Glass        extends Item("Gobelet", BigDecimal(Returnable.GLASS))
  case GlassReturn  extends Item("Retour caution (verre)", BigDecimal(-Returnable.GLASS))
  case BottleReturn extends Item("Retour caution (pichet/bouteille)", BigDecimal(-Returnable.BOTTLE))
