package polskaad1340.window;

import javax.swing.JMenuItem;

import agents.Agent;

public class AgentMenuItem extends JMenuItem{

	private static final long serialVersionUID = -2579328786697418946L;
	private Agent agent;
	
	public AgentMenuItem(String name) {
		super(name);
	}
	
	public AgentMenuItem(Agent agent) {
		super(agent.getId());
		this.agent = agent;
	}
	
	public Agent getAgent() {
		return agent;
	}
	public void setAgent(Agent agent) {
		this.agent = agent;
	}
	
	
	
}
