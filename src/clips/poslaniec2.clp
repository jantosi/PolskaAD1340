;wybiera dalszy grod na poczatku
(defrule poslaniecWybierzTrase (declare (salience 20))
    ?agent <- (poslaniec (id ?id)(idKratki ?idKratki)(cel ?cel)(mozliwyRuch ?mozliwyRuch))
    (droga (id ?drogaId)(idKratki ?idKratki)(skadGrod ?skadGrod)(dokadGrod ?dokadGrod)(nrOdcinka ?nrO)(maxOdcinek ?maxO))
    (not (grod (idKratki ?idKratki)))    
    (not (podjetoAkcje))
    (test (eq ?cel nil))
=>        
    (open "src/clips/agentResults.txt" resultFile "a")
    (if (> ?nrO (/ ?maxO 2))
    then    
        (bind ?celPodrozy ?skadGrod)           
        
    else    
        (bind ?celPodrozy ?dokadGrod)      
           
    )
   
   (modify ?agent (cel ?celPodrozy))
   (assert (akcjaPrzemieszczaniePoDrodze (idAgenta ?id)(ileKratek ?mozliwyRuch)(docelowyGrod ?celPodrozy)))
   (assert (podjetoAkcje))
        
   (printout resultFile "Agent: " ?id " wybral trase i postanowil isc do grodu: " ?celPodrozy crlf)   
   (close)
)

(defrule poslaniecWybierzTraseZGrodu (declare (salience 8))
    ?agent <- (poslaniec (id ?id)(idKratki ?idKratki)(paczki $?paczki)(cel ?cel))
    ?grod <- (grod (nazwa ?idGrodu)(idKratki ?idKratki))
    (not (podjetoAkcje))
    (test (eq ?cel nil))    
=>
    (open "src/clips/agentResults.txt" resultFile "a")   
    
    ;wybiera zawsze losowa trase do innego grodu bez sprawdzania paczek
    (bind $?dostepneTrasy (create$ (find-all-facts ((?d droga))(eq ?d:skadGrod ?idGrodu))))
    (bind ?wybranaDroga (nth$ 1 $?dostepneTrasy))
        
    (assert (akcjaZGroduNaDroge (idAgenta ?id)(idKratki (fact-slot-value ?wybranaDroga idKratki))))
    (modify ?agent (cel (fact-slot-value ?wybranaDroga dokadGrod)))
    (assert (podjetoDecyzje))
                  
    (printout resultFile "Agent: " ?id " wybral losowo dalsza droge i idzie do grodu: "(fact-slot-value ?wybranaDroga dokadGrod) crlf)
     
    (assert (podjetoDecyzje))
    (close)
)

(defrule poslaniecIdz (declare (salience 19))
    ?agent <- (poslaniec (id ?id)(idKratki ?idKratki)(cel ?cel)(mozliwyRuch ?mozliwyRuch))
    (droga (id ?drogaId)(idKratki ?idKratki)(dokadGrod ?cel)(nrOdcinka ?nrO)(maxOdcinek ?maxO))
    (not (podjetoAkcje))
=>  
    (open "src/clips/agentResults.txt" resultFile "a")      
    (assert (akcjaPrzemieszczaniePoDrodze (idAgenta ?id)(ileKratek ?mozliwyRuch)(docelowyGrod ?cel)))
    (assert (podjetoAkcje))   
   
    (printout resultFile "Agent: " ?id " kontuunuje podroz do grodu: " ?cel crlf)   
    (close)
)

;kupuje konia w grodze jesli go nie ma i ten kon ma byc zawsze najlepszy - najdrozszy
(defrule poslaniecKupKoniaWGrodzie (declare (salience 10))
    ?agent <- (poslaniec (id ?id)(idKratki ?idKratki)(zloto ?zloto)(kon ?kon))
    ?grod <- (grod (nazwa ?idGrodu)(idKratki ?idKratki))
    (test (eq ?kon nil))
    (not (podjetoAkcje))
=>
    (open "src/clips/agentResults.txt" resultFile "a")     
    
    ;znajduje najdrozszego konia
    (bind $?dostepneKonie (create$ (find-all-facts ((?k kon))(eq ?k:grod ?idGrodu))))
    (bind ?najdrozszyKon (nth$ 1 $?dostepneKonie))
    (loop-for-count (?i 2 (length $?dostepneKonie)) do
        (bind ?aktualKon (nth$ ?i $?dostepneKonie))   
        (if (> (fact-slot-value ?aktualKon cena) (fact-slot-value ?najdrozszyKon cena))
        then
            (bind ?najdrozszyKon ?aktualKon)
        )
    )
    
    ;kupuje konia jesli ma na niego pieniadze
    (if (> ?zloto (fact-slot-value ?najdrozszyKon cena))
    then
        (printout resultFile "Agent: " ?id " kupi konia: " (fact-slot-value ?najdrozszyKon id) " za cene: " (fact-slot-value ?najdrozszyKon cena) crlf)
        (assert (kupienieKonia (idAgenta ?id)(idKonia (fact-slot-value ?najdrozszyKon id))))  
        (assert (podjetoAkcje))       
    )       
       
    (close)
)

