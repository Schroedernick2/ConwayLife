import java.util.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.io.*;
import java.lang.*;

public class lifeGUI{
	JFrame window;
	Container content;
	JPanel grid;
	cell[][] board;

	int aliveCount;

	int dim = 100;


	lifeGUI(){
		aliveCount = 0;

		window = new JFrame("Nicky's Game of Life");
		content = window.getContentPane();
		content.setLayout(new GridLayout(1,1));

		grid = new JPanel(new GridLayout(dim,dim));
		board = new cell[dim][dim];

		ButtonListener listener = new ButtonListener();

		for(int i=0;i<dim;i++){
			for(int j=0;j<dim;j++){
				board[i][j] = new cell(i,j);
				board[i][j].getBody().addActionListener(listener);
				grid.add(board[i][j].getBody());
			}
		}

		content.add(grid);
		
		window.setSize(750,500);
		window.setVisible(true);
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		//SIMULATE LIFE
		//rules
			//1. any live cell with less than 2 live neighbors dies
			//2. any live cell with two or three living neighbors stays alive
			//3. any live cell with more than three live neighbors dies
			//4. any dead cell with exactly three live neighbors "gets born" 			

		int gen = 0;
		int genFroze = 0;
		boolean spawned;

		while(true){
			System.out.println("Generation: "+gen);
			spawned = false;
			gen++;

			if(gen<=10){
				for(int c=0;c<10;c++)
					randomlyPopulate();
			}

			for(int i=0;i<dim;i++){
				for(int j=0;j<dim;j++){
					int alive = getLiveNeighbors(board[i][j]);
					if(board[i][j].isAlive()){
						if(alive<2||alive>3)
							board[i][j].kill();
					}
					else{
						if(alive==3){
							board[i][j].giveBirth();
							spawned = true;
						}
					}
				}
			}

			if(!spawned)
				genFroze++;

			if(genFroze>2)
				for(int c=0;c<10;c++){
					randomlyPopulate();
					genFroze=0;
				}

			try{
				Thread.sleep(250);
			}
			catch(InterruptedException ex){
				System.out.println("Error in Timing Delay");
				System.exit(0);
				Thread.currentThread().interrupt();
			}	
		}		
	}

	private void randomlyPopulate(){
		Random rand = new Random();

		int x = rand.nextInt(10);
		int y = rand.nextInt(10);

		board[x][y].giveBirth();

		System.out.println("Cell Spawned at ("+x+","+y+")");
	}

	private int getLiveNeighbors(cell c){
		int livingNeighbors = 0;

		int x = c.getX();
		int y = c.getY();

		//north
		if(!OutofBounds(x+1,y) && board[x+1][y].isAlive())
			livingNeighbors++;
		//north east
		if(!OutofBounds(x+1,y+1) && board[x+1][y+1].isAlive())
			livingNeighbors++;
		//east
		if(!OutofBounds(x,y+1) && board[x][y+1].isAlive())
			livingNeighbors++;
		//south east
		if(!OutofBounds(x-1,y+1) && board[x-1][y+1].isAlive())
			livingNeighbors++;
		//south
		if(!OutofBounds(x-1,y) && board[x-1][y].isAlive())
			livingNeighbors++;
		//south west
		if(!OutofBounds(x-1,y-1) && board[x-1][y-1].isAlive())
			livingNeighbors++;
		//west
		if(!OutofBounds(x,y-1) && board[x][y-1].isAlive())
			livingNeighbors++;
		//north west
		if(!OutofBounds(x+1,y-1) && board[x+1][y-1].isAlive())
			livingNeighbors++;
		//if(livingNeighbors>0)
			//System.out.println("\t"+livingNeighbors);

		return livingNeighbors;
	}

	private boolean OutofBounds(int x,int y){
		if(y<0||x<0||x>=dim||y>=dim)
			return true;
		return false;
	}

	private class cell{
		boolean alive;
		JButton body;
		int x,y;

		cell(int x,int y){
			body = new JButton("");
			die();
			//born();
			this.x = x;
			this.y = y; 
		}

		private int getX(){ 
			return x;
		}

		private int getY(){
			return y;
		}

		private JButton giveBirth(){
			born();
			aliveCount++;
			return body;
		}

		private void born(){
			this.alive = true;
			this.body.setBackground(Color.BLACK);
			this.body.setForeground(Color.BLACK);
		}

		private void die(){
			this.alive = false;
			this.body.setBackground(Color.WHITE);
			this.body.setForeground(Color.WHITE);
		}

		private JButton kill(){
			die();
			aliveCount--;
			return body;
		}

		private boolean isAlive(){
			return alive;
		}

		private JButton getBody(){
			return body;
		}
	}

	class ButtonListener implements ActionListener{
		@Override
		public void actionPerformed(ActionEvent e){
			Component whichButton = (Component) e.getSource();
			if(aliveCount<100){
				System.out.println(aliveCount);
				for(int i=0;i<dim;i++){
					for(int j=0;j<dim;j++){
						if(board[i][j].getBody()==whichButton){
							if(!board[i][j].isAlive())
								board[i][j].giveBirth();
							else
								board[i][j].kill();
						}
					}
				}
			}
		}
	}	

	public static void main(String[] args){
		new lifeGUI();
	}
}
