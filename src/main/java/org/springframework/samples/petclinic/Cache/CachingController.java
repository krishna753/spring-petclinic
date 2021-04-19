package org.springframework.samples.petclinic.Cache;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * CachingController calls CachingService methods to evict and manage Spring caches It
 * provies a rest endpoint to clear caches external from the application
 *
 * @author Krishna Iyer
 * @version 1.0
 * @since 2021-04-18
 */

@RestController
public class CachingController {

	@Autowired
	CachingService cachingService;

	// Clears all caches
	@GetMapping("clearAllCaches")
	public void clearAllCaches() {
		cachingService.evictAllCaches();
	}

	// Clears the vets cache when a new vet is added.
	@GetMapping("clearVetsCache")
	public void clearVetsCache() {
		cachingService.evictAllCacheValues("vets");
	}

}
