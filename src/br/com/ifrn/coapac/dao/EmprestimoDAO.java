/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.ifrn.coapac.dao;

import java.io.Serializable;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import br.com.ifrn.coapac.model.Emprestimo;

/**
 *
 * @author Luan
 */
public class EmprestimoDAO extends GenericoDAO	 implements Serializable {
	private static final long serialVersionUID = 2704600430504870994L;

	public EmprestimoDAO(EntityManager em) {
		this.em = em;
	}

	// Recuperando dados do banco
	public List<Emprestimo> gerarLista(Query query) {
		@SuppressWarnings("unchecked")
		List<Emprestimo> lista = query.getResultList();
		return lista;
	}

	public Emprestimo getById(int id) {
		Emprestimo e = em.find(Emprestimo.class, id);
		return e;
	}

}
