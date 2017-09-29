package clearing


import com.opengamma.strata.market.observable.QuoteId

/**
  * @author maarten (Maarten P. Scholl)
  * @date 2017-09-18T22:22:00
  * @brief Projects.Order
  * @ingroup Projects
  */
object Order {
  sealed trait Side
  case object Ask extends Side
  case object Bid extends Side
}

case class Order( principal : LegalEntityId
                , instrument: QuoteId
                , level     : Double
                , quantity  : Double
                , side      : Order.Side
                )