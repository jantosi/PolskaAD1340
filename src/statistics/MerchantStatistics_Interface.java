package statistics;

/**
 * Interfejs dla statystyk posłańca.
 * 
 * @author Piotrek
 */
public interface MerchantStatistics_Interface { 
    
    /**
     * Setter dla przychodu.
     * @param int income
     * @return MerchantStatistics_Interface
     */
    public MerchantStatistics_Interface setIncome(int income);
    
    /**
     * Getter dla przychodu.
     * @return int
     */
    public int getIncome();
    
    /**
     * Setter dla zysku.
     * @param int profit
     * @return MerchantStatistics_Interface
     */
    public MerchantStatistics_Interface setProfit(int profit);
    
    /**
     * Getter dla zysku.
     * @return int
     */
    public int getProfit();
}
