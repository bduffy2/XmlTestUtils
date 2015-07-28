package com.stillwaterinsurance.xmltest.service;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringWriter;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.soap.MessageFactory;
import javax.xml.soap.SOAPBody;
import javax.xml.soap.SOAPEnvelope;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPMessage;
import javax.xml.soap.SOAPPart;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.apache.commons.io.IOUtils;
import org.apache.http.HttpHeaders;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;

public class EngineService {

	//TODO - add selection in UI for engine url
	private static final String ENGINE_URI = "http://localhost:8080/WebServiceEngine/services/WSEngine/invoke";
//	private static final String ENGINE_URI = "http://omappqua:8080/WebServiceEngine/services/WSEngine/invoke";
//	private static final String ENGINE_URI = "http://omappint:8080/WebServiceEngine/services/WSEngine/invoke";
	
	public static final String callWSEngine(final String acordRequestXml) 
			throws IOException, SOAPException, SAXException, ParserConfigurationException, TransformerException {
		String soapMessage = null;
		String responseXml = null;
		
		//add soap envelope/header
		soapMessage = addSoapWrapper(acordRequestXml);
		//call Engine posting xml
		final String responseBody = callEngine(soapMessage);
		//remove the soap envelope
		responseXml = removeSoapWrapper(responseBody);
			
		return responseXml;
	}
	
	private static String callEngine(final String soapMessage) throws IOException {
		String responseText = "";

		final CloseableHttpClient client = HttpClientBuilder.create().build();
		final HttpPost postRequest = new HttpPost(ENGINE_URI);

		try {
			postRequest.setEntity(new StringEntity(soapMessage));
			postRequest.addHeader(HttpHeaders.CONTENT_TYPE, "text/xml");
			final HttpResponse response = client.execute(postRequest);
			responseText = getResponseText(response);
		} 
		finally {
			client.close();
		}
		
		return responseText;
	}
	
	private static String getResponseText(final HttpResponse response) throws IOException {

		final StringBuffer sb = new StringBuffer();
		BufferedReader br = null;
		InputStreamReader isr = null;
		try {
			isr = new InputStreamReader(response.getEntity().getContent());
			br = new BufferedReader(isr);

			String output;
			if (br != null) {
				while ((output = br.readLine()) != null) {
					sb.append(output);
				}
			}
		}
		finally {
			if (br != null) {
				br.close();
			}
			if(isr != null) {
				isr.close();
			}
		}
		return sb.toString();

	}

	private static final String addSoapWrapper(final String xml) 
			throws SOAPException, SAXException, IOException, ParserConfigurationException {

		ByteArrayOutputStream baos = null;
		
		final MessageFactory mf = MessageFactory.newInstance();

		final InputStream xmlStream = IOUtils.toInputStream(xml);

		SOAPMessage soapMessage = null;

		// create the message...
		soapMessage = mf.createMessage();

		// get the SOAP envelope...
		final SOAPEnvelope envelope = soapMessage.getSOAPPart()
				.getEnvelope();
		// remove the empty header...

		// get the SOAP body...
		final SOAPBody soapBody = envelope.getBody();
		// insert the message into the soap envelope...
		final DocumentBuilderFactory db = DocumentBuilderFactory
				.newInstance();
		db.setNamespaceAware(true);
		final Element node = db.newDocumentBuilder().parse(xmlStream)
				.getDocumentElement();
		soapBody.appendChild(soapBody.getOwnerDocument().importNode(node,
				true));

		// save the changes...
		soapMessage.saveChanges();
		baos = new ByteArrayOutputStream();
		soapMessage.writeTo(baos);

		xmlStream.close();

		return baos.toString();
	}
	
	private static final String removeSoapWrapper(final String soap) 
			throws IOException, SOAPException, TransformerException {

		String strBody = null;
		
		final InputStream is = new ByteArrayInputStream(soap.getBytes());
		final SOAPMessage request = MessageFactory.newInstance().createMessage(null, is);
		request.saveChanges();
		is.close();

		final SOAPPart part = request.getSOAPPart();
		final SOAPEnvelope env = part.getEnvelope();
		final SOAPBody body = env.getBody();

		final Document doc = body.extractContentAsDocument();

		final Source source = new DOMSource(doc);
		final StringWriter stringWriter = new StringWriter();
		final Result result = new StreamResult(stringWriter);
		final TransformerFactory factory = TransformerFactory.newInstance();
		final Transformer transformer = factory.newTransformer();
		transformer.transform(source, result);

		strBody = stringWriter.getBuffer().toString();
		
		return strBody;

	}

}
