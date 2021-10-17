package it.prova.service;

import it.prova.service.automobile.AutomobileService;
import it.prova.service.automobile.AutomobileServiceImpl;
import it.prova.service.proprietario.ProprietarioService;
import it.prova.service.proprietario.ProprietarioServiceImpl;

public class MyServiceFactory {
	private static AutomobileService automobileServiceInstance = null;
	private static ProprietarioService proprietarioServiceInstance = null;

	public static AutomobileService getAbitanteServiceInstance() {
		if (automobileServiceInstance == null) {
			automobileServiceInstance = new AutomobileServiceImpl();
			automobileServiceInstance.setAutomobileDAO(it.prova.dao.MyDaoFactory.getAutomobileDAOInstance());
		}
		return automobileServiceInstance;
	}

	public static ProprietarioService getMunicipioServiceInstance() {
		if (proprietarioServiceInstance == null) {
			proprietarioServiceInstance = new ProprietarioServiceImpl();
			proprietarioServiceInstance.setProprietarioDAO(it.prova.dao.MyDaoFactory.getProprietarioDAOInstance());
		}
		return proprietarioServiceInstance;
	}
}
