package clearing

import java.time.LocalDate

import com.opengamma.strata.basics.currency.Currency
import com.opengamma.strata.pricer.rate.ImmutableRatesProvider

/**
  * @author maarten (Maarten P. Scholl)
  * @date 2017-09-05T17:20:00
  * @brief Projects.InterestRateModel
  * @ingroup Projects
  */
abstract class InterestRateModel(CURRENCY:Currency) extends MarketQuoteProvider {
  def update(timeStep: TimeStep[LocalDate]): Unit//ImmutableMarketData\

}
