;template'y kontrolne
(deftemplate modyfikacjaStratEnergiiPoslanca (slot idPoslanca))
(deftemplate modyfikacjaPredkosciAgenta (slot idAgenta))
(deftemplate zregenerowanoAgenta (slot idAgenta))

;REGULY
;regula okresla parametry poslanca po zakupieniu konia, czyli
;po sprawdzeniu istnienia faktu kupienieKonia
(defrule sprawdzKupienieKoniaPoslaniec
    ?poslaniec <- (poslaniec (id ?poslaniecId) (kon ?kon) (paczki $?paczki))
    ?kupienieKonia <- (kupienieKonia (idAgenta ?poslaniecId)(idKonia ?kupionyKon))
    (not (akcjaOdpoczywanie (idAgenta ?id)))
=>
    ;pobieramy index kupionego konia z bazy wiedzy        
    (bind ?kupionyKon (nth$ 1 (find-fact ((?konTmp kon)) (eq ?konTmp:id ?kupionyKon))))        
     
    ;wyciagamy poszczegolne wartosci pol z obiektu kon o okreslonym wyzej indeksie        
    (bind ?konId (fact-slot-value ?kupionyKon id))        
    (bind ?konUdzwig (fact-slot-value ?kupionyKon udzwig))
    (bind ?konPredkosc (fact-slot-value ?kupionyKon predkosc))        
    
    ;modyfikujemy parametry poslanca uzwgledniajac zakupionego konia
    (modify ?poslaniec (udzwig ?konUdzwig) (predkosc ?konPredkosc) (poleWidzenia ?konPredkosc) (kon ?konId))   
        
    (printout t "Poslaniec: " ?poslaniecId " kupil konia o predkosci: " ?konPredkosc crlf)
    ;usuwamy akcje kupienia konia        
    (retract ?kupienieKonia)
    
    ;jak poslaniec kupi konia to nalezy jeszcze raz wywolac regule obliczajaca straty energii
    ;co robimy poprzez usuniecie akcji blokujacej: modyfikacjaStratEnergiiPoslanca
    (bind ?czyModyfikacjaPoslanca (any-factp ((?m modyfikacjaStratEnergiiPoslanca)) (eq ?m:idPoslanca ?poslaniecId)))
    (if (eq ?czyModyfikacjaPoslanca TRUE)
    then
        (bind ?modyfikacjaStratEnergiiPoslancaId (nth$ 1 (find-fact ((?m modyfikacjaStratEnergiiPoslanca)) (eq ?m:idPoslanca ?poslaniecId))))     
        (retract ?modyfikacjaStratEnergiiPoslancaId )  
    )
)

;regula aktualizujaca straty energii poslanca z uwzglednieniem paczek jakie niesie
(defrule obliczStartyEnergiiPoslanca
    ?poslaniec <- (poslaniec (id ?poslaniecId) (kon ?kon) (paczki $?paczki))
    
    ;sprawdzamy czy poslaniec w danej turze nie byl jeszcze modyfikowany
    ;jest to fakt kontrolny, ktory na poczatku kazdej tury NIE ISTNIEJE w bazie wiedzy
    ;jest on wstawiany dopiero po modyfikacji poslanca    
    (not (modyfikacjaStratEnergiiPoslanca (idPoslanca ?poslaniecId)))
    (not (akcjaOdpoczywanie (idAgenta ?id)))
=>
    (bind ?sumaWagPaczek 0)
    (loop-for-count (?i 0 (- (length $?paczki) 1)) do 
        ;pobieramy id kolejnej, posiadanej przez agenta paczki        
        (bind ?paczkaId (nth$ (+ ?i 1) $?paczki))
        
        ;znajdujemy index faktu paczki w bazie wiedzy
        (bind ?paczka (nth$ 1 (find-fact ((?p paczka))(eq ?p:id ?paczkaId))))
                
        (bind ?paczkaWaga (fact-slot-value ?paczka waga))
        (bind ?sumaWagPaczek (+ ?sumaWagPaczek ?paczkaWaga))                
    )
   
    (bind ?strataEnergii (round (+ (* ?sumaWagPaczek 0.2) 2)))
    ;sprawdzamy czy poslaniec ma konia
    (if (not (eq ?kon nil))
    then
        (bind ?konIndex (nth$ 1 (find-fact ((?konTmp kon)) (eq ?konTmp:id ?kon))))
        (bind ?konZmeczenieJezdzcy (fact-slot-value ?konIndex zmeczenieJezdzcy))
        
        (bind ?strataEnergii (round (- ?strataEnergii (* ?strataEnergii ?konZmeczenieJezdzcy))))
    )    
    
    (printout t "Poslaniec: " ?poslaniecId " - nowa starta energii: " ?strataEnergii crlf)
    
    (modify ?poslaniec (strataEnergii ?strataEnergii))
    
    ;fakt kontrolny, za pomoca ktorego oznaczamy, 
    ;ze dany poslaniec w danej iteracji zostal juz zmodfikowany    
    (assert (modyfikacjaStratEnergiiPoslanca (idPoslanca ?poslaniecId)))
    
)

