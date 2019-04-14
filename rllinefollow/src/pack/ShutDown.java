package pack;

import lejos.hardware.Battery;
import lejos.hardware.Button;
import lejos.hardware.Sound;
import lejos.hardware.lcd.LCD;
import lejos.robotics.subsumption.Behavior;

public class ShutDown implements Behavior {

public boolean takeControl() {
return (Button.readButtons()!=0);
}

public void suppress() {
}

public void action() {
LCD.clear();
LCD.drawString("Now Exitting...", 0, 6);
//float voltage=Battery.getVoltage();
//LCD.drawString(Float.toString(voltage), 1, 6);
Sound.beepSequence();
System.exit(0);
}
}
