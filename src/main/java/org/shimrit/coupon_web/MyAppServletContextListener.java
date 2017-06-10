package org.shimrit.coupon_web;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import client.CouponSystem;

public class MyAppServletContextListener  implements ServletContextListener {
	
	@Override
	public void contextDestroyed(ServletContextEvent arg0) {
		System.out.println("ServletContextListener destroyed:  CouponSystem destroyed");
		CouponSystemInst.couponSystem.shutDown();
	}

    // Run this before web application is started
	@Override
	public void contextInitialized(ServletContextEvent arg0) {
		System.out.println("ServletContextListener started: CouponSystem initialised");
		CouponSystemInst.couponSystem = CouponSystem.getInstance();	
	}
}
