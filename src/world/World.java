package world;

import items.Item;

import java.awt.Point;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

import polskaad1340.InformacjeOSwiecie;
import polskaad1340.LadowanieMapy;
import CLIPSJNI.PrimitiveValue;
import clips.ClipsEnvironment;

public class World {

    public int height = 40;
    public int width = 40;
    public int iteration;
    private MapFrame[][] mapFrames;
    private ArrayList<Bandits> bandits = new ArrayList<Bandits>();
    private ArrayList<Blockade> blockades = new ArrayList<Blockade>();
    private ArrayList<Cataclysm> cataclysms = new ArrayList<Cataclysm>();
    private ArrayList<Road> roads = new ArrayList<Road>();
    private ArrayList<Town> towns = new ArrayList<Town>();
    private ArrayList<Tree> trees = new ArrayList<Tree>();
    private ClipsEnvironment clipsEnv;

    public World(ClipsEnvironment clipsEnv, LadowanieMapy ladowanieMapy) {
        this.clipsEnv = clipsEnv;
        this.loadFromMap(ladowanieMapy);
    }

    public void initializeWorld() {
        //randomBlockades();
        randomCataclysms();
        randomBandits();

        Set<String> visited = new HashSet<String>();
        for (Town town : this.towns) {
            if (!visited.contains(town.getNazwa())) {
                visited.add(town.getNazwa());
                town.randomItems();
            }
        }

    }

    public void changeItemPrices() {
        Random random = new Random();

        for (Town town : this.towns) {
            for (Item item : town.getItems()) {
                int change = random.nextInt(15) - 7;
                int newPrice = item.getPrice() + change;
                if (newPrice < 0) {
                    newPrice = 0;
                }

                item.setPrice(newPrice);
            }
        }
    }

    public void randomBlockades() {
        this.blockades = new ArrayList<Blockade>();

        Random random = new Random();
        int blockades = random.nextInt(6) + 1;
        for (int i = 0; i < blockades; i++) {
            //TODO blokady maja byc  losowane na drogach - bedzie to mozliwe po wczytaniu drog
            //this.blockades.add(new Blockade("blockade" + (i+1), mapFrame)
        }
    }

    //TODO prztestowac jak beda drogi
    public void randomBandits() {
        this.bandits = new ArrayList<Bandits>();

        Random random = new Random();
        Set<String> roadIds = new HashSet<String>();
        for (Road road : this.roads) {
            roadIds.add(road.getId());
        }

        for (String roadId : roadIds) {
            ArrayList<Road> actualRoad = new ArrayList<Road>();
            for (Road road : this.roads) {
                if (road.getId().equalsIgnoreCase(roadId)) {
                    actualRoad.add(road);
                }
            }

            int numberOfBandits = 0;
            double packageLoss = random.nextDouble() * (0.6 - 0.3) + 0.3;
            double goldLoss = random.nextDouble() * (0.6 - 0.3) + 0.3;

            while (numberOfBandits < (actualRoad.get(0).getMaxPartNo() * actualRoad.get(0).getRobberyProbability())) {
                int frame = actualRoad.get(random.nextInt(actualRoad.size())).getMapFrame();
                Bandits bandits = new Bandits(packageLoss, goldLoss, frame);
                this.bandits.add(bandits);
            }
        }
    }

    public void randomCataclysms() {
        this.cataclysms = new ArrayList<Cataclysm>();
        Random random = new Random();
        int cataclysmsNum = random.nextInt(6) + 1;
        int size = random.nextInt(3) + 3;

        for (int i = 0; i < cataclysmsNum; i++) {
            int frameStartX = random.nextInt(this.width);
            int frameStartY = random.nextInt(this.height);

            double randomizedTreesDestroy = random.nextDouble();
            int randomizedEnergyLoss = random.nextInt(20) + 4;
            int randomizedPopulationLoss = random.nextInt(10) + 4;

            for (int x = frameStartX; x < (frameStartX + size); x++) {
                for (int y = frameStartY; y < (frameStartY + size); y++) {
                    if (x < this.width && x >= 0 && y < this.height && y >= 0) {
                        Cataclysm tmpCataclysm = new Cataclysm("cataclysm" + (i + 1), this.mapFrames[x][y].getId(), randomizedTreesDestroy, randomizedEnergyLoss, randomizedPopulationLoss);
                        this.cataclysms.add(tmpCataclysm);
                    }
                }
            }
        }
    }

