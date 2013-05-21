package agents;

import java.util.ArrayList;
import items.*;

/**
 * Klasa definiująca kupca.
 * 
 * @author Piotrek
 */
public class Merchant extends Agent {
    
    /**
     * Pojemność magazynu kupca.
     * @var int
     */
    protected int _capacity;
    /**
     * Lista przedmiotów.
     * @var ArrayList<Item>
     */
    protected ArrayList<Item> _items;
    
    /**
     * Konstruktor. Ustawienie domyślnej wielkości magazynu.
     * @param int capacity 
     */
    public void Merchant(int capacity) {
        super.Agent();
        
        this._capacity = capacity;
        this._items = new ArrayList<Item>();
    }
    /**
     * Getter dla pojemności magazynu.
     * @return int
     */
    public int getCapacity() {
        return this._capacity;
    }
    
    /**
     * Setter dla pojemności magazynu.
     * @param int capacity
     * @return Merchant
     */
    public Merchant setCapacity(int capacity) {
        this._capacity = capacity;
        
        return this;
    }
    
    /**
     * Getter dla listy przedmiotów.
     * @return ArrayList<Item>
     */
    public ArrayList<Item> getItems() {
        return this._items;
    }
    
    /**
     * Kupno przedmiotu. Zwraca TRUE jeżeli kupiec ma miejsce i wystraczającą ilość golda, aby kupić
     * przedmiot. W przeciwnym wypadku zwraca FALSE.
     * @param Item item
     * @return boolean
     */
    public boolean buy(Item item) {
        if(this.getItems().size() < this.getCapacity() && this.getGold() >= item.getPrice()) {
            
            //Obniżanie wielkości mieszka.
            this.setGold(this.getGold() - item.getPrice());
            
            item.setPrice(Math.round(item.getPrice() * 1.3f));
            
            this.getItems().add(item);
            
            return true;
        }
        return false;
    }
    
    /**
     * Sprzedaż przedmiotu. Zwraca TRUE jeżeli przedmiot istnieje i uda się go sprzedać. W przeciwnym
     * wypadku zwraca FALSE.
     * @param Item item
     * @return boolean
     */
    public boolean sell(Item item) {
        if(this.getItems().indexOf(item) != -1) {
            //Usunięcie przedmiotu z magazynu.
            this.getItems().remove(item);
            //Zwiększenie wielkości mieszka.
            this.setGold(this.getGold() + item.getPrice());
            
            return true;
        } 
        return false;
    }
}
