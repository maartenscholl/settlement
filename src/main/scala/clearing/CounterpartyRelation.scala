package clearing

/**
  * @author maarten (Maarten P. Scholl)
  * @date 2017-09-04T17:31:00
  * @brief Projects.CounterpartyRelation
  * @ingroup Projects
  */
class CounterpartyRelation( entity : LegalEntity) {

}


class ClearingRelation( entity : LegalEntity
                      , clearingParty: LegalEntity)
  extends CounterpartyRelation(entity) {

}
