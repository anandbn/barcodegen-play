package utils;

import java.util.HashMap;

import play.libs.WS;
import models.DiscountCoupon;

public class FacebookUtils {

	public static void shareCouponOnFacebook(DiscountCoupon coupon){
		HashMap<String,Object> params = new HashMap<String,Object>();
		params.put("access_token",System.getenv("FB_ACCESS_TOKEN"));
		params.put("link", String.format("http://barcodegen.herokuapp.com/coupon?couponId=%s",coupon.couponId));
		WS.url("https://graph.facebook.com/me/links").params(params).post();
		
	}
}
