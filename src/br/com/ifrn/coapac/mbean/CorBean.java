package br.com.ifrn.coapac.mbean;

import java.util.List;

import javax.faces.bean.ManagedBean;

import br.com.ifrn.coapac.model.Emprestimo;
import br.com.ifrn.coapac.service.NegocioEmprestimo;
import br.com.ifrn.coapac.utils.AbstractController;
import br.com.ifrn.coapac.utils.ValidatorUtil;

@ManagedBean
public class CorBean extends AbstractController{
	private static final long serialVersionUID = -8908630895352283112L;

	public String getCorBarra(){
		NegocioEmprestimo nEmp = new NegocioEmprestimo(getEntityManager());
		List<Emprestimo> lista = nEmp.buscarInadimplente(usuario_session);
		return ValidatorUtil.isEmpty(lista)? "green": "red";    
	}
}
