package func;

import java.util.ArrayList;
import java.util.Iterator;

import javax.swing.JOptionPane;

import GUI.ConsolePanel;
import GUI.MainContainer;
import GUI.GridPanel;
import graphapi.BellmanFordSP;
import graphapi.BreadthFirstPaths;
import graphapi.DepthFirstPaths;
import graphapi.DijkstraSP;
import graphapi.DirectedEdge;
import graphapi.EdgeWeightedDigraph;
import graphapi.Graph;

public class FindPath
{
    private int source, destination, weight, stepCount, bound;
    private double time;
    private String alg, from;
    private int[][] numbers, grid, path;
    private Iterator<Integer> iterator_undirected;
    private Iterator<DirectedEdge> iterator_directed;
    private ArrayList<Integer> nodes;
    private ConsolePanel consolePanel;
    private GridPanel gridPanel;
    private Graph undirected;
    
    // Constructor Instant
    public FindPath(int[][] grid, int bound, String alg)
    {
        this(grid, bound, alg, "Constructor");

        if(nodes != null)
        {
            // Add nodes to path
            fillPath();         
        }
        gridPanel.setPath(path);
    }
    
    // Constructor Animation
    public FindPath(int[][] grid, int bound, String alg, String from)
    {
        this.source = -99;
        this.destination = -99;
        this.weight = 1;
        this.path = new int[bound][bound];
        
        this.gridPanel = ((GridPanel) MainContainer.WINDOW.getContentPane().getComponent(0));
        this.consolePanel = ((ConsolePanel) MainContainer.WINDOW.getContentPane().getComponent(2));

        this.grid = grid;
        this.alg = alg;
        this.bound = bound;
        this.from = from;
        
        // Get system time
        time = System.currentTimeMillis();
        
        // Fill array numbers
        fillArray();
        
        // Check grid for source, destination and obstacles
        checkGrid();
        
        // If source not found, pop-out a message and return
        if(source == -99)
        {
            JOptionPane.showMessageDialog(null, GridPanel.msg_noSource);
            return;
        }
        
        // If destination not found, pop-out a message and return
        if(destination == -99)
        {
            JOptionPane.showMessageDialog(null, GridPanel.msg_noDestination);
            return;
        }
        
        // If selected algorithm choice is DFS or BFS
        if(alg.equals("DFS") || alg.equals("BFS"))
            undirectedPaths();
        
        // If selected algorithm choice is Dijkstra or BellmanFord SP
        if(alg.equals("Dijkstra SP") || alg.equals("BellmanFord SP"))
            directedPaths();
        
        // If nodes is empty return because no path is found
        if(nodes == null)
            return;
        
        // Updates log
        if(!from.equals("Generate Maze"))
            updateLog();
    }
    
    public void undirectedPaths()
    {
        undirected = new Graph(bound * bound);
        
        linkNodes(undirected);
        
        if(alg.equals("DFS"))
            if(!dfs(undirected))
                return;
        
        if(alg.equals("BFS"))
            if(!bfs(undirected))
                return;
        time = (System.currentTimeMillis() - time) / 1000;
        
        
        nodes = new ArrayList<Integer>();

        while(iterator_undirected.hasNext())
            nodes.add(iterator_undirected.next());
    }
    
    public void directedPaths()
    {
        EdgeWeightedDigraph ewd = new EdgeWeightedDigraph(bound * bound);
        
        linkNodes(ewd);
        
        if(alg.equals("Dijkstra SP"))
            if(!dijkstra(ewd))
                return;
        
        if(alg.equals("BellmanFord SP"))
            if(!bellmanfordsp(ewd))
                return;
        
        time = (System.currentTimeMillis() - time) / 1000;
        
        
        nodes = new ArrayList<Integer>();

        while(iterator_directed.hasNext())
            nodes.add(iterator_directed.next().from());

    }
    
    private void fillArray()
    {
        numbers = new int[bound][bound];
        // fill array with values
        for(int i = 0, nr = 0; i < numbers.length; i++)
            for(int j = 0; j < numbers.length; j++, nr++)
                numbers[i][j] = nr;
    }
    
    private void checkGrid()
    {
        for(int i = 0; i < bound; i++)
            for(int j = 0; j < bound; j++)
                if(grid[i][j] == GridPanel.SOURCE)
                    source = numbers[i][j];
                else if(grid[i][j] == GridPanel.DESTINATION)
                    destination = numbers[i][j];
                else if(grid[i][j] == GridPanel.OBSTACLE)
                    numbers[i][j] = GridPanel.OBSTACLE;
    }
    
