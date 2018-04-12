/*
 * TCSS 342 martensk
 * Assignment 6: Maze Generator
 */

import java.awt.Point;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

/**
 * This class represents a maze generator. I can be used to create a maze of any
 * size using the depth-first-search algorithm.
 * 
 * @author Kyle
 * @version 1.0
 */
public class Maze {
	
	/**
	 * This class represents a single node in the maze, which has access to its
	 * coordinates, and nodes that are adjacent to it.
	 * 
	 * @author Kyle
	 * @version 1.0
	 */
	private class MazeNode {
		
		/**
		 * the x, y coordinates for the node.
		 */
		private int x, y;
		
		/**
		 * The nodes that are adjacent to this node.
		 */
		private List<MazeNode> adjacents;
		
		/**
		 * Constructs a new instance of the maze node for the given x, y locations.
		 * This method will run in constant time.
		 * 
		 * @param x the x coordinate for the node.
		 * @param y the y coordinate for the node.
		 */
		private MazeNode(int x, int y) {
			this.x = x;
			this.y = y;
			adjacents = new ArrayList<MazeNode>(4);
		}
		
		/**
		 *  @return a string representation of this node in the form (x-coor, y-coor)
		 *  This method will run in O(1) time.
		 */
		@Override
		public String toString() {
			return "(" + x + ", " + y + ")";
		}
	}
	
	/**
	 * The board of characters for the maze.
	 */
	private char[][] maze;
	
	/**
	 * The nodes current in the maze.
	 */
	private Map<Point, MazeNode> nodes;
	
	/**
	 * Constructs a new instance of the maze, and generates it's links between nodes.
	 * This method will operate in O(n) time, where n is the total number of nodes
	 * in the maze.
	 * 
	 * @param width the number of nodes in the maze's width.
	 * @param height the number of nodes in the maze's height.
	 * @param debug whether the maze is in debug-mode.
	 */
	public Maze(int width, int height, boolean debug) {
		maze = new char[(4 * width) + 1][(2 * height) + 1];
		nodes = new HashMap<Point, MazeNode>();
		nodes.put(new Point(0, 0), new MazeNode(0, 0));
		
//		generates the default maze by adding character values to all positions in the array
		for(int row = 0; row < maze[0].length; row++) {
			for(int col = 0; col < maze.length; col++) {
//				due to the structure of the maze, every odd row and 3rd column will
//				represent a node character.
				if(row % 2 == 1 && col % 4 == 2) {	
					maze[col][row] = ' ';
					Point curPoint = new Point(col, row);
					nodes.put(curPoint, new MazeNode(col, row));
//					nodes in the first column or row don't have neighbors to their
//					left or up respectively.
					if(col > 4) {
//						set the adjacency of the node to the left.
						Point prevPoint = new Point(col - 4, row);
						nodes.get(curPoint).adjacents.add(nodes.get(prevPoint));
						nodes.get(prevPoint).adjacents.add(nodes.get(curPoint));
					}
					if(row > 2) {
//						set the adjacency of the node to the up.
						Point prevPoint = new Point(col, row - 2);
						nodes.get(curPoint).adjacents.add(nodes.get(prevPoint));
						nodes.get(prevPoint).adjacents.add(nodes.get(curPoint));
					}
				} else if(col % 4 == 1 || col % 4 == 3) {
					maze[col][row] = ' ';
				} else if(col % 4 == 0 || row % 2 == 0) {
					maze[col][row] = 'X';
				}
			}
		}
//		sets the default start and end points.
		maze[2][0] = ' ';
		maze[maze.length - 3][maze[0].length - 1] = ' ';

		Set<MazeNode> addedNodes = new HashSet<MazeNode>();
		MazeNode start = nodes.get(new Point(2, 1));
		addedNodes.add(start);
		buildMaze(start, addedNodes, new Random(), debug);
	}
	
	/**
	 * Builds the maze using a depth-first-search. Continuously removed walls while
	 * building the maze to represent edges between two nodes.
	 * This method will run in O(n) time.
	 * 
	 * @param node The current node being added to the maze.
	 * @param addedNodes A set of nodes that have already been added to the maze.
	 * @param rand A random number generator to randomly construct the maze.
	 */
	private void buildMaze(MazeNode node, Set<MazeNode> addedNodes, Random rand, boolean debug) {
		if(debug) {
//			in debug mode we want to display the maze as we build it.
			maze[node.x][node.y] = 'V';
			display(debug);
		}
		if(node != null) {
			while (node.adjacents.size() > 0) {
				int direction = rand.nextInt(node.adjacents.size());
				MazeNode next = node.adjacents.get(direction);
				for(int i = 0; i < node.adjacents.size(); i++) {
					MazeNode adjNode = node.adjacents.get(i);
					adjNode.adjacents.remove(node);
				}
				node.adjacents.remove(next);
//				only recurse further if the next node is not already in the maze
				if(!addedNodes.contains(next)) {
					maze[node.x + ((next.x - node.x) / 2)][node.y + ((next.y - node.y) / 2)] = ' ';
					buildMaze(next, addedNodes, rand, debug);
				}
				addedNodes.add(next);
			}
		}
	}
	
	/**
	 * This method will print out the maze. Walls to the maze are represented with 'X'.
	 * In debug-mode, nodes in the maze will be represented with a 'V'.
	 * This method will run in O(n) time where n is the number of nodes in the maze.
	 */
	public void display() {
		display(false);
	}
	
	/**
	 * This method will print out the maze. Walls to the maze are represented with 'X'.
	 * In debug-mode, nodes in the maze will be represented with a 'V'.
	 * This method will run in O(n) time where n is the number of nodes in the maze.
	 * 
	 * @param debug whether the maze should display in debug-mode or not.
	 */
	public void display(boolean debug) {
		for(int row = 0; row < maze[0].length; row++) {
			for(int col = 0; col < maze.length; col++) {
//				if we're in debug mode, nodes will be represented by a 'V'.
//				We don't want this if we are on our final display.
				if(!debug && maze[col][row] == 'V') {
					maze[col][row] = ' ';
				}
				System.out.print(maze[col][row]);
			}
			System.out.println();
		}
		System.out.println();
	}
}
