package clearing

import com.opengamma.strata.product.TradeInfo

/**
  * @author maarten (Maarten P. Scholl)
  * @date 2017-09-25T20:07:00
  * @brief Projects.Match
  * @ingroup Projects
  */
class Match( taker     : LegalEntityId // aggressor
           , provider  : LegalEntityId // dealer or liquidity provider

           , level     : Double
           , quantity  : Double

           , trade     : TradeInfo
           )
