package api;

import api.*;
import org.junit.jupiter.api.*;


import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

class DWGraph_DSTest {

    directed_weighted_graph g = new DWGraph_DS();
    node_data node = new Node();
    node_data node1 = new Node();
    node_data node2 = new Node();
    node_data node3 = new Node();
    node_data node4 = new Node();


    @Test
    void nodeSize() {
        g.addNode(node);
        g.addNode(node1);
        g.addNode(node2);
        g.removeNode(node1.getKey());
        g.removeNode(node4.getKey());
        g.removeNode(node1.getKey());
        assertEquals(2,g.nodeSize());

    }

    @Test
    void edgeSize() {
        g.addNode(node);
        g.addNode(node1);
        g.addNode(node2);
        g.addNode(node3);
        g.connect(node.getKey(),node1.getKey(),1.6);
        g.connect(node.getKey(),node2.getKey(),2);
        g.connect(node.getKey(),node3.getKey(),5.6);
        g.connect(node.getKey(),node.getKey(),0.5);
        g.connect(node3.getKey(),node.getKey(),0);
        assertEquals(5, g.edgeSize());
        edge_data ed1 = g.getEdge(node.getKey(),node3.getKey());
        edge_data ed2 = g.getEdge(node3.getKey(),node.getKey());
        assertNotEquals(ed1, ed2);
        assertEquals(ed1.getWeight(), 5.6);
    }

    @Test
    void getV() {
        directed_weighted_graph g = new DWGraph_DS();
        assertTrue(g.nodeSize() == 0);
        assertTrue(g.edgeSize() == 0);
        
        g.addNode(node);
        g.addNode(node1);
        g.addNode(node2);
        g.addNode(node3);
        g.connect(node.getKey(),node1.getKey(),1.6);
        g.connect(node.getKey(),node2.getKey(),2);
        g.connect(node.getKey(),node3.getKey(),5.6);
        g.connect(node.getKey(),node.getKey(),0.5);
        g.connect(node3.getKey(),node.getKey(),0);
        Collection<node_data> cn = g.getV();
       
        Iterator<node_data> iter = cn.iterator();
        while (iter.hasNext()) {
            node_data n = iter.next();
            assertNotNull(n);
        }
    }
  
    @Test
    void connect() {
        directed_weighted_graph g = new DWGraph_DS();
        
        g.addNode(node);
        g.addNode(node1);
        g.addNode(node2);
        g.addNode(node3);
        g.connect(node.getKey(),node1.getKey(),1.6);
        g.connect(node.getKey(),node2.getKey(),2);
        g.connect(node.getKey(),node3.getKey(),5.6);
        g.removeEdge(node.getKey(),node1.getKey());
        assertNull(g.getEdge(node.getKey(),node1.getKey()));
        g.connect(node.getKey(),node.getKey(),0.5);
        g.connect(node3.getKey(),node.getKey(),0);
        g.removeEdge(node.getKey(),node1.getKey());
        assertNull(g.getEdge(node.getKey(),node1.getKey()));
        g.connect(node.getKey(),node1.getKey(),2.8);
        edge_data edge = g.getEdge(node.getKey(),node1.getKey());
        assertEquals(edge.getWeight(),2.8);
      
    }

    @Test
    void removeNode() {
        directed_weighted_graph g = new DWGraph_DS();
        g.addNode(node);
        g.addNode(node1);
        g.addNode(node2);
        g.addNode(node3);
        g.connect(node.getKey(),node1.getKey(),1.6);
        g.connect(node.getKey(),node2.getKey(),2);
        g.connect(node.getKey(),node3.getKey(),5.6);
        g.removeNode(node2.getKey());
        g.removeNode(node.getKey());
        g.removeNode(node4.getKey());
        assertNull(g.getEdge(node.getKey(),node3.getKey()));
        assertNull(g.getEdge(node3.getKey(),node.getKey()));
        assertNull(g.getEdge(node.getKey(),node1.getKey()));
       
        assertEquals(2,g.nodeSize());
    }

    @Test
    void removeEdge() {
        directed_weighted_graph g = new DWGraph_DS();
        
        g.addNode(node);
        g.addNode(node1);
        g.addNode(node2);
        g.addNode(node3);
        g.addNode(node4);
        g.connect(node.getKey(),node1.getKey(),1.6);
        g.connect(node.getKey(),node2.getKey(),2);
        g.connect(node.getKey(),node3.getKey(),5.6);
       
        g.removeEdge(node.getKey(),node1.getKey());
       
        assertNull(g.getEdge(node.getKey(),node1.getKey()));
    }





}