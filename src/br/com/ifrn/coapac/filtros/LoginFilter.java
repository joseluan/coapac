/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.ifrn.coapac.filtros;

import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import br.com.ifrn.coapac.utils.ValidatorUtil;

/**
 *
 * @author Luan
 */
@WebFilter(filterName = "LoginFilter", urlPatterns = {"/*"})
public class LoginFilter implements Filter {

    private final String[] urlsPermitidas = {};

    @Override
    public void destroy() {

    }

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
            throws IOException, ServletException {
        //URL's
        String URL = "/coapac/";
        String URLpublica = URL + "publico/";
        String URLaluno = URL + "aluno/";
        String URLbolsista = URL + "bolsista/";
        String URLservidor = URL + "servidor/";

        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) res;

        HttpSession session = request.getSession();
        String reqUrl = request.getRequestURI();
        String acesso = "";
        boolean permitido = false;
        
        try {
            if (ValidatorUtil.isNotEmpty(session.getAttribute("acesso")))
                acesso = session.getAttribute("acesso").toString();
        } catch (NullPointerException e) {
            e.printStackTrace();
        }

        if (reqUrl.contains("/arquivos/") || reqUrl.contains("/publico/") ||
            reqUrl.equals(URL) || reqUrl.contains(".faces.")) {
            permitido = true;
        }
        
        //  --- Restringindo acesso as páginas ---
        if (!ValidatorUtil.isEmpty(session.getAttribute("usuario"))
                && reqUrl.contains("aluno") && acesso.equals("ALUNO")) {
            permitido = true;
        } else if (!ValidatorUtil.isEmpty(session.getAttribute("usuario"))
                && reqUrl.contains("bolsista") && acesso.equals("BOLSISTA")) {
            permitido = true;
        } else if (!ValidatorUtil.isEmpty(session.getAttribute("usuario"))
                && reqUrl.contains("servidor") && acesso.equals("SERVIDOR")) {
            permitido = true;
        }else if (!ValidatorUtil.isEmpty(session.getAttribute("usuario"))
                && reqUrl.contains("/autenticado/")) {
            permitido = true;
        }
        
        //  --- Se estiver logado e tentar acessar o login ---
        if (!ValidatorUtil.isEmpty(session.getAttribute("usuario")) && reqUrl.contains("login")) {
            switch (acesso) {
                case "ALUNO":
                    response.sendRedirect(URLaluno + "inicio.xhtml");
                    break;
                case "BOLSISTA":
                    response.sendRedirect(URLbolsista + "inicio.xhtml");
                    break;
                case "SERVIDOR":
                    response.sendRedirect(URLservidor + "inicio.xhtml");
                    break;
                default:
                    break;
            }
        }
 
        //deixa permitido se uma url estiver na lista de url permitidas
        if (!permitido) {
            for (String url : urlsPermitidas) {
                if (reqUrl.contains(url)) {
                    permitido = true;
                    break;
                }
            }
        }

        if (permitido) {
            chain.doFilter(request, response);
        } else {
            response.sendRedirect(URLpublica + "login.xhtml");
        }

    }

    @Override
    public void init(FilterConfig arg0) throws ServletException {

    }
}
