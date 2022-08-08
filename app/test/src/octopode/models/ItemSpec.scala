package octopode
package views

import org.scalatest.flatspec.AsyncFlatSpec
import org.scalatest.matchers.should.Matchers
import octopode.models.{ Bill, Item, Order, Returnable }

class ItemSpec extends AsyncFlatSpec with Matchers {

  it should "A beer should include the price of a returnable glass" in {
    Item.Beer.price - Returnable.GLASS shouldBe BigDecimal(5)
  }
  it should "A pitcher should include the price of a returnable bottle" in {
    Item.BeerPitcher.price - Returnable.BOTTLE shouldBe BigDecimal(25)
  }

  it should "A bottle of wine should include the price of a returnable bottle" in {
    Item.WineBottle.price - Returnable.BOTTLE shouldBe BigDecimal(25)
  }

}
