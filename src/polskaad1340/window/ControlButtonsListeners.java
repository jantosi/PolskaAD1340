package polskaad1340.window;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import polskaad1340.Inference;

public class ControlButtonsListeners {

	private OknoMapy om;
	private Inference inference;
	
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
	
	public ControlButtonsListeners(OknoMapy om, Inference inference) {
		this.om = om;
		this.inference = inference;
		
		this.om.getBtnNextIter().addActionListener(new BtnNextIterListener());
		this.om.getBtnNextAgent().addActionListener(new BtnNextAgentListener());
	}
}
