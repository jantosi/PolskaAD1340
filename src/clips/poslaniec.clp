
(defrule poslaniecWybierzTrase
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

(defrule poslaniecIdz
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
(defrule poslaniecKupKoniaWGrodzie
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
