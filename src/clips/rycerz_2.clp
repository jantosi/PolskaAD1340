;;Rycerz porusza się miedzy grodami
;;będąc w nimi może kupić zbroje
;;Jezeli spotka smoka i ma wystarczająco ilosc energii to atakuje
;;zawsze najmocniejszym uderzeniem
;;odpoczywa w podrozy gdy energia < 50

;; Rycerz patroluje między grodami
(defrule idzDoGrodu (declare (salience 20))
    ?rycerz <- (rycerz (id ?id)(idKratki ?idKratki)(cel ?cel)(mozliwyRuch ?mozliwyRuch)
     ( zbroja ?zbroja)
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
   
   (modify ?rycerz (cel ?celPodrozy))
   (assert (akcjaPrzemieszczaniePoDrodze (idAgenta ?id)(ileKratek ?mozliwyRuch)(docelowyGrod ?celPodrozy)))
   (assert (podjetoAkcje))
        
   (printout resultFile "Agent: " ?id " wybral trase i postanowil isc do grodu: " ?celPodrozy crlf)   
   (close)
)

(defrule Idz (declare (salience 20))
    ?agent <- (rycerz (id ?id)(idKratki ?idKratki)(cel ?cel)(mozliwyRuch ?mozliwyRuch))
    (droga (id ?drogaId)(idKratki ?idKratki)(dokadGrod ?cel)(nrOdcinka ?nrO)(maxOdcinek ?maxO))
    (not (podjetoAkcje))
=>  
    (open "src/clips/agentResults.txt" resultFile "a")      
    (assert (akcjaPrzemieszczaniePoDrodze (idAgenta ?id)(ileKratek ?mozliwyRuch)(docelowyGrod ?cel)))
    (assert (podjetoAkcje))   
   
    (printout resultFile "Agent: " ?id " kontuunuje podroz do grodu: " ?cel crlf)   
    (close)
)

;Ryczerz kupuje zbroje
(defrule rycerzKupZbroje
 ?rycerz <- (rycerz (id ?id)(idKratki ?idKratki)(cel ?cel)(mozliwyRuch ?mozliwyRuch)
 
    ( zbroja ?zbroja) 
    (zloto ?zloto))
?grod<-(grod (nazwa ?idGrodu) (idKratki ?idKratki))
?nowaZbroja <-(zbroja (id ?idZbroi) (cena ?cenaZbroi) 
                (typ ?typZbroi) (zuzycie ?zuzycieZbroi)
               (idGrodu ?idGrodu))
(test (> ?zloto ?cenaZbroi))
(test (neq ?cel zbroja))
(not (podjetoAkcje))
=>
(open "src/clips/agentResults.txt" resultFile "a")
(printout resultFile "Rycerz " ?id " postanawia kupić zbroje" crlf) 
(close)
(modify ?rycerz (cel zbroja))
(assert ( akcjaKupZbroje  (idAgenta ?id)(idZbroi ?idZbroi)))
(assert (podjetoAkcje))


)

;Jeżeli rycerz się zmęczy to odpoczywa (<50)
(defrule smokOdpoczywaj (declare (salience 100))
    (rycerz (id ?id)(energia ?energia))
    (iteracja ?it)
    (test (< ?energia 50))
    (not (podjetoAkcje))
    (not (akcjaOdpoczywanie(idAgenta ?id)))
=>
    (open "src/clips/agentResults.txt" resultFile "a")

    (assert (akcjaOdpoczywanie (idAgenta ?id)(iteracjaKoniec (+ ?it 3))))   
    (assert (podjetoAkcje))
    (printout resultFile "Rycerz: " ?id " odpoczywa poniewaz ma mniej niz 20 pkt. energii" crlf) 
 
    (close)
)

//Atakuje smoka gdy ma wiecej niz 30 energii i atakuje najmocniejszym uderzeniem
(defrule rycerzAtakujSmoka (declare (salience 100))
    (rycerz (id ?id)(energia ?energia))
    (smok (id ?idSmoka))
    (iteracja ?it)
    (not (podjetoAkcje))
=>
    (open "src/clips/agentResults.txt" resultFile "a")

    (if (> ?energia 30)
    then
        ;wybierz sobie atak
        (bind ?atak  (3) )
        (assert (akcjaAtak (idAgenta ?id)(idOfiary ?idSmoka)(rodzajAtaku ?atak)))
        (printout resultFile "Rycerz: " ?id " atakuje smoka: " ?idSmoka "." crlf)  
    else
        ;rycerz ucieka w dowolnym kierunku.
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
        (printout resultFile "Rycerz: " ?id " ucieka przed smokiem." crlf)
    )
    
    (assert (podjetoAkcje))
    (close)
)