package cz.i.svc;

import cz.i.cache.CustomKeyGenerator;

import javax.cache.annotation.CacheResult;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.spi.CDI;
import javax.inject.Named;

@Named
@ApplicationScoped
public class CachedService {

  public static CachedService getInstance() {
    return CDI.current().select(CachedService.class).get();
  }

  @CacheResult(cacheName = "codebooks", cacheKeyGenerator = CustomKeyGenerator.class)
  public Long getCachedValue() {
    return 23L;
  }
}
