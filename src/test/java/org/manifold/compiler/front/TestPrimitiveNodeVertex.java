package org.manifold.compiler.front;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.apache.log4j.ConsoleAppender;
import org.apache.log4j.LogManager;
import org.apache.log4j.PatternLayout;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.manifold.compiler.BooleanTypeValue;
import org.manifold.compiler.NilTypeValue;
import org.manifold.compiler.NodeTypeValue;
import org.manifold.compiler.TypeValue;
import org.manifold.compiler.Value;

public class TestPrimitiveNodeVertex {

  @BeforeClass
  public static void setupLogging() {
    PatternLayout layout = new PatternLayout(
        "%-5p [%t]: %m%n");
    LogManager.getRootLogger().removeAllAppenders();
    LogManager.getRootLogger().addAppender(
        new ConsoleAppender(layout, ConsoleAppender.SYSTEM_ERR));
  }
  
  @AfterClass
  public static void afterClass() {
    LogManager.getRootLogger().removeAllAppenders();
  }
  
  private PrimitivePortVertex generatePort(ExpressionGraph g,
      TypeValue signalType) {
    ConstantValueVertex vSignalType = new ConstantValueVertex(g, signalType);
    ExpressionEdge eSignalType = new ExpressionEdge(vSignalType, null);
    g.addVertex(vSignalType);
    g.addEdge(eSignalType);
    ConstantValueVertex vAttributes = new ConstantValueVertex(g,
        NilTypeValue.getInstance());
    ExpressionEdge eAttributes = new ExpressionEdge(vAttributes, null);
    g.addVertex(vAttributes);
    g.addEdge(eAttributes);
    PrimitivePortVertex vPort = new PrimitivePortVertex(g,
        eSignalType, eAttributes);
    g.addVertex(vPort);
    return vPort;
  }
  
  private ExpressionGraph g;
  private NamespaceIdentifier defaultNamespace;
  
  @Before
  public void setup() {
    g = new ExpressionGraph();
    defaultNamespace = new NamespaceIdentifier("");
  }
  
  @Test
  public void testConstructorSetsEdgeTargets() 
      throws MultipleDefinitionException, VariableNotDefinedException {
    PrimitivePortVertex vInPort = generatePort(g, 
        BooleanTypeValue.getInstance());
    VariableIdentifier idXIn = new VariableIdentifier(
        defaultNamespace, "xIn");
    g.addVertex(idXIn);
    VariableReferenceVertex xIn = g.getVariableVertex(idXIn);
    // xIn = vInPort
    ExpressionEdge eIn = new ExpressionEdge(vInPort, xIn);
    g.addEdge(eIn);

    PrimitivePortVertex vOutPort = generatePort(g, 
        BooleanTypeValue.getInstance());
    VariableIdentifier idXOut = new VariableIdentifier(
        defaultNamespace, "xOut");
    g.addVertex(idXOut);
    VariableReferenceVertex xOut = g.getVariableVertex(idXOut);
    // xOut = vOutPort
    ExpressionEdge eOut = new ExpressionEdge(vOutPort, xOut);
    g.addEdge(eOut);
    
    MappedArray<String, ExpressionEdge> inputMap = new MappedArray<>();
    ExpressionEdge eInputType = new ExpressionEdge(xIn, null);
    g.addEdge(eInputType);
    inputMap.put("x", eInputType);
    TupleTypeValueVertex vInputType = new TupleTypeValueVertex(g,
        inputMap, new MappedArray<>());
    g.addVertex(vInputType);
    
    MappedArray<String, ExpressionEdge> outputMap = new MappedArray<>();
    ExpressionEdge eOutputType = new ExpressionEdge(xOut, null);
    g.addEdge(eOutputType);
    outputMap.put("xbar", eOutputType);
    TupleTypeValueVertex vOutputType = new TupleTypeValueVertex(g,
        outputMap, new MappedArray<>());
    g.addVertex(vOutputType);
    
    ExpressionEdge eInputTuple = new ExpressionEdge(vInputType, null);
    g.addEdge(eInputTuple);
    ExpressionEdge eOutputTuple = new ExpressionEdge(vOutputType, null);
    g.addEdge(eOutputTuple);
    
    FunctionTypeValueVertex vNodePorts = new FunctionTypeValueVertex(g, 
        eInputTuple, eOutputTuple);
    g.addVertex(vNodePorts);
    ExpressionEdge eNodePorts = new ExpressionEdge(vNodePorts, null);
    g.addEdge(eNodePorts);

    PrimitiveNodeVertex vNode = new PrimitiveNodeVertex(g, eNodePorts);
    g.addVertex(vNode);
    
    assertEquals(vNode, eNodePorts.getTarget());
  }
  
