/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.ifrn.coapac.dao;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import br.com.ifrn.coapac.model.Copia;
import br.com.ifrn.coapac.model.Limite;
import br.com.ifrn.coapac.model.Usuario;
import br.com.ifrn.coapac.utils.PagingInformation;
import br.com.ifrn.coapac.utils.ValidatorUtil;

/**
 *
 * @author Luan
 */
public class CopiaDAO implements Serializable {
	private static final long serialVersionUID = 3997788520854948772L;
	private EntityManager em;

	public CopiaDAO(EntityManager gerenciador) {
		this.em = gerenciador;
	}

	public Copia getById(int id) {
		Copia c = em.find(Copia.class, id);
		return c;
	}

	public List<Copia> minhaLista(Usuario usuario_session, Usuario usuario, int max) {
		Query query = em.createQuery("SELECT x FROM Copia x WHERE x.usuario.id = :id ORDER BY x.data_copia DESC");
		query.setMaxResults(20);
		if (max != 0)
			query.setMaxResults(max);

		if (ValidatorUtil.isEmpty(usuario)) {
			query.setParameter("id", usuario_session.getId());
		} else {
			query.setParameter("id", usuario.getId());
		}

		@SuppressWarnings("unchecked")
		List<Copia> lista = query.getResultList();
		return lista;
	}

	public void persist(Usuario usuario_session, Copia copia) {
		copia.setData_copia(new Date());
		Usuario usuario = copia.getUsuario();
		usuario.setQuantidade_copia(usuario.getQuantidade_copia() - copia.getQuantidade());

		em.merge(usuario);
		em.persist(copia);
	}

	public void mergeLimite(Limite limite) {
		em.merge(limite);
	}

	public List<Limite> listaLimite() {
		try {
			Query query = em.createQuery("SELECT x FROM Limite x WHERE x.id = 1 OR x.id = 2");
			@SuppressWarnings("unchecked")
			List<Limite> lista = query.getResultList();
			return lista;
		} catch (NumberFormatException | javax.persistence.NoResultException e) {
			e.printStackTrace();
			return null;
		}
	}

	public Limite getLimite(int id) {
		try {
			Query query = em.createQuery("SELECT x FROM Limite x WHERE x.id = :id");
			query.setParameter("id", id);
			Limite l = (Limite) query.getSingleResult();
			return l;
		} catch (NumberFormatException | javax.persistence.NoResultException e) {
			e.printStackTrace();
			return null;
		}
	}

	public void remove(Copia copia_rm) {
		Copia copia = em.find(Copia.class, copia_rm.getId());
		Usuario usuario = copia.getUsuario();
		usuario.setQuantidade_copia(usuario.getQuantidade_copia() + copia.getQuantidade());

		em.merge(usuario);
		em.remove(copia);
	}

	@SuppressWarnings("unchecked")
	public List<Copia> buscarFiltro(Usuario usuario, PagingInformation paginacao) {
		try {
			List<Copia> lista;
			String jpql = "SELECT x FROM Copia x" + " WHERE x.usuario.matricula LIKE :matricula AND"
					+ " x.usuario.nome LIKE :nome ORDER BY x.data_copia DESC";
			Query query = em.createQuery(jpql);
			query.setParameter("matricula", "%" + usuario.getMatricula() + "%");
			query.setParameter("nome", "%" + usuario.getNome() + "%");

			if (paginacao != null) {
				String jpqlPag = jpql;
				int posOrder = jpqlPag.length();
				int posSelect = (jpqlPag.contains("SELECT")) ? jpqlPag.indexOf("FROM") : 0;

				String jpqlPaginacao = "SELECT count(*) " + jpqlPag.substring(posSelect, posOrder);

				Query qPaginacao = em.createQuery(jpqlPaginacao);
				qPaginacao.setParameter("matricula", "%" + usuario.getMatricula() + "%");
				qPaginacao.setParameter("nome", "%" + usuario.getNome() + "%");

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

	@SuppressWarnings("unchecked")
	public List<Copia> buscarFiltro(Usuario usuario, PagingInformation paginacao, Date inicio, Date fim) {
		try {
			List<Copia> lista;
			String jpql = "SELECT x FROM Copia x" + " WHERE x.usuario.matricula LIKE :matricula AND"
					+ " x.usuario.nome LIKE :nome AND"
					+ " x.data_copia BETWEEN :inicio AND :fim ORDER BY x.data_copia DESC";
			Query query = em.createQuery(jpql);
			query.setParameter("matricula", "%" + usuario.getMatricula() + "%");
			query.setParameter("nome", "%" + usuario.getNome() + "%");
			query.setParameter("inicio", inicio);
			query.setParameter("fim", fim);

			if (paginacao != null) {
				String jpqlPag = jpql;
				int posOrder = jpqlPag.length();
				int posSelect = (jpqlPag.contains("SELECT")) ? jpqlPag.indexOf("FROM") : 0;

				String jpqlPaginacao = "SELECT count(*) " + jpqlPag.substring(posSelect, posOrder);

				Query qPaginacao = em.createQuery(jpqlPaginacao);
				qPaginacao.setParameter("matricula", "%" + usuario.getMatricula() + "%");
				qPaginacao.setParameter("nome", "%" + usuario.getNome() + "%");
				qPaginacao.setParameter("inicio", inicio);
				qPaginacao.setParameter("fim", fim);

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
