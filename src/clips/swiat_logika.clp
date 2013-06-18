;template'y kontrolne
(deftemplate modyfikacjaStratEnergiiPoslanca (slot idPoslanca))
(deftemplate modyfikacjaPredkosciAgenta (slot idAgenta))
(deftemplate zregenerowanoAgenta (slot idAgenta))
(deftemplate okreslonoWidocznosc (slot idAgenta))
(deftemplate dzialanieKleskiNaAgenta(slot idAgenta) (slot idKleski))
(deftemplate kleskaLas(slot idKleski))
(deftemplate kleskaGrod(slot idKleski))
(deftemplate uaktualnianieKlesk(slot idKleski))

;REGULY
;regula okresla parametry poslanca po zakupieniu konia, czyli
;po sprawdzeniu istnienia faktu kupienieKonia
(defrule sprawdzKupienieKoniaPoslaniec (declare (salience 4))
    ?poslaniec <- (poslaniec (id ?poslaniecId) (kon ?kon)(zloto ?zloto))
    ?kupienieKonia <- (kupienieKonia (idAgenta ?poslaniecId)(idKonia ?kupionyKon))
    ?konik <- (kon(id ?kupionyKon)(cena ?cena))
    (not (akcjaOdpoczywanie (idAgenta ?id)))
=>
    (open "src/clips/results.txt" resultFile "a")
    ;pobieramy index kupionego konia z bazy wiedzy        
    (bind ?kupionyKon (nth$ 1 (find-fact ((?konTmp kon)) (eq ?konTmp:id ?kupionyKon))))        
     
    ;wyciagamy poszczegolne wartosci pol z obiektu kon o okreslonym wyzej indeksie        
    (bind ?konId (fact-slot-value ?kupionyKon id))        
    (bind ?konUdzwig (fact-slot-value ?kupionyKon udzwig))
    (bind ?konPredkosc (fact-slot-value ?kupionyKon predkosc))        
    (if (> ?zloto ?cena)
    then
        ;modyfikujemy parametry poslanca uzwgledniajac zakupionego konia
        (modify ?poslaniec (udzwig ?konUdzwig) (predkosc ?konPredkosc) (poleWidzenia ?konPredkosc)(kon ?konId)(zloto (- ?zloto ?cena)))   
        
        (printout resultFile "Poslaniec: " ?poslaniecId " kupil konia o predkosci: " ?konPredkosc crlf)
     
        ;kupiony kon nie posiada juz idGrodu
        (modify ?konik (grod nil))
            
        ;jak poslaniec kupi konia to nalezy jeszcze raz wywolac regule obliczajaca straty energii
        ;co robimy poprzez usuniecie akcji blokujacej: modyfikacjaStratEnergiiPoslanca
        (bind ?czyModyfikacjaPoslanca (any-factp ((?m modyfikacjaStratEnergiiPoslanca)) (eq ?m:idPoslanca ?poslaniecId)))
        (if (eq ?czyModyfikacjaPoslanca TRUE)
        then
            (bind ?modyfikacjaStratEnergiiPoslancaId (nth$ 1 (find-fact ((?m modyfikacjaStratEnergiiPoslanca)) (eq ?m:idPoslanca ?poslaniecId))))     
            (retract ?modyfikacjaStratEnergiiPoslancaId )  
        )
    else
        (printout resultFile "Poslaniec: " ?poslaniecId " chcial kupic konia ale nie mial odpowiedniej ilosci zlota" crlf)     
    )
    
    ;usuwamy akcje kupienia konia        
    (retract ?kupienieKonia)
    
    (close resultFile)
)

;regula aktualizujaca straty energii poslanca z uwzglednieniem paczek jakie niesie
(defrule obliczStartyEnergiiPoslanca (declare (salience 2))
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
        
        (bind ?strataEnergii (round (- ?strataEnergii (* ?strataEnergii (/ ?konZmeczenieJezdzcy 100)))))
    )    
    
    (open "src/clips/results.txt" resultFile "a")
    (printout resultFile "Poslaniec: " ?poslaniecId " - nowa starta energii: " ?strataEnergii crlf)
    (close resultFile)
        
    (modify ?poslaniec (strataEnergii ?strataEnergii))
    
    ;fakt kontrolny, za pomoca ktorego oznaczamy, 
    ;ze dany poslaniec w danej iteracji zostal juz zmodfikowany    
    (assert (modyfikacjaStratEnergiiPoslanca (idPoslanca ?poslaniecId)))
    
)

