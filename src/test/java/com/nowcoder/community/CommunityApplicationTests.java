package com.nowcoder.community;

import com.nowcoder.community.dao.AlphaDao;
import com.nowcoder.community.service.AlphaService;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import java.text.SimpleDateFormat;
import java.util.Date;

@RunWith(SpringRunner.class)
@SpringBootTest
@ContextConfiguration(classes = CommunityApplication.class)
class CommunityApplicationTests implements ApplicationContextAware {

	private ApplicationContext applicationContext;


	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.applicationContext = applicationContext;
	}

	@Test
	void testApplicationContext() {
		System.out.println(applicationContext);
		AlphaDao dao = applicationContext.getBean(AlphaDao.class);
		System.out.println(dao.select());
		AlphaDao hibernateDao = applicationContext.getBean("alphaHibernate", AlphaDao.class);
		System.out.println(hibernateDao.select());
	}

	@Test
	void testBeanManagerment() {
		AlphaService service = applicationContext.getBean(AlphaService.class);
		System.out.println(service);
	}

	@Test
	void testBeanConfig() {
		SimpleDateFormat sdf = applicationContext.getBean(SimpleDateFormat.class);
		System.out.println(sdf.format(new Date()));
	}

	@Autowired
	private AlphaDao alphaDao;

	@Test
	void testDI() {
		System.out.println(alphaDao);
	}

}
