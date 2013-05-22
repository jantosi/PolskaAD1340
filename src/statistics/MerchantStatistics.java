
package statistics;

/**
 *
 * @author Kamil
 */
public class MerchantStatistics implements MerchantStatistics_Interface {
    
    /**
     * Przychód
     * @var int
     */
    protected int _income;
    
    /**
     * Zysk
     * @var int
     */
    protected int _profit;
    
    /**
     * Setter dla przychodu.
     * @param int income
     * @return MerchantStatistics_Interface
     */
    @Override
    public MerchantStatistics_Interface setIncome(int income){
        this._income=income;
        return this;
    }
    
    /**
     * Getter dla przychodu.
     * @return int
     */
    @Override
    public int getIncome(){
        return this._income;
    }
    
    /**
     * Setter dla zysku.
     * @param int profit
     * @return MerchantStatistics_Interface
     */
    @Override
    public MerchantStatistics_Interface setProfit(int profit){
        this._profit=profit;
        return this;
    }
    
    /**
     * Getter dla zysku.
     * @return int
     */
    @Override
    public int getProfit(){
        return this._profit;
    }
}
