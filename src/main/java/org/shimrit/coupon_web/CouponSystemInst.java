package org.shimrit.coupon_web;

import client.CouponSystem;

public abstract class CouponSystemInst {
	public static CouponSystem couponSystem = null;
	
	public static void initCouponSystem(){
		couponSystem = CouponSystem.getInstance();
	}
}
