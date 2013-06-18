

(defrule drwalZetnijDrzewo (declare (salience 22))
 ?drwal <- (drwal  
	( udzwig ?udzwig)  
    ( maxUdzwig ?maxUdzwig)
	( id ?id) 
	( idKratki ?idKratki) 
    ( siekiera ?idSiekiery) 


)
?drzewo <- (drzewo (idKratki ?idKratki) (stan niesciete) (rodzajDrzewa ?typDrzewa))
?siekiera <-(siekiera (id ?idSiekiery) (typ ?typ))
(test (neq ?siekiera nil))

(not (podjetoAkcje))

=>
( if (eq ?typ zelazna)
then
(bind ?mnoznik 1)
)
( if (eq ?typ zlota)
then
(bind ?mnoznik 2)
)
( if (eq ?typ tytanowa)
then
(bind ?mnoznik 3)
)

( if (eq ?typDrzewa sosna)
then
(bind ?waga 10)
)
( if (eq ?typDrzewa buk)
then
(bind ?waga 20)
)
( if (eq ?typDrzewa dab)
then
(bind ?waga 30)
)
(if (>= ?maxUdzwig ( + ?udzwig ( * ?mnoznik ?waga)))
then
(open "src/clips/agentResults.txt" resultFile "a")
(printout resultFile "Sciecie drzewa " ?idKratki crlf) 





(close)
    (assert (akcjaZetnijDrzewo (idAgenta ?id)))
    (assert (podjetoAkcje))
)

)
;kupuje wszystko gdy jest w miescie
(defrule drwalKupSiekiere
 ?drwal <- (drwal (id ?id)(idKratki ?idKratki)(cel ?cel)(mozliwyRuch ?mozliwyRuch)


     ( udzwig ?udzwig)  
    ( maxUdzwig ?maxUdzwig)
	( siekiera ?siekiera) 
	( woz ?woz) 
     (zloto ?zloto))
?grod<-(grod (nazwa ?idGrodu) (idKratki ?idKratki))
?nowaSiekiera <-(siekiera (id ?idSiekiery) (cena ?cenaSiekiery) 
                (typ ?typSiekiery) (zuzycie ?zuzycieSiekiery)
               (idGrodu ?idGrodu))
(test (> ?zloto ?cenaSiekiery))
(test (neq ?cel siekiera))
(not (podjetoAkcje))
=>
(open "src/clips/agentResults.txt" resultFile "a")
(printout resultFile "Drwal " ?id " postanawia kupić siekiere" crlf) 
(close)
(modify ?drwal (cel siekiera))
(assert ( akcjaKupSiekiere  (idAgenta ?id)(idSiekiery ?idSiekiery)))
(assert (podjetoAkcje))


)
(defrule drwalSprzedajDrewno
 ?drwal <- (drwal (id ?id)(idKratki ?idKratki) (scieteDrewno $?drewno ))
?grod<-(grod (nazwa ?idGrodu) (idKratki ?idKratki))
(test (neq $?drewno nil))
(not (podjetoAkcje))
=>
(open "src/clips/agentResults.txt" resultFile "a")
(printout resultFile "Drwal " ?id " postanawia sprzedać drewno" crlf) 

(close)
(assert ( akcjaSprzedajDrewno  (idAgenta ?id)))
(assert (podjetoAkcje))
)
; kupuje pierwsza lepszą siekiere gdy jest w miescie



;; udaj się do grodu
(defrule idzDoGrodu (declare (salience 20))
    ?drwal <- (drwal (id ?id)(idKratki ?idKratki)(cel ?cel)(mozliwyRuch ?mozliwyRuch)
     ( udzwig ?udzwig)  
    ( maxUdzwig ?maxUdzwig)
	( siekiera ?siekiera) 
	( woz ?woz) )
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
   
   (modify ?drwal (cel ?celPodrozy))
   (assert (akcjaPrzemieszczaniePoDrodze (idAgenta ?id)(ileKratek ?mozliwyRuch)(docelowyGrod ?celPodrozy)))
   (assert (podjetoAkcje))
        
   (printout resultFile "Agent: " ?id " wybral trase i postanowil isc do grodu: " ?celPodrozy crlf)   
   (close)
)

