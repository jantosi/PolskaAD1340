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
			inference.realizeRound();
			om.getTextFieldIter().setText(String.valueOf(inference.getActualIteration()));
			agentsWhoInferedNum = 0;
		}
	}
	
	private class BtnNextAgentListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			if (agentsWhoInferedNum < inference.getWorld().getAgents().size()) {
				Agent agent = inference.getWorld().getAgents().get(agentsWhoInferedNum);
				
				try {
					inference.performAgentInference(agent);
				} catch (Exception e1) {
					e1.printStackTrace();
				}
				
				inference.getAgentsWhoDidntInfer()[agentsWhoInferedNum] = -1;
				agentsWhoInferedNum += 1;
			}
		}
	}
	
	public ControlButtonsListeners(OknoMapy om, Inference inference) {
		this.om = om;
		this.inference = inference;
	}
	
	public void activateListeners() {
		this.om.getBtnNextIter().addActionListener(new BtnNextIterListener());
		this.om.getBtnNextAgent().addActionListener(new BtnNextAgentListener());
	}
}
