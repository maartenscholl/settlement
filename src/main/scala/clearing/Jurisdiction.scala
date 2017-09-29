package clearing

import java.nio.charset.StandardCharsets
import java.util.UUID

import com.opengamma.strata.basics.currency.Currency
import com.opengamma.strata.collect.ArgChecker

/**
  * @author maarten (Maarten P. Scholl)
  * @date 2017-08-31T22:26:00
  * @brief Projects.Jurisdiction
  * @ingroup Projects
  */
case class Jurisdiction(canonicalName: String){
  lazy val uuid: UUID = UUID.nameUUIDFromBytes(canonicalName.getBytes(StandardCharsets.UTF_8))
}