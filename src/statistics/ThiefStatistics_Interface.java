/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package statistics;

/**
 *
 * @author Piotrek
 */
public interface ThiefStatistics_Interface {
    
    /**
     * Setter dla całkowitej liczby pobytów w więzieniu.
     * @param int numberOfStaysInJail
     * @return ThiefStatistics_Interface
     */
    public ThiefStatistics_Interface setNumberOfStaysInJail(int numberOfStaysInJail);
    
    /**
     * Getter dla całkowitej liczby pobytów w więzieniu.
     * @return int
     */
    public int getNumberOfStaysInJail();
    
    /**
     * Setter dla całkowitego łupu.
     * @param int totalBooty
     * @return ThiefStatistics_Interface
     */
    public ThiefStatistics_Interface setTotalBooty(int totalBooty);
    
    /**
     * Getter dla całkowitego łupu.
     * @return int
     */
    public int getTotalBooty();
    
    /**
     * Setter dla całkowitego zysku.
     * @param int profit
     * @return ThiefStatistics_Interface
     */
    public ThiefStatistics_Interface setProfit(int profit);
    
    /**
     * Getter dla całkowitego zysku.
     * @return int
     */
    public int getProfit();
}
