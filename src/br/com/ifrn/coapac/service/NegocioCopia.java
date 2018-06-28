package br.com.ifrn.coapac.service;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

import br.com.ifrn.coapac.dao.CopiaDAO;
import br.com.ifrn.coapac.model.Copia;
import br.com.ifrn.coapac.model.Limite;
import br.com.ifrn.coapac.model.Usuario;
import br.com.ifrn.coapac.utils.PagingInformation;
import br.com.ifrn.coapac.utils.ValidatorUtil;

public class NegocioCopia implements Serializable{
	private static final long serialVersionUID = 2183984403647867826L;
	
	private EntityManager em;

	public NegocioCopia(EntityManager em) {
		super();
		this.em = em;
	}
	
	public List<Copia> minhaLista(Usuario usuario_session, Usuario usuario, int max) {
		CopiaDAO dao = new CopiaDAO(em);
		Query query = em.createQuery("SELECT x FROM Copia x WHERE x.usuario.id = :id ORDER BY x.data_copia DESC");
		query.setMaxResults(20);
		if (max != 0)
			query.setMaxResults(max);

		if (ValidatorUtil.isEmpty(usuario)) {
			query.setParameter("id", usuario_session.getId());
		} else {
			query.setParameter("id", usuario.getId());
		}

		List<Copia> lista = dao.gerarLista(query);
		return lista;
	}

	public void persist(Usuario usuario_session, Copia copia) {
		CopiaDAO dao = new CopiaDAO(em);
		copia.setData_copia(new Date());
		Usuario usuario = copia.getUsuario();

		dao.merge(usuario);
		dao.persist(copia);
	}

	public void mergeLimite(Limite limite) {
		CopiaDAO dao = new CopiaDAO(em);
		dao.merge(limite);
	}

	public List<Limite> listaLimite() {
		try {
			CopiaDAO dao = new CopiaDAO(em);
			Query query = em.createQuery("SELECT x FROM Limite x WHERE x.id = 1 OR x.id = 2");
			List<Limite> lista = dao.gerarListaLimite(query);
			return lista;
		} catch (NumberFormatException | javax.persistence.NoResultException e) {
			e.printStackTrace();
			return null;
		}
	}

	public Limite getLimite(int id) {
		try {
			CopiaDAO dao = new CopiaDAO(em);
			Limite l = dao.getByIdLimite(id);
			return l;
		} catch (NumberFormatException | javax.persistence.NoResultException e) {
			e.printStackTrace();
			return null;
		}
	}

	public void remove(Copia copia_rm) {
		CopiaDAO dao = new CopiaDAO(em);
		Copia copia = dao.getById(copia_rm.getId());
		Usuario usuario = copia.getUsuario();
		usuario.setQuantidade_copia(usuario.getQuantidade_copia() + copia.getQuantidade());

		dao.merge(usuario);
		dao.remove(copia);
	}

	@SuppressWarnings("unchecked")
	public List<Copia> buscarFiltro(Usuario usuario, PagingInformation paginacao) {
		try {
			CopiaDAO dao = new CopiaDAO(em);
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

			lista = dao.gerarLista(query);
			return lista;
		} catch (NumberFormatException | javax.persistence.NoResultException e) {
			e.printStackTrace();
			return null;
		}
	}

	public List<Copia> buscarFiltro(Usuario usuario, PagingInformation paginacao, Date inicio, Date fim) {
		try {
			CopiaDAO dao = new CopiaDAO(em);
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
	
	public List<Object[]> buscarListaGrafico() {
		try {
			String jpql = "SELECT MONTH(c.data_copia) AS mes, SUM(c.quantidade) AS soma, YEAR(c.data_copia) AS ano FROM Copia c GROUP BY MONTH(c.data_copia), YEAR(c.data_copia) ORDER BY YEAR(c.data_copia) DESC, MONTH(c.data_copia) DESC";
			Query query = em.createQuery(jpql);
			query.setMaxResults(6);
			int s = 0;
			//--- Retornando --
			@SuppressWarnings("unchecked")
			List<Object[]> lista = query.getResultList();
			return lista;
		} catch (NumberFormatException | javax.persistence.NoResultException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	/*@SuppressWarnings("deprecation")
	public Double porcentagemCopiaMesGeral(){
		Date dia1 = new Date();
		dia1.setDate(1);
		Date dia31 = new Date();
		dia31.setDate(0);
		int soma = buscarFiltro(dia1, dia31);
		Integer limiteGeral = getLimite(3).getQuantidade();
		
		return (double) (soma/limiteGeral);
	}*/
}
