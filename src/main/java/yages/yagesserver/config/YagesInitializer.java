package yages.yagesserver.config;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRegistration;
import javax.servlet.annotation.WebServlet;
import org.springframework.web.servlet.support.AbstractAnnotationConfigDispatcherServletInitializer;

public class YagesInitializer extends AbstractAnnotationConfigDispatcherServletInitializer {

    @Override
    protected String[] getServletMappings() {
        return new String[]{"/"};
    }

    @Override
    protected Class<?>[] getRootConfigClasses() {
        return new Class<?>[]{RootConfig.class};
    }

    @Override
    protected Class<?>[] getServletConfigClasses() {
        return new Class<?>[]{WebConfig.class};
    }
  
//    @Override
//    public void onStartup(ServletContext servletContext)    throws ServletException {
//        super.onStartup(servletContext);
//        ServletRegistration.Dynamic servlet = servletContext
//            .addServlet("h2-console", new WebServlet());
//        servlet.setLoadOnStartup(2);
//        servlet.addMapping("/console/*");
//}

    
}


