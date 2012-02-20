package controllers;

import play.*;
import play.db.jpa.JPA;
import play.libs.F;
import play.libs.WS.HttpResponse;
import play.modules.pusher.Pusher;
import play.mvc.*;
import utils.FacebookUtils;
import utils.QRCodeGenerator;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.*;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;

import models.*;
public class Application extends Controller {

    public static void index() {
        availableCoupons();
    }
    
    public static void newCoupon(){
    	render();
    }
    
    public static void coupon(String couponId) {
    	List<DiscountCoupon> results = DiscountCoupon.find("couponid", couponId).fetch();
			
			if(!results.isEmpty()){
				System.out.println(">>>>>>>>>>>>>>>"+results.get(0));
				DiscountCoupon coupon = results.get(0);
				render(coupon);
			}
    	
    }
    
    public static void deleteCoupon(String couponId) {
    	List<DiscountCoupon> results = DiscountCoupon.find("couponid", couponId).fetch();
			
			if(!results.isEmpty()){
				System.out.println(">>>>>>>>>>>>>>>"+results.get(0));
				results.get(0).delete();
				System.out.println(">>>>>>>>>>>>>>> Deleted coupon:"+results.get(0));
				availableCoupons();
			}
			
    }
    
    public static void availableCoupons() {
    	List<DiscountCoupon> coupons = DiscountCoupon.findAll();
		render(coupons);
    }
    
    public static void qrCode(String couponId,String i,String contentType,int width,int height){
     	try {
     		DiscountCoupon coupon=null;
			ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
     		if(couponId!=null){
     			List<DiscountCoupon> results = DiscountCoupon.find("couponid", couponId).fetch();
     			
     			if(!results.isEmpty()){
     				coupon = results.get(0);
     			}
     			System.out.println(String.format(">>>>>>>>>>>>>>> Got Coupon Record: %s",coupon));
         		if(coupon!=null){
        			QRCodeGenerator.generateCodeToStream(byteOut,coupon.couponText,
        					BarcodeFormat.valueOf(coupon.codeType),coupon.height,coupon.width,coupon.contentType);			
         		}
     		}else{
     			if(i==null||i.trim().length()==0){
     				throw new WriterException("Invalid Coupon Number");
     			}
    			if(width==0)
    				width=100;
    			if(height==0)
    				height=100;
    			if(contentType==null)
    				contentType="PNG";
     		}
  			renderBinary(new ByteArrayInputStream(byteOut.toByteArray()));	
     	} catch (WriterException e) {
			// TODO Auto-generated catch block
			renderText(e.getMessage());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			renderText(e.getMessage());
		}
		renderText("Error rendering QR-Code");
    }
  
    
	public static void generateQrCode(String codeTxt, String title, String description,String contentType,String codeType, int width,int height) {
		if(codeType.equalsIgnoreCase("UPC_A")){
			if(codeTxt==null || codeTxt.length()<11){
				validation.addError("codeTxt", "For UPC Codes, the coupon text needs to be a 11 or 12 digit numeric value");
			}
			try{
				Long.parseLong(codeTxt);
			}catch(Exception ex){
				validation.addError("codeTxt", "For UPC Codes, the coupon text needs to be a 11 or 12 digit numeric value");
				params.flash();
				validation.keep();
				newCoupon();
				return;
			}
			
		}else if(codeTxt==null || codeTxt.length()<11){
			validation.addError("codeTxt", "Code Text cannot be empty. Please enter a valid value.");
			params.flash();
			validation.keep();
			newCoupon();
			return;
		}
			
			if(width==0)
				width=100;
			if(height==0)
				height=100;
			if(contentType==null)
				contentType="PNG";
			if(title==null)
				title=codeTxt;
			DiscountCoupon coupon = new DiscountCoupon(	java.util.UUID.randomUUID().toString(), 
																					codeTxt, title, description,
																					width, height, codeType,contentType);
			coupon.create();
			System.out.println(">>>>>>>>>>>> Created Coupon in DB:"+coupon);
			if(System.getenv("FB_ACCESS_TOKEN")!=null && System.getenv("FB_ACCESS_TOKEN").length()>0){
				FacebookUtils job = new FacebookUtils(coupon);
	            job.now();
				System.out.println(">>>>>>>>>>>> Sent Async message to share coupon on Facebook	:"+coupon);
			}
			Pusher pusher = new Pusher();
			String jsonPushMessage = String.format("{\"title\":\"%s\",\"imgSrc\":\"%s\",\"description\":\"%s\"}",
																		coupon.title,
																		"/qrCode?couponId="+coupon.couponId,
																		coupon.description);
			System.out.println(">>>>>>>>>> Sending JSON message:"+jsonPushMessage);
			HttpResponse response = pusher.trigger("coupons", "new_coupon",jsonPushMessage);
	    	System.out.println(String.format("Sent pusher message successfully. Response :%s",response.getStatus()));
	    
			coupon(coupon.couponId);

    }
	/*
    public static void upcCode(Long i,String contentType,int width,int height){
     	try {
			ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
			if(width==0)
				width=100;
			if(height==0)
				height=30;
			if(contentType==null)
				contentType="PNG";

			QRCodeGenerator.generateCodeToStream(byteOut,String.valueOf(i),BarcodeFormat.UPC_A,height,width,contentType);			
			renderBinary(new ByteArrayInputStream(byteOut.toByteArray()));
		} catch (WriterException e) {
			// TODO Auto-generated catch block
			renderText(e.getMessage());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			renderText(e.getMessage());
		}
		renderText("Error rendering UPC Code");
    }
    
    public static void pdf417(String i,String contentType,int width,int height){
     	try {
			ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
			if(width==0)
				width=100;
			if(height==0)
				height=30;
			if(contentType==null)
				contentType="PNG";

			QRCodeGenerator.generateCodeToStream(byteOut,String.valueOf(i),BarcodeFormat.PDF_417,height,width,contentType);			
			renderBinary(new ByteArrayInputStream(byteOut.toByteArray()));
		} catch (WriterException e) {
			// TODO Auto-generated catch block
			renderText(e.getMessage());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			renderText(e.getMessage());
		}
		renderText("Error rendering UPC Code");
    }
    */

}
