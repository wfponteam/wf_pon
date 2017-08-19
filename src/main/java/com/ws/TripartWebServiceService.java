/**
 * TripartWebServiceService.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.ws;

public interface TripartWebServiceService extends javax.xml.rpc.Service {
    public java.lang.String getTripartWebServicePortAddress();

    public com.ws.TripartWebService getTripartWebServicePort() throws javax.xml.rpc.ServiceException;

    public com.ws.TripartWebService getTripartWebServicePort(java.net.URL portAddress) throws javax.xml.rpc.ServiceException;
}
