(deftemplate kratka
	(slot id)
	(slot pozycjaX) ;pozycja kratki na mapie
	(slot pozycjaY)
)
;chyba nie ma potrzby zeby droga by³a definiowana jako ca³y odcinek
(deftemplate droga
	(slot idKratki) 
	(slot skadGrod) ;nazwa grodu, z ktorego zaczyna sie dana droga
	(slot dokadGrod) ;nazwa grodu, do ktorego prowadzi dana droga
	(slot platna) ;czy droga jest platna czy teï¿½ bezplatna
	(slot nawierzchnia) ;rodzaj nawierzchni drogi: utwardzona lub nieutwardzona
	
)
;template bardziej abstrakcyjny - opisuje polozenie drogi na mapie
;(deftemplate drogaKratki 
;	(slot id) ;numer, ktory pozwoli na pï¿½niejszï¿½ referencjï¿½ do tej drogi
;	(slot idKratki) ;id kratki w ktï¿½rej zawiera siï¿½ droga
;	(slot numerOdcinka) ;okresla numer odcinka drogi
;)


;(deftemplate droga
;	(slot id) ;odniesienie do id w template drogaKratki
;	(slot skad) ;id pierwszej klatki na drodze
;	(slot dokad) ;id ostatniej kratki na drodze
;	(slot skadGrod) ;nazwa grodu, z ktorego zaczyna sie dana droga
;	(slot dokadGrod) ;nazwa grodu, do ktorego prowadzi dana droga
;	(slot platna) ;czy droga jest platna czy teï¿½ bezplatna
;	(slot nawierzchnia) ;rodzaj nawierzchni drogi: utwardzona lub nieutwardzona
;	(slot dlugosc) ;dlugosc drogi liczona w kratkach
;)

(deftemplate blokada
	;(slot droga) ;droga, na ktï¿½rej znajduje sie blokada
	(slot nazwa)
	(slot idKratki) ;id kratki, na ktorej znajduje sie blokada, ktora nalezy do drogi, do ktorej odnosimy sie poprzez id
)

(deftemplate grod
	(slot nazwa) ;to bedzie id grodu
	(slot kratkaLGR) ;wsp. grodu na mapie - lewy gorny rog
	(slot kratkaPDR) ;wsp. grodu na mapie - prawy dolny rog
	(slot liczbaMieszkancow)
	(slot wspAktywnosciStrazy)
)

(deftemplate paczka
        (slot id)
	(slot waga)
	(slot grodStart) ;grod, w ktorym paczka sie znajduje
	(slot grodKoniec) ;grod, do ktorego paczka jest przeznaczona
)

;przedmiot opisuje narzedzia dla drwala, uzbrojenie dla rycerza oraz konie dla poslanca
(deftemplate przedmiot 
	(slot id)
	(slot nazwa) ;to bï¿½dzie id
	(slot grod) ;grod, w ktorego magazynie sie znajduje
	(slot zuzycie) ;procentowy wskaznik zuzycia
	(slot cena)
)

;jako, ï¿½e drzewa pokrywaja cala mape, nie ma sensu grupowac tego w lasy
(deftemplate drzewo
	(slot rodzajDrzewa)
	(slot idKratki) ;id kratki, na ktorej sie znajduje - zajmuje tylko 1 kratke
	(slot stan) ;sciete czy niesciete
)

;CZYNNIK NIEDETERMINISTYCZNY
(deftemplate kleska
	(slot kratkaLGR) ;wspï¿½rzedne obszaru kleski - lewy gorny rog
	(slot bokObszaru) ;dlugosc boku kwadratowego obszaru kleski
	(slot niszczenieLasu) ;procent zniszczonych drzew na obszarze kleski
	(slot oslabianieAgentow) ;liczba punktow energii jaka zabiera znajdujacym sie na jej obszarze agentom
	(slot zabijanieMieszkancow) ;liczba mieszkancow, ktorych zabija, gdy w jej obszarze znajduje sie grod
)

;CZYNNIK NIEDETERMINISTYCZNY
(deftemplate rozbojnicy
	(slot idDrogi) ;droga, na ktorej sie znajduja
	(slot odcinekDrogi) ;odcinek drogi, na ktorej sie znajduja
	(slot zabieraniePaczek) ;procent paczek, ktore zabieraja poslancom
	(slot zabieranieZlota) ;procent zlota, jakie zabieraja agentom
)

;agent POSLANIEC
(deftemplate poslaniec 
	(slot udzwig) 
	(slot kon) 
	(slot id) 
	(multislot paczki)
	(slot poleWidzenia) 
	(slot predkosc) 
	(slot energia) 
	(slot strataEnergii) 
	(slot odnawianieEnergii) 
	(slot zloto)
	(slot mozliwyRuch)
	(slot idKratki)
)