;regula okreslajaca dodatki predkosci zwiazane z tym po jakiej nawierzchni się on porusza
(defrule okreslDodatekPredkosciRuchuAgenta (declare (salience 3)) 
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
            (bind ?dodatPredkosc (- ?dodatPredkosc 2))            
        )
        (if (eq ?czyScieteDrzewo TRUE)
        then
            (bind ?dodatPredkosc (- ?dodatPredkosc 1))     
        ) 
        (if (eq ?czyDrogaBezplatna TRUE)
        then
            (bind ?dodatPredkosc (+ ?dodatPredkosc 0))      
        ) 
        (if (eq ?czyDrogaPlatna TRUE)
        then
            (bind ?dodatPredkosc (+ ?dodatPredkosc 1))      
        ) 
        (if (eq ?czyDrogaUtwardzona TRUE)
        then
            (bind ?dodatPredkosc (+ ?dodatPredkosc 1))     
        )
        (if (eq ?czyDrogaNieutwardzona TRUE)
        then
            (bind ?dodatPredkosc (+ ?dodatPredkosc 1))        
        )       
        
        (if (< ?dodatPredkosc 0)
        then
            (bind ?dodatPredkosc 0)
        )  
    else ;chyba, ze jest to smok, ktory niezaleznie od nawierzchni porusza sie z ta sama predkoscia
        (bind ?dodatPredkosc 5)    
    )    
    
    ;modyfikujemy dodatek predkosci agenta oraz jego pole widzenia
    (modify ?agent (dodatekPredkosc ?dodatPredkosc)(poleWidzenia (+ ?dodatPredkosc ?predkosc))(mozliwyRuch (+ ?dodatPredkosc ?predkosc)))    
    (open "src/clips/results.txt" resultFile "a")    
    (printout resultFile "Zmodyfikowano dodatek predkosci agenta: " ?id ", dodatek predkosci = " ?dodatPredkosc crlf)
    (close)    
    ;umieszczamy fakt kontrolny, ze dla danego agenta dodatkowa predkosc zostala juz zmodyfikowana
    (assert (modyfikacjaPredkosciAgenta (idAgenta ?id)))
)
;regula pozwalajaca przmieszczac agentow wzdluz danej drogi
(defrule przemieszczaniePoDrodze (declare (salience 1))
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
    (open "src/clips/results.txt" resultFile "a")    
    sprawdzamy o ile moze sie przesunac dany agent
    (if (<= ?ileKratek ?mozliwyRuch) 
    then
       (bind ?ilePrzesunac ?ileKratek)
    else
        (bind ?ilePrzesunac ?mozliwyRuch)
    )
               
    ;sprawdzamy czy nie przejdziemy docelowego grodu
    (bind ?dotarcieDoGrodu FALSE)
    (if (> (+ ?nrOdc ?ilePrzesunac) ?maxOdcinek)
    then     
        (bind ?ilePrzesunac (- ?maxOdcinek ?nrOdc))
        (bind ?dotarcieDoGrodu TRUE)
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
                ;(printout resultFile "wykryto blokade" crlf)
                (break)         
            ) 
        )         

        ;znajdujemy kratke danej drogi po przemieszczeniu agenta
        (if (and (eq ?dotarcieDoGrodu TRUE) (eq ?czyWykryto FALSE))
        then
            (bind ?grodIndex (nth$ 1 (find-fact ((?g grod))(eq ?g:nazwa ?cel))))
            (bind ?nowaKratkaId (fact-slot-value ?grodIndex idKratki))
        else
            (bind ?drogaPoPrzes (nth$ 1 (find-fact ((?d droga))(and (eq ?d:id ?drogaId) (eq ?d:nrOdcinka ?nrOdcPoPrzesunieciu)) )))
            (bind ?nowaKratkaId (fact-slot-value ?drogaPoPrzes idKratki))
        )
        
        (bind ?tmp (nth$ 1 (find-fact ((?k kratka))(eq ?k:id ?nowaKratkaId))))
    
                
        ;przesuwamy agenta odejmujac mu przy tym punkty ruchu
        (modify ?agent (idKratki ?nowaKratkaId)(mozliwyRuch (- ?mozliwyRuch ?ilePrzesunac))(energia (- ?energia ?potrzebnaEnergia)))  
        (printout resultFile "Przesunieto agenta: " ?id " wzdluz drogi: " ?drogaId ", stara kratka: " ?idKratki ", nowa: " ?nowaKratkaId " x: " (fact-slot-value ?tmp pozycjaX) " y: " (fact-slot-value ?tmp pozycjaY) ", ile kratek: " ?ilePrzesunac ", strata energii: " ?potrzebnaEnergia crlf) 
        
        ;po przesunieciu agenta znow musimy wyznaczyc dodatek predkosci zwiazany z polozeniem na nowym terenie
        (retract ?modyfikacjaPredkosci)   
    else    
         (printout resultFile "Agent: " ?id " chcial sie przemiescic ale zabraklo mu energii - musi odpoczac" crlf)
         (assert (akcjaOdpoczywanie (idAgenta ?id)(iteracjaPoczatek ?iteracja)(iteracjaKoniec (+ ?iteracja 1))))   
    ) 
    
    (retract ?akcja)
    (close)
    
)
;regula pozwalajaca przeniesc agenta z grodu na droge
(defrule przeniesZGroduNaDroge (declare (salience 1))
    (or	
		(and 
            ?agent <- (poslaniec (id ?id))
		    (modyfikacjaStratEnergiiPoslanca (idPoslanca ?id))
        )
        ?agent <- (rycerz (id ?id))
		?agent <- (drwal (id ?id))
		?agent <- (kupiec (id ?id))
		?agent <- (zlodziej (id ?id))
		?agent <- (smok (id ?id))
    )
    ?akcja <- (akcjaZGroduNaDroge (idAgenta ?id)(idKratki ?kratkaId))
    (kratka (id ?kratkaId)(pozycjaX ?x)(pozycjaY ?y))
    (droga (idKratki ?kratkaId)(id ?drogaId))
    (not (akcjaOdpoczywanie (idAgenta ?id)))
    ?modyfikacjaPredkosci <- (modyfikacjaPredkosciAgenta (idAgenta ?id))
=>
    (open "src/clips/results.txt" resultFile "a")   
     
    (modify ?agent (idKratki ?kratkaId)) 

    (printout resultFile "Agent: " ?id " zostal przeniesiony z grodu na poczatek drogi: " ?drogaId crlf)
    
    ;modyfikujemy dodatek predkosci zwiazany z polozeniem na nowym polu
    (retract ?modyfikacjaPredkosci)
    (retract ?akcja)
    (close) 
)


