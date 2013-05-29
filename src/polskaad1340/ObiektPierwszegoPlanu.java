/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package polskaad1340;

import java.awt.Point;

/**
 *
 * @author Kuba
 */
public class ObiektPierwszegoPlanu {

    int OknoMapyListHandler = -1;
    OknoMapy om;
    int x,y;
    
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
                om.moveForegroundObject(x, y, dx, dy);
                
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
    
}
