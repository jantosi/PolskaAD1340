(deffacts startup
;kratki
(kratka (id 2) (pozycjaX 1) (pozycjaY 0))
(kratka (id 3) (pozycjaX 2) (pozycjaY 0))
(kratka (id 7) (pozycjaX 3) (pozycjaY 0))
(kratka (id 1) (pozycjaX 4) (pozycjaY 0))
(kratka (id 8) (pozycjaX 6) (pozycjaY 0))
(kratka (id 4) (pozycjaX 0) (pozycjaY 1))
(kratka (id 5) (pozycjaX 0) (pozycjaY 2))
(kratka (id 9) (pozycjaX 1) (pozycjaY 3))
(kratka (id 6) (pozycjaX 2) (pozycjaY 3))
(kratka (id 8) (pozycjaX 3) (pozycjaY 3))
(kratka (id 10) (pozycjaX 10) (pozycjaY 10))
(kratka (id 11) (pozycjaX 11) (pozycjaY 10))
(kratka (id 12) (pozycjaX 12) (pozycjaY 10))
(kratka (id 19) (pozycjaX 13) (pozycjaY 10))
(kratka (id 18) (pozycjaX 14) (pozycjaY 10))
(kratka (id 15) (pozycjaX 15) (pozycjaY 10))
(kratka (id 16) (pozycjaX 16) (pozycjaY 10))
(kratka (id 17) (pozycjaX 17) (pozycjaY 10))
;bandyci
(rozbojnicy (idKratki 1) (zabieraniePaczek 0.5) (zabieranieZlota 0.5))
;blokady
(blokada (id 1) (idKratki 1))
;kleski
(kleska (id 1) (idKratki 1) (niszczenieLasu 0.5) (oslabianieAgentow 10) (zabijanieMieszkancow 10))
;paczki
(paczka (id paczka1) (waga 17.0) (grodStart nil) (grodKoniec nil))
(paczka (id paczka2) (waga 3.0) (grodStart nil) (grodKoniec nil))
(paczka (id 1) (waga 1.5) (grodStart 1) (grodKoniec 2))
;drogi
(droga (id droga1) (idKratki 10) (skadGrod grod1) (dokadGrod grod2) (platna TRUE) (nawierzchnia utwardzona) (nrOdcinka 1) (maxOdcinek 7))
(droga (id droga1) (idKratki 11) (skadGrod grod1) (dokadGrod grod2) (platna TRUE) (nawierzchnia utwardzona) (nrOdcinka 2) (maxOdcinek 7))
(droga (id droga1) (idKratki 12) (skadGrod grod1) (dokadGrod grod2) (platna TRUE) (nawierzchnia utwardzona) (nrOdcinka 3) (maxOdcinek 7))
(droga (id droga1) (idKratki 13) (skadGrod grod1) (dokadGrod grod2) (platna TRUE) (nawierzchnia utwardzona) (nrOdcinka 4) (maxOdcinek 7))
(droga (id droga1) (idKratki 14) (skadGrod grod1) (dokadGrod grod2) (platna TRUE) (nawierzchnia utwardzona) (nrOdcinka 5) (maxOdcinek 7))
(droga (id droga1) (idKratki 15) (skadGrod grod1) (dokadGrod grod2) (platna TRUE) (nawierzchnia utwardzona) (nrOdcinka 6) (maxOdcinek 7))
(droga (id droga1) (idKratki 16) (skadGrod grod1) (dokadGrod grod2) (platna TRUE) (nawierzchnia utwardzona) (nrOdcinka 7) (maxOdcinek 7))
(droga (id 1) (idKratki 1) (skadGrod 1) (dokadGrod 2) (platna TRUE) (nawierzchnia utwardzona) (nrOdcinka 1) (maxOdcinek 12))
;grody
(grod (nazwa grod1) (idKratki 10) (liczbaMieszkancow 52) (wspAktywnosciStrazy 0.7))
(grod (nazwa grod2) (idKratki 16) (liczbaMieszkancow 12) (wspAktywnosciStrazy 0.1))
(grod (nazwa grod2) (idKratki 1) (liczbaMieszkancow 100) (wspAktywnosciStrazy 100.0))
;drzewa
(drzewo (rodzajDrzewa 1) (idKratki 1) (stan FALSE))
)