package utils;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.*;

public class QRCodeGenerator {

	public static void generateQrCodeToStream(	OutputStream out,String qrCodeString,BarcodeFormat format,
																		int height,int width,String contentType) throws WriterException, IOException
	{
		String data;
		data = new String(qrCodeString);
		// get a byte matrix for the data
		BitMatrix matrix = null;
		com.google.zxing.Writer writer = new MultiFormatWriter();
		try{
			matrix = writer.encode(data, format, width, height);
		}catch(IllegalArgumentException ex){
			throw new WriterException(ex.getMessage());
		}
		MatrixToImageWriter.writeToStream(matrix, contentType, out);
		
	}

	public static void main(String[] args) throws Exception {
		java.io.FileOutputStream out = new java.io.FileOutputStream(
				"qr_png.png");
		
		//generateQrCodeToStream(out,"12345678901",BarcodeFormat.UPC_A,30,100,"PNG");
		generateQrCodeToStream(out,"12345678901",BarcodeFormat.PDF_417,50,100,"PNG");
	}
}