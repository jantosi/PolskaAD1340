

;jesli ma mniej niz 60 pkt. energii to zawsze odpoczywa 3 iteracje
(defrule smokOdpoczywaj (declare (salience 100))
    (smok (id ?id)(energia ?energia))
    (iteracja ?it)
    (test (< ?energia 60))
    (not (podjetoAkcje))
    (not (akcjaOdpoczywanie(idAgenta ?id)))
=>
    (open "src/clips/agentResults.txt" resultFile "a")

    (assert (akcjaOdpoczywanie (idAgenta ?id)(iteracjaKoniec (+ ?it 3))))   
    (assert (podjetoAkcje))
    (printout resultFile "Smok: " ?id " bedzie odpoczywal 3 iteracje poniewaz ma mniej niz 60 pkt. energii" crlf) 
 
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

;jak widzi rycerza i ma więcej niż 50 pkt energii to z nim walczy, jak nie to ucieka.
(defrule smokReagujNaRycerza (declare (salience 100))
    (smok (id ?id)(energia ?energia))
    (rycerz (id ?idRycerza))
    (iteracja ?it)
    (not (podjetoAkcje))
=>
    (open "src/clips/agentResults.txt" resultFile "a")

    (if (> ?energia 50)
    then
        ;wybierz sobie atak
        (bind ?atak  (mod (random) 3) )
        (assert (akcjaAtak (idAgenta ?id)(idOfiary ?idRycerza)(rodzajAtaku ?atak)))
        (printout resultFile "Smok: " ?id " atakuje rycerza: " ?idRycerza "." crlf)  
    else
        ;smok ucieka w dowolnym kierunku.
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
        (assert (akcjaPrzemieszczanie (idAgenta ?id)(ileKratek ?mozliwyRuch)(kierunek ?kierunek)))
        (printout resultFile "Smok: " ?id " ucieka od rycerza." crlf)
    )
    
    (assert (podjetoAkcje))
    (close)
)