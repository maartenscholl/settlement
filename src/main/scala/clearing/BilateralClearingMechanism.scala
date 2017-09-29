package clearing

/**
  * @author maarten (Maarten P. Scholl)
  * @date 2017-09-04T15:14:00
  * @brief Projects.BilateralClearingMechanism
  * @ingroup Projects
  */
class BilateralClearingMechanism extends SettlementMechanism {

    //val settlementMechanismBehavior: Receive = {
    //  case contract: SpotContract => context.actorOf(SpotContractHandler.props(contract))
    //}


  override def settlementMechanismBehavior = ???

  def receive = {
    case "test" => log.info("received test")
    case _      => log.info("received unknown message")
  }



}
