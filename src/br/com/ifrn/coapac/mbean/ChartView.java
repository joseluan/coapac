/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.ifrn.coapac.mbean;

import br.com.ifrn.coapac.dao.EmprestimoDAO;
import java.io.Serializable;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.persistence.EntityManager;
import javax.servlet.http.HttpServletRequest;
import br.com.ifrn.coapac.model.Material;
import org.primefaces.model.chart.Axis;
import org.primefaces.model.chart.AxisType;
import org.primefaces.model.chart.BarChartModel;
import org.primefaces.model.chart.ChartSeries;

/**
 *
 * @author Luan
 */
@ManagedBean
public class ChartView implements Serializable {
	private static final long serialVersionUID = -4913075901615031151L;
	private BarChartModel barModel;

    @PostConstruct
    public void init() {
        createBarModels();
    }

    public BarChartModel getBarModel() {
        return barModel;
    }

    private BarChartModel initBarModel() {
    	EntityManager gerenciador = this.getEntityManager();
        EmprestimoDAO empDAO = new EmprestimoDAO(gerenciador);

        BarChartModel model = new BarChartModel();

        ChartSeries materiais = new ChartSeries();
        materiais.setLabel("Materiais");
        List<Material> lista = empDAO.buscarQuantidadeSolicitacoesMaterial(10);
        lista.forEach((_item) -> {
            materiais.set(_item.getNome(), _item.getQuantidade());
        });
        
        model.addSeries(materiais);
        return model;
    }

    private void createBarModels() {
        createBarModel();
    }

    private void createBarModel() {
        barModel = initBarModel();

        barModel.setTitle("Materiais mais solicitados e deferidos");
        barModel.setLegendPosition("ne");

        Axis xAxis = barModel.getAxis(AxisType.X);
        xAxis.setLabel("Material");

        Axis yAxis = barModel.getAxis(AxisType.Y);
        yAxis.setLabel("Quantidade de solicitações");
        yAxis.setMin(0);
    }

    private EntityManager getEntityManager() {
        FacesContext fc = FacesContext.getCurrentInstance();
        ExternalContext ec = fc.getExternalContext();
        HttpServletRequest request = (HttpServletRequest) ec.getRequest();
        EntityManager manager = (EntityManager) request.getAttribute("EntityManager");

        return manager;
    }
}
