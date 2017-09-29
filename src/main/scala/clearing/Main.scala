package clearing
/**
  * @author maarten (Maarten P. Scholl)
  * @date 2017-08-06T21:41:00
  * @brief Projects.Main
  * @ingroup Projects
  */
import java.time.temporal.ChronoUnit
import java.time.{LocalDate, Period}

import com.opengamma.strata.basics.StandardId
import com.opengamma.strata.basics.date.Tenor
import com.opengamma.strata.pricer.sensitivity.MarketQuoteSensitivityCalculator
import com.opengamma.strata.pricer.swap.DiscountingSwapProductPricer
import com.opengamma.strata.product.common.BuySell
import com.opengamma.strata.product.swap.`type`.{FixedIborSwapConvention, FixedIborSwapConventions, StandardFixedIborSwapConventions}
import com.opengamma.strata.basics.currency.Currency.EUR

import scala.util.Random
import com.opengamma.strata.basics.date.{BusinessDayConvention, HolidayCalendar}
import com.opengamma.strata.basics.date.HolidayCalendars.SAT_SUN
import com.opengamma.strata.basics.date.BusinessDayConventions.FOLLOWING
import com.opengamma.strata.product.{Security, SecurityId}

import scala.collection.mutable



object Main {

  def main(arguments : Array[String]): Unit = {
    Random.setSeed(0)


    /*
  {
      val i = new DieboldLiInterestRateModel()
      //val VAL_DATE = LocalDate.of(2015, 7, 21)

      val ts = TimeStep.apply(LocalDate.of(2015, 7, 20), LocalDate.of(2015, 7, 21))

      val rates = i.Update(ts )

      val SWAP_PRICER     = DiscountingSwapProductPricer.DEFAULT
      val MQC             = MarketQuoteSensitivityCalculator.DEFAULT

      val notional        = 100000000.0
      val fixedRate       = 0.0

      val trade           = FixedIborSwapConventions.EUR_FIXED_1Y_EURIBOR_6M.createTrade(ts.from_exclusive, Period.ofDays(2), Tenor.TENOR_5Y, BuySell.BUY, notional, fixedRate, i.REF_DATA)

      val product         = trade.getProduct.resolve(i.REF_DATA)
      val pts             = SWAP_PRICER.presentValueSensitivity(product, rates)
      val ps              = rates.parameterSensitivity(pts.build)
      val mqs             = MQC.sensitivity(ps, rates)
      val pv0             = SWAP_PRICER.presentValue(product, rates).getAmount(EUR).getAmount
  }

  val N=100*/



    var recentTrades: mutable.HashMap[LegalEntityId, mutable.HashMap[StandardId, Boolean]] = new mutable.HashMap[LegalEntityId, mutable.HashMap[StandardId, Boolean]]




    var p1 = new BusinessEntity("p1")
    var p2 = new BusinessEntity("p2")







    val startDate = LocalDate.of(2018, 1, 1)
    val endDate = LocalDate.of(2018, 3, 1)
    val holidayCalendar       = SAT_SUN

    val systemSize = 100
    val markets = Array[Market]()
    val system = new FinancialSystem[LocalDate](systemSize, markets)

    val e = new Experiment(system, startDate, endDate, holidayCalendar.next, holidayCalendar.daysBetween)




  }
}



























