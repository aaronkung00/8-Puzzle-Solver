/*  AI - Assignment 1
*   Author : Aaron Kung
*   Time: 2019 Winter
* */


import java.util.*;
import java.util.Queue;

public class Search
{

    private int totalTiles;
    private int[] x_index;   //x coordinates index of the goal states
    private int[] y_index;   //y coordinates index of the goal states

    private Node start_state;
    private int[][] goal_state;
    private int time = 1;
    private int space = 0;

    public Search(Node start_state, int[][] goal_state){
        this.start_state = start_state;
        this.goal_state = goal_state;
        this.totalTiles  = start_state.state.getSIZE() * start_state.state.getSIZE();
        this.x_index = new int[totalTiles];
        this.y_index = new int[totalTiles];

        for(int i = 0 ; i < start_state.state.getSIZE(); i++)
            for(int j = 0 ; j <  start_state.state.getSIZE() ; j++){

                x_index[this.goal_state[i][j]] = i;
                y_index[this.goal_state[i][j]] = j;

            }
    }

    //Breadth First Search - Queue
    public Node by_BFS(){
        Queue<Node> queue = new LinkedList<>();
        time = 1;
        space = 0;

        queue.add(this.start_state);
        Node temp  = queue.remove();
        temp.state.setExpand();

        while( !verifyState(temp.state,goal_state)){
            if(queue.size() > space)
                this.space = queue.size();
            findSuccessor(temp);
            for(Node x : temp.children)
                if(!x.state.ifIsExpand())
                    queue.add(x);

            temp = queue.remove();
            this.time++;
            temp.state.setExpand();

        }

        return temp;

    }

    //Depth First Search - Stack
    public Node by_DFS(){

        time = 1;
        space = 0;
        Stack stk = new Stack<Node>();
        HashMap<String , Integer> visited_map = new HashMap<String, Integer>();
        stk.push(this.start_state);
        Node temp  = (Node) stk.pop();


        while( !verifyState(temp.state,goal_state) ){
            if(stk.size() > space)
                this.space = stk.size();
            visited_map.put(temp.state.toString(), 0);
            findSuccessor(temp);
            for(Node x : temp.children)
                if(!visited_map.containsKey(x.state.toString()))
                    stk.push(x);

            temp =(Node) stk.pop();
            time++;
            temp.state.setExpand();
        }

        return temp;
    }


    //Iterative Deepening
    public Node by_IDDFS(){

        Stack stk = new Stack<Node>();
        HashMap<String , Integer> visited_map = new HashMap<String, Integer>();
        stk.push(this.start_state);
        Node temp  = (Node) stk.pop();
        int depthLimit = 1;
        Boolean isfound = false;


        while (depthLimit>0) {
            time = 1;
            space = 0;

            while (isfound = !verifyState(temp.state, goal_state)) {
                if(stk.size() > space)
                    this.space = stk.size();
                visited_map.put(temp.state.toString(), 1);
                findSuccessor(temp);
                for (Node x : temp.children)
                    if (!visited_map.containsKey(x.state.toString()) && x.state.getDepth() <= depthLimit)
                        stk.push(x);

                if(stk.isEmpty())
                    break;
                temp = (Node) stk.pop();
                time++;
                temp.state.setExpand();
            }

            if(!isfound)
                break;

            temp = (this.start_state);
            visited_map.clear();
            depthLimit++;
        }

        return temp;
    }


    // Uniform-Cost Search => { f(n) = g(n), where g(n) = the value of tile being moved }
    public Node by_UCS(){
        PriorityQueue<Node> minHeap = new PriorityQueue<Node>();
        HashMap<String , Integer> visited_map = new HashMap<String, Integer>();

        minHeap.add(this.start_state);

        Node temp = null;

        while (!minHeap.isEmpty())
        {
            if(minHeap.size() > space)
                this.space = minHeap.size();
            temp = minHeap.poll();
            this.time++;
            temp.state.setExpand();
            visited_map.put(temp.state.toString(),1);

            if(verifyState(temp.state,this.goal_state))
                break;;

            findSuccessor(temp);
            for(Node i : temp.children)
                if(!visited_map.containsKey(i.state.toString()))
                    minHeap.add(i);

        }

        return temp;


    }


    // Best-First Search => { f(n) = h(n), where h(n) = number of tiles that are not in correct position }
    public Node by_BestFS(){

        PriorityQueue<Node> minHeap = new PriorityQueue<Node>();
        HashMap<String , Integer> visited_map = new HashMap<String, Integer>();

        minHeap.add(this.start_state);

        Node temp = null;

        while (!minHeap.isEmpty())
        {
            if(minHeap.size() > space)
                this.space = minHeap.size();
            temp = minHeap.poll();
            this.time++;
            temp.state.setExpand();
            set_BestFS_Heuristic(temp.state);
            visited_map.put(temp.state.toString(),1);


            if(verifyState(temp.state,this.goal_state))
                break;;

            findSuccessor(temp);
            for(Node i : temp.children)
                if(!visited_map.containsKey(i.state.toString()))
                {
                    set_BestFS_Heuristic(i.state);
                    minHeap.add(i);

                }

        }

        return temp;

    }


