package org.whdl.intermediate;

import static org.junit.Assert.*;

import java.util.HashMap;

import org.junit.Test;

public class TestEndpoint {

  private static final EndpointType defaultEndpointDefinition = new EndpointType(new HashMap<String, Type>());

  @Test
  public void testGetAttribute() throws UndeclaredIdentifierException {
    Endpoint ept = new Endpoint(defaultEndpointDefinition);
    Value v = new BooleanValue(new BooleanType(), true);
    ept.setAttribute("v", v);
    Value vActual = ept.getAttribute("v");
    assertEquals(v, vActual);
  }

  @Test(expected=org.whdl.intermediate.UndeclaredIdentifierException.class)
  public void testGetAttribute_nonexistent() throws UndeclaredIdentifierException {
    Endpoint ept = new Endpoint(defaultEndpointDefinition);
    Value vBogus = ept.getAttribute("bogus");
  }
  
  @Test
  public void testSetAttribute() {
    Endpoint ept = new Endpoint(defaultEndpointDefinition);
    Value v = new BooleanValue(new BooleanType(), true);
    ept.setAttribute("v", v);
  }
  
  @Test
  public void testSetAttribute_multiple_set() {
    // setting an attribute twice should just work
    Type boolType = new BooleanType();
    Endpoint ept = new Endpoint(defaultEndpointDefinition);
    Value v = new BooleanValue(boolType, true);
    ept.setAttribute("v", v);
    Value v2 = new BooleanValue(boolType, false);
    ept.setAttribute("v", v2);
  }

}
