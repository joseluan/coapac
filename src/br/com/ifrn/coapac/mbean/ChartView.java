/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.ifrn.coapac.mbean;

import java.io.Serializable;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import org.primefaces.model.chart.Axis;
import org.primefaces.model.chart.AxisType;
import org.primefaces.model.chart.CategoryAxis;
import org.primefaces.model.chart.ChartSeries;
import org.primefaces.model.chart.LineChartModel;

import br.com.ifrn.coapac.service.NegocioCopia;
import br.com.ifrn.coapac.utils.AbstractController;

/**
 *
 * @author Luan
 */
@ManagedBean
public class ChartView extends AbstractController implements Serializable {
	private static final long serialVersionUID = -3672316628230453287L;
	private LineChartModel lineModel2;

	@PostConstruct
	public void init() {
		createLineModels();
	}

	public LineChartModel getLineModel2() {
		return lineModel2;
	}

	private void createLineModels() {
		lineModel2 = initCategoryModel();
		lineModel2.setTitle("Grafico da quantidade geral de copias pelos ultimos 6 meses");
		lineModel2.setLegendPosition("e");
		lineModel2.setShowPointLabels(true);
		lineModel2.getAxes().put(AxisType.X, new CategoryAxis("Meses"));
		Axis yAxis = lineModel2.getAxis(AxisType.Y);
		yAxis.setLabel("Quantidade");
		yAxis.setMin(0);
	}

	private LineChartModel initCategoryModel() {
		LineChartModel model = new LineChartModel();

		String[] meses = {"", "Janeiro", "Fevereiro", "Março", "Abril", "Maio", "Junho", "Julho", "Agosto", "Setembro",
				"Outubro", "Novembro", "Dezembro" };
		ChartSeries linha = new ChartSeries();
		NegocioCopia negocio = new NegocioCopia(getEntityManager());
		linha.setLabel("Quantidade de cópias");

		List<Object[]> lista = negocio.buscarListaGrafico();
		HashMap grafico = new HashMap<String, Integer>();
		for(Object[] objs: lista){
			int cont = 0;
			int mes = 0;
			int ano = 0;
			int quantidade = 0;
			for(Object item : objs){
				if(cont == 1) quantidade = Integer.parseInt(item.toString());
				if(cont == 0) mes = Integer.parseInt(item.toString());
				if(cont == 2) ano = Integer.parseInt(item.toString());
				cont++;
			}
			if(grafico.keySet().contains((meses[mes]))) quantidade += (int) grafico.get((meses[mes]));
			linha.set(meses[mes]+", "+ano, quantidade);
			grafico.put(meses[mes], quantidade);
		}

		model.addSeries(linha);
		return model;
	}
}
