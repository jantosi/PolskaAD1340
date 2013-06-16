package polskaad1340;

import java.util.ArrayList;

import world.World;
import clips.ClipsEnvironment;
import agents.Agent;

public class Inference {
	
	private ClipsEnvironment clipsEnv;
	private World world;
	private ArrayList<String> agentsInferenceResults;
	
	private int lastPerformedIter;
	private int actualIteration;
	private ArrayList<Agent> agentsWhoDidntInfer;
	
	public Inference(ClipsEnvironment clipsEnv, World world) {
		this.clipsEnv = clipsEnv;
		this.world = world;
		this.agentsInferenceResults = new ArrayList<String>();
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
		System.out.println("</wnioskowanie swiata>");
		//clipsEnv.displayWorldFacts();
		this.world.loadFromClips();
		
		this.agentsWhoDidntInfer = this.world.getAgents();
		this.actualIteration += 1;
	}

	public void performAgentInference(Agent actualAgent) throws Exception {
		ArrayList<Object> visibleObjects = this.world.getVisibleWorld(actualAgent.getId());

		this.clipsEnv.getAgentEnv().reset();
		this.clipsEnv.getAgentEnv().load(actualAgent.getPathToClipsFile());

		for (int k = 0; k < visibleObjects.size(); k++) {
			this.clipsEnv.getAgentEnv().assertString(visibleObjects.get(k).toString());
		}

		// dany agent wnioskuje
		//clipsEnv.displayAgentFacts();
		System.out.println("<wnioskowanie agenta " + actualAgent.getId() + " >");
		this.clipsEnv.getAgentEnv().run();
		System.out.println("</wnioskowanie agenta " + actualAgent.getId() + " >");
		

		ArrayList<String> agentInferenceResultsTmp = actualAgent.getInferenceResults(clipsEnv.getAgentEnv());
		// wywnioskowane przez agenta fakty dodajemy do wszystkich
		// faktow, ktore po wnioskowaniu wszystkich agentow zosatna przekazane do swiata
		for (String agentInferenceResult : agentInferenceResultsTmp) {
			this.agentsInferenceResults.add(agentInferenceResult);
		}
	}
	
	public void realizeRound() {
		System.out.println("|ITERACJA " + (this.lastPerformedIter + 1) + " |");
		
		if (this.actualIteration <= this.lastPerformedIter) {
			performWorldInference();
		}
		
		for (Agent agent : this.agentsWhoDidntInfer) {
			try {
				
				performAgentInference(agent);
				
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		System.out.println("|KONIEC ITERACJI " + (this.lastPerformedIter + 1) + " |\n");
		this.lastPerformedIter = this.actualIteration;
	}
	
}
