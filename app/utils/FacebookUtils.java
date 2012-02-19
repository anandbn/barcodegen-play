package utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.UUID;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;

import play.libs.WS;
import play.libs.WS.FileParam;
import play.mvc.Http.Response;
import models.DiscountCoupon;

public class FacebookUtils {

	public static void shareCouponOnFacebook(DiscountCoupon coupon) throws UnsupportedEncodingException{
		
			HashMap<String,Object> params = new HashMap<String,Object>();
			params.put("access_token",System.getenv("FB_ACCESS_TOKEN"));
			params.put("link", URLEncoder.encode(String.format(  	"%s/coupon?couponId=%s",
																							System.getenv("APP_BASE_URL"),
																							coupon.couponId)
																	,"UTF-8"));
			params.put("message","Coupon:"+coupon.title);
			File tmpImg = new File(System.getProperty("java.io.tmpdir") + File.separator + coupon.id+".png");
			try{
				FileOutputStream fileOut = new FileOutputStream(tmpImg);
				System.out.println(">>>>>>>>>>>>Img file Path:"+tmpImg.getAbsolutePath()+tmpImg.getName());
				QRCodeGenerator.generateCodeToStream(fileOut,coupon.couponText,
						BarcodeFormat.valueOf(coupon.codeType),coupon.height,coupon.width,coupon.contentType);
				fileOut.close();
				System.out.println(">>>>>>>>>>>>Completed writing image to file: "+tmpImg.getAbsolutePath()+tmpImg.getName());
			}catch(IOException ex){
				ex.printStackTrace();
			} catch (WriterException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			WS.FileParam imageFile = new WS.FileParam(tmpImg, "data");
			WS.HttpResponse response  = WS.url("https://graph.facebook.com/me/photos").params(params).files(imageFile).timeout("5mn").post();
			System.out.println(String.format("Posted Image to /photos. Response code:%s,Body:%s",response.getStatus(),response.getJson().toString()));
			tmpImg.delete();
		}
}
