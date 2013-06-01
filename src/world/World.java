
package world;

import java.util.ArrayList;
import java.util.Random;

import polskaad1340.InformacjeOSwiecie;
import polskaad1340.LadowanieMapy;
import CLIPSJNI.PrimitiveValue;
import clips.ClipsEnvironment;

public class World {
	public int height=40;
	public int width =40;
	private MapFrame[][] mapFrames;
	private ArrayList<Bandits> bandits = new ArrayList<Bandits>();
	private ArrayList<Blockade> blockades = new ArrayList<Blockade>();
	private ArrayList<Cataclysm> cataclysms = new ArrayList<Cataclysm>();
	private ArrayList<Package> packages = new ArrayList<Package>();
	private ArrayList<Road> roads = new ArrayList<Road>();
	private ArrayList<Town> towns = new ArrayList<Town>();
	private ArrayList<Tree> trees = new ArrayList<Tree>();

	private ClipsEnvironment clipsEnv;

	public World(ClipsEnvironment clipsEnv){
		this.clipsEnv=clipsEnv;
	}

	public void loadFromClips(){
		bandits.clear();
		blockades.clear(); 
		cataclysms.clear(); 
		packages.clear();
		roads.clear();  
		towns.clear(); 
		trees.clear(); 
		
		loadMapFrames();
		loadBandits();
		loadBlockades();
		loadCataclysms();
		loadPackages();
		loadRoads();
		loadTowns();
		loadTrees();
	}
	public void printoutMapFrames(){
		for (int i =0; i< height;i++){
			for(int k =0; k< width;k++){
				
				if (mapFrames[k][i] != null){
					System.out.format("%4s",mapFrames[k][i].getId()+(" "));
				}
				else
				{
					System.out.format("%4s",0+(" "));
				}
			}
			System.out.println();
		}
	}
	private void loadMapFrames(){
		try {

			mapFrames = new MapFrame[width][height];
			String evalString = "(find-all-facts ((?k kratka)) TRUE)";
			PrimitiveValue pv1 = clipsEnv.getWorldEnv().eval(evalString);
			
			for (int i = 0; i < pv1.size(); i++) {
				MapFrame temp = new MapFrame();
				temp.loadFromClips(pv1.get(i));
				mapFrames[temp.getX()][temp.getY()]=temp;
				
			}
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	private void loadBandits()
	{
		try {
			String evalString = "(find-all-facts ((?k rozbojnicy)) TRUE)";
			PrimitiveValue pv1 = clipsEnv.getWorldEnv().eval(evalString);
			
			for (int i = 0; i < pv1.size(); i++) {
				Bandits temp = new Bandits();
				temp.loadFromClips(pv1.get(i));
				this.bandits.add(temp);
			}
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	private void loadBlockades()
	{
		try {
			String evalString = "(find-all-facts ((?k blokada)) TRUE)";
			PrimitiveValue pv1 = clipsEnv.getWorldEnv().eval(evalString);
			
			for (int i = 0; i < pv1.size(); i++) {
				Blockade temp = new Blockade();
				temp.loadFromClips(pv1.get(i));
				this.blockades.add(temp);
			}
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	private void loadCataclysms()
	{
		try {
			String evalString = "(find-all-facts ((?k kleska)) TRUE)";
			PrimitiveValue pv1 = clipsEnv.getWorldEnv().eval(evalString);
			
			for (int i = 0; i < pv1.size(); i++) {
				Cataclysm temp = new Cataclysm();
				temp.loadFromClips(pv1.get(i));
				this.cataclysms.add(temp);
			}
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	private void loadPackages()
	{
		try {
			String evalString = "(find-all-facts ((?k paczka)) TRUE)";
			PrimitiveValue pv1 = clipsEnv.getWorldEnv().eval(evalString);
			
			for (int i = 0; i < pv1.size(); i++) {
				Package temp = new Package();
				temp.loadFromClips(pv1.get(i));
				this.packages.add(temp);
			}
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}	
	private void loadRoads()
	{
		try {
			String evalString = "(find-all-facts ((?k droga)) TRUE)";
			PrimitiveValue pv1 = clipsEnv.getWorldEnv().eval(evalString);
			
			for (int i = 0; i < pv1.size(); i++) {
				Road temp = new Road();
				temp.loadFromClips(pv1.get(i));
				this.roads.add(temp);
			}
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	private void loadTowns()
	{
		try {
			String evalString = "(find-all-facts ((?k grod)) TRUE)";
			PrimitiveValue pv1 = clipsEnv.getWorldEnv().eval(evalString);
			
			for (int i = 0; i < pv1.size(); i++) {
				Town temp = new Town();
				temp.loadFromClips(pv1.get(i));
				this.towns.add(temp);
			}
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	private void loadTrees()
	{
		try {
			String evalString = "(find-all-facts ((?k drzewo)) TRUE)";
			PrimitiveValue pv1 = clipsEnv.getWorldEnv().eval(evalString);
			
			for (int i = 0; i < pv1.size(); i++) {
				Tree temp = new Tree();
				temp.loadFromClips(pv1.get(i));
				this.trees.add(temp);
			}
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void loadFromMap(LadowanieMapy mapLoad){
		height= mapLoad.getMapSize();
		width = mapLoad.getMapSize();
		mapFrames = new MapFrame[height][width];
		//kratki
		int frameID =0;
		for (int y = 0 ; y < height; y++){
			for(int x = 0; x< width; x++){
				MapFrame mf = new MapFrame();
				mf.setX(x);
				mf.setY(y);
				mf.setId(frameID);
				mapFrames[y][x]=mf;
				frameID++;
			}
		}
		//obiekty
		int townId=1;
		
		for (int y = 0 ; y < height; y++){
			for(int x = 0; x< width; x++){
				MapFrame mf = mapFrames[y][x];
			
				if( InformacjeOSwiecie.getKluczeKafelka("las").contains(mapLoad.getMap().get(y).get(x)) ){
					Tree tree = new Tree();
					tree.setWorldFrame(mf.getId());
					trees.add(tree);
				}
				else if(InformacjeOSwiecie.getKluczeKafelka("wycięty las").contains(mapLoad.getMap().get(y).get(x))){
					Tree tree = new Tree();
					tree.setCuted(true);
					tree.setWorldFrame(mf.getId());
					trees.add(tree);
				}
				else if(InformacjeOSwiecie.getKluczeKafelka("miasto1").contains(mapLoad.getMap().get(y).get(x))){
					
					if( !InformacjeOSwiecie.getKluczeKafelka("miasto1").contains(mapLoad.getMap().get(y).get(x-1))){
						Random rand = new Random();
						Town town1 = new Town();
						town1.setMapFrame(mf.getId());
						int population =rand.nextInt(900)+100;
						float guards = (float)rand.nextDouble();
						String name = "grod"+townId;
						
						town1.setNazwa(name);
						town1.setGuardsActivity(guards);
						town1.setPopulation(population);
						
						Town town2 = new Town();
						town2.setMapFrame(mapFrames[y][x+1].getId());
						town2.setNazwa(name);
						town2.setGuardsActivity(guards);
						town2.setPopulation(population);
						
						Town town3 = new Town();
						town3.setMapFrame(mapFrames[y+1][x].getId());
						town3.setNazwa(name);
						town3.setGuardsActivity(guards);
						town3.setPopulation(population);
						
						Town town4 = new Town();
						town4.setMapFrame(mapFrames[y+1][x+1].getId());
						town4.setNazwa(name);
						town4.setGuardsActivity(guards);
						town4.setPopulation(population);
						
						towns.add(town1);
						towns.add(town2);
						towns.add(town3);
						towns.add(town4);
						
						townId++;
					}
			
				}
			}
		}
	}
	/**
	 * Na sztywno z placa pisane
	 */
	private void loadRoadsFromMap(){
		
	}
	@Override
	public String toString() {
		StringBuffer sbuf = new StringBuffer();
		sbuf.append("(deffacts startup\n");
		
		sbuf.append(";kratki\n");
		for (int i =0; i< height;i++){
			for(int k =0; k< width;k++){
				if (mapFrames[k][i] != null){
					sbuf.append(mapFrames[k][i]).append("\n");
				}
				else
				{
					continue;
				}
			}
		}
		
		sbuf.append(";bandyci\n");
		for (Bandits temp:bandits){
			sbuf.append(temp.toString()).append("\n");
		}
		
		sbuf.append(";blokady\n");
		for (Blockade temp:blockades){
			sbuf.append(temp.toString()).append("\n");
		}
		
		sbuf.append(";kleski\n");
		for (Cataclysm temp:cataclysms){
			sbuf.append(temp.toString()).append("\n");
		}
		
		sbuf.append(";paczki\n");
		for (Package temp:packages){
			sbuf.append(temp.toString()).append("\n");
		}
		
		sbuf.append(";drogi\n");
		for (Road temp:roads){
			sbuf.append(temp.toString()).append("\n");
		}
		
		sbuf.append(";grody\n");
		for (Town temp:towns){
			sbuf.append(temp.toString()).append("\n");
		}

		sbuf.append(";drzewa\n");
		for (Tree temp:trees){
			sbuf.append(temp.toString()).append("\n");
		}
		
		sbuf.append(")");
		return sbuf.toString();
	}

	public MapFrame[][] getMapFrames() {
		return mapFrames;
	}
	public void setMapFrames(MapFrame[][] mapFrames) {
		this.mapFrames = mapFrames;
	}
	public ArrayList<Bandits> getBandits() {
		return bandits;
	}
	public void setBandits(ArrayList<Bandits> bandits) {
		this.bandits = bandits;
	}
	public ArrayList<Blockade> getBlockades() {
		return blockades;
	}
	public void setBlockades(ArrayList<Blockade> blockades) {
		this.blockades = blockades;
	}
	public ArrayList<Cataclysm> getCataclysms() {
		return cataclysms;
	}
	public void setCataclysms(ArrayList<Cataclysm> cataclysms) {
		this.cataclysms = cataclysms;
	}
	public ArrayList<Package> getPackages() {
		return packages;
	}
	public void setPackages(ArrayList<Package> packages) {
		this.packages = packages;
	}
	public ArrayList<Road> getRoads() {
		return roads;
	}
	public void setRoads(ArrayList<Road> roads) {
		this.roads = roads;
	}
	public ArrayList<Town> getTowns() {
		return towns;
	}
	public void setTowns(ArrayList<Town> towns) {
		this.towns = towns;
	}
	public ArrayList<Tree> getTrees() {
		return trees;
	}
	public void setTrees(ArrayList<Tree> trees) {
		this.trees = trees;
	}


}

