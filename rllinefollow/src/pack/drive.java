package pack;

import lejos.hardware.lcd.LCD;
import lejos.hardware.motor.Motor;
import lejos.hardware.port.SensorPort;
import lejos.hardware.sensor.EV3ColorSensor;
import lejos.robotics.SampleProvider;

public class drive {
	public static final float INVALID = -10;
	public static float invalidAction;
//	public static int MB=20;
//	public static int MC=20;
	
//	public static int GetState() {
//		
//		
//	}
	
	public static void act(int a1, int a2){	
		//float 
		Motor.C.setSpeed(300);
		Motor.B.setSpeed(300);
		invalidAction=0;
		
		if (a1==0 && a2==0)
			invalidAction -=5;
		
		// left
		switch (a1){
		case -1: // Move backward
				Motor.C.backward();
				
			break;
		case 0: // Do nothing 
			Motor.C.stop();
			break;
		case 1: // Move forward
				Motor.C.forward();
			break;
		}
		
		// right
		switch (a2){
		case -1: // Move backward
				Motor.B.backward();
			break;
		case 0: // Do nothing 
			Motor.B.stop();
			break;
		case 1: // Move forward
				Motor.B.forward();
			break;
			default:
				break;
		}
//		
//		if((a1==1) && (a2==1)) {
//			Motor.C.rotate(MC);
//			Motor.B.rotate(MB);
//		}
//		else if((a1==1) && (a2==0)) {
//			Motor.C.rotate(MC);
//			Motor.B.stop();
//		}
//		else if((a1==1) && (a2==-1)) {
//			Motor.C.rotate(MC);
//			Motor.B.rotate(-MB);
//		}
//		else if((a1==0) && (a2==1)) {
//			Motor.C.stop();
//			Motor.B.rotate(MB);
//		}
//		else if((a1==0) && (a2==-1)) {
//			Motor.C.stop();
//			Motor.B.rotate(-MB);
//		}
//		else if((a1==0) && (a2==0)) {
//			Motor.C.stop();
//			Motor.B.stop();
//		}
//		else if((a1==-1) && (a2==1)) {
//			Motor.C.rotate(-MC);
//			Motor.B.rotate(MB);
//		}
//		else if((a1==-1) && (a2==0)) {
//			Motor.C.rotate(-MC);
//			Motor.B.stop();
//		}
//		else if((a1==-1) && (a2==-1)) {
//			Motor.C.rotate(-MC);
//			Motor.B.rotate(-MB);
//		}
		
		
		
		
		
	}
	
	public static int getActionIndex(int a1, int a2){
		return 3*(a1+1) + a2 + 1;
	}

}
