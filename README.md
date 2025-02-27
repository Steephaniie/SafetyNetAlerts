# SafetyNet Alerts

![img.png](img.png)

## Description
SafetyNet Alerts est une application Sprint Boot permettant d'envoyer des informations aux services de secours en fonction de diverses requêtes sur les personnes et les adresses couvertes par les casernes de pompiers.

## Fonctionnalités
- Respect des principes SOLID et architecture MVC
- CRUD complet sur les personnes, les casernes et les dossiers médicaux.
- Logs détaillés avec différents niveaux (INFO, DEBUG, ERROR)
- Tests unitaires et couverture de code (JaCoCo et Surefire)

## Technologies utilisées
- Java 21
- Spring Boot
- Maven
- Git
- IntelliJ
- JUnit 5 pour les tests
- JaCoCo pour la couverture de code
- Surefire pour l'exécution des tests
- Jackson pour le parsing JSON
- Log4j pour la gestion des logs

## Installation et exécution

### Étapes
1. **Cloner le repository** :
   ```sh
   git clone https://github.com/Steephaniie/safetynet-alerts.git
   cd safetynet-alerts
2. Construire le projet : mvn clean Install
3. Exécuter l'application : mvn spring-boot:run
4. L'application sera disponible à l'adresse : http://localhost:8080
5. Endpoints principaux : méthode HTTP : GET, POST, PUT, DELETE
6. Taux de réussite aux tests : 100 %
7. Couverture globale de test : 94 %

### Documentation
- La documentation de l'API est générée avec Swagger : http://localhost:8080/swagger-ui/index.html#/

### Tests unitaires
- Exécuter les tests : mvn verify
- le rapport de tests JaCoCo est généré dans le répertoire : SafetyNetAlerts/target/site/jacoco/index.html
- le rapport de couverture de code Surefire est généré dans le répertoire : SafetyNetAlerts/target/reports/surefire.html

### Tests via Postman
- Les requêtes API ont été testées via Postman.
- Sélectionner GET, POST, PUT ou DELETE selon l’action souhaitée.


## Données principales
- firestation : Assignations d'adresses aux casernes de pompiers
- person : Personne connue du système, avec son adresse et ses contacts
- medicalRecord : Dossier médical contenant la date de naissance, les médicaments et les allergies connues

Ces données peuvent être consultées via la méthode GET et modifiées via les méthodes POST, PUT et DELETE correspondantes.

## Endpoints de recherche
- /firestation?stationNumber=<station_number>
  → Liste des personnes couvertes par une caserne de pompiers et décompte des adultes et enfants.

- /childAlert?address=<address> 
  → Indication de présence et informations sur les enfants à une adresse donnée.

- /phoneAlert?firestation=<stationNumber>
  → Annuaire des numéros de téléphone des personnes couvertes par une caserne de pompiers.

- /fire?address=<address>
  → Informations détaillées sur un foyer.

- /flood/stations?stations=<stationNumbers>
  → Liste des foyers et des habitants couverts par une caserne.

- /personInfo?lastName=<lastName>
  → Informations détaillées des personnes portant un nom de famille donné.

- /communityEmail?city=<city>
  → Liste des emails de tous les habitants d'une ville.

## Contribuer
- Forkez le repo
- Créez une branche (feature/amélioration)
- Committez vos modifications (git commit -m "Ajout de fonctionnalité")
- Pushez (git push origin feature/amélioration)
- Faites une Pull Request

© 2025 SafetyNet Alerts. Tous droits réservés.