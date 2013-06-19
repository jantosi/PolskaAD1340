

;jesli ma mniej niz 30 pkt. energii to zawsze odpoczywa 5 iteracji
(defrule smokOdpoczywaj (declare (salience 100))
    (smok (id ?id)(energia ?energia))
    (iteracja ?it)
    (test (< ?energia 30))
    (not (podjetoAkcje))
    (not (akcjaOdpoczywanie(idAgenta ?id)))
=>
    (open "src/clips/agentResults.txt" resultFile "a")

    (assert (akcjaOdpoczywanie (idAgenta ?id)(iteracjaKoniec (+ ?it 5))))   
    (assert (podjetoAkcje))
    (printout resultFile "Smok: " ?id " bedzie odpoczywal 5 iteracji poniewaz ma mniej niz 30 pkt. energii" crlf) 
 
    (close)
)

; polataj sobie
(defrule smokLec
?smok <- (smok  
	( id ?id) 
	( mozliwyRuch ?mozliwyRuch) 
	( idKratki ?idKratki) 
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

(open "src/clips/agentResults.txt" resultFile "a")      
    (printout resultFile "Smok: " ?id " sobie lata." crlf)   
(close)
)

;jak widzi rycerza zawsze go atakuje, nie zależnie od poziomu energii.
(defrule smokReagujNaRycerza (declare (salience 100))
    (smok (id ?id)(energia ?energia))
    (rycerz (id ?idRycerza))
    (iteracja ?it)
    (not (podjetoAkcje))
=>
    (open "src/clips/agentResults.txt" resultFile "a")

    ;wybierz sobie atak
    (bind ?atak  (mod (random) 3) )
    (assert (akcjaAtak (idAgenta ?id)(idOfiary ?idRycerza)(rodzajAtaku ?atak)))
    (printout resultFile "Smok: " ?id " atakuje rycerza: " ?idRycerza "." crlf)  
    
    (assert (podjetoAkcje))
    (close)
)