;template bardziej abstrakcyjny - opisuje polozenie drogi na mapie
(deftemplate drogaKratki 
	(slot id) ;numer, ktory pozwoli na późniejszą referencję do tej drogi
	(slot kratka) ;współrzedne kratki w której zawiera się droga
	(slot kratkaY)
	(slot numerOdcinka) ;okresla kolejny odcinek drogi
)
;przykladowo stworzone fakty na podstawie template'a
;(assert (drogaKratki (id 1) (kratkaX 1) (kratkaY 1) ))
;(assert (drogaKratki (id 1) (kratkaX 2) (kratkaY 2) ))
;(assert (drogaKratki (id 2) (kratkaX 2) (kratkaY 2) ))

(deftemplate droga
	(slot id) ;odniesienie do id w template drogaKratki
	(slot skadX) ;współrzedne pierwszej klatki na drodze
	(slot skadY)
	(slot dokadX) ;wspolrzedne ostatniej kratki na drodze
	(slot dokadY)
	(slot dokadGrod) ;nazwa grodu, do ktorej prowadzi dana droga
	(slot platna) ;czy droga jest platna czy też bezplatna
)

(deftemplate blokada
	(slot droga) ;droga, na której znajduje sie blokada
	(slot nazwa)
	(slot kratkaX) ;wsp kratki na ktorej znajduje sie blokada, ktora nalezy do drogi, do ktorej odnosimy sie poprzez id
	(slot kratkaY)
)

(deftemplate grod
	(slot nazwa) ;to bedzie id grodu
	(slot kratkaLGRX) ;wsp. grodu na mapie - lewy gorny rog
	(slot kratkaLGRY)
	(slot kratkaPDRX) ;wsp. grodu na mapie - prawy dolny rog
	(slot kratkaPDRY)
	(slot liczbaMieszkancow)
	(slot wspAktywnosciStrazy)
)

(deftemplate paczka
	(slot waga)
	(slot grodStart) ;grod, w ktorym paczka sie znajduje
	(slot grodKoniec) ;grod, do ktorego paczka jest przeznaczona
)

;przedmiot opisuje narzedzia dla drwala, uzbrojenie dla rycerza oraz konie dla poslanca
(deftemplate przedmiot 
	(slot nazwa) ;to będzie id
	(slot grod) ;grod, w ktorego magazynie sie znajduje
	(slot zuzycie) ;procentowy wskaznik zuzycia
	(slot cena)
)

;jako, że drzewa pokrywaja cala mape, nie ma sensu grupowac tego w lasy
(deftemplate drzewo
	(slot rodzajDrzewa)
	(slot kratkaX) ;wspolrzedne kratki, na ktorej sie znajduje - zajmuje tylko 1 kratke
	(slot kratkaY)
	(slot stan) ;sciete czy niesciete
)

;CZYNNIK NIEDETERMINISTYCZNY
(deftemplate kleska
	(slot kratkaLGRX) ;współrzedne obszaru kleski - lewy gorny rog
	(slot kratkaLGRY)
	(slot bokObszaru) ;dlugosc boku kwadratowego obszaru kleski
	(slot niszczenieLasu) ;procent zniszczonych drzew na obszarze kleski
	(slot oslabianieAgentow) ;liczba punktow energii jaka zabiera znajdujacym sie na jej obszarze agentom
	(slot zabijanieMieszkancow) ;liczba mieszkancow, ktorych zabija, gdy w jej obszarze znajduje sie grod
)

;CZYNNIK NIEDETERMINISTYCZNY
(deftemplate rozbojnicy
	(slot droga) ;droga, na ktorej sie znajduja
	(slot kratkaX) ;kratka drogi, na ktorej sie znajduja
	(slot kratkaY)
	(slot zabieraniePaczek) ;procent paczek, ktore zabieraja poslancom
	(slot zabieranieZlota) ;procent zlota, jakie zabieraja agentom
)

;trzeba uwzglednic to co Piotrek zrobi
(deftemplate agent
	(slot id)
	(slot typ) ;czyli czy jest to poslaniec, kupiec, rycerz itd.
	(slot kratkaX) ;polozenie agenta na mapie
	(slot kratkaY)
	(slot predkosc) ;kratki na iteracje
	(slot poleWidzenia) ;liczba kratek, ktore agent widzi przed soba, za soba oraz ze swojej lewej lub prawej strony
	(slot mozliwyRuch) ;liczba kratek ruchu - na poczatku kazdej itearcji rowne tyle samo co predkosc
)

;agent DRWAL

;agent POSLANIEC

;itd..


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

;AKCJE ZŁODZIEJ
(deftemplate akcjaKradnij
	(slot idAgenta) ;chodzi o id złodzieja
	(slot rozmiarSkoku) ;chodzi o okreslenie rozmiarow skoku: mały, średni, duży 
)

;AKCJE DRWAL
(deftemplate akcjaScinanieDrzew
	(slot idAgenta) ;chodzi oczywiście o id drwala
)

;tylko agenci : RYCERZ i SMOK
(deftemplate akcjaAtak
	(slot idAgenta) ;agent atakujacy
	(slot idOfiary) ;agent aatakowany
	(slot rodzajAtaku) ;zarowno smok jak i rycerz maja 3 ataki, wiec tu chodzi o id tego ataku. Id będzie liczbą od 1 do 3
)