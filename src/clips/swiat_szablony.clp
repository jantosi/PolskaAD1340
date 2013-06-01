(deftemplate kratka
	(slot id)
	(slot pozycjaX) ;pozycja kratki na mapie
	(slot pozycjaY)
)
;chyba nie ma potrzby zeby droga by�a definiowana jako ca�y odcinek
(deftemplate droga
    (slot id)
	(slot idKratki) 
    (slot prawdopodobienstwoNapasci) ;z przedzialu (0 ; 1)
	(slot skadGrod) ;nazwa grodu, z ktorego zaczyna sie dana droga
	(slot dokadGrod) ;nazwa grodu, do ktorego prowadzi dana droga
	(slot platna) ;czy droga jest platna czy te� bezplatna
	(slot nawierzchnia) ;rodzaj nawierzchni drogi: utwardzona lub nieutwardzona
    (slot nrOdcinka)
    (slot maxOdcinek)
)


(deftemplate blokada
	(slot id)
	(slot nazwa)
	(slot idKratki) ;id kratki, na ktorej znajduje sie blokada, ktora nalezy do drogi, do ktorej odnosimy sie poprzez id
)

(deftemplate grod
	(slot nazwa) ;to bedzie id grodu
	(slot idKratki) ;idKratki nalezacej do danego grodu
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
	(slot nazwa) ;to b�dzie id
	(slot grod) ;grod, w ktorego magazynie sie znajduje
	(slot zuzycie) ;procentowy wskaznik zuzycia
	(slot cena)
)

(deftemplate kon
    (slot id)
    (slot grod)
    (slot idAgenta) ;id poslanca, ktory go ew posiada
    (slot udzwig)
    (slot predkosc)
    (slot zmeczenieJezdzcy) ;o ile procent mniejsze sa straty jezdzcy w prownaniu do poruszania sie na pieszo
    (slot cena)
    (slot zuzycie)
    (slot predkoscZuzycia)
)

;jako, �e drzewa pokrywaja cala mape, nie ma sensu grupowac tego w lasy
(deftemplate drzewo
	(slot rodzajDrzewa)
	(slot idKratki) ;id kratki, na ktorej sie znajduje - zajmuje tylko 1 kratke
	(slot stan) ;sciete czy niesciete
)

;CZYNNIK NIEDETERMINISTYCZNY
(deftemplate kleska
	(slot id)
	(slot idKratki) ;wsp�rzedne obszaru kleski - lewy gorny rog
	(slot niszczenieLasu) ;procent zniszczonych drzew na obszarze kleski
	(slot oslabianieAgentow) ;liczba punktow energii jaka zabiera znajdujacym sie na jej obszarze agentom
	(slot zabijanieMieszkancow) ;liczba mieszkancow, ktorych zabija, gdy w jej obszarze znajduje sie grod
)

;CZYNNIK NIEDETERMINISTYCZNY
(deftemplate rozbojnicy
	(slot idKratki) ;droga, na ktorej sie znajduja
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
    (slot dodatekPredkosc) ;dodatek wynikajacy z tego po jakiej powierzchni sie porusza 
	(slot energia) 
	(slot strataEnergii) 
	(slot odnawianieEnergii) 
	(slot zloto)
	(slot mozliwyRuch)
	(slot idKratki)
    (slot cel) ;dokad zmierza dany agent , np. nazwa grodu, "zabicieSmoka", "rabanieDrewna" itp.
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
    (slot dodatekPredkosc) ;dodatek wynikajacy z tego po jakiej powierzchni sie porusza 
	(slot energia) 
	(slot strataEnergii) 
	(slot odnawianieEnergii) 
	(slot zloto)
    (slot cel) ;dokad zmierza dany agent , np. nazwa grodu, "zabicieSmoka", "rabanieDrewna" itp.
)

;agent zlodziej
(deftemplate zlodziej 
	(slot id) 
	(slot mozliwyRuch) 
	(slot idKratki) 
	(slot poleWidzenia) 
	(slot predkosc)
    (slot dodatekPredkosc) ;dodatek wynikajacy z tego po jakiej powierzchni sie porusza 
	(slot energia) 
	(slot strataEnergii) 
	(slot odnawianieEnergii) 
	(slot zloto)
    (slot cel) ;dokad zmierza dany agent , np. nazwa grodu, "zabicieSmoka", "rabanieDrewna" itp.
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
    (slot dodatekPredkosc) ;dodatek wynikajacy z tego po jakiej powierzchni sie porusza 
	(slot energia) 
	(slot strataEnergii)
	(slot odnawianieEnergii) 
	(slot zloto)
    (slot cel) ;dokad zmierza dany agent , np. nazwa grodu, "zabicieSmoka", "rabanieDrewna" itp.
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
    (slot dodatekPredkosc) ;dodatek wynikajacy z tego po jakiej powierzchni sie porusza 
	(slot energia) 
	(slot strataEnergii) 
	(slot odnawianieEnergii) 
	(slot zloto)
    (slot cel) ;dokad zmierza dany agent , np. nazwa grodu, "zabicieSmoka", "rabanieDrewna" itp.
)

;agent smok
(deftemplate smok
	(multislot ataki)
	(slot id) 
	(slot mozliwyRuch) 
	(slot idKratki) 
	(slot poleWidzenia) 
	(slot predkosc)
    (slot dodatekPredkosc) ;dodatek wynikajacy z tego po jakiej powierzchni sie porusza 
	(slot energia) 
	(slot strataEnergii) 
	(slot odnawianieEnergii) 
	(slot zloto)
    (slot cel) ;dokad zmierza dany agent , np. nazwa grodu, "zabicieSmoka", "rabanieDrewna" itp.
)

;WYCINEK SWIATA, cyzli widzialna czesc swiata przez kazdego agenta
(deftemplate widzialnaCzescSwiata
	(slot idAgenta)
	(slot idKratki) ;id widzialnej kratki
)

;AKCJE AGENTOW: wspolne
(deftemplate akcjaPrzemieszczaniePoDrodze
	(slot idAgenta)
	(slot ileKratek)
    (slot docelowyGrod)
)
(deftemplate akcjaPrzemieszczanie
	(slot idAgenta)
	(slot ileKratek) ;o ile kratek przemiescic agenta
	(slot kierunek) ;w ktora strone przemiescic agenta
)
(deftemplate akcjaOminiecieBlokady
       (slot idAgenta)
       (slot idBlokady)
       (slot kierunek) ;czy ominiecie z lewej czy z prawej
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
(deftemplate kupienieKonia
    (slot idAgenta)
    (slot idKonia)
)
;AKCJE KUPIEC

;AKCJE Z�ODZIEJ
(deftemplate akcjaKradnij
	(slot idAgenta) ;chodzi o id z�odzieja
	(slot rozmiarSkoku) ;chodzi o okreslenie rozmiarow skoku: ma�y, �redni, du�y 
)

;AKCJE DRWAL
(deftemplate akcjaScinanieDrzew
	(slot idAgenta) ;chodzi oczywi�cie o id drwala
)

;tylko agenci : RYCERZ i SMOK
(deftemplate akcjaAtak
	(slot idAgenta) ;agent atakujacy
	(slot idOfiary) ;agent aatakowany
	(slot rodzajAtaku) ;zarowno smok jak i rycerz maja 3 ataki, wiec tu chodzi o id tego ataku. Id b�dzie liczb� od 1 do 3
)


