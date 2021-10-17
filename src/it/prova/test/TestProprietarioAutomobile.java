package it.prova.test;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.hibernate.LazyInitializationException;

import it.prova.model.Automobile;
import it.prova.model.Proprietario;
import it.prova.service.automobile.AutomobileService;
import it.prova.service.proprietario.ProprietarioService;

public class TestProprietarioAutomobile {
	public static void main(String[] args) {
		ProprietarioService proprietarioService = it.prova.service.MyServiceFactory.getMunicipioServiceInstance();
		AutomobileService automobileService = it.prova.service.MyServiceFactory.getAbitanteServiceInstance();

		try {

			// ora con il service posso fare tutte le invocazioni che mi servono
			System.out.println(
					"In tabellaci sono " + proprietarioService.listAll().size() + " elementi.");

			testInserisciProprietario(proprietarioService);
			System.out.println(
					"In tabella ci sono " + proprietarioService.listAll().size() + " elementi.");

			testInserisciAutomobile(proprietarioService, automobileService);
			System.out.println(
					"In tabella ci sono " + proprietarioService.listAll().size() + " elementi.");

			testRimozioneAutomobile(proprietarioService, automobileService);
			System.out.println(
					"In tabella ci sono " + proprietarioService.listAll().size() + " elementi.");
			
			testContaQuantiProprietariPossiedonoAutoImmatricolataDopoData(proprietarioService, automobileService);
			System.out.println(
					"In tabella ci sono " + proprietarioService.listAll().size() + " elementi.");
			
			testCercaTuttiByProprietarioCheHannoUnCfCheIniziaPer(proprietarioService, automobileService);
			System.out.println(
					"In tabella ci sono " + proprietarioService.listAll().size() + " elementi.");


			testLazyInitExc(proprietarioService, automobileService);

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			// questa è necessaria per chiudere tutte le connessioni quindi rilasciare il
			// main
			it.prova.dao.EntityManagerUtil.shutdown();
		}
	}
	
	private static void testInserisciProprietario(ProprietarioService proprietarioService) throws Exception {
		System.out.println(".......testInserisci inizio.............");
		// creo nuovo municipio
		Proprietario nuovoProprietario = new Proprietario("Francesco", "totti", "AA12", new java.util.Date());
		if (nuovoProprietario.getId() != null)
			throw new RuntimeException("testInserisci fallito: record già presente ");

		// salvo
		proprietarioService.inserisciNuovo(nuovoProprietario);
		// da questa riga in poi il record, se correttamente inserito, ha un nuovo id
		// (NOVITA' RISPETTO AL PASSATO!!!)
		if (nuovoProprietario.getId() == null)
			throw new RuntimeException("testInserisci fallito ");

		System.out.println(".......testInserisci fine: PASSED.............");
	}

	private static void testInserisciAutomobile(ProprietarioService proprietarioService, AutomobileService automobileService)
			throws Exception {
		System.out.println(".......testInserisci inizio.............");

		// creo nuovo abitante ma prima mi serve un municipio
		List<Proprietario> listaPropretariPresenti = proprietarioService.listAll();
		if (listaPropretariPresenti.isEmpty())
			throw new RuntimeException("testInserisci fallito: non ci sono municipi a cui collegarci ");

		Automobile nuovoAutomobile = new Automobile("Alfa", "147","TE320" , new SimpleDateFormat("yyyy-MM-dd").parse("2000-10-13"));
		// lo lego al primo municipio che trovo
		nuovoAutomobile.setProprietario(listaPropretariPresenti.get(0));

		// salvo il nuovo abitante
		automobileService.inserisciNuovo(nuovoAutomobile);

		// da questa riga in poi il record, se correttamente inserito, ha un nuovo id
		// (NOVITA' RISPETTO AL PASSATO!!!)
		if (nuovoAutomobile.getId() == null)
			throw new RuntimeException("testInserisci fallito ");

		// il test fallisce anche se non è riuscito a legare i due oggetti
		if (nuovoAutomobile.getProprietario() == null)
			throw new RuntimeException("testInserisci fallito: non ha collegato il municipio ");

		System.out.println(".......testInserisci fine: PASSED.............");
	}

	private static void testRimozioneAutomobile(ProprietarioService proprietarioService, AutomobileService automobileService)
			throws Exception {
		System.out.println(".......testRimozione inizio.............");

		// inserisco un abitante che rimuoverò
		// creo nuovo abitante ma prima mi serve un municipio
		List<Proprietario> listaProprietariPresenti = proprietarioService.listAll();
		if (listaProprietariPresenti.isEmpty())
			throw new RuntimeException("testRimozione fallito: non ci sono municipi a cui collegarci ");

		Automobile nuovoAutomobile = new Automobile("Alfa", "147","TE320" , new SimpleDateFormat("yyyy-MM-dd").parse("2000-10-13"));
		// lo lego al primo municipio che trovo
		nuovoAutomobile.setProprietario(listaProprietariPresenti.get(0));

		// salvo il nuovo abitante
		automobileService.inserisciNuovo(nuovoAutomobile);

		Long idAbitanteInserito = nuovoAutomobile.getId();
		automobileService.rimuovi(automobileService.caricaSingolo(idAbitanteInserito));
		// proviamo a vedere se è stato rimosso
		if (automobileService.caricaSingolo(idAbitanteInserito) != null)
			throw new RuntimeException("testRimozione fallito: record non cancellato ");
		System.out.println(".......testRimozione fine: PASSED.............");
	}

	