;przemieszczanie agentow po kratkach
(defrule przemieszczanie (declare (salience 1))
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
    (open "src/clips/results.txt" resultFile "a")
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
                    ;(printout resultFile "wykryto blokade" crlf)
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
                    ;(printout resultFile "wykryto blokade" crlf)
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
                    ;(printout resultFile "wykryto blokade" crlf)
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
                    ;(printout resultFile "wykryto blokade" crlf)
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
    
    	(printout resultFile "Przesunieto agenta: " ?id " w " ?kierunek ." Nowa kratka : " ?nowaKratkaId ", strata energii: " ?potrzebnaEnergia crlf)
    	
        ;po przesunieciu agenta znow musimy wyznaczyc dodatek predkosci zwiazany z polozeniem na nowym terenie    
        (retract ?modyfikacjaPredkosci)  
        
    else
        (printout resultFile "Agent: " ?id " chcial sie przemiescic ale zabraklo mu energii - musi odpoczac" crlf)
        (assert (akcjaOdpoczywanie (idAgenta ?id)(iteracjaPoczatek ?iteracja)(iteracjaKoniec (+ ?iteracja 1))))
    )
    
    ;usuwamy akcje przesuwania
    (retract ?akcja)
    (close)
)

;regula realizujaca akcje podjete przez agentow w momencie natrafienia na blokade
;moga oni albo ja przeskoczyc albo odpoczac i poczekac az zostanie usunieta
(defrule omijaniePrzeszkod (declare (salience 1))
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
    (open "src/clips/results.txt" resultFile "a")
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
                    (printout resultFile "Agent: " ?id  " przeskoczyl przeszkode, strata energii: " ?potrzebnaEnergia crlf) 
                    (retract ?akcjaBlokada)              
                else
                    (printout resultFile "Agent: " ?id " chcial przeskoczyc przeszkode ale zabraklo mu energii, potrzeba: " ?potrzebnaEnergia " - musi odpoczac" crlf)
                    (assert (akcjaOdpoczywanie (idAgenta ?id)(iteracjaPoczatek ?iteracja)(iteracjaKoniec (+ ?iteracja 1))))             
                )
            else
                (printout resultFile "Agent: " ?id " chcial przeskoczyc przeszkode ale nie mial punktow ruchu" crlf)
            )
        else
             (printout resultFile "Agent: " ?id " napotkal na przeszkode i postanowil odpoczywac" crlf)
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
                    (printout resultFile "Agent: " ?id  " przeskoczyl przeszkode, strata energii: " ?potrzebnaEnergia crlf) 
                    (retract ?akcjaBlokada)              
                else
                    (printout resultFile "Agent: " ?id " chcial przeskoczyc przeszkode ale zabraklo mu energii, potrzeba: " ?potrzebnaEnergia " - musi odpoczac" crlf)
                    (assert (akcjaOdpoczywanie (idAgenta ?id)(iteracjaPoczatek ?iteracja)(iteracjaKoniec (+ ?iteracja 1))))             
                )
            else
                (printout resultFile "Agent: " ?id " chcial przeskoczyc przeszkode ale nie mial punktow ruchu" crlf)
            )
        else
             (printout resultFile "Agent: " ?id " napotkal na przeszkode i postanowil odpoczywac" crlf)
             (assert (akcjaOdpoczywanie (idAgenta ?id)(iteracjaPoczatek ?iteracja)(iteracjaKoniec (+ ?iteracja 1))))     
        )
    ) 
    
    (close)        
)

;regula realizujaca odpoczynek agentów, regenerujac im przy tym energie
(defrule odpoczywanie (declare (salience 8))
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
    (open "src/clips/results.txt" resultFile "a")
    ;jesli agent nadal odpoczywa to przywracamy mu pewna liczbe energii,
    ;ktora okresla pole odnawianieEnergii   
    (if (> ?aktualnaIteracja ?iteracjaKoniec)
    then 
        (printout resultFile "Agent: " ?id " skonczyl odpoczywac" crlf)
        (retract ?odpoczynek)    
    else  ;jezeli przestal odpoczywac to usuwamy akcjeOdpoczynku i 
        (modify ?agent (energia (+ ?energia ?odnawianieE)))

        ;tworzymy fakt kontrolny, ze juz w tej iteracji zregenerowano agenta
        (assert (zregenerowanoAgenta (idAgenta ?id)))
            
        (printout resultFile "Agent: " ?id " odpoczywa" crlf)        
    )  
    
   (close)     
)


