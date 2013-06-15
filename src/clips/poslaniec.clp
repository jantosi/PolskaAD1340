
(defrule poslaniecWybierzTrase
    ?agent <- (poslaniec (id ?id)(idKratki ?idKratki)(cel ?cel)(mozliwyRuch ?mozliwyRuch))
    (droga (id ?drogaId)(idKratki ?idKratki)(skadGrod ?skadGrod)(dokadGrod ?dokadGrod)(nrOdcinka ?nrO)(maxOdcinek ?maxO))
    (not (podjetoAkcje))
    (test (eq ?cel nil))
=>        
    (if (> ?nrO (/ ?maxO 2))
    then    
        (bind ?celPodrozy ?dokadGrod)           
        
    else    
        (bind ?celPodrozy ?skadGrod)      
           
    )
   
   (modify ?agent (cel ?celPodrozy))
   (assert (akcjaPrzemieszczaniePoDrodze (idAgenta ?id)(ileKratek ?mozliwyRuch)(docelowyGrod ?celPodrozy)))
   (assert (podjetoAkcje))
        
   (printout t "Agent: " ?id " wybral trase" crlf)   
 
)

(defrule poslaniecIdz
    ?agent <- (poslaniec (id ?id)(idKratki ?idKratki)(cel ?cel)(mozliwyRuch ?mozliwyRuch))
    (droga (id ?drogaId)(idKratki ?idKratki)(dokadGrod ?cel)(nrOdcinka ?nrO)(maxOdcinek ?maxO))
    (not (podjetoAkcje))
=>        
    (assert (akcjaPrzemieszczaniePoDrodze (idAgenta ?id)(ileKratek ?mozliwyRuch)(docelowyGrod ?cel)))
    (assert (podjetoAkcje))   
   
    (printout t "Agent: " ?id " postanowil isc trasa do grodu " ?cel crlf)   
)