    public void loadFromClips() {
        bandits.clear();
        blockades.clear();
        cataclysms.clear();
        roads.clear();
        towns.clear();
        trees.clear();

        loadMapFrames();
        loadBandits();
        loadBlockades();
        loadCataclysms();
        loadRoads();
        loadTowns();
        loadTrees();
    }

    public ArrayList<Object> getVisibleWorld(String agentId) throws Exception {
        String evalString = "(find-all-facts ((?w widzialnaCzescSwiata)) (eq ?w:idAgenta " + agentId + "))";
        PrimitiveValue pv = this.clipsEnv.getWorldEnv().eval(evalString);

        ArrayList<Object> visibleObjects = new ArrayList<Object>();

        for (int i = 0; i < pv.size(); i++) {
            int visibleFrameId = pv.get(i).getFactSlot("idKratki").intValue();

            for (Town town : this.towns) {
                if (town.getMapFrame() == visibleFrameId) {
                    visibleObjects.add(town);

                    for (Item item : town.getItems()) {
                        visibleObjects.add(item);
                    }
                }
            }

            for (Blockade blockade : this.blockades) {
                if (blockade.getMapFrame() == visibleFrameId) {
                    visibleObjects.add(blockade);
                }
            }

            for (Tree tree : this.trees) {
                if (tree.getWorldFrame() == visibleFrameId) {
                    visibleObjects.add(tree);
                }
            }

            for (Road road : this.roads) {
                if (road.getMapFrame() == visibleFrameId) {
                    visibleObjects.add(road);
                }
            }
        }

        return visibleObjects;
    }

    public void printoutMapFrames() {
        for (int i = 0; i < height; i++) {
            for (int k = 0; k < width; k++) {

                if (mapFrames[k][i] != null) {
                    System.out.format("%4s", mapFrames[k][i].getId() + (" "));
                } else {
                    System.out.format("%4s", 0 + (" "));
                }
            }
            System.out.println();
        }
    }

