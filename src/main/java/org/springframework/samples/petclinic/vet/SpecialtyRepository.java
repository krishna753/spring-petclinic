package org.springframework.samples.petclinic.vet;

import org.springframework.dao.DataAccessException;
import org.springframework.data.repository.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;

/**
 * Repository class for <code>Specialty</code> domain objects All method names are
 * compliant with Spring Data naming conventions so this interface can easily be extended
 * for Spring Data. See:
 * https://docs.spring.io/spring-data/jpa/docs/current/reference/html/#repositories.query-methods.query-creation
 *
 * @author Krishna Iyer
 */

public interface SpecialtyRepository extends Repository<Specialty, Integer> {

	/**
	 * Retrieve all <code>Specialty</code>s from the data store.
	 * @return a <code>Collection</code> of <code>Specialty</code>s
	 */
	@Transactional(readOnly = true)
	Collection<Specialty> findAll() throws DataAccessException;

	/**
	 * Retrieve a {@link Specialty} from the data store by id.
	 * @param id the id to search for
	 * @return the {@link Specialty} if found
	 */
	@Transactional(readOnly = true)
	Specialty findById(Integer id);

	/**
	 * Save a {@link Specialty} to the data store, either inserting or updating it.
	 * @param specialty the {@link Specialty} to save
	 */
	void save(Specialty specialty);

}