  @Test
  public void testElaborate_noAttributes() 
      throws Exception {
    PrimitivePortVertex vInPort = generatePort(g, 
        BooleanTypeValue.getInstance());
    VariableIdentifier idXIn = new VariableIdentifier(
        defaultNamespace, "xIn");
    g.addVertex(idXIn);
    VariableReferenceVertex xIn = g.getVariableVertex(idXIn);
    // xIn = vInPort
    ExpressionEdge eIn = new ExpressionEdge(vInPort, xIn);
    g.addEdge(eIn);

    PrimitivePortVertex vOutPort = generatePort(g, 
        BooleanTypeValue.getInstance());
    VariableIdentifier idXOut = new VariableIdentifier(
        defaultNamespace, "xOut");
    g.addVertex(idXOut);
    VariableReferenceVertex xOut = g.getVariableVertex(idXOut);
    // xOut = vOutPort
    ExpressionEdge eOut = new ExpressionEdge(vOutPort, xOut);
    g.addEdge(eOut);
    
    MappedArray<String, ExpressionEdge> inputMap = new MappedArray<>();
    ExpressionEdge eInputType = new ExpressionEdge(xIn, null);
    g.addEdge(eInputType);
    inputMap.put("x", eInputType);
    TupleTypeValueVertex vInputType = new TupleTypeValueVertex(g,
        inputMap, new MappedArray<>());
    g.addVertex(vInputType);
    
    MappedArray<String, ExpressionEdge> outputMap = new MappedArray<>();
    ExpressionEdge eOutputType = new ExpressionEdge(xOut, null);
    g.addEdge(eOutputType);
    outputMap.put("xbar", eOutputType);
    TupleTypeValueVertex vOutputType = new TupleTypeValueVertex(g,
        outputMap, new MappedArray<>());
    g.addVertex(vOutputType);
    
    ExpressionEdge eInputTuple = new ExpressionEdge(vInputType, null);
    g.addEdge(eInputTuple);
    ExpressionEdge eOutputTuple = new ExpressionEdge(vOutputType, null);
    g.addEdge(eOutputTuple);
    
    FunctionTypeValueVertex vNodePorts = new FunctionTypeValueVertex(g, 
        eInputTuple, eOutputTuple);
    g.addVertex(vNodePorts);
    ExpressionEdge eNodePorts = new ExpressionEdge(vNodePorts, null);
    g.addEdge(eNodePorts);
    
    PrimitiveNodeVertex vNode = new PrimitiveNodeVertex(g, eNodePorts);
    g.addVertex(vNode);
    
    vNode.elaborate();
    Value v = vNode.getValue();
    assertTrue(v instanceof NodeTypeValue);
    NodeTypeValue node = (NodeTypeValue) v;
    // node has two ports
    assertTrue("node does not have two ports",
        node.getPorts().size() == 2);
    // node has a port called 'x'
    assertTrue("node does not have 'x' port",
        node.getPorts().containsKey("x"));
    // port 'x' on the node is an xIn
    assertEquals("'x' port is not of type xIn",
        xIn.getValue(), node.getPorts().get("x"));
    // node has a port called 'xbar'
    assertTrue("node does not have 'xbar' port",
        node.getPorts().containsKey("xbar"));
    // port 'xbar' on the node is an xOut
    assertEquals("'xbar' port is not of type xOut",
        xOut.getValue(), node.getPorts().get("xbar"));
    // node has no attributes
    assertTrue("node should not have any attributes",
        node.getAttributes().isEmpty());
  }
  
  @Test
  public void testElaborate_inputNil() 
      throws Exception {
    ConstantValueVertex nil = new ConstantValueVertex(g,
        NilTypeValue.getInstance());
    g.addVertex(nil);
    
    PrimitivePortVertex vInPort = generatePort(g, 
        BooleanTypeValue.getInstance());
    VariableIdentifier idXIn = new VariableIdentifier(
        defaultNamespace, "xIn");
    g.addVertex(idXIn);
    VariableReferenceVertex xIn = g.getVariableVertex(idXIn);
    // xIn = vInPort
    ExpressionEdge eIn = new ExpressionEdge(vInPort, xIn);
    g.addEdge(eIn);

    PrimitivePortVertex vOutPort = generatePort(g, 
        BooleanTypeValue.getInstance());
    VariableIdentifier idXOut = new VariableIdentifier(
        defaultNamespace, "xOut");
    g.addVertex(idXOut);
    VariableReferenceVertex xOut = g.getVariableVertex(idXOut);
    // xOut = vOutPort
    ExpressionEdge eOut = new ExpressionEdge(vOutPort, xOut);
    g.addEdge(eOut);
    
    MappedArray<String, ExpressionEdge> inputMap = new MappedArray<>();
    ExpressionEdge eInputType = new ExpressionEdge(nil, null);
    g.addEdge(eInputType);
    inputMap.put("0", eInputType);
    TupleTypeValueVertex vInputType = new TupleTypeValueVertex(g,
        inputMap, new MappedArray<>());
    g.addVertex(vInputType);
    
    MappedArray<String, ExpressionEdge> outputMap = new MappedArray<>();
    ExpressionEdge eOutputType = new ExpressionEdge(xOut, null);
    g.addEdge(eOutputType);
    outputMap.put("x", eOutputType);
    TupleTypeValueVertex vOutputType = new TupleTypeValueVertex(g,
        outputMap, new MappedArray<>());
    g.addVertex(vOutputType);
    
    ExpressionEdge eInputTuple = new ExpressionEdge(vInputType, null);
    g.addEdge(eInputTuple);
    ExpressionEdge eOutputTuple = new ExpressionEdge(vOutputType, null);
    g.addEdge(eOutputTuple);
    
    FunctionTypeValueVertex vNodePorts = new FunctionTypeValueVertex(g, 
        eInputTuple, eOutputTuple);
    g.addVertex(vNodePorts);
    ExpressionEdge eNodePorts = new ExpressionEdge(vNodePorts, null);
    g.addEdge(eNodePorts);
    
    PrimitiveNodeVertex vNode = new PrimitiveNodeVertex(g, eNodePorts);
    g.addVertex(vNode);
    
    vNode.elaborate();
    Value v = vNode.getValue();
    assertTrue(v instanceof NodeTypeValue);
    NodeTypeValue node = (NodeTypeValue) v;
    // node has one ports
    assertTrue("node does not have one port",
        node.getPorts().size() == 1);
    // node has a port called 'x'
    assertTrue("node does not have 'x' port",
        node.getPorts().containsKey("x"));
    // port 'x' on the node is an xOut
    assertEquals("'x' port is not of type xOut",
        xOut.getValue(), node.getPorts().get("x"));
    // node has no attributes
    assertTrue("node should not have any attributes",
        node.getAttributes().isEmpty());
  }
  
