package br.com.ifrn.coapac.utils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.faces.event.ActionEvent;
import javax.faces.event.ValueChangeEvent;
import javax.faces.model.SelectItem;

/**
 * Classe usada para informa��es relativas a pagina��o de consultas.
 *
 * @author Renan
 */
public class PagingInformation implements Serializable {
	private static final long serialVersionUID = 1771382512241921754L;

	private static int TAMANHO_PAGINA_DEFAULT = 30;

    /**
     * P�gina atual de navega��o
     */
    private int paginaAtual;

    /**
     * Tamanho de dados da p�gina
     */
    private int tamanhoPagina;

    /**
     * Total de registros da base de dados
     */
    private int totalRegistros;

    /**
     * Fun��es de navaga��o
     *
     * @param e
     */
    public void nextPage(ActionEvent e) {
        if (paginaAtual < getTotalPaginas()) {
            paginaAtual++;
        }

    }

    public void previousPage(ActionEvent e) {
        if (paginaAtual > 0) {
            paginaAtual--;
        }
    }

    public void ultimaPagina(ActionEvent e) {
        paginaAtual = getTotalRegistros() / tamanhoPagina;
    }

    public void primeiraPagina(ActionEvent e) {
        paginaAtual = 0;
    }

    public void changePage(ValueChangeEvent e) {
        if (e.getNewValue() != null) {
            paginaAtual = (Integer) e.getNewValue();
        }
    }

    public PagingInformation(int paginaAtual, int tamanhoPagina) {
        this.paginaAtual = paginaAtual;
        this.tamanhoPagina = tamanhoPagina;
    }

    public PagingInformation() {
        this.tamanhoPagina = TAMANHO_PAGINA_DEFAULT;
    }

    public PagingInformation(int paginaAtual) {
        this.paginaAtual = paginaAtual;
        this.tamanhoPagina = TAMANHO_PAGINA_DEFAULT;
    }

    public int getPaginaAtual() {
        return paginaAtual;
    }

    public void setPaginaAtual(int paginaAtual) {
        this.paginaAtual = paginaAtual;
    }

    public int getTamanhoPagina() {
        return tamanhoPagina;
    }

    public void setTamanhoPagina(int tamanhoPagina) {
        this.tamanhoPagina = tamanhoPagina;
    }

    /**
     * Calcula o total de p�ginas a partir do numero de registros retornados na
     * consulta e o tamanhoPagina(tamanho de registros que devem aparecer na
     * mesma pagina)
     *
     * @return
     */
    public int getTotalPaginas() {
        return (int) Math.ceil((double) totalRegistros / tamanhoPagina);

    }

    public int getTotalRegistros() {
        return totalRegistros;
    }

    public void setTotalRegistros(int totalRegistros) {
        this.totalRegistros = totalRegistros;
    }

    public boolean isUltimaPagina() {
        return ((getTotalPaginas() - 1) == getPaginaAtual());
    }

    public boolean isPossuiMaisDuasPaginas() {
        return paginaAtual + 2 <= getTotalPaginas();
    }

    public boolean isExibirMaisTresPaginas() {
        return paginaAtual + 3 <= getTotalPaginas() && paginaAtual == 0;
    }

    public boolean isPrimeiraPagina() {
        return (getPaginaAtual() == 0);
    }

    /**
     * Retorna combo box para pagina��o
     *
     * @return
     */
    public List<SelectItem> getListaPaginas() {

        ArrayList<SelectItem> paginas = new ArrayList<SelectItem>();

        /*for (int a = 1; a <= getTotalPaginas(); a++) {

			//Subtraiu �ndice da p�gina por -1, pois no banco, o offset funciona a partir de 0
			SelectItem item = new SelectItem(String.valueOf(a - 1), "Pag. " + a);
			paginas.add(item);
		}*/
        if (getTotalPaginas() > 0) {
            for (int a = 0; a < getTotalPaginas(); a++) {
                SelectItem item = new SelectItem(String.valueOf(a), "Pag. " + (a + 1));
                paginas.add(item);
            }
        } else {
            SelectItem item = new SelectItem(String.valueOf(0), "Sem P�ginas");
            paginas.add(item);
        }

        return paginas;

    }

}
