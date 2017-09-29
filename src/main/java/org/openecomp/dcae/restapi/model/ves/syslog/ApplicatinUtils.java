package org.openecomp.dcae.restapi.model.ves.syslog;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.List;

import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.protocol.HTTP;
/*import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.att.simulators.common.SConstants;
import com.att.simulators.common.beans.RequestHeader;
import com.att.simulators.common.logger.SimulatorLogger;
*/
import java.util.ArrayList;
import java.util.Iterator;

public class ApplicatinUtils {
	
	public static String readFile(String filePath)
	{
		BufferedReader br = null;

		StringBuilder strBuilder = new StringBuilder();
		try {

			String sCurrentLine;

			br = new BufferedReader(new FileReader(filePath));

			while ((sCurrentLine = br.readLine()) != null) {

				strBuilder.append(sCurrentLine);
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {

			try {

				if (br != null)
					br.close();

			} catch (IOException ex) {

				ex.printStackTrace();
			}
		}

		return strBuilder.toString();
	}
	
	public static void  setRequestHeader(HttpRequestBase httpRequest, String topicName) throws IOException
	{
		httpRequest.addHeader("topicName", topicName);
		httpRequest.addHeader("Content-Type","application/json");		
		//String requestHeaderJson = readFile(SConstants.HEADER_FILE_PATH);
		
		//RequestHeader  requetHeader = JSONUtil.convertJSONtoObject(requestHeaderJson, RequestHeader.class);
		
		
		/*httpRequest.addHeader("Accept", requetHeader.getAccept());
		httpRequest.addHeader("Authorization", requetHeader.getAuthorization());
		httpRequest.addHeader("fromSystem", requetHeader.getFromSystem());
		httpRequest.addHeader("offerName", offerName);
		httpRequest.addHeader("messageId", requetHeader.getMessageId());
		httpRequest.addHeader("timestamp", requetHeader.getTimestamp());
		httpRequest.addHeader("Cache-Control",requetHeader.getCacheControl());
		httpRequest.addHeader("Content-Type",requetHeader.getContentType());*/
		
		//httpRequest.addHeader("responseFileName",responseFileName);				
	}
	
	public static String sendEventToDashBoard(String url, String requestData, String topicName) 
	{
		StringBuffer result = new StringBuffer();
		try{
			HttpClient client = new DefaultHttpClient();//HttpClientBuilder.create().build();
			HttpPost post = new HttpPost(url);
			
			ApplicatinUtils.setRequestHeader(post, topicName);
			//String requestData = ApplicatinUtils.readFile(filePath);
			System.out.println("############################################## Request Data for "+topicName);
			System.out.println(requestData);
			
			StringEntity requestJson =new StringEntity(requestData);
			post.setEntity(requestJson);
			HttpResponse response = client.execute(post);
			
			Header header = response.getFirstHeader("Errorcode");
			if(header != null)
			{
				Header errordescext = response.getFirstHeader("Errordescext");
				Header errordesc = response.getFirstHeader("Errordesc");
				System.out.println("");
				System.out.println("****************** ERROR In Request Header ******************************");
				System.out.println("");
				System.out.println("Errorcode="+header.getValue());
				System.out.println("Errordescext="+errordescext.getValue());
				System.out.println("errordesc="+errordesc.getValue());
			}
			else
			{
				BufferedReader rd = new BufferedReader(
				        new InputStreamReader(response.getEntity().getContent()));
			
				
				String line = "";
				while ((line = rd.readLine()) != null) 
				{
					result.append(line);
				}
				
				System.out.println("");
				System.out.println("");
				System.out.println("############################# Response from server for "+topicName);
				System.out.println(result.toString());
			}
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
		return result.toString();
	}
	
	public static String executeGetRequest(String url, String offerName) throws IOException
	{
		StringBuffer result = new StringBuffer();
		HttpClient client = HttpClientBuilder.create().build();
		
		HttpGet get = new HttpGet(url);
		ApplicatinUtils.setRequestHeader(get, offerName);
		System.out.println("***************************** Request *********************************");
		System.out.println(url);
		
		HttpResponse response = client.execute(get);
		
		Header header = response.getFirstHeader("Errorcode");
		if(header != null)
		{
			Header errordescext = response.getFirstHeader("Errordescext");
			Header errordesc = response.getFirstHeader("Errordesc");
			System.out.println("");
			System.out.println("****************** ERROR In Request Header ******************************");
			System.out.println("");
			System.out.println("Errorcode="+header.getValue());
			System.out.println("Errordescext="+errordescext.getValue());
			System.out.println("errordesc="+errordesc.getValue());
		}
		else
		{
			BufferedReader rd = new BufferedReader(
			        new InputStreamReader(response.getEntity().getContent()));
		
			
			String line = "";
			while ((line = rd.readLine()) != null) {
				result.append(line);
			}
			
			System.out.println("");
			System.out.println("");
			System.out.println("############################# Response from server ##############################");
			System.out.println(result.toString());
		}
		
		return result.toString();
	}
	
	public static String sendRequest(String url, String filePath,String actionName) throws IOException
	{
		StringBuffer result = new StringBuffer();
		
		HttpClient client = HttpClientBuilder.create().build();
		HttpPost post = new HttpPost(url);
		
		
		post.setHeader("soapaction", actionName);
		post.setHeader("Content-Type", "text/xml; charset=utf-8");
		//ApplicatinUtils.setRequestHeader(post, "MIS_EXPRESS");
	
		String requestData = ApplicatinUtils.readFile(filePath);
		
		System.out.println(requestData);
		 
		StringEntity requestJson =new StringEntity(requestData , StandardCharsets.UTF_8);
		post.setEntity(requestJson);
		HttpResponse response = client.execute(post);
		
		Header header = response.getFirstHeader("Errorcode");
		if(header != null)
		{
			Header errordescext = response.getFirstHeader("Errordescext");
			Header errordesc = response.getFirstHeader("Errordesc");
			System.out.println("");
			System.out.println("****************** ERROR In Request Header ******************************");
			System.out.println("");
			System.out.println("Errorcode="+header.getValue());
			System.out.println("Errordescext="+errordescext.getValue());
			System.out.println("errordesc="+errordesc.getValue());
		}
		else
		{
			BufferedReader rd = new BufferedReader(
			        new InputStreamReader(response.getEntity().getContent()));
		
			
			String line = "";
			while ((line = rd.readLine()) != null) {
				result.append(line);
			}
			
			System.out.println("");
			System.out.println("");
			System.out.println("############################# Response from server for ");
			System.out.println(result.toString());
		}
		return result.toString();
	}	
}
