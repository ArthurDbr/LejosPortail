package portailEV3;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

import lejos.hardware.Bluetooth;
import lejos.hardware.lcd.LCD;
import lejos.remote.nxt.BTConnection;
import lejos.remote.nxt.BTConnector;
import lejos.remote.nxt.NXTConnection;
import lejos.utility.Delay;


/**
 * Main class that interacts with the portal controller and connects to the android application.
 *
 * @author Arthur Debar - Alexis Petit - Thibaut Godet - Mathis Faivre
 * @version 1.0
 */

public class MainControleur {
	
	/** The data out. */
	private static DataOutputStream dataOut; 
	
	/** The data in. */
	public static DataInputStream dataIn;
	
	/** The BT link. */
	private static BTConnection BTLink;
	
	/** The transmit received. */
	private static int transmitReceived=0;
	
	/** The app alive. */
	private static boolean app_alive;

	/**
	 * The main method.
	 *
	 * @param args the arguments
	 * @throws InterruptedException the interrupted exception
	 */
	@SuppressWarnings("unlikely-arg-type")
	public static void main(String[] args) throws InterruptedException{
		
		//Creation du controleur pour le portail
		ControleurPortail portail;
		portail = new ControleurPortail();
		//On lance la connexion à l'application android
		connect();

		app_alive = true;
		while(app_alive){
			try {
				transmitReceived = (int) dataIn.readByte(); 
				
				switch(transmitReceived){
					case 1:
						portail.ouverturePartielle();
						break;
					case 2: 
						portail.ouvertureTotale();
						break;	
					case 3 :
						app_alive = false;
						break;
				}
			}

			catch (IOException ioe) {
				System.out.println("IO Exception readInt");
				app_alive = false;
			}
		}
		//On arret les threads des capteurs de contact
		portail.capteurPortailFerme.arret();
		portail.capteurGaucheOuvert.arret();
		try {
			dataOut.close();
			dataIn.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}

	/**
	 * Connection to the android app
	 */
	public static void connect()
	{  
		System.out.println("En ecoute");
		BTConnector ncc = (BTConnector) Bluetooth.getNXTCommConnector();
		BTLink = (BTConnection) ncc.waitForConnection(30, NXTConnection.RAW);
		dataOut = BTLink.openDataOutputStream();
		dataIn = BTLink.openDataInputStream();
	}


}