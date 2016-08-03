package ch.lifeguardz.projects.pokeClean.controller;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import ch.lifeguardz.projects.pokeClean.entities.PokeClean;
import ch.lifeguardz.projects.pokeClean.entities.SortedPokemon;
import ch.lifeguardz.projects.pokeClean.services.LoginService;
import ch.lifeguardz.projects.pokeClean.services.PokemonService;

@Controller
public class PokemonController {
	
	@Resource
	private PokemonService pokemonService;
	
	@Resource
	private LoginService loginService;
	
	@RequestMapping(value = {"/app/pokemon", "/PokeClean/app/pokemon"}, method = RequestMethod.GET)
	public String pokemonList(Model model, HttpServletRequest request, RedirectAttributes redirectAttributes) {
		
		HttpSession session = request.getSession();
		if (session.getAttribute("PokeClean") == null) {
			return "redirect:/";
		}
		
		PokeClean pokeClean = (PokeClean) session.getAttribute("PokeClean");
		return "pogoclean/app/pokemon/list";
	}
	
	@RequestMapping(value = {"/app/pokemon/{id}", "/PokeClean/app/pokemon/{id}"}, method = RequestMethod.GET)
	public String pokemonTypeList(Model model, HttpServletRequest request, RedirectAttributes redirectAttributes, @PathVariable int id) {
		HttpSession session = request.getSession();
		
		if (session.getAttribute("PokeClean") == null) {
			return "redirect:/";
		}
		PokeClean pokeClean = (PokeClean) session.getAttribute("PokeClean");
		
		model.addAttribute("poke", pokemonService.getSortedPokemonById(id, pokeClean).getPokemon());
		model.addAttribute("sortedList", pokemonService.getSortedPokemonById(id, pokeClean));
		return "pogoclean/app/pokemon/pokemon";
	}
	
	@RequestMapping(value = {"/app/pokemon/{id}/{longId}/transfer", "/PokeClean/app/pokemon/{id}/{longId}/transfer"}, method = RequestMethod.GET)
	public String transferPokemon(Model model, HttpServletRequest request, RedirectAttributes redirectAttributes, @PathVariable Long longId, @PathVariable int id) {
		HttpSession session = request.getSession();
		
		if (session.getAttribute("PokeClean") == null) {
			return "redirect:/";
		}
		PokeClean pokeClean = (PokeClean) session.getAttribute("PokeClean");
		
		if(!pokemonService.tranferOnePokemonById(pokeClean.getInventories().getPokebank().getPokemonById(longId))) {
			redirectAttributes.addFlashAttribute("error", "Something went wrong.");
		} else {
			redirectAttributes.addFlashAttribute("success", "Pokemon transfered.");
		}
		pokeClean = loginService.refreshPokeClean(pokeClean);
		
		// verschiebe in service
		boolean hasPokemon = false;
		for (SortedPokemon sortedPokemon : pokeClean.getSortedPokemonList()) {
			if (sortedPokemon.getPokemon().getPokemonId().getNumber() == id){
				hasPokemon = true;
			}
		}
		
		session.setAttribute("PokeClean", pokeClean);
		if (hasPokemon) {
			return "redirect:/PokeClean/app/pokemon/" + id;
		}
		
		return "redirect:/PokeClean/app/pokemon";
	}

}
