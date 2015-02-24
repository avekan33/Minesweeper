import processing.core.*; 
import processing.data.*; 
import processing.event.*; 
import processing.opengl.*; 

import de.bezier.guido.*; 

import java.util.HashMap; 
import java.util.ArrayList; 
import java.io.File; 
import java.io.BufferedReader; 
import java.io.PrintWriter; 
import java.io.InputStream; 
import java.io.OutputStream; 
import java.io.IOException; 

public class Minesweeper extends PApplet {




//Declare and initialize NUM_ROWS and NUM_COLS = 20
public final static int NUM_COLS = 20;
public final static int NUM_ROWS = 20;
private MSButton[][] buttons; //2d array of minesweeper buttons
private ArrayList <MSButton> bombs; //ArrayList of just the minesweeper buttons that are mined
private int valBombs;
int count;
public void setup ()
{
    size(400, 400);
    textAlign(CENTER,CENTER);
    
    // make the manager
    Interactive.make( this );
    
    

    //declare and initialize buttons
    valBombs = 75;
    bombs = new ArrayList <MSButton>();
    buttons = new MSButton[NUM_ROWS][NUM_COLS];
    for(int i = 0; i< NUM_ROWS;i++)
    {
    	for(int j =0; j< NUM_COLS;j++)
    	{
    		buttons[i][j] = new MSButton(i,j);
    	}
    }
    for(int k =0;k<valBombs;k++){
    	setBombs();
    }
    //System.out.println("There are "+count+" bombs.");
}
public void setBombs()
{
    //your code
    int row = (int)(Math.random()*20);
    int col = (int)(Math.random()*20);
    if(bombs.contains(buttons[row][col]) == false){bombs.add(buttons[row][col]);count++;}
    else{valBombs++;}
}

public void draw ()
{
    background( 0 );
    if(isWon())
        displayWinningMessage();
    if(isLost())
    	displayLosingMessage();
}
public boolean isWon()
{
    //your code here
    for(int i=0;i<bombs.size();i++)
    {
    	if(!bombs.get(i).isMarked()){return false;}
    }
    for(int u=0;u<NUM_ROWS;u++)
    {
    	for(int y=0;y<NUM_COLS;y++)
    	{if(!bombs.contains(buttons[u][y]) && buttons[u][y].isMarked()){return false;}}
    }
    return true;
}
public boolean isLost()
{
	for(int j=0;j<bombs.size();j++)
	{
		if(bombs.get(j).isClicked() && !bombs.get(j).isMarked()){return true;}
	}
	return false;
}
public void displayLosingMessage()
{
    textAlign(CENTER);
    stroke(255,0,0);
    buttons[NUM_ROWS/2][NUM_COLS/2-1].setLabel("YOU");
    buttons[NUM_ROWS/2][(NUM_COLS/2)+1].setLabel("LOSE!");
    for(int r=0;r<bombs.size();r++)
    {
    	bombs.get(r).setClicked();
    	bombs.get(r).setUnmarked();
    }
}
public void displayWinningMessage()
{
    //your code here
    textAlign(CENTER);
    stroke(0,255,255);
    buttons[NUM_ROWS/2][NUM_COLS/2-1].setLabel("YOU");
    buttons[NUM_ROWS/2][(NUM_COLS/2)+1].setLabel("WIN!");
}

public class MSButton
{
    private int r, c;
    private float x,y, width, height;
    private boolean clicked, marked;
    private String label;
    
    public MSButton ( int rr, int cc )
    {
        width = 400/NUM_COLS;
        height = 400/NUM_ROWS;
        r = rr;
        c = cc; 
        x = c*width;
        y = r*height;
        label = "";
        marked = clicked = false;
        Interactive.add( this ); // register it with the manager
    }
    public boolean isMarked()
    {
        return marked;
    }
    public boolean isClicked()
    {
        return clicked;
    }
    // called by manager
    public void setUnmarked()
    {
    	marked = false;
    }
    public void setClicked()
    {
    	clicked = true;
    }
    public void mousePressed () 
    {
        clicked = true;
        //your code here
        if(keyPressed){marked = (!marked);}
        else if(bombs.contains(this)){displayLosingMessage();}
        else if(countBombs(r,c) > 0){label = ""+countBombs(r,c);}
        else{ 
        	if(isValid(r-1,c-1)&&buttons[r-1][c-1].isClicked()==false){buttons[r-1][c-1].mousePressed();}
        	if(isValid(r-1,c)&&buttons[r-1][c].isClicked()==false){buttons[r-1][c].mousePressed();}
        	if(isValid(r-1,c+1)&&buttons[r-1][c+1].isClicked()==false){buttons[r-1][c+1].mousePressed();}
        	if(isValid(r,c-1)&&buttons[r][c-1].isClicked()==false){buttons[r][c-1].mousePressed();}
        	if(isValid(r,c+1)&&buttons[r][c+1].isClicked()==false){buttons[r][c+1].mousePressed();}
        	if(isValid(r+1,c-1)&&buttons[r+1][c-1].isClicked()==false){buttons[r+1][c-1].mousePressed();}
        	if(isValid(r+1,c)&&buttons[r+1][c].isClicked()==false){buttons[r+1][c].mousePressed();}
        	if(isValid(r+1,c+1)&&buttons[r+1][c+1].isClicked()==false){buttons[r+1][c+1].mousePressed();}
        }
        	
    }

    public void draw () 
    {    
        if (marked)
            fill(0);
        else if( clicked && bombs.contains(this) ) 
            fill(255,0,0);
        else if(clicked)
            fill( 200 );
        else 
            fill( 100 );

        rect(x, y, width, height);
        fill(0);
        text(label,x+width/2,y+height/2);
    }
    public void setLabel(String newLabel)
    {
        label = newLabel;
    }
    public boolean isValid(int r, int c)
    {
        //your code here
        if(r<NUM_ROWS && c<NUM_COLS&&r>=0&&c>=0){return true;}
        return false;
    }
    public int countBombs(int r, int c)
    {
        int numBombs = 0;
        //your code here
        if(isValid(r-1,c-1)&&bombs.contains(buttons[r-1][c-1])){numBombs++;}
        if(isValid(r-1,c)&&bombs.contains(buttons[r-1][c])){numBombs++;}
        if(isValid(r-1,c+1)&&bombs.contains(buttons[r-1][c+1])){numBombs++;}
        if(isValid(r,c-1)&&bombs.contains(buttons[r][c-1])){numBombs++;}
        if(isValid(r,c+1)&&bombs.contains(buttons[r][c+1])){numBombs++;}
        if(isValid(r+1,c-1)&&bombs.contains(buttons[r+1][c-1])){numBombs++;}
        if(isValid(r+1,c)&&bombs.contains(buttons[r+1][c])){numBombs++;}
        if(isValid(r+1,c+1)&&bombs.contains(buttons[r+1][c+1])){numBombs++;}
        return numBombs;
    }
}



  static public void main(String[] passedArgs) {
    String[] appletArgs = new String[] { "Minesweeper" };
    if (passedArgs != null) {
      PApplet.main(concat(appletArgs, passedArgs));
    } else {
      PApplet.main(appletArgs);
    }
  }
}
