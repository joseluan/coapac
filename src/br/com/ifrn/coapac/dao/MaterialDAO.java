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
import br.com.ifrn.coapac.model.Material;

/**
 *
 * @author Luan
 */
public class MaterialDAO extends GenericoDAO implements Serializable {
	private static final long serialVersionUID = -3402292280314411335L;

	public MaterialDAO(EntityManager gerenciador) {
		this.em = gerenciador;
	}

	// Recuperando dados do banco
	public List<Material> gerarLista(Query query) {
		@SuppressWarnings("unchecked")
		List<Material> lista = query.getResultList();
		return lista;
	}
	
	public int getInt(Query query) {
		int quantidade = query.getResultList().size();
		return quantidade;
	}

	public Material getById(int id) {
		Material ep = em.find(Material.class, id);
		return ep;
	}
}
