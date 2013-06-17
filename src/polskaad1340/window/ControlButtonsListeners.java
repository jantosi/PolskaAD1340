package polskaad1340.window;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import polskaad1340.Inference;
import world.Bandits;
import world.Blockade;
import world.Cataclysm;
import agents.Agent;

public class ControlButtonsListeners {

	private OknoMapy om;
	private Inference inference;
	
	private int agentsWhoInferedNum;
	
	private boolean blockades;
	private boolean blockadesChange;
	
	private boolean cataclysms;
	
	private boolean bandits;
	
	private class BtnNextIterListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			om.deleteHighlightedBorders();
			//konczymy zaczeta runde
			inference.realizeRound();
			
			if (blockadesChange) {
				blockadesChange = false;
				if (blockades) {
					inference.getWorld().randomBlockades();
					om.displayBlockades(inference.getWorld().getBlockades());
				} else {
					om.hideBlockades(inference.getWorld().getBlockades());
					inference.getWorld().setBlockades(new ArrayList<Blockade>());
				}
			}
			
			
			if (cataclysms) {
				inference.getWorld().randomCataclysms();
			} else {
				inference.getWorld().setCataclysms(new ArrayList<Cataclysm>());
			}

			
			if (bandits) {
				inference.getWorld().randomBandits();
			} else {
				inference.getWorld().setBandits(new ArrayList<Bandits>());
			}
			System.out.println("size: " + inference.getWorld().getBandits().size());
			//rozpoczynamy nowa runde swiata
			inference.performWorldInference();
			
			om.getTextFieldIter().setText(String.valueOf(inference.getActualIteration()));
			
			om.getTextFieldAgent().setText("SWIAT");
			
			agentsWhoInferedNum = 0;
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
					om.setScrollFocusOn(agent.getMapFrame().getX(), agent.getMapFrame().getY());
					
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
	
	private class BtnCataclysmsListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			if (om.getBtnCataclysms().getText().equalsIgnoreCase("wylaczone")) {
				om.getBtnCataclysms().setText("WLACZONE");
				om.getBtnCataclysms().setForeground(new Color(0, 128, 0));
				cataclysms = true;
			} else {
				om.getBtnCataclysms().setText("WYLACZONE");
				om.getBtnCataclysms().setForeground(Color.RED);
				cataclysms = false;
			}
		}
	}
	
	private class BtnBlockadesListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			if (om.getBtnBlockades().getText().equalsIgnoreCase("wylaczone")) {
				om.getBtnBlockades().setText("WLACZONE");
				om.getBtnBlockades().setForeground(new Color(0, 128, 0));
				blockades = true;
				blockadesChange = true;
			} else {
				om.getBtnBlockades().setText("WYLACZONE");
				om.getBtnBlockades().setForeground(Color.RED);
				blockades = false;
				blockadesChange = true;
			}
		}
		
	}
	
	private class BtnBanditsListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			if (om.getBtnBandits().getText().equalsIgnoreCase("wylaczone")) {
				om.getBtnBandits().setText("WLACZONE");
				om.getBtnBandits().setForeground(new Color(0, 128, 0));
				bandits = true;
			} else {
				om.getBtnBandits().setText("WYLACZONE");
				om.getBtnBandits().setForeground(Color.RED);
				bandits = false;
			}
		}
		
	}
	
	public ControlButtonsListeners(OknoMapy om, Inference inference) {
		this.om = om;
		this.inference = inference;
		
		//rozpoczynamy nowa runde swiata
		inference.performWorldInference();
		
		this.om.getTextFieldIter().setText(String.valueOf(inference.getActualIteration()));
		om.getTextFieldAgent().setText("SWIAT");
		this.agentsWhoInferedNum = 0;
	}
	
	public void activateListeners() {
		this.om.getBtnNextIter().addActionListener(new BtnNextIterListener());
		this.om.getBtnNextAgent().addActionListener(new BtnNextAgentListener());
		this.om.getBtnBlockades().addActionListener(new BtnBlockadesListener());
		this.om.getBtnCataclysms().addActionListener(new BtnCataclysmsListener());
		this.om.getBtnBandits().addActionListener(new BtnBanditsListener());
	}
}
