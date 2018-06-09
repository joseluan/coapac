package br.com.ifrn.coapac.dao;

import javax.persistence.EntityManager;
import br.com.ifrn.coapac.model.EntidadePersistivel;

public abstract class GenericoDAO {
	protected EntityManager em;
	
	public void persist(EntidadePersistivel ep){
		em.persist(ep);
	}
	
	public void merge(EntidadePersistivel ep){
		em.merge(ep);
	}
	
	public void remove(EntidadePersistivel ep){
		em.remove(ep);
	}
	
}
