/*  AI - Assignment 1
 *   Author : Aaron Kung
 *   Time: 2019 Winter
 * */

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Stack;

public class Eight_Puzzle extends JDialog {

    //Initialize the state
    final int[][] GOAL = new int[][]{ { 1 , 2 , 3 } , { 8 , 0 , 4 }, { 7 , 6 , 5 } };
    final int[][] EASY = new int[][]{ { 1 , 3 , 4} , { 8 , 6 , 2 } , { 7 , 0 , 5 } };
    final int[][] MEDIUM = new int[][]{ { 2 , 8 , 1} , { 0  , 4 , 3 } , { 7 , 6 , 5 } };
    final int[][] HARD = new int[][]{ { 5 , 6 , 7} , { 4 , 0 , 8 } , { 3 , 2 , 1 } };

    private JPanel contentPane;
    private JButton btnRun;
    private JButton benCancel;
    private JRadioButton radioBtnMed;
    private JRadioButton radioBtnEasy;
    private JRadioButton radioBtnHard;
    private JRadioButton radioBtnBFS;
    private JRadioButton radioBtnDFS;
    private JRadioButton radioBtnUCS;
    private JRadioButton radioBtnBestFS;
    private JRadioButton radioBtnA1;
    private JRadioButton radioBtnA2;
    private JRadioButton radioBtnA3;
    private JTextArea outputArea;
    private JLabel lblAlg;
    private JLabel btnResult;
    private JRadioButton radioBtnIDDFS;
    private JLabel lblPerformance;
    private JTextArea txtPerfom;
    private JLabel lblSetting;

    public Eight_Puzzle() {
        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(btnRun);
        txtPerfom.setOpaque(false);

        ButtonGroup bgroup = new ButtonGroup();
        bgroup.add(radioBtnEasy);
        bgroup.add(radioBtnMed);
        bgroup.add(radioBtnHard);

        ButtonGroup bgroup2 = new ButtonGroup();
        bgroup2.add(radioBtnA1);
        bgroup2.add(radioBtnA2);
        bgroup2.add(radioBtnA3);
        bgroup2.add(radioBtnBestFS);
        bgroup2.add(radioBtnBFS);
        bgroup2.add(radioBtnDFS);
        bgroup2.add(radioBtnUCS);
        bgroup2.add(radioBtnIDDFS);




        btnRun.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {

                State state = null;
                Node result = null;
                int TIME = 0;
                int SPACE = 0;
                long RUNTIME = 0;


                //Create different States depending on selection
                if(radioBtnEasy.isSelected())
                    state = new State(EASY,"Root", 0,0,0,false);
                if(radioBtnMed.isSelected())
                    state = new State(MEDIUM,"Root", 0,0,0,false);
                if(radioBtnHard.isSelected())
                    state = new State(HARD,"Root", 0,0,0,false);

                //Create Root and Search Object
                Node root = new Node(state , null);
                Search search = new Search(root , GOAL);

                /*
                *   Each Algorithm has its timer to measure running time.
                *   All algorithms are coded in the Search object
                */

                if(radioBtnBFS.isSelected()){
                    long startTime = System.nanoTime();
                    result = search.by_BFS();
                    TIME = search.getTime();
                    SPACE = search.getSpace();
                    RUNTIME = System.nanoTime() - startTime;
                }

                if(radioBtnDFS.isSelected()){
                    long startTime = System.nanoTime();
                    result = search.by_DFS();
                    TIME = search.getTime();
                    SPACE = search.getSpace();
                    RUNTIME = System.nanoTime() - startTime;
                }

                if(radioBtnIDDFS.isSelected()){
                    long startTime = System.nanoTime();
                    result = search.by_IDDFS();
                    TIME = search.getTime();
                    SPACE = search.getSpace();
                    RUNTIME = System.nanoTime() - startTime;
                }

                if(radioBtnUCS.isSelected()){
                    long startTime = System.nanoTime();
                    result = search.by_UCS();
                    TIME = search.getTime();
                    SPACE = search.getSpace();
                    RUNTIME = System.nanoTime() - startTime;
                }



                if(radioBtnBestFS.isSelected()){
                    long startTime = System.nanoTime();
                    result = search.by_BestFS();
                    TIME = search.getTime();
                    SPACE = search.getSpace();
                    RUNTIME = System.nanoTime() - startTime;
                    Node temp = result;
                    //Counting pathCost here for BestFS
                    int pathSum=0;
                    while (temp!=null){
                        pathSum+=temp.state.getPath_Cost();
                        temp = temp.parent;
                    }
                    result.state.setTotal_cost(pathSum);
                }



                if(radioBtnA1.isSelected()){
                    long startTime = System.nanoTime();
                    result = search.by_A1();
                    TIME = search.getTime();
                    SPACE = search.getSpace();
                    RUNTIME = System.nanoTime() - startTime;

                }


                if(radioBtnA2.isSelected()){
                        long startTime = System.nanoTime();
                        result = search.by_A2();
                        TIME = search.getTime();
                        SPACE = search.getSpace();
                        RUNTIME = System.nanoTime() - startTime;
                }


                if(radioBtnA3.isSelected()){
                    long startTime = System.nanoTime();
                    result = search.by_A3();
                    TIME = search.getTime();
                    SPACE = search.getSpace();
                    RUNTIME = System.nanoTime() - startTime;
                }


                //Print Solution in the Output Area
                outputArea.setText(solutionStringGenerator(result));

                //Print measurements in the performance area
                String strTemp = "";
                strTemp += String.format(" Time: %s\n Space: %s\n", TIME,SPACE);
                strTemp += String.format(" Total Run Time: %f (milliseconds)", RUNTIME/1000000.0);

                txtPerfom.setText(strTemp);

            }
        });

        benCancel.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        });

        // call onCancel() when cross is clicked
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                onCancel();
            }
        });

        // call onCancel() on ESCAPE
        contentPane.registerKeyboardAction(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        }, KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
    }

    private void onCancel() {
        // add your code here if necessary
        dispose();
    }


    //Generate Path String
    private String solutionStringGenerator(Node node){

        Stack stk = new Stack<Node>();
        String str = "";

        while (node != null) {
            stk.push(node);
            node = node.parent;
        }

        int stkStize = stk.size();

        while (!stk.isEmpty()){
            Node temp  = (Node) stk.pop();
            str += temp.state.solutionString();
        }

        str += String.format("Path length: %s" , stkStize);


        return str;

    }



    public static void main(String[] args) {
        //Initialize the java dialog

        Eight_Puzzle dialog = new Eight_Puzzle();
        dialog.setPreferredSize(new Dimension(700,1000));
        dialog.pack();
        dialog.setVisible(true);

        System.exit(0);


    }


}
