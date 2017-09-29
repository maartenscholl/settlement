package clearing

import com.opengamma.opensimm.SimmCalculator
import com.opengamma.opensimm.basics.AssetClass.{CREDIT, INTEREST_RATE}
import com.opengamma.opensimm.basics.PortfolioExposure
import com.opengamma.opensimm.basics.RiskFactor
import com.opengamma.opensimm.basics.RiskType.SENSITIVITY
import com.opengamma.opensimm.basics.RiskFactorProperties
import com.opengamma.opensimm.util.Pair
import java.util
import java.util.Currency

import com.opengamma.opensimm.basics.{AssetClass, FxMatrix, StandardRiskFactor}
import com.opengamma.strata.basics.currency.{Currency ⇒ strataCurrency}
import com.opengamma.strata.product.SecurityId

/**
  * @author maarten (Maarten P. Scholl)
  * @date 2017-09-27T17:17:00
  * @brief Projects.Margin
  * @ingroup Projects
  */
class Margin {


  case class Call( valuationCurrency: strataCurrency
                 , amount: Double
                 , collateralAssets: List[Either[strataCurrency, SecurityId]])



  def InitialMargin(rfs:List[StandardRiskFactor]): java.util.Map[AssetClass, java.lang.Double] = {

    val IBM = StandardRiskFactor.of("IBM")
    val USD_IRSL3M_2Y = StandardRiskFactor.of("USD-IRSL3M-2Y")

    val riskFactorProperties = new util.HashMap[RiskFactor, RiskFactorProperties]()
    riskFactorProperties.put(USD_IRSL3M_2Y, RiskFactorProperties.relativeShock(INTEREST_RATE, SENSITIVITY, 0.04))
    riskFactorProperties.put(IBM, RiskFactorProperties.absoluteShock(CREDIT, SENSITIVITY))

    val baseLevels = new util.HashMap[RiskFactor, java.lang.Double]
    baseLevels.put(USD_IRSL3M_2Y, 0.01)
    baseLevels.put(IBM, 0.0120)

    val jEUR: java.util.Currency = java.util.Currency.getInstance("EUR")
    val jGBP: java.util.Currency = java.util.Currency.getInstance("GBP")
    val jUSD: java.util.Currency = java.util.Currency.getInstance("USD")

    val fxMatrix = FxMatrix.builder.addRate(jEUR, jUSD, 1.40).addRate(jGBP, jUSD, 1.60).build

    val riskFactorShocks = new util.HashMap[RiskFactor, util.List[java.lang.Double]]
    riskFactorShocks.put(USD_IRSL3M_2Y, // Normally many, many more
      util.Arrays.asList(1.0025, 1.0025, 0.9975, 0.9975, 1.0000, 1.0000, 1.0000, 1.0025))
    riskFactorShocks.put(IBM, util.Arrays.asList(0.0001, -0.0005, -0.0050, 0.0002, -0.0006, -0.0051))

    val fxShocks = new util.HashMap[Pair[java.util.Currency,java.util.Currency], util.List[java.lang.Double]]

    val sh1   = com.opengamma.opensimm.util.Pair.of(jEUR, jUSD)
    val sh2 = com.opengamma.opensimm.util.Pair.of(jGBP, jUSD)

    fxShocks.put(sh1, util.Arrays.asList(1.0000, 1.0010, 0.9975, 0.9950, 1.0002, 1.0100, 0.9950, 0.9970))
    fxShocks.put(sh2, util.Arrays.asList(0.9985, 1.0020, 1.0000, 0.9950, 1.0003, 1.0100, 0.9940, 0.9960))

    var builder = SimmCalculator.builder()

    builder = builder.varLevel(0.9)
    builder = builder.baseCurrency(jEUR)
    builder = builder.riskFactors(riskFactorProperties)
    builder = builder.riskFactorLevels(baseLevels)
    builder = builder.fxMatrix(fxMatrix)
    builder = builder.riskFactorShocks(riskFactorShocks)
    builder = builder.fxShocks(fxShocks)
    val calculator = builder.build

    // Build the portfolio to be assessed
    val portfolio = util.Arrays.asList( PortfolioExposure.of(USD_IRSL3M_2Y, 250000, jUSD)
                                      , PortfolioExposure.of(IBM, 100000, jGBP))

    // It is also possible to provide current initial and variation
    // margining positions in the same format - see version of
    // calculator.varByAssetClass taking three arguments

    val result = calculator.varByAssetClass(portfolio)
    result
  }
}

