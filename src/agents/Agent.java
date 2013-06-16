package agents;

import java.util.ArrayList;

import CLIPSJNI.Environment;
import CLIPSJNI.PrimitiveValue;

import polskaad1340.window.ObiektPierwszegoPlanu;
import world.MapFrame;

/**
 * Klasa abstrakcyjna definiująca podstawowe operacja dla każdego agenta.
 * 
 * @author Piotrek
 */
abstract public class Agent implements Cloneable{
    
	protected ObiektPierwszegoPlanu opp;
	protected String pathToClipsFile;
	
	/**
	 * Identyfikator agenta
	 */
	protected String id;
	
	/**
	 * Liczba kratek ruchu w danej turze
	 */
	protected int possibleMove;
	
	/**
	 * Id kratki, na ktorej znajduje sie agent
	 */
	protected MapFrame mapFrame;
	
    /**
     * Pole widzenia agenta.
     * @var int
     */
    protected int fieldOfView;
    
    /**
     * Prędkość agenta w czasie jednej iteracji.
     * @var int
     * 
     * @TODO: Zróżnicowanie prędkości względem terenu, po którym się porusza.
     */
    protected int velocity;
    
    /**
     * Energia agenta.
     * @var int
     */
    protected int energy;
    
    /**
     * Szybkość zużycia energii podczas ruchu o jedną kratkę.
     * @var int
     */
    protected int energyLoss;
    
    /**
     * Szybkość regeneracji energii w czasie jednej iteracji
     * @var int
     */
    protected int energyRecovery;
    
    /**
     * Ilość sztuk złota
     * @var int
     */
    protected int gold;
    
    protected String target;
    protected int extraVelocity;
    
    public Agent() {

    }
    
    /**
     * Konstruktor. Ustawienie domyślnych parametrów dla większości agentów.
     */
    public Agent(String id, String pathToClipsFile) {
        //Domyślne parametry dla większości agentów.
        this.setEnergy(100);
        this.setEnergyLoss(2);
        this.setEnergyRecovery(2);
        this.setId(id);
        this.gold = 20;
        this.pathToClipsFile = pathToClipsFile;
    }
    
