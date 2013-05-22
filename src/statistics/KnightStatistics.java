
package statistics;

/**
 *
 * @author Kamil
 */
public class KnightStatistics implements KnightStatistics_Interface{
    
    /**
     * liczba zabitych smoków
     * @var int
     */
    protected int _numberOfKilledDragons;
    
    /** 
     * liczba wizyt w grodach
     * @var int
     */
    protected int _numberOfVisitsInCities;
    
    /**
     * Setter dla liczby zabitych smoków.
     * @param int numberOfKilledDragons
     * @return KnightStatistics_Interface
     */
    @Override
    public KnightStatistics setNumberOfKilledDragons(int numberOfKilledDragons){
        this._numberOfKilledDragons=numberOfKilledDragons;
        return this;
    }
    
    /**
     * Getter dla liczby zabitych smoków.
     * @return int
     */
    @Override
    public int getNumberOfKilledDragons(){
        return this._numberOfKilledDragons;
    }
    
    /**
     * Setter dla liczby wizyt w grodach.
     * @param int numberOfVisitsInCities
     * @return KnightStatistics_Interface
     */
    @Override
    public KnightStatistics setNumberOfVisitsInCities(int numberOfVisitsInCities){
        this._numberOfVisitsInCities=numberOfVisitsInCities;
        return this;
    }
    
    /**
     * Getter dla liczby wizyt w grodach.
     * @return int
     */
    @Override
    public int getNumberOfVisitsInCities(){
        return this._numberOfVisitsInCities;
    }
    
}
