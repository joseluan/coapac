package br.com.ifrn.coapac.utils;

import java.util.Collection;
import java.util.Map;


/**
 * Classe responsável por fornecer métodos úteis para validação de dados
 * em geral.
 * 
 * @author Renan
 */
public class ValidatorUtil {
	
	/**
	 * Valida se string é diferente de null e não é vazia.
	 *
	 * @return
	 */
	public static final boolean isEmpty(String s) {
		return (s == null || s.trim().length() == 0);
	}
	
	/**
	 * Valida se um objeto é vazio. O seu funcionamento vai depender do tipo de objeto
	 * passado como par�metro. Se o objeto for nulo, e vazio. Se for uma String, verifica
	 * se não é string vazia ou não é formada apenas por espaços. Se for uma coleçãoo,
	 * verifica se a coleão está vazia, etc.
	 *
	 */
	public static final boolean isEmpty(Object o) {
		if (o == null)
			return true;
		if (o instanceof String)
			return isEmpty( (String) o);
		if (o instanceof Number) {
			Number i = (Number) o;
			return (i.intValue() == 0);
		}
		if (o instanceof Object[])
			return ((Object[]) o).length == 0;
		if (o instanceof int[])
			return ((int[]) o).length == 0;
		if (o instanceof Collection<?>)
			return ((Collection<?>) o).size() == 0;
		if (o instanceof Map<?, ?>)
			return ((Map<?, ?>) o).size() == 0;
		return false;
	}
	
	public static final boolean isNotEmpty(Object o) {
		return !isEmpty(o);
	}
	
}
