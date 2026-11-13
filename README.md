# StockIMS — Gestion de stock avec Spring Boot

**Participants :** Marwa BAAOUI · Ghita CHOULADI  

Application web de gestion de stock : articles, entrées, sorties, stock calculé.
Java 21 · Spring Boot · Spring Data JPA · MySQL · Thymeleaf · Lombok · Bootstrap 5.

## Lancement

1. Installer **JDK 21** et **MySQL 8** (service démarré).
2. Créer la base et l'utilisateur (ou importer `docs/stock_ims.sql`, qui crée aussi les données de test) :
   ```sql
   CREATE DATABASE stock_ims CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
   CREATE USER 'stock_app'@'localhost' IDENTIFIED BY 'VotreMotDePasse';
   GRANT ALL PRIVILEGES ON stock_ims.* TO 'stock_app'@'localhost';
   ```
3. Définir le mot de passe dans la variable d'environnement `STOCKIMS_DB_PASSWORD`
   (IntelliJ : *Run > Edit Configurations > Environment variables* ; terminal : `$env:STOCKIMS_DB_PASSWORD="..."`).
4. Lancer : `./mvnw spring-boot:run` (Windows : `mvnw.cmd spring-boot:run`) ou exécuter `StockimsApplication`.
5. Ouvrir http://localhost:8080

## Paramètres MySQL à adapter (`src/main/resources/application.properties`)

| Paramètre | Valeur par défaut |
|---|---|
| `spring.datasource.url` | `jdbc:mysql://localhost:3306/stock_ims` |
| `spring.datasource.username` | `stock_app` |
| `spring.datasource.password` | `${STOCKIMS_DB_PASSWORD}` (variable d'environnement) |

Les tables `article`, `entree`, `sortie` sont créées automatiquement (`ddl-auto=update`).

## Architecture (conforme au guide)

- `model` : `Article`, `MouvementStock` (classe abstraite, `quantiteSignee()`), `Entree` (+), `Sortie` (−)
- `repository` : `ArticleRepository` (verrou), `EntreeRepository`, `SortieRepository`
- `service` : `ArticleService`, `StockService` (calcul du stock, règles métier, transactions)
- `web` : `ArticleController`, `StockController`, `ArticleForm`, `MouvementForm`

**Stock = somme des entrées − somme des sorties**, calculé à partir des mouvements (jamais stocké).
Une sortie supérieure au stock est refusée par `StockService.sortir` (message affiché sur la fiche).
Toutes les validations sont côté serveur (`@NotBlank`, `@NotNull`, `@Min(1)`, contrainte `UNIQUE` sur la référence) et fonctionnent sans JavaScript.

## Personnalisation de l'interface

- **HTML / Thymeleaf** : gabarit commun `fragments/layout.html` (en-tête, barre de navigation, alertes, pied de page, scripts) ; pages Accueil, Liste des articles, Formulaire, Fiche de stock avec historique.
- **CSS** (`static/css/stockims.css` + Bootstrap 5 en local) : palette bleu nuit / turquoise, barre de navigation dégradée, cartes, tableaux, badges de stock (vert / orange / rouge), boutons et messages personnalisés, affichage adaptatif (menu replié et boutons pleine largeur sur petit écran).
- **JavaScript** (`static/js/stockims.js`) : filtre de recherche en direct dans la liste des articles, confirmation avant une sortie, disparition automatique des messages de succès.

## Vérification (scénario de l'énoncé)

Stock initial 0 → entrée de 20 → sortie de 7 → **stock 13** · sortie de 15 **refusée**, stock inchangé (13) ·
quantité 0, négative, vide ou non entière refusée · données conservées après redémarrage (MySQL).

## Captures d'écran

Dossier `docs/captures/` : 

<img width="1903" height="971" alt="image" src="https://github.com/user-attachments/assets/0dbe1bf2-79da-4377-9e96-a1fe563f250e" />

<img width="1912" height="970" alt="image" src="https://github.com/user-attachments/assets/da94d9c4-5972-4468-b7d9-61cd591fad14" />

<img width="1907" height="860" alt="image" src="https://github.com/user-attachments/assets/463f6b44-9065-42cc-8bb2-d513b445c994" />

<img width="1757" height="732" alt="image" src="https://github.com/user-attachments/assets/7d0bace4-0556-4bda-af84-906f29349f9c" />

<img width="1851" height="968" alt="image" src="https://github.com/user-attachments/assets/a58edbb0-6e14-40dc-a1ac-070ff5c5f0db" />

<img width="1848" height="776" alt="image" src="https://github.com/user-attachments/assets/a64e32e5-ed00-4efa-8126-749d94494a88" />

<img width="1810" height="972" alt="image" src="https://github.com/user-attachments/assets/71a09816-2fa9-4200-ae1c-24f4b10b7864" />
