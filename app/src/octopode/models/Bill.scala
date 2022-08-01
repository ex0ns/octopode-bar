package octopode
package models

enum Bill(val value: BigDecimal):
  // coins cents
  case Bill5C  extends Bill(BigDecimal(0.05))
  case Bill10C extends Bill(BigDecimal(0.10))
  case Bill20C extends Bill(BigDecimal(0.20))
  case Bill50C extends Bill(BigDecimal(0.50))

  // coins
  case Bill1 extends Bill(BigDecimal(1))
  case Bill2 extends Bill(BigDecimal(2))
  case Bill5 extends Bill(BigDecimal(5))

  // bills
  case Bill10  extends Bill(BigDecimal(10))
  case Bill20  extends Bill(BigDecimal(20))
  case Bill50  extends Bill(BigDecimal(50))
  case Bill100 extends Bill(BigDecimal(100))
  case Bill200 extends Bill(BigDecimal(200))
