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

import org.springframework.cache.annotation.Cacheable;
import org.springframework.dao.DataAccessException;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.List;

public interface VetSpecialitiesRepository extends Repository<VetSpecialties, Integer> {

	/**
	 * Retrieve all <code>VetSpecialties</code>s from the data store.
	 * @return a <code>Collection</code> of <code>Vet</code>s
	 */
	@Transactional(readOnly = true)
	@Cacheable("vetSpecialties")
	Collection<VetSpecialties> findAll() throws DataAccessException;

	/**
	 * Retrieve an {@link VetSpecialties} from the data store by id.
	 * @param id the id to search for
	 * @return the {@link VetSpecialties} if found
	 */
	@Query("SELECT vetSpecialties FROM VetSpecialties vetSpecialties WHERE vetSpecialties.vet_id =:id")
	@Transactional(readOnly = true)
	List<VetSpecialties> findById(@Param("id") Integer id);

	/**
	 * Save an {@link VetSpecialties} to the data store, either inserting or updating it.
	 * @param vetSpecialties the {@link VetSpecialties} to save
	 */
	void save(VetSpecialties vetSpecialties);

}
