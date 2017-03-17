package com.boco.workflow.webservice.remote;

public class ResourceCheckServiceImplProxy implements ResourceCheckServiceImpl {
  private String _endpoint = null;
  private ResourceCheckServiceImpl resourceCheckServiceImpl = null;
  
  public ResourceCheckServiceImplProxy() {
    _initResourceCheckServiceImplProxy();
  }
  
  public ResourceCheckServiceImplProxy(String endpoint) {
    _endpoint = endpoint;
    _initResourceCheckServiceImplProxy();
  }
  
  private void _initResourceCheckServiceImplProxy() {
    try {
      resourceCheckServiceImpl = (new ResourceCheckServiceImplServiceLocator()).getResourceCheckServiceImplPort();
      if (resourceCheckServiceImpl != null) {
        if (_endpoint != null)
          ((javax.xml.rpc.Stub)resourceCheckServiceImpl)._setProperty("javax.xml.rpc.service.endpoint.address", _endpoint);
        else
          _endpoint = (String)((javax.xml.rpc.Stub)resourceCheckServiceImpl)._getProperty("javax.xml.rpc.service.endpoint.address");
      }
      
    }
    catch (javax.xml.rpc.ServiceException serviceException) {}
  }
  
  public String getEndpoint() {
    return _endpoint;
  }
  
  public void setEndpoint(String endpoint) {
    _endpoint = endpoint;
    if (resourceCheckServiceImpl != null)
      ((javax.xml.rpc.Stub)resourceCheckServiceImpl)._setProperty("javax.xml.rpc.service.endpoint.address", _endpoint);
    
  }
  
  public ResourceCheckServiceImpl getResourceCheckServiceImpl() {
    if (resourceCheckServiceImpl == null)
      _initResourceCheckServiceImplProxy();
    return resourceCheckServiceImpl;
  }
  
  public java.lang.String resourceCheck(java.lang.String arg0) throws java.rmi.RemoteException{
    if (resourceCheckServiceImpl == null)
      _initResourceCheckServiceImplProxy();
    return resourceCheckServiceImpl.resourceCheck(arg0);
  }
  
  
}