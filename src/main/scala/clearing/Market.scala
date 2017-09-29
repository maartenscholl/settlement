package clearing

import akka.actor.InvalidMessageException
import clearing.Messages._
import com.opengamma.strata.basics.StandardId
import com.opengamma.strata.market.observable.QuoteId
import com.opengamma.strata.product._

import scala.collection.mutable


/**
  * @author maarten (Maarten P. Scholl)
  * @date 2017-09-04T22:48:00
  * @brief Projects.Market
  * @ingroup Projects
  */
abstract class Market( name:String
                     , quotes: Array[QuoteId]
                     ) extends LegalEntity(name) {

  sealed trait TradingStatus
  case object Active extends TradingStatus
  case object Inactive extends TradingStatus

  var participants : mutable.HashMap[LegalEntityId, TradingStatus]

  // tracks recent trades and their confirmation
  private var recentTrades: mutable.HashMap[LegalEntityId, mutable.HashMap[StandardId, Boolean]] = new mutable.HashMap[LegalEntityId, mutable.HashMap[StandardId, Boolean]]

  def addParticipant(participant:LegalEntity) = participants.put(participant.legalEntityId, Inactive)

  def removeParticipant(participant:LegalEntity) = participants.remove(participant.legalEntityId)


  def sessionDone: Boolean = {
    participants.forall(kv => kv._2 == Inactive)
  }

  override def receive = {
    case Order( principal : LegalEntityId
              , instrument: QuoteId
              , level     : Double
              , quantity  : Double
              , side      : Order.Side) => {

    }



    case _ => {
      print("?Market?")
    }

  }


  def update(): Unit =
  {
    participants.foreach(p => participants.put(p._1,Active))
    recentTrades = new mutable.HashMap

  }

}