;regula okreslajaca dodatki predkosci zwiazane z tym po jakiej nawierzchni się on porusza
(defrule okreslDodatekPredkosciRuchuAgenta
    (or	
		?agent <- (poslaniec (id ?id)(idKratki ?idKratki)(predkosc ?predkosc))
		?agent <- (rycerz (id ?id)(idKratki ?idKratki)(predkosc ?predkosc))
		?agent <- (drwal (id ?id)(idKratki ?idKratki)(predkosc ?predkosc))
		?agent <- (kupiec (id ?id)(idKratki ?idKratki)(predkosc ?predkosc))
		?agent <- (zlodziej (id ?id)(idKratki ?idKratki)(predkosc ?predkosc))
		?agent <- (smok (id ?id)(idKratki ?idKratki)(predkosc ?predkosc))
	) 
    ;fakt kontrolny, zapobiegajacy nieskonczonemu wywolywaniu tej reguly
    (not (modyfikacjaPredkosciAgenta (idAgenta ?id)))
    (not (akcjaOdpoczywanie (idAgenta ?id)))
=>   
    (bind ?dodatPredkosc 0)    
    (if (not (eq (sub-string 1 4 ?id) "smok") )
    then
        ;w zaleznosci od tego na czym stoi agent, tym otrzymuje inny bonus do predkosci        
        (bind ?czyDrzewo (any-factp ((?drz drzewo))(and (eq ?drz:idKratki ?idKratki) (eq ?drz:stan niesciete))))
        (bind ?czyScieteDrzewo (any-factp ((?drz drzewo)) (and (eq ?drz:idKratki ?idKratki) (eq ?drz:stan sciete))))        
        (bind ?czyDrogaBezplatna (any-factp ((?d droga)) (and (eq ?d:idKratki ?idKratki) (eq ?d:platna false))))        
        (bind ?czyDrogaPlatna (any-factp ((?d droga)) (and (eq ?d:idKratki ?idKratki) (eq ?d:platna true))))
        (bind ?czyDrogaUtwardzona (any-factp ((?d droga)) (and (eq ?d:idKratki ?idKratki) (eq ?d:nawierzchnia utwardzona))))
        (bind ?czyDrogaNieutwardzona (any-factp ((?d droga)) (and (eq ?d:idKratki ?idKratki) (eq ?d:nawierzchnia nieutwardzona))))
        
        (if (eq ?czyDrzewo TRUE)
        then
            (bind ?dodatPredkosc (+ ?dodatPredkosc 1))            
        )
        (if (eq ?czyScieteDrzewo TRUE)
        then
            (bind ?dodatPredkosc (+ ?dodatPredkosc 2))     
        ) 
        (if (eq ?czyDrogaBezplatna TRUE)
        then
            (bind ?dodatPredkosc (+ ?dodatPredkosc 3))      
        ) 
        (if (eq ?czyDrogaPlatna TRUE)
        then
            (bind ?dodatPredkosc (+ ?dodatPredkosc 4))      
        ) 
        (if (eq ?czyDrogaUtwardzona TRUE)
        then
            (bind ?dodatPredkosc (+ ?dodatPredkosc 1))     
        )
        (if (eq ?czyDrogaNieutwardzona TRUE)
        then
            (bind ?dodatPredkosc (+ ?dodatPredkosc 0))        
        )         
    else ;chyba, ze jest to smok, ktory niezaleznie od nawierzchni porusza sie z ta sama predkoscia
        (bind ?dodatPredkosc 5)    
    )    
    
    ;modyfikujemy dodatek predkosci agenta oraz jego pole widzenia
    (modify ?agent (dodatekPredkosc ?dodatPredkosc)(poleWidzenia (+ ?dodatPredkosc ?predkosc)))    
    (printout t "Zmodyfikowano dodatek predkosci agenta: " ?id ", dodatek predkosci = " ?dodatPredkosc crlf)
    
    ;umieszczamy fakt kontrolny, ze dla danego agenta dodatkowa predkosc zostala juz zmodyfikowana
    (assert (modyfikacjaPredkosciAgenta (idAgenta ?id)))
)
;regula pozwalajaca przmieszczac agentow wzdluz danej drogi
(defrule przemieszczaniePoDrodze
    (or	
		(and 
            ?agent <- (poslaniec (id ?id)(mozliwyRuch ?mozliwyRuch)(idKratki ?idKratki)(cel ?cel)(energia ?energia)(strataEnergii ?strE))
		    (modyfikacjaStratEnergiiPoslanca (idPoslanca ?id))
        )
        ?agent <- (rycerz (id ?id)(mozliwyRuch ?mozliwyRuch)(idKratki ?idKratki)(cel ?cel)(energia ?energia)(strataEnergii ?strE))
		?agent <- (drwal (id ?id)(mozliwyRuch ?mozliwyRuch)(idKratki ?idKratki)(cel ?cel)(energia ?energia)(strataEnergii ?strE))
		?agent <- (kupiec (id ?id)(mozliwyRuch ?mozliwyRuch)(idKratki ?idKratki)(cel ?cel)(energia ?energia)(strataEnergii ?strE))
		?agent <- (zlodziej (id ?id)(mozliwyRuch ?mozliwyRuch)(idKratki ?idKratki)(cel ?cel)(energia ?energia)(strataEnergii ?strE))
		?agent <- (smok (id ?id)(mozliwyRuch ?mozliwyRuch)(idKratki ?idKratki)(cel ?cel)(energia ?energia)(strataEnergii ?strE))
	)
    ?droga <- (droga (id ?drogaId)(idKratki ?idKratki)(dokadGrod ?cel)(nrOdcinka ?nrOdc)(maxOdcinek ?maxOdcinek))    
    ?akcja <- (akcjaPrzemieszczaniePoDrodze (idAgenta ?id)(ileKratek ?ileKratek)(docelowyGrod ?cel))
    (iteracja ?iteracja)
    (not (akcjaOdpoczywanie (idAgenta ?id)))
    ?modyfikacjaPredkosci <- (modyfikacjaPredkosciAgenta (idAgenta ?id))
=>
    sprawdzamy o ile moze sie przesunac dany agent
    (if (<= ?ileKratek ?mozliwyRuch) 
    then
       (bind ?ilePrzesunac ?ileKratek)
    else
        (bind ?ilePrzesunac ?mozliwyRuch)
    )
               
    ;sprawdzamy czy nie przejdziemy docelowego grodu
    (if (> (+ ?nrOdc ?ilePrzesunac) ?maxOdcinek)
    then     
        (bind ?ilePrzesunac (- ?maxOdcinek ?nrOdc))
    )
    
    ;sprawdzamy czy agent ma odpowiednia ilosc energii aby sie przmiescic
    ;jesli nie ma, automatycznie musi odpoczywac dana iteracje    
    (bind ?potrzebnaEnergia (* ?strE ?ilePrzesunac))    
    (if (>= (- ?energia ?potrzebnaEnergia) 6 )
    then                   
        (bind ?nrOdcPoPrzesunieciu (+ ?nrOdc ?ilePrzesunac))  
        
        ;sprawdzamy czy na drodze jest gdzies blokada, jesli tak to zatrzymujemy agenta przed nia
        (bind ?czyWykryto FALSE)
        (loop-for-count (?i ?nrOdc (+ ?ilePrzesunac ?nrOdc)) do
            (bind ?drogaPrzemieszczania (nth$ 1 (find-fact ((?d droga))(and (eq ?d:id ?drogaId) (eq ?d:nrOdcinka ?i)) )))
            (bind ?kratkaPrzemieszczania (fact-slot-value ?drogaPrzemieszczania idKratki))        
            (bind ?czyBlokada (any-factp ((?b blokada))(eq ?b:idKratki ?kratkaPrzemieszczania)))  
              
            (if (eq ?czyBlokada TRUE)
            then
                (bind ?czyWykryto TRUE)
                (bind ?nrOdcPoPrzesunieciu (- ?i 1))
                (bind ?ilePrzesunac (- ?nrOdcPoPrzesunieciu ?nrOdc))
                (bind ?potrzebnaEnergia (* ?strE ?ilePrzesunac))
                (printout t "wykryto blokade" crlf)
                (break)         
            ) 
        )         

        ;znajdujemy kratke danej drogi po przemieszczeniu agenta
        (bind ?drogaPoPrzes (nth$ 1 (find-fact ((?d droga))(and (eq ?d:id ?drogaId) (eq ?d:nrOdcinka ?nrOdcPoPrzesunieciu)) )))
        (bind ?nowaKratkaId (fact-slot-value ?drogaPoPrzes idKratki))
    
        ;przesuwamy agenta odejmujac mu przy tym punkty ruchu
        (modify ?agent (idKratki ?nowaKratkaId)(mozliwyRuch (- ?mozliwyRuch ?ilePrzesunac))(energia (- ?energia ?potrzebnaEnergia)))  
        (printout t "Przesunieto agenta: " ?id " wzdluz drogi: " ?drogaId ", stara kratka: " ?idKratki ", nowa: " ?nowaKratkaId ", ile kratek: " ?ilePrzesunac ", strata energii: " ?potrzebnaEnergia crlf) 
        
        ;po przesunieciu agenta znow musimy wyznaczyc dodatek predkosci zwiazany z polozeniem na nowym terenie
        (retract ?modyfikacjaPredkosci)   
    else    
         (printout t "Agent: " ?id " chcial sie przemiescic ale zabraklo mu energii - musi odpoczac" crlf)
         (assert (akcjaOdpoczywanie (idAgenta ?id)(iteracjaPoczatek ?iteracja)(iteracjaKoniec (+ ?iteracja 1))))   
    ) 
    
    (retract ?akcja)
    
)

