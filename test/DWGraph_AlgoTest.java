

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import api.*;

import gameClient.util.Point3D;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class DWGraph_AlgoTest {

	directed_weighted_graph g ;
	dw_graph_algorithms ga ;
	node_data n1 ;
	node_data n2 ;
	node_data n3 ;
	node_data n4 ;
	node_data n5 ;

	@BeforeEach
	void init() {
		ga = new DWGraph_Algo();
		g = new DWGraph_DS();
		n1 = new Node();
		n2 = new Node();
		n3 = new Node();
		n4 = new Node();
		n5 = new Node();
		ga.init(g);
	}


	@Test
	void copy_save_load() {

		g.addNode(n1);
		g.addNode(n2);
		g.addNode(n3);

		g.getNode(n1.getKey()).setLocation(new Point3D(1,8,500));
		g.connect(n1.getKey(), n2.getKey(), 20);
		g.connect(n1.getKey(), n3.getKey(), 30);
		g.connect(n2.getKey(), n1.getKey(), 40);
		g.connect(n3.getKey(), n1.getKey(), 50);
		ga.init(g);
		
		directed_weighted_graph g1 = ga.copy();
		assertEquals(g1,g);
		
		
		ga.save("data//testSave");
		ga.load("data//testSave");
		directed_weighted_graph g2=ga.getGraph();
		assertEquals(g2,g);
	}

	@Test
    void copy() {
        dw_graph_algorithms ga = new DWGraph_Algo();
    	g.addNode(n1);
		g.addNode(n2);
		g.addNode(n3);

		g.getNode(n1.getKey()).setLocation(new Point3D(1,8,500));
		g.connect(n1.getKey(), n2.getKey(), 20);
		g.connect(n1.getKey(), n3.getKey(), 30);
		g.connect(n2.getKey(), n1.getKey(), 40);
		g.connect(n3.getKey(), n1.getKey(), 50);

        directed_weighted_graph g1 = new DWGraph_DS();
        ga.init(g);
        g1 = ga.copy();

        assertEquals(g.edgeSize(), g1.edgeSize());
        assertEquals(g.nodeSize(), g1.nodeSize());
        assertEquals(g.getEdge(1, 2), g1.getEdge(1, 2));
        g.removeNode(5);
        assertNotEquals(g.nodeSize(), g1.nodeSize());


    }

	@Test
	void isConnected() {

		g.addNode(n1);
		g.addNode(n2);
		g.addNode(n3);
		g.addNode(n4);
		g.connect(n1.getKey(),n2.getKey(),0.1);
		g.connect(n1.getKey(),n3.getKey(),7.2);
		g.connect(n2.getKey(),n4.getKey(),2);
		g.connect(n3.getKey(),n4.getKey(),2.5);
		boolean flag = ga.isConnected();
		assertFalse(flag);


	}

	@Test
	void isConnected2() {

		g.addNode(n1);
		g.addNode(n2);
		g.addNode(n3);
		g.addNode(n4);
		g.connect(n1.getKey(),n2.getKey(),0);
		g.connect(n2.getKey(),n3.getKey(),7.2);
		g.connect(n3.getKey(),n4.getKey(),2);
		g.connect(n4.getKey(),n1.getKey(),2.3);


		boolean flag1 = ga.isConnected();

		assertTrue(flag1);



	}
	@Test
	void shortestPathDist() {
		directed_weighted_graph g = new DWGraph_DS();
		node_data n1 = new Node();
		node_data n2 = new Node();
		node_data n3 = new Node();
		node_data n4 = new Node();
		node_data n5 = new Node();
		g.addNode(n1);
		g.addNode(n2);
		g.addNode(n3);
		g.addNode(n4);
		g.addNode(n5);
		g.connect(n1.getKey(),n2.getKey(),10);
		g.connect(n2.getKey(),n3.getKey(),7);
		g.connect(n2.getKey(),n5.getKey(),20);
		g.connect(n3.getKey(),n4.getKey(),6);
		g.connect(n4.getKey(),n5.getKey(),5);
		g.connect(n3.getKey(),n5.getKey(),10);
		DWGraph_Algo graph_algo = new DWGraph_Algo();
		graph_algo.init(g);
		double dist = graph_algo.shortestPathDist(n1.getKey(),n5.getKey());

		assertEquals(dist,27);
	}

	@Test
	void shortestPath() {
		g.addNode(n1);
		g.addNode(n2);
		g.addNode(n3);
		g.addNode(n4);
		g.addNode(n5);
		g.connect(n1.getKey(),n2.getKey(),10);
		g.connect(n2.getKey(),n3.getKey(),7);
		g.connect(n2.getKey(),n5.getKey(),20);
		g.connect(n3.getKey(),n4.getKey(),6);
		g.connect(n4.getKey(),n5.getKey(),5);
		g.connect(n3.getKey(),n5.getKey(),10);
		ga.init(g);
		List<node_data> l = ga.shortestPath(n1.getKey(),n5.getKey());
		assertTrue(l.size() == 4);
		assertTrue(ga.shortestPathDist(n1.getKey(),n5.getKey()) == 27);
		//        for(node_data i: l) {
			//        	System.out.print(i.getKey()+" ");
			//        	
			//        }

		g.removeNode(2);
		assertTrue(g.edgeSize() == 3);
		assertTrue(ga.shortestPathDist(n1.getKey(),n5.getKey()) == 30);
		List<node_data> l1 = ga.shortestPath(n1.getKey(),n5.getKey());
		assertTrue(l1.size() == 3);
		g.removeNode(1);
		assertTrue(g.edgeSize() == 1);
		assertTrue(ga.shortestPathDist(n1.getKey(),n5.getKey()) == -1);
		List<node_data> l2 = ga.shortestPath(n1.getKey(),n5.getKey());
		assertTrue(l2 == null);
	}


	
	
	  @Test
	    void save() {
	        String path = "data//G1.json";
	        dw_graph_algorithms ga = new DWGraph_Algo();
	        directed_weighted_graph g = new DWGraph_DS();
	        ga.init(g);
	      
	        g.addNode(n1);
	        g.addNode(n2);
	        g.addNode(n3);
	        g.addNode(n4);
	        g.addNode(n5);
	        g.connect(n1.getKey(),n2.getKey(),10);
	        g.connect(n2.getKey(),n3.getKey(),7);
	        g.connect(n2.getKey(),n5.getKey(),20);
	        g.connect(n3.getKey(),n4.getKey(),6);
	        g.connect(n4.getKey(),n5.getKey(),5);
	        g.connect(n3.getKey(),n5.getKey(),10);
	        ga.save(path);
	    }

}
