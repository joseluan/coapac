/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.ifrn.coapac.mbean;

import br.com.ifrn.coapac.utils.PagingInformation;
import br.com.ifrn.coapac.dao.EmprestimoDAO;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.persistence.EntityManager;
import javax.servlet.http.HttpServletRequest;

import br.com.ifrn.coapac.model.Emprestimo;
import br.com.ifrn.coapac.model.Material;
import br.com.ifrn.coapac.model.TipoSituacao;
import br.com.ifrn.coapac.model.TipoSolicitacao;
import br.com.ifrn.coapac.model.TipoUsuario;
import br.com.ifrn.coapac.model.Usuario;
import org.primefaces.context.RequestContext;
import br.com.ifrn.coapac.utils.AbstractController;
import br.com.ifrn.coapac.utils.ValidatorUtil;

/**
 *
 * @author Luan
 */
@ManagedBean
@ViewScoped
public class EmprestimoBean extends AbstractController implements Serializable {
	private static final long serialVersionUID = -570856485295932070L;
	
	private Emprestimo emprestimo = new Emprestimo();
    private Usuario usuario = new Usuario();
    private Emprestimo emprestimoSelecionado = new Emprestimo();
    private List<Emprestimo> emprestimos = new ArrayList<>();
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
        emprestimo.setMaterial(new Material());
        emprestimo.setUsuario_sol(new Usuario());
        emprestimo.setUsuario_ent(new Usuario());
        emprestimo.setUsuario_emp(new Usuario());
        paginacao = new PagingInformation(0, QTD_CODIGOS);
    }

    public List<Emprestimo> getMeusEmprestimos() {
    	EntityManager gerenciador = this.getEntityManager();
    	EmprestimoDAO dao = new EmprestimoDAO(gerenciador);
        List<Emprestimo> lista = dao.minhaLista(usuario_session, null, 20);
        return lista;
    }
    
    public String minhasSolicitacoes() {
        Usuario ubusca = usuario_session;
        //Identificar no DAO o que é usuario_session
        ubusca.setIsSession(true);
        emprestimo.setUsuario_sol(ubusca);
        listaFiltro();
        return null;
    }

    public List<Emprestimo> getEmprestimosPara(Usuario usuario_busca) {
    	EntityManager gerenciador = this.getEntityManager();
    	EmprestimoDAO dao = new EmprestimoDAO(gerenciador);
        List<Emprestimo> lista = dao.minhaLista(usuario_session, usuario_busca, 0);
        return lista;
    }

    public List<Emprestimo> getListaExpirado() {
    	EntityManager gerenciador = this.getEntityManager();
    	EmprestimoDAO dao = new EmprestimoDAO(gerenciador);
        List<Emprestimo> lista = dao.buscarInadimplente(usuario_session);
        if (ValidatorUtil.isEmpty(lista)) {
            return null;
        }
        return lista;
    }

    public String getCorBarraInadimplente() {
        if (ValidatorUtil.isNotEmpty(getListaExpirado())) {
            return "red";
        } else {
            return "green";
        }
    }

    public boolean userIsInadimplente(Usuario usuario) {
    	EntityManager gerenciador = this.getEntityManager();
    	EmprestimoDAO dao = new EmprestimoDAO(gerenciador);
        List<Emprestimo> lista = dao.buscarInadimplente(usuario);
        return ValidatorUtil.isNotEmpty(lista);
    }

    public String listaFiltro() {
    	EntityManager gerenciador = this.getEntityManager();
    	EmprestimoDAO dao = new EmprestimoDAO(gerenciador);
        if (ValidatorUtil.isNotEmpty(emprestimos)) {
            if (emprestimos.size() == 1) {
                paginacao.setPaginaAtual(0);
            }
        }
        if (ValidatorUtil.isNotEmpty(inicio) && ValidatorUtil.isNotEmpty(fim)) {
            emprestimos = dao.buscarFiltro(emprestimo, paginacao, inicio, fim);
        } else {
            emprestimos = dao.buscarFiltro(emprestimo, paginacao);
        }
        return null;
    }
    
    //pagina de inicio
    public List<Emprestimo> getListaSolicitacao() {
    	EntityManager gerenciador = this.getEntityManager();
    	EmprestimoDAO dao = new EmprestimoDAO(gerenciador);
        List<Emprestimo> lista = dao.buscarSolicitacoes(usuario_session, 5);
        return lista;
    }

    public String remover(Emprestimo emprestimo_rm) {
    	EntityManager gerenciador = this.getEntityManager();
    	EmprestimoDAO dao = new EmprestimoDAO(gerenciador);
        dao.remove(emprestimo_rm.getId());
        listaFiltro();
        return null;
    }

    @SuppressWarnings({ "deprecation", "incomplete-switch" })
	public String atualizarSolicitacao(Emprestimo selecionado) {
    	EntityManager gerenciador = this.getEntityManager();
    	EmprestimoDAO dao = new EmprestimoDAO(gerenciador);
        if (selecionado.getSolicitacao() == TipoSolicitacao.DEFERIDO) {
            if (selecionado.getMaterial().getQuantidade() <= 0) {
                return null;
            }

            selecionado.setSituacao(TipoSituacao.EMPRESTADO);
            Material m = selecionado.getMaterial();
            m.setQuantidade(m.getQuantidade() - 1);
            selecionado.setMaterial(m);
            Date d = new Date();
            //--- verifica se é aluno ou servidor
            if (usuario_session.getTipo() == TipoUsuario.ALUNO) {
                //--- Define a quantidade de dias para pode entregar o material
                switch (m.getTipo()) {
                    case ARMARIO:
                        return null;//Aluno não pode solicitar armario
                    case LIVRO:
                        d.setYear(d.getYear() + 1);//1 ano
                        break;
                    case DIARIO:
                        d.setDate(d.getDate() + 1);//1 dia
                        break;
                }
            } else if (usuario_session.getTipo() == TipoUsuario.SERVIDOR) {
                //--- Define a quantidade de dias para pode entregar o material
                switch (m.getTipo()) {
                    case ARMARIO:
                        d.setYear(d.getYear() + 10);//10 anos
                        break;
                    case LIVRO:
                        d.setYear(d.getYear() + 3);//3 anos
                        break;
                    case DIARIO:
                        d.setDate(d.getDate() + 3);//3 dias
                        break;
                    case PERMANENTE:
                        d.setDate(d.getDate() + 1);//3 dias
                        break;
                }
            }
            selecionado.setData_expiracao(d);
        }

        //Impede que o bolsista aprove sua própia solicitação de empréstimo
        if (usuario_session.getAcesso().equals(TipoUsuario.BOLSISTA)
                && selecionado.getUsuario_sol().getAcesso().equals(TipoUsuario.BOLSISTA)) {
            return null;
        }

        selecionado.setUsuario_emp(usuario_session);
        dao.merge(selecionado);
        listaFiltro();
        return null;
    }

    public String atualizarEntrega(Emprestimo selecionado) {
    	EntityManager gerenciador = this.getEntityManager();
    	EmprestimoDAO dao = new EmprestimoDAO(gerenciador);
        selecionado.setSituacao(TipoSituacao.ENTREGUE);
        selecionado.setUsuario_ent(usuario_session);
        selecionado.setData_entrega(new Date());

        Material m = selecionado.getMaterial();
        m.setQuantidade(m.getQuantidade() + 1);
        selecionado.setMaterial(m);
        dao.merge(selecionado);

        listaFiltro();
        return null;
    }

    public String selecionar(Emprestimo emp) {
        this.emprestimoSelecionado = emp;
        RequestContext context = RequestContext.getCurrentInstance();
        context.execute("PF('dialogo').show();");
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
    public Emprestimo getEmprestimo() {
        return emprestimo;
    }

    public void setEmprestimo(Emprestimo emprestimo) {
        this.emprestimo = emprestimo;
    }

    public List<Emprestimo> getEmprestimos() {
        return emprestimos;
    }

    public void setEmprestimos(List<Emprestimo> emprestimos) {
        this.emprestimos = emprestimos;
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

    public Emprestimo getEmprestimoSelecionado() {
        return emprestimoSelecionado;
    }

    public void setEmprestimoSelecionado(Emprestimo emprestimoSelecionado) {
        this.emprestimoSelecionado = emprestimoSelecionado;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public PagingInformation getPaginacao() {
        return paginacao;
    }

    public void setPaginacao(PagingInformation paginacao) {
        this.paginacao = paginacao;
    }

}