;przemieszczanie agentow po kratkach
(defrule przemieszczanie
	(or	
		(and
            ?agent <- (poslaniec (id ?id)(mozliwyRuch ?ruch)(idKratki ?idKratki)(energia ?energia)(strataEnergii ?strE))
            (modyfikacjaStratEnergiiPoslanca (idPoslanca ?id))
        )		
        ?agent <- (rycerz (id ?id)(mozliwyRuch ?ruch)(idKratki ?idKratki)(energia ?energia)(strataEnergii ?strE))
		?agent <- (drwal (id ?id)(mozliwyRuch ?ruch)(idKratki ?idKratki)(energia ?energia)(strataEnergii ?strE))
		?agent <- (kupiec (id ?id)(mozliwyRuch ?ruch)(idKratki ?idKratki)(energia ?energia)(strataEnergii ?strE))
		?agent <- (zlodziej (id ?id)(mozliwyRuch ?ruch)(idKratki ?idKratki)(energia ?energia)(strataEnergii ?strE))
		?agent <- (smok (id ?id)(mozliwyRuch ?ruch)(idKratki ?idKratki)(energia ?energia)(strataEnergii ?strE))
	)
	?kratka <- (kratka (id ?idKratki)(pozycjaX ?kratkaX)(pozycjaY ?kratkaY))
	?akcja <- (akcjaPrzemieszczanie (idAgenta ?id)(ileKratek ?kratki)(kierunek ?kierunek))
	(test (>= ?ruch ?kratki))
	(mapa ?height ?width)
    (iteracja ?iteracja)
    (not (akcjaOdpoczywanie (idAgenta ?id)))
    ?modyfikacjaPredkosci <- (modyfikacjaPredkosciAgenta (idAgenta ?id))
=>
	(bind ?ilePrzesunac 0)
     
    ;okreslamy wspolrzedne nowej kratki, na ktorej bedzie stal agent po przemieszczeniu
	;uwzgledniajac przy tym granice mapki - aby agent nie wyszedl poza mapke
	(switch ?kierunek
		(case dol then
			(bind ?nowaKratkaY (+ ?kratkaY ?kratki))
			(bind ?nowaKratkaX ?kratkaX)
			
			(if (>= ?nowaKratkaY ?height)
			then
				(bind ?nowaKratkaY ?height)
			)
           ;sprawdzamy czy na drodze jest gdzies blokada, jesli tak to zatrzymujemy agenta przed nia
           (bind ?czyWykryto FALSE)
           (loop-for-count (?y ?kratkaY ?nowaKratkaY) do
                (bind ?kratkaTmp (fact-slot-value (nth$ 1 (find-fact ((?k kratka))(and (eq ?k:pozycjaX ?kratkaX)(eq ?k:pozycjaY ?y)))) id))                
                (bind ?czyBlokada (any-factp ((?b blokada))(eq ?b:idKratki ?kratkaTmp)))  
                
                (if (eq ?czyBlokada TRUE)
                then
                    (bind ?czyWykryto TRUE)
                    (bind ?nowaKratkaY (- ?y 1))
                    (printout t "wykryto blokade" crlf)
                    (break)         
                ) 
                (bind ?ilePrzesunac (+ ?ilePrzesunac 1))
           )     
		)
		(case gora then 
			(bind ?nowaKratkaY (- ?kratkaY ?kratki))
			(bind ?nowaKratkaX ?kratkaX)
			
			(if (<= ?nowaKratkaY 0)
			then
				(bind ?nowaKratkaY 0)
			)
            ;sprawdzamy czy na drodze jest gdzies blokada, jesli tak to zatrzymujemy agenta przed nia           
           (bind ?czyWykryto FALSE)
           (loop-for-count (?y ?nowaKratkaY ?kratkaY) do
                (bind ?kratkaTmp (fact-slot-value (nth$ 1 (find-fact ((?k kratka))(and (eq ?k:pozycjaX ?kratkaX)(eq ?k:pozycjaY ?y)))) id))                
                (bind ?czyBlokada (any-factp ((?b blokada))(eq ?b:idKratki ?kratkaTmp)))  
              
                (if (eq ?czyBlokada TRUE)
                then
                    (bind ?czyWykryto TRUE)
                    (bind ?nowaKratkaY (- ?y 1))
                    (printout t "wykryto blokade" crlf)
                    (break)         
                ) 
           ) 
           (bind ?ilePrzesunac (+ ?ilePrzesunac 1))
		)
		(case lewo then 
		    (bind ?nowaKratkaX (- ?kratkaX ?kratki))
			(bind ?nowaKratkaY ?kratkaY)
			
			(if (<= ?nowaKratkaX 0)
			then
				(bind ?nowaKratkaX 0)
			)
            ;sprawdzamy czy na drodze jest gdzies blokada, jesli tak to zatrzymujemy agenta przed nia           
           (bind ?czyWykryto FALSE)
           (loop-for-count (?x ?nowaKratkaX ?kratkaX) do
                (bind ?kratkaTmp (fact-slot-value (nth$ 1 (find-fact ((?k kratka))(and (eq ?k:pozycjaX ?x)(eq ?k:pozycjaY ?kratkaY)))) id))                
                (bind ?czyBlokada (any-factp ((?b blokada))(eq ?b:idKratki ?kratkaTmp)))  
              
                (if (eq ?czyBlokada TRUE)
                then
                    (bind ?czyWykryto TRUE)
                    (bind ?nowaKratkaX (- ?x 1))
                    (printout t "wykryto blokade" crlf)
                    (break)         
                ) 
           )    
           (bind ?ilePrzesunac (+ ?ilePrzesunac 1))
		)
		(case prawo then 
			(bind ?nowaKratkaX (+ ?kratkaX ?kratki))
			(bind ?nowaKratkaY ?kratkaY)
			
			(if (>= ?nowaKratkaX ?width)
			then
				(bind ?nowaKratkaX ?width)
			)
           ;sprawdzamy czy na drodze jest gdzies blokada, jesli tak to zatrzymujemy agenta przed nia
           (bind ?czyWykryto FALSE)
           (loop-for-count (?x ?kratkaX ?nowaKratkaX) do
                (bind ?kratkaTmp (fact-slot-value (nth$ 1 (find-fact ((?k kratka))(and (eq ?k:pozycjaX ?x)(eq ?k:pozycjaY ?kratkaY)))) id))                
                (bind ?czyBlokada (any-factp ((?b blokada))(eq ?b:idKratki ?kratkaTmp)))  
              
                (if (eq ?czyBlokada TRUE)
                then
                    (bind ?czyWykryto TRUE)
                    (bind ?nowaKratkaX (- ?x 1))
                    (printout t "wykryto blokade" crlf)
                    (break)         
                ) 
           ) 
           (bind ?ilePrzesunac (+ ?ilePrzesunac 1))
		)
	)
	
    ;sprawdzamy czy agent odpowiednia ilosc energii aby sie przmiescic
    ;jesli nie ma, automatycznie musi odpoczywac dana iteracje
    (bind ?potrzebnaEnergia (* ?strE ?ilePrzesunac))    
    (if (>= (- ?energia ?potrzebnaEnergia) 6 )
    then   
        ;pobieramy id nowej kratki, na ktorej bedzie stal agent po przemieszczeniu
    	(bind ?nowaKratkaId (fact-slot-value (nth$ 1 (find-fact ((?k kratka)) (and (eq ?k:pozycjaX ?nowaKratkaX) (eq ?k:pozycjaY ?nowaKratkaY)))) id))
    	
    	;zamieniamy id kratki, na ktorej stoi agent oraz odejmujemy mu punkty ruchu
    	(modify ?agent (idKratki ?nowaKratkaId)(mozliwyRuch (- ?ruch ?kratki))(energia (- ?energia ?potrzebnaEnergia)))
    
    	(printout t "Przesunieto agenta: " ?id " w " ?kierunek ." Nowa kratka : " ?nowaKratkaId ", strata energii: " ?potrzebnaEnergia crlf)
    	
        ;po przesunieciu agenta znow musimy wyznaczyc dodatek predkosci zwiazany z polozeniem na nowym terenie    
        (retract ?modyfikacjaPredkosci)  
        
    else
        (printout t "Agent: " ?id " chcial sie przemiescic ale zabraklo mu energii - musi odpoczac" crlf)
        (assert (akcjaOdpoczywanie (idAgenta ?id)(iteracjaPoczatek ?iteracja)(iteracjaKoniec (+ ?iteracja 1))))
    )
    
    ;usuwamy akcje przesuwania
    (retract ?akcja)
)

