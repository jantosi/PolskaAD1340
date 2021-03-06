(deffacts startup

(rycerz (id rycerz1) 
	(idKratki 11)
	(predkosc 2)
	(poleWidzenia 1)
	(mozliwyRuch 4)
    (cel grod2)
    (energia 50)
    (strataEnergii 10)
    (odnawianieEnergii 3)
)
(akcjaPrzemieszczaniePoDrodze 
    (idAgenta poslaniec1)
    (ileKratek 5)
    (docelowyGrod grod2)
)
(akcjaPrzemieszczaniePoDrodze  
    (idAgenta poslaniec1)
    (ileKratek 3)
    (docelowyGrod grod2)
)
(akcjaPrzemieszczanie (idAgenta poslaniec1)
	(ileKratek 2)
	(kierunek prawo)
)
;(akcjaPrzemieszczanie (idAgenta rycerz1)
;    (ileKratek 1)
;	(kierunek lewo)
;)
(mapa 30 30)


(kon (id kon1) 
    (grod grod1) 
    (udzwig 117)
    (predkosc 3)
    (cena 100)
    (zmeczenieJezdzcy 0.3)
)
(kon (id kon2) 
    (grod grod1) 
    (udzwig 1)
    (predkosc 2)
    (cena 100)
    (zmeczenieJezdzcy 0.1)
)
(paczka (id paczka1)
    (waga 17)
)
(paczka (id paczka2)
    (waga 3)
)
(kupienieKonia (idAgenta poslaniec1)
    (idKonia kon1)
)
;(kupienieKonia (idAgenta poslaniec1)
;    (idKonia kon2)
;)
(grod
	(nazwa grod1) 
	(idKratki 19)
	(liczbaMieszkancow 52)
	(wspAktywnosciStrazy 0.7)
)
(grod
	(nazwa grod2) 
	(idKratki 16)
	(liczbaMieszkancow 12)
	(wspAktywnosciStrazy 0.1)
)
(droga
    (id droga1)
	(idKratki 10) 
	(skadGrod grod1)
	(dokadGrod grod2)
	(platna true)
	(nawierzchnia utwardzona)
    (nrOdcinka 1)
    (maxOdcinek 7)
)
(droga
    (id droga1)
	(idKratki 11) 
	(skadGrod grod1)
	(dokadGrod grod2)
	(platna true)
	(nawierzchnia utwardzona)
    (nrOdcinka 2)
    (maxOdcinek 7)
)
(droga
    (id droga1)
	(idKratki 12) 
	(skadGrod grod1)
	(dokadGrod grod2)
	(platna true)
	(nawierzchnia utwardzona)
    (nrOdcinka 3)
    (maxOdcinek 7)
)
(droga
    (id droga1)
	(idKratki 13) 
	(skadGrod grod1)
	(dokadGrod grod2)
	(platna true)
	(nawierzchnia utwardzona)
    (nrOdcinka 4)
    (maxOdcinek 7)
)
(droga
    (id droga1)
	(idKratki 14) 
	(skadGrod grod1)
	(dokadGrod grod2)
	(platna true)
	(nawierzchnia utwardzona)
    (nrOdcinka 5)
    (maxOdcinek 7)
)
(droga
    (id droga1)
	(idKratki 15) 
	(skadGrod grod1)
	(dokadGrod grod2)
	(platna true)
	(nawierzchnia utwardzona)
    (nrOdcinka 6)
    (maxOdcinek 7)
)
(droga
    (id droga1)
	(idKratki 16) 
	(skadGrod grod1)
	(dokadGrod grod2)
	(platna true)
	(nawierzchnia utwardzona)
    (nrOdcinka 7)
    (maxOdcinek 7)
)

(iteracja 11)
(akcjaOdpoczywanie (idAgenta poslaniec1)(iteracjaKoniec 10))
;(blokada 
;    (id blokada1)
;    (idKratki 14)
;)
;(akcjaZobaczenieBlokady 
;    (idAgenta poslaniec1)
;    (idBlokady blokada1)
;    (podjetaAkcja ominiecie)
;)
(drzewo
	(rodzajDrzewa buk)
	(idKratki 19) ;id kratki, na ktorej sie znajduje - zajmuje tylko 1 kratke
	(stan niesciete) ;sciete czy niesciete
)
( woz
    ( id woz1)
    ( udzwig 120)
    ( cena 20)
    (idGrodu grod1)
)
( woz
    ( id woz2)
    ( udzwig 150)
    ( cena 50)
    ( idGrodu grod1)
)
( siekiera
    (id siekiera1)
    (typ tytanowa)
    (cena 20)   
    (zuzycie 90 )
    (idGrodu grod1)
) 
( siekiera
    (id siekiera2)
    (typ zlota)
    (cena 20)   
    (zuzycie 0 )
    (idGrodu grod1)
) 

;( akcjaZetnijDrzewo
;	(idAgenta drwal1) ;chodzi oczywi�cie o id drwala
;)
( akcjaKupWoz
    (idAgenta drwal1)
    (idWozu woz2)
)

;( akcjaKupSiekiere
;    (idAgenta drwal1)
;    (idSiekiery siekiera2)
;)
(drewno 
     (id "drewno1")
    ( waga 10)
    ( cena 10)
)
(drewno 
    (id "drewno2")
    ( waga 10)
    ( cena 10)
)
( akcjaSprzedajDrewno
    (idAgenta drwal1)
)
( kleska
	(id kleska1) 
    (czasTrwania 1)
	(idKratki 19) ;kratka na której jest klęska
	(niszczenieLasu 30) ;procent zniszczonych drzew na obszarze kleski
	(oslabianieAgentow 10) ;liczba punktow energii jaka zabiera znajdujacym sie na jej obszarze agentom
	(zabijanieMieszkancow 2) ;liczba mieszkancow, ktorych zabija, gdy w jej obszarze znajduje sie grod
)
(kleska
	(id kleska2) 
    (czasTrwania 1)
	(idKratki 19) ;kratka na której jest klęska
	(niszczenieLasu 30) ;procent zniszczonych drzew na obszarze kleski
	(oslabianieAgentow 10) ;liczba punktow energii jaka zabiera znajdujacym sie na jej obszarze agentom
	(zabijanieMieszkancow 2) ;liczba mieszkancow, ktorych zabija, gdy w jej obszarze znajduje sie grod
)
(paczka
    (id paczka3)
	(waga 1)
	(grodStart grod1) ;grod, w ktorym paczka sie znajduje
	(grodKoniec grod2) ;grod, do ktorego paczka jest przeznaczona
)
(akcjaWezPaczke (idAgenta poslaniec1)(idPaczki paczka3))
)



