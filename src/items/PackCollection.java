package items;

import java.util.ArrayList;

/**
 * Klasa kontenera paczek.
 * 
 * @author Piotrek
 */
public class PackCollection {
    protected ArrayList<Pack> _packages;
    
    public PackCollection() {
        this._packages = new ArrayList<Pack>();
    }
    
    /**
     * Dodawanie paczki.
     * @param Pack pack
     * @return PackCollection
     */
    public PackCollection addPackage(Pack pack) {
        this._packages.add(pack);
        
        return this;
    }
    
    /**
     * Usuwanie paczki.
     * @param Pack pack
     * @return PackCollection
     */
    public PackCollection removePackage(Pack pack) {
        if(this._packages.indexOf(pack) != -1) {
            this._packages.remove(pack);
        }
        
        return this;
    }
    
    /**
     * Dostarczanie paczki. Zwraca TRUE jeżeli paczka istnieje w kontenerze i można ją dostarczyć,
     * w przeciwnym wypadku FALSE.
     * @param Pack pack
     * @return boolean
     */
    public boolean deliveryPackage(Pack pack) {
        if(this._packages.indexOf(pack) != -1) {
            this._packages.get(this._packages.indexOf(pack)).setIsDeliveried(true);
            
            return true;
        }
        
        return false;
    }
    
    /**
     * Całkowita waga paczek w kontenerze.
     * @return int
     */
    public int getTotalWeight() {
        int weight = 0;
        for(int i = 0; i < this._packages.size(); i++) {
            if(!this._packages.get(i).getIsDeliveried()) {
                weight += this._packages.get(i).getMass();
            }
        }
        
        return weight;
    }
    
    /**
     * Lista wszystkich paczek w kontenerze.
     * @return ArrayList<Pack>
     */
    public ArrayList<Pack> getAll() {
        return this._packages;
    }
}
