using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Drawing;

namespace MazeGenerator
{
    /**
     * This class represents a maze generator. It can be used to create a maze of any
     * size using the depth-first-search algorithm.
     */
    class Maze
    {
        /**
	     * This class represents a single point in the maze, which has access to its
	     * coordinates, and points that are adjacent to it.
	     */
	    private class MazeNode {
		
		    //The x, y coordinates for the point.
		    public int X { get; set; }
            public int Y { get; set; }
		
		    //The points that are adjacent to this one.
		    public IList<MazeNode> Adjacents { get; set; }
		
		    /**
		     * Constructs a new instance of the maze node for the given x, y locations.
		     */
		    public MazeNode(int x, int y) {
			    X = x;
			    Y = y;
			    Adjacents = new List<MazeNode>(4);
		    }
		
		    /**
		     *  Returns a string representation of this point in the maze in the form (x-coor, y-coor).
		     */
		    public override string ToString() {
			    return "(" + X + ", " + Y + ")";
		    }
	    }
	
	    /**
	     * The board of characters for the maze.
	     */
	    private char[,] MazeBoard { get; set; }
	
	    /**
	     * The nodes currently in the maze.
	     */
	    private IDictionary<Point, MazeNode> Nodes { get; set; }
	
	    /**
	     * Constructs a new instance of the maze, and generates it's links between nodes.
	     */
	    public Maze(int width, int height, bool debug) {
            MazeBoard = new char[(4 * width) + 1, (2 * height) + 1];
		    Nodes = new Dictionary<Point, MazeNode>();
		    Nodes.Add(new Point(0, 0), new MazeNode(0, 0));
		
            //Generates the default maze by adding character values to all positions in the array.
            for (int row = 0; row < MazeBoard.GetLength(1); row++)
            {
                for (int col = 0; col < MazeBoard.GetLength(0); col++)
                {
                    //Due to the structure of the maze, every odd row and 3rd column will
                    //represent a node character.
				    if(row % 2 == 1 && col % 4 == 2) {
                        MazeBoard[col, row] = ' ';
					    Point curPoint = new Point(col, row);
                        Nodes.Add(curPoint, new MazeNode(col, row));
                        //Nodes in the first column or row don't have neighbors to their
                        //left or up respectively.
					    if(col > 4) {
                            //Set the adjacency of the node to the left.
						    Point prevPoint = new Point(col - 4, row);
                            Nodes[curPoint].Adjacents.Add(Nodes[prevPoint]);
                            Nodes[prevPoint].Adjacents.Add(Nodes[curPoint]);
					    }
					    if(row > 2) {
                            //Set the adjacency of the node to the up.
						    Point prevPoint = new Point(col, row - 2);
                            Nodes[curPoint].Adjacents.Add(Nodes[prevPoint]);
                            Nodes[prevPoint].Adjacents.Add(Nodes[curPoint]);
					    }
				    } else if(col % 4 == 1 || col % 4 == 3) {
                        MazeBoard[col, row] = ' ';
				    } else if(col % 4 == 0 || row % 2 == 0) {
                        MazeBoard[col, row] = 'X';
				    }
			    }
		    }
            //Sets the default start and end points.
            MazeBoard[2, 0] = ' ';
            MazeBoard[MazeBoard.GetLength(0) - 3, MazeBoard.GetLength(1) - 1] = ' ';

		    HashSet<MazeNode> addedNodes = new HashSet<MazeNode>();
		    MazeNode start = Nodes[new Point(2, 1)];
		    addedNodes.Add(start);
		    buildMaze(start, addedNodes, new Random(), debug);
	    }
	
	    /**
	     * Builds the maze using a depth-first-search. Continuously removed walls while
	     * building the maze to represent edges between two nodes.
	     */
	    private void buildMaze(MazeNode node, HashSet<MazeNode> addedNodes, Random rand, bool debug) {
		    if(debug) {
                //In debug mode we want to display the maze as we build it.
                MazeBoard[node.X, node.Y] = 'V';
			    display(debug);
		    }
		    if(node != null) {
			    while (node.Adjacents.Count > 0) {
				    int direction = rand.Next(node.Adjacents.Count);
				    MazeNode next = node.Adjacents[direction];
				    for(int i = 0; i < node.Adjacents.Count; i++) {
					    MazeNode adjNode = node.Adjacents[i];
					    adjNode.Adjacents.Remove(node);
				    }
				    node.Adjacents.Remove(next);
                    //Only recurse further if the next node is not already in the maze.
				    if(!addedNodes.Contains(next)) {
                        MazeBoard[node.X + ((next.X - node.X) / 2), node.Y + ((next.Y - node.Y) / 2)] = ' ';
					    buildMaze(next, addedNodes, rand, debug);
				    }
				    addedNodes.Add(next);
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
	     */
	    public void display(bool debug) {
		    for(int row = 0; row < MazeBoard.GetLength(1); row++) {
			    for(int col = 0; col < MazeBoard.GetLength(0); col++) {
                    //If we're in debug mode, nodes will be represented by a 'V'.
                    //We don't want this if we are on our final display.
				    if(!debug && MazeBoard[col, row] == 'V') {
                        MazeBoard[col, row] = ' ';
				    }
                    Console.Write(MazeBoard[col, row]);
			    }
			    Console.WriteLine();
		    }
            Console.WriteLine();
	    }
    }
}
