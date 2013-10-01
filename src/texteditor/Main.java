package texteditor;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.io.*;

public class Main  extends JFrame implements ActionListener, ItemListener
{
    private JTextArea output;
    private JTextArea textEditor = new JTextArea();
    private JFileChooser chooser = new JFileChooser();
    private String newline = "\n";
    private String filePath, content;
    private int fileCounter = 0;
    private String fileName = "Untitled_"; // default file name

    public JMenuBar createMenuBar()
    {
        JMenuBar menuBar;
        JMenu menu;
        JMenuItem menuItem;

        //Create the menu bar.
        menuBar = new JMenuBar();

        //Build the first menu.
        menu = new JMenu("File");
        menuBar.add(menu);

        //a group of JMenuItems
        menuItem = new JMenuItem("New",KeyEvent.VK_T);
        menuItem.setActionCommand("New");
        menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N, ActionEvent.CTRL_MASK));
        menuItem.addActionListener(this);
        menu.add(menuItem);

        menuItem = new JMenuItem("Open",KeyEvent.VK_T);
        menuItem.setActionCommand("Open");
        menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, ActionEvent.CTRL_MASK));
        menuItem.addActionListener(this);
        menu.add(menuItem);

        menuItem = new JMenuItem("Save",KeyEvent.VK_T);
        menuItem.setActionCommand("Save");
        menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, ActionEvent.CTRL_MASK));
        menuItem.addActionListener(this);
        menu.add(menuItem);

        menuItem = new JMenuItem("Save As",KeyEvent.VK_T);
        menuItem.setActionCommand("Save As");
        menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_A, ActionEvent.CTRL_MASK));
        menuItem.addActionListener(this);
        menu.add(menuItem);

        menu.addSeparator();
        menuItem = new JCheckBoxMenuItem("Reverse");
        menuItem.setActionCommand("Reverse");
        menuItem.addActionListener(this);
        menu.add(menuItem);

        return menuBar;
    }
    public Container createContentPane()
    {
        //Create the content-pane-to-be.
        JPanel contentPane = new JPanel(new BorderLayout());
        contentPane.setOpaque(true);

        //Create a scrolled text area.
        output = new JTextArea(5, 30);
        output.setEditable(false);
        output.setBounds(3, 3, 300, 200);

        //Add the text area to the content pane.
        textEditor.setEditable(true);
        contentPane.add(textEditor);

        // Right and down scroll bar
        JScrollPane scrollBar = new JScrollPane(textEditor,JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
        contentPane.add(scrollBar);

        return contentPane;
    }
    private String convertUTF8toASCII(String text) throws UnsupportedEncodingException
    {
        byte[] b = text.getBytes("US-ASCII");
        String s = new String(b);
        return text;
    }
    public void actionPerformed(ActionEvent e)
    {
        if(e.getActionCommand().equals("New")) // New document
        {
            textEditor.setText("");
        }
        if(e.getActionCommand().equals("Open")) // Open file chooser
        {
            chooser.setMultiSelectionEnabled(true);
            int option = chooser.showOpenDialog(Main.this);
            if (option == JFileChooser.APPROVE_OPTION)
            {
                File[] sf = chooser.getSelectedFiles();
                filePath = sf[0].getAbsolutePath();

          // Read file content
                try
                {
                    FileInputStream fstream = new FileInputStream(filePath);
                    DataInputStream in = new DataInputStream(fstream);
                    BufferedReader br = new BufferedReader(new InputStreamReader(in));

                    textEditor.setText(""); // clear text editor
                    while ((content = br.readLine()) != null)
                    {
                        textEditor.append(convertUTF8toASCII(content));
                        textEditor.append(newline);
                    }
                    in.close();
                }
                catch(Exception excp)
                {
                    System.err.println(excp);
                }
            }
        }
        if(e.getActionCommand().equals("Save")) // Save file in temporary folder
        {
            // Save file in a new file .txt format
                try
                {
                    FileWriter fstream = new FileWriter("/tmp/"+fileName+fileCounter+".txt"); // Linux OS
                    BufferedWriter out = new BufferedWriter(fstream);
                    out.write(convertUTF8toASCII(textEditor.getText()));
                    //Close the output stream
                    fileCounter++;
                    out.close();
                }
                catch (Exception excpt)
                {
                    System.err.println("Error: " + excpt.getMessage());
                }
        }
        if(e.getActionCommand().equals("Save As")) // Save file as you want it
        {
            int returnVal = chooser.showSaveDialog(Main.this);

            if (returnVal == JFileChooser.APPROVE_OPTION)
            {
                File file = chooser.getSelectedFile();
                try
                {
                    FileWriter fstream = new FileWriter(file.getAbsolutePath());
                    BufferedWriter out = new BufferedWriter(fstream);
                    out.write(convertUTF8toASCII(textEditor.getText()));
                    //Close the output stream
                    out.close();
                }
                catch (Exception excpt)
                {
                    System.err.println("Error: " + excpt.getMessage());
                }
            }
        }
        if(e.getActionCommand().equals("Reverse"))
        {
            String reverse = new StringBuffer(textEditor.getText()).reverse().toString();
            textEditor.setText(reverse);
            try
            {
                FileWriter fstream = new FileWriter("/tmp/"+fileName+fileCounter+".txt"); // Linux OS
                BufferedWriter out = new BufferedWriter(fstream);
                out.write(convertUTF8toASCII(textEditor.getText()));
                //Close the output stream
                fileCounter++;
                out.close();
             }
             catch (Exception excpt)
             {
                System.err.println("Error: " + excpt.getMessage());
             }
        }
    }
    @Override
    public void itemStateChanged(ItemEvent e)
    {
    }

    /**
     * Create the GUI and show it.  For thread safety,
     * this method should be invoked from the
     * event-dispatching thread.
     */
    private static void createAndShowGUI()
    {
        //Create and set up the window.
        JFrame frame = new JFrame("Text editor");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        //Create and set up the content pane.
        Main demo = new Main();
        frame.setJMenuBar(demo.createMenuBar());
        frame.setContentPane(demo.createContentPane());

        //Display the window.
        frame.setSize(640, 480);
        frame.setLocation(400, 60);
        frame.setResizable(true);
        frame.setVisible(true);
    }
    public static void main(String[] args)
    {
        //Schedule a job for the event-dispatching thread:
        //creating and showing this application's GUI.
        javax.swing.SwingUtilities.invokeLater(new Runnable()
        {
            public void run()
            {
                createAndShowGUI();
            }
        });
    }
}