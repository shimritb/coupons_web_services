package org.shimrit.coupon_web;

import java.time.LocalDate;
import java.util.HashMap;

import db_services.CouponClientFacade;

// TODO: add thread to go over facade collection and delete facaded that are old
// TODO: example of calculation:
//	long minutes = ChronoUnit.MINUTES.between(fromDate, toDate);
//	long hours = ChronoUnit.HOURS.between(fromDate, toDate);
public class FacadeManager {
	private final long limitFacadeLifeTime = 30;
	private HashMap<String, FacadeTime> facadeCollection = null;
	
	private static FacadeManager instance = null;
	
	private FacadeManager(){
		facadeCollection = new HashMap<String, FacadeTime>();
	};
	
	public static synchronized FacadeManager getInstance() {
		if(instance == null){
            instance = new FacadeManager();
        }
        return instance;
	}
	
	public synchronized void addFacade(String key, CouponClientFacade facade, String userName) {
		FacadeTime someFacade = facadeCollection.get(key);
		
		if(someFacade == null) {
			facadeCollection.put(key, new FacadeTime(LocalDate.now(), facade, userName) );
		} 
		
		System.out.println(facadeCollection.toString());
	}
	
	public synchronized void removeFacade(String key) {
		FacadeTime someFacade = facadeCollection.get(key);
		
		if(someFacade != null) {
			facadeCollection.remove(key);
		} 
	}
	
	public synchronized CouponClientFacade getFacade(String key) {
		FacadeTime someFacade = facadeCollection.get(key);
		
		if(someFacade != null) {
			return someFacade.getFacade();
		} else return null;
	}
	
	private class FacadeTime {
		private LocalDate initialTime;
		private CouponClientFacade facade;
		private String userName;
		
		public FacadeTime(LocalDate initialTime, CouponClientFacade facade, String userName) {
			this.initialTime = initialTime;
			this.facade = facade;
		}
		
		public CouponClientFacade getFacade() {
			return facade;
		}
		
		public LocalDate getInitialTime() {
			return initialTime;
		}
		
		public String getUserName() {
			return userName;
		}
	}
}
