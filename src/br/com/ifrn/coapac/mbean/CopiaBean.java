/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.ifrn.coapac.mbean;

import br.com.ifrn.coapac.dao.CopiaDAO;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.persistence.EntityManager;
import javax.servlet.http.HttpServletRequest;

import br.com.ifrn.coapac.model.Copia;
import br.com.ifrn.coapac.model.Limite;
import br.com.ifrn.coapac.model.Usuario;
import br.com.ifrn.coapac.utils.AbstractController;
import br.com.ifrn.coapac.utils.PagingInformation;
import br.com.ifrn.coapac.utils.ValidatorUtil;
import java.util.Collections;

/**
 *
 * @author Luan
 */
@ManagedBean
@ViewScoped
public class CopiaBean extends AbstractController implements Serializable {
	private static final long serialVersionUID = 6254933665221415978L;
	
	private Usuario usuario = new Usuario();
    private Copia copia = new Copia();
    private Limite limiteALUNO = new Limite();
    private Limite limiteSERVIDOR = new Limite();
    private List<Copia> copias;
    private Date inicio;
    private Date fim;
    /**
     * Quantidade de c�digos a serem exibidos por p�gina.
     */
    private static final int QTD_CODIGOS = 10;
    /**
     * Armazena as op��es de pagina��o de consulta a c�digos.
     */
    public PagingInformation paginacao;

    @PostConstruct
    public void init() {
    	EntityManager gerenciador = this.getEntityManager();
    	CopiaDAO dao = new CopiaDAO(gerenciador);
        limiteALUNO = dao.getLimite(1);
        limiteSERVIDOR = dao.getLimite(2);
        paginacao = new PagingInformation(0, QTD_CODIGOS);
    }

    public List<Copia> getMinhasCopias() {
    	EntityManager gerenciador = this.getEntityManager();
    	CopiaDAO dao = new CopiaDAO(gerenciador);
        List<Copia> lista = dao.minhaLista(usuario_session, null, 0);
        return lista;
    }

    public List<Copia> getMinhasCopiasLimite() {
    	EntityManager gerenciador = this.getEntityManager();
    	CopiaDAO dao = new CopiaDAO(gerenciador);
        List<Copia> lista = dao.minhaLista(usuario_session, null, 5);
        Collections.reverse(lista);
        return lista;
    }

    public List<Limite> getLimites() {
    	EntityManager gerenciador = this.getEntityManager();
    	CopiaDAO dao = new CopiaDAO(gerenciador);
        List<Limite> lista = dao.listaLimite();
        return lista;
    }

    public String listaFiltro() {
    	EntityManager gerenciador = this.getEntityManager();
    	CopiaDAO dao = new CopiaDAO(gerenciador);
        if (ValidatorUtil.isNotEmpty(copias)) {
            if (copias.size() == 1) {
                paginacao.setPaginaAtual(0);
            }
        }
        if (ValidatorUtil.isNotEmpty(inicio) && ValidatorUtil.isNotEmpty(fim)) {
            copias = dao.buscarFiltro(usuario, paginacao, inicio, fim);
        } else {
            copias = dao.buscarFiltro(usuario, paginacao);
        }
        return null;
    }

    public String atualizar(Limite limite) {
    	EntityManager gerenciador = this.getEntityManager();
    	CopiaDAO dao = new CopiaDAO(gerenciador);
        dao.mergeLimite(limite);
        return null;
    }

    public String remover(Copia copia_rm) {
    	EntityManager gerenciador = this.getEntityManager();
    	CopiaDAO dao = new CopiaDAO(gerenciador);
        dao.remove(copia_rm);
        //--- Mudando a Session
        usuario_session.setQuantidade_copia(usuario_session.getQuantidade_copia()
                + copia_rm.getQuantidade());
        getCurrentSession().removeAttribute("usuario");
        getCurrentSession().setAttribute("usuario", usuario_session);

        listaFiltro();
        return null;
    }

    public String adicionar() {
    	EntityManager gerenciador = this.getEntityManager();
    	CopiaDAO dao = new CopiaDAO(gerenciador);
        if (copia.getQuantidade() <= usuario_session.getQuantidade_copia() &&
            copia.getQuantidade() != 0) {

            copia.setUsuario(usuario);
            if (ValidatorUtil.isEmpty(usuario.getId())) {
                copia.setUsuario(usuario_session);
                usuario = usuario_session;
                usuario.setQuantidade_copia(usuario.getQuantidade_copia()-copia.getQuantidade());
            }
            //Persistindo
            dao.persist(usuario_session, copia);
            addMsgInfo(copia.getQuantidade()+" cópias adicionadas");
            //--- Mudando a Session
            if (usuario_session.getId() == copia.getUsuario().getId()) {
                getCurrentSession().removeAttribute("usuario");
                getCurrentSession().setAttribute("usuario", usuario);
            }
            copia = new Copia();
        }
        return null;
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
    
    private EntityManager getEntityManager(){
        FacesContext fc = FacesContext.getCurrentInstance();
        ExternalContext ec = fc.getExternalContext();
        HttpServletRequest request = (HttpServletRequest) ec.getRequest();
        EntityManager manager = (EntityManager)request.getAttribute("EntityManager");
        
        return manager;
    }
    
    //GET e SET
    public Copia getCopia() {
        return copia;
    }

    public void setCopia(Copia copia) {
        this.copia = copia;
    }

    public List<Copia> getCopias() {
        return copias;
    }

    public void setCopias(List<Copia> copias) {
        this.copias = copias;
    }

    public Date getInicio() {
        return inicio;
    }

    public void setInicio(Date inicio) {
        this.inicio = inicio;
    }

    public Date getFim() {
        return fim;
    }

    public void setFim(Date fim) {
        this.fim = fim;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public Limite getLimiteALUNO() {
        return limiteALUNO;
    }

    public void setLimiteALUNO(Limite limiteALUNO) {
        this.limiteALUNO = limiteALUNO;
    }

    public Limite getLimiteSERVIDOR() {
        return limiteSERVIDOR;
    }

    public void setLimiteSERVIDOR(Limite limiteSERVIDOR) {
        this.limiteSERVIDOR = limiteSERVIDOR;
    }

    public PagingInformation getPaginacao() {
        return paginacao;
    }

    public void setPaginacao(PagingInformation paginacao) {
        this.paginacao = paginacao;
    }

}
