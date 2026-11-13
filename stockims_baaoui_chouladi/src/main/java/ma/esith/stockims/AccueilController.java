package ma.esith.stockims;

import lombok.RequiredArgsConstructor;
import ma.esith.stockims.service.StockService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class AccueilController {

	private final StockService stockService;

	@GetMapping("/")
	public String accueil(Model model) {
		var message = new MessageAccueil("Bienvenue dans StockIMS");
		model.addAttribute("message", message.getTexte());
		model.addAttribute("resume", stockService.resume());
		return "accueil";
	}
}
