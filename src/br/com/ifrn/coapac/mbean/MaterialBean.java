/*
r5 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.ifrn.coapac.mbean;

import br.com.ifrn.coapac.model.Ativo;
import java.io.Serializable;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.persistence.EntityManager;
import javax.servlet.http.HttpServletRequest;

import br.com.ifrn.coapac.model.Material;
import br.com.ifrn.coapac.service.NegocioEmprestimo;
import br.com.ifrn.coapac.service.NegocioMaterial;

import org.primefaces.context.RequestContext;
import br.com.ifrn.coapac.utils.AbstractController;
import br.com.ifrn.coapac.utils.PagingInformation;
import br.com.ifrn.coapac.utils.ValidatorUtil;

/**
 *
 * @author Luan
 */
@ManagedBean
@ViewScoped
public class MaterialBean extends AbstractController implements Serializable{
	private static final long serialVersionUID = 7694432577145366300L;
	
	private Material material = new Material();
    private List<Material> materiais;
    private Material materialSelecionado = new Material();
    /**
     * Quantidade de c�digos a serem exibidos por p�gina.
     */
    private static final int QTD_CODIGOS = 10;
    /**
     * Armazena as op��es de pagina��o de consulta a c�digos.
     */
    public PagingInformation paginacao = new PagingInformation(0, QTD_CODIGOS);
    
    public int getQtdMaterial() {
    	NegocioMaterial negocio = this.getMyNegocio();
        int quantidade = negocio.qtd();
        return quantidade;
    }

    public String listaFiltro() {
    	NegocioMaterial negocio = this.getMyNegocio();
        if (ValidatorUtil.isNotEmpty(materiais)) {
            if (materiais.size() == 1) {
                 paginacao.setPaginaAtual(0);
            }
        }
        materiais = negocio.buscarFiltro(usuario_session,material,paginacao);
        return null;
    }
    
    public String remover(Material material_rm) {
    	NegocioMaterial negocio = this.getMyNegocio();
    	negocio.remove(material_rm.getId());
        listaFiltro();
        return null;
    }

    public String adicionar() throws InterruptedException {
    	NegocioMaterial negocio = this.getMyNegocio();
        material.setIsAtivo(Ativo.VERDADEIRO);
        negocio.persist(material);
        material = new Material();
        return null;
    }
    
    //NEGOCIO EMPRESTIMO
	public String adicionarEmprestimo() throws InterruptedException {
    	EntityManager gerenciador = this.getEntityManager();
        NegocioEmprestimo negocio = new NegocioEmprestimo(gerenciador);
        negocio.persist(usuario_session, materialSelecionado);
        listaFiltro();
        return null;
    }

    public String atualizar() {
    	NegocioMaterial negocio = this.getMyNegocio();
    	negocio.merge(materialSelecionado);
        listaFiltro();
        return null;
    }
    
    public String selecionar(Material selecionado) {
        this.materialSelecionado = selecionado;
        RequestContext context = RequestContext.getCurrentInstance();
        context.execute("PF('ModalEditar').show();");
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
    
    public NegocioMaterial getMyNegocio(){
    	EntityManager gerenciador = this.getEntityManager();
    	return new NegocioMaterial(gerenciador);
    }
    
    /**
     * Possibilita o acesso ao EntityManager.
     * @return EntityManager
     */
    private EntityManager getEntityManager(){
        FacesContext fc = FacesContext.getCurrentInstance();
        ExternalContext ec = fc.getExternalContext();
        HttpServletRequest request = (HttpServletRequest) ec.getRequest();
        EntityManager manager = (EntityManager)request.getAttribute("EntityManager");
        
        return manager;
    }
    
    //GET e SET
    public Material getMaterial() {
        return material;
    }

    public void setMaterial(Material material) {
        this.material = material;
    }

    public List<Material> getMateriais() {
        return materiais;
    }

    public void setMateriais(List<Material> materiais) {
        this.materiais = materiais;
    }

    public Material getMaterialSelecionado() {
        return materialSelecionado;
    }

    public void setMaterialSelecionado(Material materialSelecionado) {
        this.materialSelecionado = materialSelecionado;
    }

    public PagingInformation getPaginacao() {
        return paginacao;
    }

    public void setPaginacao(PagingInformation paginacao) {
        this.paginacao = paginacao;
    }
        
}
