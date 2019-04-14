package pack;

import java.util.Random;

import lejos.hardware.Battery;
import lejos.hardware.lcd.LCD;
import lejos.hardware.motor.Motor;
import lejos.hardware.port.SensorPort;
import lejos.hardware.sensor.EV3IRSensor;
import lejos.hardware.sensor.EV3TouchSensor;
import lejos.hardware.sensor.SensorMode;
import lejos.robotics.SampleProvider;
import lejos.robotics.subsumption.Behavior;
import lejos.utility.Delay;

//second behavior to back up and rotate when the robot detects an object
public class HitWall implements Behavior {
private Random rnd = new Random();
EV3TouchSensor touchSensor;
SensorMode touchSP;
EV3IRSensor irSensor;
SensorMode distanceSP;
private boolean suppressed = false;

public HitWall() {
touchSensor = new EV3TouchSensor(SensorPort.S3);
touchSP = touchSensor.getTouchMode();

irSensor = new EV3IRSensor(SensorPort.S2);
distanceSP = irSensor.getDistanceMode();
}

public boolean takeControl() { 
return (isTouched(touchSP) || (getDistance(distanceSP) < 60));
}
public void suppress() {
suppressed = true;
}

public void action() {
	LCD.clear();
	LCD.drawString("HitT...", 0, 6);

	suppressed = false;
	Motor.B.setSpeed(300);
	Motor.C.setSpeed(300);
	
	Motor.B.stop();
	Motor.C.stop();
	Delay.msDelay(100);
	
	Motor.B.backward();
	Motor.C.backward();
	Delay.msDelay(500);
	
	Motor.B.forward();
	Motor.C.backward();
	Delay.msDelay(750);
	
	Motor.B.forward();
	Motor.C.forward();
	Delay.msDelay(500);
	
	//Motor.B.rotate(rnd.nextInt(180)-360, true);
	//Motor.C.rotate(rnd.nextInt(180)-360, true);
//	while( Motor.B.isMoving() && !suppressed )
	Thread.yield();
//	Motor.B.stop();
//	Motor.C.stop();
}

// method to read touch sensor and return true or false if touched.
private static boolean isTouched(SampleProvider sp) {
float[] sample = new float[sp.sampleSize()];
sp.fetchSample(sample, 0);
if (sample[0] == 0)
return false;
else
return true;
}

private static float getDistance(SampleProvider sp) {
float[] sample = new float[sp.sampleSize()];
sp.fetchSample(sample, 0);
return sample[0];
}
}