    //wyciaga wszystkie podjete decyzje(akcje) agenta po wnioskowaniu
    public ArrayList<String> getInferenceResults(Environment agentEnv) {
    	ArrayList<String> results = new ArrayList<String>();
    	
    	String evalString = "(find-all-facts ((?a akcjaPrzemieszczanie)) TRUE)";
    	PrimitiveValue pv = agentEnv.eval(evalString);
    	try {
			for (int i = 0; i < pv.size(); i++) {
				PrimitiveValue tmpPv = pv.get(i);
				StringBuffer buf = new StringBuffer();
				
				buf.append("(akcjaPrzemieszczanie ")
				   .append("(idAgenta ").append(tmpPv.getFactSlot("idAgenta").toString()).append(") ")
				   .append("(ileKratek ").append(tmpPv.getFactSlot("ileKratek").toString()).append(") ")
				   .append("(kierunek ").append(tmpPv.getFactSlot("kierunek").toString()).append(")) ");
				
				results.add(buf.toString());
			}
		} catch (Exception e) {
		}
    	
    	evalString = "(find-all-facts ((?a akcjaPrzemieszczaniePoDrodze)) TRUE)";
    	pv = agentEnv.eval(evalString);
    	try {
			for (int i = 0; i < pv.size(); i++) {
				PrimitiveValue tmpPv = pv.get(i);
				StringBuffer buf = new StringBuffer();
				
				buf.append("(akcjaPrzemieszczaniePoDrodze ")
				   .append("(idAgenta ").append(tmpPv.getFactSlot("idAgenta").toString()).append(") ")
				   .append("(ileKratek ").append(tmpPv.getFactSlot("ileKratek").toString()).append(") ")
				   .append("(docelowyGrod ").append(tmpPv.getFactSlot("docelowyGrod").toString()).append(")) ");
				
				results.add(buf.toString());
			}
		} catch (Exception e) {
		}
    	
    	evalString = "(find-all-facts ((?a kupienieKonia)) TRUE)";
    	pv = agentEnv.eval(evalString);
    	try {
			for (int i = 0; i < pv.size(); i++) {
				PrimitiveValue tmpPv = pv.get(i);
				StringBuffer buf = new StringBuffer();
				
				buf.append("(kupienieKonia ")
				   .append("(idAgenta ").append(tmpPv.getFactSlot("idAgenta").toString()).append(") ")
				   .append("(idKonia ").append(tmpPv.getFactSlot("idKonia").toString()).append(")) ");
				
				results.add(buf.toString());
			}
		} catch (Exception e) {
		}
    	
    	evalString = "(find-all-facts ((?a akcjaOdpoczywanie)) TRUE)";
    	pv = agentEnv.eval(evalString);
    	try {
			for (int i = 0; i < pv.size(); i++) {
				PrimitiveValue tmpPv = pv.get(i);
				StringBuffer buf = new StringBuffer();
				
				buf.append("(akcjaOdpoczywanie ")
				   .append("(idAgenta ").append(tmpPv.getFactSlot("idAgenta").toString()).append(") ")
				   .append("(iteracjaKoniec ").append(tmpPv.getFactSlot("iteracjaKoniec").toString()).append(")) ");
				
				results.add(buf.toString());
			}
		} catch (Exception e) {
		}
    	
    	evalString = "(find-all-facts ((?a akcjaZetnijDrzewo)) TRUE)";
    	pv = agentEnv.eval(evalString);
    	try {
			for (int i = 0; i < pv.size(); i++) {
				PrimitiveValue tmpPv = pv.get(i);
				StringBuffer buf = new StringBuffer();
				
				buf.append("(akcjaZetnijDrzewo ")
				   .append("(idAgenta ").append(tmpPv.getFactSlot("idAgenta").toString()).append(")) ");
				
				results.add(buf.toString());
			}
		} catch (Exception e) {
		}

    	evalString = "(find-all-facts ((?a akcjaKupWoz)) TRUE)";
    	pv = agentEnv.eval(evalString);
    	try {
			for (int i = 0; i < pv.size(); i++) {
				PrimitiveValue tmpPv = pv.get(i);
				StringBuffer buf = new StringBuffer();
				
				buf.append("(akcjaKupWoz ")
				   .append("(idAgenta ").append(tmpPv.getFactSlot("idAgenta").toString()).append(") ")
				   .append("(idWozu ").append(tmpPv.getFactSlot("idWozu").toString()).append(")) ");
				
				results.add(buf.toString());
			}
		} catch (Exception e) {
		}
    	
    	evalString = "(find-all-facts ((?a akcjaKupSiekiere)) TRUE)";
    	pv = agentEnv.eval(evalString);
    	try {
			for (int i = 0; i < pv.size(); i++) {
				PrimitiveValue tmpPv = pv.get(i);
				StringBuffer buf = new StringBuffer();
				
				buf.append("(akcjaKupSiekiere ")
				   .append("(idAgenta ").append(tmpPv.getFactSlot("idAgenta").toString()).append(") ")
				   .append("(idSiekiery ").append(tmpPv.getFactSlot("idSiekiery").toString()).append(")) ");
				
				results.add(buf.toString());
			}
		} catch (Exception e) {
		}

    	evalString = "(find-all-facts ((?a akcjaSprzedajDrewno)) TRUE)";
    	pv = agentEnv.eval(evalString);
    	try {
			for (int i = 0; i < pv.size(); i++) {
				PrimitiveValue tmpPv = pv.get(i);
				StringBuffer buf = new StringBuffer();
				
				buf.append("(akcjaSprzedajDrewno ")
				   .append("(idAgenta ").append(tmpPv.getFactSlot("idAgenta").toString()).append(")) ");
				
				results.add(buf.toString());
			}
		} catch (Exception e) {
		}
    	
    	evalString = "(find-all-facts ((?a akcjaWezPaczke)) TRUE)";
    	pv = agentEnv.eval(evalString);
    	try {
			for (int i = 0; i < pv.size(); i++) {
				PrimitiveValue tmpPv = pv.get(i);
				StringBuffer buf = new StringBuffer();
				
				buf.append("(akcjaWezPaczke ")
				   .append("(idAgenta ").append(tmpPv.getFactSlot("idAgenta").toString()).append(") ")
				   .append("(idPaczki ").append(tmpPv.getFactSlot("idPaczki").toString()).append(")) ");
				
				results.add(buf.toString());
			}
		} catch (Exception e) {
		}
    	
    	evalString = "(find-all-facts ((?a akcjaZobaczenieBlokady)) TRUE)";
    	pv = agentEnv.eval(evalString);
    	try {
			for (int i = 0; i < pv.size(); i++) {
				PrimitiveValue tmpPv = pv.get(i);
				StringBuffer buf = new StringBuffer();
				
				buf.append("(akcjaZobaczenieBlokady ")
				   .append("(idAgenta ").append(tmpPv.getFactSlot("idAgenta").toString()).append(") ")
				   .append("(idBlokady ").append(tmpPv.getFactSlot("idBlokady").toString()).append(") ")
				   .append("(podjetaAkcja ").append(tmpPv.getFactSlot("podjetaAkcja").toString()).append(")) ");
				
				results.add(buf.toString());
			}
		} catch (Exception e) {
		}
    			
    	evalString = "(find-all-facts ((?p poslaniec)) TRUE)";
    	pv = agentEnv.eval(evalString);
    	try {
				Courier courier = new Courier();
				courier.loadFromClips(pv.get(0));
				results.add(courier.toString());
				
		} catch (Exception e) {
		
		}
    	
    	evalString = "(find-all-facts ((?d drwal)) TRUE)";
    	pv = agentEnv.eval(evalString);
    	try {
				Woodman woodman = new Woodman();
				woodman.loadFromClips(pv.get(0));
				results.add(woodman.toString());
				
		} catch (Exception e) {
		
		}
    	
    	return results;
    }
    
