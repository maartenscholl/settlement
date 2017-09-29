package clearing

import scala.collection.JavaConverters._
import scala.collection.{breakOut, mutable}
import scala.util.Random
import java.time.{LocalDate, Period}
import java.util

import com.opengamma.strata.basics.currency.Currency
import com.opengamma.strata.basics.{ReferenceData, StandardId}
import com.opengamma.strata.basics.currency.Currency.EUR
import com.opengamma.strata.basics.date._
import com.opengamma.strata.basics.index.{IborIndex, OvernightIndex}
import com.opengamma.strata.collect.array.{DoubleArray, DoubleMatrix}
import com.opengamma.strata.data.{ImmutableMarketData, ImmutableMarketDataBuilder}
import com.opengamma.strata.market.ValueType
import com.opengamma.strata.market.curve._
import com.opengamma.strata.market.curve.interpolator.{CurveExtrapolators, CurveInterpolators}
import com.opengamma.strata.market.curve.node._
import com.opengamma.strata.market.observable.QuoteId
import com.opengamma.strata.math.impl.function.ParameterizedCurve
import com.opengamma.strata.pricer.curve.CurveCalibrator
import com.opengamma.strata.product.swap.`type`.FixedIborSwapConventions.EUR_FIXED_1Y_EURIBOR_6M
import com.opengamma.strata.product.swap.`type`.FixedOvernightSwapConventions.EUR_FIXED_1Y_EONIA_OIS
import com.opengamma.strata.product.swap.`type`.{FixedIborSwapConvention, FixedIborSwapTemplate, FixedOvernightSwapConvention, FixedOvernightSwapTemplate}


/**
  * @author maarten (Maarten P. Scholl)
  * @date 2017-08-24T18:35:00
  * @brief Projects.DieboldLiInterestRateModel
  *
  *
  *
  * @ingroup Projects
  */
