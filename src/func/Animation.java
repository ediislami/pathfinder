package func;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.Timer;

import GUI.MainContainer;
import GUI.GridPanel;

public class Animation implements ActionListener
{
    private GridPanel gridPanel;
    private ArrayList<Integer> nodes;
    private Timer timer;
    private int delay;
    
    public Animation(ArrayList<Integer> nodes)
    {
        this.gridPanel = ((GridPanel) MainContainer.WINDOW.getContentPane().getComponent(0));
        this.nodes = nodes;
    
        this.timer = new Timer(delay, this);
        this.timer.setInitialDelay(50);
        this.timer.start();
    }
    
    @Override
    public void actionPerformed(ActionEvent e)
    {
        if(nodes.isEmpty())
            return;
        
        int x = nodes.get(0) / gridPanel.getBound();
        int y = nodes.remove(0) - x * gridPanel.getBound();
        
        gridPanel.setPath(x, y);
        gridPanel.repaint();
    }
    
    public void setDelay(int delay)
    {
        timer.setDelay(30 - delay);
    }
    
    public void stop()
    {
        timer.stop();
    }
}