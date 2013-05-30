package world;

import java.util.ArrayList;

public class World {
private MapFrame[][] mapFrames;
private ArrayList<Bandits> bandits = new ArrayList<Bandits>();
private ArrayList<Blockade> blockades = new ArrayList<Blockade>();
private ArrayList<Cataclysm> cataclysms = new ArrayList<Cataclysm>();
private ArrayList<Package> packages = new ArrayList<Package>();
private ArrayList<Road> roads = new ArrayList<Road>();
private ArrayList<Town> towns = new ArrayList<Town>();
private ArrayList<Tree> trees = new ArrayList<Tree>();
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
