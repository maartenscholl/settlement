package clearing

/**
  * @author maarten (Maarten P. Scholl)
  * @date 2017-08-08T21:40:00
  * @brief Projects.BalanceSheet is a datastructure that contains a legal entities' books and functionality to compute
  *       statistics that typically make up a balance sheet
  * @ingroup Projects
  */
import com.opengamma.strata.basics.currency.{Currency, CurrencyAmount}
import com.opengamma.strata.product._

import scala.collection.mutable._


object BalanceSheet {
  class Book extends HashSet[CurrencyAmount] {

    def toBaseCurrency ( exchange : (Currency, Double) => Double) : Double = {
      this.foldLeft(0.0)((accumulator, kv) => accumulator + exchange(kv.getCurrency, kv.getAmount))
    }
  }
}

case class BalanceSheet ( var Capital     : BalanceSheet.Book                                 = new BalanceSheet.Book()
                        , var Loans       : HashMap[CounterpartyRelation, BalanceSheet.Book]  = new HashMap()
                        , var Securities  : HashSet[SecurityPosition]                         = new HashSet()
                        , var Derivatives : HashMap[CounterpartyRelation, ProductTrade]       = new HashMap()
                        , var Property    : BalanceSheet.Book                                 = new BalanceSheet.Book()
                        , var Equity      : BalanceSheet.Book                                 = new BalanceSheet.Book()
                        ){
}