(defrule wezPaczke (declare (salience 4))
    ?agent <- (poslaniec (id ?id)(udzwig ?udzwig)(paczki $?paczki))
    ?paczka <- (paczka (id ?idPaczki)(waga ?waga))
    ?akcja <- (akcjaWezPaczke (idAgenta ?id)(idPaczki ?idPaczki))
    ?modyfikacja <- (modyfikacjaStratEnergiiPoslanca (idPoslanca ?id))
=>
    (open "src/clips/results.txt" resultFile "a")
    
    (bind ?sumaWagPaczek 0)
    (loop-for-count (?i 1 (length $?paczki)) do 
        (printout resultFile "Weszlo" crlf)
        ;pobieramy id kolejnej, posiadanej przez agenta paczki        
        (bind ?paczkaId (nth$ ?i $?paczki))
        
        ;znajdujemy index faktu paczki w bazie wiedzy
        (bind ?paczkaTmp (nth$ 1 (find-fact ((?p paczka))(eq ?p:id ?paczkaId))))
                
        (bind ?paczkaWaga (fact-slot-value ?paczkaTmp waga))
        (bind ?sumaWagPaczek (+ ?sumaWagPaczek ?paczkaWaga))                
    )
        
    (bind ?sumaWagPaczek (+ ?sumaWagPaczek ?waga))    
    (if (>= ?udzwig ?sumaWagPaczek)
    then        
        (printout resultFile "Poslaniec : " ?id " wzial paczke o id: " ?idPaczki crlf)
        (modify ?agent (paczki $?paczki ?idPaczki))
        (modify ?paczka (grodStart nil))
        
        ;jeszcze raz musimy zmodyfikowac jego straty energii        
        (retract ?modyfikacja)
    else
        (printout resultFile "Poslaniec : " ?id " chcial wziac paczke ale nie mial juz miejsca" crlf)     
    )
   

    (retract ?akcja)
    
    (close)
)

;WIP: Ataki pomiedzy rycerzem a smokiem
(defrule atakujSmoka (declare (salience 3))
    ?smok <- (smok (id ?idSmoka) (idKratki ?idKratkiSmoka) (energia ?energiaSmoka) (zloto ?zlotoSmoka) (strataEnergii ?strataEnergiiSmoka))
    ?rycerz <- (rycerz (id ?idRycerza) (idKratki ?idKratki) (energia ?energia) (zloto ?zloto) (strataEnergii ?strataEnergii))
    ?akcja <- (akcjaAtak (idAgenta ?idAgenta) (idOfiary ?idOfiary) (rodzajAtaku ?rodzajAtaku))
=>
    (open "src/clips/results.txt" resultFile "a")
    ;jesli bestia jeszcze zyje
    (if ( = ?rodzajAtaku 1)
        then
        (modify ?smok
            (energia (- ?energiaSmoka 15))
            
        )
        (modify ?rycerz
            (energia ( - ?energia ?strataEnergii)) ;rycerz sie meczy atakiem
        )
    )
    (if ( = ?rodzajAtaku 2)
        then
        (modify ?smok
            (energia (- ?energiaSmoka 25))
            
        )
        (modify ?rycerz
            (energia ( - ?energia ( * 2 ?strataEnergii))) ;rycerz sie meczy atakiem
        )
    )
    (if ( = ?rodzajAtaku 3)
        then
        (modify ?smok
            (energia (- ?energiaSmoka 45))
            
        )
        (modify ?rycerz
            (energia ( - ?energia ( * 3 ?strataEnergii))) ;rycerz sie meczy atakiem
        )
    )
    
    ;jesli smok juz jest martwy
    (if ( <= ?energiaSmoka 0 )
        then 
        
        (modify ?rycerz 
            (zloto ( + ?zloto ?zlotoSmoka)) ;zdobadz lup, rycerzu
        )
        (retract ?smok)
        (printout resultFile "Smok: " ?idSmoka " stracil zycie." crlf)
    )
    
    (retract ?akcja)
    (close)
)

