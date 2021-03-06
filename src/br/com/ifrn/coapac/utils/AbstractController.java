package br.com.ifrn.coapac.utils;

import java.io.Serializable;

import javax.faces.application.FacesMessage;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.persistence.EntityManager;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import br.com.ifrn.coapac.model.Usuario;

/**
 * Classe que implementa métodos comuns a todos os controladores do sistema.
 * Portanto, deve ser estendido por eles.
 *
 * @author Renan
 */
@SuppressWarnings("serial")
public abstract class AbstractController implements Serializable {
    
    public Usuario usuario_session = (Usuario) getCurrentSession().getAttribute("usuario");

    protected void addMsgInfo(String msg) {
        FacesMessage fm = new FacesMessage(FacesMessage.SEVERITY_INFO, msg, null);
        FacesContext.getCurrentInstance().addMessage(null, fm);
    }

    protected void addMsgWarning(String msg) {
        FacesMessage fm = new FacesMessage(FacesMessage.SEVERITY_WARN, msg, null);
        FacesContext.getCurrentInstance().addMessage(null, fm);
    }

    protected void addMsgError(String msg) {
        FacesMessage fm = new FacesMessage(FacesMessage.SEVERITY_ERROR, msg, null);
        FacesContext.getCurrentInstance().addMessage(null, fm);
    }
    
    /**
     * Possibilita o acesso ao EntityManager.
     * @return EntityManager
     */
    protected EntityManager getEntityManager(){
        FacesContext fc = FacesContext.getCurrentInstance();
        ExternalContext ec = fc.getExternalContext();
        HttpServletRequest request = (HttpServletRequest) ec.getRequest();
        EntityManager manager = (EntityManager)request.getAttribute("EntityManager");
        
        return manager;
    }

    /**
     * Possibilita o acesso ao HttpServletRequest.
     * @return HttpServletRequest
     */
    public HttpServletRequest getCurrentRequest() {
        return (HttpServletRequest) getExternalContext().getRequest();
    }

    /**
     * Possibilita o acesso ao HttpServletResponse.
     * @return HttpServletResponse
     */
    public HttpServletResponse getCurrentResponse() {
        return (HttpServletResponse) getExternalContext().getResponse();
    }

    /**
     * Possibilita o acesso ao HttpSession.
     * @return HttpSession
     */
    public HttpSession getCurrentSession() {
        return getCurrentRequest().getSession(true);
    }

    /**
     * Acessa o external context do JavaServer Faces
	 *
     */
    private ExternalContext getExternalContext() {
        return FacesContext.getCurrentInstance().getExternalContext();
    }

    public String getParameter(String param) {
        return getCurrentRequest().getParameter(param);
    }

    public Integer getParameterInt(String param) {
        return Integer.parseInt(getParameter(param));
    }

    public Integer getParameterInt(String param, int padrao) {
        String valor = getParameter(param);
        return valor != null ? Integer.parseInt(valor) : padrao;
    }
}