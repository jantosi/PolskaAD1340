package clips;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

import CLIPSJNI.Environment;

public class ClipsEnvironment {

	private Environment worldEnv;
	private Environment agentEnv;
	
	public ClipsEnvironment() {
		this.worldEnv = new Environment();
		this.agentEnv = new Environment();
		initializeWorldEnv();
		initializeAgentEnv();
	}
	
	private void initializeWorldEnv() {
		this.worldEnv.load("src/clips/swiat_szablony.clp");
		this.worldEnv.load("src/clips/swiat_logika.clp");
		//this.worldEnv.load("src/clips/test.clp");
		this.worldEnv.reset();
	}
	
	private void initializeAgentEnv() {
		this.agentEnv.load("src/clips/swiat_szablony.clp");
	}
	
	
	public String getWorldInferenceResults(String fileName) {
		StringBuffer buf = new StringBuffer();
		File inferenceResultFile = new File(fileName);
		Scanner scanner;
		
		buf.append("");
		try {
			scanner = new Scanner(inferenceResultFile);

			while (scanner.hasNextLine()) {
				buf.append(scanner.nextLine()).append("\n");
			}

			scanner.close();
			inferenceResultFile.delete();

		} catch (FileNotFoundException e) {

		}
		
		return buf.toString();
	}
	
	public void displayWorldFacts() {
		this.worldEnv.eval("(facts)");
	}
	public void displayWorldRules() {
		this.worldEnv.eval("(rules)");
	}
	public void displayAgentFacts() {
		this.agentEnv.eval("(facts)");
	}
	public void displayAgentRules() {
		this.agentEnv.eval("(rules)");
	}
	public Environment getWorldEnv() {
		return worldEnv;
	}
	public void setWorldEnv(Environment worldEnv) {
		this.worldEnv = worldEnv;
	}
	public Environment getAgentEnv() {
		return agentEnv;
	}
	public void setAgentEnv(Environment agentEnv) {
		this.agentEnv = agentEnv;
	}
}
