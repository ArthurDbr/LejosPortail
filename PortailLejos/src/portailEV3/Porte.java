package portailEV3;

import lejos.hardware.port.Port;

/**
 * Class who contol the motor of a door
 *
 * @author Arthur Debar - Alexis Petit - Thibaut Godet - Mathis Faivre
 * @version 1.0
 */

public class Porte {

	/** The moteur. */
	ServoMoteur moteur;

	/**
	 * Instantiates a new porte.
	 *
	 * @param port the port
	 */
	public Porte(Port port) {
		moteur = new ServoMoteur(port);
	}

	/**
	 * Ouvrir.
	 */
	public void ouvrir() {
		this.moteur.pousser();

	}
	
	/**
	 * Pauser.
	 *
	 * @param ouvert the ouvert
	 */
	public void pauser(boolean ouvert) {
		this.moteur.stop();
	}

	/**
	 * Fermer.
	 */
	public void fermer() {
		this.moteur.tirer();
	}
	
	/**
	 * Gets the porte.
	 *
	 * @return the porte
	 */
	public ServoMoteur getPorte() {
		return moteur;
	}
	
}
