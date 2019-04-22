package etat;

public class EtatPorteFermer implements EtatPorte {

	@Override
	public void ouverture() {
		System.out.println("Porte en ouverture");
	}

	@Override
	public void fermeture() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void pauser() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void fermer() {
		System.out.println("Porte déja fermé");
		
	}

	@Override
	public void ouvert() {
		System.out.println("Porte déja ouverte");
		
	}

	@Override
	public void getEtat() {
		System.out.println("Porte fermé");
		
	}

	@Override
	public void initialisation() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void ouvertPartiellement() {
		// TODO Auto-generated method stub
		
	}

}
