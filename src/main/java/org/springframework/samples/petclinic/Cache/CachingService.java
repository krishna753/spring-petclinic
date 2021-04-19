package org.springframework.samples.petclinic.Cache;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Component;

/**
 * CachingService provides methods to evict and manage Spring caches
 *
 * @author Krishna Iyer
 * @version 1.0
 * @since 2021-04-18
 */

@Component
public class CachingService {

	@Autowired
	CacheManager cacheManager;

	// Clear cache based on parameter
	public void evictAllCacheValues(String cacheName) {
		cacheManager.getCache(cacheName).clear();
	}

	// Clear all caches
	public void evictAllCaches() {
		cacheManager.getCacheNames().parallelStream().forEach(cacheName -> cacheManager.getCache(cacheName).clear());
	}

}