(defrule atakujRycerza (declare (salience 3))
    ?smok <- (smok (id ?idSmoka) (idKratki ?idKratkiSmoka) (energia ?energiaSmoka) (zloto ?zlotoSmoka) (strataEnergii ?strataEnergiiSmoka))
    ?rycerz <- (rycerz (id ?idRycerza) (idKratki ?idKratki) (energia ?energia) (zloto ?zloto) (strataEnergii ?strataEnergii))
    ?akcja <- (akcjaAtak (idAgenta ?idAgenta) (idOfiary ?idOfiary) (rodzajAtaku ?rodzajAtaku))
=>
    (open "src/clips/results.txt" resultFile "a")
    
    ;jesli biedak jeszcze zyje
    (if ( = ?rodzajAtaku 1)
        then
        (modify ?rycerz
            (energia (- ?energia 15))
            
        )
        (modify ?smok
            (energia ( - ?energiaSmoka ?strataEnergii)) ;smok sie meczy atakiem
        )
    )
    (if ( = ?rodzajAtaku 2)
        then
        (modify ?rycerz
            (energia (- ?energia 25))
            
        )
        (modify ?smok
            (energia ( - ?energiaSmoka ( * 2 ?strataEnergii))) ;smok sie meczy atakiem
        )
    )
    (if ( = ?rodzajAtaku 3)
        then
        (modify ?rycerz
            (energia (- ?energia 45))
            
        )
        (modify ?smok
            (energia ( - ?energiaSmoka ( * 3 ?strataEnergii))) ;smok sie meczy atakiem
        )
    )
    
    ;jesli rycerz juz jest martwy
    (if ( <= ?energia 0 )
        then 
        
        (modify ?smok 
            (zloto ( + ?zlotoSmoka ?zloto)) ;zdobadz lup, zly smoku
        )
        (retract ?rycerz)    
        (printout resultFile "Rycerz: " ?idRycerza " stracil zycie." crlf)   
    )
    (retract ?akcja)
    (close)
)

