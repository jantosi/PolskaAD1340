;kolejnosc podejmowanych decyzji
; 1. zawsze wybieram trase jesli nie mam celu
; 2. jak mam cel to ide
; 3. jak jestem w grodzi to kupuje konia - oczywiscie jak mam zloto na niego
; 4. biore paczke

(defrule poslaniecWybierzTrase (declare (salience 20))
    ?agent <- (poslaniec (id ?id)(idKratki ?idKratki)(cel ?cel)(mozliwyRuch ?mozliwyRuch))
    (droga (id ?drogaId)(idKratki ?idKratki)(skadGrod ?skadGrod)(dokadGrod ?dokadGrod)(nrOdcinka ?nrO)(maxOdcinek ?maxO))
    (not (podjetoAkcje))
    (test (eq ?cel nil))
=>        
    (open "src/clips/agentResults.txt" resultFile "a")
    (if (> ?nrO (/ ?maxO 2))
    then    
        (bind ?celPodrozy ?dokadGrod)           
        
    else    
        (bind ?celPodrozy ?skadGrod)      
           
    )
   
   (modify ?agent (cel ?celPodrozy))
   (assert (akcjaPrzemieszczaniePoDrodze (idAgenta ?id)(ileKratek ?mozliwyRuch)(docelowyGrod ?celPodrozy)))
   (assert (podjetoAkcje))
        
   (printout resultFile "Agent: " ?id " wybral trase i postanowil isc do grodu: " ?celPodrozy crlf)   
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

;kupuje konia w grodze jesli go nie mam i jesli mam kase na niego
(defrule poslaniecKupKoniaWGrodzie (declare (salience 10))
    ?agent <- (poslaniec (id ?id)(idKratki ?idKratki)(zloto ?zloto)(kon ?kon))
    ?grod <- (grod (nazwa ?idGrodu)(idKratki ?idKratki))
    (test (eq ?kon nil))
    (not (podjetoAkcje))
=>
    (open "src/clips/agentResults.txt" resultFile "a")     
    
    ;znajduje najtanszego konia
    (bind $?dostepneKonie (create$ (find-all-facts ((?k kon))(eq ?k:grod ?idGrodu))))
    (bind ?najtanszyKon (nth$ 1 $?dostepneKonie))
    (loop-for-count (?i 2 (length $?dostepneKonie)) do
        (bind ?aktualKon (nth$ ?i $?dostepneKonie))   
        (if (< (fact-slot-value ?aktualKon cena) (fact-slot-value ?najtanszyKon cena))
        then
            (bind ?najtanszyKon ?aktualKon)
        )
    )
    
    ;jesli ma kase na dowolnego konia to go kupuje - kupuje zawsze najtanszego
    (if (> ?zloto (fact-slot-value ?najtanszyKon cena))
    then
        (printout resultFile "Agent: " ?id " kupi konia: " (fact-slot-value ?najtanszyKon id) " za cene: " (fact-slot-value ?najtanszyKon cena) crlf)
        (assert (kupienieKonia (idAgenta ?id)(idKonia (fact-slot-value ?najtanszyKon id))))  
        (assert (podjetoAkcje))       
    )       
       
    (close)
)

;zawsze bierze najciezsze paczki i zawsze tylko jedna
(defrule poslaniecWezPaczke (declare (salience 5))
    ?agent <- (poslaniec (id ?id)(idKratki ?idKratki)(zloto ?zloto)(paczki $?paczki)(udzwig ?udzwig))
    ?grod <- (grod (nazwa ?idGrodu)(idKratki ?idKratki))
    (not (podjetoAkcje))
    (test (< (length $?paczki) 1))
=>
    (open "src/clips/agentResults.txt" resultFile "a")
    
    ;znajduje najciezsza paczke, ktora jest w stanie udzwignac
    (bind $?dostepnePaczki (create$ (find-all-facts ((?p paczka))(eq ?p:grodStart ?idGrodu))))
    
    (bind ?czyJest FALSE)
    (bind ?najciezszaPaczka (nth$ 1 $?dostepnePaczki))
    (loop-for-count (?i 2 (length $?dostepnePaczki)) do
        (bind ?aktualPaczka (nth$ ?i $?dostepnePaczki))   
        (if  (>= ?udzwig (fact-slot-value ?aktualPaczka waga) )
        then   
            (bind ?czyJest TRUE)
            
            (if (> (fact-slot-value ?aktualPaczka waga) (fact-slot-value ?najciezszaPaczka waga))            
            then
               (bind ?najciezszaPaczka ?aktualPaczka)            
            )
        )
    )   
    
    (if  (>= ?udzwig (fact-slot-value ?najciezszaPaczka waga) )
    then      
         (bind ?czyJest TRUE)       
    )   
    (if (eq ?czyJest TRUE)
    then  
            
        (assert (akcjaWezPaczke (idAgenta ?id)(idPaczki (fact-slot-value ?najciezszaPaczka id))))
    
        (printout resultFile "Agent: " ?id " bedzie chcial wziac paczke: " (fact-slot-value ?najciezszaPaczka id) " o wadze: " (fact-slot-value ?najciezszaPaczka waga) crlf)
        (assert (podjetoAkcje))       
    else
        (printout resultFile "Agent: " ?id " nie znalazl w grodzie zadnej paczki, ktora moglby udzwignac" crlf)   
    )    
    
    (close)
)


