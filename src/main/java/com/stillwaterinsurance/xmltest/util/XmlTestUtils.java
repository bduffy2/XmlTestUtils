package com.stillwaterinsurance.xmltest.util;

import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.Writer;

import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

public class XmlTestUtils {

	public static final String formatXML(final String unformattedXml) throws TransformerException, IOException {

		final TransformerFactory factory = TransformerFactory.newInstance();
		final Transformer transformer = factory.newTransformer();

		transformer.setOutputProperty(OutputKeys.INDENT, "yes");
		transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");

		final StringReader sr = new StringReader(unformattedXml);
		final Writer out = new StringWriter();

		transformer.transform(new StreamSource(sr), new StreamResult(out));

		out.close();
		sr.close();

		return out.toString();
	}

}
