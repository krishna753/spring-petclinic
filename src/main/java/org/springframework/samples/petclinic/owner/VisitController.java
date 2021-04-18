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
package org.springframework.samples.petclinic.owner;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.samples.petclinic.vet.Vet;
import org.springframework.samples.petclinic.vet.VetRepository;
import org.springframework.samples.petclinic.visit.Visit;
import org.springframework.samples.petclinic.visit.VisitRepository;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author Juergen Hoeller
 * @author Ken Krebs
 * @author Arjen Poutsma
 * @author Michael Isvy
 * @author Dave Syer
 */
@Controller
class VisitController {

	private final VisitRepository visits;

	private final PetRepository pets;

	private final VetRepository vets;

	@Value("${application.pet.visit.timeslot.duration}")
	private Integer visitDuration;

	public VisitController(VisitRepository visits, PetRepository pets, VetRepository vets) {
		this.visits = visits;
		this.pets = pets;
		this.vets = vets;
	}

	@ModelAttribute("vets")
	public Collection<Vet> populateVets() {
//		return TimeSlots.getList();
		return this.vets.findAll();
	}

	@ModelAttribute("timeSlots")
	public String[] populateTimeSlots() {
//		return TimeSlots.getList();
		return getTimeSet(this.visitDuration);
	}

	private String[] getTimeSet( int visitDuration) {

		Calendar cal = new GregorianCalendar();
// reset hour, minutes, seconds and millis
		cal.set(Calendar.HOUR_OF_DAY, 8);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);

		SimpleDateFormat sdf = new SimpleDateFormat("hh:mm a");
		int slots = 540/visitDuration;
		String[] results = new String[slots];
		int index=0;
		while (index < slots) {
			String startTime = sdf.format(cal.getTime());
			cal.add(Calendar.MINUTE, visitDuration);
			String endTime = sdf.format(cal.getTime());

			results[index++] =startTime+"-"+endTime;
		}
		return results;
}

	@InitBinder
	public void setAllowedFields(WebDataBinder dataBinder) {
		dataBinder.setDisallowedFields("id");
	}

	/**
	 * Called before each and every @RequestMapping annotated method. 2 goals: - Make sure
	 * we always have fresh data - Since we do not use the session scope, make sure that
	 * Pet object always has an id (Even though id is not part of the form fields)
	 * @param petId
	 * @return Pet
	 */

	@ModelAttribute("visit")
	public Visit loadPetWithPreviousVisit(@PathVariable("petId") int petId, Map<String, Object> model) {
		Pet pet = this.pets.findById(petId);
		List<Visit> visits = this.visits.findByPetId(petId).stream().filter(v -> LocalDate.now().isAfter(v.getDate()))
				.collect(Collectors.toList());
		pet.setVisitsInternal(visits);
		model.put("pet", pet);
		Visit visit = new Visit();
		pet.addVisit(visit);
		return visit;
	}

	@ModelAttribute("upcomingVisit")
	public Visit loadPetWithUpcomingVisit(@PathVariable("petId") int petId, Map<String, Object> model) {
		Pet pet = this.pets.findById(petId);
		List<Visit> visits = this.visits.findByPetId(petId).stream()
			.filter(v -> LocalDate.now()
				.isBefore(v.getDate()) || LocalDate.now().isEqual(v.getDate()))
				.collect(Collectors.toList());
		pet.setVisitsInternal(visits);
		model.put("ipet", pet);
		Visit visit = new Visit();
		pet.addVisit(visit);
		return visit;
	}

	// Spring MVC calls method loadPetWithVisit(...) before initNewVisitForm is called
	@GetMapping("/owners/*/pets/{petId}/visits/new")
	public String initNewVisitForm(@PathVariable("petId") int petId, Map<String, Object> model) {
		return "pets/createOrUpdateVisitForm";
	}

	// Spring MVC calls method loadPetWithVisit(...) before processNewVisitForm is called
	@PostMapping("/owners/{ownerId}/pets/{petId}/visits/new")
	public String processNewVisitForm(@Valid Visit visit, String vet, BindingResult result) {
		if (result.hasErrors()) {
			return "pets/createOrUpdateVisitForm";
		}
		else {
			String[] timeSlots = visit.getStartTime().split("-");
			visit.setStartTime(timeSlots[0]);
			visit.setEndTime(timeSlots[1]);
			this.visits.save(visit);
			return "redirect:/owners/{ownerId}";
		}
	}

	@PostMapping("/owners/{ownerId}/pets/{petId}/visits{visitId}/delete")
	public String processDeleteVisitForm(@Valid Visit visit, BindingResult result) {
		if (result.hasErrors()) {
			return "pets/createOrUpdateVisitForm";
		}
		else {
			this.visits.deleteVisitById(visit.getId());
			return "redirect:/owners/{ownerId}";
		}
	}

}
