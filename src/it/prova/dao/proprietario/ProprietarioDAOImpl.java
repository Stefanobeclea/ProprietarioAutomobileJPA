package it.prova.dao.proprietario;


import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import it.prova.model.Automobile;
import it.prova.model.Proprietario;

public class ProprietarioDAOImpl implements ProprietarioDAO{
	private EntityManager entityManager;

	public void setEntityManager(EntityManager entityManager) {
		this.entityManager = entityManager;
	}

	@Override
	public List<Proprietario> list() throws Exception {
		// dopo la from bisogna specificare il nome dell'oggetto (lettera maiuscola) e
		// non la tabella
		return entityManager.createQuery("from Proprietario", Proprietario.class).getResultList();
	}

	@Override
	public Proprietario get(Long id) throws Exception {
		return entityManager.find(Proprietario.class, id);
	}

	@Override
	public void update(Proprietario municipioInstance) throws Exception {
		if (municipioInstance == null) {
			throw new Exception("Problema valore in input");
		}
		municipioInstance = entityManager.merge(municipioInstance);
	}

	@Override
	public void insert(Proprietario municipioInstance) throws Exception {
		if (municipioInstance == null) {
			throw new Exception("Problema valore in input");
		}
		entityManager.persist(municipioInstance);
	}

	@Override
	public void delete(Proprietario municipioInstance) throws Exception {
		if (municipioInstance == null) {
			throw new Exception("Problema valore in input");
		}
		entityManager.remove(entityManager.merge(municipioInstance));
	}

	@Override
	public int countQuantiProprietariPossiedonoAutoImmatricolataDopoData(java.util.Date input) throws Exception {
		if (input == null) {
			throw new Exception("Problema valore in input");
		}
		TypedQuery<Automobile> query = entityManager.createQuery("count(p.id) from Proprietario p where p.automobile.annoimmatricolazione > ?1", Automobile.class);
		return query.setParameter(1, input).getFirstResult();
	}
}
