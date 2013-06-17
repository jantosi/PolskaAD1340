/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package polskaad1340.window;

import javax.swing.JLabel;

/**
 *
 * @author Kuba
 */
public class ObiektPierwszegoPlanu {

    int OknoMapyListHandler = -1;
    OknoMapy om;
    public int x,y;
    JLabel tile;
    private String id;
    
    public ObiektPierwszegoPlanu(int x, int y, String id) {
        this.x = x;
        this.y = y;
        this.id = id;
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
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (x != other.x)
			return false;
		if (y != other.y)
			return false;
		return true;
	}

    

}