;zawsze bierze w danym grodzie tyle paczek ile udzwignie
; wybiera zawsze paczki, ktore maja wage mniejsza niz 8
(defrule poslaniecWezPaczke (declare (salience 9))
    ?agent <- (poslaniec (id ?id)(idKratki ?idKratki)(zloto ?zloto)(paczki $?paczki)(udzwig ?udzwig))
    ?grod <- (grod (nazwa ?idGrodu)(idKratki ?idKratki))
    (not (podjetoAkcje))
    (not (nieWyszloZPaczkami))
=>
    (open "src/clips/agentResults.txt" resultFile "a")
    
    ;znajduje paczki o wadze mniejszej niz 8
    (bind ?czySaPaczki (any-factp ((?p paczka))(and (eq ?p:grodStart ?idGrodu) (< ?p:waga 8))))
    
    (if (eq ?czySaPaczki TRUE)
    then
        (bind $?dostepnePaczki (create$ (find-all-facts ((?p paczka))(and (eq ?p:grodStart ?idGrodu) (< ?p:waga 8)))))
        (bind ?wybranaPaczka (nth$ 1 $?dostepnePaczki))
        
        (bind ?sumaWagPaczek 0)
        (loop-for-count (?i 1 (length $?paczki)) do 
            ;pobieramy id kolejnej, posiadanej przez agenta paczki        
            (bind ?paczkaId (nth$ ?i $?paczki))
        
            ;znajdujemy index faktu paczki w bazie wiedzy
            (bind ?paczkaTmp (nth$ 1 (find-fact ((?p paczka))(eq ?p:id ?paczkaId))))
                
            (bind ?paczkaWaga (fact-slot-value ?paczkaTmp waga))
            (bind ?sumaWagPaczek (+ ?sumaWagPaczek ?paczkaWaga))                
        )
        
        (bind ?sumaWagPaczek (+ ?sumaWagPaczek (fact-slot-value ?wybranaPaczka waga)))    
        (if (>= ?udzwig ?sumaWagPaczek)
        then  
            (assert (akcjaWezPaczke (idAgenta ?id)(idPaczki (fact-slot-value ?wybranaPaczka id))))
    
            (printout resultFile "Agent: " ?id " bedzie chcial wziac paczke: " (fact-slot-value ?wybranaPaczka id) " o wadze: " (fact-slot-value ?wybranaPaczka waga) crlf)
            (assert (podjetoAkcje))       
        
        else
            (printout resultFile "Agent: " ?id " nie mogl zmiescic juz zadnej dodatkowej paczki" crlf)
            (assert (nieWyszloZPaczkami))         
        )
    else
        (printout resultFile "Agent: " ?id " nie znalazl w grodzie zadnej paczki, ktora moglby wziac" crlf)
        (assert (nieWyszloZPaczkami))  
    )
  
    (close)
)

;jesli widzi przeszkode to zawraca
(defrule poslaniecReagujNaPrzeszkode (declare (salience 100))
    ?agent <- (poslaniec (id ?id)(idKratki ?idKratki)(cel ?cel)(mozliwyRuch ?mozliwyRuch))
    (blokada (id ?idBlokady))
    (droga (id ?drogaId)(idKratki ?idKratki)(dokadGrod ?cel)(skadGrod ?skadGrod))
    (not (podjetoAkcje))
=>
    (open "src/clips/agentResults.txt" resultFile "a")

    (modify ?agent (cel ?skadGrod))  
    (assert (akcjaPrzemieszczaniePoDrodze (idAgenta ?id)(ileKratek ?mozliwyRuch)(docelowyGrod ?skadGrod)))
    (assert (podjetoAkcje))   
    
    (printout resultFile "Agent: " ?id " po napotkaniu na blokade zawraca" crlf) 
 
    (close)
)

;jesli ma mniej niz 40 pkt. energii to zawsze odpoczywa 3 iteracje
(defrule poslaniecOdpoczywaj (declare (salience 100))
    (poslaniec (id ?id)(energia ?energia))
    (iteracja ?it)
    (test (< ?energia 40))
    (not (podjetoAkcje))
    (not (akcjaOdpoczywanie(idAgenta ?id)))
=>
    (open "src/clips/agentResults.txt" resultFile "a")

    (assert (akcjaOdpoczywanie (idAgenta ?id)(iteracjaKoniec (+ ?it 3))))   
    (assert (podjetoAkcje))
    (printout resultFile "Agent: " ?id " bedzie odpoczywal 3 iteracje poniewaz ma mniej niz 40 pkt. energii" crlf) 
 
    (close)
)



