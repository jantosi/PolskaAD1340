package statistics;

/**
 * Interfejs dla statystyk drwala.
 * @author Piotrek
 */
public interface WoodmanStatistics_Interface {
    
    /**
     * Setter dla ilości ściętych drzew.
     * @param int numberOfShearedWoods
     * @return WoodmanStatistics_Interface
     */
    public WoodmanStatistics_Interface setNumberOfShearedWoods( int numberOfShearedWoods);
    
    /**
     * Getter dla ilości ściętych drzew.
     * @return 
     */
    public int getNumberOfShearedWoods();
    
    /**
     * Setter dla całkowitego zysku.
     * @param int profit
     * @return WoodmanStatistics_Interface
     */
    public WoodmanStatistics_Interface setProfit(int profit);
    
    /**
     * Getter dla całkowtego zysku.
     * @return int
     */
    public int getProfit();
    
    /**
     * Setter dla całkowitego przychodu.
     * @param int income
     * @return WoodmanStatistics_Interface
     */
    public WoodmanStatistics_Interface setIncome(int income);
    
    /**
     * Getter dla całkowitego przychodu.
     * @return int
     */
    public int getIncome();
}
