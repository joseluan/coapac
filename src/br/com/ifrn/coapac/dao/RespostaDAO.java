/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.ifrn.coapac.dao;

import br.com.ifrn.coapac.model.Resposta;
import java.util.List;
import javax.persistence.Query;
import java.io.Serializable;
import javax.persistence.EntityManager;

/**
 *
 * @author Luan
 */
public class RespostaDAO extends GenericoDAO implements Serializable {
	private static final long serialVersionUID = 161449400044493483L;

	public RespostaDAO(EntityManager gerenciador) {
		this.em = gerenciador;
	}

	//Recuperando dados do banco
	public List<Resposta> gerarLista(Query query){
		@SuppressWarnings("unchecked")
		List<Resposta> lista = query.getResultList();
		return lista;
	}
	
	public Resposta getById(int id){
		Resposta r = em.find(Resposta.class, id);
		return r;
	}
}
