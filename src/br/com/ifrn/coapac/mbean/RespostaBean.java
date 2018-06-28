/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.ifrn.coapac.mbean;

import br.com.ifrn.coapac.model.Postagem;
import br.com.ifrn.coapac.model.Resposta;
import br.com.ifrn.coapac.service.NegocioResposta;
import br.com.ifrn.coapac.utils.AbstractController;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.persistence.EntityManager;

import org.primefaces.context.RequestContext;

import java.io.Serializable;
import java.util.List;

/**
 *
 * @author Luan
 */
@ViewScoped
@ManagedBean
public class RespostaBean extends AbstractController implements Serializable{
	private static final long serialVersionUID = 7847762132808187799L;
	
	Resposta resposta = new Resposta();
	Resposta respostaSelecionada = new Resposta();
    
    public String adicionar(Postagem postagem) {
    	NegocioResposta negocio = this.getMyNegocio();
    	resposta.setPostagem(postagem);
    	negocio.persist(usuario_session, resposta);
        addMsgInfo("Resposta Cadastrada!");
        resposta = new Resposta();
        return null;
    }

    public String remover(Resposta r) {
    	NegocioResposta negocio = this.getMyNegocio();
    	negocio.remove(r.getId());
        addMsgWarning("Resposta removida !");
        return null;
    }
    
    public String atualizar() {
    	NegocioResposta negocio = this.getMyNegocio();
    	negocio.merge(respostaSelecionada);
        addMsgInfo("Resposta Atualizada !");
        return null;
    }
    
    public List<Resposta> getLimiteRespostaByPergunta(Postagem p){
    	NegocioResposta negocio = this.getMyNegocio();
    	List<Resposta> lista = negocio.limiteResposta(p.getId(), 10);
    	return lista;
    }
    
    public String selecionar(Resposta r){
        this.respostaSelecionada = r;
        RequestContext context = RequestContext.getCurrentInstance();
        context.execute("PF('ModalResposta').show();");
        return null;
    }
    
    public NegocioResposta getMyNegocio(){
    	EntityManager gerenciador = this.getEntityManager();
    	return new NegocioResposta(gerenciador);
    }

	public Resposta getResposta() {
		return resposta;
	}

	public void setResposta(Resposta resposta) {
		this.resposta = resposta;
	}

	public Resposta getRespostaSelecionada() {
		return respostaSelecionada;
	}

	public void setRespostaSelecionada(Resposta respostaSelecionada) {
		this.respostaSelecionada = respostaSelecionada;
	}
    
}
