package func;

import java.util.Random;

import javax.swing.JOptionPane;

import GUI.MainContainer;
import GUI.GridPanel;

public class GenerateMaze
{
    private int sx, sy, dx, dy, bound, intensity;
    private int[][] genGrid;
    private Random rnd;
    private GridPanel gridPanel;
    private boolean finished = false;
    
    public GenerateMaze(int bound, int intensity, boolean rndSelected)
    {
        this.gridPanel = ((GridPanel) MainContainer.WINDOW.getContentPane().getComponent(0));
        this.sx = gridPanel.getSource()[0];
        this.sy = gridPanel.getSource()[1];
        this.dx = gridPanel.getDest()[0];
        this.dy = gridPanel.getDest()[1];
        
        this.bound = bound;
        this.genGrid = new int[bound][bound];
        this.rnd = new Random();
        this.intensity = intensity;

        
        if(sx == -99)
        {
            JOptionPane.showMessageDialog(null, GridPanel.msg_noSource);
            return;
        }
        
        if(dx == -99 && !rndSelected)
        {
            JOptionPane.showMessageDialog(null, GridPanel.msg_noDestination);
            return;
        }
        
        do
        {
            generateMaze();
        
            genGrid[sx][sy] = GridPanel.SOURCE;            

            if(rndSelected)
            {
                dx = rnd.nextInt(bound);
                dy = rnd.nextInt(bound);
                gridPanel.setDest(dx, dy);
            }
            genGrid[dx][dy] = GridPanel.DESTINATION;
        }while(!new FindPath(genGrid, bound, "BFS", "Generate Maze").hasPath());
        
        finished = true;
    }
    
    public boolean isFinished()
    {
        return finished;
    }
    
    public int[][] getGenGrid()
    {
        return genGrid;
    }
    
    public void setIntensity(int intensity)
    {
        this.intensity = intensity;
    }
    
    private void generateMaze()
    {
        // Reset genGrid
        this.genGrid = new int[bound][bound];
        
        // Add current source and generated destination to grid
        for(int i = 0; i < genGrid.length; i++)
            for(int j = 0; j < genGrid.length; j++)
                if((sx == i && sy == j) || (dx == i && dx == j))
                    continue;
                else
                    genGrid[i][j] = rnd.nextInt(101) < intensity ? GridPanel.OBSTACLE : genGrid[i][j];
    }
}