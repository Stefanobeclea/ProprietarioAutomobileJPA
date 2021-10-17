package it.prova.service.proprietario;

import java.util.List;

import it.prova.dao.proprietario.ProprietarioDAO;
import it.prova.model.Proprietario;

public interface ProprietarioService {
	public List<Proprietario> listAll() throws Exception;

	public Proprietario caricaSingolo(Long id) throws Exception;

	public void aggiorna(Proprietario proprietarioInstance) throws Exception;

	public void inserisciNuovo(Proprietario proprietarioInstance) throws Exception;

	public void rimuovi(Proprietario proprietarioInstance) throws Exception;
	
	public int contaQuantiProprietariPossiedonoAutoImmatricolataDopoData(java.util.Date input) throws Exception;
	
	//per injection
	public void setProprietarioDAO(ProprietarioDAO proprietarioDAO);
}
