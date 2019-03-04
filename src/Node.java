/*  AI - Assignment 1
 *   Author : Aaron Kung
 *   Time: 2019 Winter
 * */

import java.util.ArrayList;

public class Node  implements Comparable{

    public State state;
    public Node parent;
    public ArrayList<Node> children = new ArrayList<>();

    public Node(State s , Node  parent){
        this.state = new State(s.getState_array(),s.getAction(),  s.getPath_Cost() , s.getTotal_cost() ,s.getDepth(), s.ifIsExpand());
        this.parent = parent;
    }


    //Overide CompareTo for MinHeap
    @Override
    public int compareTo(Object o) {

        if(this.state.getTotal_cost()- ((Node) o).state.getTotal_cost() > 0)
            return 1;
        else if(this.state.getTotal_cost() - ((Node) o).state.getTotal_cost() < 0)
            return -1;
        else
            return 0;
    }
}
