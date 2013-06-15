(defrule poslaniecWybierzTrase
    ?agent <- (poslaniec (id ?id) (idKratki ?idKratki)(cel ?cel))
=>
    (printout t "weszlo" crlf)
)