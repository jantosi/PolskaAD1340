/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package polskaad1340.window;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.plaf.basic.BasicBorders;

import agents.Agent;

/**
 *
 * @author Kuba
 */
public final class OknoMapy extends javax.swing.JFrame {

	private static final long serialVersionUID = 6686871012707857578L;
	
	public int tileSize;
    private ArrayList<ArrayList<JLabel>> foregroundTileGrid, backgroundTileGrid; //najpierw Y, potem X
    private int defaultBgTileID = 72;
    public BasicBorders.FieldBorder defaultBorder;
    private boolean isTileBordered = false;
    ArrayList<ObiektPierwszegoPlanu> foregroundList = new ArrayList<>();
    
    
    private javax.swing.JPanel backgroundPanel;
    private javax.swing.JPanel bulkPanel;
    private javax.swing.JPanel contextPanel;
    private javax.swing.JPanel foregroundPanel;
    private javax.swing.JPanel iconDisplayPanel;
    private javax.swing.JCheckBoxMenuItem jCheckBoxMenuItem1;
    private javax.swing.JMenu jMenu3;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JMenuItem jMenuItem1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JPanel overallBackgroundPanel;
    private javax.swing.JPanel tileInfoPanel;
    private javax.swing.JLabel tileInfoPanelLabel1;
    private JPanel controlPanel;
    private JButton btnNextIter;
    private JButton btnNextAgent;
    private JLabel lblIteracja;
    private JLabel lblAgent;
    private JTextField textFieldIter;
    private JTextField textFieldAgent;

    public int addObjectToForegroundList(ObiektPierwszegoPlanu opp) {
        int index = this.foregroundList.size();
        this.foregroundList.add(opp);

        opp.om = this;
        opp.OknoMapyListHandler = index;
        return index;
    }

    public void removeObjectFromForegroundList(ObiektPierwszegoPlanu opp) {
        opp.om = null;
        opp.OknoMapyListHandler = -1;
        this.foregroundList.remove(opp);
    }

    public int isValidCoords(Point p) {
        if (p.x >= foregroundTileGrid.size() || p.x < 0 || p.y >= foregroundTileGrid.get(0).size() || p.y < 0) {
            return -1; // out of bounds;
        } 
        
        return 1;
    }

    public void moveForegroundObject(ObiektPierwszegoPlanu opp, int dx, int dy) {
    	if (isValidCoords(new Point(opp.x + dx, opp.y + dy)) == -1) {//poza mapa
        	return;
        }
    	TilesLabel actual = (TilesLabel)this.foregroundTileGrid.get(opp.y).get(opp.x);
        JLabel target = this.foregroundTileGrid.get(opp.y + dy).get(opp.x + dx);
    	
    	if (this.foregroundTileGrid.get(opp.y + dy).get(opp.x + dx) instanceof TilesLabel) {
        	((TilesLabel)target).addObject(opp);
        	
        } else {
        	this.foregroundTileGrid.get(opp.y + dy).set(opp.x + dx, new TilesLabel(opp));
        }
    	
    	actual.removeObject(opp);
    	if (actual.getForegroundObjects().isEmpty()) {
    		this.foregroundTileGrid.get(opp.y).set(opp.x, this.tileFromNumber(0));
    	}
    	
        this.drawAllTiles();
        this.repaint();
    }

    public JLabel tileFromNumber(int num) {
        String path = "/images/" + num + ".png";

        ImageIcon ii = new ImageIcon(getClass().getResource(path));

        JLabel jl = new JLabel(ii);
        jl.setSize(tileSize, tileSize);

        jl.setBackground(new Color(num));
        //"optymalizacja": w polu Color trzymamy numer kratki.

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


        initComponents();

        this.tileSize = 32;
        this.defaultBorder = new BasicBorders.FieldBorder(Color.lightGray, new Color(0, true), new Color(0, true), new Color(0, true));
        jCheckBoxMenuItem1.setSelected(false);

        addTileGridToWindow(createTileGrid(100, defaultBgTileID), overallBackgroundPanel);

        drawAllTiles();
        bulkPanel.setPreferredSize(this.getSize());
        
        contextPanel.setVisible(false);
        this.requestFocus();
    }

