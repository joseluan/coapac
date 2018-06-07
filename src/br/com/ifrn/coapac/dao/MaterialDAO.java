/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.ifrn.coapac.dao;

import br.com.ifrn.coapac.model.Ativo;
import java.io.Serializable;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import br.com.ifrn.coapac.model.Material;
import br.com.ifrn.coapac.model.TipoMaterial;
import br.com.ifrn.coapac.model.TipoUsuario;
import br.com.ifrn.coapac.model.Usuario;
import br.com.ifrn.coapac.utils.PagingInformation;

/**
 *
 * @author Luan
 */
public class MaterialDAO implements Serializable {
	private static final long serialVersionUID = 9210290188303644812L;
	private EntityManager em;

	public MaterialDAO(EntityManager gerenciador) {
		this.em = gerenciador;
	}

	public Material getById(int id) {
		Material m = em.find(Material.class, id);
		return m;
	}

	public int qtd() {
		Query query = em.createQuery("SELECT x.id FROM Material x WHERE x.isAtivo = :ativo");
		query.setParameter("ativo", Ativo.VERDADEIRO);
		int quantidade = query.getResultList().size();
		return quantidade;
	}

	public void persist(Material material) {
		em.persist(material);
	}

	public void merge(Material material) {
		em.merge(material);
	}

	public void remove(int id) {
		Material material = em.find(Material.class, id);
		material.setIsAtivo(Ativo.FALSO);
		em.merge(material);
	}

	@SuppressWarnings("unchecked")
	public List<Material> buscarFiltro(Usuario usuario_session, Material material, PagingInformation paginacao) {
		List<Material> lista;
		try {
			String jpql = "SELECT x FROM Material x WHERE x.nome LIKE :nome AND x.codigo LIKE :cod";
			if (usuario_session.getTipo() == TipoUsuario.ALUNO) {
				jpql += " AND x.tipo != :tipo";
			}
			if (material.getTipo() != TipoMaterial.TODOS) {
				jpql += " AND x.tipo = :tipo_filter";
			}
			if (material.getIsAtivo() != Ativo.AMBOS) {
				jpql += " AND x.isAtivo = :ativo";
			}
			Query query = em.createQuery(jpql);

			// adicionado os parâmetros da query
			query.setParameter("nome", "%" + material.getNome() + "%");
			query.setParameter("cod", "%" + material.getCodigo() + "%");
			if (usuario_session.getTipo() == TipoUsuario.ALUNO) {
				query.setParameter("tipo", TipoMaterial.ARMARIO);
			}
			if (material.getTipo() != TipoMaterial.TODOS) {
				query.setParameter("tipo_filter", material.getTipo());
			}
			if (material.getIsAtivo() != Ativo.AMBOS) {
				query.setParameter("ativo", material.getIsAtivo());
			}

			// Paginação
			if (paginacao != null) {
				String jpqlPag = jpql;
				int posOrder = jpqlPag.length();
				int posSelect = (jpqlPag.contains("SELECT")) ? jpqlPag.indexOf("FROM") : 0;

				Query qPaginacao = em.createQuery("SELECT count(*) " + jpqlPag.substring(posSelect, posOrder));

				// adicionado os parâmetros da query
				qPaginacao.setParameter("nome", "%" + material.getNome() + "%");
				qPaginacao.setParameter("cod", "%" + material.getCodigo() + "%");
				if (usuario_session.getTipo() == TipoUsuario.ALUNO) {
					qPaginacao.setParameter("tipo", TipoMaterial.ARMARIO);
				}
				if (material.getTipo() != TipoMaterial.TODOS) {
					qPaginacao.setParameter("tipo_filter", material.getTipo());
				}
				if (material.getIsAtivo() != Ativo.AMBOS) {
					qPaginacao.setParameter("ativo", material.getIsAtivo());
				}

				paginacao.setTotalRegistros((int) ((Long) qPaginacao.getSingleResult()).longValue());
				query.setFirstResult(paginacao.getPaginaAtual() * paginacao.getTamanhoPagina());
				query.setMaxResults(paginacao.getTamanhoPagina());
				lista = query.getResultList();
				return lista;
			}

			lista = query.getResultList();
			return lista;
		} catch (NumberFormatException | javax.persistence.NoResultException e) {
			e.printStackTrace();
			return null;
		}
	}

}
