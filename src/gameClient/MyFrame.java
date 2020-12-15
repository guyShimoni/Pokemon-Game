package gameClient;

import api.directed_weighted_graph;
import api.edge_data;
import api.geo_location;
import api.node_data;
import gameClient.util.Point3D;
import gameClient.util.Range;
import gameClient.util.Range2D;

import javax.swing.*;
import java.awt.*;
import java.util.Iterator;
import java.util.List;

/**
 * This class represents a very simple GUI class to present a
 * game on a graph
 *
 */
public class MyFrame extends JFrame{
	
	private Arena _ar;
	private gameClient.util.Range2Range _w2f;
	private MyJpanel panel;
	
	MyFrame(String a) {
		super(a);
	}
	public void update(Arena ar) {
		this._ar = ar;
		panel = new MyJpanel();
		this.add(panel);
		updateFrame();
	}

	private void updateFrame() {
		Range rx = new Range(20,this.getWidth()-20);
		Range ry = new Range(this.getHeight()-10,150);
		Range2D frame = new Range2D(rx,ry);
		directed_weighted_graph g = _ar.getGraph();
		_w2f = Arena.w2f(g,frame);
		panel.update(_ar);
		panel.repaint();
	}
	public void paint(Graphics g) {
		
		updateFrame();
		drawInfo(g);
	}
	private void drawInfo(Graphics g) {
		List<String> str = _ar.get_info();
		String dt = "none";
		for(int i=0;i<str.size();i++) {
			g.drawString(str.get(i)+" dt: "+dt,100,60+i*20);
		}	
	}
	
	public void setTime(long time) {
		panel.setTime( time);
	}
}
