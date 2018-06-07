package br.com.ifrn.coapac.filtros;

import java.io.IOException;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;

@WebFilter(filterName = "JPAFilter", servletNames={"Faces Servlet"})
public class JPAFilter implements Filter{


    private EntityManagerFactory fabrica;
    
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        this.fabrica = Persistence.createEntityManagerFactory("COAPAC-PU");
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
  
        EntityManager gerenciador = this.fabrica.createEntityManager();
        request.setAttribute("EntityManager", gerenciador);
        gerenciador.getTransaction().begin();
        
        chain.doFilter(request, response);
    
        try {
            gerenciador.getTransaction().commit();
        } catch (Exception e) {
            gerenciador.getTransaction().rollback();
        } finally {
            gerenciador.close();
        }
    }

    @Override
    public void destroy() {
        this.fabrica.close();
    }

}
