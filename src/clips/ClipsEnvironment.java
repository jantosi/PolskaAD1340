package clips;

import CLIPSJNI.Environment;

public class ClipsEnvironment {

	private Environment worldEnv;
	
	public ClipsEnvironment() {
		this.worldEnv = new Environment();
		initialize();
	}
	
	private void initialize() {
		this.worldEnv.load("src/clips/swiat_szablony.clp");
		this.worldEnv.load("src/clips/swiat_logika.clp");
		this.worldEnv.load("src/clips/test.clp");
		this.worldEnv.reset();
	}
	
	public void displayFacts() {
		this.worldEnv.eval("(facts)");
	}
	public void displayRules() {
		this.worldEnv.eval("(rules)");
	}
	public Environment getWorldEnv() {
		return worldEnv;
	}
	public void setWorldEnv(Environment worldEnv) {
		this.worldEnv = worldEnv;
	}
}
