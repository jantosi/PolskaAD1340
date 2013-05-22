package statistics;

/**
 * Interfejs dla statystyk rycerza.
 * @author Piotrek
 */
public interface KnightStatistics_Interface {
    
    /**
     * Setter dla liczby zabitych smoków.
     * @param int numberOfKilledDragons
     * @return KnightStatistics_Interface
     */
    public KnightStatistics_Interface setNumberOfKilledDragons(int numberOfKilledDragons);
    
    /**
     * Getter dla liczby zabitych smoków.
     * @return int
     */
    public int getNumberOfKilledDragons();
    
    /**
     * Setter dla liczby wizyt w grodach.
     * @param int numberOfVisitsInCities
     * @return KnightStatistics_Interface
     */
    public KnightStatistics_Interface setNumberOfVisitsInCities(int numberOfVisitsInCities);
    
    /**
     * Getter dla liczby wizyt w grodach.
     * @return int
     */
    public int getNumberOfVisitsInCities();
}
