package clearing

import java.time.LocalDate

import akka.event.Logging

/**
  * @author maarten (Maarten P. Scholl)
  * @date 2017-09-01T21:59:00
  * @brief Projects.Experiment
  * @ingroup Projects
  */
class Experiment(system: FinancialSystem[LocalDate]
                , start: LocalDate
                , end: LocalDate
                , nextTemporal: (LocalDate => LocalDate)
                , betweenTemporal: ((LocalDate,LocalDate)  => Long)
                ){

  var temporal:LocalDate = start



  def Initialise(): Unit = {
    for (e <- system.getEntities){
      //e ! Messages.ProcessMarketData
    }
  }

  def Step(/*timeStep: TimeStep[LocalDate]*/): Unit = {

    val timeStep = TimeStep(temporal, nextTemporal(temporal))

    //system.Update(timeStep)

    temporal = timeStep.toInclusive
  }


  /**
    * current represents the current timestep in the simulation
    * start:LocalDate, current:LocalDate, end:LocalDate
    */
  type ProgressCallback = (LocalDate, LocalDate, LocalDate) => Unit

  def defaultPrintProgress(start:LocalDate, current:LocalDate, end:LocalDate):Unit = {
    print   ( betweenTemporal(start, current).toString
            + "/"
            + betweenTemporal(start, end).toString
            + "\n")
  }


  def Run( progressCallback: ProgressCallback = defaultPrintProgress): Unit = {
    temporal = start

    Initialise()

    for(t <- 0L until betweenTemporal(start, end)){
      Step()
      progressCallback(start, temporal, end)
    }
  }
}
