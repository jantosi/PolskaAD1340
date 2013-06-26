package polskaad1340.window;

import items.Item;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JScrollPane;
import javax.swing.border.LineBorder;

import polskaad1340.Inference;
import world.Bandits;
import world.Blockade;
import world.Cataclysm;
import world.MapFrame;
import world.Town;
import agents.Agent;

public class ControlButtonsListeners {

	private OknoMapy om;
	private Inference inference;
	
	private int agentsWhoInferedNum;
	
	private boolean blockades;
	private boolean blockadesChange;
	
	private boolean cataclysms;
	
	private boolean bandits;
	
	private boolean changePrices;
	
	private MapFrame heighlitedTown;
	
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
			
			//usuwamy poprzednie kleski z mapy
			for (Cataclysm cataclysm : inference.getWorld().getCataclysms()) {
				om.getForegroundTileGrid().get(cataclysm.getOpp().y).set(cataclysm.getOpp().x, om.tileFromNumber(0));
			}
			
			if (cataclysms) {
				inference.getWorld().randomCataclysms();
			} else {
				inference.getWorld().setCataclysms(new ArrayList<Cataclysm>());
			}

			//usuwamy poprzednich bandydtow z mapy
			for (Bandits bandit : inference.getWorld().getBandits()) {
				om.getForegroundTileGrid().get(bandit.getOpp().y).set(bandit.getOpp().x, om.tileFromNumber(0));
			}
			if (bandits) {
				inference.getWorld().randomBandits();
			} else {
				inference.getWorld().setBandits(new ArrayList<Bandits>());
			}
			
			//zmiana cen w grodzie
			if (changePrices) {
				changePrices = false;
				inference.getWorld().changeItemPrices();
				om.getLblPricesChanged().setVisible(true);
			} else {
				om.getLblPricesChanged().setVisible(false);
			}
			
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
	
