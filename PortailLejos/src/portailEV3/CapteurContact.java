package portailEV3;

import lejos.hardware.lcd.LCD;
import lejos.hardware.port.Port;
import lejos.hardware.sensor.EV3TouchSensor;
import lejos.utility.Delay;
// TODO: Auto-generated Javadoc

/**
 * Class implementing the threads to manage the door contact
 *
 * @author Arthur Debar - Alexis Petit - Thibaut Godet - Mathis Faivre
 * @version 1.0
 */
public class CapteurContact extends Thread  {

	/** The capteur contact. */
	private EV3TouchSensor capteurContact;
	
	/** The controleur. */
	private ControleurPortail controleur;
	
	/** The marche. */
	private boolean marche;

	/**
	 * Instantiates a new capteur contact.
	 *
	 * @param _capteur the capteur
	 */
	public CapteurContact(EV3TouchSensor _capteur) {
		this.capteurContact = _capteur; 
		this.marche = true ;
	}
	
	/**
	 * Gets the running.
	 *
	 * @return the running
	 */
	public boolean getRunning() {
		return marche;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Thread#run()
	 */
	@Override
	public void run() {
		try {
			this.contact();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Contact.
	 *
	 * @throws InterruptedException the interrupted exception
	 */
	public void contact() throws InterruptedException {
		while (marche) {
			int size = this.capteurContact.sampleSize();
			float[] sample = new float[size];
			capteurContact.fetchSample(sample,0);
			if( sample[0] == 1.0) {
				this.controleur.sauverContact();
			}
		}
	}
	
	/**
	 * Checks if is contact.
	 *
	 * @return true, if is contact
	 */
	public boolean isContact() {
		int size = this.capteurContact.sampleSize();
		float[] sample = new float[size];
		capteurContact.fetchSample(sample,0);
		if( sample[0] == 1.0) {
			return true;
		}
		return false;
	}

	/**
	 * Gets the controller.
	 *
	 * @return the controller
	 */
	public ControleurPortail getController() {
		return controleur;
	}

	/**
	 * Sets the controller.
	 *
	 * @param controleur the new controller
	 */
	public void setController(ControleurPortail controleur) {
		this.controleur = controleur;
	}

	/**
	 * Arret.
	 */
	public void arret() {
		marche = false ;
	}

}
