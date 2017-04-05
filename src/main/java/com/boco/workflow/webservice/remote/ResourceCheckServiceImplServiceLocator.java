/**
 * ResourceCheckServiceImplServiceLocator.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.boco.workflow.webservice.remote;

import com.boco.core.spring.SysProperty;

public class ResourceCheckServiceImplServiceLocator extends org.apache.axis.client.Service implements ResourceCheckServiceImplService {

    public ResourceCheckServiceImplServiceLocator() {
    }


    public ResourceCheckServiceImplServiceLocator(org.apache.axis.EngineConfiguration config) {
        super(config);
    }

    public ResourceCheckServiceImplServiceLocator(java.lang.String wsdlLoc, javax.xml.namespace.QName sName) throws javax.xml.rpc.ServiceException {
        super(wsdlLoc, sName);
    }

    // Use to get a proxy class for ResourceCheckServiceImplPort
    private java.lang.String ResourceCheckServiceImplPort_address =  SysProperty.getInstance().getValue("RESOURCE_CHECK_URL");

    public java.lang.String getResourceCheckServiceImplPortAddress() {
        return ResourceCheckServiceImplPort_address;
    }

    // The WSDD service name defaults to the port name.
    private java.lang.String ResourceCheckServiceImplPortWSDDServiceName = "ResourceCheckServiceImplPort";

    public java.lang.String getResourceCheckServiceImplPortWSDDServiceName() {
        return ResourceCheckServiceImplPortWSDDServiceName;
    }

    public void setResourceCheckServiceImplPortWSDDServiceName(java.lang.String name) {
        ResourceCheckServiceImplPortWSDDServiceName = name;
    }

    public ResourceCheckServiceImpl getResourceCheckServiceImplPort() throws javax.xml.rpc.ServiceException {
       java.net.URL endpoint;
        try {
            endpoint = new java.net.URL(ResourceCheckServiceImplPort_address);
        }
        catch (java.net.MalformedURLException e) {
            throw new javax.xml.rpc.ServiceException(e);
        }
        return getResourceCheckServiceImplPort(endpoint);
    }

    public ResourceCheckServiceImpl getResourceCheckServiceImplPort(java.net.URL portAddress) throws javax.xml.rpc.ServiceException {
        try {
            ResourceCheckServiceImplPortBindingStub _stub = new ResourceCheckServiceImplPortBindingStub(portAddress, this);
            _stub.setPortName(getResourceCheckServiceImplPortWSDDServiceName());
            return _stub;
        }
        catch (org.apache.axis.AxisFault e) {
            return null;
        }
    }

    public void setResourceCheckServiceImplPortEndpointAddress(java.lang.String address) {
        ResourceCheckServiceImplPort_address = address;
    }

    /**
     * For the given interface, get the stub implementation.
     * If this service has no port for the given interface,
     * then ServiceException is thrown.
     */
    public java.rmi.Remote getPort(Class serviceEndpointInterface) throws javax.xml.rpc.ServiceException {
        try {
            if (ResourceCheckServiceImpl.class.isAssignableFrom(serviceEndpointInterface)) {
                ResourceCheckServiceImplPortBindingStub _stub = new ResourceCheckServiceImplPortBindingStub(new java.net.URL(ResourceCheckServiceImplPort_address), this);
                _stub.setPortName(getResourceCheckServiceImplPortWSDDServiceName());
                return _stub;
            }
        }
        catch (java.lang.Throwable t) {
            throw new javax.xml.rpc.ServiceException(t);
        }
        throw new javax.xml.rpc.ServiceException("There is no stub implementation for the interface:  " + (serviceEndpointInterface == null ? "null" : serviceEndpointInterface.getName()));
    }

    /**
     * For the given interface, get the stub implementation.
     * If this service has no port for the given interface,
     * then ServiceException is thrown.
     */
    public java.rmi.Remote getPort(javax.xml.namespace.QName portName, Class serviceEndpointInterface) throws javax.xml.rpc.ServiceException {
        if (portName == null) {
            return getPort(serviceEndpointInterface);
        }
        java.lang.String inputPortName = portName.getLocalPart();
        if ("ResourceCheckServiceImplPort".equals(inputPortName)) {
            return getResourceCheckServiceImplPort();
        }
        else  {
            java.rmi.Remote _stub = getPort(serviceEndpointInterface);
            ((org.apache.axis.client.Stub) _stub).setPortName(portName);
            return _stub;
        }
    }

    public javax.xml.namespace.QName getServiceName() {
        return new javax.xml.namespace.QName("http://impl.service.project.bundles/", "ResourceCheckServiceImplService");
    }

    private java.util.HashSet ports = null;

    public java.util.Iterator getPorts() {
        if (ports == null) {
            ports = new java.util.HashSet();
            ports.add(new javax.xml.namespace.QName("http://impl.service.project.bundles/", "ResourceCheckServiceImplPort"));
        }
        return ports.iterator();
    }

    /**
    * Set the endpoint address for the specified port name.
    */
    public void setEndpointAddress(java.lang.String portName, java.lang.String address) throws javax.xml.rpc.ServiceException {
        
if ("ResourceCheckServiceImplPort".equals(portName)) {
            setResourceCheckServiceImplPortEndpointAddress(address);
        }
        else 
{ // Unknown Port Name
            throw new javax.xml.rpc.ServiceException(" Cannot set Endpoint Address for Unknown Port" + portName);
        }
    }

    /**
    * Set the endpoint address for the specified port name.
    */
    public void setEndpointAddress(javax.xml.namespace.QName portName, java.lang.String address) throws javax.xml.rpc.ServiceException {
        setEndpointAddress(portName.getLocalPart(), address);
    }

}
