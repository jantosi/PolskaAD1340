;REGULY

;przemieszczanie agentow
(defrule przemieszczaj
	?agent <- (agent (id ?id)(mozliwyRuch ?ruch)(kratkaX ?kratkaX)(kratkaY ?kratkaY))
	?akcja <- (akcjaPrzemieszczanie (idAgenta ?id)(ileKratek ?kratki)(kierunek ?kierunek))
	(test (>= ?ruch ?kratki))
	(mapa ?height ?width)
=>
	(switch ?kierunek
		(case dol then
			(bind ?przesuniecieY (+ ?kratkaY ?kratki))
			
			(if (>= ?przesuniecieY ?height)
			then
				(bind ?przesuniecieY ?height)
			)
		
			(modify ?agent (kratkaY ?przesuniecieY)(mozliwyRuch (- ?ruch ?kratki)))
		)
		(case gora then 
			(bind ?przesuniecieY (- ?kratkaY ?kratki))
			
			(if (<= ?przesuniecieY 0)
			then
				(bind ?przesuniecieY 0)
			)
			
			(modify ?agent (kratkaY ?przesuniecieY)(mozliwyRuch (- ?ruch ?kratki)))
		)
		(case lewo then 
			(bind ?przesuniecieX (- ?kratkaX ?kratki))
			
			(if (<= ?przesuniecieX 0)
			then
				(bind ?przesuniecieX 0)
			)
			
			(modify ?agent (kratkaX ?przesuniecieX)(mozliwyRuch (- ?ruch ?kratki)))
		)
		(case prawo then 
			(bind ?przesuniecieX (+ ?kratkaX ?kratki))
			
			(if (>= ?przesuniecieX ?width)
			then
				(bind ?przesuniecieX ?width)
			)
			
			(modify ?agent (kratkaX ?przesuniecieX)(mozliwyRuch (- ?ruch ?kratki)))
		)
	)
	(printout t "Przesunieto agenta o id: "?id " o ruchu: " ?ruch " w " ?kierunek crlf)
	(retract ?akcja)
)
