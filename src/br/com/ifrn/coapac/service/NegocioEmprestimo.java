package br.com.ifrn.coapac.service;

import static br.com.ifrn.coapac.model.TipoSituacao.AGUARDANDO;
import static br.com.ifrn.coapac.model.TipoSolicitacao.PENDENTE;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.TemporalType;

import br.com.ifrn.coapac.dao.EmprestimoDAO;
import br.com.ifrn.coapac.model.Ativo;
import br.com.ifrn.coapac.model.Emprestimo;
import br.com.ifrn.coapac.model.Material;
import br.com.ifrn.coapac.model.TipoSituacao;
import br.com.ifrn.coapac.model.TipoSolicitacao;
import br.com.ifrn.coapac.model.Usuario;
import br.com.ifrn.coapac.utils.PagingInformation;
import br.com.ifrn.coapac.utils.ValidatorUtil;

public class NegocioEmprestimo implements Serializable{
	private static final long serialVersionUID = -3880620230570395763L;
	
	private EntityManager em;

	public NegocioEmprestimo(EntityManager em) {
		super();
		this.em = em;
	}
	
	public List<Emprestimo> minhaLista(Usuario usuario_session, Usuario usuario, int limite) {
		EmprestimoDAO dao = new EmprestimoDAO(em);
		Query query = em.createQuery("SELECT x FROM Emprestimo x WHERE x.usuario_sol.id = :id");
		if (limite != 0) {
			query.setMaxResults(limite);
		}

		if (ValidatorUtil.isEmpty(usuario)) {
			query.setParameter("id", usuario_session.getId());
		} else {
			query.setParameter("id", usuario.getId());
		}

		List<Emprestimo> lista = dao.gerarLista(query);
		return lista;
	}

	public void persist(Usuario usuario_session, Material material_ps) {
		EmprestimoDAO dao = new EmprestimoDAO(em);
		Emprestimo emp = new Emprestimo();
		emp.setMaterial(material_ps);
		emp.setUsuario_sol(usuario_session);
		emp.setSituacao(TipoSituacao.AGUARDANDO);
		emp.setSolicitacao(TipoSolicitacao.PENDENTE);
		dao.persist(emp);
	}

	public void merge(Emprestimo emprestimo_mg) {
		EmprestimoDAO dao = new EmprestimoDAO(em);
		dao.merge(emprestimo_mg);
		dao.merge(emprestimo_mg.getMaterial());
	}

	public void remove(int id) {
		EmprestimoDAO dao = new EmprestimoDAO(em);
		Emprestimo emprestimo = dao.getById(id);
		dao.remove(emprestimo);
		if (emprestimo.getSolicitacao() == TipoSolicitacao.DEFERIDO
				&& emprestimo.getSituacao() == TipoSituacao.EMPRESTADO) {
			Material m = emprestimo.getMaterial();
			m.setQuantidade(m.getQuantidade() + 1);
			dao.merge(m);
		}
	}

