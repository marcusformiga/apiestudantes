package com.apistudents;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

@Component
public class ApplicationContextLoad  implements ApplicationContextAware{
	@Autowired
	private static ApplicationContext applicationContext;
	@SuppressWarnings("static-access")
	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.applicationContext = applicationContext;
		
	}
	// pegar a instancia do usersRepo
	public static ApplicationContext getAppContext() {
		return applicationContext;
	}
}
