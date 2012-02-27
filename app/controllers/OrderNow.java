package controllers;

import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import org.apache.http.HttpEntity;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.ParseException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;




import play.cache.Cache;
import play.mvc.Controller;
import xml.FlowResponse;
import models.UserSessionInfo;

public class OrderNow extends Controller {
	private static String LOGIN_XML=	"<env:Envelope xmlns:xsd='http://www.w3.org/2001/XMLSchema' xmlns:xsi='http://www.w3.org/2001/XMLSchema-instance' " +
	"xmlns:env='http://schemas.xmlsoap.org/soap/envelope/'><env:Body><n1:login xmlns:n1='urn:partner.soap.sforce.com'>" +
	"<n1:username>%s</n1:username><n1:password>%s</n1:password>" +
	"</n1:login></env:Body></env:Envelope> ";

	public static void start(){
		//Login and set Access Token and API Endpoint
		login();
		
		System.out.println("##################  step-1-login ##################");
		System.out.println(String.format(">>>>>>>>sesionId:%s,Api End point:%s",accessToken(),apiEndpoint()));
		System.out.println("##################  step-1-login ##################");
		System.out.println(String.format(">>>>>>>>sesionId:%s,Api End point:%s",accessToken(),apiEndpoint()));
		
		//Start the "flow"
		HttpClient httpclient = new DefaultHttpClient();
		HttpPost httpPost;
		List<NameValuePair> formparams = new ArrayList<NameValuePair>();
		StringEntity entity=null;

		try {
			
			httpPost = new HttpPost(apiEndpoint()+"/services/flow/startInterview/iPhone_iPad_Purchase");
			formparams = new ArrayList<NameValuePair>();
			formparams.add(new BasicNameValuePair("vInput1", "fromRest"));
			System.out.println("##################  step-1-requestBody ##################");
			System.out.println(formparams.toString());
			entity = new UrlEncodedFormEntity(formparams, "UTF-8");
			httpPost.addHeader("Authorization","OAuth "+accessToken());
			HttpResponse response = httpclient.execute(httpPost);
			HttpEntity respEntity = response.getEntity();
			String responseStr = null;
			if (respEntity != null) {
			    responseStr = getResponseBody(respEntity);
	    		System.out.println("##################  step-1:responseStr ##################");
	    		System.out.println(responseStr);
			    JAXBContext jaxbContext = JAXBContext.newInstance((Class) FlowResponse.class);
	            FlowResponse flowResponse =  (FlowResponse) jaxbContext.createUnmarshaller().unmarshal(new ByteArrayInputStream(responseStr.getBytes()));
	    		System.out.println("##################  step-1:flowResponse ##################");
	            System.out.println(flowResponse);
	            setFlowState(flowResponse.getState());
	            UserSessionInfo usrInfo = (UserSessionInfo)Cache.get(session.getId());
	            render(flowResponse,usrInfo);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	public static void navigate(){
		
		
		System.out.println("##################  login params ##################");
		System.out.println(String.format(">>>>>>>>sesionId:%s,Api End point:%s",accessToken(),apiEndpoint()));
		
		try {
			HttpClient httpclient = new DefaultHttpClient();
			HttpPost httpPost = new HttpPost(apiEndpoint()+"/services/flow/navigateInterview.xml");
			List<NameValuePair> formparams = new ArrayList<NameValuePair>();
			formparams.add(new BasicNameValuePair("_action", "NEXT"));
			formparams.add(new BasicNameValuePair("_state", flowState()));
			for(java.util.Map.Entry<String,String> param: request.params.allSimple().entrySet()){
				System.out.println(String.format(">>>>>>>>>>>>>>> param key=%s,value=%s",param.getKey(),param.getValue()));
				if(!param.getKey().equalsIgnoreCase("controller") && !param.getKey().equalsIgnoreCase("body")&& !param.getKey().equalsIgnoreCase("action")){
					formparams.add(new BasicNameValuePair(param.getKey(),param.getValue()));
				}
			}
			
			System.out.println("##################  requestBody ##################");
			System.out.println(formparams.toString());
			UrlEncodedFormEntity entity = new UrlEncodedFormEntity(formparams, "UTF-8");
			httpPost.setEntity(entity);
			httpPost.addHeader("Authorization","OAuth "+accessToken());
			org.apache.http.HttpResponse response = httpclient.execute(httpPost);
			HttpEntity respEntity = response.getEntity();
			String responseStr = null;
			if (respEntity != null) {
			    responseStr = getResponseBody(respEntity);
	    		System.out.println("################## responseStr ##################");
	    		System.out.println(responseStr);
			    JAXBContext jaxbContext = JAXBContext.newInstance((Class) FlowResponse.class);
	            FlowResponse flowResponse =  (FlowResponse) jaxbContext.createUnmarshaller().unmarshal(new ByteArrayInputStream(responseStr.getBytes()));
	    		System.out.println("##################  flowResponse ##################");
	            System.out.println(flowResponse);
	            setFlowState(flowResponse.getState());
	            UserSessionInfo usrInfo = (UserSessionInfo)Cache.get(session.getId());
	            renderTemplate("OrderNow/start.html", flowResponse,usrInfo);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			renderText("Error occurred navigating flow");
		}

		
	}
	
	
	/*
	public static void step2(String selectedProduct){
		System.out.println("##################  step-2-login ##################");
		System.out.println(String.format(">>>>>>>>sesionId:%s,Api End point:%s,flowState=%s",accessToken(),apiEndpoint(),flowState()));
		try {
			HttpClient httpclient = new DefaultHttpClient();
			HttpPost httpPost = new HttpPost(apiEndpoint()+"/services/flow/navigateInterview.xml");
			List<NameValuePair> formparams = new ArrayList<NameValuePair>();
			formparams.add(new BasicNameValuePair("_action", "NEXT"));
			formparams.add(new BasicNameValuePair("_state", flowState()));
			formparams.add(new BasicNameValuePair(selectedProduct+".selected","true"));
			System.out.println("##################  step-2-requestBody ##################");
			System.out.println(formparams.toString());
			UrlEncodedFormEntity entity = new UrlEncodedFormEntity(formparams, "UTF-8");
			httpPost.setEntity(entity);
			httpPost.addHeader("Authorization","OAuth "+accessToken());
			org.apache.http.HttpResponse response = httpclient.execute(httpPost);
			HttpEntity respEntity = response.getEntity();
			String responseStr=null;
			if (entity != null) {
			    long len = entity.getContentLength();
			    if (len != -1 && len < 2048) {
			    	responseStr=EntityUtils.toString(respEntity);
			    } else {
			    	InputStream in = respEntity.getContent();
			    	StringBuilder b = new StringBuilder();
					byte[] buf = new byte[2000];
					int n = 0;
					while((n=in.read(buf))!=-1) {
						b.append(new String(buf,0,n));
					}
					responseStr = b.toString();
			    }
			System.out.println("##################  step-2-responseStr ##################");
			 System.out.println(responseStr);
			JAXBContext jaxbContext = JAXBContext.newInstance((Class) FlowResponse.class);
            FlowResponse flowResponse =  (FlowResponse) jaxbContext.createUnmarshaller().unmarshal(new ByteArrayInputStream(responseStr.getBytes()));
            System.out.println("flowResponse:"+flowResponse);
			
            setFlowState(flowResponse.getState());
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
		System.out.println(String.format(">>>>>>>>sesionId:%s,Api End point:%s,flowState=%s",accessToken,apiEndpoint,getFlowState()));
		try {
			String reqBody = String.format("_action=NEXT&_state=%s&%s.selected=true",URLEncoder.encode(getFlowState(),"UTF-8"),selectedProduct);
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
		System.out.println(String.format(">>>>>>>>sesionId:%s,Api End point:%s,flowState=%s",accessToken,apiEndpoint,getFlowState()));
		try {
			String reqBody = String.format("_action=NEXT&_state=%s",URLEncoder.encode(getFlowState(),"UTF-8"));
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
		System.out.println(String.format(">>>>>>>>sesionId:%s,Api End point:%s,flowState=%s",accessToken,apiEndpoint,getFlowState()));
		try {
			String reqBody = String.format("_action=NEXT&_state=%s&FirstName=%s&Last_Name=%s&EmailAddress=%s&ZipCode=%s",
														URLEncoder.encode(getFlowState(),"UTF-8"),
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
		System.out.println(String.format(">>>>>>>>sesionId:%s,Api End point:%s,flowState=%s",accessToken,apiEndpoint,getFlowState()));
		try {
			String reqBody = String.format("_action=NEXT&_state=%s&FirstName=%s&Last_Name=%s&EmailAddress=%s&ZipCode=%s",
														URLEncoder.encode(getFlowState(),"UTF-8"),
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
	*/
	private static String getResponseBody(HttpEntity respEntity) throws ParseException, IOException{
		long len = respEntity.getContentLength();
	    if (len != -1 && len < 2048) {
	    	return EntityUtils.toString(respEntity);
	    } else {
	    	InputStream in = respEntity.getContent();
	    	StringBuilder b = new StringBuilder();
			byte[] buf = new byte[2000];
			int n = 0;
			while((n=in.read(buf))!=-1) {
				b.append(new String(buf,0,n));
			}
			return b.toString();
	    }
	}
	private static void login(){
		HttpClient httpclient = new DefaultHttpClient();
		HttpPost httpPost = new HttpPost("https://login.salesforce.com/services/Soap/u/24.0");
		StringEntity entity=null;
		String accessToken, apiEndpoint;
		try {
			entity = new StringEntity(String.format(LOGIN_XML,System.getenv("SFDC_USERNAME"),System.getenv("SFDC_PASSWORD")), "UTF-8");

			httpPost.setEntity(entity);
			httpPost.addHeader("SOAPAction", "login");
			httpPost.addHeader("Content-type", "text/xml");
			HttpResponse response = httpclient.execute(httpPost);
			HttpEntity respEntity = response.getEntity();
			String responseStr = null;
			if (entity != null) {
				long len = entity.getContentLength();
				if (len != -1 && len < 2048) {
					responseStr = EntityUtils.toString(respEntity);
					System.out.println("Response Str:" + responseStr);
				} else {
					InputStream in = respEntity.getContent();
					StringBuilder b = new StringBuilder();
					byte[] buf = new byte[2000];
					int n = 0;
					while ((n = in.read(buf)) != -1) {
						b.append(new String(buf, 0, n));
					}
					responseStr = b.toString();
				}
				accessToken = responseStr.replaceAll("^.*<sessionId>(.*)</sessionId>.*$", "$1").trim();
				apiEndpoint = "https://"+ responseStr.replaceAll("^.*<serverUrl>.*https://([^/]*)/.*</serverUrl>.*$","$1").trim();
				UserSessionInfo usrSession = (UserSessionInfo)Cache.get(session.getId());
				if(usrSession==null){
					usrSession = new UserSessionInfo(accessToken,apiEndpoint);
					Cache.add(session.getId(),usrSession );
				}else{
					usrSession = new UserSessionInfo(accessToken,apiEndpoint);
					Cache.replace(session.getId(),usrSession );
				}
				
				
			}
		} catch (UnsupportedEncodingException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	private static void setFlowState(String state){
        //Set the flowState to cache
		UserSessionInfo usrInfo = ((UserSessionInfo) Cache.get(session.getId()));
        usrInfo.flowState=state;
        usrInfo.incrementStep();
        Cache.replace(session.getId(),usrInfo);
	}
	private static String flowState(){
		return ((UserSessionInfo)Cache.get(session.getId())).flowState;
	}
	private static String accessToken(){
		return ((UserSessionInfo)Cache.get(session.getId())).accessToken;
	}
	private static String apiEndpoint(){
		return ((UserSessionInfo)Cache.get(session.getId())).apiEndpoint;
	}
	
}
