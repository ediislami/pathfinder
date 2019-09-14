package GUI;
import java.awt.Color;

import javax.swing.JFrame;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;

public class MainContainer
{
    public static JFrame WINDOW;
    private GridPanel gridPanel;
    private SettingsPanel settingsPanel;
    private ConsolePanel consolePanel;
    
    public MainContainer()
    {
        // Window set-up
        WINDOW = new JFrame("Path finder v3.3");
        WINDOW.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        WINDOW.getContentPane().setLayout(null);
            
        // Grid panel setup
        gridPanel = new GridPanel();
        gridPanel.setBounds(5, 14, 400, 400);
        WINDOW.getContentPane().add(gridPanel);
        
        // Settings panel setup
        settingsPanel = new SettingsPanel();
        settingsPanel.setBorder(new TitledBorder(new LineBorder(new Color(184, 207, 229)), " Settings ", TitledBorder.CENTER, TitledBorder.TOP, null, new Color(51, 51, 51)));
        settingsPanel.setBounds(420, 12, 223, 403);
        WINDOW.getContentPane().add(settingsPanel);
        
        // Console panel setup
        consolePanel = new ConsolePanel();
        consolePanel.setBounds(4, 420, 638, 176);
        consolePanel.setBorder(new TitledBorder(new LineBorder(new Color(184, 207, 229)), " Console ", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(51, 51, 51)));
        WINDOW.getContentPane().add(consolePanel);

        // show window
        WINDOW.setSize(650, 627);
        WINDOW.setLocationRelativeTo(null);
        WINDOW.setResizable(false);
        WINDOW.setVisible(true);
    }
    
    /**************************************************
     *               Getters and setters              *
     **************************************************/
    
    public JFrame getWindow()
    {
        return WINDOW;
    }
    
    public GridPanel getGridPanel()
    {
        return gridPanel;
    }
    
    public SettingsPanel getSettingsPanel()
    {
        return settingsPanel;
    }
    
    public ConsolePanel getConsolePanel()
    {
        return consolePanel;
    }
    
    public static void main(String[] args)
    {
        new MainContainer();
    }
}