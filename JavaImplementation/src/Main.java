
public class Main {

	public static void main(String[] args) {
		// generate a maze with n by m cells
		Maze maze = new Maze(5,5, true);
		maze.display();
		maze = new Maze(5,5, false);
		maze.display();
		maze = new Maze(10,15, false);
		maze.display();
	}

}
