package it.prova.dao.automobile;

import java.util.List;

import it.prova.dao.IBaseDAO;
import it.prova.model.Automobile;

public interface AutomobileDAO extends IBaseDAO<Automobile>{
	public List<Automobile> findAllByProprietarioCheHannoUnCfCheIniziaPer (String input) throws Exception;
}
