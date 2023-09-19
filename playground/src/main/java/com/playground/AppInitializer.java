package com.playground;

import org.springframework.web.WebApplicationInitializer;
import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.servlet.DispatcherServlet;

import jakarta.servlet.Servlet;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRegistration;

public class AppInitializer implements WebApplicationInitializer{

	@Override
	public void onStartup(ServletContext container) throws ServletException {
		// TODO Auto-generated method stub
//		AnnotationConfigWebApplicationContext context = new AnnotationConfigWebApplicationContext();
//        context.scan("com.playground");
//        container.addListener(new ContextLoaderListener(context));
//
//        ServletRegistration.Dynamic dispatcher = container.addServlet("mvc", (Servlet) new DispatcherServlet(context));
//        dispatcher.setLoadOnStartup(1);
//        dispatcher.addMapping("/");   
	}

}
