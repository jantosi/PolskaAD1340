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
    (printout t "Poslaniec o id: " ?id " kupil przedmiot o id: " ?idPrzedmiotu crlf)
)