    private void loadMapFrames() {
        try {

            mapFrames = new MapFrame[width][height];
            String evalString = "(find-all-facts ((?k kratka)) TRUE)";
            PrimitiveValue pv1 = clipsEnv.getWorldEnv().eval(evalString);

            for (int i = 0; i < pv1.size(); i++) {
                MapFrame temp = new MapFrame();
                temp.loadFromClips(pv1.get(i));
                mapFrames[temp.getX()][temp.getY()] = temp;

            }

        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    private void loadBandits() {
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

    private void loadBlockades() {
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

    private void loadCataclysms() {
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

    private void loadRoads() {
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

    private void loadTowns() {
        try {
            String evalString = "(find-all-facts ((?k grod)) TRUE)";
            PrimitiveValue pv1 = clipsEnv.getWorldEnv().eval(evalString);

            for (int i = 0; i < pv1.size(); i++) {
                Town temp = new Town(pv1.get(i));
                this.towns.add(temp);
            }

        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    private void loadTrees() {
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

    public void loadFromMap(LadowanieMapy mapLoad) {
        Random random = new Random();
        height = mapLoad.getMapSize();
        width = mapLoad.getMapSize();
        mapFrames = new MapFrame[height][width];

        ArrayList<Point> pozycjeGrodow = new ArrayList<>();
        pozycjeGrodow.add(new Point(-1, -1)); //WAŻNE: grody numerowane od jedynki. Element zerowy to placeholder.
        // kratki
        int frameID = 0;
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                MapFrame mf = new MapFrame();
                mf.setX(x);
                mf.setY(y);
                mf.setId(frameID);
                mapFrames[y][x] = mf;
                frameID++;
            }
        }
        // obiekty
        int townId = 1;

        String[] woodTypes = {"buk", "dab", "sosna"};
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                MapFrame mf = mapFrames[y][x];

                if (InformacjeOSwiecie.getKluczeKafelka("las").contains(mapLoad.getMap().get(y).get(x))) {
                    Tree tree = new Tree();
                    tree.setWorldFrame(mf.getId());
                    tree.setType(woodTypes[random.nextInt(woodTypes.length)]);
                    trees.add(tree);
                } else if (InformacjeOSwiecie.getKluczeKafelka("wycięty las").contains(mapLoad.getMap().get(y).get(x))) {
                    Tree tree = new Tree();
                    tree.setState("sciete");
                    tree.setWorldFrame(mf.getId());
                    trees.add(tree);
                } else if (InformacjeOSwiecie.getKluczeKafelka("miasto1").contains(mapLoad.getMap().get(y).get(x))) {

                    if (!InformacjeOSwiecie.getKluczeKafelka("miasto1").contains(mapLoad.getMap().get(y).get(x - 1))) {
                        Random rand = new Random();
                        int population = rand.nextInt(900) + 100;
                        float guards = (float) rand.nextDouble();
                        String name = "grod" + townId;

                        Town town1 = new Town(name, mf.getId(), population, guards);

                        Town town2 = new Town(name, mapFrames[y][x + 1].getId(), population, guards);

                        Town town3 = new Town(name, mapFrames[y + 1][x].getId(), population, guards);

                        Town town4 = new Town(name, mapFrames[y + 1][x + 1].getId(), population, guards);

                        towns.add(town1);
                        towns.add(town2);
                        towns.add(town3);
                        towns.add(town4);

                        pozycjeGrodow.add(new Point(x, y));
                        pozycjeGrodow.add(new Point(x, y + 1));
                        pozycjeGrodow.add(new Point(x + 1, y));
                        pozycjeGrodow.add(new Point(x + 1, y + 1));


                        townId++;
                    }

                }
            }
        }

        ArrayList<Set<Point>> wykryteDrogiNieplatne = new ArrayList<>();
        ArrayList<Set<Point>> wykryteDrogiPlatne = new ArrayList<>();
        ArrayList<ArrayList<Set<Point>>> drogi = new ArrayList<>();
        drogi.add(wykryteDrogiNieplatne);
        drogi.add(wykryteDrogiPlatne);

        int idKafelkaDrogiNieplatnej = (int) InformacjeOSwiecie.getKluczeKafelka("droga").iterator().next();
        int idKafelkaDrogiPlatnej = (int) InformacjeOSwiecie.getKluczeKafelka("droga płatna").iterator().next();
        ArrayList<Integer> idKafelkowDrog = new ArrayList<>();
        idKafelkowDrog.add(idKafelkaDrogiNieplatnej);
        idKafelkowDrog.add(idKafelkaDrogiPlatnej);

        for (int typdrogi = 0; typdrogi < drogi.size(); typdrogi++) {
            ArrayList<Set<Point>> wykryteDrogi = drogi.get(typdrogi);
            int idKafelkaDrogi = idKafelkowDrog.get(typdrogi);

            //znajdz polaczone ze soba kratki drog
            ArrayList<Point> listaKratekDrog = new ArrayList<>();
            ArrayList<ArrayList<Integer>> mapa = mapLoad.getMap();

            for (int i = 0; i < mapa.size(); i++) {
                ArrayList<Integer> arrayList = mapa.get(i);
                for (int j = 0; j < arrayList.size(); j++) {
                    Integer integer = arrayList.get(j);
                    //System.out.print(integer);
                    if (integer == idKafelkaDrogi) {
                        listaKratekDrog.add(new Point(j, i));
                    }
                }
            }
            //utworz z nich obiekty drog

            int currSet = -1;
            Point prevKratkaDrogi = null;
            for (int i = 0; i < listaKratekDrog.size(); i++) {
                Point kratkaDrogi = listaKratekDrog.get(i);
                if (prevKratkaDrogi == null || !InformacjeOSwiecie.arePointsAdjacent(kratkaDrogi, prevKratkaDrogi)) {//znaleziono nowa droge, nowy set
                    Set<Point> nowaDroga = new HashSet<>();
                    nowaDroga.add(kratkaDrogi);

                    currSet++;
                    wykryteDrogi.add(nowaDroga);
                } else {//kratka jest przystajaca do poprzedniej, dodaj do starej drogi.
                    wykryteDrogi.get(currSet).add(kratkaDrogi);
                }

                prevKratkaDrogi = kratkaDrogi;
            }

            //poszukaj pofragmentowanych drog - setow do polaczenia. jesli takie znajdziesz - polacz w jedna droge.


            //lacz drogi poki mozesz.
            boolean changesMade = true;
            while (changesMade) {
                //rozwiazanie jest nieoptymalne, ale mapa nigdy nie jest duza, a deadline blisko
                //FIXME: zamiast brute-force Dijkstra albo A*
                changesMade = false;
                for (int i = 0; i < wykryteDrogi.size(); i++) {
                    Set<Point> setA = wykryteDrogi.get(i);
                    nextset:
                    for (int j = 0; j < wykryteDrogi.size(); j++) {
                        Set<Point> setB = wykryteDrogi.get(j);
                        if (setA == setB) {
                            continue;
                        }
                        for (Point pointA : setA) {
                            for (Point pointB : setB) {
                                //dla kazdych dwoch setow, szukaj przystajacych punktow. jesli takie istnieja, polacz sety.
                                if (InformacjeOSwiecie.arePointsAdjacent(pointA, pointB)) {
                                    setA.addAll(setB);
                                    wykryteDrogi.remove(j);
                                    changesMade = true;
                                    break nextset;
                                }
                            }
                        }
                    }
                }
            }
        }

        System.out.println("NP:" + wykryteDrogiNieplatne.size() + " P:" + wykryteDrogiPlatne.size());

        ArrayList<ArrayList<Set<Point>>> typyDrog = new ArrayList<>();
        typyDrog.add(wykryteDrogiNieplatne);
        typyDrog.add(wykryteDrogiPlatne);

        for (int typ = 0; typ < typyDrog.size(); typ++) {
            ArrayList<Set<Point>> wykryteDrogiDanegoTypu = typyDrog.get(typ);

            for (int i = 0; i < wykryteDrogiDanegoTypu.size(); i++) {
                ArrayList<Point> punktyKoncowe = new ArrayList<>();
                ArrayList<Integer> grodyPunktowKoncowych = new ArrayList<>();

                Set<Point> aktualnaDroga = wykryteDrogiDanegoTypu.get(i);
                //szukaj punktów początkowych w drodze - czyli
                //punktów, które mają tylko jednego sąsiada
                for (Point kawalekDrogi : aktualnaDroga) {

                    int neighbours = 0;
                    Point[] neighs = InformacjeOSwiecie.getNeighboursOfPoint(kawalekDrogi);


                    for (int j = 0; j < neighs.length; j++) {
                        Point sasiad = neighs[j];
                        if (aktualnaDroga.contains(sasiad)) {
                            neighbours++;
                        }
                    }
                    if (neighbours == 1) {
                        punktyKoncowe.add(kawalekDrogi);
                    }
                }

                //punkty koncowe sa znane; teraz nalezy przejsc cala droge tworzac kratki drogi.
                for (int j = 0; j < punktyKoncowe.size(); j++) {
                    Point[] sasiedziMogacyBycGrodem = InformacjeOSwiecie.getNeighboursOfPoint(punktyKoncowe.get(j));
                    int grodID = -1;
                    for (int k = 0; k < sasiedziMogacyBycGrodem.length; k++) {
                        Point point = sasiedziMogacyBycGrodem[k];

                        int find = pozycjeGrodow.indexOf(point);

                        if (find != -1) {
                            grodID = find;
                            break;
                        }

                    }

                    if (grodID == -1) {
                        System.err.println("Droga nie ma zakonczenia w grodzie!");
                    }

                    grodyPunktowKoncowych.add(grodID);
                }

                //mamy punkty koncowe i ich grody. trawersuj droge od pierwszego do drugiego punktu koncowego.
                Point aktualnyPunkt = punktyKoncowe.get(0);
                Point poprzedniPunkt = null;
                ArrayList<Road> roadForwards = new ArrayList<>();

                int iteration = 1;

                while (aktualnyPunkt != punktyKoncowe.get(1)) {
                    Road aktualnyKawalekDrogi; //max zostanie wypelniony pozniej
                    String nazwatypu;
                    if(typ==0)
                    {
                        nazwatypu = "nieplatna";
                    }
                    else
                    {
                        nazwatypu = "platna";
                    }
                    aktualnyKawalekDrogi = new Road(nazwatypu + i, frameID, grodyPunktowKoncowych.get(0).toString(), grodyPunktowKoncowych.get(1).toString(), "utwardzona", true, iteration, -1);
                    roadForwards.add(aktualnyKawalekDrogi);
                    //szukaj nastepnego kawalka drogi
                    for (Point potencjalnyKrok : aktualnaDroga) {
                        if (InformacjeOSwiecie.arePointsAdjacent(aktualnyPunkt, potencjalnyKrok) && (poprzedniPunkt == null || poprzedniPunkt != potencjalnyKrok)) {
                            //wykonaj krok
                            poprzedniPunkt = aktualnyPunkt;
                            aktualnyPunkt = potencjalnyKrok;
                            break;
                        }
                    }
                    iteration++;
                }

                for (int j = 0; j < roadForwards.size(); j++) {
                    Road road = roadForwards.get(j);
                    road.setMaxPartNo(iteration);
                }

                ArrayList<Road> roadBackwards = new ArrayList<>();
                //droga w tyl to droga w przod z zamieniona kolejnoscia.
                for (int j = 0; j < roadForwards.size(); j++) {
                    Road roadFwdPart = roadForwards.get(j);
                    Road roadBackPart = new Road(roadFwdPart);

                    roadBackPart.setId(roadBackPart.getId() + "_wstecz");
                    //swap source and destination.
                    String sourceTown = roadBackPart.getSourceTown();
                    roadBackPart.setSourceTown(roadBackPart.getDestinationTown());
                    roadBackPart.setDestinationTown(sourceTown);

                    //change progress accordingly
                    roadBackPart.setCurrentPartNo(roadForwards.size() - j);

                    //add newly created part
                    roadBackwards.add(roadBackPart);
                }

                this.roads.addAll(roadForwards);
                System.out.println("Dodaję "+roadForwards.size()+" kratek drogi "+roadForwards.get(0).getId() +" w przód");
                this.roads.addAll(roadBackwards);
                System.out.println("Dodaję "+roadBackwards.size()+" kratek drogi "+roadBackwards.get(0).getId() +" w tył");
            }
        }
        randomRoadType();
    }

    private void randomRoadType() {
    	Set<String> roadsId = new HashSet<String>();
    	for (Road road : this.roads) {
    		roadsId.add(road.getId());
    	}
    	
    	String[] roadTypes = {"utwardzona", "nieutwardzona"};
    	Random random = new Random();
    	for (String roadId : roadsId) {
    		String roadType = roadTypes[random.nextInt(roadTypes.length)];
    		
    		for (Road road : this.roads) {
    			if (road.getId().equalsIgnoreCase(roadId)) {
    				road.setType(roadType);
    			}
    		}
    	}
    }
    
    @Override
    public String toString() {
        StringBuffer sbuf = new StringBuffer();

        // sbuf.append(";kratki\n");
        for (int i = 0; i < height; i++) {
            for (int k = 0; k < width; k++) {
                if (mapFrames[k][i] != null) {
                    sbuf.append(mapFrames[k][i]).append("\n");
                } else {
                    continue;
                }
            }
        }

        // sbuf.append(";bandyci\n");
        for (Bandits temp : bandits) {
            sbuf.append(temp.toString()).append("\n");
        }

        // sbuf.append(";blokady\n");
        for (Blockade temp : blockades) {
            sbuf.append(temp.toString()).append("\n");
        }

        // sbuf.append(";kleski\n");
        for (Cataclysm temp : cataclysms) {
            sbuf.append(temp.toString()).append("\n");
        }

        // sbuf.append(";drogi\n");
        for (Road temp : roads) {
            sbuf.append(temp.toString()).append("\n");
        }

        // /sbuf.append(";grody\n");
        for (Town temp : towns) {
            sbuf.append(temp.toString()).append("\n");
        }

        // sbuf.append(";drzewa\n");
        for (Tree temp : trees) {
            sbuf.append(temp.toString()).append("\n");
        }

        for (Town town : this.towns) {
            for (Item item : town.getItems()) {
                sbuf.append(item.toString()).append("\n");
            }
        }
        return sbuf.toString();
    }

    public void saveToClips(ClipsEnvironment clips) {
        for (String fact : this.toString().split("\n")) {
            clips.getWorldEnv().assertString(fact);
        }
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
