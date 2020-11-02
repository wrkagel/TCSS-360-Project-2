package view;

/*
TCSS 360 Project #2
Group 8
RJ Alabado, Walter Kagel, Taehong Kim
 */

/**
 * Builds and displays the GUI. Informs of user actions using the ViewListener.
 * @author Group 8, Lead: Taehong Kim
 * @version 10/25/2020
 */
import control.ViewListener;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.*;
import java.util.Scanner;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;
import javax.swing.border.TitledBorder;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.filechooser.FileSystemView;

/**
 *
 */
public class GUI extends JFrame implements ActionListener{
    /**SerialVersion for GUI class.**/
    private static final long serialVersionUID = 1L;
    /**Setting Object code text area.**/
    private JTextArea ObjCode;
    /**Setting Main frame.**/
    private JFrame frame = new JFrame();
    /**Setting up-Panel.**/
    private JPanel upPanel = new JPanel();
    /**Setting Line Start Panel.**/
    private JPanel lineStartPanel = new JPanel();
    /**Setting Center main Panel.**/
    private JPanel CenterPanel;
    /**Setting font type for titles.**/
    private Font myFont = new Font("Plain", Font.BOLD, 17);
    /**Setting font types for inside of text area.**/
    private Font myFont2 = new Font("Plain", Font.CENTER_BASELINE,9);
    /**Setting CPU panel.**/
    private JPanel CpuPanel;
    /**Setting top of CPU panel.**/
    private JPanel CpupanelTop;
    /**Setting N check button.**/
    private JLabel Nmark;
    /**Setting Z check button.**/
    private JLabel Zmark;
    /**Setting V check button.**/
    private JLabel Vmark;
    /**Setting C check button.**/
    private JLabel Cmark;
    /**Setting Accumulator label.**/
    private JLabel Accumulator;
    /**Setting Accumulator text area.**/
    private JTextField Accumulatorout1;
    /**Setting Accumulator second text area.**/
    private JTextField Accumulatorout2;
    /**Setting Program Counter Label.**/
    private JLabel ProgramCounter;
    /**Setting program counter text area.**/
    private JTextField ProgramCounterout1;
    /**Setting program counter text second area.**/
    private JTextField ProgramCounterout2;
    /**Setting Instruction Label.**/
    private JLabel Instruction;
    /**Setting Instruction text area.**/
    private JTextField Instructionout1;
    /**Setting Instruction text second area.**/
    private JTextField Instructionout2;
    /**Setting Tabbed panel for batch and terminal.**/
    private JTabbedPane tabbedPane2;
    /**Setting Batch IO text area.**/
    private JTextArea BatchIO;
    /**Setting Terminal text area.**/
    private JTextArea Terminal;
    /**Setting Output text area.**/
    private JTextArea Outputtext;
    /**Setting Line-End main Panel.**/
    private JPanel LineEndPanel;
    /**Setting Memory Panel.**/
    private JTextArea Memory;
    /**Setting Scroll for memory panel.**/
    private JScrollPane scroll;
    /**Setting main Menu bar.**/
    private JMenuBar menuBar;
    /**Setting sub-menw for new button.**/
    private JMenuItem newMenu;
    /**Setting File menu.**/
    private JMenu File;
    /**Setting edit menu.**/
    private JMenu Edit;
    /**Setting cut sub-menu.**/
    private JMenuItem cutMenu;
    /**Setting paste sub-menu.**/
    private JMenuItem pasteMenu;
    /**Setting Build menu.**/
    private JMenu Build;
    /**Setting Load menu.**/
    private JMenuItem loadMenu;
    /**Setting Run menu**/
    private JMenuItem runMenu;
    /**Setting open sub-menu.**/
    private JMenuItem openMenu;
    /**Setting tab panel.**/
    private JTabbedPane tabbedPane;
    /**Setting source code tab with text area.**/
    private JTextArea sourceTab;
    /**Setting trace tab with text area.**/
    private JTextArea traceTab;
    /**Setting Text area for Aslisting.**/
    private JTextArea AsListing;
    /**Setting down of cpu panel.**/
    private JPanel Cpupaneldown;
    /**Setting Stack pointer labels.**/
    private JLabel StackPointer;
    /**Setting Stack pointer text areas1.**/
    private JTextField StackPointer1;
    /**Setting Stack pointer text areas1.**/
    private JTextField StackPointer2;
    /**Setting Index Register Label.**/
    private JLabel IndexRegister;
    /**Setting Index Register text areas1.**/
    private JTextField IndexRegister1;
    /**Setting Index Register text areas2.**/
    private JTextField IndexRegister2;
    /**Setting Operand label.**/
    private JLabel Operand;
    /**Setting Operand text field1.**/
    private JTextField Operand1;
    /**Setting Operand text field2.**/
    private JTextField Operand2;
    /**Setting Operand specifier label.**/
    private JLabel OperandSpecifier;
    /**Setting Operand specifier text field1.**/
    private JTextField OperandSpecifier1;
    /**Setting Operand specifier text field2.**/
    private JTextField OperandSpecifier2;
    /**Setting words scanner to scanner result.**/
    private Scanner wordsScanner;
    /**Setting screensize to calculate size of application**/
    private Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
    private JButton TraceButton;
    private JButton TraceButton2;
    private JCheckBox TraceChecker;
    private ViewListener ViewListner;

