package clearing

import com.opengamma.strata.basics.{ReferenceDataId, StandardId}
import com.opengamma.strata.product.Security

/**
  * @author maarten (Maarten P. Scholl)
  * @date 2017-09-12T15:49:00
  * @brief Projects.LegalEntityId
  * @ingroup Projects
  */
case class LegalEntityId(standardId : StandardId) extends ReferenceDataId[LegalEntity]{
  override def getReferenceDataType = classOf[LegalEntity]

  def getStandardId: StandardId = standardId
}

object LegalEntityId{
  def of(standardId: StandardId) : LegalEntityId = {
    new LegalEntityId(standardId)
  }
}