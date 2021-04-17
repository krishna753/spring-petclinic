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

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * @author Juergen Hoeller
 * @author Mark Fisher
 * @author Ken Krebs
 * @author Arjen Poutsma
 */
@Controller
class VetController {

	private static final String VIEWS_VET_CREATE_OR_UPDATE_FORM = "vets/createOrUpdateVetForm";

	private final VetRepository vets;

	private final VetSpecialitiesRepository vetSpecialities;

	public VetController(VetRepository clinicService, VetSpecialitiesRepository vetSpecialities) {
		this.vets = clinicService;
		this.vetSpecialities = vetSpecialities;
	}

	@ModelAttribute("specialties")
	public Collection<Specialties> populateSpecialtyTypes() {
		return this.vets.findSpecialtyTypes();
	}

	@InitBinder
	public void setAllowedFields(WebDataBinder dataBinder) {
		dataBinder.setDisallowedFields("id");
	}

	@GetMapping("/vets/new")
	public String initCreationForm(ModelMap model) {
		Vet vet = new Vet();
		model.put("vet", vet);
		return VIEWS_VET_CREATE_OR_UPDATE_FORM;
	}

	@PostMapping("/vets/new")
	public String processCreationForm(@Valid Vet vet, String specialties, BindingResult result, ModelMap model) {
		if (result.hasErrors()) {
			model.put("vet", vet);
			return VIEWS_VET_CREATE_OR_UPDATE_FORM;
		}
		else {
			this.vets.save(vet);
			VetSpecialties vetSpecialties = new VetSpecialties();
			List<Specialties> specialtyList = (List<Specialties>) model.getAttribute("specialties");
			for (Specialties specialty : specialtyList) {
				if (specialty.getName().equalsIgnoreCase(specialties)) {
					vetSpecialties.setVet_id(vet.getId());
					vetSpecialties.setSpecialty_id(specialty.getId());
					break;
				}
			}
			this.vetSpecialities.save(vetSpecialties);
			return "redirect:/vets/" + vet.getId();
		}
	}

	/**
	 * Custom handler for displaying an vet.
	 * @param vetId the ID of the vet to display
	 * @return a ModelMap with the model attributes for the view
	 */
	@GetMapping("/vet/{vetId}")
	public ModelAndView showOwner(@PathVariable("vetId") int vetId) {
		ModelAndView mav = new ModelAndView("vet/vetDetails");
		Vet vet = this.vets.findById(vetId);
		mav.addObject(vet);
		return mav;
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
