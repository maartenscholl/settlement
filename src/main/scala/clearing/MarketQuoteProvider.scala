package clearing

import java.time.LocalDate

import com.opengamma.strata.data.ImmutableMarketData
import com.opengamma.strata.market.observable.QuoteId

import scala.collection.mutable

/**
  * @author maarten (Maarten P. Scholl)
  * @date 2017-09-12T17:18:00
  * @brief Projects.MarketQuoteProvider
  * @ingroup Projects
  */
trait MarketQuoteProvider {
  def getQuoteIds: Array[QuoteId]

  def getQuotes(timeStep:TimeStep[LocalDate]): mutable.HashMap[QuoteId, ImmutableMarketData]

}
