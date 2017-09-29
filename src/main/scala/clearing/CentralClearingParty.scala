package clearing

/**
  * @author maarten (Maarten P. Scholl)
  * @date 2017-09-07T18:33:00
  * @brief Projects.CentralClearingParty
  * @ingroup Projects
  */
class CentralClearingParty( name : String) extends BusinessEntity(name)
{
  def Novate( partyLeft: LegalEntity
            , partyRight: LegalEntity
            ) : (ClearingRelation,ClearingRelation) = {


    (new ClearingRelation(partyLeft, this), new ClearingRelation(partyRight, this))
  }



}

