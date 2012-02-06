package controllers;

import play.*;
import play.mvc.*;
import utils.QRCodeGenerator;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.*;

import com.google.zxing.WriterException;

import models.*;

public class Application extends Controller {

    public static void index() {
        render();
    }
    public static void qrCode(String qrCodeText){
     	byte[] qrCodeBytes=null;
		try {
			qrCodeBytes = QRCodeGenerator.generateQrCode(qrCodeText);
	     	renderBinary(new ByteArrayInputStream(qrCodeBytes));
		} catch (WriterException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		renderText("Error rendering QRCode");
    }

}