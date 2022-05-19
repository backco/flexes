package com.co.back.flexes;

import org.deckfour.xes.factory.XFactory;
import org.deckfour.xes.factory.XFactoryBufferedImpl;
import org.deckfour.xes.in.XesXmlGZIPParser;
import org.deckfour.xes.in.XesXmlParser;
import org.deckfour.xes.model.XAttribute;
import org.deckfour.xes.model.XEvent;
import org.deckfour.xes.model.XLog;
import org.deckfour.xes.model.XTrace;

import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.time.ZonedDateTime;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.NavigableMap;
import java.util.Set;

public class FleXES {

    public static final String	TIMESTAMP	= "time:timestamp";

    public static Map<String, Set<String>> eventAttributes (String path) throws IOException {

        XLog log = loadXES(path);

        return eventAttributes(log);
    }

    public static Map<String, Set<String>> eventAttributes (XLog log) {

	Map<String, Set<String>> attributes = new HashMap<>();

	for (XTrace t : log) {
	    for ( XEvent e : t) {
	        for ( Map.Entry<String, XAttribute> xa : e.getAttributes().entrySet()) {
		    XAttribute att = xa.getValue();
	            attributes.computeIfAbsent(att.getKey(), v -> new HashSet<>()).add(att.toString());
		}
	    }
	}

	return attributes;
    }

    public static Map<String, Set<String>> traceAttributes (String path) throws IOException {

	XLog log = loadXES(path);

	return traceAttributes(log);
    }

    public static Map<String, Set<String>> traceAttributes (XLog log) {

	Map<String, Set<String>> attributes = new HashMap<>();

	for ( XTrace t : log ) {
	    for ( Map.Entry<String, XAttribute> xa : t.getAttributes().entrySet() ) {
	        //XAttribute att = xa.getValue();
		attributes.computeIfAbsent(xa.getKey(), v -> new HashSet<>()).add(xa.getValue().toString());
	    }
	}

	return attributes;
    }

    public static boolean canParse(String path) throws IOException {

	final File file = read(path);
	final XesXmlParser xesXmlParser = new XesXmlParser();
	final XesXmlGZIPParser xesXmlGZIPParser = new XesXmlGZIPParser();

	if (xesXmlParser.canParse(file)) {

	    return true;

	} else if (xesXmlGZIPParser.canParse(file)) {

	    return true;

	} else {

	    return false;
	}

    }

    private static List<XLog> load(String path, PrintStream ps) throws IOException {

	final PrintStream psOrig = System.out;
	System.setOut(ps);
	System.setErr(ps);

	final File             file             = read(path);
	List<XLog>             xLogs            = null;
	final XesXmlParser     xesXmlParser     = new XesXmlParser();
	final XesXmlGZIPParser xesXmlGZIPParser = new XesXmlGZIPParser();

	if (xesXmlParser.canParse(file)) {

	    xLogs = parse(file, xesXmlParser);

	} else if (xesXmlGZIPParser.canParse(file)) {

	    xLogs = parse(file, xesXmlGZIPParser);

	} else {
	    throw new IOException("file format cannot be parsed");
	}

	System.setOut(psOrig);
	System.setErr(psOrig);

	return xLogs;
    }

    public static XLog loadXES(String logPath) throws IOException {

	return loadXES(logPath, false, System.out);
    }

    public static XLog loadXES(String logPath, boolean sortByTimeStamp, PrintStream ps) throws IOException {

	final File f = new File(logPath);

	if (f.isFile()) {

	    if (logPath.toLowerCase().endsWith("xes") || logPath.toLowerCase().endsWith("gz")) {

		if (canParse(logPath)) {

		    final XLog log = load(logPath, ps).get(0);

		    if (sortByTimeStamp) {
			sortByTimeStamp(log);
		    }

		    return log;

		} else {
		    throw new IOException("cannot parse file: " + logPath);
		}

	    } else {
		throw new IOException("wrong extension: " + logPath);
	    }

	} else {
	    throw new IOException("not a file: " + logPath);
	}
    }

    private static List<XLog> parse(File file, XesXmlGZIPParser parser) throws IOException {

	List<XLog> xLogs = null;

	try {
	    xLogs = parser.parse(file);
	} catch (final Exception e) {
	    e.printStackTrace();
	    throw new IOException("problem parsing file (GZIP): " + file.getAbsolutePath());
	}

	return xLogs;
    }

    private static List<XLog> parse(File file, XesXmlParser parser) throws IOException {

	List<XLog> xLogs = null;

	try {
	    xLogs = parser.parse(file);

	} catch (final Exception e) {
	    throw new IOException("problem parsing file: " + file.getAbsolutePath());
	}

	return xLogs;
    }

    private static File read(String path) throws IOException {

	File file = null;

	try {
	    file = new File(path);
	} catch (final NullPointerException e) {
	    throw new IOException("problem loading file: " + path);
	}

	return file;
    }

    public static void sortByTimeStamp(XLog l) {

	for (final XTrace t : l) {
	    sortByTimeStamp(t);
	}

	l.sort((x, y) -> ZonedDateTime.parse(x.get(0).getAttributes().get(TIMESTAMP).toString()).compareTo(ZonedDateTime.parse(y.get(0).getAttributes().get(TIMESTAMP).toString())));
    }

    public static void sortByTimeStamp(XTrace t) {

	t.sort((x, y) -> ZonedDateTime.parse(x.getAttributes().get(TIMESTAMP).toString()).compareTo(ZonedDateTime.parse(y.getAttributes().get(TIMESTAMP).toString())));
    }

    public static XLog merge( NavigableMap<String, XLog> logs) {

    	XFactory factory = new XFactoryBufferedImpl();
    	XLog result = factory.createLog();

    	for ( Map.Entry<String, XLog> e : logs.entrySet()) {

	    String filename = e.getKey();
	    XLog   log      = e.getValue();

    	    for (XTrace t : log) {
    	        XAttribute a = factory.createAttributeLiteral("sublog", filename, null);
    	        t.getAttributes().put("sublog", a);
    	        result.add(t);
	    }
	}

    	return result;
    }
}