	private static void testLazyInitExc(ProprietarioService proprietarioService, AutomobileService automobileService)
			throws Exception {
		System.out.println(".......testLazyInitExc inizio.............");

		// prima mi serve un municipio
		List<Proprietario> listaProprietariPresenti = proprietarioService.listAll();
		if (listaProprietariPresenti.isEmpty())
			throw new RuntimeException("testLazyInitExc fallito: non ci sono municipi a cui collegarci ");

		Proprietario proprietarioSuCuiFareIlTest = listaProprietariPresenti.get(0);
		// se interrogo la relazione devo ottenere un'eccezione visto che sono LAZY
		try {
			proprietarioSuCuiFareIlTest.getAutomobili().size();
			// se la riga sovrastante non da eccezione il test fallisce
			throw new RuntimeException("testLazyInitExc fallito: eccezione non lanciata ");
		} catch (LazyInitializationException e) {
			// 'spengo' l'eccezione per il buon fine del test
		}
		// una LazyInitializationException in quanto il contesto di persistenza è chiuso
		// se usiamo un caricamento EAGER risolviamo...dipende da cosa ci serve!!!
		// municipioService.caricaSingoloMunicipioConAbitanti(...);
		System.out.println(".......testLazyInitExc fine: PASSED.............");
	}
	
	private static void testCercaTuttiByProprietarioCheHannoUnCfCheIniziaPer(ProprietarioService proprietarioService, AutomobileService automobileService)
			throws Exception {
		System.out.println(".......testCercaTuttiByProprietarioCheHannoUnCfCheIniziaPer inizio.............");

		// inserisco un abitante che rimuoverò
		// creo nuovo abitante ma prima mi serve un municipio
		Proprietario nuovoProprietario = new Proprietario("Francesco", "totti", "CC1", new java.util.Date());
		if (nuovoProprietario.getId() != null)
			throw new RuntimeException("testInserisci fallito: record già presente ");

		// salvo
		proprietarioService.inserisciNuovo(nuovoProprietario);

		Automobile nuovoAutomobile = new Automobile("bmw", "e92","abc02" , new SimpleDateFormat("yyyy-MM-dd").parse("2000-10-13"));
		// lo lego al primo municipio che trovo
		nuovoAutomobile.setProprietario(nuovoProprietario);

		// salvo il nuovo abitante
		automobileService.inserisciNuovo(nuovoAutomobile);

		// proviamo a vedere se è stato rimosso
		if (	automobileService.cercaTuttiByProprietarioCheHannoUnCfCheIniziaPer("CC").size() != 1)
			throw new RuntimeException("testCercaTuttiByProprietarioCheHannoUnCfCheIniziaPer fallito:  ");
		
		automobileService.rimuovi(nuovoAutomobile);
		proprietarioService.rimuovi(nuovoProprietario);
		
		System.out.println(".......testCercaTuttiByProprietarioCheHannoUnCfCheIniziaPer fine: PASSED.............");
		
	}
	
	private static void testContaQuantiProprietariPossiedonoAutoImmatricolataDopoData(ProprietarioService proprietarioService, AutomobileService automobileService)
			throws Exception {
		System.out.println(".......testCercaTuttiByProprietarioCheHannoUnCfCheIniziaPer inizio.............");

		// inserisco un abitante che rimuoverò
		// creo nuovo abitante ma prima mi serve un municipio
		Proprietario nuovoProprietario = new Proprietario("Francesco", "totti", "CC1", new java.util.Date());
		if (nuovoProprietario.getId() != null)
			throw new RuntimeException("testInserisci fallito: record già presente ");

		// salvo
		proprietarioService.inserisciNuovo(nuovoProprietario);

		Automobile nuovoAutomobile = new Automobile("bmw", "e92","abc02" , new SimpleDateFormat("yyyy-MM-dd").parse("2022-10-13"));
		// lo lego al primo municipio che trovo
		nuovoAutomobile.setProprietario(nuovoProprietario);

		// salvo il nuovo abitante
		automobileService.inserisciNuovo(nuovoAutomobile);
		Date dataDaCercare =  new SimpleDateFormat("yyyy-MM-dd").parse("2021-10-13");
		// proviamo a vedere se è stato rimosso
		if (	proprietarioService.contaQuantiProprietariPossiedonoAutoImmatricolataDopoData(dataDaCercare) != 1)
			throw new RuntimeException("testCercaTuttiByProprietarioCheHannoUnCfCheIniziaPer fallito:  ");
		
		automobileService.rimuovi(nuovoAutomobile);
		proprietarioService.rimuovi(nuovoProprietario);
		
		System.out.println(".......testCercaTuttiByProprietarioCheHannoUnCfCheIniziaPer fine: PASSED.............");
		
	}
	
	
	
	
	
	
}