;regula do sciecia drzew
(defrule zetnijDrzewo (declare (salience 3))
    ?drwal <- (drwal (id ?id) (idKratki ?idKratki) (scieteDrewno $?drewnoDrwala) 
     (siekiera ?idSiekiery) (udzwig ?udzwig) ( maxUdzwig ?maxUdzwig) (energia ?energia) (strataEnergii ?strataEnergii))
    ?drzewo <- (drzewo (idKratki ?idKratki) (stan ?stanDrzewa) (rodzajDrzewa ?rodzajDrzewa)) 
    ?siekiera <- (siekiera (id ?idSiekiery) (typ ?typSiekiery) (zuzycie ?zuzycie))
    ?akcja <- (akcjaZetnijDrzewo (idAgenta ?id))
    (iteracja ?iteracja)
    (not (akcjaOdpoczywanie (idAgenta ?id)))
=>
    (open "src/clips/results.txt" resultFile "a")
    
    (if (eq ?stanDrzewa sciete)
    then 
        (printout resultFile "Drwal: " ?id " chcial sciac sciete drzewo" crlf)  
    else  
       
        (if ( eq ?rodzajDrzewa dab)
        then
            (bind ?cenaDrewna 100)
            (bind ?wagaDrewna 30)
        )
        (if ( eq ?rodzajDrzewa buk) 
        then
        
            (bind ?cenaDrewna 70)
            (bind ?wagaDrewna 20)
        )
        (if ( eq ?rodzajDrzewa sosna) 
        then
        
            (bind ?cenaDrewna 40)
            (bind ?wagaDrewna 10)
        )
       
         (if ( eq ?typSiekiery zelazna)
        then
            (if  ( >= ?maxUdzwig (+ ?udzwig ?wagaDrewna) )
            then
                (if ( >= (- ?energia (* 2 ?strataEnergii)) 0 )
                then
                (bind ?idDrewna_a (str-cat ?id "_" ?iteracja a))
                (assert (drewno (id ?idDrewna_a) (cena ?cenaDrewna) ( waga ?wagaDrewna) ) )
                
                (modify ?drzewo  (stan sciete) )
                 ;zuzycie siekiery
                (if ( >= ( + ?zuzycie 10) 100) 
                then
                    ;tracimy siekierę
                    (retract ?siekiera)
                    (modify ?drwal (scieteDrewno ?drewnoDrwala ?idDrewna_a)
                            (udzwig  (+ ?udzwig ?wagaDrewna))
                            (energia ( - ?energia (* 2 ?strataEnergii)))
                            (siekiera nil)
                    )
                 (printout resultFile "Drwal: " ?id " stracil swa siekiere " ?idSiekiery". " crlf)
                else
                    (modify ?siekiera (zuzycie (+ ?zuzycie 10)))
                
                    (modify ?drwal (scieteDrewno ?drewnoDrwala ?idDrewna_a)
                            (udzwig  (+ ?udzwig ?wagaDrewna))
                            (energia ( - ?energia (* 2 ?strataEnergii)))
                    )
                )
                (printout resultFile "Drwal: " ?id " scial drzewo na kratce " ?idKratki " " crlf)
                else
                (printout resultFile "Drwal: " ?id " nie moze sciac drzewa. Za malo enegrii." crlf)
           )
            else
            (printout resultFile "Drwal: " ?id " nie moze sciac drzewa. Za maly udzwig maksymalny." crlf)
            )
         )
         (if ( eq ?typSiekiery zlota)
        then
            (if  ( >= ?maxUdzwig (+ ?udzwig ?wagaDrewna ?wagaDrewna) )
            then
                (if ( >= (- ?energia (* 2 ?strataEnergii)) 0 )
                then
                (bind ?idDrewna_a (str-cat ?id "_" ?iteracja a))
                (assert (drewno (id ?idDrewna_a) (cena ?cenaDrewna) ( waga ?wagaDrewna) ) )
                
                 (bind ?idDrewna_b (str-cat ?id "_" ?iteracja b))
                (assert (drewno (id ?idDrewna_b) (cena ?cenaDrewna) ( waga ?wagaDrewna) ) )
                
        
                (modify ?drzewo  (stan sciete) )
                 ;zuzycie siekiery
                (if ( >= ( + ?zuzycie 10) 100) 
                then
                    ;tracimy siekierę
                    (retract ?siekiera)
                    (modify ?drwal (scieteDrewno ?drewnoDrwala ?idDrewna_a ?idDrewna_b)
                            (udzwig  (+ ?udzwig ?wagaDrewna ?wagaDrewna))
                            (energia ( - ?energia (* 2 ?strataEnergii)))
                            (siekiera nil)
                    )
                 (printout resultFile "Drwal: " ?id " stracil swa siekiere " ?idSiekiery". " crlf)
                else
                    (modify ?siekiera (zuzycie (+ ?zuzycie 10)))
                
                    (modify ?drwal (scieteDrewno ?drewnoDrwala ?idDrewna_a ?idDrewna_b)
                            (udzwig  (+ ?udzwig ?wagaDrewna ?wagaDrewna))
                            (energia ( - ?energia (* 2 ?strataEnergii)))
                    )
                )
                (printout resultFile "Drwal: " ?id " scial drzewo na kratce " ?idKratki " " crlf)
                else
                (printout resultFile "Drwal: " ?id " nie moze sciac drzewa. Za malo enegrii." crlf)
           )
            else
            (printout resultFile "Drwal: " ?id " nie moze sciac drzewa. Za maly udzwig maksymalny." crlf)
            )
         )
            (if ( eq ?typSiekiery tytanowa)
        then
            (if  ( >= ?maxUdzwig (+ ?udzwig ?wagaDrewna ?wagaDrewna ?wagaDrewna) )
            then
                (if ( >= (- ?energia (* 2 ?strataEnergii)) 0 )
                then
                (bind ?idDrewna_a (str-cat ?id "_" ?iteracja a))
                (assert (drewno (id ?idDrewna_a) (cena ?cenaDrewna) ( waga ?wagaDrewna) ) )
                
                 (bind ?idDrewna_b (str-cat ?id "_" ?iteracja b))
                (assert (drewno (id ?idDrewna_b) (cena ?cenaDrewna) ( waga ?wagaDrewna) ) )
                
                 (bind ?idDrewna_c (str-cat ?id "_" ?iteracja c))
                (assert (drewno (id ?idDrewna_c) (cena ?cenaDrewna) ( waga ?wagaDrewna) ) )
                
                (modify ?drzewo  (stan sciete) )
                 ;zuzycie siekiery
                (if ( >= ( + ?zuzycie 10) 100) 
                then
                    ;tracimy siekierę
                    (retract ?siekiera)
                    (modify ?drwal (scieteDrewno ?drewnoDrwala ?idDrewna_a ?idDrewna_b ?idDrewna_c)
                            (udzwig  (+ ?udzwig ?wagaDrewna ?wagaDrewna ?wagaDrewna))
                            (energia ( - ?energia (* 2 ?strataEnergii)))
                            (siekiera nil)
                    )
                 (printout resultFile "Drwal: " ?id " stracil swa siekiere " ?idSiekiery". " crlf)
                else
                    (modify ?siekiera (zuzycie (+ ?zuzycie 10)))
                
                    (modify ?drwal (scieteDrewno ?drewnoDrwala ?idDrewna_a ?idDrewna_b ?idDrewna_c )
                            (udzwig  (+ ?udzwig ?wagaDrewna ?wagaDrewna ?wagaDrewna))
                            (energia ( - ?energia (* 2 ?strataEnergii)))
                    )
                )
                (printout resultFile "Drwal: " ?id " scial drzewo na kratce " ?idKratki " " crlf)
                else
                (printout resultFile "Drwal: " ?id " nie moze sciac drzewa. Za malo enegrii." crlf)
           )
            else
            (printout resultFile "Drwal: " ?id " nie moze sciac drzewa. Za maly udzwig maksymalny." crlf)
            )
         )
    ) 
   
    (retract ?akcja)
    (close)
) 
; kupowanie wozu z grodu przez drwala
(defrule kupWozZGrodu (declare (salience 4))
    ?drwal <- (drwal (id ?id) (idKratki ?idKratki) (zloto ?zlotoDrwala))
    ?grod <- (grod (nazwa ?nazwaGrodu) (idKratki ?idKratki))
    ?nowyWoz <-(woz (id ?idWozu) (cena ?cenaWozu)(udzwig ?udzwigNowegoWozu) 
               (idGrodu ?nazwaGrodu))
    ?akcja <- (akcjaKupWoz (idAgenta ?id) (idWozu ?idWozu))
    (not (akcjaOdpoczywanie (idAgenta ?id)))
=>
    (open "src/clips/results.txt" resultFile "a")
    
    (if (>= ?zlotoDrwala ?cenaWozu)
    then
        (modify ?drwal (woz ?idWozu)(zloto (- ?zlotoDrwala ?cenaWozu))
         (maxUdzwig ?udzwigNowegoWozu)  
        )    
        (printout resultFile "Drwal: " ?id " kupil woz o udzwigu " ?udzwigNowegoWozu " za " ?cenaWozu " sztuk zlota." crlf)
    else
    (printout resultFile "Drwal: " ?id "ma za malo zlota zeby kupić woz. " crlf)
    ) 
   (retract ?akcja)
   (modify ?nowyWoz (idGrodu nil))
   (close)
)
; drwal kupuje siekiere z grodu
(defrule kupSiekiereZGrodu (declare (salience 4))
    ?drwal <- (drwal (id ?id) (idKratki ?idKratki)   (zloto ?zlotoDrwala) )
    ?grod <- (grod (nazwa ?nazwaGrodu) (idKratki ?idKratki))
    ?nowaSiekiera <-(siekiera (id ?idSiekiery) (cena ?cenaSiekiery) 
                (typ ?typSiekiery) (zuzycie ?zuzycieSiekiery)
               (idGrodu ?nazwaGrodu))
    ?akcja <- (akcjaKupSiekiere (idAgenta ?id) (idSiekiery ?idSiekiery))
    (not (akcjaOdpoczywanie (idAgenta ?id)))
=>
    (open "src/clips/results.txt" resultFile "a")
    
    (if (>= ?zlotoDrwala ?cenaSiekiery)
    then
        (modify ?drwal (siekiera ?idSiekiery)
        (zloto (- ?zlotoDrwala ?cenaSiekiery))  
        )    
        ;naprawiamy siekierę. Przecież musi być nowa :)
        (modify ?nowaSiekiera (zuzycie 0))
        (printout resultFile "Drwal: " ?id " kupil siekiere typu " ?typSiekiery " za " ?cenaSiekiery " sztuk zlota." crlf)
    else
        (printout resultFile "Drwal: " ?id "ma za malo zlota zeby kupić siekiere. " crlf)
    ) 
   (retract ?akcja)
   (modify ?nowaSiekiera (idGrodu nil))
   (close)
)
;drwal zawsze sprzedaje całe drewno jakie ma
(defrule sprzedajDrewnoWGrodzie (declare (salience 2))
?drwal <- (drwal (id ?id) (idKratki ?idKratki)   (zloto ?zlotoDrwala) (scieteDrewno $?drewno) )
    ?grod <- (grod (nazwa ?nazwaGrodu) (idKratki ?idKratki))
    ?akcja <- (akcjaSprzedajDrewno (idAgenta ?id) )
    (not (akcjaOdpoczywanie (idAgenta ?id)))
=>
(open "src/clips/results.txt" resultFile "a")

(bind ?zysk 0)
(loop-for-count (?i 0 (- (length $?drewno) 1)) do
    
    (bind ?drewnoId (nth$ (+ ?i 1) $?drewno))
 
    (bind ?cena (fact-slot-value (nth$ 1 (find-fact ((?d drewno))(eq ?d:id ?drewnoId))) cena ) )
    (bind ?zysk (+ ?zysk ?cena))

   (retract (nth$ 1(find-fact ((?d drewno))(eq ?d:id ?drewnoId))))
)
    (modify ?drwal(scieteDrewno nil) (udzwig 0) (zloto (+ ?zlotoDrwala ?zysk)))
    (printout resultFile "Drwal o id: " ?id " sprzedal drewno" crlf)
    (retract ?akcja)
    (close)
)
; TODO: Sprawdzenie, czy ma miejsce w magazynie.
(defrule kupTowarZGrodu (declare (salience 4))
    ?agent <- (kupiec (id ?id)(pojemnoscMagazynu ?pojemnosc)(przedmioty ?przedmioty))
    ?grod <- (grod (nazwa ?idGrodu))
    ?akcja <- (akcjaKupowanie (idAgenta ?id)(idPrzedmiotu ?idPrzedmiotu)(idSprzedawcy ?idGrodu))
=>
(open "src/clips/results.txt" resultFile "a")


(close)
)

