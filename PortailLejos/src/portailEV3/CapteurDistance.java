package portailEV3;

import lejos.hardware.port.Port;
import lejos.hardware.sensor.EV3UltrasonicSensor;

// TODO: Auto-generated Javadoc
/**
 * The Class CapteurDistance.
 */
public class CapteurDistance {

	/** The capteur distance. */
	EV3UltrasonicSensor capteurDistance;

	/**
	 * Instantiates a new capteur distance.
	 *
	 * @param port the port
	 */
	CapteurDistance(Port port) {
		this.capteurDistance = new EV3UltrasonicSensor(port);
	}

	/**
	 * Contact.
	 *
	 * @return true, if successful
	 */
	boolean contact() {

		float[] sample = new float[capteurDistance.sampleSize()];
		capteurDistance.fetchSample(sample, 0);

		float etat = sample[0];
		
		if (etat < 0.5 )
			return true;
		else
			return false;

	}

}