;regula realizujaca akcje podjete przez agentow w momencie natrafienia na blokade
;moga oni albo ja przeskoczyc albo odpoczac i poczekac az zostanie usunieta
(defrule omijaniePrzeszkod
    (or	
		(and
            ?agent <- (poslaniec (id ?id)(energia ?energia)(strataEnergii ?strE)(cel ?cel)(mozliwyRuch ?mozliwyRuch)(idKratki ?agentKratka))
            (modyfikacjaStratEnergiiPoslanca (idPoslanca ?id))
        )		
        ?agent <- (rycerz (id ?id)(energia ?energia)(strataEnergii ?strE)(cel ?cel)(mozliwyRuch ?mozliwyRuch)(idKratki ?agentKratka))
		?agent <- (drwal (id ?id)(energia ?energia)(strataEnergii ?strE)(cel ?cel)(mozliwyRuch ?mozliwyRuch)(idKratki ?agentKratka))
		?agent <- (kupiec (id ?id)(energia ?energia)(strataEnergii ?strE)(cel ?cel)(mozliwyRuch ?mozliwyRuch)(idKratki ?agentKratka))
		?agent <- (zlodziej (id ?id)(energia ?energia)(strataEnergii ?strE)(cel ?cel)(mozliwyRuch ?mozliwyRuch)(idKratki ?agentKratka))
		?agent <- (smok (id ?id)(energia ?energia)(strataEnergii ?strE)(cel ?cel)(mozliwyRuch ?mozliwyRuch)(idKratki ?agentKratka))
	)
    (kratka (id ?agentKratka)(pozycjaX ?agentKratkaX)(pozycjaY ?agentKratkaY))
    
    (blokada (id ?blokadaId)(idKratki ?blokadaKratka))
    (kratka (id ?blokadaKratka)(pozycjaX ?blokadaKratkaX)(pozycjaY ?blokadaKratkaY))
    
    ?akcjaBlokada <- (akcjaZobaczenieBlokady (idAgenta ?id)(idBlokady ?blokadaId)(podjetaAkcja ?akcja))
    (not (akcjaOdpoczywanie (idAgenta ?id)))
    (iteracja ?iteracja)
    (modyfikacjaPredkosciAgenta (idAgenta ?id))
=>  
    ;jezeli agent porusza sie po drodze
    (if (eq (sub-string 1 4 ?cel) "grod")
    then
        (bind ?drogaZBlokada (nth$ 1 (find-fact ((?d droga)) (and (eq ?d:idKratki ?blokadaKratka) (eq ?d:dokadGrod ?cel)))))
        (bind ?drogaZBlokadaId (fact-slot-value ?drogaZBlokada id))       
        (bind ?drogaZBlokadaOdc (fact-slot-value ?drogaZBlokada nrOdcinka))
                
        (bind ?odcinekZaBlokada (nth$ 1 (find-fact ((?d droga))(and (eq ?d:id ?drogaZBlokadaId)(eq ?d:nrOdcinka (+ ?drogaZBlokadaOdc 1))))))
        (bind ?kratkaZaBlokadaId (fact-slot-value (nth$ 1 (find-fact ((?k kratka)) (eq ?k:id (fact-slot-value ?odcinekZaBlokada idKratki)) )) id))
        (if (eq ?akcja ominiecie)      
        then
            (if (>= ?mozliwyRuch 4)
            then
                (bind ?potrzebnaEnergia (+ (* ?strE 4) 2))
                (if (> (- ?energia ?potrzebnaEnergia) 6 )
                then
                    (modify ?agent (idKratki ?kratkaZaBlokadaId)(mozliwyRuch (- ?mozliwyRuch 4))(energia (- ?energia ?potrzebnaEnergia)))
                    (printout t "Agent: " ?id  " przeskoczyl przeszkode, strata energii: " ?potrzebnaEnergia crlf) 
                    (retract ?akcjaBlokada)              
                else
                    (printout t "Agent: " ?id " chcial przeskoczyc przeszkode ale zabraklo mu energii, potrzeba: " ?potrzebnaEnergia " - musi odpoczac" crlf)
                    (assert (akcjaOdpoczywanie (idAgenta ?id)(iteracjaPoczatek ?iteracja)(iteracjaKoniec (+ ?iteracja 1))))             
                )
            else
                (printout t "Agent: " ?id " chcial przeskoczyc przeszkode ale nie mial punktow ruchu" crlf)
            )
        else
             (printout t "Agent: " ?id " napotkal na przeszkode i postanowil odpoczywac" crlf)
             (assert (akcjaOdpoczywanie (idAgenta ?id)(iteracjaPoczatek ?iteracja)(iteracjaKoniec (+ ?iteracja 1))))     
        )
    else ;jezeli agent chodzi w po lasach itd.
        (bind ?roznicaX (- ?agentKratkaX ?blokadaKratkaX))
        (bind ?roznicaY (- ?agentKratkaY ?blokadaKratkaY))
        
        (if (< ?roznicaX 0)
        then
            ;(printout t "agent skacze w prawo" crlf)
            (bind ?kratkaPoSkokuId (fact-slot-value (nth$ 1 (find-fact ((?k kratka))(and (eq ?k:pozycjaX (+ ?blokadaKratkaX 1))(eq ?k:pozycjaY ?blokadaKratkaY) )))id))   
        )
        
        (if (> ?roznicaX 0)
        then
            ;(printout t "agent skacze w lewo" crlf) 
            (bind ?kratkaPoSkokuId (fact-slot-value (nth$ 1 (find-fact ((?k kratka))(and (eq ?k:pozycjaX (- ?blokadaKratkaX 1))(eq ?k:pozycjaY ?blokadaKratkaY) )))id))    
        )
        
        (if (< ?roznicaY 0)
        then
            ;(printout t "agent skacze w dol" crlf)  
            (bind ?kratkaPoSkokuId (fact-slot-value (nth$ 1 (find-fact ((?k kratka))(and (eq ?k:pozycjaX ?blokadaKratkaX)(eq ?k:pozycjaY (+ ?blokadaKratkaY 1)) )))id))   
        )
        
        (if (> ?roznicaY 0)
        then
            ;(printout t "agent skacze w gore" crlf)   
            (bind ?kratkaPoSkokuId (fact-slot-value (nth$ 1 (find-fact ((?k kratka))(and (eq ?k:pozycjaX ?blokadaKratkaX)(eq ?k:pozycjaY (- ?blokadaKratkaY 1)) )))id))  
        )
        
        (if (eq ?akcja ominiecie)      
        then
            (if (>= ?mozliwyRuch 4)
            then
                (bind ?potrzebnaEnergia (+ (* ?strE 4) 2))
                (if (> (- ?energia ?potrzebnaEnergia) 6 )
                then
                    (modify ?agent (idKratki ?kratkaPoSkokuId)(mozliwyRuch (- ?mozliwyRuch 4))(energia (- ?energia ?potrzebnaEnergia)))
                    (printout t "Agent: " ?id  " przeskoczyl przeszkode, strata energii: " ?potrzebnaEnergia crlf) 
                    (retract ?akcjaBlokada)              
                else
                    (printout t "Agent: " ?id " chcial przeskoczyc przeszkode ale zabraklo mu energii, potrzeba: " ?potrzebnaEnergia " - musi odpoczac" crlf)
                    (assert (akcjaOdpoczywanie (idAgenta ?id)(iteracjaPoczatek ?iteracja)(iteracjaKoniec (+ ?iteracja 1))))             
                )
            else
                (printout t "Agent: " ?id " chcial przeskoczyc przeszkode ale nie mial punktow ruchu" crlf)
            )
        else
             (printout t "Agent: " ?id " napotkal na przeszkode i postanowil odpoczywac" crlf)
             (assert (akcjaOdpoczywanie (idAgenta ?id)(iteracjaPoczatek ?iteracja)(iteracjaKoniec (+ ?iteracja 1))))     
        )
    )         
)

