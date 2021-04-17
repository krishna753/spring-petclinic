package org.springframework.samples.petclinic.vet;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.dao.DataAccessException;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;

public interface SpecialitiesRepository extends Repository<Specialties, Integer> {

	/**
	 * Save an {@link Specialties} to the data store, either inserting or updating it.
	 * @param specialty the {@link Specialties} to save
	 */
	void save(Specialties specialty);

	/**
	 * Retrieve all <code>Specialties</code>s from the data store.
	 * @return a <code>Collection</code> of <code>Specialties</code>s
	 */
	@Transactional(readOnly = true)
	@Cacheable("specialties")
	Collection<Specialties> findAll() throws DataAccessException;

	/**
	 * Retrieve an {@link Specialties} from the data store by id.
	 * @param id the id to search for
	 * @return the {@link Specialties} if found
	 */
	@Query("SELECT specialties FROM Specialties specialties WHERE specialties.id =:id")
	@Transactional(readOnly = true)
	Specialties findById(@Param("id") Integer id);

}
