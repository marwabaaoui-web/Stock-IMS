package ma.esith.stockims.service;

import ma.esith.stockims.model.*;
import ma.esith.stockims.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDate;
import java.util.*;
import org.springframework.data.domain.Sort;

@Service
@RequiredArgsConstructor
public class StockService {

	private final ArticleRepository articles;
	private final EntreeRepository entrees;
	private final SortieRepository sorties;

	/** Stock = somme des entrées - somme des sorties (polymorphisme via quantiteSignee()). */
	@Transactional(readOnly = true)
	public long calculerStock(Long articleId) {
		List<MouvementStock> mouvements = new ArrayList<>();
		mouvements.addAll(entrees.findByArticle_Id(articleId));
		mouvements.addAll(sorties.findByArticle_Id(articleId));
		long stock = 0;
		for (MouvementStock m : mouvements) {
			stock += m.quantiteSignee();
		}
		return stock;
	}

	public Optional<Article> chercher(Long id) {
		return articles.findById(id);
	}

	private Article verrouiller(Long id) {
		return articles.verrouiller(id).orElseThrow(
				() -> new IllegalArgumentException("Article introuvable."));
	}

	@Transactional
	public void entrer(Long id, int quantite) {
		Article article = verrouiller(id);
		entrees.save(new Entree(article, quantite, LocalDate.now()));
	}

	@Transactional
	public void sortir(Long id, int quantite) {
		Article article = verrouiller(id);
		long stock = calculerStock(id);
		if (quantite > stock) {
			throw new IllegalArgumentException(
					"Stock insuffisant : " + stock + " disponible(s), la sortie de " + quantite + " est refusée.");
		}
		sorties.save(new Sortie(article, quantite, LocalDate.now()));
	}

	/** Mouvements d'un article, du plus récent au plus ancien. */
	@Transactional(readOnly = true)
	public List<MouvementStock> historique(Long id) {
		List<MouvementStock> liste = new ArrayList<>();
		liste.addAll(entrees.findByArticle_Id(id));
		liste.addAll(sorties.findByArticle_Id(id));
		liste.sort(Comparator.comparing(MouvementStock::getDateMouvement).reversed());
		return liste;
	}

	/** Stock de chaque article (id -> stock) pour la liste. */
	@Transactional(readOnly = true)
	public Map<Long, Long> stocks() {
		Map<Long, Long> map = new HashMap<>();
		for (Article a : articles.findAll()) {
			map.put(a.getId(), calculerStock(a.getId()));
		}
		return map;
	}

	/** Compteurs de la page d'accueil. */
	@Transactional(readOnly = true)
	public Map<String, Long> resume() {
		return Map.of("articles", articles.count(), "entrees", entrees.count(), "sorties", sorties.count());
	}

	private static final Sort RECENT_D_ABORD = Sort.by(Sort.Direction.DESC, "dateMouvement", "id");

	/** Toutes les entrées, de la plus récente à la plus ancienne. */
	@Transactional(readOnly = true)
	public List<Entree> toutesLesEntrees() {
		return entrees.findAll(RECENT_D_ABORD);
	}

	/** Toutes les sorties, de la plus récente à la plus ancienne. */
	@Transactional(readOnly = true)
	public List<Sortie> toutesLesSorties() {
		return sorties.findAll(RECENT_D_ABORD);
	}
}
