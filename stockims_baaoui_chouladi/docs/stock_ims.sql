-- StockIMS : structure + données de test (MySQL 8)
-- Import : mysql -u root -p < docs/stock_ims.sql
CREATE DATABASE IF NOT EXISTS stock_ims CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE stock_ims;

DROP TABLE IF EXISTS sortie;
DROP TABLE IF EXISTS entree;
DROP TABLE IF EXISTS article;

CREATE TABLE article (
  id bigint NOT NULL AUTO_INCREMENT,
  designation varchar(80) NOT NULL,
  reference varchar(20) NOT NULL,
  PRIMARY KEY (id),
  UNIQUE KEY uk_article_reference (reference)
) ENGINE=InnoDB;

CREATE TABLE entree (
  id bigint NOT NULL AUTO_INCREMENT,
  date_mouvement date NOT NULL,
  quantite int NOT NULL,
  article_id bigint NOT NULL,
  PRIMARY KEY (id),
  CONSTRAINT fk_entree_article FOREIGN KEY (article_id) REFERENCES article (id)
) ENGINE=InnoDB;

CREATE TABLE sortie (
  id bigint NOT NULL AUTO_INCREMENT,
  date_mouvement date NOT NULL,
  quantite int NOT NULL,
  article_id bigint NOT NULL,
  PRIMARY KEY (id),
  CONSTRAINT fk_sortie_article FOREIGN KEY (article_id) REFERENCES article (id)
) ENGINE=InnoDB;

-- 3 articles de test
INSERT INTO article (id, reference, designation) VALUES
  (1, 'TIS-001', 'Rouleau de coton'),
  (2, 'STY-002', 'Stylo bleu'),
  (3, 'CAH-003', 'Cahier A4');

-- Scénario de l'énoncé sur l'article 1 : entrée 20, sortie 7 => stock 13
INSERT INTO entree (article_id, quantite, date_mouvement) VALUES (1, 20, CURDATE()), (2, 50, CURDATE());
INSERT INTO sortie (article_id, quantite, date_mouvement) VALUES (1, 7, CURDATE()), (2, 12, CURDATE());
-- Article 3 : aucun mouvement => stock 0

-- Vérification du stock (attendu : 13, 38, 0)
SELECT a.reference,
       COALESCE((SELECT SUM(quantite) FROM entree WHERE article_id = a.id), 0)
     - COALESCE((SELECT SUM(quantite) FROM sortie WHERE article_id = a.id), 0) AS stock
FROM article a;