    public ObiektPierwszegoPlanu nowyObiektPierwszegoPlanu(int x, int y, int tileID) {
        if(isValidCoords(new Point(x, y)) == -1) {
            return null;
        }
        ObiektPierwszegoPlanu opp = new ObiektPierwszegoPlanu(x, y);
        opp.tile = this.tileFromNumber(tileID);
        
        if (getTileIDFromColor(foregroundTileGrid.get(y).get(x)) == 0) {
        	this.getForegroundTileGrid().get(y).set(x, new TilesLabel(opp));
        } else {
        	((TilesLabel)this.getForegroundTileGrid().get(y).get(x)).addObject(opp);
        }
        
        this.addObjectToForegroundList(opp);
        
        return opp;
    }

    public JLabel getTileAt(ArrayList<ArrayList<JLabel>> tileGrid, int tiledX, int tiledY) {
        return (tileGrid.get(tiledY)).get(tiledX);
    }

    public void setTileBorders(boolean on) {
        //FIXME: Tiles don't remove their borders

        for (int i = 0; i < foregroundTileGrid.size(); i++) {
            ArrayList<JLabel> arrayList = foregroundTileGrid.get(i);
            for (int j = 0; j < arrayList.size(); j++) {
                JLabel jLabel = arrayList.get(j);
                if (on) {
                    jLabel.setBorder(defaultBorder);
                } else {
                    jLabel.setBorder(BorderFactory.createEmptyBorder());
                }

                jLabel.repaint();
            }
        }

        this.drawAllTiles();
        this.repaint();
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
        targetPanel.removeAll();

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

    public void highlightVisibleFrames(Agent agent) {
    	for (int x = -agent.getFieldOfView(); x <= agent.getFieldOfView(); x++ ) {
    		for (int y = -agent.getFieldOfView(); y <= agent.getFieldOfView(); y++) {
    			int yAfter = agent.getMapFrame().getY() + y;
    			int xAfter = agent.getMapFrame().getY() + y;
    			
    			if (yAfter < this.backgroundTileGrid.size() && yAfter >= 0 
    					&& xAfter < this.backgroundTileGrid.get(0).size() && xAfter >=0)
    			
    			this.backgroundTileGrid.get(agent.getMapFrame().getY() + y).get(agent.getMapFrame().getX() + x).setBorder(new LineBorder(new Color(185, 226, 18), 3, true));
    		}
    	}
    }
    
    public void deleteHighlightedBorders() {
    	for (int x = 0; x < this.backgroundTileGrid.size(); x++ ) {
    		for (int y = 0; y < this.backgroundTileGrid.get(0).size(); y++) {
    			this.backgroundTileGrid.get(y).get(x).setBorder(null);
    		}
    	}
    }
    
    public void focusOnAgent(Agent agent) {
    	JScrollBar vertical = jScrollPane1.getVerticalScrollBar();
    	vertical.setValue( agent.getMapFrame().getY() * this.tileSize - 5 * this.tileSize);
    	
    	JScrollBar horizontal = jScrollPane1.getHorizontalScrollBar();
    	horizontal.setValue( agent.getMapFrame().getX() * this.tileSize - 10 * this.tileSize);
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

    public JPanel getForegroundPanel() {
        return foregroundPanel;
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        bulkPanel = new javax.swing.JPanel();
        contextPanel = new javax.swing.JPanel();
        iconDisplayPanel = new javax.swing.JPanel();
        tileInfoPanel = new javax.swing.JPanel();
        tileInfoPanelLabel1 = new javax.swing.JLabel();
        foregroundPanel = new javax.swing.JPanel();
        backgroundPanel = new javax.swing.JPanel();
        overallBackgroundPanel = new javax.swing.JPanel();
        jMenuBar1 = new javax.swing.JMenuBar();
        jMenu3 = new javax.swing.JMenu();
        jMenuItem1 = new javax.swing.JMenuItem();
        jCheckBoxMenuItem1 = new javax.swing.JCheckBoxMenuItem();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Polska, AD 1340");
        setBackground(new java.awt.Color(102, 102, 102));
        setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        setMinimumSize(new java.awt.Dimension(800, 600));
        addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
            	formMouseClicked(evt);
            }
        });
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

