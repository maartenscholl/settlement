package clearing

/**
  * @author maarten (Maarten P. Scholl)
  * @date 2017-08-31T15:15:00
  * @brief Projects.SettlementMechanism
  * @ingroup Projects
  */
import akka.actor.{Actor, ActorLogging}

trait SettlementMechanism extends Actor with ActorLogging {

  def settlementMechanismBehavior: Receive

}
