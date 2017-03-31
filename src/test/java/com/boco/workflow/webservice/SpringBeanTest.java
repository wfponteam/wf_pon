package com.boco.workflow.webservice;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath*:config/spring/spring-*.xml","classpath*:config/spring/applicationContext*.xml","classpath*:config/spring/local-*.xml"})
public class SpringBeanTest extends AbstractJUnit4SpringContextTests  {
	

	@Test
	public void aopTest(){
		
		
	}

}