    public int getFieldOfView() {
        return this.fieldOfView;
    }
    
    public Agent setFieldOfView(int fieldOfView) {
        this.fieldOfView = fieldOfView;
        
        return this;
    }
    
    public int getVelocity() {
        return this.velocity;
    }

    public Agent setVelocity(int velocity) {
        this.velocity = velocity;
        
        return this;
    }
    
    public int getEnergy() {
        return this.energy;
    }
    
    public Agent setEnergy(int energy) {
        if(energy > 100) {
            energy = 100;
        }       
        
        this.energy = energy;
        
        return this;
    }

    public int getEnergyLoss() {
        return this.energyLoss;
    }
    
    public Agent setEnergyLoss(int energyLoss) {
        this.energyLoss = energyLoss;
        
        return this;
    }
    
    public int getEnergyRecovery() {
        return this.energyRecovery;
    }
    
    public Agent setEnergyRecovery(int energyRecovery) {
        this.energyRecovery = energyRecovery;
        
        return this;
    }
    
    public int getGold() {
        return this.gold;
    }
    
    public Agent setGold(int gold) {
        this.gold = gold;
        
        return this;
    }
    
    /**
     * Zużycie energii podczas ruchu. Zwraca TRUE jeżeli agent może się poruszać i FALSE gdy nie.
     * @return boolean
     */
    public boolean run() {
        this.setEnergy(this.getEnergy()-this.getEnergyLoss());
        
        return true;
    }
    
	public String getId() {
		return id;
	}

	public void setId(String _id) {
		this.id = _id;
	}

	public String getPathToClipsFile() {
		return pathToClipsFile;
	}

	public void setPathToClipsFile(String pathToClipsFile) {
		this.pathToClipsFile = pathToClipsFile;
	}

	public MapFrame getMapFrame() {
		return mapFrame;
	}

	public void setMapFrame(MapFrame mapFrame) {
		this.mapFrame = mapFrame;
	}
}
