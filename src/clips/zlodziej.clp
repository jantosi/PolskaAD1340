; Zlodziej#1
; Opis dzialan:
; 1. Jestem cwany i w dobrze pilnowanych grodach nie kradne
; 2. Jak kradne to duzo i niebezpiecznie
; 3. Jak ukradne, ide do innego grodu by mnie nie znaleziono
;
; Pomysl na drugiego zlodzieja:
; 1. Jestem dobrym zlodziejem i kradne wszedzie
; 2. Kradne malo niebezpiecznie, ale zostaje w grodzie na dluzej
; 3. Jesli straz jest mocno zaalarmowana, wyruszam do innego miasta

(defrule zlodziejKradnij (declare (salience 10))
	?agent <- (zlodziej (id ?id)(idKratki ?idKratki)(cel ?cel)(energia ?energia)(strataEnergii ?strE)(zloto ?zloto))
	?grod <- (grod (nazwa ?idGrodu)(idKratki ?idKratki)(wspAktywnosciStrazy ?wspAktywnosciStrazy))
    (not (podjetoAkcje))
=>
	(open "src/clips/agentResults.txt" resultFile "a")
	(printout resultFile "Agent: " ?id " postanowil ograbic grod " ?idGrodu crlf)   

	;znajduje najdrozszego konia
    (bind $?dostKonie (create$ (find-all-facts ((?k kon))(eq ?k:grod ?idGrodu))))
    (bind ?najdrozszyKon (nth$ 1 $?dostKonie))
    (loop-for-count (?i 2 (length $?dostKonie)) do
        (bind ?aktualNajdrozszy (nth$ ?i $?dostKonie))   
        (if (< (fact-slot-value ?aktualNajdrozszy cena) (fact-slot-value ?najdrozszyKon cena))
        then
            (bind ?najdrozszyKon ?aktualNajdrozszy)
        )
    )

    ;kon znaleziony, teraz trzeba go tylko ukrasc

    (if( > (* ?wspAktywnosciStrazy 1000) (mod (random) 1000) ) ;udany rzut na kradziez
    then
        (modify ?agent (zloto (+ ?zloto (fact-slot-value ?najdrozszyKon cena))))
        (retract ?najdrozszyKon)
    else ;do lochu z nim!
        ;TODO: Prawdziwy loch. Na razie zakładamy, że złodziej ginie.
        (retract ?agent)
    )
	(assert (podjetoAkcje))
	(close)
)
