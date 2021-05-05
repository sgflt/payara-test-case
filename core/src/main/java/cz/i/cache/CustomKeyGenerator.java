package cz.i.cache;

import javax.cache.annotation.CacheKeyGenerator;
import javax.cache.annotation.CacheKeyInvocationContext;
import javax.cache.annotation.GeneratedCacheKey;
import java.lang.annotation.Annotation;

public class CustomKeyGenerator implements CacheKeyGenerator {
  @Override
  public GeneratedCacheKey generateCacheKey(final CacheKeyInvocationContext<? extends Annotation> cacheKeyInvocationContext) {
    return new CustomGeneratedCacheKey();
  }

  private static class CustomGeneratedCacheKey implements GeneratedCacheKey {
    @Override
    public int hashCode() {
      return 23;
    }

    @Override
    public boolean equals(final Object obj) {
      return obj instanceof Long && obj.equals(23L);
    }
  }
}
