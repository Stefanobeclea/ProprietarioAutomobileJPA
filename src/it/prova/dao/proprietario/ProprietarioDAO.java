package it.prova.dao.proprietario;


import it.prova.dao.IBaseDAO;
import it.prova.model.Proprietario;

public interface ProprietarioDAO extends IBaseDAO<Proprietario> {
	public int countQuantiProprietariPossiedonoAutoImmatricolataDopoData (java.util.Date input) throws Exception;
}
