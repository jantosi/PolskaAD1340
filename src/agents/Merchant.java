package agents;

import items.Item;

import java.util.ArrayList;

import statistics.MerchantStatistics_Interface;

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
     * Statystyki kupca.
     * @var MerchantStatistics_Interface
     */
    protected MerchantStatistics_Interface _statistics;
    
    /**
     * Konstruktor. Ustawienie domyślnej wielkości magazynu.
     * @param int capacity 
     */
    public Merchant(String id, String pathToClipsFile, int capacity, MerchantStatistics_Interface stat) {
        super(id, pathToClipsFile);
        
        this.setCapacity(capacity);
        this._items = new ArrayList<Item>();
        this._statistics = stat;
        this.setGold(0);
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
            //Zwiększenie przychodu.
            this.getStatistics().setIncome(this.getStatistics().getIncome() + item.getPrice());
            
            return true;
        } 
        return false;
    }
    
    
    /**
     * Przeciążenie settera dla wielkości mieszka uwzględniając zapis do statystyk.
     * @param int gold
     * @return Merchant
     */
    @Override
    public Merchant setGold(int gold) {
        super.setGold(gold);
        this.getStatistics().setProfit(this.getGold());
        
        return this;
    }
    
    /**
     * Setter dla statystyk kupca.
     * @param MerchantStatistics_Interface statistics
     * @return Merchant
     */
    public Merchant setStatistics(MerchantStatistics_Interface statistics) {
        this._statistics = statistics;
        
        return this;
    }
    
    /**
     * Getter dla statystyk kupca.
     * @return MerchantStatistics_Interface
     */
    public MerchantStatistics_Interface getStatistics() {
        return this._statistics;
    }
	@Override
	public String toString() {
		StringBuffer buffer = new StringBuffer();
		buffer.append("(kupiec (pojemnoscMagazynu ");
		buffer.append(_capacity);
		buffer.append(")");
		
		if (!this._items.isEmpty()) {
			buffer.append(" (przedmioty ");
			for (int i = 0; i < this._items.size(); i++) {
				buffer.append(this._items.get(i).getId());
				
				if (i < this._items.size() - 1) {
					buffer.append(" ");
				}
			}
			buffer.append(")");
		}
		
		buffer.append(" (id ");
		buffer.append(id);
		buffer.append(")");
	
		buffer.append("(mozliwyRuch ");
		buffer.append(possibleMove);
		buffer.append(")");

		buffer.append("(idKratki ");
		buffer.append(this.mapFrame.getId());
		buffer.append(")");

		buffer.append("(poleWidzenia ");
		buffer.append(fieldOfView);
		buffer.append(") (predkosc ");
		buffer.append(velocity);
		buffer.append(") (energia ");
		buffer.append(energy);
		buffer.append(") (strataEnergii ");
		buffer.append(energyLoss);
		buffer.append(") (odnawianieEnergii ");
		buffer.append(energyRecovery);
		buffer.append(") (zloto ");
		buffer.append(gold);
		buffer.append("))");
		return buffer.toString();
	}
}
