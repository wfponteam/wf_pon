package com.ws;

public class TripartWebServiceProxy implements com.ws.TripartWebService {
  private String _endpoint = null;
  private com.ws.TripartWebService tripartWebService = null;
  
  public TripartWebServiceProxy() {
    _initTripartWebServiceProxy();
  }
  
  public TripartWebServiceProxy(String endpoint) {
    _endpoint = endpoint;
    _initTripartWebServiceProxy();
  }
  
  private void _initTripartWebServiceProxy() {
    try {
      tripartWebService = (new com.ws.TripartWebServiceServiceLocator()).getTripartWebServicePort();
      if (tripartWebService != null) {
        if (_endpoint != null)
          ((javax.xml.rpc.Stub)tripartWebService)._setProperty("javax.xml.rpc.service.endpoint.address", _endpoint);
        else
          _endpoint = (String)((javax.xml.rpc.Stub)tripartWebService)._getProperty("javax.xml.rpc.service.endpoint.address");
      }
      
    }
    catch (javax.xml.rpc.ServiceException serviceException) {}
  }
  
  public String getEndpoint() {
    return _endpoint;
  }
  
  public void setEndpoint(String endpoint) {
    _endpoint = endpoint;
    if (tripartWebService != null)
      ((javax.xml.rpc.Stub)tripartWebService)._setProperty("javax.xml.rpc.service.endpoint.address", _endpoint);
    
  }
  
  public com.ws.TripartWebService getTripartWebService() {
    if (tripartWebService == null)
      _initTripartWebServiceProxy();
    return tripartWebService;
  }
  
  public java.lang.String active(java.lang.String arg0) throws java.rmi.RemoteException{
    if (tripartWebService == null)
      _initTripartWebServiceProxy();
    return tripartWebService.active(arg0);
  }
  
  
}