class DieboldLiInterestRateModel( CURVE: ParameterizedCurve = NelsonSiegelParameterizedCurve
                                 , CURRENCY: Currency = EUR
                                 , FORECAST_CONVENTION: FixedIborSwapConvention = EUR_FIXED_1Y_EURIBOR_6M
                                 , DISCOUNT_CONVENTION: FixedOvernightSwapConvention = EUR_FIXED_1Y_EONIA_OIS
                                 , correlation: DoubleMatrix
                                  = DoubleMatrix.copyOf(Array[Array[Double]]
                                  ( Array( 1.00000000, -0.94996610, -0.49012365)
                                  , Array(-0.94996610,  1.00000000,  0.33316724)
                                  , Array(-0.49012365,  0.33316724,  1.00000000)))
                                 , sigma:Tuple3[Double,Double,Double]          = (0.085220302745296558, 0.089222250588068, 0.15550887434484245)
                                 , autoregression:Tuple3[Double,Double,Double] = (0.99966844, 0.99923602, 0.99600245)
                                 , tau: Double = 0.49
                                 , var beta: (Double, Double, Double) = (5.16644531, -1.86982715, -3.03360701)
                                 , val standard: Double = 0.11625771151486891
                                ) extends InterestRateModel(CURRENCY) with MarketQuoteProvider {

  val FORECAST_INDEX: IborIndex = FORECAST_CONVENTION.getFloatingLeg.getIndex
  val DISCOUNT_INDEX: OvernightIndex = DISCOUNT_CONVENTION.getFloatingLeg.getIndex

  val TENORS:Array[Period]  = (Array ( Period.ofDays(1)
                                    , Period.ofWeeks(1)
                                    , Period.ofMonths(1), Period.ofMonths(2), Period.ofMonths(3), Period.ofMonths(6)
                                    ) ++
                                    (1 to 10).map(y => Period.ofYears(y)) ++
                                    Array(Period.ofYears(15), Period.ofYears(20), Period.ofYears(25), Period.ofYears(30),Period.ofYears(35), Period.ofYears(40), Period.ofYears(45), Period.ofYears(50))
    ).filter(p => (p.toTotalMonths * 30 + p.getDays) >= (FORECAST_CONVENTION.getFloatingLeg.getPaymentFrequency.getPeriod.toTotalMonths * 30 + FORECAST_CONVENTION.getFloatingLeg.getPaymentFrequency.getPeriod.getDays))

  private val volatility      = (1.0, 1.0, 1.0)


  def update(timeStep:TimeStep[LocalDate]): Unit = {


    val rn = ( volatility._1 * sigma._1 * Random.nextGaussian * standard
             , volatility._2 * sigma._2 * Random.nextGaussian * standard
             , volatility._3 * sigma._3 * Random.nextGaussian * standard
             )

    val c = ( correlation.get(0,0) * rn._1 + correlation.get(0,1) * rn._2 + correlation.get(0,2) * rn._3
            , correlation.get(1,0) * rn._1 + correlation.get(1,1) * rn._2 + correlation.get(1,2) * rn._3
            , correlation.get(2,0) * rn._1 + correlation.get(2,1) * rn._2 + correlation.get(2,2) * rn._3
            )

    val p = ( autoregression._1 * beta._1
            , autoregression._2 * beta._2
            , autoregression._3 * beta._3
            )

    beta =  ( beta._1 + p._1 * c._1
            , beta._2 + p._2 * c._2
            , beta._3 + p._3 * c._3)
  }

  val REF_DATA : ReferenceData  = ReferenceData.standard
  private val SCHEME            = "CALIBRATION"

  private val CURVE_GROUP_NAME  = CurveGroupName.of(CURRENCY.toString + "|DISCOUNT:" + DISCOUNT_INDEX.getName + "|FORECAST:" + FORECAST_INDEX.getName)
  private val DSC_CURVE_NAME    = CurveName.of(CURRENCY.toString + "-DISCOUNT-" + DISCOUNT_INDEX.getTenor.toString)
  private val FWD_CURVE_NAME    = CurveName.of(CURRENCY.toString + "-FORECAST-" + FORECAST_INDEX.getTenor.toString)

  private val DSC_ID_VALUE      = TENORS.map(tenor => CURRENCY.toString + "-" + DISCOUNT_INDEX.getTenor.toString + "-" + tenor.toString.substring(1))
  private val FWD_ID_VALUE      = TENORS.map(tenor => CURRENCY.toString + "-" + FORECAST_INDEX.getTenor.toString + "-" + tenor.toString.substring(1))

  private val CALIBRATOR        = CurveCalibrator.of(1e-9, 1e-9, 100)

  val DSC_NODES: util.List[FixedOvernightSwapCurveNode] = TENORS.indices.map(i =>
    FixedOvernightSwapCurveNode.of( FixedOvernightSwapTemplate.of ( Period.ZERO
                                  , Tenor.of(TENORS(i))
                                  , DISCOUNT_CONVENTION)
    , QuoteId.of(StandardId.of(SCHEME, DSC_ID_VALUE(i))))
    ).toList.asJava

  val DSC_CURVE_DEFN: InterpolatedNodalCurveDefinition = InterpolatedNodalCurveDefinition.builder
    .name(DSC_CURVE_NAME)
    .xValueType(ValueType.YEAR_FRACTION)
    .yValueType(ValueType.ZERO_RATE)
    .dayCount(DISCOUNT_INDEX.getDayCount)
    .interpolator(CurveInterpolators.LOG_LINEAR)
    .extrapolatorLeft(CurveExtrapolators.FLAT)
    .extrapolatorRight(CurveExtrapolators.FLAT)
    .nodes(DSC_NODES).build

  val FWD_NODES: util.List[FixedIborSwapCurveNode] = TENORS.indices.map(i => FixedIborSwapCurveNode.of(FixedIborSwapTemplate.of(Period.ZERO, Tenor.of(TENORS(i)), FORECAST_CONVENTION), QuoteId.of(StandardId.of(SCHEME, FWD_ID_VALUE(i))))).toList.asJava

  val FWD_CURVE_DEFN: InterpolatedNodalCurveDefinition = InterpolatedNodalCurveDefinition.builder
    .name(FWD_CURVE_NAME)
    .xValueType(ValueType.YEAR_FRACTION)
    .yValueType(ValueType.ZERO_RATE)
    .dayCount(FORECAST_INDEX.getDayCount)
    .interpolator(CurveInterpolators.LOG_LINEAR)
    .extrapolatorLeft(CurveExtrapolators.FLAT)
    .extrapolatorRight(CurveExtrapolators.FLAT)
    .nodes(FWD_NODES).build

  val CURVE_GROUP_CONFIG: CurveGroupDefinition = CurveGroupDefinition.builder.name(CURVE_GROUP_NAME).addCurve(DSC_CURVE_DEFN, CURRENCY, DISCOUNT_INDEX).addForwardCurve(FWD_CURVE_DEFN, FORECAST_INDEX).build

  val QUOTE_ID:QuoteId = QuoteId.of(StandardId.of("RATES", CURVE_GROUP_NAME.getName))

  override def getQuoteIds: Array[QuoteId] = {
    Array[QuoteId](QUOTE_ID)
  }

  override def getQuotes(timeStep:TimeStep[LocalDate]): mutable.HashMap[QuoteId, ImmutableMarketData] = {

    val immutableMarketDataBuilder:ImmutableMarketDataBuilder = ImmutableMarketData.builder(timeStep.toInclusive)

    val DSC_MARKET_QUOTES = TENORS.indices.map(i => 0.01 * CURVE.evaluate(i.toDouble + 1, DoubleArray.of(tau, beta._1, beta._2, beta._3)).doubleValue).toArray[Double]
    immutableMarketDataBuilder.addValueMap(new util.HashMap[QuoteId, Double](((DSC_MARKET_QUOTES.indices.map(i => QuoteId.of(StandardId.of(SCHEME, DSC_ID_VALUE(i)))) zip DSC_MARKET_QUOTES)(breakOut):Map[QuoteId, Double]).asJava))

    val FWD_MARKET_QUOTES = TENORS.indices.map(i => 0.01 * CURVE.evaluate(i.toDouble + 1, DoubleArray.of(tau, beta._1, beta._2, beta._3)).doubleValue).toArray[Double]
    immutableMarketDataBuilder.addValueMap(new util.HashMap[QuoteId, Double](((FWD_MARKET_QUOTES.indices.map(i => QuoteId.of(StandardId.of(SCHEME, FWD_ID_VALUE(i)))) zip FWD_MARKET_QUOTES)(breakOut):Map[QuoteId, Double]).asJava))


    val immutableMarketData = immutableMarketDataBuilder.build

    //val ratesProvider  = CALIBRATOR.calibrate(CURVE_GROUP_CONFIG, immutableMarketData, REF_DATA)

    mutable.HashMap(QUOTE_ID -> immutableMarketData)
  }

}
