/*  AI - Assignment 1
 *   Author : Aaron Kung
 *   Time: 2019 Winter
 * */

public class State implements Cloneable{

    private final int SIZE = 3;
    private int[][] state_array = new int[SIZE][SIZE];
    private String action = "";
    private boolean isExpand = false;
    private int path_Cost = 0;
    private int depth = 0;
    private  int total_cost = 0;


    //constructor
    public State(int[][] input_state , String act,  int path_Cost   , int total_cost, int depth , boolean isExpand ){
        for(int i = 0 ; i < SIZE ; i++)
            for (int j = 0; j < SIZE ; j++)
                state_array[i][j] = input_state[i][j];

         this.action = act;
         this.isExpand = isExpand;
         this.path_Cost = path_Cost;
         this.depth = depth;
         this.total_cost = total_cost;
    }



    //Getters and Setters
    public int getTotal_cost() {
        return total_cost;
    }

    public int getSIZE() {
        return SIZE;
    }

    public String getAction() {
        return action;
    }

    public int[][] getState_array() {
        return state_array;
    }

    public int getDepth() {
        return depth;
    }

    public int getPath_Cost() {
        return path_Cost;
    }

    public boolean ifIsExpand() {
        return isExpand;
    }

    public void setExpand(){
        isExpand = true;
    }

    public void setPath_Cost(int path_Cost) {
        this.path_Cost = path_Cost;
    }

    public void setTotal_cost(int total_cost) {
        this.total_cost = total_cost;
    }

    /*
    *   The following functions are operators
    *   The index of zero is passed and swap the adjacent tiles
    */

    public void mv_up(int i , int j){
        this.state_array[i][j] = this.state_array[i-1][j];
        this.state_array[i-1][j] = 0;
    }



    public void mv_down(int i, int j) {
        this.state_array[i][j] = this.state_array[i+1][j];
        this.state_array[i+1][j] = 0;

    }


    public void mv_left(int i, int j) {
        this.state_array[i][j] =  this.state_array[i][j-1];
        this.state_array[i][j-1] = 0;
    }


    public void mv_right(int i, int j) {
        this.state_array[i][j] = this.state_array[i][j+1];
        this.state_array[i][j+1] = 0;
    }


    //Convert 2d array to String for HashTable for looking visited nodes
    public String toString(){
        String str = "";
        for(int i = 0 ; i < SIZE ; i++)
            for(int j = 0 ; j < SIZE ; j++)
                str += this.state_array[i][j];

        return  str;
    }


    //Generate State String
    public String solutionString(){

        String str = "";
        String arrayString = "";

        for(int i = 0 ; i < 3 ; i++){
            for(int j = 0 ; j < 3; j++)
                arrayString += this.state_array[i][j] + " ";
            arrayString += "\n";

        }


        str = String.format( "Action : %s\nExpand : %s\nPath Cost : %s\nTotal Cost: %s\nDepth : %s\nCurrent Status : \n%s\n"
                                ,  this.action , this.isExpand, this.path_Cost, this.total_cost , this.depth , arrayString) ;

        return  str;
    }



}
