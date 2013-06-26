;Rycerz losowo patroluje całą mape
;Jeżeli zobaczy smoka to atakuje losowym atakiem 
;Odpoczywa jeżeli ma mniej niż 20pkt energii
;Nie kupuje zbroi

;Rycerz porusza sie po mapie i patroluje
(defrule rycerzPatroluj
?rycerz <- (rycerz  
	( id ?id) 
	( mozliwyRuch ?mozliwyRuch) 
	( idKratki ?idKratki) 
    ( cel ?cel) 
)
(not (podjetoAkcje))
=>

(bind ?kierunek  (mod (random) 4) )
(if (eq ?kierunek 0)
 then
    (bind ?kierunek prawo)
)
(if (eq ?kierunek 1)
 then
    (bind ?kierunek dol)
)
(if (eq ?kierunek 2)
 then
    (bind ?kierunek dol)
)
(if (eq ?kierunek 3)
 then
    (bind ?kierunek prawo)
)
(assert (podjetoAkcje))
(assert (akcjaPrzemieszczanie (idAgenta ?id)(ileKratek ?mozliwyRuch)(kierunek ?kierunek)))
(modify ?rycerz (cel nil))
(open "src/clips/agentResults.txt" resultFile "a")      
    (printout resultFile "Rycerz: " ?id " patroluje " crlf)   
(close)
)

;jak widzi smoka to odrazu atakuje
(defrule rycerzAtakujSmoka (declare (salience 100))
    (rycerz (id ?id)(energia ?energia))
    (smok (id ?idSmoka))
    (iteracja ?it)
    (not (podjetoAkcje))
=>
    (open "src/clips/agentResults.txt" resultFile "a")

    ;wybiera atak losowo
    (bind ?atak  (mod (random) 3) )
    (assert (akcjaAtak (idAgenta ?id)(idOfiary ?idSmoka)(rodzajAtaku ?atak)))
    (printout resultFile "Rycerz: " ?id " atakuje smoka: " ?idSmoka "." crlf)  
    
    (assert (podjetoAkcje))
    (close)
)

;Jeżeli rycerz się zmęczy to odpoczywa (<20)
(defrule smokOdpoczywaj (declare (salience 100))
    (rycerz (id ?id)(energia ?energia))
    (iteracja ?it)
    (test (< ?energia 20))
    (not (podjetoAkcje))
    (not (akcjaOdpoczywanie(idAgenta ?id)))
=>
    (open "src/clips/agentResults.txt" resultFile "a")

    (assert (akcjaOdpoczywanie (idAgenta ?id)(iteracjaKoniec (+ ?it 3))))   
    (assert (podjetoAkcje))
    (printout resultFile "Rycerz: " ?id " odpoczywa poniewaz ma mniej niz 20 pkt. energii" crlf) 
 
    (close)
)