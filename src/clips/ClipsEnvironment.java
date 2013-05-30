package clips;

import CLIPSJNI.Environment;

public class ClipsEnvironment {

	private Environment worldEnv;
	
	public ClipsEnvironment() {
		this.worldEnv = new Environment();
		initialize();
	}
	
	private void initialize() {
		this.worldEnv.load("src/logika_CLIPS/swiat_szablony.clp");
		this.worldEnv.load("src/logika_CLIPS/swiat_logika.clp");
		this.worldEnv.load("src/logika_CLIPS/test.clp");
		this.worldEnv.reset();
	}
	public Environment getWorldEnv() {
		return worldEnv;
	}
	public void setWorldEnv(Environment worldEnv) {
		this.worldEnv = worldEnv;
	}
}