    private void linkNodes(Graph undirected)
    {
        for(int i = 0; i < bound; i++)
        {
            for(int j = 0; j < bound; j++)
            {
                // If selected node is obstacle skip/continue
                if(numbers[i][j] == GridPanel.OBSTACLE)
                    continue;
                
                // position validity check (IndexOutOfBounds exception)
                if(i - 1 >= 0 && j - 1 >= 0 && numbers[i - 1][j - 1] != GridPanel.OBSTACLE && (numbers[i][j - 1] != GridPanel.OBSTACLE && numbers[i - 1][j] != GridPanel.OBSTACLE))
                    undirected.addEdge(numbers[i][j], numbers[i - 1][j - 1]);
                
                if(j - 1 >= 0 && numbers[i][j - 1] != GridPanel.OBSTACLE)
                    undirected.addEdge(numbers[i][j], numbers[i][j - 1]);
                
                if(i + 1 < bound && j - 1 >= 0 && numbers[i + 1][j - 1] != GridPanel.OBSTACLE && (numbers[i][j - 1] != GridPanel.OBSTACLE && numbers[i + 1][j] != GridPanel.OBSTACLE))
                    undirected.addEdge(numbers[i][j], numbers[i + 1][j - 1]);

                if(i + 1 < bound && numbers[i + 1][j] != GridPanel.OBSTACLE)
                    undirected.addEdge(numbers[i][j], numbers[i + 1][j]);
                
                if(i + 1 < bound && j + 1 < bound && numbers[i + 1][j + 1] != GridPanel.OBSTACLE && (numbers[i + 1][j] != GridPanel.OBSTACLE && numbers[i][j + 1] != GridPanel.OBSTACLE))
                    undirected.addEdge(numbers[i][j], numbers[i + 1][j + 1]);
                
                if(j + 1 < bound && numbers[i][j + 1] != GridPanel.OBSTACLE)
                    undirected.addEdge(numbers[i][j], numbers[i][j + 1]);
                
                if(i - 1 >= 0 && j + 1 < bound && numbers[i - 1][j + 1] != GridPanel.OBSTACLE && (numbers[i][j + 1] != GridPanel.OBSTACLE && numbers[i - 1][j] != GridPanel.OBSTACLE))
                    undirected.addEdge(numbers[i][j], numbers[i - 1][j + 1]);
                
                if(i - 1 >= 0 && numbers[i - 1][j] != GridPanel.OBSTACLE)
                    undirected.addEdge(numbers[i][j], numbers[i - 1][j]);
            }
        }
    }
    
    private void linkNodes(EdgeWeightedDigraph ewd)
    {
        for(int i = 0; i < bound; i++)
        {
            for(int j = 0; j < bound; j++)
            {
                // If selected node is obstacle skip/continue
                if(numbers[i][j] == GridPanel.OBSTACLE)
                    continue;
                
                // position validity check (IndexOutOfBounds exception)
                if(i - 1 >= 0 && j - 1 >= 0 && numbers[i - 1][j - 1] != GridPanel.OBSTACLE && (numbers[i][j - 1] != GridPanel.OBSTACLE && numbers[i - 1][j] != GridPanel.OBSTACLE))
                    ewd.addEdge(new DirectedEdge(numbers[i][j], numbers[i - 1][j - 1], weight));
                
                if(j - 1 >= 0 && numbers[i][j - 1] != GridPanel.OBSTACLE)
                    ewd.addEdge(new DirectedEdge(numbers[i][j], numbers[i][j - 1], weight));
                
                if(i + 1 < bound && j - 1 >= 0 && numbers[i + 1][j - 1] != GridPanel.OBSTACLE && (numbers[i][j - 1] != GridPanel.OBSTACLE && numbers[i + 1][j] != GridPanel.OBSTACLE))
                    ewd.addEdge(new DirectedEdge(numbers[i][j], numbers[i + 1][j - 1], weight));

                if(i + 1 < bound && numbers[i + 1][j] != GridPanel.OBSTACLE)
                    ewd.addEdge(new DirectedEdge(numbers[i][j], numbers[i + 1][j], weight));
                
                if(i + 1 < bound && j + 1 < bound && numbers[i + 1][j + 1] != GridPanel.OBSTACLE && (numbers[i + 1][j] != GridPanel.OBSTACLE && numbers[i][j + 1] != GridPanel.OBSTACLE))
                    ewd.addEdge(new DirectedEdge(numbers[i][j], numbers[i + 1][j + 1], weight));
                
                if(j + 1 < bound && numbers[i][j + 1] != GridPanel.OBSTACLE)
                    ewd.addEdge(new DirectedEdge(numbers[i][j], numbers[i][j + 1], weight));
                
                if(i - 1 >= 0 && j + 1 < bound && numbers[i - 1][j + 1] != GridPanel.OBSTACLE && (numbers[i][j + 1] != GridPanel.OBSTACLE && numbers[i - 1][j] != GridPanel.OBSTACLE))
                    ewd.addEdge(new DirectedEdge(numbers[i][j], numbers[i - 1][j + 1], weight));
                
                if(i - 1 >= 0 && numbers[i - 1][j] != GridPanel.OBSTACLE)
                    ewd.addEdge(new DirectedEdge(numbers[i][j], numbers[i - 1][j], weight));
            }
        }
    }
    
