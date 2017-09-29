/**
  * @author maarten (Maarten P. Scholl)
  * @date 2017-08-13T22:41:00
  * @brief Projects.LegalEntity
  * @ingroup Projects
  */

package clearing

import akka.actor.{Actor, ActorLogging}
import com.opengamma.strata.basics.StandardId

import akka.persistence._


abstract class LegalEntity( name: String
                          , jurisdiction:Jurisdiction = Jurisdiction("default")
                          , var mutations: Long = 0
                          ) extends PersistentActor with AtLeastOnceDelivery with  ActorLogging
{
  val legalEntityId:LegalEntityId = LegalEntityId.of(StandardId.of("LegalEntity", name))

}