	private class BtnChangePricesListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			changePrices = true;
		}
		
	}
	
	private class MenuBrowseListener extends MouseAdapter {

		@Override
		public void mouseEntered(MouseEvent e) {
			om.getMnBrowseAgents().removeAll();
			for (Agent agent : inference.getWorld().getAgents()) {
				JMenuItem newMenuItem = new AgentMenuItem(agent);
				newMenuItem.addActionListener(new MenuBrowseAgentsListener());
				
				om.getMnBrowseAgents().add(newMenuItem);
			}
			
			om.getMnBrowseTowns().removeAll();
			Set<String> visitedTowns = new HashSet<String>();
			for (Town town : inference.getWorld().getTowns()) {
				if (!visitedTowns.contains(town.getId())) {
					visitedTowns.add(town.getId());
					JMenuItem newMenuItem = new TownMenuItem(town);
					newMenuItem.addActionListener(new MenuBrowseTownsListener());
					
					om.getMnBrowseTowns().add(newMenuItem);
				}
			}
		}
		
	}
	
	private class MenuBrowseAgentsListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			if (heighlitedTown != null) {
				om.getBackgroundTileGrid().get(heighlitedTown.getY()).get(heighlitedTown.getX()).setBorder(null);
				heighlitedTown = null;
			}
			
			om.getBrowseDetailFrame().setVisible(false);
			om.getBrowseDetailFrame().getContentPane().removeAll();
			
			om.getBrowseDetailFrame().setBounds(400, 100, 350, 350);
			
			JScrollPane scrollPanel = new JScrollPane();
			scrollPanel.getVerticalScrollBar().setUnitIncrement(8);
			om.getBrowseDetailFrame().getContentPane().add(scrollPanel);
			
			
			Agent agentTmp = ((AgentMenuItem)((JMenuItem)e.getSource())).getAgent();
			System.out.println(agentTmp);
			String tmp = agentTmp.toString().replaceAll(" \\) \\( ", "<br>");
			tmp = tmp.replaceAll("\\(\\w* \\( ", "");
			tmp = tmp.replaceAll(" \\) \\)", "");
			String[] tmp2 = tmp.split("<br>");

			StringBuffer buf = new StringBuffer();
			buf.append("<html>");
			for (int i = 0; i < tmp2.length; i++) {
				String[] splitedRow = tmp2[i].split(" ");
				buf.append(splitedRow[0]);
				buf.append("<font color=\"green\"> : ");
				for (int j = 1; j < splitedRow.length; j++) {
					buf.append(splitedRow[j]).append(" ");
				}
				buf.append("</font><br>");
			}
			buf.append("</html>");

			JLabel content = new JLabel(buf.toString());
			content.setFont(new Font("Tahoma", Font.PLAIN, 14));
			scrollPanel.setViewportView(content);
			
			om.getBrowseDetailFrame().setTitle("Szczegoly " + agentTmp.getId());
			om.getBrowseDetailFrame().setVisible(true);
			om.getBrowseDetailFrame().setClosable(true);
			om.getBrowseDetailFrame().validate();
		}
		
	}
	
	private class MenuBrowseTownsListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			if (heighlitedTown != null) {
				om.getBackgroundTileGrid().get(heighlitedTown.getY()).get(heighlitedTown.getX()).setBorder(null);
				heighlitedTown = null;
			}
			om.getBrowseDetailFrame().setVisible(false);
			om.getBrowseDetailFrame().getContentPane().removeAll();
			om.getBrowseDetailFrame().setBounds(300, 100, 400, 320);
			
			JScrollPane scrollPanel = new JScrollPane();
			scrollPanel.getVerticalScrollBar().setUnitIncrement(8);
			om.getBrowseDetailFrame().getContentPane().add(scrollPanel);
			
			Town townTmp = ((TownMenuItem)((JMenuItem)e.getSource())).getTown();
			StringBuffer buf = new StringBuffer();
			buf.append("<html>");
			buf.append(townTmp.toString()).append("<br><br>");
			for (Item item : townTmp.getItems()) {
				buf.append(item.toString()).append("<br>");
			}
			buf.append("</html>");
				
			JLabel content = new JLabel(buf.toString());
			scrollPanel.setViewportView(content);
			
			MapFrame mapFrame = inference.getWorld().getFrameById(townTmp.getMapFrame());
			om.setScrollFocusOn(mapFrame.getX(), mapFrame.getY());
			om.getBackgroundTileGrid().get(mapFrame.getY()).get(mapFrame.getX()).setBorder(new LineBorder(Color.YELLOW, 3, true));
			heighlitedTown = mapFrame;
			
			om.getBrowseDetailFrame().setTitle("Szczegoly " + townTmp.getId());
			om.getBrowseDetailFrame().setVisible(true);
			om.getBrowseDetailFrame().setClosable(true);
			om.getBrowseDetailFrame().validate();

		}
		
	}
	
	private class MenuShowWorldInferenceListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			om.getBrowseDetailFrame().setVisible(false);
			om.getBrowseDetailFrame().getContentPane().removeAll();
			om.getBrowseDetailFrame().setBounds(300, 100, 400, 320);
			
			JScrollPane scrollPanel = new JScrollPane();
			scrollPanel.getVerticalScrollBar().setUnitIncrement(8);
			om.getBrowseDetailFrame().getContentPane().add(scrollPanel);
			
			StringBuffer buf = new StringBuffer();
			buf.append("<html>");
			buf.append(inference.getLastWorldInferenceres().replaceAll("\n", "<br>"));
			buf.append("</html>");

			JLabel content = new JLabel(buf.toString());
			scrollPanel.setViewportView(content);
			
			om.getBrowseDetailFrame().setTitle("Ostatni wynik wnioskowania swiata");
			om.getBrowseDetailFrame().setVisible(true);
			om.getBrowseDetailFrame().setClosable(true);
			om.getBrowseDetailFrame().validate();
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
		
		this.heighlitedTown = null;
	}
	
	public void activateListeners() {
		this.om.getBtnNextIter().addActionListener(new BtnNextIterListener());
		this.om.getBtnNextAgent().addActionListener(new BtnNextAgentListener());
		this.om.getBtnBlockades().addActionListener(new BtnBlockadesListener());
		this.om.getBtnCataclysms().addActionListener(new BtnCataclysmsListener());
		this.om.getBtnBandits().addActionListener(new BtnBanditsListener());
		this.om.getBtnChangePrices().addActionListener(new BtnChangePricesListener());
		
		this.om.getMnBrowse().addMouseListener(new MenuBrowseListener());
		this.om.getMnBrowseAgents().addActionListener(new MenuBrowseAgentsListener());
		this.om.getMnBrowseTowns().addActionListener(new MenuBrowseTownsListener());
		this.om.getMnWorldInference().addActionListener(new MenuShowWorldInferenceListener());
	}
}
