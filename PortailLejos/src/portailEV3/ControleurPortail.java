package portailEV3;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import etat.EtatPorte;
import etat.EtatPorteFermer;
import etat.EtatPorteFermeture;
import etat.EtatPorteOuvert;
import etat.EtatPorteOuvertPartiellement;
import etat.EtatPorteOuverture;
import etat.EtatPorteOuverturePartiel;
import etat.EtatPortePartiel;
import lejos.hardware.lcd.LCD;
import lejos.hardware.port.MotorPort;
import lejos.hardware.port.SensorPort;
import lejos.hardware.sensor.EV3TouchSensor;
import lejos.robotics.RegulatedMotor;
import lejos.utility.Delay;
// TODO: Auto-generated Javadoc

/**
 * Class who control the sensor of the portal and the motor of the doors
 *
 * @author Arthur Debar - Alexis Petit - Thibaut Godet - Mathis Faivre
 * @version 1.0
 */


public class ControleurPortail {

	//Declaration of variables

	/** The capteur gauche ouvert. */
	public CapteurContact capteurGaucheOuvert;
	
	/** The capteur portail ferme. */
	public CapteurContact capteurPortailFerme;

	/** The porte gauche. */
	private Porte porteGauche;
	
	/** The porte droite. */
	private Porte porteDroite;


	/** The etat courant. */
	private EtatPorte etatCourant;
	
	/** The etat fermer. */
	private EtatPorte etatFermer;
	
	/** The etat fermeture. */
	private EtatPorte etatFermeture;
	
	/** The etat ouvert. */
	private EtatPorte etatOuvert;
	
	/** The etat ouverture. */
	private EtatPorte etatOuverture;
	
	/** The etat partiel. */
	private EtatPorte etatPartiel;
	
	/** The etat ouvert partiellement. */
	private EtatPorte etatOuvertPartiellement;
	
	/** The etat ouverture partiel. */
	private EtatPorte etatOuverturePartiel;
	
	/** The liste etat. */
	private ArrayList<EtatPorte> listeEtat;

