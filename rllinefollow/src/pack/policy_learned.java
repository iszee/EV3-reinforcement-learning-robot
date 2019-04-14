package pack;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.*;

import lejos.hardware.Button;
import lejos.hardware.lcd.LCD;

public class policy_learned {
	public static float[][] Q; 
	public static int[][] A;
	public static float alpha;
	public static float gamma;
	public static float epsilon;
	public static int nStates;
	public static int nActions;
	public static writefile wf;
	//traceability
	public static float[][] e;
	public static float lmbda;
	
	policy_learned(int s, int a,float alf,float gam,float eps, float lm){

		alpha = alf;
		gamma = gam;
		epsilon = eps;
		nStates = s;
		nActions = a;
		Q = new float[s][a]; // temp solution make Q bigger 
		A = new int[a][2];
		//trace
		e = new float[s][a];
		lmbda = lm;
		wf = new writefile("./Qnewbat8.txt",true);
		for (int a1=-1;a1<=1;a1++)
			for (int a2=-1;a2<=1;a2++){
			A[3*(a1+1)+a2+1][0]=a1;
			A[3*(a1+1)+a2+1][1]=a2;
		}
		for (int i=0;i<s;i++){
			for(int j=0;j<a;j++){
				Q[i][j] = 0f; //(float) Math.random();
				//initialize eligiability trace
				e[i][j] = 0f;
			}
		}

	}

	public int[] getBestAction(int state){
		return findActions(greedy_behavior_policy(state));
	}
	//Find the index of greedy action 
	public int greedy_behavior_policy(int s){
		int stateIndex = s;
		float[] Q_part = Q[stateIndex];
		
		int maxIndex = findMax(Q_part);
		LCD.drawString("Best"+maxIndex+"="+Q_part[maxIndex], 1, 6);
		LCD.refresh();
		double chance = Math.random();
		Random rand = new Random();
		
		if (chance < epsilon ){
			return rand.nextInt(9);
		}
		else
			return maxIndex;
	}
	//Find the greedy action pair
	public int[] findActions(int actionIndex){
		int[] actions = new int[2];
		actions[0] = A[actionIndex][0];
		actions[1] = A[actionIndex][1];
		return actions;
	}
	
	
	//Return the index of the maximum value of an Array
	public int findMax(float[] Q_part){
		//float[] array = Q_part;
		int max = 0;
		float maxValue = Q_part[0];
		for (int i = 0; i < Q_part.length; i++) {
		    if (Q_part[i] > maxValue) {
		      max = i;
		    }
		}
		return max;
	}
	
	public float maxQ(int s){
		float[] Q_part = Q[s];
		int max = findMax(Q_part);
		float maxValue = Q_part[max];
		return maxValue;
	}
	
	
	public void Q_learning(int state, int S_1, int action, float reward){
		// update Q matrix
		// Q[s][a]=Q[s][a]+alpha*(R + gamma max Qt - Qt)
		//LCD.drawString("S_1 "+S_1+"  ", 1, 6);
		//LCD.refresh();
		float update = reward + gamma * maxQ(S_1)-Q[state][action];
		
		Q[state][action] = Q[state][action]+alpha*update;
		//epsilon=(float) (epsilon*0.9);
	}

	
	//Q_lambda_learning program
	public void Q_lambda_learning(int state, int S_1, int action, float reward, int[] A_1){
		float update = reward + gamma *maxQ(S_1)-Q[state][action];
		int greedyAction = findMax(Q[S_1]);
		int realAction = (1+A_1[0])*3+(A_1[1]+1);
		e[state][action] = e[state][action] + 1 ;//replacing traces
		
		//Q[state][action] = Q[state][action]+alpha*update*e[state][action];
		

		
		for (int i=0;i<nStates;i++){
		    for(int j=0;j<nActions;j++){
			Q[i][j] = Q[i][j]+alpha*update*e[i][j];
			
			if(greedyAction == realAction){
			    e[i][j] = gamma*lmbda*e[i][j];
			    Button.LEDPattern(1);
			}
			else
			    e[i][j] = 0;
				Button.LEDPattern(2);
		    }
		}
	}
	
	public void Q_learned_smooth(int state, int S_1, int action, float reward){
		// update Q matrix
		// Q[s][a]=Q[s][a]+alpha*(R + gamma max Qt - Qt)
		//LCD.drawString("S_1 "+S_1+"  ", 1, 6);
		//LCD.refresh();
		float update = reward + gamma * maxQ(S_1)-Q[state][action];
		
		Q[state][action] = (float) (Q[state][action]+0.00001*update);
		//epsilon=(float) (epsilon*0.9);
	}
	
	public void Q_learned_run(int state, int S_1, int action, float reward){
		// update Q matrix
		// Q[s][a]=Q[s][a]+alpha*(R + gamma max Qt - Qt)
		//LCD.drawString("S_1 "+S_1+"  ", 1, 6);
		//LCD.refresh();
		
//		float update = reward + gamma * maxQ(S_1)-Q[state][action];
		
		Q[state][action] = Q[state][action];
		//epsilon=(float) (epsilon*0.9);
	}

	public void dumpQ(){
		for (int i=0;i<nStates;i++){
			for (int j=0;j<nActions;j++)
			{
				try {
					wf.writeToFile(""+Q[i][j]+",");
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			try {
				wf.writeToFile("\n");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	public void loadQ() throws FileNotFoundException {
		File f= new File ("./Qnewbat7.txt");
	    Scanner sc = new Scanner(f);  
	    sc.useDelimiter("\\Z"); 
	    String st=sc.next();
	    String[] tokens = st.split(",");
	    int k=0;
	    
		for (int i=0;i<nStates;i++){
			for (int j=0;j<nActions;j++)
			{
				Q[i][j]=(float) Double.parseDouble(tokens[k]);
				k++;
				}
			}
	}
	

	
	

}
