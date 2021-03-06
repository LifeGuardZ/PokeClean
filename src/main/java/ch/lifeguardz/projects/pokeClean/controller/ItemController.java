package ch.lifeguardz.projects.pokeClean.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import ch.lifeguardz.projects.pokeClean.entities.PokeClean;

@Controller
public class ItemController {

	@RequestMapping(value = {"/app/items", "/PokeClean/app/items"}, method = RequestMethod.GET)
	public String itemList(Model model, HttpServletRequest request, RedirectAttributes redirectAttributes) {
		HttpSession session = request.getSession();
		if (session.getAttribute("PokeClean") == null) {
			return "redirect:/";
		}
		
		PokeClean pokeClean = (PokeClean) session.getAttribute("PokeClean");
		return "pogoclean/app/items/list";
	}

}