        contextPanel.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(204, 204, 204), 4, true));
        contextPanel.setOpaque(false);
        contextPanel.setLayout(new javax.swing.BoxLayout(contextPanel, javax.swing.BoxLayout.Y_AXIS));

        iconDisplayPanel.setBackground(new java.awt.Color(255, 255, 255));
        iconDisplayPanel.setPreferredSize(new java.awt.Dimension(20, 50));
        iconDisplayPanel.setLayout(new java.awt.GridLayout(1, 1));
        contextPanel.add(iconDisplayPanel);

        tileInfoPanel.setLayout(new java.awt.CardLayout());

        tileInfoPanelLabel1.setText("jLabel1");
        tileInfoPanelLabel1.setVerticalAlignment(javax.swing.SwingConstants.TOP);
        tileInfoPanel.add(tileInfoPanelLabel1, "card2");

        contextPanel.add(tileInfoPanel);

        bulkPanel.add(contextPanel);
        contextPanel.setBounds(500, 270, 290, 180);

        foregroundPanel.setOpaque(false);
        foregroundPanel.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                foregroundPanelMouseClicked(evt);
            }
        });
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
        jScrollPane1.setBounds(0, 0, 680, 540);

        controlPanel = new JPanel();
        controlPanel.setLayout(null);
        controlPanel.setBounds(683, 0, 101, 540);
        getContentPane().add(controlPanel);
        
        btnNextIter = new JButton("<html><center>Nastepna<br>iteracja</center></html>");
        btnNextIter.setBounds(10, 11, 81, 92);
        btnNextIter.setFocusable(false);
        controlPanel.add(btnNextIter);
        
        btnNextAgent = new JButton("<html><center>Nastepny<br>agent</center></html>");
        btnNextAgent.setBounds(10, 128, 81, 92);
        btnNextAgent.setFocusable(false);
        controlPanel.add(btnNextAgent);
        
        lblIteracja = new JLabel("ITERACJA");
        lblIteracja.setBounds(13, 256, 78, 19);
        controlPanel.add(lblIteracja);
        
        lblAgent = new JLabel("AGENT");
        lblAgent.setBounds(12, 308, 41, 19);
        controlPanel.add(lblAgent);
        
        textFieldIter = new JTextField();
        textFieldIter.setBounds(8, 276, 86, 20);
        textFieldIter.setFocusable(false);
        textFieldIter.setEditable(false);
        controlPanel.add(textFieldIter);
        textFieldIter.setColumns(10);
        
        textFieldAgent = new JTextField();
        textFieldAgent.setBounds(7, 329, 86, 20);
        textFieldAgent.setFocusable(false);
        textFieldAgent.setEditable(false);
        controlPanel.add(textFieldAgent);
        textFieldAgent.setColumns(10);
        
        jMenu3.setText("Widok");

        jMenuItem1.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_ENTER, java.awt.event.InputEvent.ALT_MASK));
        jMenuItem1.setText("Dopasuj do ekranu");
        jMenuItem1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem1ActionPerformed(evt);
            }
        });
        jMenu3.add(jMenuItem1);

        jCheckBoxMenuItem1.setSelected(true);
        jCheckBoxMenuItem1.setText("Wyświetl siatkę");
        jCheckBoxMenuItem1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBoxMenuItem1ActionPerformed(evt);
            }
        });
        jMenu3.add(jCheckBoxMenuItem1);

        jMenuBar1.add(jMenu3);

        setJMenuBar(jMenuBar1);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    public void resizeContentsTo(Dimension d) {
        jScrollPane1.setPreferredSize(d);
        jScrollPane1.setSize(d);
        
        //przesuwam odpowiednio panel kontrolny
        this.controlPanel.setBounds(d.width, 0, this.controlPanel.getWidth(), this.controlPanel.getHeight());

        if (this.backgroundTileGrid != null) {
            bulkPanel.setPreferredSize(new Dimension(this.tileSize * this.backgroundTileGrid.size(), this.tileSize * this.backgroundTileGrid.size()));
        }

        if (jScrollPane1.getHorizontalScrollBar() != null) {
            jScrollPane1.getHorizontalScrollBar().setUnitIncrement(tileSize / 2);
        }
        if (jScrollPane1.getVerticalScrollBar() != null) {
            jScrollPane1.getVerticalScrollBar().setUnitIncrement(tileSize / 2);
        }
        
        this.jScrollPane1.validate();
    }

    private void formComponentResized(java.awt.event.ComponentEvent evt) {//GEN-FIRST:event_formComponentResized

    	//nalezy takze uwzglednic panel kontrolny, ktory jest obok scroll panelu,
    	//aby scroll panel nie nachodzil na panel kontrolny
        Dimension contentPanelDim = this.getContentPane().getSize();
        Dimension controlPanelDim = this.controlPanel.getSize();
        Dimension newDim = new Dimension(contentPanelDim.width - controlPanelDim.width, contentPanelDim.height);
        
        this.resizeContentsTo(newDim);

    }//GEN-LAST:event_formComponentResized

    private void formKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_formKeyReleased