  @Test
  public void testElaborate_outputNil() 
      throws Exception { 
    ConstantValueVertex nil = new ConstantValueVertex(g,
        NilTypeValue.getInstance());
    g.addVertex(nil);
    
    PrimitivePortVertex vInPort = generatePort(g, 
        BooleanTypeValue.getInstance());
    VariableIdentifier idXIn = new VariableIdentifier(
        defaultNamespace, "xIn");
    g.addVertex(idXIn);
    VariableReferenceVertex xIn = g.getVariableVertex(idXIn);
    // xIn = vInPort
    ExpressionEdge eIn = new ExpressionEdge(vInPort, xIn);
    g.addEdge(eIn);

    PrimitivePortVertex vOutPort = generatePort(g, 
        BooleanTypeValue.getInstance());
    VariableIdentifier idXOut = new VariableIdentifier(
        defaultNamespace, "xOut");
    g.addVertex(idXOut);
    VariableReferenceVertex xOut = g.getVariableVertex(idXOut);
    // xOut = vOutPort
    ExpressionEdge eOut = new ExpressionEdge(vOutPort, xOut);
    g.addEdge(eOut);
    
    MappedArray<String, ExpressionEdge> inputMap = new MappedArray<>();
    ExpressionEdge eInputType = new ExpressionEdge(xIn, null);
    g.addEdge(eInputType);
    inputMap.put("x", eInputType);
    TupleTypeValueVertex vInputType = new TupleTypeValueVertex(g,
        inputMap, new MappedArray<>());
    g.addVertex(vInputType);
    
    MappedArray<String, ExpressionEdge> outputMap = new MappedArray<>();
    ExpressionEdge eOutputType = new ExpressionEdge(nil, null);
    g.addEdge(eOutputType);
    outputMap.put("0", eOutputType);
    TupleTypeValueVertex vOutputType = new TupleTypeValueVertex(g,
        outputMap, new MappedArray<>());
    g.addVertex(vOutputType);
    
    ExpressionEdge eInputTuple = new ExpressionEdge(vInputType, null);
    g.addEdge(eInputTuple);
    ExpressionEdge eOutputTuple = new ExpressionEdge(vOutputType, null);
    g.addEdge(eOutputTuple);
    
    FunctionTypeValueVertex vNodePorts = new FunctionTypeValueVertex(g, 
        eInputTuple, eOutputTuple);
    g.addVertex(vNodePorts);
    ExpressionEdge eNodePorts = new ExpressionEdge(vNodePorts, null);
    g.addEdge(eNodePorts);
    
    PrimitiveNodeVertex vNode = new PrimitiveNodeVertex(g, eNodePorts);
    g.addVertex(vNode);
    
    vNode.elaborate();
    Value v = vNode.getValue();
    assertTrue(v instanceof NodeTypeValue);
    NodeTypeValue node = (NodeTypeValue) v;
    // node has one ports
    assertTrue("node does not have one port",
        node.getPorts().size() == 1);
    // node has a port called 'x'
    assertTrue("node does not have 'x' port",
        node.getPorts().containsKey("x"));
    // port 'x' on the node is an xIn
    assertEquals("'x' port is not of type xIn",
        xIn.getValue(), node.getPorts().get("x"));
    // node has no attributes
    assertTrue("node should not have any attributes",
        node.getAttributes().isEmpty());
  }
  
}
