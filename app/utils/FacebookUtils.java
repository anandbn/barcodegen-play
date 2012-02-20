package utils;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.UUID;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.*;
import org.apache.http.entity.mime.*;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.client.*;

import models.DiscountCoupon;

public class FacebookUtils {

	public static void shareCouponOnFacebook(DiscountCoupon coupon) throws UnsupportedEncodingException{
		
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
			/*WS.FileParam imageFile = new WS.FileParam(tmpImg, "data");
			WS.HttpResponse response  = WS.url("https://graph.facebook.com/me/photos").params(params).files(imageFile).timeout("5mn").post();
			System.out.println(String.format("Posted Image to /photos. Response code:%s,Body:%s",response.getStatus(),response.getJson().toString()));
			*/
			 HttpClient httpClient = new DefaultHttpClient();
			MultipartEntity entity = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE);
			entity.addPart("message",new StringBody("Coupon:"+coupon.title));
			entity.addPart("access_token",new StringBody(System.getenv("FB_ACCESS_TOKEN")));
			entity.addPart("link", new StringBody(URLEncoder.encode(String.format(  	"%s/coupon?couponId=%s",
																														System.getenv("APP_BASE_URL"),
																														coupon.couponId)
																								,"UTF-8")
																)
			);
			FileBody fileBody = new FileBody(tmpImg);
			entity.addPart("data", fileBody);

			HttpPost httpPost = new HttpPost("https://graph.facebook.com/me/photos");
			httpPost.setEntity(entity);
			HttpResponse response=null;
			try {
				response = httpClient.execute(httpPost);
				HttpEntity result = response.getEntity();
				byte[] readBuf = new byte[new Long(result.getContentLength()).intValue()];	
				InputStream in = result.getContent();
				in.read(readBuf);
				
				System.out.println(">>>>>>>>>>>Response from posting:"+new String(readBuf));
			} catch (ClientProtocolException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			tmpImg.delete();
		}
}
