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

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataIntegrityViolationException;
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
import java.util.Calendar;
import java.util.Collection;
import java.util.GregorianCalendar;
import java.util.Map;

/**
 * @author Juergen Hoeller
 * @author Ken Krebs
 * @author Arjen Poutsma
 * @author Michael Isvy
 * @author Dave Syer
 */
@Controller
@Slf4j
class VisitController {

	private final VisitRepository visits;

	private final PetRepository pets;

	private final VetRepository vets;

	// TODO: Should be configurable via config properties
	private static final Integer MINUTES_BETWEEN_8AM_5PM = 9 * 60;

	@Value("${application.pet.visit.timeslot.duration}")
	private Integer visitDuration=30;

	private static final String VISIT_SAVE_EXCEPTION = "Exception occured during saving visit. Please try again!";

	private static final String SLOT_ALREADY_BOOKED = "This slot has already been booked by someone! Please choose a different slot.";

	private static final String CREATE_OR_UPDATE_VISIT_FORM_PATH = "pets/createOrUpdateVisitForm";

	private static final String REDIRECT_OWNER_DETAIL_PAGE = "redirect:/owners/{ownerId}";

	public VisitController(VisitRepository visits, PetRepository pets, VetRepository vets) {
		this.visits = visits;
		this.pets = pets;
		this.vets = vets;
	}

	@ModelAttribute("vets")
	public Collection<Vet> populateVets() {
		return this.vets.findAll();
	}

	@ModelAttribute("timeSlots")
	public String[] populateTimeSlots() {
		return getTimeSet(this.visitDuration);
	}

	@ModelAttribute("pageErrors")
	public String populatePageErrors() {
		return "";
	}

	// Calculate time windows/slots to populate in dropdown
	// TODO: Filter out the timeslots that a Vet is already booked for.
	private String[] getTimeSet(int visitDuration) {

		Calendar cal = new GregorianCalendar();
		cal.set(Calendar.HOUR_OF_DAY, 8);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);

		SimpleDateFormat sdf = new SimpleDateFormat("hh:mm a");
		int slots = MINUTES_BETWEEN_8AM_5PM / visitDuration;
		String[] results = new String[slots];
		int index = 0;
		while (index < slots) {
			String startTime = sdf.format(cal.getTime());
			cal.add(Calendar.MINUTE, visitDuration);
			String endTime = sdf.format(cal.getTime());
			results[index++] = startTime + "-" + endTime;
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
	public Visit loadPetWithVisit(@PathVariable("petId") int petId, Map<String, Object> model) {
		Pet pet = this.pets.findById(petId);
		pet.setVisitsInternal(this.visits.findByPetId(petId));
		model.put("pet", pet);
		Visit visit = new Visit();
		pet.addVisit(visit);
		return visit;
	}

	// Spring MVC calls method loadPetWithVisit(...) before initNewVisitForm is called
	@GetMapping("/owners/*/pets/{petId}/visits/new")
	public String initNewVisitForm(@PathVariable("petId") int petId, Map<String, Object> model) {
		return CREATE_OR_UPDATE_VISIT_FORM_PATH;
	}

	// Spring MVC calls method loadPetWithVisit(...) before processNewVisitForm is called
	// Use the selected timeSlot from drop down and persist it as start,end time for
	// Visits
	// If unable to save due to Unique constraint violation, throw a Frontend error.
	// TODO: A filtered time slot drop down will avoid this exception.
	@PostMapping("/owners/{ownerId}/pets/{petId}/visits/new")
	public String processNewVisitForm(@Valid Visit visit, BindingResult result, Map<String, Object> model) {
		if (result.hasErrors()) {
			return CREATE_OR_UPDATE_VISIT_FORM_PATH;
		}
		else {
			String[] timeSlots = visit.getStartTime().split("-");
			visit.setStartTime(timeSlots[0]);
			visit.setEndTime(timeSlots[1]);
			try {
				this.visits.save(visit);
			}
			catch (DataIntegrityViolationException ex) {
				model.put("pageErrors", SLOT_ALREADY_BOOKED);
				return CREATE_OR_UPDATE_VISIT_FORM_PATH;
			}
			return REDIRECT_OWNER_DETAIL_PAGE;
		}
	}

	// Delete visit
	@GetMapping("/owners/{ownerId}/pets/{petId}/visits/delete/{visitId}")
	public String processDeleteVisitForm(@PathVariable("visitId") int visitId, Map<String, Object> model) {
		try {
			this.visits.deleteVisitById(visitId);
		}
		catch (Exception ex) {
			log.error(VISIT_SAVE_EXCEPTION);
			model.put("pageErrors", VISIT_SAVE_EXCEPTION);
			return CREATE_OR_UPDATE_VISIT_FORM_PATH;
		}
		return REDIRECT_OWNER_DETAIL_PAGE;
	}

}
