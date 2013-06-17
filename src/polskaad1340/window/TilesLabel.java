package polskaad1340.window;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import javax.swing.ImageIcon;
import javax.swing.JLabel;

/**
 * Klasa ktora roszerza JLabel, umozliwiajac umieszczenie na jednej kratce kilku agentow
 * @author Kamil
 *
 */
public class TilesLabel extends JLabel{
	private static final long serialVersionUID = -6944337505630590290L;
	
	private ArrayList<ObiektPierwszegoPlanu> foregroundObjects;
	
	public TilesLabel(ObiektPierwszegoPlanu opp) {
		this.foregroundObjects = new ArrayList<ObiektPierwszegoPlanu>();
		this.foregroundObjects.add(opp);
		this.updateIcon();
	}
	public void removeObject(ObiektPierwszegoPlanu opp) {
		this.foregroundObjects.remove(opp);
		this.updateIcon();
	}
	
	public void addObject(ObiektPierwszegoPlanu opp) {
		if (!this.foregroundObjects.contains(opp)) {
			this.foregroundObjects.add(opp);
			this.updateIcon();
		}
	}
	
	//informacja po klikniecu na kafelek
	public String getDescription() {
		StringBuffer buf = new StringBuffer();
		for (int i = 0; i < this.foregroundObjects.size(); i++) {
			ObiektPierwszegoPlanu opp = this.foregroundObjects.get(i);
			buf.append(InformacjeOSwiecie.getOpisKafelka(opp.tile.getBackground().getRGB() & 0xFFFFFF));
			
			if (i < this.foregroundObjects.size() - 1) {
				buf.append(", ");
			}
		}
		return buf.toString();
	}
	
	private void updateIcon() {
		if (!this.foregroundObjects.isEmpty()) {
			ImageIcon firstTileIcon = (ImageIcon) this.foregroundObjects.get(0).tile.getIcon();
			BufferedImage combinedImage = new BufferedImage(firstTileIcon.getIconWidth(), firstTileIcon.getIconHeight(), BufferedImage.TYPE_INT_ARGB);
			Graphics g = combinedImage.getGraphics();

			for (int i = 0; i < this.foregroundObjects.size(); i++) {
				ImageIcon actualTileIcon = (ImageIcon) this.foregroundObjects.get(i).tile.getIcon();
				g.drawImage(actualTileIcon.getImage(), 0, 0, null);
			}

			ImageIcon newTileIcon = new ImageIcon(combinedImage);
			this.setIcon(newTileIcon);
			this.setSize(newTileIcon.getIconWidth(), newTileIcon.getIconHeight());
		}
	}

	public ArrayList<ObiektPierwszegoPlanu> getForegroundObjects() {
		return foregroundObjects;
	}

	public void setForegroundObjects(ArrayList<ObiektPierwszegoPlanu> foregroundObjects) {
		this.foregroundObjects = foregroundObjects;
	}
}
