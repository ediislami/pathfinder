package GUI;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

@SuppressWarnings("serial")
public class GridPanel extends JPanel
{
    public static final String msg_noSource = "Please select a source point!", 
                          msg_noDestination = "Please select a destination point!",
                          msg_noPath = "No path found!";
    
    
    public static final int SOURCE = -100, DESTINATION = -101, OBSTACLE = -102, PATH = -103, BLANK = 0;
    
    private SettingsPanel settingsPanel;
    private ConsolePanel consolePanel;
    
    private int mazeSize, rectSize, bound, sx, sy, dx, dy;
    private MouseHandler mouseHandler;
    private int[][] grid;
    private int[][] path;
    
    public GridPanel()
    {
        // Set maze size
        this.mazeSize = (int) getPreferredSize().getWidth();
        
        // Set rectangle size
        this.rectSize = mazeSize / 10;
        
        // Set bound
        this.bound = mazeSize / rectSize;
        
        // Set source and destination
        this.sx = -99;
        this.sy = -99;
        this.dx = -99;
        this.dy = -99;
        
        // Initialize MouseHandler to get x, y coordinates
        mouseHandler = new MouseHandler();
        
        // Add mouse listeners to panel
        addMouseListener(mouseHandler);
        addMouseMotionListener(mouseHandler);
        
        // Initialize grid
        grid = new int[40][40];
                
        // Set maze border
        setBorder(BorderFactory.createLineBorder(Color.black, 2));
    }
    
    /**************************************************
     *               Getters and setters              *
     **************************************************/
    
    public int getBound()
    {
        return bound;
    }
    
    public int getMazeSize()
    {
        return mazeSize;
    }
    
    public void setMazeSize(int size)
    {
        if(size < 10)
            return;
        
        // If size >= 0 assign new size else do nothing
        rectSize = size >= 0 ? size : 10;
        
        // Set bound
        this.bound = mazeSize / rectSize;
    }    
    
    public int[][] getPath()
    {
        return path;
    }
    
    public void setPath(int[][] path)
    {
        this.path = path;
    }
    
    public void setPath(int x, int y)
    {
        path[x][y] = GridPanel.PATH;
    }
    
    public int[][] getGrid()
    {
        return grid;
    }

    public void setGrid(int[][] grid)
    {
        this.grid = grid;
    }
    
    public Dimension getPreferredSize()
    {
        return new Dimension(400, 400);
    }
    
    public int getRectSize()
    {
        return rectSize;
    }
    
    public int[] getSource()
    {
        return new int[]{sx, sy};
    }
    
    public int[] getDest()
    {
        return new int[]{dx, dy};
    }
    
    public void setDest(int dx, int dy)
    {
       this.dx = dx;
       this.dy = dy;
    }
    
    public void reset()
    {
        setGrid(new int[40][40]);
        setPath(new int[40][40]);
        sx = -99;
        sy = -99;
        dx = -99;
        dy = -99;
    }
    
    /**************************************************
     *                 Mouse listeners                *
     **************************************************/
    
    class MouseHandler implements MouseListener, MouseMotionListener
    {
        private int x, y;
        
        public void mouseReleased(MouseEvent e)
        {
            // Get X and Y Coordinates
            x = calculateCordPos(e.getX());
            y = calculateCordPos(e.getY());
            
            mouseMoved(e);
            
            // If X or Y less than 0 -> do nothing
            if(x < 0 || y < 0 || x >= bound || y >= bound)
                return;
            
            // If add radio button is selected and is mouse button is left
            if(settingsPanel.getRdbtnAdd().isSelected() && SwingUtilities.isLeftMouseButton(e))
            {
                // Gets selected option
                String selected = (String) settingsPanel.getComboBox().getSelectedItem();
                
                // Checks if selected x-y coordinates are blank
                if(grid[x][y] == GridPanel.BLANK)
                {
                    // If selected option is obstacle
                    if(selected.equals("Obstacle"))
                        grid[x][y] = GridPanel.OBSTACLE;
                
                    // If selected option is Source
                    if(selected.equals("Source"))
                    {
                        if(sx == -99)
                        {
                            sx = x; 
                            sy = y;
                            grid[x][y] = GridPanel.SOURCE;
                        }
                        else
                        {
                            // Draws old coordinates to blank
                            grid[sx][sy] = GridPanel.BLANK;
                                
                            // Stores new source coordinates
                            sx = x; 
                            sy = y;
                                
                            // Draws new source point
                            grid[x][y] = GridPanel.SOURCE;                            
                        }
                    }
                    
                    // If selected option is Destination
                    if(selected.equals("Destination"))
                    {
                        if(dx == -99)
                        {
                            dx = x;
                            dy = y;
                            grid[x][y] = GridPanel.DESTINATION;
                        }
                        else
                        {
                            // Draws old coordinates to blank
                            grid[dx][dy] = GridPanel.BLANK;
                                
                            // Stores new source coordinates
                            dx = x;
                            dy = y;
                            
                            // Draws new source point
                            grid[x][y] = GridPanel.DESTINATION;
                        }
                    }
                }
            }
            else
            {
                if(grid[x][y] == GridPanel.SOURCE)
                {
                    sx = -99;
                    sy = -99;
                }
                if(grid[x][y] == GridPanel.DESTINATION)
                {
                    dx = -99;
                    dy = -99;
                }
                grid[x][y] = GridPanel.BLANK;
                if(path != null)
                    path[x][y] = GridPanel.BLANK;
            }
            repaint();
        }
        
