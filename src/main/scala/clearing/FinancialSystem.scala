package clearing

import java.time.LocalDate
import java.time.temporal.Temporal

import akka.actor.{ActorRef, ActorRefFactory, ActorSystem, Props, TypedActorExtension, TypedActorFactory}
import akka.testkit.{TestActorRef, TestKit, TestProbe}

/**
  * @author maarten (Maarten P. Scholl)
  * @date 2017-08-31T22:48:00
  * @brief Projects.System
  * @ingroup Projects
  */
class FinancialSystem[T <: Temporal] ( systemSize:  Int
                                     , markets: Array[Market]
                                     , actorSystem: ActorSystem = ActorSystem("FinancialSystem")
                                     )
  extends TestKit(actorSystem)
{
  val entities: Array[ActorRef] = (0 until systemSize).map(i => {
    this.system.actorOf(Props(new BusinessEntity(i.toString)))
  }).toArray

  def getEntities: Array[ActorRef] = entities

}
