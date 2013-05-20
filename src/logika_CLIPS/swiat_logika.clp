;REGULY

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
	
	;pobieramy id nowej kratki, na ktorej bedzie stal agent po przemieszczeniu
	(bind ?nowaKratkaId (fact-slot-value (nth$ 1 (find-fact ((?k kratka)) (and (eq ?k:pozycjaX ?nowaKratkaX) (eq ?k:pozycjaY ?nowaKratkaY)))) id))
	
	;zamieniamy id kratki, na ktorej stoi agent oraz odejmujemy mu punkty ruchu
	(modify ?agent (idKratki ?nowaKratkaId)(mozliwyRuch (- ?ruch ?kratki)))
	
	;usuwamy akcje przesuwania
	(retract ?akcja)
	
	(printout t "Przesunieto agenta o id: " ?id " w " ?kierunek " nowy x : " ?nowaKratkaX  " nowy y: " ?nowaKratkaY " kratka: " ?nowaKratkaId crlf)
)
