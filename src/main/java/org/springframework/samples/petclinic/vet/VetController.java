/*
 * Copyright 2012-2019 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.springframework.samples.petclinic.vet;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.petclinic.Cache.CachingController;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Collection;
import java.util.Map;
import java.util.Optional;

/**
 * @author Juergen Hoeller
 * @author Mark Fisher
 * @author Ken Krebs
 * @author Arjen Poutsma
 */
@Controller
class VetController {

	private static final String VIEWS_VETS_CREATE_OR_UPDATE_FORM = "vets/createOrUpdateVetForm";

	private final VetRepository vets;

	private final SpecialtyRepository specialtyRepository;

	@Autowired
	private CachingController cachingController;

	public VetController(VetRepository clinicService, SpecialtyRepository specialtyRepository) {
		this.vets = clinicService;
		this.specialtyRepository = specialtyRepository;
	}

	@ModelAttribute("specialties")
	public Collection<Specialty> populateSpecialties() {
		return this.vets.findSpecialties();
	}

	@GetMapping("/vets/new")
	public String initCreationForm(ModelMap model) {
		Vet vet = new Vet();
		model.put("vet", vet);
		return VIEWS_VETS_CREATE_OR_UPDATE_FORM;
	}

	@PostMapping("/vets/new")
	public String processCreationForm(Vet vet, String[] specialties, BindingResult result, ModelMap model) {
		if (result.hasErrors()) {
			model.put("vet", vet);
			return VIEWS_VETS_CREATE_OR_UPDATE_FORM;
		}
		else {
			Collection<Specialty> allSpecialties = specialtyRepository.findAll();
			for (String sp : specialties) {
				Optional<Specialty> optionalSpecialty = allSpecialties.stream()
						.filter(p -> p.getName().equalsIgnoreCase(sp)).findFirst();
				Specialty selectedSpecialty = null;
				if (optionalSpecialty.isPresent()) {
					selectedSpecialty = optionalSpecialty.get();
					selectedSpecialty.getVets().add(vet);
					vet.addSpecialty(selectedSpecialty);
				}
			}
			cachingController.clearVetsCache();
			this.vets.save(vet);
			return "redirect:/vets.html";
		}
	}

	@GetMapping("/vets.html")
	public String showVetList(Map<String, Object> model) {
		// Here we are returning an object of type 'Vets' rather than a collection of Vet
		// objects so it is simpler for Object-Xml mapping
		Vets vets = new Vets();
		vets.getVetList().addAll(this.vets.findAll());
		model.put("vets", vets);
		return "vets/vetList";
	}

	@GetMapping({ "/vets" })
	public @ResponseBody Vets showResourcesVetList() {
		// Here we are returning an object of type 'Vets' rather than a collection of Vet
		// objects so it is simpler for JSon/Object mapping
		Vets vets = new Vets();
		vets.getVetList().addAll(this.vets.findAll());
		return vets;
	}

}
