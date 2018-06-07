/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.ifrn.coapac.dao;

import br.com.ifrn.coapac.model.Postagem;
import br.com.ifrn.coapac.model.Usuario;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import javax.persistence.Query;
import java.io.Serializable;
import javax.persistence.EntityManager;

/**
 *
 * @author Luan
 */
public class PostagemDAO implements Serializable {
	private static final long serialVersionUID = -2733386216869241584L;
	private EntityManager em;

	public PostagemDAO(EntityManager gerenciador) {
		this.em = gerenciador;
	}

	public Postagem getById(int id) {
		Postagem p = null;
		try {
			p = em.find(Postagem.class, id);
		} catch (NullPointerException e) {
			e.printStackTrace();
		}
		return p;
	}

	public Postagem getByIdESession(Usuario usuario_session, int id) {
		Postagem buscaPostagem = new Postagem();
		try {
			buscaPostagem = em.find(Postagem.class, id);
			if (Objects.equals(usuario_session.getId(), buscaPostagem.getUsuario().getId())) {
				return buscaPostagem;
			}
		} catch (NullPointerException e) {
			e.printStackTrace();
		}
		return buscaPostagem;
	}

	public void persist(Usuario usuario_session, Postagem postagem) {
		// adicionando a pergunta o usuario da sessão
		postagem.setUsuario(usuario_session);
		postagem.setData_postagem(new Date());

		// cadastrando a pergunta
		em.persist(postagem);
	}

	public void merge(Postagem postagem) {
		em.merge(postagem);
	}

	public void remove(int id) {
		Postagem postagem = getById(id);
		em.remove(postagem);
	}

	public List<Postagem> todasPostagems() {
		Query query = em.createQuery("SELECT x FROM Postagem x ORDER BY x.data_postagem DESC");
		@SuppressWarnings("unchecked")
		List<Postagem> lista = query.getResultList();
		return lista;
	}

	public List<Postagem> minhasPostagems(Usuario usuario_session) {
		Query query = em
				.createQuery("SELECT x FROM Postagem x WHERE x.usuario.id = :id  ORDER BY x.data_postagem DESC");
		query.setParameter("id", usuario_session.getId());
		@SuppressWarnings("unchecked")
		List<Postagem> lista = query.getResultList();
		return lista;
	}

	public List<Postagem> limitePostagems(int limite) {
		Query query = em.createQuery("SELECT x FROM Postagem x ORDER BY x.data_postagem DESC");
		if (limite > 0)
			query.setMaxResults(limite);
		@SuppressWarnings("unchecked")
		List<Postagem> lista = query.getResultList();
		return lista;
	}
}
