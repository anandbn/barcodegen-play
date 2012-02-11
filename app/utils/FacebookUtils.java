package utils;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;

import play.libs.WS;
import models.DiscountCoupon;

public class FacebookUtils {

	public static void shareCouponOnFacebook(DiscountCoupon coupon) throws UnsupportedEncodingException{
		HashMap<String,Object> params = new HashMap<String,Object>();
		params.put("q",URLEncoder.encode(String.format("http://barcodegen.herokuapp.com/coupon?couponId=%s",coupon.couponId),"UTF-8"));
		params.put("fbrefresh","true");
		WS.url("http://developers.facebook.com/tools/debug/og/object").params(params).get();	
		System.out.println(">>>>>>>>>>>Refreshed thumbnail link");
		params = new HashMap<String,Object>();
		params.put("access_token",System.getenv("FB_ACCESS_TOKEN"));
		params.put("link", URLEncoder.encode(String.format("http://barcodegen.herokuapp.com/coupon?couponId=%s",coupon.couponId),"UTF-8"));
		//params.put("picture", URLEncoder.encode(String.format("http://barcodegen.herokuapp.com/public/images/favicon.png"),"UTF-8"));
		WS.url("https://graph.facebook.com/me/links").params(params).post();
	}
}