    public static void main(String []args) {
        new GUI();
    }

    /**
     * Constructor Virtual simulator GUI with frame.
     */
    public GUI() {
        frame = new JFrame("Virtual Simulator");
        frame.setSize(screenSize.width*3/4,screenSize.height*3/4); // frame size
        frame.setVisible(true);
        frame.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        });
        MenuBar();
        frame.setJMenuBar(menuBar);
        UpPanel();
        frame.getContentPane().add(upPanel, BorderLayout.PAGE_START);
        LineStartPanel();
        frame.getContentPane().add(lineStartPanel, BorderLayout.LINE_START);
        CentralPanel();
        frame.getContentPane().add(CenterPanel, BorderLayout.CENTER);
        LineEndPanel();
        frame.getContentPane().add(LineEndPanel, BorderLayout.LINE_END);
        frame.pack();

    }

    /**
     * Constructor Up-Panel with buttons.
     */
    private void UpPanel() {
        /*setting main UpPanel*/
        upPanel = new JPanel();
        upPanel.setLayout(new GridLayout(1,7));
        upPanel.setPreferredSize(new Dimension (screenSize.width*3/4,screenSize.height*3/50));
        /*setting buttons on main upPanel*/
        final JButton newButton = new JButton("New");
        newButton.setFont(myFont);
        newButton.setBackground(Color.lightGray);
        newButton.addActionListener(this);
        newButton.setPreferredSize(new Dimension (screenSize.width/12,screenSize.height*3/50));

        JButton startButton = new JButton("Start Debugging");
        startButton.setFont(myFont);
        startButton.setBackground(Color.lightGray);
        startButton.addActionListener(this);
        startButton.setPreferredSize(new Dimension (screenSize.width/12,screenSize.height*3/50));

        JButton saveButton = new JButton("Save");
        saveButton.setBackground(Color.lightGray);
        saveButton.addActionListener(this);
        saveButton.setFont(myFont);
        saveButton.setPreferredSize(new Dimension (screenSize.width/12,screenSize.height*3/50));

        JButton runCodeButton = new JButton("Run Source");
        runCodeButton.setBackground(Color.lightGray);
        runCodeButton.addActionListener(this);
        runCodeButton.setFont(myFont);
        runCodeButton.setPreferredSize(new Dimension (screenSize.width/12,screenSize.height*3/50));

        JButton assembleButton = new JButton("Assemble");
        assembleButton.setBackground(Color.lightGray);
        assembleButton.addActionListener(this);
        assembleButton.setFont(myFont);
        assembleButton.setPreferredSize(new Dimension (screenSize.width/12,screenSize.height*3/50));

        /*Setting empty panels to fill empty areas of upPanel*/
        JPanel emptyPanel = new JPanel();
        emptyPanel.setLayout(new GridLayout(1,1));
        emptyPanel.setPreferredSize(new Dimension (screenSize.width/12,screenSize.height*3/50));
        JPanel emptyPanel2 = new JPanel();
        emptyPanel2.setLayout(new GridLayout(1,1));
        emptyPanel2.setPreferredSize(new Dimension (screenSize.width/12,screenSize.height*3/50));

        /*Adding whole buttons in UpPanel*/
        upPanel.add(newButton);upPanel.add(runCodeButton); upPanel.add(saveButton);
        upPanel.add(startButton);upPanel.add(assembleButton);
        upPanel.add(emptyPanel);upPanel.add(emptyPanel2);
    }

    /**
     * Constructor for line start Panel with components small panels.
     */
    private void LineStartPanel () {
        /*setting main Line Start Panel*/
        lineStartPanel = new JPanel();
        lineStartPanel.setLayout(new GridLayout(3,1));
        lineStartPanel.setBorder(new TitledBorder(null, "CODE", TitledBorder.CENTER, TitledBorder.TOP, myFont, null));
        lineStartPanel.setPreferredSize(new Dimension(screenSize.width/4,screenSize.height*9/14));
        /*Create first sub-panel with Tabbedpane*/
        tabbedPane = new JTabbedPane();
        tabbedPane.setSize(screenSize.width/4,screenSize.height*3/14);
        tabbedPane.setFont(myFont);

        /*Setting source code and trace tabs with text areas and add to main Line Start Panel*/
        sourceTab = new JTextArea("Please type your Souce Code here",screenSize.width/4,screenSize.height*3/14);
        sourceTab.setFont(myFont);
        traceTab = new JTextArea(null,screenSize.width/4,screenSize.height*3/14);
        traceTab.setEditable(false);
        tabbedPane.addTab("Code", sourceTab); tabbedPane.addTab("Trace", traceTab);
        lineStartPanel.add(tabbedPane);

        /*Setting object code text areas and add to main Line Start Panel*/
        ObjCode = new JTextArea(null,screenSize.width/4,screenSize.height*3/14);
        ObjCode.setFont(myFont);
        ObjCode.setBorder(new TitledBorder(null, "Object Code", TitledBorder.CENTER, TitledBorder.TOP, myFont, null));
        ObjCode.setLineWrap(true);
        lineStartPanel.add(ObjCode);

        /*Setting Aslisting text areas and add to main Line Start Panel*/
        AsListing = new JTextArea(null,screenSize.width/4,screenSize.height*3/14);
        AsListing.setFont(myFont);
        AsListing.setEditable(false);
        AsListing.setBorder(new TitledBorder(null, "Assembler Listing", TitledBorder.CENTER, TitledBorder.TOP, myFont, null));
        lineStartPanel.add(AsListing);
    }

    /**
     * Constructor for main Central Panel with component small panels.
     */
    private void CentralPanel() {
        /*setting main Center Panel*/
        CenterPanel = new JPanel();
        CenterPanel.setLayout(new GridLayout(3,1));
        CenterPanel.setBorder(new TitledBorder(null, "CPU", TitledBorder.CENTER, TitledBorder.TOP, myFont, null));
        CenterPanel.setPreferredSize(new Dimension(screenSize.width/4,screenSize.height*9/14));
        /*setting cpu Center sub Panel*/
        CpuPanel = new JPanel();
        CpuPanel.setLayout(new GridLayout(2,1));
        CpuPanel.setBorder(new TitledBorder(null, null, TitledBorder.CENTER, TitledBorder.TOP, myFont, null));
        CpuPanel.setPreferredSize(new Dimension(screenSize.width/4,screenSize.height*3/14));
        /*setting  top Panel*/
        CpupanelTop = new JPanel();
        CpupanelTop.setLayout(new GridLayout(1,9));
        CpupanelTop.setSize(screenSize.width/4,screenSize.height*1/112);
        //setPreferredSize(new Dimension(screenSize.width/4,screenSize.height*1/112));
        /*Calling components for buttons, labels, and text areas.*/
        CpuChecker();
        downCpuPanel();

        AccumulatorPanel();
        IndexRegister();
        StackPointer();
        ProgramCounterPanel();
        InstructionPanel();
        OperandSpecifier();
        Operand();
        BatchIO();
    }

    /**
     * Constructor N,Z,V,C checker with labels for cpu panel.
     */
    private void CpuChecker() {
        /*adding check box with labels N.*/
        Nmark = new JLabel("N");
        Nmark.setFont(myFont);
        Nmark.setSize(new Dimension(screenSize.width*1/112,screenSize.height*1/112));
        CpupanelTop.add(Nmark);
        JCheckBox Nbox = new JCheckBox();
        Nbox.setEnabled(false);
        Nbox.setSize(new Dimension(screenSize.width*1/112,screenSize.height*1/112));
        CpupanelTop.add(Nbox);

        /*adding check box with labels Z.*/
        Zmark = new JLabel("Z");
        Zmark.setFont(myFont);
        Zmark.setSize(new Dimension(screenSize.width*1/112,screenSize.height*1/112));
        CpupanelTop.add(Zmark);
        JCheckBox Zbox = new JCheckBox();
        Zbox.setEnabled(false);
        Zbox.setSize(new Dimension(screenSize.width*1/112,screenSize.height*1/112));
        CpupanelTop.add(Zbox);

        /*adding check box with labels V.*/
        Vmark = new JLabel("V");
        Vmark.setFont(myFont);
        Vmark.setSize(new Dimension(screenSize.width*1/112,screenSize.height*1/112));
        CpupanelTop.add(Vmark);
        JCheckBox Vbox = new JCheckBox();
        Vbox.setEnabled(false);
        Vbox.setSize(new Dimension(screenSize.width*1/112,screenSize.height*1/112));
        CpupanelTop.add(Vbox);

        /*adding check box with labels C.*/
        Cmark = new JLabel("C");
        Cmark.setFont(myFont);
        Cmark.setSize(new Dimension(screenSize.width*1/112,screenSize.height*1/112));
        CpupanelTop.add(Cmark);
        JCheckBox Cbox = new JCheckBox();
        Cbox.setEnabled(false);
        Cbox.setSize(new Dimension(screenSize.width*1/112,screenSize.height*1/112));
        CpupanelTop.add(Cbox);
        CpuPanel.add(CpupanelTop,  BorderLayout.NORTH);
    }

    /**
     * Constructor for down of cpu penl in main cpu penl.
     */
    private void downCpuPanel() {
        /*Setting down of CPU panel*/
        Cpupaneldown = new JPanel();
        Cpupaneldown.setLayout(new GridLayout(8,3));
        Cpupaneldown.setPreferredSize(new Dimension(screenSize.width/4,screenSize.height*3/16));
        CpuPanel.add(Cpupaneldown, BorderLayout.NORTH);
    }

    /**
     * Constructor for Accumulator labels, text areas.
     */
    private void AccumulatorPanel() {
        Accumulator = new JLabel("Accumulator");
        Accumulator.setFont(myFont2);
        Accumulator.setPreferredSize(new Dimension(screenSize.width/12,screenSize.height*3/112));
        Cpupaneldown.add(Accumulator);
        Accumulatorout1 = new JTextField();
        Accumulatorout1.setEditable(false);
        Accumulatorout1.setPreferredSize(new Dimension(screenSize.width/12,screenSize.height*3/112));
        Accumulatorout1.setFont(myFont2);
        Cpupaneldown.add(Accumulatorout1);
        Accumulatorout2 = new JTextField();
        Accumulatorout2.setEditable(false);
        Accumulatorout2.setPreferredSize(new Dimension(screenSize.width/12,screenSize.height*3/112));
        Accumulatorout2.setFont(myFont2);
        Cpupaneldown.add(Accumulatorout2);
    }

    /**
     * Constructor for ProgramCounter labels, text areas.
     */
    private void ProgramCounterPanel() {
        ProgramCounter = new JLabel("ProgramCounter");
        ProgramCounter.setFont(myFont2);
        ProgramCounter.setPreferredSize(new Dimension(screenSize.width/12,screenSize.height*3/112));
        Cpupaneldown.add(ProgramCounter);
        ProgramCounterout1 = new JTextField();
        ProgramCounterout1.setEditable(false);
        ProgramCounterout1.setPreferredSize(new Dimension(screenSize.width/12,screenSize.height*3/112));
        ProgramCounterout1.setFont(myFont2);
        Cpupaneldown.add(ProgramCounterout1);
        ProgramCounterout2 = new JTextField();
        ProgramCounterout2.setEditable(false);
        ProgramCounterout2.setPreferredSize(new Dimension(screenSize.width/12,screenSize.height*3/112));
        ProgramCounterout2.setFont(myFont2);
        Cpupaneldown.add(ProgramCounterout2);
    }

    /**
     * Constructor for StackPointer labels, text areas.
     */
    private void StackPointer() {
        StackPointer = new JLabel("StackPointer");
        StackPointer.setFont(myFont2);
        StackPointer.setPreferredSize(new Dimension(screenSize.width/12,screenSize.height*3/112));
        Cpupaneldown.add(StackPointer);
        StackPointer1 = new JTextField();
        StackPointer1.setEditable(false);
        StackPointer1.setPreferredSize(new Dimension(screenSize.width/12,screenSize.height*3/112));
        StackPointer1.setFont(myFont2);
        Cpupaneldown.add(StackPointer1);
        StackPointer2 = new JTextField();
        StackPointer2.setEditable(false);
        StackPointer2.setPreferredSize(new Dimension(screenSize.width/12,screenSize.height*3/112));
        StackPointer2.setFont(myFont2);
        Cpupaneldown.add(StackPointer2);
    }

    /**
     * Constructor for Index register labels, text areas.
     */
    private void IndexRegister() {
        IndexRegister = new JLabel("IndexRegister");
        IndexRegister.setFont(myFont2);
        IndexRegister.setPreferredSize(new Dimension(screenSize.width/12,screenSize.height*3/112));
        Cpupaneldown.add(IndexRegister);
        IndexRegister1 = new JTextField();
        IndexRegister1.setEditable(false);
        IndexRegister1.setPreferredSize(new Dimension(screenSize.width/12,screenSize.height*3/112));
        IndexRegister1.setFont(myFont2);
        Cpupaneldown.add(IndexRegister1);
        IndexRegister2 = new JTextField();
        IndexRegister2.setEditable(false);
        IndexRegister2.setPreferredSize(new Dimension(screenSize.width/12,screenSize.height*3/112));
        IndexRegister2.setFont(myFont2);
        Cpupaneldown.add(IndexRegister2);
    }

    /**
     * Constructor for Operand labels, text areas.
     */
    private void Operand() {
        Operand = new JLabel("(Operand)");
        Operand.setFont(myFont2);
        Operand.setPreferredSize(new Dimension(screenSize.width/12,screenSize.height*3/112));
        Cpupaneldown.add(Operand);
        Operand1 = new JTextField();
        Operand1.setEditable(false);
        Operand1.setPreferredSize(new Dimension(screenSize.width/12,screenSize.height*3/112));
        Operand1.setFont(myFont2);
        Cpupaneldown.add(Operand1);
        Operand2 = new JTextField();
        Operand2.setEditable(false);
        Operand2.setPreferredSize(new Dimension(screenSize.width/12,screenSize.height*3/112));
        Operand2.setFont(myFont2);
        Cpupaneldown.add(Operand2);
    }


    /**
     * Constructor for Operand specifier labels, text areas.
     */
    private void OperandSpecifier() {
        OperandSpecifier = new JLabel("Operand Specifier");
        OperandSpecifier.setFont(myFont2);
        OperandSpecifier.setPreferredSize(new Dimension(screenSize.width/12,screenSize.height*3/112));
        Cpupaneldown.add(OperandSpecifier);
        OperandSpecifier1 = new JTextField();
        OperandSpecifier1.setEditable(false);
        OperandSpecifier1.setPreferredSize(new Dimension(screenSize.width/12,screenSize.height*3/112));
        OperandSpecifier1.setFont(myFont2);
        Cpupaneldown.add(OperandSpecifier1);
        OperandSpecifier2 = new JTextField();
        OperandSpecifier2.setEditable(false);
        OperandSpecifier2.setPreferredSize(new Dimension(screenSize.width/12,screenSize.height*3/112));
        OperandSpecifier2.setFont(myFont2);
        Cpupaneldown.add(OperandSpecifier2);
    }


    /**
     * Constructor for InstructionPanel labels, text areas.
     */
    private void InstructionPanel() {
        Instruction = new JLabel("Instruction Specifier");
        Instruction.setFont(myFont2);
        Instruction.setPreferredSize(new Dimension(screenSize.width/12,screenSize.height*3/112));
        Cpupaneldown.add(Instruction);
        Instructionout1 = new JTextField();
        Instructionout1.setEditable(false);
        Instructionout1.setPreferredSize(new Dimension(screenSize.width/12,screenSize.height*3/112));
        Instructionout1.setFont(myFont2);
        Cpupaneldown.add(Instructionout1);
        Instructionout2 = new JTextField();
        Instructionout2.setEditable(false);
        Instructionout2.setPreferredSize(new Dimension(screenSize.width/12,screenSize.height*3/112));
        Cpupaneldown.add(Instructionout2);
        CpuPanel.add(Cpupaneldown);
        CenterPanel.add(CpuPanel, BorderLayout.SOUTH);
    }

    /**
     * Constructor for BatchIO and terminal text area with tabs.
     */
    private void BatchIO() {
        tabbedPane2 = new JTabbedPane();
        tabbedPane2.setPreferredSize(new Dimension(screenSize.width/4,screenSize.height*3/14));
        tabbedPane2.setFont(myFont);
        BatchIO = new JTextArea(null,screenSize.width/4,screenSize.height*3/14);
        BatchIO.setFont(myFont);
        Terminal = new JTextArea(null,screenSize.width/4,screenSize.height*3/14);
        tabbedPane2.addTab("BatchI/O", BatchIO); tabbedPane2.addTab("Terminal I/O", Terminal);
        CenterPanel.add(tabbedPane2);
        Outputtext = new JTextArea(null,screenSize.width/4,screenSize.height*3/14);
        Outputtext.setFont(myFont);
        Outputtext.setEditable(true);
        Outputtext.setBorder(new TitledBorder(null, "OUTPUT", TitledBorder.CENTER, TitledBorder.TOP, myFont, null));
        Outputtext.setPreferredSize(new Dimension(screenSize.width/4,screenSize.height*3/14));
        CenterPanel.add(Outputtext, BorderLayout.CENTER);
    }

    /**
     * Constructor for Line end Panel with components panels and scroll.
     */
    private void LineEndPanel() {
        LineEndPanel = new JPanel();
        LineEndPanel.setLayout(new GridLayout(1,1));
        LineEndPanel.setBorder(new TitledBorder(null, "Memory", TitledBorder.CENTER, TitledBorder.TOP, myFont, null));
        LineEndPanel.setPreferredSize(new Dimension(screenSize.width/4,screenSize.height*9/14));
        Memory = new JTextArea(null,screenSize.width/4,screenSize.height*9/14);
        Memory.setFont(myFont);
        Memory.setEditable(false);
        scroll = new JScrollPane(Memory);
        scroll.setVerticalScrollBarPolicy (ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS );
        Memory.setBorder(new TitledBorder(null, null, TitledBorder.CENTER, TitledBorder.TOP, myFont, null));
        LineEndPanel.add(scroll);
    }

    /**
     * Constructor for Menu Bar with sub-menu.
     */
    private void MenuBar() {
        /*Create file menu with sub-menu*/
        menuBar = new JMenuBar();
        File = new JMenu("File");
        File.setFont(myFont);
        newMenu = new JMenuItem("New");
        newMenu.addActionListener(this);
        openMenu = new JMenuItem("Open");
        openMenu.addActionListener(this);
        newMenu.setFont(myFont);
        openMenu.setFont(myFont);
        File.add(newMenu);
        File.add(openMenu);

        /*Create edit menu with sub-menu*/
        Edit = new JMenu("Edit");
        Edit.setFont(myFont);
        cutMenu = new JMenuItem("Cut Object Code");
        cutMenu.addActionListener(this);
        pasteMenu = new JMenuItem("Paste into Object Code");
        pasteMenu.addActionListener(this);
        cutMenu.setFont(myFont);
        pasteMenu.setFont(myFont);
        Edit.add(cutMenu);
        Edit.add(pasteMenu);

        /*Create build menu with sub-menu*/
        Build = new JMenu("Build");
        Build.setFont(myFont);
        loadMenu = new JMenuItem("Load");
        loadMenu.addActionListener(this);
        runMenu = new JMenuItem("Run");
        runMenu.addActionListener(this);
        loadMenu.setFont(myFont);
        runMenu.setFont(myFont);
        Build.add(loadMenu);
        Build.add(runMenu);

        /*Adding all menu buttons to menubar*/
        menuBar.add(File);menuBar.add(Build);menuBar.add(Edit);
        frame.setJMenuBar(menuBar);
    }

    /**
     * Performs actions based on the action of the user. Open, Load, Save, Start, etc.
     * Listen action by user and call viewListenr with string value.
     * @param e The event triggered.
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        String userinput = e.getActionCommand();
        /**
         * Button input
         */
        if (userinput.equals("New")) {
            ViewListner.buttonPushed("New");
        } else if (userinput.equals("Run Source")) {
            ViewListner.buttonPushed("Run Source");
        } else if (userinput.equals("Save")) {
            ViewListner.buttonPushed("Save");
        } else if (userinput.equals("Start Debugging")) {
            ViewListner.buttonPushed("Start Debugging");
        } else if (userinput.equals("Assemble")) {
            ViewListner.buttonPushed("Assemble");
        }
        /**
         * file menu input
         */
        else if (userinput.equals("New")) {
            ViewListner.fileSelection("New");
        } else if (userinput.equals("Open")) {
            ViewListner.fileSelection("Open");
        }
        /**
         * build menu input
         */
        else if (userinput.equals("Load")) {
            ViewListner.buildSelection("Load");
        } else if (userinput.equals("Run")) {
            ViewListner.buildSelection("Run");
        }
        /**
         * edit menu input
         */
        else if (userinput.equals("Cut Object Code")) {
            ViewListner.editSelection("Cut Object Code");
        } else if (userinput.equals("Paste into Object Code")) {
            ViewListner.editSelection("Paste into Object Code");
        }
    }

    /**
     * setter for memory
     * @param memory
     */
    public void setMemory(byte[] memory){
        StringBuilder sb= new StringBuilder();
        for (int i = 0; i < memory.length; i++) {
            if (i % 8 == 0) {
                sb.append("\n");
                String addrHex = String.format("%04x", i);
                sb.append(addrHex.toUpperCase());
                sb.append(": ");
            }
            String hex = String.format("%02x", memory[i]);
            sb.append(hex.toUpperCase());
            sb.append(" ");
        }
        sb.replace(0, 1, "");
        Memory.setText(sb.toString());
    }

    /**
     * Setter for register information
     * {Accumulator, IndexResigister, StackPointer,
     * ProgramCounter, Instruction Specifier, Operand Specifier, Operand}
     * @param registerList
     */
    public void setRegistersText(short[] registerList) {
        String Accumulator = String.format("%04x", registerList[0]);
        Accumulatorout1.setText(Accumulator.toUpperCase());
        Accumulatorout2.setText(""+registerList[0]);

        String IndexResigister = String.format("%04x", registerList[1]);
        IndexRegister1.setText(Accumulator.toUpperCase());
        IndexRegister2.setText(""+registerList[1]);

        String StackPointer = String.format("%04x", registerList[2]);
        StackPointer1.setText(Accumulator.toUpperCase());
        StackPointer2.setText(""+registerList[2]);

        String ProgramCounter = String.format("%04x", registerList[3]);
        ProgramCounterout1.setText(Accumulator.toUpperCase());
        ProgramCounterout2.setText(""+registerList[3]);

        String InstructionSpecifier = String.format("%04x", registerList[4]);
        Instructionout1.setText(Accumulator.toUpperCase());
        Instructionout2.setText(""+registerList[4]);

        String OperandSpecifier = String.format("%04x", registerList[5]);
        OperandSpecifier1.setText(Accumulator.toUpperCase());
        OperandSpecifier2.setText(""+registerList[5]);

        String Operand = String.format("%04x", registerList[6]);
        Operand1.setText(Accumulator.toUpperCase());
        Operand2.setText(""+registerList[6]);
    }

    /**
     * output setter
     * @param c character
     */
    public void setoutput(char c) {
        Outputtext.setText(Outputtext.getText() + c);
    }

    /**
     * getter for object code
     * @return shorts value of object code
     */
    public short getObjectCode(){
        return (short) (Integer.parseInt(ObjCode.getText(),16));
    }

    /**
     * Getter for source Code
     * @return string of source code
     */
    public String getSourceCode(){
        return sourceTab.getText();
    }

    /**
     * getter batch input
     * @return batch input character
     */
    public String getBatchInput(){
        return BatchIO.getText();
    }
}
