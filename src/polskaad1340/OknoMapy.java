/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package polskaad1340;

import java.awt.AWTEvent;
import java.awt.Dimension;
import java.awt.Event;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.util.ArrayList;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;
import javax.swing.UIManager;
import javax.swing.border.BevelBorder;

/**
 *
 * @author Kuba
 */
public final class OknoMapy extends javax.swing.JFrame {

    public int tileSize;
    private ArrayList<ArrayList<JLabel>> foregroundTileGrid, backgroundTileGrid;
    private int defaultBgTileID = 72;
    private boolean isResizedNow = false;

    public JLabel tileFromNumber(int num) {
        String path = "/images/" + num + ".png";

        ImageIcon ii = new ImageIcon(getClass().getResource(path));

        JLabel jl = new JLabel(ii);
        jl.setSize(tileSize, tileSize);
        return jl;
    }

    public OknoMapy() {
        /* Set native look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            UIManager.setLookAndFeel(
            UIManager.getSystemLookAndFeelClassName());
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(OknoMapy.class.getName()).log(java.util.logging.Level.WARNING, null, ex);
        }
        //</editor-fold>
        
        setScrollbars(false);
        initComponents();
        this.tileSize = 32;
        addTileGridToWindow(createTileGrid(100, defaultBgTileID), overallBackgroundPanel);

        drawAllTiles();
        bulkPanel.setPreferredSize(this.getSize());
    }

    public JLabel getTileAt(ArrayList<ArrayList<JLabel>> tileGrid, int tiledX, int tiledY) {
        return (tileGrid.get(tiledY)).get(tiledX);
    }

    public void setTileAt(ArrayList<ArrayList<JLabel>> tileGrid, int tiledX, int tiledY, JLabel jl) {
        tileGrid.get(tiledY).get(tiledX).setIcon(jl.getIcon());
    }

    public void drawAllTiles() {
        if (foregroundTileGrid != null) {
            addTileGridToWindow(foregroundTileGrid, foregroundPanel);
        }

        if (backgroundTileGrid != null) {
            addTileGridToWindow(backgroundTileGrid, backgroundPanel);
        }
    }

    public void importForegroundTileGrid(ArrayList<ArrayList<Integer>> arr) {
        ArrayList<ArrayList<JLabel>> tileGrid = new ArrayList<>(arr.size());
        for (int i = 0; i < arr.size(); i++) {

            ArrayList<JLabel> tileGridRow = new ArrayList<>(arr.size());

            for (int j = 0; j < arr.size(); j++) {

                int num = arr.get(i).get(j);

                JLabel jl = tileFromNumber(num);
                tileGridRow.add(jl);
            }
            tileGrid.add(tileGridRow);
        }

        this.foregroundTileGrid = tileGrid;
    }

    public void importBackgroundTileGrid(ArrayList<ArrayList<Integer>> arr) {
        ArrayList<ArrayList<JLabel>> tileGrid = new ArrayList<>(arr.size());
        for (int i = 0; i < arr.size(); i++) {

            ArrayList<JLabel> tileGridRow = new ArrayList<>(arr.size());

            for (int j = 0; j < arr.size(); j++) {

                int num = arr.get(i).get(j);

                JLabel jl = tileFromNumber(num);
                tileGridRow.add(jl);
            }
            tileGrid.add(tileGridRow);
        }

        this.backgroundTileGrid = tileGrid;
    }

    public ArrayList<ArrayList<JLabel>> createTileGrid(int size, int defaultTileNo) {
        ArrayList<ArrayList<JLabel>> tileGrid = new ArrayList<>(size);
        for (int i = 0; i < size; i++) {

            ArrayList<JLabel> tileGridRow = new ArrayList<>(size);

            for (int j = 0; j < size; j++) {

                int num = defaultTileNo;
                if (defaultTileNo == -1) {
                    num = i * size + j;
                } //wszystkie obrazy po kolei

                JLabel jl = tileFromNumber(num);
                tileGridRow.add(jl);
            }
            tileGrid.add(tileGridRow);
        }
        return tileGrid;
    }

    private void addTileGridToWindow(ArrayList<ArrayList<JLabel>> tileGrid, JPanel targetPanel) {
        int size = tileGrid.size();

        GridLayout gl = (GridLayout) targetPanel.getLayout();
        if (tileGrid.size() > 0) {
            gl.setRows(tileGrid.size());
            gl.setColumns(tileGrid.size());
            targetPanel.setSize(size * tileSize, size * tileSize);
        }

        for (int i = 0; i < tileGrid.size(); i++) {
            ArrayList<JLabel> arrayList = tileGrid.get(i);
            for (int j = 0; j < arrayList.size(); j++) {
                targetPanel.add(arrayList.get(j));
            }
        }
    }

    public ArrayList<ArrayList<JLabel>> getForegroundTileGrid() {
        return foregroundTileGrid;
    }

    public ArrayList<ArrayList<JLabel>> getBackgroundTileGrid() {
        return backgroundTileGrid;
    }

    public void setForegroundTileGrid(ArrayList<ArrayList<JLabel>> foregroundTileGrid) {
        this.foregroundTileGrid = foregroundTileGrid;
    }

    public void setScrollbars(boolean scrollbarsShown) {
        //TODO
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        bulkPanel = new javax.swing.JPanel();
        foregroundPanel = new javax.swing.JPanel();
        backgroundPanel = new javax.swing.JPanel();
        overallBackgroundPanel = new javax.swing.JPanel();
        jMenuBar1 = new javax.swing.JMenuBar();
        jMenu3 = new javax.swing.JMenu();
        jMenuItem1 = new javax.swing.JMenuItem();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Polska, AD 1340");
        setBackground(new java.awt.Color(102, 102, 102));
        setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        setMinimumSize(new java.awt.Dimension(800, 600));
        addComponentListener(new java.awt.event.ComponentAdapter() {
            public void componentResized(java.awt.event.ComponentEvent evt) {
                formComponentResized(evt);
            }
        });
        addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                formKeyReleased(evt);
            }
        });
        getContentPane().setLayout(null);

        bulkPanel.setCursor(new java.awt.Cursor(java.awt.Cursor.CROSSHAIR_CURSOR));
        bulkPanel.setLayout(null);

        foregroundPanel.setOpaque(false);
        foregroundPanel.setLayout(new java.awt.GridLayout(1, 0));
        bulkPanel.add(foregroundPanel);
        foregroundPanel.setBounds(0, 0, 750, 400);

        backgroundPanel.setForeground(new java.awt.Color(240, 240, 240));
        backgroundPanel.setOpaque(false);
        backgroundPanel.setLayout(new java.awt.GridLayout(1, 0));
        bulkPanel.add(backgroundPanel);
        backgroundPanel.setBounds(0, 0, 710, 410);

        overallBackgroundPanel.setForeground(new java.awt.Color(240, 240, 240));
        overallBackgroundPanel.setLayout(new java.awt.GridLayout(1, 0));
        bulkPanel.add(overallBackgroundPanel);
        overallBackgroundPanel.setBounds(0, 0, 620, 410);

        jScrollPane1.setViewportView(bulkPanel);

        getContentPane().add(jScrollPane1);
        jScrollPane1.setBounds(0, 0, 790, 470);

        jMenu3.setText("Widok");

        jMenuItem1.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_ENTER, java.awt.event.InputEvent.ALT_MASK));
        jMenuItem1.setText("Dopasuj do ekranu");
        jMenuItem1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem1ActionPerformed(evt);
            }
        });
        jMenu3.add(jMenuItem1);

        jMenuBar1.add(jMenu3);

        setJMenuBar(jMenuBar1);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    public void resizeContentsTo(Dimension d)
    {
        //        d.height -= 37;
//        d.width -= 15; //corrections for window borders

        jScrollPane1.setPreferredSize(d);
        jScrollPane1.setSize(d);

        if (this.backgroundTileGrid != null) {
            bulkPanel.setPreferredSize(new Dimension(this.tileSize * this.backgroundTileGrid.size(), this.tileSize * this.backgroundTileGrid.size()));
        }
        
        if(jScrollPane1.getHorizontalScrollBar()!=null) {
            jScrollPane1.getHorizontalScrollBar().setUnitIncrement(tileSize/2);
        }
        if(jScrollPane1.getVerticalScrollBar()!=null) {
            jScrollPane1.getVerticalScrollBar().setUnitIncrement(tileSize/2);
        }
    }
    
    private void formComponentResized(java.awt.event.ComponentEvent evt) {//GEN-FIRST:event_formComponentResized

        Dimension d = this.getContentPane().getSize();
        this.resizeContentsTo(d);



    }//GEN-LAST:event_formComponentResized

    private void formKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_formKeyReleased
//        demo eventu       
//        int num = evt.getExtendedKeyCode();
//        System.out.println(num);
//        this.setTileAt(foregroundTileGrid, num / 10, num / 10, this.tileFromNumber(num));
//        this.repaint();
    }//GEN-LAST:event_formKeyReleased

    private void jMenuItem1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem1ActionPerformed
        Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
        d.height*=0.8; d.width*=0.8;
        
        this.setSize(d);
        this.resizeContentsTo(d);
    }//GEN-LAST:event_jMenuItem1ActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel backgroundPanel;
    private javax.swing.JPanel bulkPanel;
    private javax.swing.JPanel foregroundPanel;
    private javax.swing.JMenu jMenu3;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JMenuItem jMenuItem1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JPanel overallBackgroundPanel;
    // End of variables declaration//GEN-END:variables
}
