/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.ifrn.coapac.mbean;

import br.com.ifrn.coapac.dao.PostagemDAO;
import br.com.ifrn.coapac.model.Postagem;
import br.com.ifrn.coapac.model.Usuario;
import br.com.ifrn.coapac.utils.AbstractController;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.persistence.EntityManager;
import javax.servlet.http.HttpServletRequest;

import java.io.Serializable;
import java.util.List;
import org.primefaces.context.RequestContext;

/**
 *
 * @author Luan
 */
@ViewScoped
@ManagedBean
public class PostagemBean extends AbstractController implements Serializable{
	private static final long serialVersionUID = -2467233559358128533L;
	Postagem postagem = new Postagem();
    Postagem postagemSelecionada = new Postagem();
    
    public String adicionar() {
    	EntityManager gerenciador = this.getEntityManager();
        PostagemDAO dao = new PostagemDAO(gerenciador);
        dao.persist(usuario_session, postagem);
        addMsgInfo("Postagem Cadastrada!");
        postagem = new Postagem();
        return "/autenticado/mural";
    }

    public String atualizar() {
    	EntityManager gerenciador = this.getEntityManager();
        PostagemDAO dao = new PostagemDAO(gerenciador);
        dao.merge(postagemSelecionada);
        addMsgInfo("Postagem Atualizada !");
        return null;
    }
    
    public String selecionar(Postagem p){
        this.postagemSelecionada = p;
        RequestContext context = RequestContext.getCurrentInstance();
        context.execute("PF('ModalEditar').show();");
        return null;
    }

    public String remover() {
    	EntityManager gerenciador = this.getEntityManager();
        PostagemDAO dao = new PostagemDAO(gerenciador);
        dao.remove(postagem.getId());
        postagem = new Postagem();
        addMsgWarning("Resposta removida !");
        return null;
    }
    
    public String remover(Postagem p) {
    	EntityManager gerenciador = this.getEntityManager();
        PostagemDAO dao = new PostagemDAO(gerenciador);
        dao.remove(p.getId());
        addMsgWarning("Resposta removida !");
        return null;
    }
    
    public List<Postagem> getListaMinhasPostagems(){
    	EntityManager gerenciador = this.getEntityManager();
        PostagemDAO dao = new PostagemDAO(gerenciador);
        return dao.minhasPostagems(usuario_session);
    }
    
    public List<Postagem> getTodasPostagems(){
    	EntityManager gerenciador = this.getEntityManager();
        PostagemDAO dao = new PostagemDAO(gerenciador);
        return dao.todasPostagems();
    }
    
    private EntityManager getEntityManager(){
        FacesContext fc = FacesContext.getCurrentInstance();
        ExternalContext ec = fc.getExternalContext();
        HttpServletRequest request = (HttpServletRequest) ec.getRequest();
        EntityManager manager = (EntityManager)request.getAttribute("EntityManager");
        
        return manager;
    }
    
    //GET e SET
    public Postagem getPostagem() {
        return postagem;
    }

    public void setPostagem(Postagem postagem) {
        this.postagem = postagem;
    }

    public Usuario getUsuario_session() {
        return usuario_session;
    }

    public void setUsuario_session(Usuario usuario_session) {
        this.usuario_session = usuario_session;
    }

    public Postagem getPostagemSelecionada() {
        return postagemSelecionada;
    }

    public void setPostagemSelecionada(Postagem postagemSelecionada) {
        this.postagemSelecionada = postagemSelecionada;
    }
    
}
