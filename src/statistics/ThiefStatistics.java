
package statistics;

/**
 *
 * @author Kamil
 */
public class ThiefStatistics implements ThiefStatistics_Interface{
    
    /**
     * Całkowita liczba pobytów w więzieniu
     * @var int
     */
    protected int _numberOfStaysInJail;
    
    /**
     * Całkowity łup 
     * @var int
     */
    protected int _totalBooty;
    
    /**
     * Całkowity zysk
     */
    protected int _profit;
    
    /**
     * Setter dla całkowitej liczby pobytów w więzieniu.
     * @param int numberOfStaysInJail
     * @return ThiefStatistics_Interface
     */
    @Override
    public ThiefStatistics setNumberOfStaysInJail(int numberOfStaysInJail){
        this._numberOfStaysInJail=numberOfStaysInJail;
        return this;
    }
    
    /**
     * Getter dla całkowitej liczby pobytów w więzieniu.
     * @return int
     */
    @Override
    public int getNumberOfStaysInJail(){
        return this._numberOfStaysInJail;
    }
    
    /**
     * Setter dla całkowitego łupu.
     * @param int totalBooty
     * @return ThiefStatistics_Interface
     */
    @Override
    public ThiefStatistics setTotalBooty(int totalBooty){
        this._totalBooty=totalBooty;
        return this;
    }
    
    /**
     * Getter dla całkowitego łupu.
     * @return int
     */
    @Override
    public int getTotalBooty(){
        return this._totalBooty;
    }
    
    /**
     * Setter dla całkowitego zysku.
     * @param int profit
     * @return ThiefStatistics_Interface
     */
    @Override
    public ThiefStatistics setProfit(int profit){
        this._profit=profit;
        return this;
    }
    
    /**
     * Getter dla całkowitego zysku.
     * @return int
     */
    @Override
    public int getProfit(){
        return this._profit;
    }
    
}
