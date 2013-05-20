;REGULY

;regula, ktora okresla jakie kratki widzi dany agent
(defrule okreslanieWidocznosci
	(agent (id ?agentId)(idKratki ?idKratki)(poleWidzenia ?poleWidzenia))
	(kratka (id ?idKratki)(pozycjaX ?kX)(pozycjaY ?kY))
=>	
	;kazdy agent widzi kwadratowy obszar o dlugosci boku rownej wartosci poleWidzenia
	(loop-for-count (?i (- 0 ?poleWidzenia) ?poleWidzenia) do
		(loop-for-count (?j (- 0 ?poleWidzenia) ?poleWidzenia) do
			(bind ?x (+ ?kX ?i))
			(bind ?y (+ ?kY ?j))
			
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
	?agent <- (agent (id ?id)(mozliwyRuch ?ruch)(idKratki ?idKratki))
	?kratka <- (kratka (id ?idKratki)(pozycjaX ?kratkaX)(pozycjaY ?kratkaY))
	?akcja <- (akcjaPrzemieszczanie (idAgenta ?id)(ileKratek ?kratki)(kierunek ?kierunek))
	(test (>= ?ruch ?kratki))
	(mapa ?height ?width)
=>
	;okreslamy wspolrzedne nowej kratki, na ktorej bedzie stal agent po przemieszczeniu
	;uwzgledniajac przy tym granice mapki - aby agent nie wyszedl poza mapke
	(switch ?kierunek
		(case "dol" then
			(bind ?nowaKratkaY (+ ?kratkaY ?kratki))
			(bind ?nowaKratkaX ?kratkaX)
			
			(if (>= ?nowaKratkaY ?height)
			then
				(bind ?nowaKratkaY ?height)
			)
		)
		(case "gora" then 
			(bind ?nowaKratkaY (- ?kratkaY ?kratki))
			(bind ?nowaKratkaX ?kratkaX)
			
			(if (<= ?nowaKratkaY 0)
			then
				(bind ?nowaKratkaY 0)
			)
		)
		(case "lewo" then 
			(bind ?nowaKratkaX (- ?kratkaX ?kratki))
			(bind ?nowaKratkaY ?kratkaY)
			
			(if (<= ?nowaKratkaX 0)
			then
				(bind ?nowaKratkaX 0)
			)
		)
		(case "prawo" then 
			(bind ?nowaKratkaX (+ ?kratkaX ?kratki))
			(bind ?nowaKratkaY ?kratkaY)
			
			(if (>= ?nowaKratkaX ?width)
			then
				(bind ?nowaKratkaX ?width)
			)
		)
	)
	
	;pobieramy id nowej kratki, na ktorej bedzie stal agent po przemieszczeniu
	(bind ?nowaKratkaId (fact-slot-value (nth$ 1 (find-fact ((?k kratka)) (and (eq ?k:pozycjaX ?nowaKratkaX) (eq ?k:pozycjaY ?nowaKratkaY)))) id))
	
	;zamieniamy id kratki, na ktorej stoi agent oraz odejmujemy mu punkty ruchu
	(modify ?agent (idKratki ?nowaKratkaId)(mozliwyRuch (- ?ruch ?kratki)))
	
	;usuwamy akcje przesuwania
	(retract ?akcja)
	
	(printout t "Przesunieto agenta o id: " ?id " w " ?kierunek " nowy x : " ?nowaKratkaX  " nowy y: " ?nowaKratkaY " kratka: " ?nowaKratkaId crlf)
)
