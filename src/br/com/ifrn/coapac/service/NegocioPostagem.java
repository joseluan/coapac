package br.com.ifrn.coapac.service;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import br.com.ifrn.coapac.dao.PostagemDAO;
import br.com.ifrn.coapac.model.Postagem;
import br.com.ifrn.coapac.model.Usuario;

//Regras de negocio da entidade Postagem
public class NegocioPostagem implements Serializable{
	private static final long serialVersionUID = -1658409352635523010L;
	
	private EntityManager em;

	public NegocioPostagem(EntityManager gerenciador) {
		super();
		this.em = gerenciador;
	}
	
	public void persist(Usuario usuario_session, Postagem postagem) {
		PostagemDAO dao = new PostagemDAO(em);
		// adicionando a pergunta o usuario da sessão
		postagem.setUsuario(usuario_session);
		postagem.setData_postagem(new Date());

		// cadastrando a pergunta
		dao.persist(postagem);
	}

	public void merge(Postagem postagem) {
		PostagemDAO dao = new PostagemDAO(em);
		dao.merge(postagem);
	}

	public void remove(int id) {
		PostagemDAO dao = new PostagemDAO(em);
		Postagem postagem = dao.getById(id);
		em.remove(postagem);
	}
	
	public Postagem getByIdESession(Usuario usuario_session, int id) {
		PostagemDAO dao = new PostagemDAO(em);
		Postagem buscaPostagem = new Postagem();
		try {
			buscaPostagem = dao.getById(id);
			if (Objects.equals(usuario_session.getId(), buscaPostagem.getUsuario().getId())) {
				return buscaPostagem;
			}
		} catch (NullPointerException e) {
			e.printStackTrace();
		}
		return buscaPostagem;
	}
	
	public List<Postagem> limitePostagems(int limite) {
		PostagemDAO dao = new PostagemDAO(em);
		Query query = em.createQuery("SELECT x FROM Postagem x ORDER BY x.data_postagem DESC");
		if (limite > 0)
			query.setMaxResults(limite);
		List<Postagem> lista = dao.gerarLista(query);
		return lista;
	}
	
	public List<Postagem> todasPostagems() {
		PostagemDAO dao = new PostagemDAO(em);
		Query query = em.createQuery("SELECT x FROM Postagem x ORDER BY x.data_postagem DESC");
		List<Postagem> lista = dao.gerarLista(query);
		return lista;
	}
	
	public List<Postagem> minhasPostagems(Usuario usuario_session) {
		PostagemDAO dao = new PostagemDAO(em);
		Query query = em
				.createQuery("SELECT x FROM Postagem x WHERE x.usuario.id = :id  ORDER BY x.data_postagem DESC");
		query.setParameter("id", usuario_session.getId());
		List<Postagem> lista = dao.gerarLista(query);
		return lista;
	}
}
