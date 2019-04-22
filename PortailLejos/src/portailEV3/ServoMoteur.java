package portailEV3;

import lejos.hardware.motor.EV3LargeRegulatedMotor;
import lejos.hardware.port.*;
import lejos.robotics.RegulatedMotor;
// TODO: Auto-generated Javadoc

/**
 * Classe who operates the motor
 *
 * @author Arthur Debar - Alexis Petit - Thibaut Godet - Mathis Faivre
 * @version 1.0
 */
public class ServoMoteur {

	/** The moteur. */
	private RegulatedMotor moteur;

	/**
	 * Instantiates a new servo moteur.
	 *
	 * @param port the port
	 */
	ServoMoteur(Port port) {
		this.moteur = new EV3LargeRegulatedMotor(port);
		this.moteur.setSpeed(30);
	}
	
	/**
	 * Gets the moteur.
	 *
	 * @return the moteur
	 */
	public RegulatedMotor getMoteur() {
		return moteur;
	}

	/**
	 * Pousser.
	 */
	void pousser() {
		this.moteur.forward();
	}

	/**
	 * Tirer.
	 */
	void tirer() {
		this.moteur.backward();
	}

	/**
	 * Stop.
	 */
	void stop() {
		this.moteur.stop();
	}
}
