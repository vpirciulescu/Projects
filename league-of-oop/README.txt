~ PÎRCIULESCU VALENTINA - 323 CD ~

main - Main
joc - Fight
    - Hero
    - Knight 
    - Pyromancer
    - Rogue
    - Wizard

  Main
- citire input
- jocul
- scriere output

  Fight
- citire input
  Desfasurarea jocului
  - mutarea eroilor (daca nu sunt afectati de incapacitate)
  - verific daca au dmg over time si il aplic
  - calculez damageul dat de fiecare erou doar daca acestia sunt in viata
  - actualizez hp
  - actualizez status (dead or alive)
  - actualizez level, hp si xp, daca este cazul (eroul a castigat)

  Hero 
- initiere erou
- metoda "unu" calculeaza damageul dat de prima abilitate
- metoda "doi" calculeaza damageul dat de a doua abilitate
- metoda "trei" calculeaza damageul dat de prima abilitate fara race modf
- metoda "patru" calculeaza damageul dat de a doua abilitate fara race modf

   Clasele Knight, Pyromancer, Rogue si Wizard contin implementarea celor 4 metode in functie
de abilitatile fiecarui erou.



