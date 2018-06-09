/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.ifrn.coapac.dao;

import br.com.ifrn.coapac.model.Postagem;
import java.util.List;
import javax.persistence.Query;
import java.io.Serializable;
import javax.persistence.EntityManager;

/**
 *
 * @author Luan
 */
public class PostagemDAO extends GenericoDAO implements Serializable {
	private static final long serialVersionUID = 873984453190660021L;

	public PostagemDAO(EntityManager gerenciador) {
		this.em = gerenciador;
	}

	//Recuperando dados do banco
	public List<Postagem> gerarLista(Query query){
		@SuppressWarnings("unchecked")
		List<Postagem> lista = query.getResultList();
		return lista;
	}
	
	public Postagem getById(int id){
		Postagem ep = em.find(Postagem.class, id);
		return ep;
	}
}
