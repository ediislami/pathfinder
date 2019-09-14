package GUI;
import java.awt.Color;
import java.awt.Desktop;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.io.File;
import java.io.IOException;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.border.CompoundBorder;

@SuppressWarnings("serial")
public class ConsolePanel extends JPanel
{
    private final static String clog_header = "Pathfinder v3.3 by Entor Arifi\n-----------------------------------------\n";
    
    public static Color SOURCE = Color.GREEN, DESTINATION = Color.RED, OBSTACLE = Color.BLACK, PATH = Color.ORANGE;

    private GridPanel gridPanel;
    private SettingsPanel settingsPanel;
    private JTextArea txtConsoleLog;
    private JScrollPane cLogScrollBar;
    private JLabel lblX;
    private JLabel lblY;
    private JButton btnClearCon;
    private JButton btnAbout;
    
    public ConsolePanel()
    {
        // Set panel variables
        txtConsoleLog = new JTextArea();
        lblX = new JLabel("X pos: 0");
        lblY = new JLabel("Y pos: 0");
        btnClearCon = new JButton("Clear log");
        btnAbout = new JButton("About");
        
        // Set layout to absolute
        setLayout(null);

        // Console log settings
        txtConsoleLog.setEditable(false);
        txtConsoleLog.setLineWrap(true);
        txtConsoleLog.setWrapStyleWord(true);
        txtConsoleLog.setFont(new Font("Lucida Console", Font.PLAIN, 14));
        txtConsoleLog.setText(clog_header);
        txtConsoleLog.setBorder(new CompoundBorder(BorderFactory.createLineBorder(Color.BLACK), BorderFactory.createEmptyBorder(5, 5, 0, 0)));
        
        // Add scroll bar to console Log
        cLogScrollBar = new JScrollPane(txtConsoleLog, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        cLogScrollBar.setBounds(new Rectangle(10, 20, 400, 150));
       
        // X pos label Settings
        lblX.setFont(new Font("Arial", Font.PLAIN, 12));
        
        // Y pos label Settings
        lblY.setFont(new Font("Arial", Font.PLAIN, 12));
        
        
        /**************************************************
         *                 Mouse listeners                *
         **************************************************/
        
        // Listener to instantiate mazePanel and controlsPanel variable
        this.addMouseMotionListener(new MouseMotionAdapter() 
        {
            @Override
            public void mouseMoved(MouseEvent e) 
            {
                gridPanel = ((GridPanel) getParent().getComponent(0));
                settingsPanel = ((SettingsPanel) getParent().getComponent(1));
            }
        });
        
        // Mouse listener for color-choosers
        this.addMouseListener(new MouseAdapter() 
        {
            @Override
            public void mouseClicked(MouseEvent e) 
            {
                // Source color chooser
                if((e.getX() >= 423 && e.getX() <= 493) && (e.getY() >= 20 && e.getY() <= 33))
                {
                    Color temp = JColorChooser.showDialog(null, "Choose color: ", SOURCE);
                    SOURCE = checkColor(temp) ? SOURCE : temp;
                    settingsPanel.getComboBox().setSelectedIndex(0);
                }
                
                // Destination color chooser
                if((e.getX() >= 520 && e.getX() <= 620) && (e.getY() >= 20 && e.getY() <= 33))
                {
                    Color temp = JColorChooser.showDialog(null, "Choose color: ", DESTINATION);
                    DESTINATION = checkColor(temp) ? DESTINATION : temp;
                    settingsPanel.getComboBox().setSelectedIndex(1);
                }
                    
                // Obstacle color chooser
                if((e.getX() >= 423 && e.getX() <= 493) && (e.getY() >= 40 && e.getY() <= 53))
                {
                    Color temp = JColorChooser.showDialog(null, "Choose color: ", OBSTACLE);
                    OBSTACLE = checkColor(temp) ? OBSTACLE : temp;
                    settingsPanel.getComboBox().setSelectedIndex(2);
                }
                
                // Path color chooser
                if((e.getX() >= 520 && e.getX() <= 575) && (e.getY() >= 40 && e.getY() <= 53))
                {
                    Color temp = JColorChooser.showDialog(null, "Choose color: ", OBSTACLE);
                    PATH = checkColor(temp)? PATH : temp;
                }
                    
                // repaints maze
                gridPanel.repaint();
                
                // repaints bottom panel
                repaint();
            }
        });

        // Mouse listener to clear the console
        btnClearCon.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                txtConsoleLog.setText(clog_header);
            }
        });
        
        btnAbout.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                    Desktop d = Desktop.getDesktop();
                    try
                    {
                        d.open(new File("readme.txt"));
                    }
                    catch(IOException e1)
                    {
                        e1.printStackTrace();
                    }
            }
        });
                
        // Set components bounds
        txtConsoleLog.  setBounds(10, 20, 400, 150);
        cLogScrollBar.  setBounds(12, 22, 403, 147);
        lblX.           setBounds(424, 65, 67, 15);
        lblY.           setBounds(522, 65, 67, 15);
        btnAbout.       setBounds(532, 147, 97, 20);
        btnClearCon.    setBounds(423, 147, 97, 20);
        
        // Add components to panel
        add(cLogScrollBar);
        add(lblX);
        add(lblY);
        add(btnClearCon);
        add(btnAbout);
    }
    
    
    /**************************************************
     *                    Setters                     *
     **************************************************/
    public void setLblX(int x)
    {
        this.lblX.setText("X pos: " + x);;
    }

    public void setLblY(int y)
    {
        this.lblY.setText("Y pos: " + y);
    }
    
    public void setText(String text)
    {
        txtConsoleLog.setText(txtConsoleLog.getText() + text);
    }
    
    // Checks if two same color are the same 
    public boolean checkColor(Color c)
    {
        if(c == null)
            return true;
        if(c.equals(ConsolePanel.SOURCE))
            return true;
        if(c.equals(ConsolePanel.DESTINATION))
            return true;
        if(c.equals(ConsolePanel.OBSTACLE))
            return true;
        if(c.equals(ConsolePanel.PATH))
            return true;
        return false;
    }
    
    // Paint panel
    public void paintComponent(Graphics g1)
    {
        super.paintComponent(g1);
        
        // Cast graphics to 2D Graphics 
        Graphics2D g = (Graphics2D) g1;
        
        g.setColor(SOURCE);
        g.fillRect(423, 20, 13, 13);
        
        g.setColor(DESTINATION);
        g.fillRect(520, 20, 13, 13);
        
        g.setColor(OBSTACLE);
        g.fillRect(423, 40, 13, 13);
        
        g.setColor(PATH);
        g.fillRect(520, 40, 13, 13);
        
        
        g.setColor(Color.BLACK);
        g.setFont(new Font("Times New Roman", Font.PLAIN, 12));
        g.drawString("- Source", 440, 31);
        g.drawString("- Destination", 537, 31);
        g.drawString("- Obstacle", 440, 51);
        g.drawString("- Path", 537, 51);
    }    
}