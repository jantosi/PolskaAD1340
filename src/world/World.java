package world;

import items.Item;

import java.awt.Point;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

import polskaad1340.window.InformacjeOSwiecie;
import polskaad1340.window.LadowanieMapy;
import polskaad1340.window.OknoMapy;
import statistics.CourierStatistics;
import statistics.WoodmanStatistics;
import CLIPSJNI.PrimitiveValue;
import agents.Agent;
import agents.Courier;
import agents.Woodman;
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
    private ArrayList<Agent> agents = new ArrayList<Agent>();
    private ClipsEnvironment clipsEnv;
  

    public World(ClipsEnvironment clipsEnv, LadowanieMapy ladowanieMapy, OknoMapy om) {
        this.clipsEnv = clipsEnv;
        this.loadFromMap(ladowanieMapy);
        initializeWorld(om);
    }

    private void initializeWorld(OknoMapy om) {
        initializeAgents(om);
    	randomBlockades();
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

    private void initializeAgents(OknoMapy om) {
    	Random random = new Random();
    	
    	WoodmanStatistics ws = new WoodmanStatistics();
    	MapFrame mapFrame = this.getFrameById(this.roads.get(random.nextInt(this.roads.size())).getMapFrame());
        Woodman woodman = new Woodman("drwal1", "src/clips/poslaniec.clp", ws, mapFrame, om);
        
        
        CourierStatistics cs = new CourierStatistics();
        mapFrame = this.getFrameById(this.roads.get(random.nextInt(this.roads.size())).getMapFrame());
        Courier courier = new Courier("poslaniec1", "src/clips/poslaniec.clp", cs, mapFrame, om);
        
        this.agents.add(woodman);
        this.agents.add(courier);
        om.drawAllTiles();
    }
    
    public void randomBlockades() {
        this.blockades = new ArrayList<Blockade>();

        Random random = new Random();
        int blockades = random.nextInt(6) + 4;
        
        Set<Integer> roadWithBlockades = new HashSet<Integer>();
        for (int i = 0; i < blockades; i++) {
        	Road randomRoad = this.roads.get(random.nextInt(this.roads.size()));
        	while (roadWithBlockades.contains(randomRoad.getMapFrame())) {
        		randomRoad = this.roads.get(random.nextInt(this.roads.size()));
        	}
        	Blockade blockade = new Blockade("blokada" + (i+1), randomRoad.getMapFrame());
        	this.blockades.add(blockade);
        }
    }

    public void randomBandits() {
        this.bandits = new ArrayList<Bandits>();

        Random random = new Random();
        Set<String> roadIds = new HashSet<String>();
        for (Road road : this.roads) {
            String tmp = road.getId().replace("_wstecz", "");
        	roadIds.add(tmp);
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
                numberOfBandits++;
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

            int randomizedTreesDestroy = random.nextInt(100);
            int randomizedLiveTime = random.nextInt(20)+4;
            int randomizedEnergyLoss = random.nextInt(20) + 4;
            int randomizedPopulationLoss = random.nextInt(10) + 4;

            for (int x = frameStartX; x < (frameStartX + size); x++) {
                for (int y = frameStartY; y < (frameStartY + size); y++) {
                    if (x < this.width && x >= 0 && y < this.height && y >= 0) {
                        Cataclysm tmpCataclysm = new Cataclysm("cataclysm" + (i + 1), this.mapFrames[x][y].getId(),
                        				randomizedTreesDestroy, randomizedEnergyLoss, randomizedPopulationLoss,randomizedLiveTime);
                        this.cataclysms.add(tmpCataclysm);
                    }
                }
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

    public void loadFromClips() {
        this.bandits.clear();
        this.blockades.clear();
        this.cataclysms.clear();
        this.roads.clear();
        this.towns.clear();
        this.trees.clear();

        loadMapFrames();
        loadBandits();
        loadBlockades();
        loadCataclysms();
        loadRoads();
        loadTowns();
        loadTrees();
        loadAgents();
    }

    public ArrayList<Object> getVisibleWorld(String agentId) throws Exception {
        String evalString = "(find-all-facts ((?w widzialnaCzescSwiata)) (eq ?w:idAgenta " + agentId + "))";
        PrimitiveValue pv = this.clipsEnv.getWorldEnv().eval(evalString);

        ArrayList<Object> visibleObjects = new ArrayList<Object>();
        //dodajemy informacje o agencie
        for (Agent agent : this.agents) {
        	if (agent.getId().equalsIgnoreCase(agentId)) {
        		visibleObjects.add(agent.toString());
        		break;
        	}
        }
        
        
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

    private void loadAgents() {
    	loadWoodmens();
    	loadCouriers();
    }
    
    private void loadWoodmens() {
    	try {
            String evalString = "(find-all-facts ((?d drwal)) TRUE)";
            PrimitiveValue pv1 = clipsEnv.getWorldEnv().eval(evalString);

            for (int i = 0; i < pv1.size(); i++) {
                Woodman woodmanTmp = new Woodman();
                woodmanTmp.loadFromClips(pv1.get(i));
                
                MapFrame mapFrame = this.getFrameById(woodmanTmp.getMapFrame().getId());
                woodmanTmp.setMapFrame(mapFrame);
                
                for (int k = 0; k < this.agents.size(); k++) {
                	if (this.agents.get(k).getId().equalsIgnoreCase(woodmanTmp.getId())) {
                		woodmanTmp.setPathToClipsFile(this.agents.get(k).getPathToClipsFile());
                		this.agents.set(k, woodmanTmp);
                		break;
                	}
                }
            }

        } catch (Exception e) {
        }
    }
    
    private void loadCouriers() {
    	try {
            String evalString = "(find-all-facts ((?p poslaniec)) TRUE)";
            PrimitiveValue pv1 = clipsEnv.getWorldEnv().eval(evalString);

            for (int i = 0; i < pv1.size(); i++) {
                Courier courierTmp = new Courier();
                courierTmp.loadFromClips(pv1.get(i));
                
                MapFrame mapFrame = this.getFrameById(courierTmp.getMapFrame().getId());
                courierTmp.setMapFrame(mapFrame);
                
                for (int k = 0; k < this.agents.size(); k++) {
                	if (this.agents.get(k).getId().equalsIgnoreCase(courierTmp.getId())) {
                		courierTmp.setPathToClipsFile(this.agents.get(k).getPathToClipsFile());
                		this.agents.set(k, courierTmp);
                		break;
                	}
                }
            }

        } catch (Exception e) {
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
                ArrayList<String> grodyPunktowKoncowych = new ArrayList<>();

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
                    String grodID = "";
                    for (int k = 0; k < sasiedziMogacyBycGrodem.length; k++) {
                        Point point = sasiedziMogacyBycGrodem[k];

                        //szukamy id kratki, ktora nalezy do grodu
                        int mapFrame = 0;
                        outer:for (int g = 0; g < this.mapFrames.length; g++) {
                        	for (int h = 0; h < this.mapFrames[g].length; h++) {
                        		if (this.mapFrames[g][h].getX() == point.x && this.mapFrames[g][h].getY() == point.y) {
                        			mapFrame = this.mapFrames[g][h].getId();
                        			break outer;
                        		}
                        	}
                        }
                        int find = this.towns.indexOf(new Town("", mapFrame, 0, 0));
                        
                        if (find != -1) {
                            grodID = this.towns.get(find).getNazwa();
                            break;
                        }

                    }

                    if (grodID.equals("")) {
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
                    boolean platna;
                    if(typ==0)
                    {
                        nazwatypu = "nieplatna";
                        platna = false;
                    }
                    else
                    {
                        nazwatypu = "platna";
                        platna = true;
                    }
                    
                    //szukamy id kratki, ktora nalezy do drogi
                    int mapFrame = getFrameIdByCoords(aktualnyPunkt.x,aktualnyPunkt.y);
                    
                    aktualnyKawalekDrogi = new Road(nazwatypu + i, mapFrame, grodyPunktowKoncowych.get(0), grodyPunktowKoncowych.get(1), "utwardzona", platna, iteration, -1);
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

                //ma koniec szukamy kratki z ostatnim punktem koncowym
                int mapFrame = getFrameIdByCoords(punktyKoncowe.get(1).x, punktyKoncowe.get(1).y);
                Road tmp = roadForwards.get(roadForwards.size() - 1);
                Road koncowyKawalek = new Road(tmp.getId(), mapFrame, tmp.getSourceTown(), tmp.getDestinationTown(),
                		tmp.getType(), tmp.isPaid(), tmp.getCurrentPartNo() + 1, -1);
                roadForwards.add(koncowyKawalek);
                
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
        initializeRoads();
    }

    /**
     * ustawia prawodopobienstwo napasci oraz nawierzchnie drogi
     */
    private void initializeRoads() {
    	Set<String> roadsId = new HashSet<String>();
    	for (Road road : this.roads) {
    		roadsId.add(road.getId());
    	}
    	
    	String[] roadTypes = {"utwardzona", "nieutwardzona"};
    	Random random = new Random();
    	for (String roadId : roadsId) {
    		String roadType = roadTypes[random.nextInt(roadTypes.length)];
    		
    		double robberyProbabilityFreeRoad = random.nextDouble() * 0.3 + 0.3;
    		double robberyProbabilityPaidRoad = random.nextDouble() * 0.3;
    		for (Road road : this.roads) {
    			if (road.getId().equalsIgnoreCase(roadId)) {
    				road.setType(roadType);
    				
    				if (!road.isPaid()) {
    					road.setRobberyProbability(robberyProbabilityFreeRoad);
    				} else {
    					road.setRobberyProbability(robberyProbabilityPaidRoad);
    				}
    				
    			}
    		}
    	}
    }
    
    @Override
    public String toString() {
        StringBuffer sbuf = new StringBuffer();

        sbuf.append("(mapa ").append(this.height).append(" ").append(this.width).append(" )\n");
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
        
       // for (Agent agent : this.agents) {
       // 	sbuf.append(agent.toString()).append("\n");
       // }
        
        return sbuf.toString();
    }

    public void saveToClips() {
        for (String fact : this.toString().split("\n")) {
            this.clipsEnv.getWorldEnv().assertString(fact);
        }
    }

    public MapFrame getFrameById(int id) {
    	for (int i = 0; i < this.mapFrames.length; i++) {
    		for (int j = 0; j < this.mapFrames[i].length; j++) {
    			if (this.mapFrames[i][j].getId() == id) {
    				return this.mapFrames[i][j];
    			}
    		}
    	}
    	
    	return null;
    }
    
    public int getFrameIdByCoords(int x, int y) {
    	for (int i = 0; i < this.mapFrames.length; i++) {
    		for (int j = 0; j < this.mapFrames[i].length; j++) {
    			if (this.mapFrames[i][j].getX() == x && this.mapFrames[i][j].getY() == y) {
    				return this.mapFrames[i][j].getId();
    			}
    		}
    	}
    	
    	return -1;
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

	public ArrayList<Agent> getAgents() {
		return agents;
	}

	public void setAgents(ArrayList<Agent> agents) {
		this.agents = agents;
	}
}