;regula, ktora okresla jakie kratki widzi dany agent
(defrule okreslanieWidocznosci (declare (salience 0))
	(or 
		(poslaniec (id ?agentId)(idKratki ?idKratki)(poleWidzenia ?poleWidzenia))
		(kupiec (id ?agentId)(idKratki ?idKratki)(poleWidzenia ?poleWidzenia))
		(zlodziej (id ?agentId)(idKratki ?idKratki)(poleWidzenia ?poleWidzenia))
		(rycerz (id ?agentId)(idKratki ?idKratki)(poleWidzenia ?poleWidzenia))
		(drwal (id ?agentId)(idKratki ?idKratki)(poleWidzenia ?poleWidzenia))
		(smok (id ?agentId)(idKratki ?idKratki)(poleWidzenia ?poleWidzenia))
	)
	(kratka (id ?idKratki)(pozycjaX ?kX)(pozycjaY ?kY))
    ;(not (akcjaPrzemieszczaniePoDrodze (idAgenta ?agentId)))
    ;(not (akcjaPrzemieszczanie (idAgenta ?agentId)))
    (not (okreslonoWidocznosc (idAgenta ?agentId)))
=>	
(open "src/clips/results.txt" resultFile "a")    
	;kazdy agent widzi kwadratowy obszar o dlugosci boku rownej wartosci poleWidzenia
    (bind ?kratki 0)	
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
			
                (bind ?kratki (+ ?kratki 1))			
            )
		)
	)
 
    (assert (okreslonoWidocznosc (idAgenta ?agentId))) 
    (printout resultFile "Agent: " ?agentId " widzi " ?kratki " kratek" crlf)
    (close)
)
; REGULY DO KLESKI
; niszczenie lasu
(defrule kleskaNiszczLas (declare (salience 10))
    ?drzewo <- (drzewo  (idKratki ?idKratki) )
    ?kleska <- (kleska (id ?idKleski) (idKratki ?idKratki) (niszczenieLasu ?niszczenie))

    (not (kleskaLas (idKleski ?idKleski )))
=>
(open "src/clips/results.txt" resultFile "a")

    (bind ?szansa (+ (mod (random) 100) 1))

    (if  (<= ?szansa ?niszczenie)
    then
    (modify ?drzewo (stan sciete))
    (printout resultFile "Kleska o id: " ?idKleski " zniszczyla drzewo na kratce " ?idKratki crlf)
    )

    (assert (kleskaLas (idKleski ?idKleski )))
    (close)
)
; zabicie mieszkancow
(defrule kleskaZabijMieszkancow (declare (salience 10))
    ?grod <- (grod (nazwa ?idGrodu) (idKratki ?idKratki) (liczbaMieszkancow ?mieszkancow ) )
    ?kleska <- (kleska (id ?idKleski) (idKratki ?idKratki) (zabijanieMieszkancow ?zabijanie))
    (not (kleskaGrod (idKleski ?idKleski )))
=>
(open "src/clips/results.txt" resultFile "a")

    (bind ?mieszkancowNew (- ?mieszkancow ?zabijanie))
    (if (<= ?mieszkancowNew 0)
    then
    (bind ?mieszkancowNew 0)
    )
    (modify ?grod (liczbaMieszkancow ?mieszkancowNew))
    (printout resultFile "Kleska o id: " ?idKleski " zabila  " ?zabijanie " mieszkancow w grodzie " ?idGrodu crlf)

    (assert (kleskaGrod (idKleski ?idKleski )))
    (close)
)

