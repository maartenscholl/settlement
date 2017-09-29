package clearing

import com.opengamma.strata.product.Trade

import scala.collection.mutable

/**
  * @author maarten (Maarten P. Scholl)
  * @date 2017-09-18T21:09:00
  * @brief Projects.ChoicePriceMarketMechanism
  * @ingroup Projects
  */
class ChoicePriceMarketMechanism(rate: Double = 1.0) extends MarketMechanism {

  val bids = mutable.Queue[Order]()

  val asks = mutable.Queue[Order]()

  override def processOrder(order: Order): Array[Match] = {


    new Array[Match](0)
  }



}
