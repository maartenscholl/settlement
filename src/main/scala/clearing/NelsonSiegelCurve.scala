package clearing

import com.opengamma.strata.basics.date.DayCount
import com.opengamma.strata.collect.array.DoubleArray
import com.opengamma.strata.data.MarketDataName
import com.opengamma.strata.market.curve.{Curve, CurveMetadata, Curves}
import com.opengamma.strata.market.param.UnitParameterSensitivity

import scala.math.exp

/**
  * @author maarten (Maarten P. Scholl)
  * @date 2017-09-06T13:24:00
  * @brief Projects.NelsonSiegelCurve
  * @ingroup Projects
  */


class NelsonSiegelCurve ( var tau: Double
                        , var beta_0: Double
                        , var beta_1: Double
                        , var beta_2: Double
                        , dayCount: DayCount
                        ) extends Curve {



  override def getMetadata: CurveMetadata = {
    Curves.zeroRates("NelsonSiegelCurve", dayCount)
  }

  override def yValueParameterSensitivity(x: Double): UnitParameterSensitivity = {
    UnitParameterSensitivity.of( this.getName : MarketDataName[Curve]//MarketDataName[Curve]()
      , DoubleArray.of ( - beta_1 *  (exp(-x/tau) / tau)
        + beta_1 *  (1 - exp(-x/tau)) / x
        + beta_2 *  (- x * exp(-x/tau) / (tau*tau)
                     + (1 - exp(-x/tau)) / x
                     - (exp(-x/tau) / tau)
                    )
      , 1
      , (tau - tau * exp(-x/tau)) / x
      , (exp(-x/tau) * (tau * (exp(x/tau) -1) - x)) / x
      )
    )
  }

  /**
    * TODO: do not ignore change in metadata
    * @param metadata
    * @return
    */
  override def withMetadata(metadata: CurveMetadata): Curve = {

    new NelsonSiegelCurve(tau,      beta_0,   beta_1,   beta_2, dayCount)
  }

  /**
    *Returns a copy of the NelsonSiegelCurve with the value at the specified index altered
    * @param parameterIndex
    * @param newValue
    * @return
    */
  override def withParameter(parameterIndex: Int, newValue: Double): Curve = {
    parameterIndex match {
      case 0 => new NelsonSiegelCurve(newValue, beta_0,   beta_1,   beta_2,   dayCount)
      case 1 => new NelsonSiegelCurve(tau,      newValue, beta_1,   beta_2,   dayCount)
      case 2 => new NelsonSiegelCurve(tau,      beta_0,   newValue, beta_2,   dayCount)
      case 3 => new NelsonSiegelCurve(tau,      beta_0,   beta_1,   newValue, dayCount)
    }
  }

  override def yValue(x: Double): Double = {
    NelsonSiegelParameterizedCurve.evaluate(x, DoubleArray.of(tau, beta_0, beta_1, beta_2))
  }

  override def firstDerivative(x: Double): Double = {
    ( exp(-x / tau) * ((beta_0 * beta_0 * (exp(x / tau) - 1) * (beta_1 + beta_2))
                      + beta_0 * x * (beta_1 + beta_2)
                      + beta_2 * x * x
                      )
    ) / (beta_0 * x * x)
  }

  override def getParameterCount: Int = 4

  override def getParameter(parameterIndex: Int): Double = {
    parameterIndex match {
      case 0 => tau
      case 1 => beta_0
      case 2 => beta_1
      case 3 => beta_2
    }
  }
}