;agent KUPIEC
(deftemplate kupiec 
	(slot pojemnoscMagazynu) 
	(slot id)
	(multislot przedmioty)
	(slot mozliwyRuch)
	(slot idKratki)
	(slot poleWidzenia) 
	(slot predkosc) 
	(slot energia) 
	(slot strataEnergii) 
	(slot odnawianieEnergii) 
	(slot zloto)
)

;agent zlodziej
(deftemplate zlodziej 
	(slot id) 
	(slot mozliwyRuch) 
	(slot idKratki) 
	(slot poleWidzenia) 
	(slot predkosc) 
	(slot energia) 
	(slot strataEnergii) 
	(slot odnawianieEnergii) 
	(slot zloto)
)

;agent drwal
(deftemplate drwal 
	(slot udzwig)  
	(slot siekiera) 
	(slot woz) 
	(slot scieteDrewno) 
	(slot id) 
	(slot mozliwyRuch) 
	(slot idKratki) 
	(slot poleWidzenia) 
	(slot predkosc) 
	(slot energia) 
	(slot strataEnergii)
	(slot odnawianieEnergii) 
	(slot zloto)
)

;agent rycerz
(deftemplate rycerz
	(slot zbroja)
	(multislot ataki)
	(slot id) 
	(slot mozliwyRuch) 
	(slot idKratki) 
	(slot poleWidzenia) 
	(slot predkosc)
	(slot energia) 
	(slot strataEnergii) 
	(slot odnawianieEnergii) 
	(slot zloto)
)

;agent smok
(deftemplate smok
	(multislot ataki)
	(slot id) 
	(slot mozliwyRuch) 
	(slot idKratki) 
	(slot poleWidzenia) 
	(slot predkosc)
	(slot energia) 
	(slot strataEnergii) 
	(slot odnawianieEnergii) 
	(slot zloto)
)

;WYCINEK SWIATA, cyzli widzialna czesc swiata przez kazdego agenta
(deftemplate widzialnaCzescSwiata
	(slot idAgenta)
	(slot idKratki) ;id widzialnej kratki
)

;AKCJE AGENTOW: wspolne
(deftemplate akcjaPrzemieszczaniePoDrodze
	(slot idAgenta)
	(slot idDrogi)
	(slot kierunek)	;w ktora strone sie przemieszcza
)
(deftemplate akcjaPrzemieszczanie
	(slot idAgenta)
	(slot ileKratek) ;o ile kratek przemiescic agenta
	(slot kierunek) ;w ktora strone przemiescic agenta
)
;mozliwa tylko wtedy, gdy dany agent spotyka kupca
;w przypadku kupca chodzi o kupowanie z grodu lub drewna od drwala
(deftemplate akcjaKupowanie
	(slot idAgenta)
	(slot idPrzedmiotu) ;jaki przedmiot chce kupic
	(slot idSprzedawcy) ;albo id kupca w przypadku agenta innego niz kupiec albo id grodu lub id drwala w przypadku agenta typu kupiec
)
(deftemplate akcjaOdpoczywanie
	(slot idAgenta)
	(slot dlugoscOdpoczynku) ;w liczbie iteracji
)
(deftemplate akcjaSpotkanieWroga
	(slot idAgenta)
	(slot idWroga)
	(slot podjetaAkcja) ;czyli co robi agent jak zobaczy wroga, mozliwe opcje: ucieczka, walka
)

;AKCJE POSLANIEC
(deftemplate akcjaWezPaczke
	(slot idAgenta) ;chodzi o id poslanca
	(slot idPaczki) 
)

;AKCJE KUPIEC

;AKCJE Zï¿½ODZIEJ
(deftemplate akcjaKradnij
	(slot idAgenta) ;chodzi o id zï¿½odzieja
	(slot rozmiarSkoku) ;chodzi o okreslenie rozmiarow skoku: maï¿½y, ï¿½redni, duï¿½y 
)

;AKCJE DRWAL
(deftemplate akcjaScinanieDrzew
	(slot idAgenta) ;chodzi oczywiï¿½cie o id drwala
)

;tylko agenci : RYCERZ i SMOK
(deftemplate akcjaAtak
	(slot idAgenta) ;agent atakujacy
	(slot idOfiary) ;agent aatakowany
	(slot rodzajAtaku) ;zarowno smok jak i rycerz maja 3 ataki, wiec tu chodzi o id tego ataku. Id bï¿½dzie liczbï¿½ od 1 do 3
)


