package pack;

import lejos.hardware.Sound;
import lejos.hardware.lcd.LCD;
import lejos.robotics.subsumption.*;

public class sscontrolclass {
public static void main(String[] args) {
LCD.clear();
//LCD.drawString("LeJOS Subsumption", 0, 5);
Sound.twoBeeps();

Behavior b1 = new MoveForwardSSRL();
Behavior b2 = new HitWall();
//Behavior b3 = new LightCaptureSSRL();
Behavior b4 = new ShutDown();
Behavior [] bArray = {b1, b2, b4};
Arbitrator arby = new Arbitrator(bArray);
arby.start();
}
}
 	