        @Override
        public void mouseDragged(MouseEvent e)
        {
            mouseReleased(e);
        }    
        
        @Override
        public void mousePressed(MouseEvent e)
        {
            // Get X and Y Coordinates
            x = calculateCordPos(e.getX());
            y = calculateCordPos(e.getY());
            
            if(grid[x][y] == GridPanel.SOURCE)
                settingsPanel.getComboBox().setSelectedIndex(0);

            if(grid[x][y] == GridPanel.DESTINATION)
                settingsPanel.getComboBox().setSelectedIndex(1);
            
            if(grid[x][y] == GridPanel.OBSTACLE)
                settingsPanel.getComboBox().setSelectedIndex(2);
        }

        @Override
        public void mouseClicked(MouseEvent e){}

        @Override
        public void mouseEntered(MouseEvent e){}

        @Override
        public void mouseExited(MouseEvent e){}

        @Override
        public void mouseMoved(MouseEvent e)
        {
            // Instantiate mazePanel and controlsPanel variables
            settingsPanel = ((SettingsPanel) getParent().getComponent(1));
            consolePanel = ((ConsolePanel) getParent().getComponent(2));
            
            // Update x and y coordinates 
            consolePanel.setLblX(e.getX());
            consolePanel.setLblY(e.getY());
        }
    
        public int calculateCordPos(int cord)
        {
            return (((cord + (rectSize - 1)) / rectSize) * rectSize - rectSize) / rectSize;
        }

        public int calculateCord(int cord)
        {
            return (((cord + (rectSize - 1)) / rectSize) * rectSize - rectSize);
        }
    }
    
    // Paint maze
    public void paintComponent(Graphics g1)
    {
        super.paintComponent(g1);
        Graphics2D g = (Graphics2D) g1;
        
        // Set color
        g.setColor(Color.BLACK);
        
        // Get maze measurements
        int mazeSize = (int) getSize().getWidth();
        
        // Iterate through each x, y position / draw maze
        for(int y = 0; y < mazeSize; y += rectSize)
            for(int x = 0; x < mazeSize; x += rectSize)
                g.drawRect(x, y, rectSize, rectSize);
        
        // Iterate through each x, y position / draw nodes
        for(int x = 0; x < grid.length; x++)
        {
            for(int y = 0; y < grid.length; y++)
            {
                if(grid[x][y] == GridPanel.SOURCE)
                    g.setColor(ConsolePanel.SOURCE);
                
                if(grid[x][y] == GridPanel.DESTINATION)
                    g.setColor(ConsolePanel.DESTINATION);
                
                if(grid[x][y] == GridPanel.OBSTACLE)
                    g.setColor(ConsolePanel.OBSTACLE);

                if(grid[x][y] == GridPanel.BLANK)
                    g.clearRect(x * rectSize + 1, y * rectSize + 1, rectSize - 1, rectSize - 1);
                else
                    g.fillRect(x * rectSize + 1, y * rectSize + 1, rectSize - 1, rectSize - 1);
            }
        }
        
        // Set color to path
        g.setColor(ConsolePanel.PATH);
        
        if(path == null)
            return;
        
        // paint path
        for(int x = 0; x < path.length; x++)
            for(int y = 0; y < path.length; y++)
                if(path[x][y] == GridPanel.PATH && grid[x][y] != GridPanel.SOURCE && grid[x][y] != GridPanel.DESTINATION && grid[x][y] != GridPanel.OBSTACLE)
                    g.fillRect(x * rectSize + 1, y * rectSize + 1, rectSize - 1, rectSize - 1);
    }
}