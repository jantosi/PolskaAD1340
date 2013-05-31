;REGULY
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
				(printout t "Agent o id: " ?agentId " widzi kratke o id: " ?widzialnaKratkaId crlf)
			)
		)
	)
)

;przemieszczanie agentow po kratkach
(defrule przemieszczanie
	(or	
		?agent <- (poslaniec (id ?id)(mozliwyRuch ?ruch)(idKratki ?idKratki))
		?agent <- (rycerz (id ?id)(mozliwyRuch ?ruch)(idKratki ?idKratki))
		?agent <- (drwal (id ?id)(mozliwyRuch ?ruch)(idKratki ?idKratki))
		?agent <- (kupiec (id ?id)(mozliwyRuch ?ruch)(idKratki ?idKratki))
		?agent <- (zlodziej (id ?id)(mozliwyRuch ?ruch)(idKratki ?idKratki))
		?agent <- (smok (id ?id)(mozliwyRuch ?ruch)(idKratki ?idKratki))
	)
	?kratka <- (kratka (id ?idKratki)(pozycjaX ?kratkaX)(pozycjaY ?kratkaY))
	?akcja <- (akcjaPrzemieszczanie (idAgenta ?id)(ileKratek ?kratki)(kierunek ?kierunek))
	(test (>= ?ruch ?kratki))
	(mapa ?height ?width)
=>
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
		)
		(case gora then 
			(bind ?nowaKratkaY (- ?kratkaY ?kratki))
			(bind ?nowaKratkaX ?kratkaX)
			
			(if (<= ?nowaKratkaY 0)
			then
				(bind ?nowaKratkaY 0)
			)
		)
		(case lewo then 
			(bind ?nowaKratkaX (- ?kratkaX ?kratki))
			(bind ?nowaKratkaY ?kratkaY)
			
			(if (<= ?nowaKratkaX 0)
			then
				(bind ?nowaKratkaX 0)
			)
		)
		(case prawo then 
			(bind ?nowaKratkaX (+ ?kratkaX ?kratki))
			(bind ?nowaKratkaY ?kratkaY)
			
			(if (>= ?nowaKratkaX ?width)
			then
				(bind ?nowaKratkaX ?width)
			)
		)
	)
	
	;sprawdzamy czy jest na mapie kratka, na ktora ma sie przemiescic agent	
	(bind ?czyJestKratka (any-factp ((?k kratka)) (and (eq ?k:pozycjaX ?nowaKratkaX) (eq ?k:pozycjaY ?nowaKratkaY))))
	(if (eq ?czyJestKratka TRUE)
	then
		;pobieramy id nowej kratki, na ktorej bedzie stal agent po przemieszczeniu
		(bind ?nowaKratkaId (fact-slot-value (nth$ 1 (find-fact ((?k kratka)) (and (eq ?k:pozycjaX ?nowaKratkaX) (eq ?k:pozycjaY ?nowaKratkaY)))) id))
	
		;zamieniamy id kratki, na ktorej stoi agent oraz odejmujemy mu punkty ruchu
		(modify ?agent (idKratki ?nowaKratkaId)(mozliwyRuch (- ?ruch ?kratki)))

		(printout t "Przesunieto agenta o id: " ?id " w " ?kierunek ." Nowy x : " ?nowaKratkaX  ", nowy y: " ?nowaKratkaY crlf)
	)
	
	;usuwamy akcje przesuwania
	(retract ?akcja)
)

;regula okresla parametry poslanca po zakupieniu konia, czyli
;po sprawdzeniu istnienia faktu kupienieKonia
(defrule sprawdzKupienieKoniaPoslaniec
    ?poslaniec <- (poslaniec (id ?poslaniecId) (kon ?kon) (paczki $?paczki))
    ?kupienieKonia <- (kupienieKonia (idAgenta ?poslaniecId)(idKonia ?kupionyKon))
=>
    ;pobieramy index kupionego konia z bazy wiedzy        
    (bind ?kupionyKon (nth$ 1 (find-fact ((?konTmp kon)) (eq ?konTmp:id ?kupionyKon))))        
     
    ;wyciagamy poszczegolne wartosci pol z obiektu kon o okreslonym wyzej indeksie        
    (bind ?konId (fact-slot-value ?kupionyKon id))        
    (bind ?konUdzwig (fact-slot-value ?kupionyKon udzwig))
    (bind ?konPredkosc (fact-slot-value ?kupionyKon predkosc))        
    
     ;modyfikujemy parametry poslanca uzwgledniajac zakupionego konia
    (modify ?poslaniec (udzwig ?konUdzwig) (predkosc ?konPredkosc) (poleWidzenia ?konPredkosc) (kon ?konId))
    
    (printout t "kupienie: " ?kupienieKonia crlf)        
        
    ;usuwamy akcje kupienia konia        
    (retract ?kupienieKonia)
)

;regula aktualizujaca straty energii poslanca z uwzglednieniem paczek jakie niesie
(defrule obliczStartyEnergiiPoslanca
    ?poslaniec <- (poslaniec (id ?poslaniecId) (kon ?kon) (paczki $?paczki))
    
    ;sprawdzamy czy poslaniec w danej turze nie byl jeszcze modyfikowany
    ;jest to fakt kontrolny, ktory na poczatku kazdej tury NIE ISTNIEJE w bazie wiedzy
    ;jest on wstawiany dopiero po modyfikacji poslanca    
    (not (modyfikacjaPoslanca ?poslaniecId))
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
   
    (bind ?strataEnergii (round (+ (* ?sumaWagPaczek 0.5) 2)))
    (if (not (eq ?kon nil))
    then
        (bind ?konIndex (nth$ 1 (find-fact ((?konTmp kon)) (eq ?konTmp:id ?kon))))
        (bind ?konZmeczenieJezdzcy (fact-slot-value ?konIndex zmeczenieJezdzcy))
        
        (bind ?strataEnergii (round (- ?strataEnergii (* ?strataEnergii ?konZmeczenieJezdzcy))))
    )    
    
    (printout t "nowa starta energii: " ?strataEnergii crlf)
    
    (modify ?poslaniec (strataEnergii ?strataEnergii))
    
    ;fakt kontrolny, za pomoca ktorego oznaczamy, 
    ;ze dany poslaniec w danej iteracji zostal juz zmodfikowany    
    (assert (modyfikacjaPoslanca ?poslaniecId))
    
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
