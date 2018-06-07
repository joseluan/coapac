/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.ifrn.coapac.mbean;

import br.com.ifrn.coapac.dao.CopiaDAO;
import br.com.ifrn.coapac.dao.EmprestimoDAO;
import br.com.ifrn.coapac.dao.UsuarioDAO;
import java.io.Serializable;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.persistence.EntityManager;
import javax.servlet.http.HttpServletRequest;

import br.com.ifrn.coapac.model.Copia;
import br.com.ifrn.coapac.model.Emprestimo;
import br.com.ifrn.coapac.model.Usuario;
import br.com.ifrn.coapac.utils.AbstractController;
import br.com.ifrn.coapac.utils.Criptografia;
import br.com.ifrn.coapac.utils.PagingInformation;

/**
 *
 * @author Luan
 */
@ManagedBean
@ViewScoped
public class UsuarioBean extends AbstractController implements Serializable {
	private static final long serialVersionUID = 1440760232114934887L;
	private Usuario usuario = new Usuario();
    private Usuario usuario_busca;
    private int copia;
    private List<Usuario> usuarios;

    /**
     * Quantidade de c�digos a serem exibidos por p�gina.
     */
    private static final int QTD_CODIGOS = 10;
    /**
     * Armazena as op��es de pagina��o de consulta a c�digos.
     */
    public PagingInformation paginacao = new PagingInformation(0, QTD_CODIGOS);

    public String atualizar() {
    	EntityManager gerenciador = this.getEntityManager();
        UsuarioDAO uDAO = new UsuarioDAO(gerenciador);

        usuario.setSenha(Criptografia.esconderMD5(usuario.getSenha()));
        uDAO.merge(usuario);
        return null;
    }

    public String atualizar(Usuario user) {
    	EntityManager gerenciador = this.getEntityManager();
        UsuarioDAO uDAO = new UsuarioDAO(gerenciador);
        uDAO.merge(user);
        if (usuario_session.getId() == user.getId()) {
            //--- Mudando a Session
            getCurrentSession().removeAttribute("usuario");
            getCurrentSession().setAttribute("usuario", user);
        }
        return null;
    }

    public List<Emprestimo> getEmprestimosPara() {
    	EntityManager gerenciador = this.getEntityManager();
        EmprestimoDAO eDAO = new EmprestimoDAO(gerenciador);
		List<Emprestimo> lista = eDAO.minhaLista(usuario_session, usuario_busca, 0);
        return lista;
    }

    public List<Copia> getCopiasPara() {
    	EntityManager gerenciador = this.getEntityManager();
        CopiaDAO cDAO = new CopiaDAO(gerenciador);
		List<Copia> lista = cDAO.minhaLista(usuario_session, usuario_busca, 0);
        return lista;
    }

    /**
     * M�todo chamado para redirecionar para a pr�xima p�gina da pagina��o,
     * referente � listagem de publica��es.
     *
     * @return
     */
    public String next() {
        paginacao.nextPage(null);
        listaFiltro();
        return null;
    }

    /**
     * M�todo chamado para redirecionar para a p�gina anterior da pagina��o,
     * referente � listagem de publica��es.
     *
     * @return
     */
    public String previous() {
        paginacao.previousPage(null);
        listaFiltro();
        return null;
    }

    /**
     * Mude a página atual da paginação de acordo com o parâmetro informado,
     * referente à listagem de publicações.
     *
     * @return String
     */
    public String changePage() {
        int pagina = getParameterInt("pagina");
        paginacao.setPaginaAtual(pagina);
        listaFiltro();
        return null;
    }

    public String listaFiltro() {
    	EntityManager gerenciador = this.getEntityManager();
        UsuarioDAO uDAO = new UsuarioDAO(gerenciador);
        usuarios = uDAO.lista(usuario, paginacao);
        return null;
    }

    public String buscarFiltro() {
    	EntityManager gerenciador = this.getEntityManager();
        UsuarioDAO uDAO = new UsuarioDAO(gerenciador);
        usuario_busca = uDAO.getUsuario(usuario);
        return null;
    }
    
    private EntityManager getEntityManager(){
        FacesContext fc = FacesContext.getCurrentInstance();
        ExternalContext ec = fc.getExternalContext();
        HttpServletRequest request = (HttpServletRequest) ec.getRequest();
        EntityManager manager = (EntityManager)request.getAttribute("EntityManager");
        
        return manager;
    }

    //GET E SET
    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public List<Usuario> getUsuarios() {
        return usuarios;
    }

    public void setUsuarios(List<Usuario> usuarios) {
        this.usuarios = usuarios;
    }

    public PagingInformation getPaginacao() {
        return paginacao;
    }

    public void setPaginacao(PagingInformation paginacao) {
        this.paginacao = paginacao;
    }

    public Usuario getUsuario_session() {
        return usuario_session;
    }

    public void setUsuario_session(Usuario usuario_session) {
        this.usuario_session = usuario_session;
    }

    public Usuario getUsuario_busca() {
        return usuario_busca;
    }

    public void setUsuario_busca(Usuario usuario_busca) {
        this.usuario_busca = usuario_busca;
    }

    public int getCopia() {
        return copia;
    }

    public void setCopia(int copia) {
        this.copia = copia;
    }

}
