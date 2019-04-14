package pack;

import lejos.hardware.Button;
import lejos.hardware.port.SensorPort;
import lejos.hardware.sensor.EV3ColorSensor;
import lejos.robotics.SampleProvider;
import lejos.utility.Delay;

public class dump {

	//Robot Configuration
	private static EV3ColorSensor color1 = new EV3ColorSensor(SensorPort.S4);

	//Configuration
	private static int HALF_SECOND = 500;

	public static void main(String[] args) {
        //Ambient Mode
		System.out.println("Switching to Ambient Mode");
		SampleProvider sp = color1.getAmbientMode();

		int sampleSize = sp.sampleSize();
		float[] sample = new float[sampleSize];

        // Takes some samples and prints them
        while (Button.ESCAPE.isUp()) {
        	sp.fetchSample(sample, 0);
			System.out.println((float)sample[0]);
            Delay.msDelay(HALF_SECOND);
        }
	}

}