(defrule Idz (declare (salience 20))
    ?agent <- (drwal (id ?id)(idKratki ?idKratki)(cel ?cel)(mozliwyRuch ?mozliwyRuch))
    (droga (id ?drogaId)(idKratki ?idKratki)(dokadGrod ?cel)(nrOdcinka ?nrO)(maxOdcinek ?maxO))
    (not (podjetoAkcje))
=>  
    (open "src/clips/agentResults.txt" resultFile "a")      
    (assert (akcjaPrzemieszczaniePoDrodze (idAgenta ?id)(ileKratek ?mozliwyRuch)(docelowyGrod ?cel)))
    (assert (podjetoAkcje))   
   
    (printout resultFile "Agent: " ?id " kontuunuje podroz do grodu: " ?cel crlf)   
    (close)
)
;; rusz się jak już nie mas nic innego do zobienia:)
(defrule drwalRuszSie
?drwal <- (drwal  
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
(modify ?drwal (cel nil))
(open "src/clips/agentResults.txt" resultFile "a")      
    (printout resultFile "Drwal: " ?id " szuka drogi " crlf)   
(close)
)
; przesuwa na drzewo jeżeli tylko w zasiegu
(defrule przesunNaDrzewo (declare (salience 21))
 ?drwal <- (drwal  
	( udzwig ?udzwig)  
    ( maxUdzwig ?maxUdzwig)
	( siekiera ?idSiekiery) 
	( woz ?woz) 
	( scieteDrewno $?scieteDrewno) 
	( id ?id) 
	( mozliwyRuch ?mozliwyRuch) 
	( idKratki ?idKratki) 
	( poleWidzenia ?poleWidzenia) 
	( predkosc ?predkosc) 
	( dodatekPredkosc ?dodatekPredkosc)
	( energia ?energia) 
	( strataEnergii ?strataEnergii)
	( odnawianieEnergii ?odnawianieEnergii) 
	( zloto ?zloto)
    ( cel ?cel) 
)
?drzewo <- (drzewo (idKratki ?kratkaDrzewa) (stan niesciete) (rodzajDrzewa ?typDrzewa))
?siekiera <-(siekiera (id ?idSiekiery) (typ ?typ))
(test (neq ?idSiekiery nil))

(not (podjetoAkcje))
=>
( if (eq ?typ zelazna)
then
(bind ?mnoznik 1)
)
( if (eq ?typ zlota)
then
(bind ?mnoznik 2)
)
( if (eq ?typ tytanowa)
then
(bind ?mnoznik 3)
)

( if (eq ?typDrzewa sosna)
then
(bind ?waga 10)
)
( if (eq ?typDrzewa buk)
then
(bind ?waga 20)
)
( if (eq ?typDrzewa dab)
then
(bind ?waga 30)
)
(if (>= ?maxUdzwig ( + ?udzwig ( * ?mnoznik ?waga)))
then
(open "src/clips/agentResults.txt" resultFile "a")
(printout resultFile "Drzewo na kratce " ?kratkaDrzewa crlf) 


    (assert (akcjaPrzesunNaKratke (idAgenta ?id) (idKratki ?kratkaDrzewa)))
    (printout resultFile "Drwal: " ?id "idzie do drzewa"  crlf)
    (assert (podjetoAkcje))
(close)
)
)
;akcja debugujaca

(defrule debug
 ?drwal <- (drwal  
	( udzwig ?udzwig)  
    ( maxUdzwig ?maxUdzwig)
	( siekiera ?siekiera) 
	( woz ?woz) 
	( scieteDrewno $?scieteDrewno) 
	( id ?id) 
	( mozliwyRuch ?mozliwyRuch) 
	( idKratki ?idKratki) 
	( poleWidzenia ?poleWidzenia) 
	( predkosc ?predkosc) 
	( dodatekPredkosc ?dodatekPredkosc)
	( energia ?energia) 
	( strataEnergii ?strataEnergii)
	( odnawianieEnergii ?odnawianieEnergii) 
	( zloto ?zloto)
    ( cel ?cel) 
)
=>
(open "src/clips/agentResults.txt" resultFile "a")
;(printout resultFile "Drwal: " ?id "stoi na kratce" ?idKratki  crlf)
;(printout resultFile "Drwal udzwig: " ?udzwig " max udzwig " ?maxUdzwig  crlf)
;(printout resultFile "Drwal różnica" (test (>= (- ?maxUdzwig ?udzwig) 30 )  crlf)
;(facts)
(close)
)

;template decyzji drwala

(defrule drwalDecyzja 
 ?drwal <- (drwal  
	( udzwig ?udzwig)  
    ( maxUdzwig ?maxUdzwig)
	( siekiera ?siekiera) 
	( woz ?woz) 
	( scieteDrewno $?scieteDrewno) 
	( id ?id) 
	( mozliwyRuch ?mozliwyRuch) 
	( idKratki ?idKratki) 
	( poleWidzenia ?poleWidzenia) 
	( predkosc ?predkosc) 
	( dodatekPredkosc ?dodatekPredkosc)
	( energia ?energia) 
	( strataEnergii ?strataEnergii)
	( odnawianieEnergii ?odnawianieEnergii) 
	( zloto ?zloto)
    ( cel ?cel) 
)
 
(not (podjetoAkcje))
=>
(open "src/clips/agentResults.txt" resultFile "a")
 (printout resultFile "Drwal: " ?id "cos zadecydowal"  crlf)
 (assert (podjetoAkcje))
(close)
;if zuzyta siekiera
;if pełny udzwig
;if mało energii
;if jest w grodzie

)
