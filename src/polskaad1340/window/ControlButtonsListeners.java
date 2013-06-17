package polskaad1340.window;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import polskaad1340.Inference;
import agents.Agent;

public class ControlButtonsListeners {

	private OknoMapy om;
	private Inference inference;
	
	private int agentsWhoInferedNum;
	
	private class BtnNextIterListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			//konczymy zaczeta runde
			inference.realizeRound();
			
			//rozpoczynamy nowa runde swiata
			inference.performWorldInference();
			
			om.getTextFieldIter().setText(String.valueOf(inference.getActualIteration()));
			
			//wnioskuje rowniez pierwszy agent
			Agent agent = inference.getWorld().getAgents().get(0);
			
			try {
				om.deleteHighlightedBorders();
				om.highlightVisibleFrames(agent);
				om.focusOnAgent(agent);
				inference.performAgentInference(agent);
			} catch (Exception e1) {
				e1.printStackTrace();
			}
			om.getTextFieldAgent().setText(agent.getId());
			
			inference.getAgentsWhoDidntInfer()[0] = -1;
			
			agentsWhoInferedNum = 1;
		}
	}
	
	private class BtnNextAgentListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			if (agentsWhoInferedNum < inference.getWorld().getAgents().size()) {
				Agent agent = inference.getWorld().getAgents().get(agentsWhoInferedNum);
				
				try {
					om.deleteHighlightedBorders();
					om.highlightVisibleFrames(agent);
					om.focusOnAgent(agent);
					inference.performAgentInference(agent);
				} catch (Exception e1) {
					e1.printStackTrace();
				}
				
				om.getTextFieldAgent().setText(agent.getId());
				
				inference.getAgentsWhoDidntInfer()[agentsWhoInferedNum] = -1;
				agentsWhoInferedNum += 1;
			}
		}
	}
	
	public ControlButtonsListeners(OknoMapy om, Inference inference) {
		this.om = om;
		this.inference = inference;
		
		//rozpoczynamy nowa runde swiata
		inference.performWorldInference();
		
		this.om.getTextFieldIter().setText(String.valueOf(inference.getActualIteration()));
		
		//wnioskuje rowniez pierwszy agent
		Agent agent = inference.getWorld().getAgents().get(0);
		
		try {
			this.om.deleteHighlightedBorders();
			this.om.highlightVisibleFrames(agent);
			this.om.focusOnAgent(agent);
			this.inference.performAgentInference(agent);
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		this.om.getTextFieldAgent().setText(agent.getId());
		this.inference.getAgentsWhoDidntInfer()[0] = -1;
		
		this.agentsWhoInferedNum = 1;
	}
	
	public void activateListeners() {
		this.om.getBtnNextIter().addActionListener(new BtnNextIterListener());
		this.om.getBtnNextAgent().addActionListener(new BtnNextAgentListener());
	}
}
