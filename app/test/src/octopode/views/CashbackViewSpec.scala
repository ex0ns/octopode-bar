package octopode
package views

import org.scalatest.flatspec.AsyncFlatSpec
import org.scalatest.matchers.should.Matchers
import octopode.models.{ Bill, Item, Order }
import com.raquo.airstream.state.Var
import com.raquo.airstream.ownership.{ Owner, Subscription }
import org.scalatestplus.scalacheck.ScalaCheckDrivenPropertyChecks
import org.scalacheck.Gen
import octopode.models.Cashback

class CashbackViewSpec extends AsyncFlatSpec with Matchers with ScalaCheckDrivenPropertyChecks {
  implicit private def testableOwner: Owner = new Owner {
    def _testSubscriptions: List[Subscription] = subscriptions.toList
    override def killSubscriptions(): Unit =
      super.killSubscriptions()
  }

  private val itemGen     = Gen.someOf(Item.values.toSeq)
  private val billGen     = Gen.someOf(Bill.values.toSeq)
  private val cashbackGen = billGen.map(bills => Cashback.apply(bills.toList))

  it should "Expect the price of the order" in {
    val view = new CashbackView(Order(List(Item.Drink)), Var(ViewState.Cashback))
    view.ows.observe(testableOwner).now() shouldBe -Item.Drink.price.toDouble
  }

  it should "Reduce the price based on the selection" in {
    val view = new CashbackView(Order(List(Item.Drink)), Var(ViewState.Cashback))
    view.selected.update(_.add(Bill.Bill1))
    view.ows.observe(testableOwner).now() shouldBe (-Item.Drink.price + Bill.Bill1.value).toDouble
  }

  it should "be able to combine items and bills" in {
    forAll(itemGen, cashbackGen) { (items, cashback) =>
      val view = new CashbackView(Order(items.toList), Var(ViewState.Cashback))
      view.selected.set(cashback)

      view.ows.observe(testableOwner).now() shouldBe (-items.map(_.price).sum + cashback.rawTotal).toDouble
    }
  }
}
