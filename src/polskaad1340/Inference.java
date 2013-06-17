package polskaad1340;

import java.util.ArrayList;

import polskaad1340.window.OknoMapy;
import world.World;
import agents.Agent;
import clips.ClipsEnvironment;

public class Inference {
	
	private ClipsEnvironment clipsEnv;
	private World world;
	private OknoMapy om;
	private ArrayList<String> agentsInferenceResults;
	
	//ostatnia pelna runda czyli swiat i wszyscy agenci
	private int lastPerformedIter;
	//aktualna runda swiata
	private int actualIteration;
	
	//to jest po to, aby jak klikniemy przycisk nastepny agent
	//a pozniej nastepna iteracja, to zebysmy wiedziele, ktorzy agenci musza
	//w danej rundzie wywnioskowac przed przejsciem do nastepnej rundy
	private int[] agentsWhoDidntInfer;
	
	public Inference(ClipsEnvironment clipsEnv, World world, OknoMapy om) {
		this.clipsEnv = clipsEnv;
		this.world = world;
		this.om = om;
		this.agentsInferenceResults = new ArrayList<String>();
		
		//pierwsza runda jest inicjalizacyjna
		System.out.println("<RUNDA INICJALIZACYJNA - NIE MA WPLYWU NA ROZGRYWKE>");
		realizeRound();
		System.out.println("</RUNDA INICJALIZACYJNA>\n");
		
		this.lastPerformedIter = 0;
		this.actualIteration = 0;
	}
	
	public void performWorldInference() {
		this.clipsEnv.getWorldEnv().reset();
		
		// do swiata przekazujemy obiekty swiata oraz wywnioskowane
		// przez agentow fakty
		this.world.saveToClips();
		for (int k = 0; k < this.agentsInferenceResults.size(); k++) {
			clipsEnv.getWorldEnv().assertString(this.agentsInferenceResults.get(k));
		}
		this.clipsEnv.getWorldEnv().assertString("(iteracja " + (this.actualIteration + 1) + ")");
		
		this.agentsInferenceResults = new ArrayList<String>();

		System.out.println("<wnioskowanie swiata>");
		this.clipsEnv.getWorldEnv().run();
		
		String worldInfRes = this.clipsEnv.getWorldInferenceResults("src/clips/results.txt");
		System.out.println(worldInfRes);
		
		om.setScrollFocusOn(10, 10);
		this.om.displayInferenceResults(20 * this.om.tileSize, 20 * this.om.tileSize
				, "Wynik wnioskowania swiata", worldInfRes, "world");
		
		System.out.println("fakty:");
		//clipsEnv.displayWorldFacts();
		System.out.println("</wnioskowanie swiata>");
		
		ArrayList<Agent> agentsBeforeMoving = new ArrayList<Agent>();
		for (Agent agentTmp : this.world.getAgents()) {
			agentsBeforeMoving.add(agentTmp);
		}
		
		this.world.loadFromClips();
		
		this.agentsWhoDidntInfer = new int[this.world.getAgents().size()];
		for (int i = 0; i < this.agentsWhoDidntInfer.length; i++) {
			this.agentsWhoDidntInfer[i] = i;
		}
		
		this.actualIteration += 1;
		
		this.om.drawAllTiles();
	}

	public void performAgentInference(Agent actualAgent) throws Exception {
		ArrayList<Object> visibleObjects = this.world.getVisibleWorld(actualAgent.getId());

		this.clipsEnv.getAgentEnv().reset();
		this.clipsEnv.getAgentEnv().load(actualAgent.getPathToClipsFile());

		for (int k = 0; k < visibleObjects.size(); k++) {
			this.clipsEnv.getAgentEnv().assertString(visibleObjects.get(k).toString());
		}

		// dany agent wnioskuje
		System.out.println("<wnioskowanie agenta " + actualAgent.getId() + " >");
		this.clipsEnv.getAgentEnv().run();
		
		String agentInfRes = this.clipsEnv.getWorldInferenceResults("src/clips/agentResults.txt");
		System.out.println(agentInfRes);
		
		om.displayInferenceResults(actualAgent.getMapFrame().getX() * om.tileSize + 25, actualAgent.getMapFrame().getY() * om.tileSize + 10
				,"Wynik wnioskowania agenta " + actualAgent.getId(), agentInfRes, "agent");
		
		System.out.println("fakty:");
		clipsEnv.displayAgentFacts();
		System.out.println("</wnioskowanie agenta " + actualAgent.getId() + " >");
		

		ArrayList<String> agentInferenceResultsTmp = actualAgent.getInferenceResults(clipsEnv.getAgentEnv());
		// wywnioskowane przez agenta fakty dodajemy do wszystkich
		// faktow, ktore po wnioskowaniu wszystkich agentow zosatna przekazane do swiata
		for (String agentInferenceResult : agentInferenceResultsTmp) {
			this.agentsInferenceResults.add(agentInferenceResult);
		}
		
		//zerujemy kafelek na ktorym stal agent
		this.om.getForegroundTileGrid().get(actualAgent.getOpp().y).set(actualAgent.getOpp().x, this.om.tileFromNumber(0));
	}
	
	public void realizeRound() {
		System.out.println("|ITERACJA " + (this.lastPerformedIter + 1) + " |");
		
		if (this.actualIteration <= this.lastPerformedIter) {
			performWorldInference();
		}
		
		for (int agent : this.agentsWhoDidntInfer) {
			try {
				if (agent != -1) {
					performAgentInference(this.world.getAgents().get(agent));
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		System.out.println("|KONIEC ITERACJI " + (this.lastPerformedIter + 1) + " |\n");
		this.lastPerformedIter = this.actualIteration;
	}

	public ClipsEnvironment getClipsEnv() {
		return clipsEnv;
	}

	public void setClipsEnv(ClipsEnvironment clipsEnv) {
		this.clipsEnv = clipsEnv;
	}

	public World getWorld() {
		return world;
	}

	public void setWorld(World world) {
		this.world = world;
	}

	public int getLastPerformedIter() {
		return lastPerformedIter;
	}

	public void setLastPerformedIter(int lastPerformedIter) {
		this.lastPerformedIter = lastPerformedIter;
	}

	public int getActualIteration() {
		return actualIteration;
	}

	public void setActualIteration(int actualIteration) {
		this.actualIteration = actualIteration;
	}

	public int[] getAgentsWhoDidntInfer() {
		return agentsWhoDidntInfer;
	}

	public void setAgentsWhoDidntInfer(int[] agentsWhoDidntInfer) {
		this.agentsWhoDidntInfer = agentsWhoDidntInfer;
	}

	public OknoMapy getOm() {
		return om;
	}

	public void setOm(OknoMapy om) {
		this.om = om;
	}
	
}
