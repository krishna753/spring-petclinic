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

import org.springframework.samples.petclinic.model.NamedEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotEmpty;
import java.io.Serializable;

/**
 * Models a {@link Vet Vet's} specialty (for example, dentistry).
 *
 * @author Juergen Hoeller
 */
@Entity
@Table(name = "vet_specialties")
public class VetSpecialties implements Serializable {

	@Id
	@Column(name = "vet_id")
	@NotEmpty
	int vet_id;

	@Column(name = "specialty_id")
	@NotEmpty
	int specialty_id;

	public int getVet_id() {
		return vet_id;
	}

	public void setVet_id(int vet_id) {
		this.vet_id = vet_id;
	}

	public int getSpecialty_id() {
		return specialty_id;
	}

	public void setSpecialty_id(int specialty_id) {
		this.specialty_id = specialty_id;
	}

}
