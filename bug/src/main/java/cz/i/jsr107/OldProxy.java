package cz.i.jsr107;

import cz.i.svc.CachedService;

public class OldProxy {

  private CachedService service;

  public OldProxy() {
    this.service = CachedService.getInstance();
  }

  public CachedService getService() {
    return service;
  }
}
