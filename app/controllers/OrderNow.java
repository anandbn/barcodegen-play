package controllers;

import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;




import play.mvc.Controller;
import xml.FlowResponse;
import com.force.api.*;
import com.force.api.http.*;

public class OrderNow extends Controller {
	public static void start(){
		ApiSession apiSession=null;
		System.out.println("session.get('apiSession')=="+(session.get("apiSession")));
		apiSession = Auth.authenticate(new ApiConfig().setUsername(System.getenv("SFDC_USERNAME"))
	  											  				  		   	.setPassword(System.getenv("SFDC_PASSWORD")));
	
		System.out.println("login:apiSession.getAccessToken="+apiSession.getAccessToken());
		System.out.println("login:apiSession.getApiEndpoint="+apiSession.getApiEndpoint());
		String accessToken= apiSession.getAccessToken();
		String apiEndpoint = apiSession.getApiEndpoint();
		flash.put("accessToken",accessToken);
		flash.put("apiEndpoint",apiEndpoint);
		System.out.println("##################  step-1-login ##################");
		System.out.println(String.format(">>>>>>>>sesionId:%s,Api End point:%s",accessToken,apiEndpoint));
		try {
			HttpRequest request = new HttpRequest().url(apiEndpoint+"/services/flow/startInterview/iPhone_iPad_Purchase")
																		.method("POST")
																		.header("Content-type", "application/x-www-form-urlencoded")
																		.header("Authorization",String.format("OAuth %s",accessToken));
			Http httpClient = new Http();
			HttpResponse response = httpClient.send(request);
			InputStream in = response.getStream();
			StringBuilder b = new StringBuilder();
			byte[] buf = new byte[2000];
			int n = 0;
			while((n=in.read(buf))!=-1) {
				b.append(new String(buf,0,n));
			}
			String responseStr = b.toString();
			JAXBContext jaxbContext = JAXBContext.newInstance((Class) FlowResponse.class);
            FlowResponse flowResponse =  (FlowResponse) jaxbContext.createUnmarshaller().unmarshal(new ByteArrayInputStream(responseStr.getBytes()));
    		System.out.println("##################  step-1:flowResponse ##################");
            System.out.println(flowResponse);
    		System.out.println("##################  step-1:flowResponse.state: ##################");
            System.out.println(flowResponse.getState());
            flash.put("flowState", flowResponse.getState());
			render(flowResponse);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void step2(String selectedProduct){
		String accessToken= flash.get("accessToken");
		String apiEndpoint = flash.get("apiEndpoint");
		flash.put("accessToken",accessToken);
		flash.put("apiEndpoint",apiEndpoint);
		System.out.println("##################  step-2-login ##################");
		System.out.println(String.format(">>>>>>>>sesionId:%s,Api End point:%s,flowState=%s",accessToken,apiEndpoint,flash.get("flowState")));
		try {
			String reqBody = String.format("_action=NEXT&_state=%s&%s.selected=true",URLEncoder.encode(flash.get("flowState"),"UTF-8"),selectedProduct);
			System.out.println("##################  step-2-reqBody ##################");
			System.out.println(reqBody);
			HttpRequest request = new HttpRequest().url(apiEndpoint+"/services/flow/navigateInterview.xml")
																		.method("POST")
																		.content(reqBody.getBytes())
																		.header("Content-type", "application/x-www-form-urlencoded")
																		.header("Authorization",String.format("OAuth %s",accessToken));
																		
			Http httpClient = new Http();
			HttpResponse response = httpClient.send(request);
			System.out.println("##################  step-2-HTTP Status ##################");
			System.out.println(response.getResponseCode());
			InputStream in = response.getStream();
			StringBuilder b = new StringBuilder();
			byte[] buf = new byte[2000];
			int n = 0;
			while((n=in.read(buf))!=-1) {
				b.append(new String(buf,0,n));
			}
			String responseStr = b.toString();
			System.out.println("##################  step-2-responseStr ##################");
			 System.out.println(responseStr);
			JAXBContext jaxbContext = JAXBContext.newInstance((Class) FlowResponse.class);
            FlowResponse flowResponse =  (FlowResponse) jaxbContext.createUnmarshaller().unmarshal(new ByteArrayInputStream(responseStr.getBytes()));
            System.out.println("flowResponse:"+flowResponse);
			
            flash.put("flowState", flowResponse.getState());
			render(flowResponse);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void step3(String selectedProduct){
		String accessToken= flash.get("accessToken");
		String apiEndpoint = flash.get("apiEndpoint");
		flash.put("accessToken",accessToken);
		flash.put("apiEndpoint",apiEndpoint);
		System.out.println("##################  step-3-login ##################");
		System.out.println(String.format(">>>>>>>>sesionId:%s,Api End point:%s,flowState=%s",accessToken,apiEndpoint,flash.get("flowState")));
		try {
			String reqBody = String.format("_action=NEXT&_state=%s&%s.selected=true",URLEncoder.encode(flash.get("flowState"),"UTF-8"),selectedProduct);
			System.out.println("##################  step-3-reqBody ##################");
			System.out.println(reqBody);
			HttpRequest request = new HttpRequest().url(apiEndpoint+"/services/flow/navigateInterview.xml")
																		.method("POST")
																		.content(reqBody.getBytes())
																		.header("Content-type", "application/x-www-form-urlencoded")
																		.header("Authorization",String.format("OAuth %s",accessToken));
																		
			Http httpClient = new Http();
			HttpResponse response = httpClient.send(request);
			System.out.println("##################  step-3-HTTP Status ##################");
			System.out.println(response.getResponseCode());
			InputStream in = response.getStream();
			StringBuilder b = new StringBuilder();
			byte[] buf = new byte[2000];
			int n = 0;
			while((n=in.read(buf))!=-1) {
				b.append(new String(buf,0,n));
			}
			String responseStr = b.toString();
			System.out.println("##################  step-3-responseStr ##################");
			 System.out.println(responseStr);
			JAXBContext jaxbContext = JAXBContext.newInstance((Class) FlowResponse.class);
            FlowResponse flowResponse =  (FlowResponse) jaxbContext.createUnmarshaller().unmarshal(new ByteArrayInputStream(responseStr.getBytes()));
            System.out.println("flowResponse:"+flowResponse);
			
            flash.put("flowState", flowResponse.getState());
			render(flowResponse);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void step4(){
		String accessToken= flash.get("accessToken");
		String apiEndpoint = flash.get("apiEndpoint");
		flash.put("accessToken",accessToken);
		flash.put("apiEndpoint",apiEndpoint);
		System.out.println("##################  step-4-login ##################");
		System.out.println(String.format(">>>>>>>>sesionId:%s,Api End point:%s,flowState=%s",accessToken,apiEndpoint,flash.get("flowState")));
		try {
			String reqBody = String.format("_action=NEXT&_state=%s",URLEncoder.encode(flash.get("flowState"),"UTF-8"));
			System.out.println("##################  step-4-reqBody ##################");
			System.out.println(reqBody);
			HttpRequest request = new HttpRequest().url(apiEndpoint+"/services/flow/navigateInterview.xml")
																		.method("POST")
																		.content(reqBody.getBytes())
																		.header("Content-type", "application/x-www-form-urlencoded")
																		.header("Authorization",String.format("OAuth %s",accessToken));
																		
			Http httpClient = new Http();
			HttpResponse response = httpClient.send(request);
			System.out.println("##################  step-4-HTTP Status ##################");
			System.out.println(response.getResponseCode());
			InputStream in = response.getStream();
			StringBuilder b = new StringBuilder();
			byte[] buf = new byte[2000];
			int n = 0;
			while((n=in.read(buf))!=-1) {
				b.append(new String(buf,0,n));
			}
			String responseStr = b.toString();
			System.out.println("##################  step-4-responseStr ##################");
			 System.out.println(responseStr);
			JAXBContext jaxbContext = JAXBContext.newInstance((Class) FlowResponse.class);
            FlowResponse flowResponse =  (FlowResponse) jaxbContext.createUnmarshaller().unmarshal(new ByteArrayInputStream(responseStr.getBytes()));
            System.out.println("flowResponse:"+flowResponse);
			
            flash.put("flowState", flowResponse.getState());
			render(flowResponse);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void step5(String FirstName,String Last_Name,String EmailAddress,String ZipCode){
		String accessToken= flash.get("accessToken");
		String apiEndpoint = flash.get("apiEndpoint");
		flash.put("accessToken",accessToken);
		flash.put("apiEndpoint",apiEndpoint);
		System.out.println("##################  step-4-login ##################");
		System.out.println(String.format(">>>>>>>>sesionId:%s,Api End point:%s,flowState=%s",accessToken,apiEndpoint,flash.get("flowState")));
		try {
			String reqBody = String.format("_action=NEXT&_state=%s&FirstName=%s&Last_Name=%s&EmailAddress=%s&ZipCode=%s",
														URLEncoder.encode(flash.get("flowState"),"UTF-8"),
														URLEncoder.encode(FirstName,"UTF-8"),
														URLEncoder.encode(Last_Name,"UTF-8"),
														URLEncoder.encode(EmailAddress,"UTF-8"),
														URLEncoder.encode(ZipCode,"UTF-8"));
			System.out.println("##################  step-4-reqBody ##################");
			System.out.println(reqBody);
			HttpRequest request = new HttpRequest().url(apiEndpoint+"/services/flow/navigateInterview.xml")
																		.method("POST")
																		.content(reqBody.getBytes())
																		.header("Content-type", "application/x-www-form-urlencoded")
																		.header("Authorization",String.format("OAuth %s",accessToken));
																		
			Http httpClient = new Http();
			HttpResponse response = httpClient.send(request);
			System.out.println("##################  step-4-HTTP Status ##################");
			System.out.println(response.getResponseCode());
			InputStream in = response.getStream();
			StringBuilder b = new StringBuilder();
			byte[] buf = new byte[2000];
			int n = 0;
			while((n=in.read(buf))!=-1) {
				b.append(new String(buf,0,n));
			}
			String responseStr = b.toString();
			System.out.println("##################  step-4-responseStr ##################");
			 System.out.println(responseStr);
			JAXBContext jaxbContext = JAXBContext.newInstance((Class) FlowResponse.class);
            FlowResponse flowResponse =  (FlowResponse) jaxbContext.createUnmarshaller().unmarshal(new ByteArrayInputStream(responseStr.getBytes()));
            System.out.println("flowResponse:"+flowResponse);
			
            flash.put("flowState", flowResponse.getState());
			render(flowResponse);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void step6(String FirstName,String Last_Name,String EmailAddress,String ZipCode){
		String accessToken= flash.get("accessToken");
		String apiEndpoint = flash.get("apiEndpoint");
		flash.put("accessToken",accessToken);
		flash.put("apiEndpoint",apiEndpoint);
		System.out.println("##################  step-4-login ##################");
		System.out.println(String.format(">>>>>>>>sesionId:%s,Api End point:%s,flowState=%s",accessToken,apiEndpoint,flash.get("flowState")));
		try {
			String reqBody = String.format("_action=NEXT&_state=%s&FirstName=%s&Last_Name=%s&EmailAddress=%s&ZipCode=%s",
														URLEncoder.encode(flash.get("flowState"),"UTF-8"),
														URLEncoder.encode(FirstName,"UTF-8"),
														URLEncoder.encode(Last_Name,"UTF-8"),
														URLEncoder.encode(EmailAddress,"UTF-8"),
														URLEncoder.encode(ZipCode,"UTF-8"));
			System.out.println("##################  step-4-reqBody ##################");
			System.out.println(reqBody);
			HttpRequest request = new HttpRequest().url(apiEndpoint+"/services/flow/navigateInterview.xml")
																		.method("POST")
																		.content(reqBody.getBytes())
																		.header("Content-type", "application/x-www-form-urlencoded")
																		.header("Authorization",String.format("OAuth %s",accessToken));
																		
			Http httpClient = new Http();
			HttpResponse response = httpClient.send(request);
			System.out.println("##################  step-4-HTTP Status ##################");
			System.out.println(response.getResponseCode());
			InputStream in = response.getStream();
			StringBuilder b = new StringBuilder();
			byte[] buf = new byte[2000];
			int n = 0;
			while((n=in.read(buf))!=-1) {
				b.append(new String(buf,0,n));
			}
			String responseStr = b.toString();
			System.out.println("##################  step-4-responseStr ##################");
			 System.out.println(responseStr);
			JAXBContext jaxbContext = JAXBContext.newInstance((Class) FlowResponse.class);
            FlowResponse flowResponse =  (FlowResponse) jaxbContext.createUnmarshaller().unmarshal(new ByteArrayInputStream(responseStr.getBytes()));
            System.out.println("flowResponse:"+flowResponse);
			
            flash.put("flowState", flowResponse.getState());
			render(flowResponse);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