    private boolean dfs(Graph undirected)
    {
        DepthFirstPaths dfs = new DepthFirstPaths(undirected, source);

        // If there is no path to destination, show message dialog and return
        if(!dfs.hasPathTo(destination))
        {            
            JOptionPane.showMessageDialog(null, GridPanel.msg_noPath);
            return false;
        }
        
        iterator_undirected = dfs.pathTo(destination).iterator();
        return true;
    }

    private boolean bfs(Graph undirected)
    {
        BreadthFirstPaths bfs = new BreadthFirstPaths(undirected, source);
        
        // If there is no path to destination, show message dialog and return
        if(!bfs.hasPathTo(destination))
        {            
            if(!from.equals("Generate Maze"))
                JOptionPane.showMessageDialog(null, GridPanel.msg_noPath);
            return false;
        }
        
        iterator_undirected = bfs.pathTo(destination).iterator();
        return true;
    }

    private boolean dijkstra(EdgeWeightedDigraph ewd)
    {
        DijkstraSP dijkstra = new DijkstraSP(ewd, source);
        // If there is no path to destination, show message dialog and return
        if(!dijkstra.hasPathTo(destination))
        {
            JOptionPane.showMessageDialog(null, GridPanel.msg_noPath);
            return false;
        }
        
        iterator_directed = dijkstra.pathTo(destination).iterator();
        return true;
    }
    
    private boolean bellmanfordsp(EdgeWeightedDigraph ewd)
    {
        BellmanFordSP bellmanford = new BellmanFordSP(ewd, source);
        // If there is no path to destination, show message dialog and return
        if(!bellmanford.hasPathTo(destination))
        {
            JOptionPane.showMessageDialog(null, GridPanel.msg_noPath);
            return false;
        }
        
        iterator_directed = bellmanford.pathTo(destination).iterator();
        return true;
    }
    
    private void fillPath()
    {
        // fill path
        for(int i = 0; i < bound; i++)
            for(int j = 0; j < bound; j++)
                path[i][j] = (grid[i][j] != GridPanel.SOURCE && grid[i][j] != GridPanel.DESTINATION) && nodes.contains(numbers[i][j]) ? GridPanel.PATH : GridPanel.BLANK;
    }
    
    private void updateLog()
    {
        // Get step count
        stepCount = nodes.size();
        
        if(alg.equals("BFS") || alg.equals("DFS"))
            stepCount -= 2;
        else
            stepCount -= 1;
        
        // Set text
        String s = " " + alg + " started..."
                 + "\n Source: x -> " + gridPanel.getSource()[0] + " y -> " + gridPanel.getSource()[1]
                 + "\n Destination: x -> " + gridPanel.getDest()[0] + " y -> " + gridPanel.getDest()[1] 
                 + "\n Steps:  " + stepCount
                 + "\n Time: " + time + " s"
                 + "\n-----------------------------------------\n";
       
        consolePanel.setText(s);
    }

    public ArrayList<Integer> getNodes(String alg)
    {
        if(alg.equals("DFS") || alg.equals("BFS"))
        {
            nodes.remove(0);
            nodes.remove(nodes.size() - 1);            
        }
        else
        {
            nodes.remove(0);            
        }
        return nodes;
    }
    
    public boolean hasPath()
    {
        return new BreadthFirstPaths(undirected, source).hasPathTo(destination);
    }
}