package clearing

import java.time.temporal.Temporal

/**
  * @author maarten (Maarten P. Scholl)
  * @date 2017-09-01T18:49:00
  * @brief Projects.TimeStep
  * @ingroup Projects
  */
case class TimeStep[T <: Temporal](fromExclusive: T, toInclusive: T){

}