//        demo eventu   
        if (evt.getKeyCode() == KeyEvent.VK_DOWN) {
            this.foregroundList.get(0).move(0, 1);
        }
        if (evt.getKeyCode() == KeyEvent.VK_UP) {
            this.foregroundList.get(0).move(0, -1);
        }
        if (evt.getKeyCode() == KeyEvent.VK_LEFT) {
            this.foregroundList.get(0).move(-1, 0);
        }
        if (evt.getKeyCode() == KeyEvent.VK_RIGHT) {
            this.foregroundList.get(0).move(1, 0);
        }

        this.revalidate();
    }//GEN-LAST:event_formKeyReleased

    private void jMenuItem1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem1ActionPerformed
        this.setExtendedState(Frame.MAXIMIZED_BOTH);
    }//GEN-LAST:event_jMenuItem1ActionPerformed

    private void formMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_formMouseClicked
        //TODO
    }//GEN-LAST:event_formMouseClicked

    public int getTileIDFromColor(JLabel tile) {
        return (tile.getBackground().getRGB() & 0xFFFFFF);
    }

    private void foregroundPanelMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_foregroundPanelMouseClicked
        if (evt.getButton() == MouseEvent.BUTTON1) { //LMB

            contextPanel.setVisible(false);
            ((JPanel) contextPanel.getComponent(0)).removeAll();


            Point pointClicked = evt.getPoint();
            int tileX = pointClicked.x / this.tileSize;
            int tileY = pointClicked.y / this.tileSize;

            //klikniety tile tła
            JLabel clickedTile = this.backgroundTileGrid.get(tileY).get(tileX);
            
            int tileNum = getTileIDFromColor(clickedTile);
            
            Rectangle rect = new Rectangle(pointClicked.x, pointClicked.y, contextPanel.getWidth(), contextPanel.getHeight());
            if (rect.y > this.getContentPane().getHeight() - rect.height) {
                rect.y -= rect.height;
            }
            if (rect.x > this.getContentPane().getWidth() - rect.width) {
                rect.x -= rect.width;
            }
            contextPanel.setBounds(rect);
            
            JLabel iconForContextPanel = tileFromNumber(tileNum);

            iconForContextPanel.setHorizontalAlignment(JLabel.CENTER);
            iconForContextPanel.setVerticalAlignment(JLabel.CENTER);

            ((JPanel) contextPanel.getComponent(0)).add(iconForContextPanel);

            //klikniety tile pierwszego planu
            JLabel clickedFgTile = this.foregroundTileGrid.get(tileY).get(tileX);
            
            
            int tileFgNum = -1;
            //jesli klikniety tile jest typu TilesLabel to wtedy pobiramy liste obiektow, ktore tam sa
            //i wyswietlamy o nich informacje
            JLabel iconFgForContextPanel = new JLabel(clickedFgTile.getIcon());
            if (!(clickedFgTile instanceof TilesLabel)) {
            	 tileFgNum = getTileIDFromColor(clickedFgTile);
                 iconFgForContextPanel = tileFromNumber(tileFgNum);
            } else {
            	if (((TilesLabel)clickedFgTile).getForegroundObjects().size() == 1) {
            		tileFgNum = ((TilesLabel)clickedFgTile).getForegroundObjects().get(0).tile.getBackground().getRGB() & 0xFFFFFF;
            	}
            }
            
            iconFgForContextPanel.setHorizontalAlignment(JLabel.CENTER);
            iconFgForContextPanel.setVerticalAlignment(JLabel.CENTER);
            ((JPanel) contextPanel.getComponent(0)).add(iconFgForContextPanel);

            JLabel opisIkony;
            opisIkony = new JLabel("<html><h3>Informacje o polu</h3></html>");
            opisIkony.setBorder(new EmptyBorder(0, 8, 0, 0));

            iconDisplayPanel.add(opisIkony);

            StringBuilder sb = new StringBuilder("<html>");
            sb.append("Kliknięte pole: <pre>X: <b>").append(tileX).append("</b> Y: <b>").append(tileY).append("</b></pre>");
            sb.append("<br/> ");
            sb.append("Warstwa obiektów:<br/>");
            
            if (!(clickedFgTile instanceof TilesLabel)) {
            	sb.append(tileFgNum).append(": <b>").append(InformacjeOSwiecie.getOpisKafelka(tileFgNum)).append("</b>");
            } else {
            	sb.append(tileFgNum).append(": <b>").append(((TilesLabel)clickedFgTile).getDescription()).append("</b>");
            }
            
            
            sb.append("<br/>Warstwa tła:<br/>");
            sb.append(tileNum).append(": <b>").append(InformacjeOSwiecie.getOpisKafelka(tileNum)).append("</b>");
            sb.append("</html>");

            tileInfoPanelLabel1.setText(sb.toString());
            contextPanel.setVisible(true);
        } else if (evt.getButton() == MouseEvent.BUTTON3) { //RMB
            contextPanel.setVisible(false);
            ((JPanel) contextPanel.getComponent(0)).removeAll();
        }
    }//GEN-LAST:event_foregroundPanelMouseClicked

    private void jCheckBoxMenuItem1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBoxMenuItem1ActionPerformed
        this.isTileBordered = !this.isTileBordered;
        this.setTileBorders(isTileBordered);
    }//GEN-LAST:event_jCheckBoxMenuItem1ActionPerformed

	public javax.swing.JPanel getIconDisplayPanel() {
		return iconDisplayPanel;
	}

	public void setIconDisplayPanel(javax.swing.JPanel iconDisplayPanel) {
		this.iconDisplayPanel = iconDisplayPanel;
	}

	public javax.swing.JPanel getTileInfoPanel() {
		return tileInfoPanel;
	}

	public void setTileInfoPanel(javax.swing.JPanel tileInfoPanel) {
		this.tileInfoPanel = tileInfoPanel;
	}

	public javax.swing.JLabel getTileInfoPanelLabel1() {
		return tileInfoPanelLabel1;
	}

	public void setTileInfoPanelLabel1(javax.swing.JLabel tileInfoPanelLabel1) {
		this.tileInfoPanelLabel1 = tileInfoPanelLabel1;
	}

	public JButton getBtnNextIter() {
		return btnNextIter;
	}

	public void setBtnNextIter(JButton btnNextIter) {
		this.btnNextIter = btnNextIter;
	}

	public JButton getBtnNextAgent() {
		return btnNextAgent;
	}

	public void setBtnNextAgent(JButton btnNextAgent) {
		this.btnNextAgent = btnNextAgent;
	}

	public JTextField getTextFieldIter() {
		return textFieldIter;
	}

	public void setTextFieldIter(JTextField textFieldIter) {
		this.textFieldIter = textFieldIter;
	}

	public JTextField getTextFieldAgent() {
		return textFieldAgent;
	}

	public void setTextFieldAgent(JTextField textFieldAgent) {
		this.textFieldAgent = textFieldAgent;
	}


}
