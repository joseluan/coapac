package br.com.ifrn.coapac.service;

import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import br.com.ifrn.coapac.dao.RespostaDAO;
import br.com.ifrn.coapac.model.Resposta;
import br.com.ifrn.coapac.model.Usuario;

public class NegocioResposta {
	private EntityManager em;

	public NegocioResposta(EntityManager em) {
		super();
		this.em = em;
	}
	
	public void persist(Usuario usuario_session, Resposta resposta){
		RespostaDAO dao = new RespostaDAO(em);
		resposta.setData_resposta(new Date());
		resposta.setUsuario(usuario_session);
		dao.persist(resposta);
	}
	
	public void remove(int id){
		RespostaDAO dao = new RespostaDAO(em);
		Resposta r = dao.getById(id);
		dao.remove(r);
	}
	
	public void merge(Resposta resposta){
		RespostaDAO dao = new RespostaDAO(em);
		dao.merge(resposta);
	}
	
	public List<Resposta> limiteResposta(int id, int limite){
		RespostaDAO dao = new RespostaDAO(em);
		Query query = em.createQuery("SELECT x FROM Resposta x WHERE x.postagem.id = :id ");
		query.setParameter("id", id);
		query.setMaxResults(limite);
		List<Resposta> lista = dao.gerarLista(query);
		return lista;
	}
}
