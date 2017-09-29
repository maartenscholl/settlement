package clearing

import java.time.LocalDate
import java.time.temporal.Temporal

import com.opengamma.strata.basics.StandardId
import com.opengamma.strata.data.ImmutableMarketData
import com.opengamma.strata.market.observable.QuoteId
import com.opengamma.strata.product.TradeInfo

/**
  * @author maarten (Maarten P. Scholl)
  * @date 2017-09-06T21:33:00
  * @brief Projects.Messages
  * @ingroup Projects
  */

object Messages {
  class SystemMessage

  case class Participant_New    (participant: LegalEntityId) extends SystemMessage
  case class Participant_Delete (participant: LegalEntityId) extends SystemMessage
  case class Participant_Done   (participant: LegalEntityId) extends SystemMessage

  //////////////////////////////////////////////////

  class MarketMessage

  case class MarketDataMessage[T <: Temporal]( timeStep: TimeStep[T]
                                             , quotes  : Array[QuoteId]
                                             , data    : ImmutableMarketData
                                             ) extends MarketMessage

}