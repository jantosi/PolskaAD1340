
(defrule poslaniecWybierzTrase
    ?agent <- (poslaniec (id ?id)(idKratki ?idKratki)(cel ?cel)(mozliwyRuch ?mozliwyRuch))
    (droga (id ?drogaId)(idKratki ?idKratki)(skadGrod ?skadGrod)(dokadGrod ?dokadGrod)(nrOdcinka ?nrO)(maxOdcinek ?maxO))
    (not (wybranoTrase))
=>        
    (if (eq ?cel nil)
    then
        (if (> ?nrO (/ ?maxO 2))
        then    
            (bind ?celPodrozy ?dokadGrod)           
        
        else    
            (bind ?celPodrozy ?skadGrod)      
           
        )
        (printout t "weszlo" crlf)
   
        (modify ?agent (cel ?celPodrozy))
        (assert (akcjaPrzemieszczaniePoDrodze (idAgenta ?id)(ileKratek ?mozliwyRuch)(docelowyGrod ?celPodrozy)))
        (assert (wybranoTrase))   
    )
       
)