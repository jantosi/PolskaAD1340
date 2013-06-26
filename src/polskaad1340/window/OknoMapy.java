/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package polskaad1340.window;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Frame;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.plaf.basic.BasicBorders;

import world.Blockade;
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
    private ArrayList<ObiektPierwszegoPlanu> foregroundList = new ArrayList<>();
    private LadowanieMapy lm;
    
    
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
    private JScrollPane tileInfoScrollPanel;
    private javax.swing.JLabel tileInfoPanelLabel1;
    private JPanel controlPanel;
    private JButton btnNextIter;
    private JButton btnNextAgent;
    private JLabel lblIteracja;
    private JLabel lblAgent;
    private JTextField textFieldIter;
    private JTextField textFieldAgent;
    private Rectangle contextPanelDefaultRect;
    private JLabel lblBlockades;
    private JButton btnBlockades;
    private JLabel lblCataclysms;
    private JButton btnCataclysms;
    private JLabel lblBandits;
    private JButton btnBandits;
    private JLabel lblChangePrices;
    private JButton btnChangePrices;
    private JLabel lblPricesChanged;
    private JInternalFrame browseDetailFrame;
    private JMenu mnBrowse;
    private JMenu mnBrowseAgents;
    private JMenu mnBrowseTowns;
    private JMenu mnInference;
    private JMenuItem mnWorldInference;

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

    public JLabel tileFromNumber(int num) {
        String path = "/images/" + num + ".png";
        //System.out.println("path: " + path);
        ImageIcon ii = new ImageIcon(getClass().getResource(path));

        JLabel jl = new JLabel(ii);
        jl.setSize(tileSize, tileSize);

        jl.setBackground(new Color(num));
        //"optymalizacja": w polu Color trzymamy numer kratki.

        return jl;
    }

    public OknoMapy(LadowanieMapy lm) {
    	this.lm = lm;
    	
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
        
        mnInference = new JMenu("Pokaz wnioskowanie");
        jMenuBar1.add(mnInference);
        
        mnWorldInference = new JMenuItem("swiat");
        mnInference.add(mnWorldInference);
        
        addTileGridToWindow(createTileGrid(100, defaultBgTileID), overallBackgroundPanel);

        drawAllTiles();
        bulkPanel.setPreferredSize(this.getSize());
        
        contextPanel.setVisible(false);
        this.requestFocus();

    }

    public ObiektPierwszegoPlanu nowyObiektPierwszegoPlanu(int x, int y, String id, int tileID) {
        if(isValidCoords(new Point(x, y)) == -1) {
            return null;
        }
        ObiektPierwszegoPlanu opp = new ObiektPierwszegoPlanu(x, y, id);
        opp.tile = this.tileFromNumber(tileID);
        
		if (getTileIDFromColor(foregroundTileGrid.get(y).get(x)) == 0) {
			this.getForegroundTileGrid().get(y).set(x, new TilesLabel(opp));
		} else {
			((TilesLabel) this.getForegroundTileGrid().get(y).get(x)).addObject(opp);
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
        tileGrid.get(tiledY).get(tiledX).setBackground(jl.getBackground());
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
    			int xAfter = agent.getMapFrame().getX() + x;
    			if (yAfter < this.backgroundTileGrid.size() && yAfter >= 0 
    					&& xAfter < this.backgroundTileGrid.get(0).size() && xAfter >=0) {
    				this.backgroundTileGrid.get(yAfter).get(xAfter).setBorder(new LineBorder(new Color(185, 226, 18), 3, true));
    			}
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
    
    public void setScrollFocusOn(int x, int y) {
    	JScrollBar vertical = jScrollPane1.getVerticalScrollBar();
    	vertical.setValue( y * this.tileSize - 5 * this.tileSize);
    	
    	JScrollBar horizontal = jScrollPane1.getHorizontalScrollBar();
    	horizontal.setValue( x * this.tileSize - 10 * this.tileSize);
    }
    
    public void displayInferenceResults(int x, int y, String header, String inferenceMessage, String type) {
    	this.displayContextPanelWithInferenceResults(x, y, header, inferenceMessage, type);
    }
    
    public void displayBlockades(ArrayList<Blockade> blockades) {
    	for (Blockade blockade : blockades) {
    		int x = blockade.getMapFrame().getX();
    		int y = blockade.getMapFrame().getY();
    		
    		blockade.setReplacedTile(this.backgroundTileGrid.get(y).get(x));
    		this.setTileAt(this.backgroundTileGrid, x, y, blockade.getTile());
    	}
    }
    
    public void hideBlockades(ArrayList<Blockade> blockades) {
    	this.importBackgroundTileGrid(this.lm.getMap());
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
        tileInfoScrollPanel = new JScrollPane();
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
        
        getContentPane().setLayout(null);

        bulkPanel.setCursor(new java.awt.Cursor(java.awt.Cursor.CROSSHAIR_CURSOR));
        bulkPanel.setLayout(null);

        contextPanel.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(204, 204, 204), 4, true));
        contextPanel.setOpaque(false);
        contextPanel.setLayout(new BoxLayout(contextPanel, BoxLayout.Y_AXIS));
        contextPanel.setBounds(500, 270, 290, 180);
        contextPanelDefaultRect = new Rectangle(contextPanel.getBounds());
        
        iconDisplayPanel.setBackground(new java.awt.Color(255, 255, 255));
        iconDisplayPanel.setPreferredSize(new java.awt.Dimension(20, 50));
        iconDisplayPanel.setLayout(new java.awt.GridLayout(1, 1));
        contextPanel.add(iconDisplayPanel);

        
        tileInfoPanelLabel1.setText("jLabel1");
        tileInfoPanelLabel1.setVerticalAlignment(javax.swing.SwingConstants.TOP);
        tileInfoScrollPanel.setViewportView(tileInfoPanelLabel1);
        contextPanel.add(tileInfoScrollPanel);
        
        bulkPanel.add(contextPanel);
        
        

        foregroundPanel.setOpaque(false);
        foregroundPanel.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                foregroundPanelMouseClicked(evt);
            }
        });
        
        browseDetailFrame = new JInternalFrame("Szczegoly");
        browseDetailFrame.setBounds(0, 0, 700, 320);
        browseDetailFrame.setLocation(300, 100);
        getContentPane().add(browseDetailFrame);
        browseDetailFrame.getContentPane().setLayout(new BoxLayout(browseDetailFrame.getContentPane(), BoxLayout.Y_AXIS));
        browseDetailFrame.setVisible(false);
        
        
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
        lblIteracja.setBounds(13, 226, 78, 19);
        controlPanel.add(lblIteracja);
        
        lblAgent = new JLabel("AGENT");
        lblAgent.setBounds(12, 278, 41, 19);
        controlPanel.add(lblAgent);
        
        textFieldIter = new JTextField();
        textFieldIter.setBounds(8, 246, 86, 20);
        textFieldIter.setFocusable(false);
        textFieldIter.setEditable(false);
        controlPanel.add(textFieldIter);
        textFieldIter.setColumns(10);
        
        textFieldAgent = new JTextField();
        textFieldAgent.setBounds(7, 299, 86, 20);
        textFieldAgent.setFocusable(false);
        textFieldAgent.setEditable(false);
        controlPanel.add(textFieldAgent);
        textFieldAgent.setColumns(10);
        
        lblBlockades = new JLabel("BLOKADY");
        lblBlockades.setBounds(10, 330, 81, 19);
        controlPanel.add(lblBlockades);
        
        btnBlockades = new JButton("WYLACZONE");
        btnBlockades.setFont(new Font("Tahoma", Font.PLAIN, 8));
        btnBlockades.setForeground(Color.RED);
        btnBlockades.setBounds(10, 348, 83, 23);
        btnBlockades.setFocusable(false);
        btnBlockades.setToolTipText("<html>Po wlaczeniu/wylaczeniu przy NASTEPNEJ iteracji<br>" +
        		"zostana wlaczone/wylaczone wylosowane blokady</html>");
        controlPanel.add(btnBlockades);
        
        lblCataclysms = new JLabel("KLESKI");
        lblCataclysms.setBounds(10, 378, 81, 19);
        controlPanel.add(lblCataclysms);
        
        btnCataclysms = new JButton("WYLACZONE");
        btnCataclysms.setFont(new Font("Tahoma", Font.PLAIN, 8));
        btnCataclysms.setForeground(Color.RED);
        btnCataclysms.setFocusable(false);
        btnCataclysms.setBounds(10, 396, 83, 23);
        btnCataclysms.setToolTipText("<html>Po wlaczeniu/wylaczeniu przy NASTEPNEJ iteracji<br>" +
        		"zostana wlaczone/wylaczone wylosowane kleski<html>");
        controlPanel.add(btnCataclysms);
        
        lblBandits = new JLabel("ROZBOJNICY");
        lblBandits.setBounds(10, 426, 81, 19);
        controlPanel.add(lblBandits);
        
        btnBandits = new JButton("WYLACZONE");
        btnBandits.setForeground(Color.RED);
        btnBandits.setFont(new Font("Tahoma", Font.PLAIN, 8));
        btnBandits.setFocusable(false);
        btnBandits.setBounds(10, 444, 83, 23);
        btnBandits.setToolTipText("<html>Po wlaczeniu przy KAZDEJ iteracji<br>" +
        		"beda losowani na nowo rozbojnicy<html>");
        controlPanel.add(btnBandits);
        
        lblChangePrices = new JLabel("ZMIEN CENY");
        lblChangePrices.setBounds(11, 476, 81, 19);
        controlPanel.add(lblChangePrices);
        
        btnChangePrices = new JButton("ZMIEN CENY");
        btnChangePrices.setToolTipText("<html>Zmiena losowo ceny w kazdym grodzie z przedzialu (-7, 8)<html>");
        btnChangePrices.setForeground(new Color(128, 128, 0));
        btnChangePrices.setFont(new Font("Tahoma", Font.PLAIN, 8));
        btnChangePrices.setFocusable(false);
        btnChangePrices.setBounds(11, 494, 83, 23);
        controlPanel.add(btnChangePrices);
        
        lblPricesChanged = new JLabel("zmieniono ceny");
        lblPricesChanged.setForeground(new Color(0, 128, 0));
        lblPricesChanged.setBounds(15, 517, 77, 19);
        lblPricesChanged.setVisible(false);
        controlPanel.add(lblPricesChanged);
        
        jMenu3.setText("Widok");
        jMenuBar1.add(jMenu3);
        
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

        mnBrowse = new JMenu("Przegladaj");
        jMenuBar1.add(mnBrowse);
        
        mnBrowseAgents = new JMenu("agenci");
        mnBrowse.add(mnBrowseAgents);
        
        mnBrowseTowns = new JMenu("grody");
        mnBrowse.add(mnBrowseTowns);

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
        	Point pointClicked = evt.getPoint();
        	this.displayContextPanelWithTileInfo(pointClicked.x, pointClicked.y);
            
        } else if (evt.getButton() == MouseEvent.BUTTON3) { //RMB
            contextPanel.setVisible(false);
            ((JPanel) contextPanel.getComponent(0)).removeAll();
        }
    }//GEN-LAST:event_foregroundPanelMouseClicked

    public void displayContextPanelWithTileInfo(int x, int y) {
    	contextPanel.setVisible(false);
        ((JPanel) contextPanel.getComponent(0)).removeAll();

        int tileX = x / this.tileSize;
        int tileY = y / this.tileSize;

        //klikniety tile tła
        JLabel clickedTile = this.backgroundTileGrid.get(tileY).get(tileX);
        
        int tileNum = getTileIDFromColor(clickedTile);
        
        contextPanel.setBounds(this.contextPanelDefaultRect);
        Rectangle rect = new Rectangle(x, y, contextPanel.getWidth(), contextPanel.getHeight());
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
    }
    
    public void displayContextPanelWithInferenceResults(int x, int y, String header, String message, String type) {
    	contextPanel.setVisible(false);
        ((JPanel) contextPanel.getComponent(0)).removeAll();

        contextPanel.setBounds(this.contextPanelDefaultRect);
        Rectangle rect;
        if (type.equals("world")) {
        	rect = new Rectangle(x, y, contextPanel.getWidth() + 110, contextPanel.getHeight() + 180);
        } else {
        	rect = new Rectangle(x, y, contextPanel.getWidth() + 80, contextPanel.getHeight());
        }
        
        if (rect.y > this.getContentPane().getHeight() - rect.height) {
            rect.y -= rect.height;
        }
        if (rect.x > this.getContentPane().getWidth() - rect.width) {
            rect.x -= rect.width;
        }
        contextPanel.setBounds(rect);
        
        JLabel opisIkony;
        opisIkony = new JLabel("<html><h3>" + header + "</h3></html>");
        opisIkony.setBorder(new EmptyBorder(0, 8, 0, 0));

        iconDisplayPanel.add(opisIkony);

        StringBuffer sb = new StringBuffer("<html>");
        sb.append(message.replaceAll("\n", "<br>"));
        sb.append("</html>");
        tileInfoPanelLabel1.setText(sb.toString());
        contextPanel.setVisible(true);
    }
    
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

	public int getTileSize() {
		return tileSize;
	}

	public void setTileSize(int tileSize) {
		this.tileSize = tileSize;
	}

	public JButton getBtnBlockades() {
		return btnBlockades;
	}

	public void setBtnBlockades(JButton btnBlockades) {
		this.btnBlockades = btnBlockades;
	}

	public JButton getBtnCataclysms() {
		return btnCataclysms;
	}

	public void setBtnCataclysms(JButton btnCataclysms) {
		this.btnCataclysms = btnCataclysms;
	}

	public JButton getBtnBandits() {
		return btnBandits;
	}

	public void setBtnBandits(JButton btnBandits) {
		this.btnBandits = btnBandits;
	}

	public JButton getBtnChangePrices() {
		return btnChangePrices;
	}

	public void setBtnChangePrices(JButton btnChangePrices) {
		this.btnChangePrices = btnChangePrices;
	}

	public JLabel getLblPricesChanged() {
		return lblPricesChanged;
	}

	public void setLblPricesChanged(JLabel lblPricesChanged) {
		this.lblPricesChanged = lblPricesChanged;
	}

	public JMenu getMnBrowseAgents() {
		return mnBrowseAgents;
	}

	public void setMnBrowseAgents(JMenu mnBrowseAgents) {
		this.mnBrowseAgents = mnBrowseAgents;
	}

	public JMenu getMnBrowseTowns() {
		return mnBrowseTowns;
	}

	public void setMnBrowseTowns(JMenu mnBrowseTowns) {
		this.mnBrowseTowns = mnBrowseTowns;
	}

	public JMenu getMnBrowse() {
		return mnBrowse;
	}

	public void setMnBrowse(JMenu mnBrowse) {
		this.mnBrowse = mnBrowse;
	}

	public JInternalFrame getBrowseDetailFrame() {
		return browseDetailFrame;
	}

	public void setBrowseDetailFrame(JInternalFrame browseDetailFrame) {
		this.browseDetailFrame = browseDetailFrame;
	}

	public JMenuItem getMnWorldInference() {
		return mnWorldInference;
	}

	public void setMnWorldInference(JMenuItem mnWorldInference) {
		this.mnWorldInference = mnWorldInference;
	}

	public javax.swing.JPanel getContextPanel() {
		return contextPanel;
	}

	public void setContextPanel(javax.swing.JPanel contextPanel) {
		this.contextPanel = contextPanel;
	}
}
