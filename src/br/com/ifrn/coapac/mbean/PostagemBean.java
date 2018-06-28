/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.ifrn.coapac.mbean;

import br.com.ifrn.coapac.model.Postagem;
import br.com.ifrn.coapac.model.Usuario;
import br.com.ifrn.coapac.service.NegocioPostagem;
import br.com.ifrn.coapac.utils.AbstractController;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.persistence.EntityManager;

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
	private static final long serialVersionUID = -1830894869051510119L;
	
	Postagem postagem = new Postagem();
    Postagem postagemSelecionada = new Postagem();
    
    public String adicionar() {
    	NegocioPostagem negocio = this.getMyNegocio();
    	negocio.persist(usuario_session, postagem);
        addMsgInfo("Postagem Cadastrada!");
        postagem = new Postagem();
        return "/autenticado/mural";
    }

    public String atualizar() {
    	NegocioPostagem negocio = this.getMyNegocio();
    	negocio.merge(postagemSelecionada);
        addMsgInfo("Postagem Atualizada !");
        return null;
    }
    
    public String selecionar(Postagem p){
        this.postagemSelecionada = p;
        RequestContext context = RequestContext.getCurrentInstance();
        context.execute("PF('ModalPostagem').show();");
        return null;
    }

    public String remover() {
    	NegocioPostagem negocio = this.getMyNegocio();
    	negocio.remove(postagem.getId());
        postagem = new Postagem();
        addMsgWarning("Resposta removida !");
        return null;
    }
    
    public String remover(Postagem p) {
    	NegocioPostagem negocio = this.getMyNegocio();
    	negocio.remove(p.getId());
        addMsgWarning("Resposta removida !");
        return null;
    }
    
    public List<Postagem> getListaMinhasPostagems(){
    	NegocioPostagem negocio = this.getMyNegocio();
        return negocio.minhasPostagems(usuario_session);
    }
    
    public List<Postagem> getTodasPostagems(){
    	NegocioPostagem negocio = this.getMyNegocio();
        return negocio.todasPostagems();
    }
    
    public NegocioPostagem getMyNegocio(){
    	EntityManager gerenciador = this.getEntityManager();
    	return new NegocioPostagem(gerenciador);
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