;regula realizujaca odpoczynek agentów, regenerujac im przy tym energie
(defrule odpoczywanie
    (or	
		?agent <- (poslaniec (id ?id)(energia ?energia)(odnawianieEnergii ?odnawianieE))
		?agent <- (rycerz (id ?id)(energia ?energia)(odnawianieEnergii ?odnawianieE))
		?agent <- (drwal (id ?id)(energia ?energia)(odnawianieEnergii ?odnawianieE))
		?agent <- (kupiec (id ?id)(energia ?energia)(odnawianieEnergii ?odnawianieE))
		?agent <- (zlodziej (id ?id)(energia ?energia)(odnawianieEnergii ?odnawianieE))
		?agent <- (smok (id ?id)(energia ?energia)(odnawianieEnergii ?odnawianieE))
	)
    (iteracja ?aktualnaIteracja)
    ?odpoczynek <- (akcjaOdpoczywanie (idAgenta ?id)(iteracjaKoniec ?iteracjaKoniec))
    (not (zregenerowanoAgenta (idAgenta ?id)))
=>
    ;jesli agent nadal odpoczywa to przywracamy mu pewna liczbe energii,
    ;ktora okresla pole odnawianieEnergii   
    (if (> ?aktualnaIteracja ?iteracjaKoniec)
    then 
        (printout t "Agent: " ?id " skonczyl odpoczywac" crlf)
        (retract ?odpoczynek)    
    else  ;jezeli przestal odpoczywac to usuwamy akcjeOdpoczynku i 
        (modify ?agent (energia (+ ?energia ?odnawianieE)))

        ;tworzymy fakt kontrolny, ze juz w tej iteracji zregenerowano agenta
        (assert (zregenerowanoAgenta (idAgenta ?id)))
            
        (printout t "Agent: " ?id " odpoczywa" crlf)        
    )  
)

