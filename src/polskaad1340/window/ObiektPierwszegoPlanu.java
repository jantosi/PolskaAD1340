/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package polskaad1340.window;

import java.awt.Point;

import javax.swing.JLabel;

/**
 *
 * @author Kuba
 */
public class ObiektPierwszegoPlanu {

    int OknoMapyListHandler = -1;
    OknoMapy om;
    int x,y;
    JLabel tile;
    
    private boolean isInOknoMapy()
    {
        return(om!=null && OknoMapyListHandler!=-1);
    }
    
    public void move(int dx, int dy)
    {
        if(isInOknoMapy())
        {
            Point nuCoords = new Point(x+dx, y+dy);
            int check = om.isValidCoords(nuCoords);
            if(check==1)
            {
                om.moveForegroundObject(this, dx, dy);
                
                this.x += dx;
                this.y += dy;
            }
            om.drawAllTiles();
            om.getForegroundPanel().repaint();
        }
        
    }
    
    public ObiektPierwszegoPlanu(int x, int y) {
        this.x = x; this.y = y;
    }

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ObiektPierwszegoPlanu other = (ObiektPierwszegoPlanu) obj;
		if (OknoMapyListHandler != other.OknoMapyListHandler)
			return false;
		if (om == null) {
			if (other.om != null)
				return false;
		} else if (!om.equals(other.om))
			return false;
		if (tile == null) {
			if (other.tile != null)
				return false;
		} else if (!tile.equals(other.tile))
			return false;
		if (x != other.x)
			return false;
		if (y != other.y)
			return false;
		return true;
	}
}