	/**
	 * Instantiates a new controleur portail.
	 */
	public ControleurPortail() {
		//Initialisation des capteurs de contact avec l'attribution des ports 
		//et le lancement des threads
		capteurGaucheOuvert = new CapteurContact(new EV3TouchSensor(SensorPort.S1));
		this.capteurGaucheOuvert.setController(this);
		this.capteurGaucheOuvert.start();
		
		capteurPortailFerme = new CapteurContact(new EV3TouchSensor(SensorPort.S3));
		this.capteurPortailFerme.setController(this);
		this.capteurPortailFerme.start();
		
		//Initialisation des portes qui vont controller des moteurs
		//et attribution des ports
		porteGauche = new Porte(MotorPort.A);
		porteDroite = new Porte(MotorPort.B);
		
		//On fixe l'état courant à fermer
		this.etatCourant 	= new EtatPorteFermer();
		
		this.etatFermer 			= new EtatPorteFermer();
		this.etatFermeture 			= new EtatPorteFermeture();
		this.etatOuvert 			= new EtatPorteOuvert();
		this.etatOuverture 			= new EtatPorteOuverture();
		this.etatPartiel			= new EtatPortePartiel();
		this.etatOuverturePartiel	= new EtatPorteOuverturePartiel();
		this.listeEtat				= new ArrayList<EtatPorte>();
		this.etatOuvertPartiellement 	= new EtatPorteOuvertPartiellement();
		
		try {
			//Permet de lancer un un test pour vérifier que les moteurs et les capteurs
			//fonctionnent
			initialisation();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
	}
	
	/**
	 * Initialisation.
	 *
	 * @throws InterruptedException the interrupted exception
	 */
	public void initialisation() throws InterruptedException{
		System.out.println("Initialisation");
		ouvertureTotale();
		TimeUnit.MILLISECONDS.sleep(5000);
		ouvertureTotale();
		TimeUnit.MILLISECONDS.sleep(3000);
		System.out.println("Initialisation termine");
	}
	
	/**
	 * Ouverture partielle.
	 *
	 * @throws InterruptedException the interrupted exception
	 */
	public void ouverturePartielle() throws InterruptedException{
		if(etatCourant instanceof EtatPorteFermer) {
			porteGauche.ouvrir();
			
			while(capteurGaucheOuvert.isContact()) {}
			Thread.sleep(500);
			System.out.println("En ouverture partielle");
			
			etatCourant = etatOuverturePartiel;			
			sauverEtat(etatCourant);
		}else if (etatCourant instanceof EtatPorteOuverturePartiel) {
			porteGauche.pauser(true);
			etatCourant = etatOuvertPartiellement;
			sauverEtat(etatCourant);
		}else if(etatCourant instanceof EtatPorteOuvertPartiellement ||
				etatCourant instanceof EtatPorteOuvert ||
				etatCourant instanceof EtatPortePartiel) {
			fermeture();
		}
	}
	
	/**
	 * Ouverture totale.
	 *
	 * @throws InterruptedException the interrupted exception
	 */
	public void ouvertureTotale() throws InterruptedException {
		if(etatCourant instanceof EtatPorteFermer) {
			porteGauche.ouvrir();
			porteDroite.ouvrir();

			while(capteurGaucheOuvert.isContact()) {}
			Thread.sleep(500);
			System.out.println("Portes en ouverture");
			
			etatCourant = etatOuverture;
			sauverEtat(etatCourant);
		} else if(etatCourant instanceof EtatPorteOuvert) {
			fermeture();
		} else if(etatCourant instanceof EtatPorteOuverture) {
			porteGauche.pauser(true);
			porteDroite.pauser(true);
			
			System.out.println("Porte en pause");
			
			etatCourant = etatPartiel;
			sauverEtat(etatCourant);
		} else if(etatCourant instanceof EtatPortePartiel) {
			fermeture();
		}
	}
	
	/**
	 * Fermeture.
	 *
	 * @throws InterruptedException the interrupted exception
	 */
	public void fermeture() throws InterruptedException {
		/*Lorsque les deux portes du portail sont ouvertes totalement
		ou partiellement, on ferme le portail*/
		if(etatCourant instanceof EtatPorteOuvert || 
					etatCourant instanceof EtatPortePartiel) {
			porteGauche.fermer();
			porteDroite.fermer();

			while(capteurPortailFerme.isContact()) {}
			Thread.sleep(500);
			
			System.out.println("Fermeture");
			
			etatCourant = etatFermeture;
			sauverEtat(etatCourant);
		} 
		/*Lorsque le portail est en fermeture on pause les portes*/
		else if(etatCourant instanceof EtatPorteFermeture) {
			porteGauche.pauser(true);
			porteDroite.pauser(true);
			
			System.out.println("Etat pause");
			
	        etatCourant = etatPartiel;
			sauverEtat(etatCourant);
		} 
		/*Lorsque le portail a la porte de gauche ouverte partiellement on la ferme*/
		else if(etatCourant instanceof EtatPorteOuvertPartiellement) {
			porteGauche.fermer();
			
			while(capteurPortailFerme.isContact()) {}
			Thread.sleep(500);
			System.out.println("Fermeture Partielle");
			
			etatCourant = etatFermeture;
	        sauverEtat(etatCourant);
		}
	}
	
	/**
	 * Contact.
	 *
	 * @throws InterruptedException the interrupted exception
	 */
	public void contact() throws InterruptedException  {
		if(etatCourant instanceof EtatPorteFermeture) {
			System.out.println("Portes ferme");
			porteGauche.pauser(true);
			porteDroite.pauser(true);
	        etatCourant = etatFermer; 
	        sauverEtat(etatCourant);
		} else if(etatCourant instanceof EtatPorteOuverture) {
			System.out.println("Portes Ouverte");
			porteGauche.pauser(true);
			porteDroite.pauser(true);
	        etatCourant = etatOuvert; 
	        sauverEtat(etatCourant);
		} else if(etatCourant instanceof EtatPorteOuverturePartiel) {
			System.out.println("Porte ouverte partiellement");
			porteGauche.pauser(true);
			etatCourant = etatOuvertPartiellement;
			sauverEtat(etatCourant);
		}
	}
	
	/**
	 * Gets the etat.
	 *
	 * @return etatCourant
	 */
	public EtatPorte getEtat() {
		return etatCourant;
	}
	
	/**
	 * Sauver etat.
	 *
	 * @param etatCourant the etat courant
	 */
	public void sauverEtat(EtatPorte etatCourant) {
		listeEtat.add(etatCourant);
	}
	
	/**
	 * Sets the etat.
	 *
	 * @param etat the new etat
	 */
	public void setEtat(EtatPorte etat) {
		etatCourant = etat;
	}
	
	/**
	 * Sauver contact.
	 *
	 * @throws InterruptedException the interrupted exception
	 */
	public void sauverContact()throws InterruptedException {
		contact();
	}
	
}

