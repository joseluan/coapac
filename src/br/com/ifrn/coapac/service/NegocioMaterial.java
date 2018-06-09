package br.com.ifrn.coapac.service;

import java.io.Serializable;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import br.com.ifrn.coapac.dao.MaterialDAO;
import br.com.ifrn.coapac.model.Ativo;
import br.com.ifrn.coapac.model.Material;
import br.com.ifrn.coapac.model.TipoMaterial;
import br.com.ifrn.coapac.model.TipoUsuario;
import br.com.ifrn.coapac.model.Usuario;
import br.com.ifrn.coapac.utils.PagingInformation;

public class NegocioMaterial implements Serializable{
	private static final long serialVersionUID = -3295042121794224406L;
	
	private EntityManager em;

	public NegocioMaterial(EntityManager em) {
		super();
		this.em = em;
	}

	public void persist(Material material) {
		MaterialDAO dao = new MaterialDAO(em);
		dao.persist(material);
	}

	public void merge(Material material) {
		MaterialDAO dao = new MaterialDAO(em);
		dao.merge(material);
	}

	public void remove(int id) {
		MaterialDAO dao = new MaterialDAO(em);
		Material material = dao.getById(id);
		material.setIsAtivo(Ativo.FALSO);
		dao.merge(material);
	}

	public int qtd() {
		MaterialDAO dao = new MaterialDAO(em);
		Query query = em.createQuery("SELECT x.id FROM Material x WHERE x.isAtivo = :ativo");
		query.setParameter("ativo", Ativo.VERDADEIRO);
		int quantidade = dao.getInt(query);
		return quantidade;
	}

	public List<Material> buscarFiltro(Usuario usuario_session, Material material, PagingInformation paginacao) {
		MaterialDAO dao = new MaterialDAO(em);
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
				lista = dao.gerarLista(query);
				return lista;
			}

			lista = dao.gerarLista(query);
			return lista;
		} catch (NumberFormatException | javax.persistence.NoResultException e) {
			e.printStackTrace();
			return null;
		}
	}
}