; TODO: Sprawdzenie, czy poslaniec moze wziac wiecej paczek.
(defrule wezPaczke
    ?agent <- (poslaniec (id ?id)(udzwig ?udzwig)(paczki ?paczki))
    ?paczka <- (paczka (id ?idPaczki)(waga ?waga))
    ?akcja <- (akcjaWezPaczke (idAgenta ?id)(idPaczki ?idPaczki))
=>
    (modify ?agent (paczki ?paczki ?idPaczki))

    (retract ?akcja)

    (printout t "Poslaniec o id: " ?id " wzial paczke o id: " ?idPaczki crlf)
)

; TODO: Sprawdzenie, czy ma miejsce w magazynie.
(defrule kupTowarZGrodu
    ?agent <- (kupiec (id ?id)(pojemnoscMagazynu ?pojemnosc)(przedmioty ?przedmioty))
    ?grod <- (grod (nazwa ?idGrodu))
    ?przedmiot <- (przedmiot (id ?idPrzedmiotu))
    ?akcja <- (akcjaKupowanie (idAgenta ?id)(idPrzedmiotu ?idPrzedmiotu)(idSprzedawcy ?idGrodu))
=>
    (modify ?agent (przedmioty ?przedmioty ?idPrzedmiotu))
    (printout t "Kupiec o id: " ?id " kupil przedmiot o id: " ?idPrzedmiotu crlf)
)