	public List<Emprestimo> buscarFiltro(Emprestimo emp, PagingInformation paginacao) {
		EmprestimoDAO dao = new EmprestimoDAO(em);
		List<Emprestimo> lista;
		try {
			String jpql = "SELECT x FROM Emprestimo x"
					+ " WHERE x.material.nome LIKE :nome AND x.material.codigo LIKE :codigo"
					+ " AND x.usuario_sol.matricula LIKE :matricula ";

			if (emp.getSituacao() != TipoSituacao.TODOS && ValidatorUtil.isNotEmpty(emp.getSituacao())) {
				jpql += " AND x.situacao = :situacao";
			}
			if (emp.getSolicitacao() != TipoSolicitacao.TODOS && ValidatorUtil.isNotEmpty(emp.getSolicitacao())) {
				jpql += " AND x.solicitacao = :solicitacao";
			}
			if (emp.getMaterial().getIsAtivo() != Ativo.AMBOS) {
				jpql += " AND x.material.isAtivo = :ativo";
			}

			jpql += " ORDER BY x.data_expiracao";

			Query query = em.createQuery(jpql);
			query.setParameter("nome", "%" + emp.getMaterial().getNome() + "%");
			query.setParameter("codigo", "%" + emp.getMaterial().getCodigo() + "%");
			query.setParameter("matricula", "%" + emp.getUsuario_sol().getMatricula() + "%");

			if (emp.getSituacao() != TipoSituacao.TODOS && ValidatorUtil.isNotEmpty(emp.getSituacao())) {
				query.setParameter("situacao", emp.getSituacao());
			}
			if (emp.getSolicitacao() != TipoSolicitacao.TODOS && ValidatorUtil.isNotEmpty(emp.getSolicitacao())) {
				query.setParameter("solicitacao", emp.getSolicitacao());
			}
			if (emp.getMaterial().getIsAtivo() != Ativo.AMBOS) {
				query.setParameter("ativo", emp.getMaterial().getIsAtivo());
			}

			if (paginacao != null) {
				String jpqlPag = jpql;
				int posOrder = jpqlPag.length();
				int posSelect = (jpqlPag.contains("SELECT")) ? jpqlPag.indexOf("FROM") : 0;

				String jpqlPaginacao = "SELECT count(*) " + jpqlPag.substring(posSelect, posOrder);

				Query qPaginacao = em.createQuery(jpqlPaginacao);
				qPaginacao.setParameter("nome", "%" + emp.getMaterial().getNome() + "%");
				qPaginacao.setParameter("codigo", "%" + emp.getMaterial().getCodigo() + "%");
				qPaginacao.setParameter("matricula", "%" + emp.getUsuario_sol().getMatricula() + "%");

				if (emp.getSituacao() != TipoSituacao.TODOS && ValidatorUtil.isNotEmpty(emp.getSituacao())) {
					qPaginacao.setParameter("situacao", emp.getSituacao());
				}
				if (emp.getSolicitacao() != TipoSolicitacao.TODOS && ValidatorUtil.isNotEmpty(emp.getSolicitacao())) {
					qPaginacao.setParameter("solicitacao", emp.getSolicitacao());
				}
				if (emp.getMaterial().getIsAtivo() != Ativo.AMBOS) {
					qPaginacao.setParameter("ativo", emp.getMaterial().getIsAtivo());
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

	public List<Emprestimo> buscarSolicitacoes(Usuario usuario_session, int max) {
		List<Emprestimo> lista = null;
		try {
			EmprestimoDAO dao = new EmprestimoDAO(em);
			Query query = em
					.createQuery("SELECT x FROM Emprestimo x " + "WHERE x.usuario_sol.matricula LIKE :matricula AND"
							+ " x.situacao = :situacao AND x.solicitacao = :solicitacao");
			query.setParameter("matricula", "%" + usuario_session.getMatricula() + "%");
			query.setParameter("situacao", AGUARDANDO);
			query.setParameter("solicitacao", PENDENTE);
			query.setMaxResults(5);
			if (max != 0) {
				query.setMaxResults(max);
			}
			lista = dao.gerarLista(query);
			return lista;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public List<Emprestimo> buscarInadimplente(Usuario usuario_session) {
		EmprestimoDAO dao = new EmprestimoDAO(em);
		Query query = em.createQuery(
				"SELECT x FROM Emprestimo x" + " WHERE x.usuario_sol.id = :id AND x.data_expiracao < :inicio"
						+ " AND x.situacao = :sit ORDER BY x.data_expiracao DESC");
		query.setParameter("id", usuario_session.getId());
		query.setParameter("sit", TipoSituacao.EMPRESTADO);
		query.setParameter("inicio", new Date(), TemporalType.DATE);
		query.setMaxResults(5);

		List<Emprestimo> lista = dao.gerarLista(query);
		return lista;
	}

	public List<Emprestimo> buscarInadimplente(Usuario usuario_session, Usuario usuario) {
		EmprestimoDAO dao = new EmprestimoDAO(em);
		Query query = em.createQuery(
				"SELECT x FROM Emprestimo x " + "WHERE x.usuario_sol.id = :id AND x.data_expiracao < :inicio AND"
						+ " x.situacao = :sit ORDER BY x.data_expiracao DESC");
		query.setParameter("id", usuario.getId());
		query.setParameter("sit", TipoSituacao.EMPRESTADO);
		query.setParameter("inicio", new Date(), TemporalType.DATE);
		query.setMaxResults(1);

		List<Emprestimo> lista = dao.gerarLista(query);
		return lista;
	}

	public List<Emprestimo> buscarFiltro(Emprestimo emp, PagingInformation paginacao, Date inicio, Date fim) {
		List<Emprestimo> lista;
		try {
			EmprestimoDAO dao = new EmprestimoDAO(em);
			String jpql = "SELECT x FROM Emprestimo x"
					+ " WHERE x.material.nome LIKE :nome AND x.material.codigo LIKE :codigo"
					+ " AND x.data_expiracao BETWEEN :inicio AND :fim";

			// Para o caso de solcitações do usuario_session
			// o = deve ser o usado quando se tem apenas um usuário para busca
			// session serve pra identifica o usuario_session
			if (emp.getUsuario_sol().isIsSession()) {
				jpql += " AND x.usuario_sol.matricula = :matricula";
			} else {
				jpql += " AND x.usuario_sol.matricula LIKE :matricula";
			}

			if (emp.getSituacao() != TipoSituacao.TODOS && ValidatorUtil.isNotEmpty(emp.getSituacao())) {
				jpql += " AND x.situacao = :situacao";
			}
			if (emp.getSolicitacao() != TipoSolicitacao.TODOS && ValidatorUtil.isNotEmpty(emp.getSolicitacao())) {
				jpql += " AND x.solicitacao = :solicitacao";
			}
			if (emp.getMaterial().getIsAtivo() != Ativo.AMBOS) {
				jpql += " AND x.material.isAtivo = :ativo";
			}

			jpql += " ORDER BY x.data_expiracao";

			Query query = em.createQuery(jpql);
			query.setParameter("nome", "%" + emp.getMaterial().getNome() + "%");
			query.setParameter("codigo", "%" + emp.getMaterial().getCodigo() + "%");

			// Para o caso de solcitações do usuario_session
			// o = deve ser o usado quando se tem apenas um usuário para busca
			if (emp.getUsuario_sol().isIsSession()) {
				query.setParameter("matricula", emp.getUsuario_sol().getMatricula());
			} else {
				query.setParameter("matricula", "%" + emp.getUsuario_sol().getMatricula() + "%");
			}
			query.setParameter("inicio", inicio);
			query.setParameter("fim", fim);

			if (emp.getSituacao() != TipoSituacao.TODOS && ValidatorUtil.isNotEmpty(emp.getSituacao())) {
				query.setParameter("situacao", emp.getSituacao());
			}
			if (emp.getSolicitacao() != TipoSolicitacao.TODOS && ValidatorUtil.isNotEmpty(emp.getSolicitacao())) {
				query.setParameter("solicitacao", emp.getSolicitacao());
			}
			if (emp.getMaterial().getIsAtivo() != Ativo.AMBOS) {
				query.setParameter("ativo", emp.getMaterial().getIsAtivo());
			}

			if (paginacao != null) {
				String jpqlPag = jpql;
				int posOrder = jpqlPag.length();
				int posSelect = (jpqlPag.contains("SELECT")) ? jpqlPag.indexOf("FROM") : 0;

				String jpqlPaginacao = "SELECT count(*) " + jpqlPag.substring(posSelect, posOrder);

				Query qPaginacao = em.createQuery(jpqlPaginacao);
				qPaginacao.setParameter("nome", "%" + emp.getMaterial().getNome() + "%");
				qPaginacao.setParameter("codigo", "%" + emp.getMaterial().getCodigo() + "%");
				// Para o caso de solcitações do usuario_session
				// o = deve ser o usado quando se tem apenas um usuário para
				// busca
				if (emp.getUsuario_sol().isIsSession()) {
					qPaginacao.setParameter("matricula", emp.getUsuario_sol().getMatricula());
				} else {
					qPaginacao.setParameter("matricula", "%" + emp.getUsuario_sol().getMatricula() + "%");
				}
				qPaginacao.setParameter("inicio", inicio);
				qPaginacao.setParameter("fim", fim);

				if (emp.getSituacao() != TipoSituacao.TODOS && ValidatorUtil.isNotEmpty(emp.getSituacao())) {
					qPaginacao.setParameter("situacao", emp.getSituacao());
				}
				if (emp.getSolicitacao() != TipoSolicitacao.TODOS && ValidatorUtil.isNotEmpty(emp.getSolicitacao())) {
					qPaginacao.setParameter("solicitacao", emp.getSolicitacao());
				}
				if (emp.getMaterial().getIsAtivo() != Ativo.AMBOS) {
					qPaginacao.setParameter("ativo", emp.getMaterial().getIsAtivo());
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
