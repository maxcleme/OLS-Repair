La réparation automatique de bugs est un domaine en pleine expansion. En effet, le nombre
de programme évolue de plus en plus chaques jours et par corrélation le nombre de bugs
également. Il est envisageable dans les prochaines années que les développeurs ne soit pas assez
nombreux pour résoudre tous ces bugs. C’est ici qu'intervient la réparation automatique de
bugs, consistant à utiliser des programmes pour réparer d’autres programmes afin de remplacer
ou guider ces développeurs.
Des approches déjà existantes ont déjà fait leurs preuves. Elles permettent effectivement
de réparer des bugs, mais leurs comportement n'est pas d´eterministe. Elles appliquent
des transformations sur le code jusqu'à faire passer les tests. Est-il possible de trouver un
comportement déterministe dans le but de réparer des bugs ?
L'objectif de ce rapport est de montrer qu'un tel comportement est possible.
La solution proposée ici est appelé OLS-Repair, elle est grandement inspirée par un outils
appelé Nopol. Cette approche utilise les entrées / sorties des tests pour définir des contraintes,
et essaye de les résoudre à l'aide de solver SMT.
