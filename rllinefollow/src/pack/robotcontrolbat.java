package pack;

import lejos.hardware.device.DLights;

import java.io.FileNotFoundException;

import lejos.hardware.Battery;
import lejos.hardware.Button;
import lejos.hardware.LED;
import lejos.hardware.Sound;
import lejos.hardware.lcd.LCD;
import lejos.hardware.motor.Motor;
import lejos.hardware.port.SensorPort;
import lejos.hardware.sensor.EV3ColorSensor;
import lejos.hardware.sensor.EV3IRSensor;
import lejos.robotics.RangeFinderAdapter;
import lejos.robotics.SampleProvider;
import lejos.robotics.objectdetection.Feature;
import lejos.robotics.objectdetection.FeatureDetector;
import lejos.robotics.objectdetection.FeatureListener;
import lejos.robotics.objectdetection.RangeFeatureDetector;
import lejos.utility.Delay;

public class robotcontrolbat {
	public static int LightId; //6 in path
	static final int MaxSteps = 50000;
    public static int pathColorId;
    public static int ChargepathColorId=7; //	charging path color 7
	public static int notPathColorId;
	public static int []a_1;
	
	static float batteryLevel;
//	static float batteryLevel2;
//	static float batteryConsumedPercentage;

	
	
	static EV3ColorSensor colorsensor=new EV3ColorSensor(SensorPort.S1);
	
	public static void main(String[] args) throws FileNotFoundException {
		
		float alpha = (float) 0.2;
		float gamma = (float) 0.8;
		float epsilon = (float) 0.001;
		float lmbda = 0.9f;
		float rewardSum =0;
		int state;
		int stateVolume=37;
		int actionVolume=9;
		int S_1;
		
		
		int colorId1=0; //
		int colorId2=0; //path
		int color1Value=0;
		int color2Value=0;
		
		policy_learned plearn = new policy_learned(stateVolume,actionVolume,alpha,gamma,epsilon,lmbda);
		//learned policy
		
		plearn.loadQ();
		//loading values
		
		colorId1=colorsensor.getColorID();
		LCD.clear();
		LCD.drawString("color1 "+colorId1, 0, 1);
		for(int i=0;i<2;i++) {
			
			while(colorId1==colorsensor.getColorID()) {
				Motor.B.forward();
				Motor.C.forward();
				color1Value++;
			}
			Motor.B.stop();
			Motor.C.stop();
			
			colorId2=colorsensor.getColorID();
			LCD.drawString("color2 "+colorId2, 0, 2);
			while(colorId2==colorsensor.getColorID()) {
				Motor.B.forward();
				Motor.C.forward();
				color2Value++;
			}
			Motor.B.stop();
			Motor.C.stop();
			
//			Motor.B.rotate(90);
//			Motor.C.rotate(-90);
//			
//			Motor.B.rotate(100);
//			Motor.C.rotate(100);
//			
//			Motor.B.rotate(100);
//			Motor.C.rotate(100);
//			
//			Motor.B.rotate(100);
//			Motor.C.rotate(100);
//			
//			Motor.B.rotate(90);
//			Motor.C.rotate(-90);
//			
				
			
		}
		
		
		
		if(color1Value>color2Value) {
			pathColorId=colorId2;
			notPathColorId=colorId1;
		}
		else {
			pathColorId=colorId1;
			notPathColorId=colorId2;
		}
		
		LCD.clear();
		LCD.drawString("path color "+pathColorId, 0, 1);
		LCD.drawString("env color "+notPathColorId, 0, 2);
		Sound.twoBeeps();
		Delay.msDelay(1000);
		Sound.beepSequenceUp();
		
		
		
		 for (int j=1;j<MaxSteps;j++){
			 
			 if(Button.ESCAPE.isDown())
				 break;
			 batteryLevel=Battery.getVoltage();
			 
			 if (colorsensor.getColorID()==pathColorId)
				 state= 1*(int)batteryLevel;
			 else if(colorsensor.getColorID()==notPathColorId)
				 state= 4*(int)batteryLevel;
			 
			 else if (colorsensor.getColorID()==ChargepathColorId) {
				 state=0;
//			 	batteryLevel=8;
			 	}
			 
			 else
				 state=10;
//			 getstate
			 
			 int[] a=plearn.getBestAction(state);
			 drive.act(a[0],a[1]);
//			 bestaction
			 
			 
			 float R=getReward();
			 
			 if(R>0){
				 Button.LEDPattern(1);
			 }
			 else {
				 Button.LEDPattern(2);
			 }
			 rewardSum=rewardSum+R;
			 LCD.clear();
			 LCD.drawString("Reward "+R+"  ", 1, 3);
        	 LCD.drawString("Avg "+rewardSum/j+" ", 1, 4);
        	 int pending = MaxSteps-j;
        	 LCD.drawString("Pending "+pending+"  ", 1, 5);
        	 LCD.drawString("Voltage "+Float.toString(batteryLevel)+" ", 1, 2);
        	 LCD.refresh();
         	 Delay.msDelay(1);
//			 reward
         	 
         	 
         	batteryLevel=Battery.getVoltage();
         	
			 if (colorsensor.getColorID()==pathColorId)
				 S_1= 1*(int)batteryLevel;
			 else if(colorsensor.getColorID()==notPathColorId)
				 S_1= 4*(int)batteryLevel;
			 
			 else if (colorsensor.getColorID()==ChargepathColorId) {
				 S_1=0;
//				 batteryLevel=8;
			 }
			 
			 else
				 S_1=10;
			
			a_1=plearn.getBestAction(S_1);
			
			 plearn.Q_learning(state, S_1, drive.getActionIndex(a[0], a[1]), R);
//			learn
			 
//			 plearn.Q_learned_smooth(state, S_1, drive.getActionIndex(a[0], a[1]), R);
//        	learned
        	
//        	plearn.Q_lambda_learning(state, S_1, drive.getActionIndex(a[0], a[1]), R,a_1);
//			switch to Q_lambda_learning		 
		 }
		 
		 plearn.dumpQ();
		 //write learned
	}
	
	public static float getReward() {
		float reward;
		LightId=colorsensor.getColorID();
		batteryLevel=Battery.getVoltage();
		int tempbat=(int)batteryLevel;
		
		if (LightId==ChargepathColorId) {
			reward=18+drive.invalidAction+(tempbat-5);
//			batteryLevel=8;
		}
		
		if (LightId==pathColorId)
			reward=10+drive.invalidAction+tempbat;
		else if (LightId==notPathColorId)
			reward=-20+drive.invalidAction-2*(8-tempbat);
		
		else if (LightId==ChargepathColorId) {
			
			reward=drive.invalidAction+20*(8-tempbat);
//			batteryLevel=8;
		}
		
		else
			reward=-15+drive.invalidAction-2*(8-tempbat);
		return reward;
		
	}

}