; zranienie agentow
(defrule kleskaZranAgentow (declare (salience 10))
 (or	
		?agent <- (poslaniec (id ?id)(energia ?energia) (idKratki ?idKratki))
		?agent <- (rycerz (id ?id)(energia ?energia) (idKratki ?idKratki))
		?agent <- (drwal (id ?id)(energia ?energia) (idKratki ?idKratki))
		?agent <- (kupiec (id ?id)(energia ?energia) (idKratki ?idKratki))
		?agent <- (zlodziej (id ?id)(energia ?energia) (idKratki ?idKratki))
		?agent <- (smok (id ?id)(energia ?energia) (idKratki ?idKratki))
	)
    ?kleska <- (kleska (id ?idKleski) (idKratki ?idKratki) (oslabianieAgentow ?oslabienie))
    (not (dzialanieKleskiNaAgenta (idAgenta ?id ) ( idKleski ?idKleski) ))

=>
(open "src/clips/results.txt" resultFile "a")

    (bind ?energiaNew (- ?energia ?oslabienie))
    (if (<= ?energiaNew 0)
    then
        (bind ?energiaNew 0)
    )
    (modify ?agent (energia ?energiaNew))
    (printout resultFile "Kleska o id: " ?idKleski " zabrala  " ?oslabienie " punktow energi agentowi " ?id crlf)
    (assert (dzialanieKleskiNaAgenta ( idAgenta ?id) ( idKleski ?idKleski) ))
    (close)

)
; update klesk
(defrule kleskaUaktualnij (declare (salience 9))
?kleska<- (kleska (id ?idKleski) (czasTrwania ?czas) )
(iteracja ?iteracja)
(not (uaktualnianieKlesk ( idKleski ?idKleski)))
=>
(open "src/clips/results.txt" resultFile "a")

(bind ?czasNew (- ?czas 1))
    (if (<= ?czasNew 0)
    then
        (retract ?kleska)
        (printout resultFile "Kleska o id: " ?idKleski " skonczyla sie" crlf)
    else
        (modify ?kleska (czasTrwania ?czasNew))
        (printout resultFile "Kleska o id: " ?idKleski " zostala uaktualniona" crlf)
    )
    (assert (uaktualnianieKlesk ( idKleski ?idKleski)))
    (close)
)

