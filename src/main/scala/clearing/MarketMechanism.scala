package clearing

import com.opengamma.strata.product.Trade

/**
  * @author maarten (Maarten P. Scholl)
  * @date 2017-09-18T16:41:00
  * @brief Projects.MarketMechanism
  * @ingroup Projects
  */
trait MarketMechanism { // aka matching engine

  def processOrder(order: Order) : Array[Match]

}
