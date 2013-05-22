
package statistics;

/**
 *
 * @author Kamil
 */
public class DragonStatistics implements DragonStatistics_Interface{
    
    /**
     * ilość zeżartych ludzi
     * @var int
     */
    protected int _numberOfEatenPeople;
    
    /**
     * Całkowita liczba ukradzionych dóbr
     * @val int
     */
    protected int _totalBooty;
    
    /**
     * całkowite obrażenia
     */
    protected int _totalInjuries;
    
    /**
     * Setter dla ilości zeżartych ludzi.
     * @param int numberOfEatenPeople
     * @return DragonStatistics_Interface
     */
    @Override
    public DragonStatistics setNumberOfEatenPeople(int numberOfEatenPeople){
        this._numberOfEatenPeople=numberOfEatenPeople;
        return this;
    }
    
    /**
     * Getter dla ilości zeżartych ludzi.
     * @return int
     */
    @Override
    public int getNumberOfEatenPeople(){
        return this._numberOfEatenPeople;
    }
    
    /**
     * Setter dla całkowitej liczby ukradzionych dóbr.
     * @param int totalBooty
     * @return DragonStatistics_Interface
     */
    @Override
    public DragonStatistics setTotalBooty(int totalBooty){
        this._totalBooty=totalBooty;
        return this;
    }
    
    /**
     * Getter dla całkowitej liczby ukradzionych dóbr.
     * @return int
     */
    @Override
    public int getTotalBooty(){
        return this._totalBooty;
    }
    
    /**
     * Setter dla całkowitych obrażeń.
     * @param int totalInjuries
     * @return DragonStatistics_Interface
     */
    @Override
    public DragonStatistics setTotalInjuries(int totalInjuries){
        this._totalInjuries=totalInjuries;
        return this;
    }
    
    /**
     * Getter dla całkowityh obrażeń.
     * @return int
     */
    @Override
    public int getTotalInjuries(){
        return this._totalInjuries;
    }
    
}
