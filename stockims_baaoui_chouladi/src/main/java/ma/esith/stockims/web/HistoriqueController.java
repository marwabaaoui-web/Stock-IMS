package ma.esith.stockims.web;

import lombok.RequiredArgsConstructor;
import ma.esith.stockims.service.StockService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class HistoriqueController {

	private final StockService service;

	@GetMapping("/entrees")
	public String entrees(Model model) {
		model.addAttribute("mouvements", service.toutesLesEntrees());
		model.addAttribute("type", "entrees");
		model.addAttribute("titre", "Les entrées");
		return "mouvements/liste";
	}

	@GetMapping("/sorties")
	public String sorties(Model model) {
		model.addAttribute("mouvements", service.toutesLesSorties());
		model.addAttribute("type", "sorties");
		model.addAttribute("titre", "Les sorties");
		return "mouvements/liste";
	}
}
