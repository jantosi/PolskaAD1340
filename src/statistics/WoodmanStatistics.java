
package statistics;

/**
 *
 * @author Kamil
 */
public class WoodmanStatistics implements WoodmanStatistics_Interface{
    
    /**
     * ilość sciętych drzwe
     * @var int
     */
    protected int _numberOfShearedWoods;
    
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
     * Setter dla ilości ściętych drzew.
     * @param int numberOfShearedWoods
     * @return WoodmanStatistics_Interface
     */
    @Override
    public WoodmanStatistics setNumberOfShearedWoods( int numberOfShearedWoods){
        this._numberOfShearedWoods=numberOfShearedWoods;
        return this;
    }
    
    /**
     * Getter dla ilości ściętych drzew.
     * @return 
     */
    @Override
    public int getNumberOfShearedWoods(){
        return this._numberOfShearedWoods;
    }
    
    /**
     * Setter dla całkowitego zysku.
     * @param int profit
     * @return WoodmanStatistics_Interface
     */
    @Override
    public WoodmanStatistics setProfit(int profit){
        this._profit=profit;
        return this;
    }
    
    /**
     * Getter dla całkowtego zysku.
     * @return int
     */
    @Override
    public int getProfit(){
        return this._profit;
    }
    
    /**
     * Setter dla całkowitego przychodu.
     * @param int income
     * @return WoodmanStatistics_Interface
     */
    @Override
    public WoodmanStatistics setIncome(int income){
        this._income=income;
        return this;
    }
    
    /**
     * Getter dla całkowitego przychodu.
     * @return int
     */
    @Override
    public int getIncome(){
        return this._income;
    }
    
}
