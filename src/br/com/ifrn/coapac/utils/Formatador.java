package br.com.ifrn.coapac.utils;

import java.text.SimpleDateFormat;
import java.util.Date;
import javax.faces.bean.ManagedBean;

/**
 * Classe que contém métodos para formatação de diversos tipos de dados.
 * @author Renan
 *
 */

@ManagedBean
public class Formatador {
	
	private static Formatador singleton;
	
	/**
	 * Mantem formatos para datas e horas.
	 */
	private SimpleDateFormat df, dfH, horaF;
	
	/**
	 * Contrutor Padrão
	 */
	public Formatador() {
		df = new SimpleDateFormat("dd/MM/yyyy");
		df.setLenient(false);
		dfH = new SimpleDateFormat("dd/MM/yyyy HH:mm");
		horaF = new SimpleDateFormat("HH:mm");
	}
	
	/**
	 * Retorna uma instância do Formatador. Singleton.
	 *
	 * @return
	 */
	public static Formatador getInstance() {
		if (singleton == null) {
			singleton = new Formatador();
		}
		return singleton;
	}
	
	/**
	 * Formata o nome de usuário para os 2 primeiros nomes
	 *
	 * @param nome
	 * @return String
	 */
	public String formatarNomeUsuario(String nome) {
		String[] listanome= nome.split(" ");
                return listanome[0] + " " + listanome[1];
	}
	/**
	 * Formata data e hora. Máscara: dd/MM/yyyy HH:mm
	 *
	 * @param data
	 * @return
	 */
	public String formatarDataHora(Date data) {
		return dfH.format(data);
	}
	
	/**
	 * Formatar hora com HH:mm
	 * @param data
	 * @return
	 */
	public String formatarHora(Date data) {
		try {
			return horaF.format(data);
		} catch (Exception e) {
			return null;
		}
	}
	
	/**
	 * Formata data no formato dd/MM/yyyy
	 */
	public String formatarData(Date data) {
		return (data == null ? "" : df.format(data));
	}
}
