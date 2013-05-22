package statistics;

/**
 * Interfejs dla statystyk posłańca.
 * 
 * @author Piotrek
 */
public interface CourierStatistics_Interface { 
    
    /**
     * Stosunek procentowy dostaczonych do otrzymanych.
     * @return float
     */
    public float getDeliveriedToRecieved();
    
    /**
     * Średni czas dostawy przesyłki.
     * @return 
     */
    public float getAverageDeliveryTime();
    
    /**
     * Setter dla przychodu.
     * @param int income
     * @return CourierStatistics_Interface
     */
    public CourierStatistics_Interface setIncome(int income);
    
    /**
     * Getter dla przychodu.
     * @return int
     */
    public int getIncome();
    
    /**
     * Setter dla zysku.
     * @param int profit
     * @return CourierStatistics_Interface
     */
    public CourierStatistics_Interface setProfit(int profit);
    
    /**
     * Getter dla zysku.
     * @return int
     */
    public int getProfit();
    
    /**
     * Setter dla liczby paczek.
     * @param int numberOfPacks
     * @return CourierStatistics_Interface
     */
    public CourierStatistics_Interface setNumberOfPacks(int numberOfPacks);
    
    /**
     * Getter dla liczby paczek.
     * @return int
     */
    public int getNumberOfPacks();
    
    /**
     * Setter dla liczby dostarczonych paczek
     * @param int numberOfDeliveriedPacks
     * @return CourierStatistics_Interface
     */
    public CourierStatistics_Interface setNumberOfDeliveriedPacks(int numberOfDeliveriedPacks);
    
    /**
     * Getter dla liczby dostarczonych paczek.
     * @return int
     */
    public int getNumberOfDeliveriedPacks();
    
    /**
     * Setter dla całkowitego czasu dostawy
     * @param int totalDeliveryTime
     * @return CourierStatistics_Interface
     */
    public CourierStatistics_Interface setTotalDeliveryTime(int totalDeliveryTime);
    
    /**
     * Getter dla całkowitego czasu dostawy.
     * @return int
     */
    public int getTotalDeliveryTime();
}
