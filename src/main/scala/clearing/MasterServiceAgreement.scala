package clearing

import com.opengamma.strata.basics.currency.{Currency, CurrencyAmount}
import com.opengamma.strata.basics.currency.Currency.EUR
import com.opengamma.strata.product.SecurityId
/**
  * @author maarten (Maarten P. Scholl)
  * @date 2017-08-06T22:36:00
  * @brief Projects.MasterServiceAgreement
  * @ingroup Projects
  */
class MasterServiceAgreement( parties: Array[BusinessEntity]
                            ){



  class CreditSupportAnnex( inFavourOf: Array[BusinessEntity] = Array()
                          , valuationCurrency: Currency = EUR
                          , activationTreshold: Double  = 0
                          , minimumChange: Double       = 0
                          , roundingAmount: Double      = 0
                          , collateralCurrencies: Set[Currency] = Set(EUR)
                          , collateralAssets: Set[Either[Currency, SecurityId]] =Set[Either[Currency, SecurityId]]()
                          )
  {

  }



  var csa : Option[CreditSupportAnnex] = None
}
