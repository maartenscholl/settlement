package clearing

/**
  * @author maarten (Maarten P. Scholl)
  * @date 2017-09-04T14:52:00
  * @brief Projects.CCPSettlementMechanism
  * @ingroup Projects
  */
//import akka.actor.{ActorRef, Props}

/** Central counterparty (CCP) clearing mechanism.
  *
  * @note The key difference between CCP clearing and bilateral clearing is that
  * CCP inserts itself as the counterparty to both the ask and the bid
  * trading parties before processing the final transaction. By acting as
  * a counterparty on every transaction the CCP effectively assumes all
  * counterparty risk.
  */
class CCPSettlementMechanism extends SettlementMechanism {

  override def settlementMechanismBehavior = ???

  def receive = {
    case _      => log.info("received unknown message")
  }


}