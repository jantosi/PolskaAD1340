package polskaad1340.window;

import javax.swing.JMenuItem;

import world.Town;

public class TownMenuItem extends JMenuItem {

	private static final long serialVersionUID = 1773413221774184809L;

	private Town town;
	
	public TownMenuItem(Town town) {
		super(town.getId());
		this.town = town;
	}

	public Town getTown() {
		return town;
	}

	public void setTown(Town town) {
		this.town = town;
	}
	
}
