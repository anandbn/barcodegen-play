package controllers;

import play.*;
import play.mvc.*;
import utils.QRCodeGenerator;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.*;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;

import models.*;

public class Application extends Controller {

    public static void index() {
        render();
    }
    public static void qrCode(String i,String contentType,int width,int height){
     	try {
			ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
			if(width==0)
				width=100;
			if(height==0)
				height=100;
			if(contentType==null)
				contentType="PNG";
			QRCodeGenerator.generateCodeToStream(byteOut,i,BarcodeFormat.QR_CODE,height,width,contentType);			
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