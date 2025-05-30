# Plateforme de Gestion Universitaire – Microservices UGB

Ce projet est une plateforme modulaire basée sur une architecture microservices pour la gestion des étudiants, enseignants, utilisateurs, notes, cursus et reporting. Il utilise **JHipster**, **OAuth2**, **Consul**, **PostgreSQL** et **Docker Compose**.

---

## 1. Technologies utilisées

| Composant              | Technologie             |
| ---------------------- | ----------------------- |
| Générateur de projet   | JHipster                |
| Langage backend        | Java 17 (Spring Boot)   |
| Authentification       | OAuth2                  |
| Découverte de services | Consul                  |
| API Gateway            | Spring Cloud Gateway    |
| Base de données        | PostgreSQL              |
| Conteneurisation       | Docker + Docker Compose |
| Moteur de recherche    | Elasticsearch           |

---

## 2. Pré-requis système

Avant de démarrer, installez les éléments suivants :

* Java JDK 17+
* Node.js (v18 recommandé)
* npm
* Maven
* Docker et Docker Compose
* Git

---

## 3. Installation & Démarrage

### 3.1. Cloner le projet

```bash
git clone https://github.com/laminethiam02/projet-fin-module-archi.git
cd projet-fin-module-archi
```

### 3.2. Démarrer avec Docker Compose

Assurez-vous que Docker est lancé, puis exécutez :

```bash
docker-compose up -d
```

Cela lancera :

* L'API Gateway (`gateway`)
* Tous les microservices :

  * `userService`
  * `studentService`
  * `teacherService`
  * `curriculumService`
  * `noteService`
  * `reportingService`
* Les bases de données PostgreSQL
* Le serveur Consul
* Elasticsearch

### 3.3. Construire les services localement (optionnel)

```bash
cd userService
./mvnw
```


---

## 4. Accès à l’application

| Application       | URL                                            | Port |
| ----------------- | ---------------------------------------------- | ---- |
| API Gateway       | [http://localhost:8080](http://localhost:8080) | 8080 |
| Consul Server     | [http://127.0.0.1:8500](http://127.0.0.1:8500) | 8500 |
| userService       | [http://localhost:8081](http://localhost:8081) | 8081 |
| studentService    | [http://localhost:8082](http://localhost:8082) | 8082 |
| teacherService    | [http://localhost:8083](http://localhost:8083) | 8083 |
| curriculumService | [http://localhost:8084](http://localhost:8084) | 8084 |
| noteService       | [http://localhost:8085](http://localhost:8085) | 8085 |
| reportingService  | [http://localhost:8086](http://localhost:8086) | 8086 |

L'accès se fait via **OAuth2**. Vous pouvez configurer **Keycloak** ou utiliser une version intégrée.

---

## 5. Comptes de test

| Rôle        | Login | Mot de passe |
| ----------- | ----- | ------------ |
| Admin       | admin | admin        |
| Utilisateur | user  | user         |

Les comptes sont à configurer dans **Keycloak** (ou dans une base H2 si utilisée en développement).

---

## 6. Bases de données exportées

Les schémas PostgreSQL sont automatiquement créés à partir des entités JHipster. Chaque microservice utilise une base PostgreSQL dédiée :

| Microservice      | Base de données   |
| ----------------- | ----------------- |
| userService       | userService       |
| studentService    | studentService    |
| teacherService    | teacherService    |
| curriculumService | curriculumService |
| noteService       | noteService       |
| reportingService  | reportingService  |

Les exports de chaque base sont inclus dans le fichier `.zip`.

---

## 7. Structure des entités

### userService

* Compte (login, motDePasse, actif)
* Profil (email, téléphone, compteId)

### studentService

* DossierEtudiant (matricule, profilId)
* Bulletin (moyenne, dossierId)
* ReleveNote (noteGlobale, dossierId)

### teacherService

* Contrat (typeContrat, dateDebut, profilId)
* Classe (nomClasse)
* ChargeHoraire (contratId, classeId)

### curriculumService

* Programme
* Niveau
* UE
* MatiereProgramme

### noteService

* Examen
* Bareme
* Resultat

### reportingService

* RapportStatistique
* Indicateur
* AccesRapport

---

## 8. Recherche & Pagination

* Toutes les entités **sauf `Resultat` et `AccesRapport`** ont la pagination activée.
* Toutes les entités sont exposées avec **MapStruct (DTO)** et peuvent être recherchées via **Elasticsearch**.

---

## 9. Déploiement

Pour déployer tous les services en production via Docker :

```bash
docker-compose -f docker-compose-prod.yml up -d
```