    //Set BestFS Heuristic
    public void set_BestFS_Heuristic(State state) {

        int misplaced = 0;

        for(int i = 0 ; i < state.getSIZE() ; i++)
            for(int j = 0 ; j <  state.getSIZE()  ; j++){
                if(state.getState_array()[i][j] != goal_state[i][j] && state.getState_array()[i][j] > 0)
                    misplaced++;
            }

        state.setPath_Cost(misplaced);
        state.setTotal_cost(misplaced);

    }



    // A* Search 1 => { f(n) = g(n) + h(n), where h(n) = number of tiles that are not in correct position }
    public Node by_A1(){

        PriorityQueue<Node> minHeap = new PriorityQueue<Node>();
        HashMap<String , Integer> visited_map = new HashMap<String, Integer>();

        minHeap.add(this.start_state);  //add root

        Node temp = null;

        while (!minHeap.isEmpty())
        {
            if(minHeap.size() > space)
                this.space = minHeap.size();
            temp = minHeap.poll();
            this.time++;
            temp.state.setExpand();

            if(temp.parent==null)           //only set root's heuristic
              set_A1_Heuristic(temp.state);

            visited_map.put(temp.state.toString(),1);

            if(verifyState(temp.state,this.goal_state))
                break;

            findSuccessor(temp);
            for(Node i : temp.children)
                if(!visited_map.containsKey(i.state.toString()))
                {
                    set_A1_Heuristic(i.state);
                    minHeap.add(i);
                }
        }

        return temp;
    }


    //Set A1 Heuristic
    public void set_A1_Heuristic(State state) {

        int misplaced = 0;

        for(int i = 0 ; i < state.getSIZE() ; i++)
            for(int j = 0 ; j <  state.getSIZE()  ; j++){
                if(state.getState_array()[i][j] != goal_state[i][j] && state.getState_array()[i][j] > 0)
                    misplaced++;
            }

        //Correct the cost
        int tmp = state.getPath_Cost();
        int tmpTotal = state.getTotal_cost();
        state.setPath_Cost(misplaced + state.getPath_Cost());
        state.setTotal_cost(state.getPath_Cost()+tmpTotal-tmp);
    }


    // A* Search 2 => { f(n) = g(n) + h(n), where h(n) = sum of Manhattan distances between all tiles and their correct positions }
    public Node by_A2(){

        PriorityQueue<Node> minHeap = new PriorityQueue<Node>();
        HashMap<String , Integer> visited_map = new HashMap<String, Integer>();

        minHeap.add(this.start_state);  //add root

        Node temp = null;

        while (!minHeap.isEmpty())
        {
            if(minHeap.size() > space)
                this.space = minHeap.size();
            temp = minHeap.poll();
            this.time++;
            temp.state.setExpand();

            if(temp.parent==null)           //only set root's heuristic
            {
                set_A2_Heuristic(temp.state);
            }

            visited_map.put(temp.state.toString(),1);

            if(verifyState(temp.state,this.goal_state))
                break;;

            findSuccessor(temp);
            for(Node i : temp.children)
                if(!visited_map.containsKey(i.state.toString()))
                {
                    set_A2_Heuristic(i.state);
                    minHeap.add(i);

                }
        }

        return temp;
    }


    //Set A2 Heuristic - manhattan_dist
    public void set_A2_Heuristic(State state) {

        int sumOfManhattan = 0;

        for(int i = 0 ; i < state.getSIZE() ; i++)
            for(int j = 0 ; j <  state.getSIZE()  ; j++){
                int current = state.getState_array()[i][j];
                if(current != goal_state[i][j] && current!=0)
                    sumOfManhattan += manhattan_dist(i,j,x_index[current],y_index[current]);

            }

        //Correct the cost
        int tmp = state.getPath_Cost();
        int tmpTotal = state.getTotal_cost();
        state.setPath_Cost(sumOfManhattan + state.getPath_Cost());
        state.setTotal_cost(state.getPath_Cost()+tmpTotal-tmp);


    }


