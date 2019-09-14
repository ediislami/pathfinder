# Pathfinder

You can choose between DFS, BFS, Dijkstra SP and BellmanFord SP algorithms to determine the shortest path between two points

*This application was written in 2015 as a side project.*

## Installation

To compile this application run the following command inside the `src` folder.

```
javac -d ../bin GUI/*.java func/*.java graphapi/*.java
```

## Usage

To run the application navigate to the `bin` directory where the `.class` files are stored.

Run the following command to launch the GUI

```
java GUI/MainContainer
```

You will be presented with a very straightforward interface where you can select your source and destination. You can also add obstacles.

You can choose between different algorithms to determine the shortest path between the two points.

## Note

Source files inside the `graphapi` folder are used under the **GPLv3** license and are part of [Algorithms, 4th Edition](https://algs4.cs.princeton.edu/code/)

## License

[GNU General Public License, version 3 (GPLv3)](http://www.gnu.org/copyleft/gpl.html)
