package polskaad1340.window;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import world.World;
import clips.ClipsEnvironment;

public class ControlButtonsListeners {

	private OknoMapy om;
	private ClipsEnvironment clipsEnv;
	private World world;
	
	private class BtnNextIterListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			
			om.getTextFieldAgent().setText("aa");
		}
	}
	
	private class BtnNextAgentListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			
		}

	}
	
	public ControlButtonsListeners(OknoMapy om, ClipsEnvironment clipsEnv, World world) {
		this.om = om;
		this.clipsEnv = clipsEnv;
		this.world = world;
		
		this.om.getBtnNextIter().addActionListener(new BtnNextIterListener());
		this.om.getBtnNextAgent().addActionListener(new BtnNextAgentListener());
	}
}
