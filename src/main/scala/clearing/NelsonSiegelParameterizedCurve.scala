package clearing

import java.lang

import scala.math._
import com.opengamma.strata.collect.array.DoubleArray
import com.opengamma.strata.math.impl.function.ParameterizedCurve

/**
  * @author maarten (Maarten P. Scholl)
  * @date 2017-08-24T15:14:00
  * @brief Projects.NelsonSiegel
  * @ingroup Projects
  */
object NelsonSiegelParameterizedCurve extends ParameterizedCurve{

  /**
    * @return 4 parameters: tau, beta_{1,2,3}
    */
  override def getNumberOfParameters: Int = 4

  override def evaluate(x: lang.Double, parameters: DoubleArray): lang.Double = {
    val tau = parameters.get(0)

    (                                              parameters.get(1)
    + ((1-exp(-x / tau))/(x/tau))                * parameters.get(2)
    + ((1-exp(-x / tau))/(x/tau) -exp(-x / tau)) * parameters.get(3)
    )
  }
}

