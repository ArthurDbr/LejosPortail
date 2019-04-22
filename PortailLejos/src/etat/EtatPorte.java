package etat;

public interface EtatPorte {
	public void initialisation();
	public void ouverture();
	public void fermeture();
	public void pauser();
	public void fermer();
	public void ouvert();
	public void ouvertPartiellement();
	public void getEtat();
}