    // A* Search 3 => { f(n) = g(n) + h(n), where h(n) = Minimun of g(n) and Sum of the Euclidean distance between all tiles and their correct positions (My own Heuristic) }
    public Node by_A3(){
        PriorityQueue<Node> minHeap = new PriorityQueue<Node>();
        HashMap<String , Integer> visited_map = new HashMap<String, Integer>();

        minHeap.add(this.start_state);  //add root

        Node temp = null;

        while (!minHeap.isEmpty())
        {
            if(minHeap.size() > space)
                this.space = minHeap.size();
            temp = minHeap.poll();
            this.time++;
            temp.state.setExpand();

            if(temp.parent==null)           //only set root's heuristic
            {
                set_A3_Heuristic(temp.state);
                temp.state.setTotal_cost(temp.state.getPath_Cost());
                //  temp.state.print();
            }

            visited_map.put(temp.state.toString(),1);

            if(verifyState(temp.state,this.goal_state))
                break;;

            findSuccessor(temp);
            for(Node i : temp.children)
                if(!visited_map.containsKey(i.state.toString()))
                {
                    set_A3_Heuristic(i.state);
                    minHeap.add(i);
                }
        }

        return temp;
    }

    //Set A3 Heuristic - Euclidean distance
    public void set_A3_Heuristic(State state) {

        int sumOfEuclidean = 0;

        for(int i = 0 ; i < state.getSIZE() ; i++)
            for(int j = 0 ; j <  state.getSIZE()  ; j++){
                int current = state.getState_array()[i][j];
                if(current != goal_state[i][j] && current !=0)
                {
                    sumOfEuclidean += (Euclidean_dist(i,j,x_index[current],y_index[current]));
                }

            }

        //Correct the cost
        int tmp = state.getPath_Cost();
        state.setPath_Cost(Math.min(sumOfEuclidean , tmp) + tmp);
        state.setTotal_cost(state.getTotal_cost() + state.getPath_Cost() - tmp);

    }


    //Getters and Heuristic Setters
    public int getSpace() {
        return space;
    }

    public int getTime() {
        return time;
    }




    public int manhattan_dist(int x1 , int y1 , int x2, int y2){

        return Math.abs(x2-x1) + Math.abs(y2-y1);
    }



    private double Euclidean_dist(int x1 , int y1 , int x2, int y2) {

        return  Math.sqrt(((x2-x1)*(x2-x1)+ (y2-y1)*(y2-y1)));
    }


    private boolean verifyState(State cur, int[][] goal){

        for(int i = 0 ; i < 3 ; i++)
            for(int j = 0 ; j < 3 ; j++)
                if(cur.getState_array()[i][j] != goal[i][j])
                    return false;
        return true;
    }
    /*
    *   The findSuccessor is going to find where zero is and detect its adjacent elements
    *   and add previous total cost and current moving path cost and then create a new state.
    *   The new state will put in its children list for later use.
    * */


    private static void findSuccessor(Node node){

            for(int row = 0 ; row < 3 ; row++)
                for(int col = 0 ; col < 3 ; col++){
                    if(node.state.getState_array()[row][col] == 0) {

                        State tempstate;

                        if(row + 1 < 3){
                            tempstate = new State(node.state.getState_array(), node.state.getState_array()[row+1][col] + " -> up", node.state.getState_array()[row+1][col],node.state.getState_array()[row+1][col]+node.state.getTotal_cost(),node.state.getDepth()+1, false);
                            tempstate.mv_down(row,col);
                            Node temp_node = new Node(tempstate,node);
                            node.children.add(temp_node);
                         }

                        if(col + 1 < 3){
                            tempstate =  new State(node.state.getState_array(),node.state.getState_array()[row][col+1] + " -> left",  node.state.getState_array()[row][col+1],node.state.getState_array()[row][col+1]+node.state.getTotal_cost(),node.state.getDepth()+1, false);
                            tempstate.mv_right(row,col);
                            Node temp_node = new Node(tempstate,node);
                            node.children.add(temp_node);
                        }

                        if(row - 1 >= 0){
                            tempstate = new State(node.state.getState_array(),node.state.getState_array()[row-1][col] + " -> down",  node.state.getState_array()[row-1][col],node.state.getState_array()[row-1][col]+node.state.getTotal_cost(),node.state.getDepth()+1, false);
                            tempstate.mv_up(row,col);
                            Node temp_node = new Node(tempstate,node);
                            node.children.add(temp_node);
                        }

                        if(col - 1 >= 0){
                            tempstate =  new State(node.state.getState_array(),node.state.getState_array()[row][col-1] + " -> right",  node.state.getState_array()[row][col-1],node.state.getState_array()[row][col-1]+node.state.getTotal_cost(),node.state.getDepth()+1, false);
                            tempstate.mv_left(row,col);
                            Node temp_node = new Node(tempstate,node);
                            node.children.add(temp_node);
                        }

                            break;
                    }
                }

    }



}
