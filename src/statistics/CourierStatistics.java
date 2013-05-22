
package statistics;

/**
 *
 * @author Kamil
 */
public class CourierStatistics implements CourierStatistics_Interface {
    
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
     * Liczba wszystkich "pobranych" paczek
     * @var int 
     */
    protected int _numberOfPacks;
    
    /**
     * Liczba dostarczonych paczek
     * @var int
     */
    protected int _numberOfDeliveriedPacks;
    
    /**
     * Całkowity czas dostawy
     * @var int
     */
    protected int _totalDeliveryTime;
    
    /**
     * Stosunek procentowy dostaczonych do otrzymanych.
     * @return float
     */
    @Override
    public float getDeliveriedToRecieved(){
        return this._numberOfDeliveriedPacks/this._numberOfPacks;
    }
    
    /**
     * Średni czas dostawy przesyłki.
     * @return 
     */
    @Override
    public float getAverageDeliveryTime(){
        return this._totalDeliveryTime/this._numberOfPacks;
    }
    
    /**
     * Setter dla przychodu.
     * @param int income
     * @return CourierStatistics_Interface
     */
    @Override
    public CourierStatistics_Interface setIncome(int income){
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
     * @return CourierStatistics_Interface
     */
    @Override
    public CourierStatistics_Interface setProfit(int profit){
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
    
    /**
     * Setter dla liczby paczek.
     * @param int numberOfPacks
     * @return CourierStatistics_Interface
     */
    @Override
    public CourierStatistics_Interface setNumberOfPacks(int numberOfPacks){
        this._numberOfPacks=numberOfPacks;
        return this;
    }
    
    /**
     * Getter dla liczby paczek.
     * @return int
     */
    @Override
    public int getNumberOfPacks(){
        return this._numberOfPacks;
    }
    
    /**
     * Setter dla liczby dostarczonych paczek
     * @param int numberOfDeliveriedPacks
     * @return CourierStatistics_Interface
     */
    @Override
    public CourierStatistics_Interface setNumberOfDeliveriedPacks(int numberOfDeliveriedPacks){
        this._numberOfDeliveriedPacks=numberOfDeliveriedPacks;
        return this;
    }
    
    /**
     * Getter dla liczby dostarczonych paczek.
     * @return int
     */
    @Override
    public int getNumberOfDeliveriedPacks(){
        return this._numberOfDeliveriedPacks;
    }
    
    /**
     * Setter dla całkowitego czasu dostawy
     * @param int totalDeliveryTime
     * @return CourierStatistics_Interface
     */
    @Override
    public CourierStatistics_Interface setTotalDeliveryTime(int totalDeliveryTime){
        this._totalDeliveryTime=totalDeliveryTime;
        return this;
    }
    
    /**
     * Getter dla całkowitego czasu dostawy.
     * @return int
     */
    @Override
    public int getTotalDeliveryTime(){
        return this._totalDeliveryTime;
    }

}
