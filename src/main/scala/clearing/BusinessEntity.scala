package clearing

import akka.persistence.SnapshotOffer

/**
  * @author maarten (Maarten P. Scholl)
  * @date 2017-09-12T13:22:00
  * @brief Projects.BusinessEntity
  * @ingroup Projects
  */


case class Cmd(data: String)



case class Evt(data: String)



////////////////////////////////////////////////////////////////////////


class BusinessEntity( name : String
                    , var balanceSheet: BalanceSheet = BalanceSheet()
                    ) extends LegalEntity(name) {

  def updateState(evt: Evt) = {

  }










  override def receiveRecover: Receive = {
    case evt: Evt                                 => updateState(evt)
    case SnapshotOffer(_, snapshot: BalanceSheet) => balanceSheet = snapshot
  }




  override def receiveCommand: Receive = {
    case Cmd(data) =>
      persist( Evt(s"${data}-${mutations}")
             ) { (event) => {
          updateState(event)
          context.system.eventStream.publish(event)

          if (lastSequenceNr != 0) {
            saveSnapshot(balanceSheet)
          }
        }
      }





    case "print" => println(balanceSheet)
  }

  override def persistenceId: String = {
    name
  }

}