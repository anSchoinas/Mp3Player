import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.FactoryConfigurationError;
import javax.xml.parsers.ParserConfigurationException;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.w3c.dom.Document;
import org.w3c.dom.DOMException;
import java.util.Scanner;

// For write operation
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamSource;
import javax.xml.transform.stream.StreamResult;
import java.io.*;
import org.apache.commons.lang3.*;
import org.apache.commons.io.*;


class getHtml{
//static Document document;
//String content = new String();

	public String asda(String artist,String summary) {
	String output = new String();
	String filename="Logs/Bios/"+artist+".html";
	File artistbio = new File(filename);
	try{
	if (!artistbio.exists()){
		artistbio.createNewFile();
		artistbio.mkdirs();
		output = StringEscapeUtils.escapeHtml4(summary);
		FileUtils.writeStringToFile(artistbio, output);	
	}
	else{
	output = FileUtils.readFileToString(artistbio);
	}
	return output;
	}catch (Exception e){
	System.out.println("Darn me!"+e);
	return "null";
	}
	}
}
