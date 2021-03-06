package br.com.ifrn.coapac.service;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import br.com.ifrn.coapac.dao.UsuarioDAO;
import br.com.ifrn.coapac.model.TipoUsuario;
import br.com.ifrn.coapac.model.Usuario;
import br.com.ifrn.coapac.utils.Criptografia;
import br.com.ifrn.coapac.utils.PagingInformation;
import br.com.ifrn.coapac.utils.ValidatorUtil;

public class NegocioUsuario implements Serializable{
	private static final long serialVersionUID = -3837995741448817024L;
	
	private EntityManager em;

	public NegocioUsuario(EntityManager em) {
		super();
		this.em = em;
	}
	
	public Usuario getUsuario(Usuario usuario) {
		try {
			UsuarioDAO dao = new UsuarioDAO(em);
			Query query = em.createQuery("SELECT x FROM Usuario x WHERE x.matricula = :matricula OR x.nome = :nome");
			query.setParameter("matricula", usuario.getMatricula());
			query.setParameter("nome", usuario.getNome());
			Usuario u = dao.getByQuery(query);
			return u;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public Usuario persist(Usuario usuario, HashMap<String, String> dados) {
		UsuarioDAO dao = new UsuarioDAO(em);
		Query query = em.createQuery("SELECT x.quantidade FROM Limite x WHERE x.id = :id");
		if (!ValidatorUtil.isEmpty(dados.get("curso"))) {
			query.setParameter("id", 1);
			usuario.setVinculo(dados.get("curso").toString());
			usuario.setTipo(TipoUsuario.ALUNO);
		} else {
			query.setParameter("id", 2);
			usuario.setVinculo(dados.get("cargo").toString());
			usuario.setTipo(TipoUsuario.SERVIDOR);
		}
		int quantidade = (int) query.getSingleResult();
		usuario.setQuantidade_copia(quantidade);
		usuario.setExpiracao_copia(new Date());
		usuario.setAcesso(TipoUsuario.ALUNO);
		usuario.setNome(dados.get("nome").toString());
		usuario.setSenha(Criptografia.esconderMD5(usuario.getSenha()));
		
		dao.persist(usuario);
		return usuario;
	}

	public Usuario merge(Usuario usuario, HashMap<String, String> dados) {
		Usuario retorno = new Usuario();
		UsuarioDAO dao = new UsuarioDAO(em);
		DateFormat mesFormat = new SimpleDateFormat("MM");
		DateFormat anoFormat = new SimpleDateFormat("yyyy");
		Date hoje = new Date();
		Query query = em.createQuery("SELECT x.quantidade FROM Limite x WHERE x.id = :id");
		// --- verifica se é ALUNO ou SERVIDOR
		if (!ValidatorUtil.isEmpty(dados.get("curso"))) {
			// --- ALUNO ---
			query.setParameter("id", 1);
			usuario.setVinculo(dados.get("curso").toString());
		} else {
			// --- SERVIDOR ---
			query.setParameter("id", 2);
			usuario.setVinculo(dados.get("cargo").toString());
		}
		int quantidade = (int) query.getSingleResult();

		if (anoFormat.format(hoje).equals(anoFormat.format(usuario.getExpiracao_copia())) && Integer
				.parseInt(mesFormat.format(hoje)) > Integer.parseInt(mesFormat.format(usuario.getExpiracao_copia()))) {
			usuario.setQuantidade_copia(quantidade);
			usuario.setExpiracao_copia(hoje);
		} else if (Integer.parseInt(anoFormat.format(hoje)) > Integer
				.parseInt(anoFormat.format(usuario.getExpiracao_copia()))) {
			usuario.setQuantidade_copia(quantidade);
			usuario.setExpiracao_copia(hoje);
		}
		usuario.setNome(dados.get("nome").toString());
		usuario.setSenha(Criptografia.esconderMD5(usuario.getSenha()));

		dao.merge(usuario);
		retorno = usuario;
		return retorno;
	}

	public void merge(Usuario usuario) {
		UsuarioDAO dao = new UsuarioDAO(em);
		dao.merge(usuario);
	}

	public Usuario buscarPorLoginESenha(String matricula, String senha) {
		Usuario retorno;
		try {
			UsuarioDAO dao = new UsuarioDAO(em);
			Query query = em
					.createQuery("SELECT x FROM Usuario x WHERE x.matricula = :matricula and x.senha = :senha ");
			query.setParameter("matricula", matricula);
			query.setParameter("senha", senha);// --- COLOCAR CRIPTOGRAFIA !!!
			retorno = dao.getByQuery(query);
		} catch (NumberFormatException | javax.persistence.NoResultException e) {
			e.printStackTrace();
			return null;
		}
		return retorno;
	}

	public List<Usuario> lista(Usuario usuario, PagingInformation paginacao) {
		List<Usuario> lista;
		try {
			UsuarioDAO dao = new UsuarioDAO(em);
			String jpql = "SELECT x FROM Usuario x" + " WHERE x.matricula LIKE :matricula AND" + " x.nome LIKE :nome";
			if (usuario.getTipo() != TipoUsuario.TODOS) {
				jpql += " AND x.tipo = :tipo";
			}
			if (usuario.getAcesso() != TipoUsuario.TODOS) {
				jpql += " AND x.acesso = :acesso";
			}

			Query query = em.createQuery(jpql);
			query.setParameter("matricula", "%" + usuario.getMatricula() + "%");
			query.setParameter("nome", "%" + usuario.getNome() + "%");

			if (usuario.getTipo() != TipoUsuario.TODOS) {
				query.setParameter("tipo", usuario.getTipo());
			}
			if (usuario.getAcesso() != TipoUsuario.TODOS) {
				query.setParameter("acesso", usuario.getAcesso());
			}

			if (paginacao != null) {
				String jpqlPag = jpql;
				int posOrder = jpqlPag.length();
				int posSelect = (jpqlPag.contains("SELECT")) ? jpqlPag.indexOf("FROM") : 0;

				Query qPaginacao = em.createQuery("SELECT count(*) " + jpqlPag.substring(posSelect, posOrder));
				qPaginacao.setParameter("nome", "%" + usuario.getNome() + "%");
				qPaginacao.setParameter("matricula", "%" + usuario.getMatricula() + "%");

				if (usuario.getTipo() != TipoUsuario.TODOS) {
					qPaginacao.setParameter("tipo", usuario.getTipo());
				}
				if (usuario.getAcesso() != TipoUsuario.TODOS) {
					qPaginacao.setParameter("acesso", usuario.getAcesso());
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
