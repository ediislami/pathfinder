package GUI;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JSlider;
import javax.swing.JTextField;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import func.Animation;
import func.FindPath;
import func.GenerateMaze;

@SuppressWarnings("serial")
public class SettingsPanel extends JPanel
{
    private GridPanel gridPanel;
    private Animation animation;
    private JSlider sldMazeSize;
    private JLabel lblSize;
    private JLabel lblCursorMode;
    private ButtonGroup btnGrAddorRemove;
    private JRadioButton rdbtnAdd;
    private JRadioButton rdbtnRemove;
    private JComboBox<String> cboxOptions;
    private JComboBox<String> cboxAlgs;
    private JButton btnClear;
    private JLabel lblSelectAlg;
    private JLabel lblResolveType;
    private JRadioButton rdbtnInstant;
    private JRadioButton rdbtnAnimation;
    private ButtonGroup btnGrResType;
    private JSlider sldAnimationSpeed;
    private JLabel lblIntensity;
    private JTextField txtIntensity;
    private JCheckBox chckbRndDest;
    private JButton btnGenerateMaze;
    private JButton btnRun;

    public SettingsPanel()
    {
        // Set panel variables
        lblSize = new JLabel("Size: ");
        sldMazeSize = new JSlider(10, 100);
        lblCursorMode = new JLabel("Cursor mode: ");
        cboxOptions = new JComboBox<String>();
        rdbtnAdd = new JRadioButton("Add");
        rdbtnRemove = new JRadioButton("Remove");
        btnGrAddorRemove = new ButtonGroup();
        btnClear = new JButton("Clear");
        lblSelectAlg = new JLabel("Select algorithm:");
        cboxAlgs = new JComboBox<String>();
        lblResolveType = new JLabel("Resolve type: ");
        rdbtnInstant = new JRadioButton("Instant");
        rdbtnAnimation = new JRadioButton("Animation");
        btnGrResType = new ButtonGroup();
        sldAnimationSpeed = new JSlider(5, 30);
        lblIntensity = new JLabel("Intensity: ");
        txtIntensity = new JTextField();
        chckbRndDest = new JCheckBox("Random destination");
        btnGenerateMaze = new JButton("Generate");
        btnRun = new JButton("Run");
        
        // Set layout to absolute 
        setLayout(null);

        // Set sldMazeSize cursor to Hand Cursor
        sldMazeSize.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        
        // Set Add button default mode to selected
        rdbtnAdd.setSelected(true);
        
        // Add radio buttons (Add & Remove) to button group
        btnGrAddorRemove.add(rdbtnAdd);
        btnGrAddorRemove.add(rdbtnRemove);
        
        // Add Items to combobox Options
        cboxOptions.addItem("Source");
        cboxOptions.addItem("Destination");
        cboxOptions.addItem("Obstacle");
        
        // Add Items to combobox Algs
        cboxAlgs.addItem("DFS");
        cboxAlgs.addItem("BFS");
        cboxAlgs.addItem("Dijkstra SP");
        cboxAlgs.addItem("BellmanFord SP");
        
        // Set instant button default mode to selected
        rdbtnInstant.setSelected(true);
        
        // Add radio buttons (Instant & Resolve) to button group
        btnGrResType.add(rdbtnInstant);
        btnGrResType.add(rdbtnAnimation);
        
        // Set sldAnimationSpeed cursor to Hand Cursor & set state to disabled
        sldAnimationSpeed.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        sldAnimationSpeed.setEnabled(false);        
        
        // Set text intensity margin
        txtIntensity.setForeground(Color.GRAY);
        txtIntensity.setText("20-50");
        txtIntensity.setMargin(new Insets(0, 5, 0, 0));
        
        /**************************************************
         *                 Mouse listeners                *
         **************************************************/
        
        // Mouse listener to instantiate mazePanel variable
        this.addMouseMotionListener(new MouseMotionAdapter() 
        {
            @Override
            public void mouseMoved(MouseEvent e) 
            {
                gridPanel = ((GridPanel) getParent().getComponent(0));
            }
        });
        
        // Mouse listener to change maze size
        sldMazeSize.addChangeListener(new ChangeListener()
        {
            @Override
            public void stateChanged(ChangeEvent e)
            {
                int sliderVal = sldMazeSize.getValue();

                int mazeSize = (int) gridPanel.getPreferredSize().getWidth();
                
                if(sliderVal != 0 && mazeSize % sliderVal == 0)
                    gridPanel.setMazeSize(sliderVal);                
                
                gridPanel.repaint();                
            }
        });
        
        // Mouse listener for clearing the maze
        btnClear.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                // Set all nodes of grid to BLANK 
                gridPanel.reset();
                
                // Repaints the panel
                gridPanel.repaint();
            }
        });
        
        // Change listener for button remove
        rdbtnRemove.addChangeListener(new ChangeListener()
        {
            @Override
            public void stateChanged(ChangeEvent e)
            {
                if(rdbtnRemove.isSelected())
                    cboxOptions.setEnabled(false);
                else
                    cboxOptions.setEnabled(true);
            }
        });
        
        // Change listener for button remove
        rdbtnAnimation.addChangeListener(new ChangeListener()
        {
            @Override
            public void stateChanged(ChangeEvent e)
            {
                if(rdbtnAnimation.isSelected())
                    sldAnimationSpeed.setEnabled(true);
                else
                    sldAnimationSpeed.setEnabled(false);
            }
        });
        
        // Listener for txtIntensity
        txtIntensity.addFocusListener(new FocusListener()
        {
            @Override
            public void focusGained(FocusEvent e)
            {
                if(txtIntensity.getText().equals("20-50"))
                {
                    txtIntensity.setForeground(Color.BLACK);                    
                    txtIntensity.setText("");
                }
            }

            @Override
            public void focusLost(FocusEvent e)
            {
                if(txtIntensity.getText().isEmpty())
                {
                    txtIntensity.setForeground(Color.GRAY);
                    txtIntensity.setText("20-50");                    
                }
                
                if(!txtIntensity.getText().matches("([2-4][0-9]|50)"))
                {
                    txtIntensity.setForeground(Color.GRAY);
                    txtIntensity.setText("20-50");
                }
            }
        });
        
        // Mouse listener for button Generate
        btnGenerateMaze.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                if(animation != null)
                {
                    animation.stop();
                    animation = null;
                }
                
                gridPanel.setPath(new int[40][40]);
                
                int intensity = 0;
                if(txtIntensity.getText().equals("20-50"))
                    intensity = 30;
                else
                    intensity = Integer.parseInt(txtIntensity.getText());
                GenerateMaze gm = new GenerateMaze(gridPanel.getBound(), intensity, chckbRndDest.isSelected());
                
                if(gm.isFinished())
                {
                    gridPanel.setGrid(gm.getGenGrid());          
                    gridPanel.repaint();
                }
            }
        });
        
        
        // Mouse listener for button Run
        btnRun.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {   
                gridPanel.setPath(new int[40][40]);
                FindPath fp = null;
                
                if(rdbtnInstant.isSelected())
                {
                    fp = new FindPath(gridPanel.getGrid(), gridPanel.getBound(), cboxAlgs.getSelectedItem().toString());
                }
                else
                {
                    fp = new FindPath(gridPanel.getGrid(), gridPanel.getBound(), cboxAlgs.getSelectedItem().toString(), "Animation");
                    
                    animation = new Animation(fp.getNodes(cboxAlgs.getSelectedItem().toString()));
                    animation.setDelay(sldAnimationSpeed.getValue());    
           
                    // Change listener for slider animation speed
                    sldAnimationSpeed.addChangeListener(new ChangeListener()
                    {
                        public void stateChanged(ChangeEvent e)
                        {
                            if(animation != null)
                                animation.setDelay(sldAnimationSpeed.getValue());
                        }
                    });
                }
                gridPanel.repaint();
            }
        });
        
        // Add hint text to components
        sldMazeSize.        setToolTipText("Maze size...");
        rdbtnAdd.           setToolTipText("Add nodes to maze...");
        rdbtnRemove.        setToolTipText("Remove nodes from maze...");
        btnClear.           setToolTipText("Clear maze...");
        rdbtnInstant.       setToolTipText("Reveal path instantly...");
        rdbtnAnimation.     setToolTipText("Reveal path sequentially...");
        sldAnimationSpeed.  setToolTipText("Animation speed...");
        txtIntensity.       setToolTipText("Obstacle intensity...");
        chckbRndDest.       setToolTipText("Generate random destination point...");
        btnGenerateMaze.    setToolTipText("Generate random maze...");
        
        // Set components bounds
        sldMazeSize.        setBounds(9, 45, 200, 16);
        lblSize.            setBounds(14, 23, 39, 15);
        lblCursorMode.      setBounds(14, 74, 99, 15);
        rdbtnAdd.           setBounds(10, 95, 52, 23);
        rdbtnRemove.        setBounds(66, 95, 99, 23);
        cboxOptions.        setBounds(15, 125, 120, 20);
        btnClear.           setBounds(140, 125, 74, 20);
        lblSelectAlg.       setBounds(14, 160, 130, 20);
        cboxAlgs.           setBounds(15, 185, 140, 20);
        lblResolveType.     setBounds(14, 220, 100, 23);
        rdbtnInstant.       setBounds(10, 243, 77, 23);
        rdbtnAnimation.     setBounds(92, 243, 99, 23);
        sldAnimationSpeed.  setBounds(9, 270, 185, 16);
        lblIntensity.       setBounds(14, 295, 185, 16);
        txtIntensity.       setBounds(15, 315, 53, 25);
        chckbRndDest.       setBounds(12, 345, 180, 20);
        btnRun.             setBounds(140, 375, 74, 20);
        btnGenerateMaze.    setBounds(15, 375, 110, 20);
        
        // Add components to panel
        add(lblSize);
        add(sldMazeSize);
        add(lblCursorMode);
        add(rdbtnAdd);
        add(rdbtnRemove);
        add(cboxOptions);
        add(btnClear);
        add(lblSelectAlg);
        add(cboxAlgs);
        add(lblResolveType);
        add(rdbtnInstant);
        add(rdbtnAnimation);
        add(sldAnimationSpeed);
        add(lblIntensity);
        add(txtIntensity);
        add(chckbRndDest);
        add(btnRun);
        add(btnGenerateMaze);
    }

    /**************************************************
     *                    Getters                     *
     **************************************************/
    public JComboBox<String> getComboBox()
    {
        return cboxOptions;
    }

    public JRadioButton getRdbtnAdd()
    {
        return rdbtnAdd;
    }
}