;regula, ktora okresla jakie kratki widzi dany agent
(defrule okreslanieWidocznosci
	(or 
		(poslaniec (id ?agentId)(idKratki ?idKratki)(poleWidzenia ?poleWidzenia))
		(kupiec (id ?agentId)(idKratki ?idKratki)(poleWidzenia ?poleWidzenia))
		(zlodziej (id ?agentId)(idKratki ?idKratki)(poleWidzenia ?poleWidzenia))
		(rycerz (id ?agentId)(idKratki ?idKratki)(poleWidzenia ?poleWidzenia))
		(drwal (id ?agentId)(idKratki ?idKratki)(poleWidzenia ?poleWidzenia))
		(smok (id ?agentId)(idKratki ?idKratki)(poleWidzenia ?poleWidzenia))
	)
	(kratka (id ?idKratki)(pozycjaX ?kX)(pozycjaY ?kY))
=>	
	;kazdy agent widzi kwadratowy obszar o dlugosci boku rownej wartosci poleWidzenia
	(loop-for-count (?i (- 0 ?poleWidzenia) ?poleWidzenia) do
		(loop-for-count (?j (- 0 ?poleWidzenia) ?poleWidzenia) do
			(bind ?x (+ ?kX ?i))
			(bind ?y (+ ?kY ?j))
			
			;sprawdzenie czy kratka, ktora widzi agent nie wykracza poza obszar mapy
			(bind ?czyJestKratka (any-factp ((?k kratka)) (and (eq ?k:pozycjaX ?x) (eq ?k:pozycjaY ?y))))
			
			(if (eq ?czyJestKratka TRUE)
			then
				(bind ?widzialnaKratkaId (fact-slot-value (nth$ 1 (find-fact ((?k kratka)) (and (eq ?k:pozycjaX ?x) (eq ?k:pozycjaY ?y)))) id))
				(assert (widzialnaCzescSwiata (idAgenta ?agentId)(idKratki ?widzialnaKratkaId)))
				;(printout t "Agent o id: " ?agentId " widzi kratke o id: " ?widzialnaKratkaId crlf)
			)
		)
	)
)