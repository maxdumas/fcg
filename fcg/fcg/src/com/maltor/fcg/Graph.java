package com.maltor.fcg;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Array;
import com.maltor.fcg.actors.LinkActor;
import com.maltor.fcg.actors.NodeActor;

public class Graph  {
	protected static short nextGroupIndex = -2; // groupIndex -1 is reserved for particles
	
	public final short groupIndex, id;
	public final Array<NodeActor> nodes = new Array<NodeActor>();
	public Stage stage;
	
	public Graph(Stage stage) {
		this.stage = stage;
		groupIndex = nextGroupIndex--;
		id = (short) -groupIndex;
	}
	
	public boolean addNode(NodeActor node) {
		if(node.graph != null) return false;
		nodes.add(node);
		node.graph = this;
		stage.addActor(node);
		System.out.println("Adding a node!: " + node.getCenterX() + ", " + node.getCenterY());
		
		return true;
	}
	
	public <T extends NodeActor> boolean addNode(T node, World world, float x, float y) {
		if(node.graph != null) return false;
		node.instantiate(world, x, y, groupIndex);
		addNode(node);
		
		return true;
	}
	
	public NodeActor getNearestNode(float x, float y) {
		if(nodes.size == 1)
			return nodes.get(0);
		
		NodeActor closest = null;
		Vector2 nv = new Vector2(0, 0);
		float distance = Float.POSITIVE_INFINITY;
		
		for(NodeActor n : nodes) {
			nv.set(n.getCenterX(), n.getCenterY());
			float distance_n = nv.dst(x, y);
			if(distance_n < distance) {
				closest = n;
				distance = distance_n;
			}
		}
		
		return closest;
	}
	
	public NodeActor getContainedNode(float x, float y) {
		for(NodeActor n : nodes)
			if(x > n.getX() && x < n.getX() + n.getWidth() && y > n.getY() && y < n.getY() + n.getWidth())
				return n;
		
		return null;
	}
	
	public LinkActor connectNodes(NodeActor a, NodeActor b) {
		if(a == null || b == null) return null;
		if(!nodes.contains(a, true)) addNode(a);
		if(!nodes.contains(b, true)) addNode(b);
		
		a.connectTo(b);
		LinkActor link = a.neighbors.get(b);
		stage.addActor(link);
		return link;
	}
	
	public void disconnectNodes(NodeActor a, NodeActor b) {
		a.disconnectFrom(b, true);
	}
}
