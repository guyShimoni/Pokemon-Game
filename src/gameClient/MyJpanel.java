package gameClient;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.imageio.ImageIO;

import javax.swing.JPanel;

import api.directed_weighted_graph;
import api.edge_data;
import api.geo_location;
import api.node_data;
import gameClient.util.Point3D;
import gameClient.util.Range;
import gameClient.util.Range2D;

public class MyJpanel extends JPanel {

	private Arena _ar;
	private gameClient.util.Range2Range _w2f;
    private BufferedImage pok;
    private BufferedImage adg;
    private BufferedImage area;
	private long time;
   
	public MyJpanel() {
		super();	
		try {
			pok = ImageIO.read(new File("pokemon.png"));
			adg = ImageIO.read(new File("agent.png"));
			area= ImageIO.read(new File("area.jpg"));
		} catch (IOException e) {
			
			e.printStackTrace();
		}    
	}
	
	public void update(Arena ar) {
		this._ar = ar;
		updateFrame();
		
	}

	private void updateFrame() {
		Range rx = new Range(20,this.getWidth()-20);
		Range ry = new Range(this.getHeight()-10,150);
		Range2D frame = new Range2D(rx,ry);
		directed_weighted_graph g = _ar.getGraph();
		_w2f = Arena.w2f(g,frame);
		
		
	}
	public void paint(Graphics g) {
		int w = this.getWidth();
		int h = this.getHeight();
		g.drawImage(area,0, 0, w, h,this);
		g.setColor(Color.RED);
		g.setFont(new Font("",Font.BOLD,25));
		g.drawString("Time: "+time, 25, 50);
		drawGraph(g);
		drawPokemons(g);
		drawAgants(g);
		drawInfo(g);
		
	}
	private void drawInfo(Graphics g) {
		List<String> str = _ar.get_info();
		String dt = "none";
		g.setColor(Color.RED);
		for(int i=0;i<str.size();i++) {
			g.drawString(str.get(i)+" dt: "+dt,100,1);
		
		}
		
	}
	private void drawGraph(Graphics g) {
		directed_weighted_graph gg = _ar.getGraph();
		Iterator<node_data> iter = gg.getV().iterator();
		while(iter.hasNext()) {
			node_data n = iter.next();
			g.setColor(Color.black);
			drawNode(n,5,g);
			Iterator<edge_data> itr = gg.getE(n.getKey()).iterator();
			while(itr.hasNext()) {
				edge_data e = itr.next();
				g.setColor(Color.gray);
				drawEdge(e, g);
			}
		}
	}
	private void drawPokemons(Graphics g) {
		List<CL_Pokemon> fs = _ar.getPokemons();
		if(fs!=null) {
		Iterator<CL_Pokemon> itr = fs.iterator();
		
		while(itr.hasNext()) {
			
			CL_Pokemon f = itr.next();
			Point3D c = f.getLocation();
			int r=15;
			g.setColor(Color.green);
			if(f.getType()<0) {g.setColor(Color.orange);}
			if(c!=null) {

				geo_location fp = this._w2f.world2frame(c);
			     g.drawImage(this.pok,(int)fp.x()-r, (int)fp.y()-r, 2*r, 2*r, this);  
				
			}
		}
		}
	}
	private void drawAgants(Graphics g) {
		List<CL_Agent> rs = _ar.getAgents();
		
		int i=0;
		while(rs!=null && i<rs.size()) {
			geo_location c = rs.get(i).getLocation();
			int r=15;
			String s= "Value: "+Double.toString(rs.get(i).getValue());
			i++;
			if(c!=null) {

				geo_location fp = this._w2f.world2frame(c);
				g.drawImage(this.adg, (int)fp.x()-r, (int)fp.y()-r, 2*r, 2*r, this);
				g.setColor(Color.red);
				g.drawString(s,(int)fp.x(), (int)(fp.y()-4*r));
			}
		}
	}
	private void drawNode(node_data n, int r, Graphics g) {
		geo_location pos = n.getLocation();
		geo_location fp = this._w2f.world2frame(pos);
		g.fillOval((int)fp.x()-r, (int)fp.y()-r, 2*r, 2*r);
		g.drawString(""+n.getKey(), (int)fp.x(), (int)fp.y()-4*r);
	}
	private void drawEdge(edge_data e, Graphics g) {
		directed_weighted_graph gg = _ar.getGraph();
		geo_location s = gg.getNode(e.getSrc()).getLocation();
		geo_location d = gg.getNode(e.getDest()).getLocation();
		geo_location s0 = this._w2f.world2frame(s);
		geo_location d0 = this._w2f.world2frame(d);
		g.drawLine((int)s0.x(), (int)s0.y(), (int)d0.x(), (int)d0.y());
	}

	public void setTime(long time) {
		long seconds = TimeUnit.MILLISECONDS.toSeconds(time);
		this.time=seconds;
	}



}
