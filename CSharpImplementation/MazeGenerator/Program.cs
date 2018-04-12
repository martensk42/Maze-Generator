using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace MazeGenerator
{
    class Program
    {
        static void Main(string[] args)
        {
            //Generate a maze with n by m cells and write each maze to the conosle.
            Maze maze = new Maze(5, 5, false);
            maze.display();
            maze = new Maze(5, 5, false);
            maze.display();
            maze = new Maze(10, 15, false);
            maze.display();
        }
    }
}
