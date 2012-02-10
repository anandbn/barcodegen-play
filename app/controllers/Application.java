package controllers;

import play.*;
import play.db.jpa.JPA;
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
    	System.out.println(String.format("Random UUID:%s",java.util.UUID.randomUUID()));
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
  
    
	public static void generateQrCode(String codeTxt, String title, String description,String contentType, int width,int height) {
		
		DiscountCoupon coupon = new DiscountCoupon(	java.util.UUID.randomUUID().toString(), 
																				codeTxt, title, description,
																				width, height, "QR_CODE",contentType);
		coupon.create();
		System.out.println(">>>>>>>>>>>> Created Coupon in DB:"+coupon);
		try {
			FacebookUtils.shareCouponOnFacebook(coupon);
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			renderText("Coupon generation failed !!!!");
		}
		renderText("Successfully posted to Facebook");
    }
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

}
