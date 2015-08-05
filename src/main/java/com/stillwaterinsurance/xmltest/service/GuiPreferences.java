package com.stillwaterinsurance.xmltest.service;

import java.util.prefs.Preferences;

public class GuiPreferences {
	
	private static final String ENGINE_URI = "engineuri";
	private static final String XML_START_PATH = "xmlstartpath";
	private static final String DEFAULT_URI = "http://omappqua:8080/WebServiceEngine/services/WSEngine/invoke";
	
	private static final Preferences prefs = Preferences.userNodeForPackage(GuiPreferences.class);
	
	public static void setEngineUri(String uri) {
		prefs.put(ENGINE_URI, uri);
	}
	
	public static String getEngineUri() {
		return prefs.get(ENGINE_URI, DEFAULT_URI);
	}
	
	public static void setXmlStartPath(String startPath) {
		prefs.put(XML_START_PATH, startPath);
	}
	
	public static String getXmlStartPath() {
		return prefs.get(XML_START_PATH, System.getProperty("user.home"));
	}
}
