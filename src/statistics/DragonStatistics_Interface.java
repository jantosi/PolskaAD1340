package statistics;

/**
 * Interfejs dla statystyk smoka.
 * @author Piotrek
 */
public interface DragonStatistics_Interface {
    
    /**
     * Setter dla ilości zeżartych ludzi.
     * @param int numberOfEatenPeople
     * @return DragonStatistics_Interface
     */
    public DragonStatistics_Interface setNumberOfEatenPeople(int numberOfEatenPeople);
    
    /**
     * Getter dla ilości zeżartych ludzi.
     * @return int
     */
    public int getNumberOfEatenPeople();
    
    /**
     * Setter dla całkowitej liczby ukradzionych dóbr.
     * @param int totalBooty
     * @return DragonStatistics_Interface
     */
    public DragonStatistics_Interface setTotalBooty(int totalBooty);
    
    /**
     * Getter dla całkowitej liczby ukradzionych dóbr.
     * @return int
     */
    public int getTotalBooty();
    
    /**
     * Setter dla całkowitych obrażeń.
     * @param int totalInjuries
     * @return DragonStatistics_Interface
     */
    public DragonStatistics_Interface setTotalInjuries(int totalInjuries);
    
    /**
     * Getter dla całkowityh obrażeń.
     * @return int
     */
    public int getTotalInjuries();
}
