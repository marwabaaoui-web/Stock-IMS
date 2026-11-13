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

Dossier `docs/captures/` : `1_interface.png`, `2_stock_calcule.png`, `3_message_validation.png`.
![img.png](img.png)

![img_3.png](img_3.png)

![img_2.png](img_2.png)

![img_5.png](img_5.png)

![img_4.png](img_4.png)

![img_6.png](img_6.png)

![img_7.png](img_7.png)

![img_8.png](img_8.png)
