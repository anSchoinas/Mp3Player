import nu.xom.*;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import java.io.*;
import java.net.*;

class getInfo {
	int i;
	//String message;
	public String[] songSearch(String song) throws MalformedURLException{
		String[] a = new String[4];
		String song2 =song.replace(" ","+");
		String songsearch ="http://ws.audioscrobbler.com/2.0/?method=track.search&track="+song2+"&api_key=14aac673f4c883b1db79453a3a1e4ae5";
		System.out.println(songsearch);
		URL page = new URL(songsearch);
		StringBuffer text = new StringBuffer();
		File logfile=new File("Logs/"+song2+".xml");
		
		if(!logfile.exists() || logfile.isDirectory()){
		System.out.println("Log file for track "+song+" does not exist. Connecting to last.fm to retrieve info...");
		try {
			logfile.createNewFile();
			logfile.mkdirs();
			HttpURLConnection conn = (HttpURLConnection)
				page.openConnection();
			conn.connect();
			InputStreamReader in = new InputStreamReader((InputStream)conn.getContent());
			BufferedReader buff = new BufferedReader(in);
			FileOutputStream xml= new FileOutputStream(("Logs/"+song2+".xml"));
			int read = 0;
			int line=1024;
			char[] inputarray= new char[1024];
			 byte[] bytes = new byte[inputarray.length];
			 while ((line=buff.read(inputarray,0,line))!=-1){
			 String message = String.valueOf(inputarray);
			bytes=message.getBytes("UTF-8");
			xml.write(bytes,0,line);
			}			
			xml.close();
		} catch (IOException ioe){
			System.out.println("IO Error:"+ioe.getMessage());
			a[0]="Problem retrieving";
			a[1]="Problem retrieving";
			a[2]="img/dummy_album.png";
			a[3]="null";
		}
		}

			try{
				Builder builder = new Builder();
				File xmlFile = new File(("Logs/"+song2+".xml"));
				Document doc = builder.build(xmlFile);
				
				Element root = doc.getRootElement();
				Element results =root.getFirstChildElement("results");
				Element trackmatches = results.getFirstChildElement("trackmatches");
				Element track = trackmatches.getFirstChildElement("track");
				//Element mbid =track.getFirstChildElement("mbid");
				Element name =track.getFirstChildElement("name");
				Element artist =track.getFirstChildElement("artist");
				//Text mbidout = (Text)mbid.getChild(0);
				Text nameout = (Text)name.getChild(0);
				Text artistout = (Text)artist.getChild(0);
				System.out.println("1 good so far");
				a=trackInfo(nameout.getValue(),artistout.getValue());
				}catch (Exception pe){
					System.out.println("error1"+pe);
					a[0]="Problem retrieving";
					a[1]="Problem retrieving";
					a[2]="img/dummy_album.png";
					a[3]="null";
				}
			return a;	
		
	
	}
	private String[] trackInfo(String songname, String artname) throws MalformedURLException {
		String[] b = new String[4];
		String trackinfolink ="http://ws.audioscrobbler.com/2.0/?method=track.getInfo&api_key=14aac673f4c883b1db79453a3a1e4ae5&artist="+artname.replace(" ","+")+"&track="+songname.replace(" ","+");
		System.out.println(trackinfolink);
		URL page = new URL(trackinfolink);
		StringBuffer text = new StringBuffer();
		File tracklogfile=new File("Logs/Songs/"+songname+".xml");
		if(!tracklogfile.exists() || tracklogfile.isDirectory()){
		System.out.println("Log file for song"+songname+" does not exist. Connecting to last.fm to retrieve info...");
		try {
			tracklogfile.createNewFile();
			tracklogfile.mkdirs();
			HttpURLConnection conn = (HttpURLConnection)
				page.openConnection();
			conn.connect();
			InputStreamReader in = new InputStreamReader((InputStream)conn.getContent());
			BufferedReader buff = new BufferedReader(in);
			FileOutputStream artxml= new FileOutputStream(new File("Logs/Songs/"+songname+".xml"));
			int read = 0;
			
			int line=1024;
			char[] inputarray= new char[1024];
			 while ((line=buff.read(inputarray,0,line))!=-1){;
				byte[] bytes = new byte[inputarray.length];
				for (i = 0; i < inputarray.length; i++) {
					bytes[i] = (byte) inputarray[i];
				}
				artxml.write(bytes,0,line);
			}
			artxml.close();
		} catch (IOException ioe){
			System.out.println("IO Error:"+ioe.getMessage());
			b[0]="Problem retrieving";
			b[1]="Problem retrieving";
			b[2]="img/dummy_album.png";
			b[3]="null";
		}
		}
		try{
			Builder builder = new Builder();
			File xmlFile = new File("Logs/Songs/"+songname+".xml");
			Document doc = builder.build(xmlFile);
			Element root = doc.getRootElement();
			Element track =root.getFirstChildElement("track");
			Element album = track.getFirstChildElement("album");
			Element artist = album.getFirstChildElement("artist");
			Element title = album.getFirstChildElement("title");
			Element image = album.getFirstChildElement("image");
			Element artist2 = track.getFirstChildElement("artist");
			Element mbid = artist2.getFirstChildElement("mbid");
			Text artmbid = (Text)mbid.getChild(0);
			Text artistq=(Text)artist.getChild(0);
			Text titleq = (Text) title.getChild(0);
			Text imgurl= (Text) image.getChild(0);
			b[0]=artistq.getValue();
			b[1]=titleq.getValue();
			b[2]=imgurl.getValue();
			b[3]=artmbid.getValue();
		}catch (Exception pe){
			b[0]="Problem retrieving";
			b[1]="Problem retrieving";
			b[2]="img/dummy_album.png";
			b[3]="null";
			System.out.println("error2"+pe);
				}
		return b;
	}

	public String artistBio(String mbid) throws MalformedURLException{
		String biosearch ="http://ws.audioscrobbler.com/2.0/?method=artist.getinfo&mbid="+mbid+"&api_key=14aac673f4c883b1db79453a3a1e4ae5";
		URL page = new URL(biosearch);
		StringBuffer text = new StringBuffer();
		File artlogfile=new File("Logs/Artists/"+mbid+".xml");
		if(!artlogfile.exists() || artlogfile.isDirectory()){
		System.out.println("Log file for artist with mbid "+mbid+" does not exist. Connecting to last.fm to retrieve info...");
		try {
			artlogfile.createNewFile();
			artlogfile.mkdirs();
			HttpURLConnection conn = (HttpURLConnection)
				page.openConnection();
			conn.connect();
			InputStreamReader in = new InputStreamReader((InputStream)conn.getContent());
			BufferedReader buff = new BufferedReader(in);
			FileOutputStream xml= new FileOutputStream(new File("Logs/Artists/"+mbid+".xml"));
			int read = 0;
			
			int line=1024;
			char[] inputarray= new char[1024];
			 while ((line=buff.read(inputarray,0,line))!=-1){;
				byte[] bytes = new byte[inputarray.length];
				for (i = 0; i < inputarray.length; i++) {
					bytes[i] = (byte) inputarray[i];
				}
				xml.write(bytes,0,line);
			}
			xml.close();
		} catch (IOException ioe){
			System.out.println("IO Error:"+ioe.getMessage());
		}
		}

		try{
			Builder builder = new Builder();
			File xmlFile = new File("Logs/Artists/"+mbid+".xml");
			Document doc = builder.build(xmlFile);
			
			Element root=doc.getRootElement();
			Element artist = root.getFirstChildElement("artist");
			Element bio = artist.getFirstChildElement("bio");
			Element summary = bio.getFirstChildElement("summary");
			Text sum = (Text)summary.getChild(0);
			return (sum.getValue());
		}catch(Exception e){
			System.out.println(e);
			return ("Problem retrieving file....");
		}
				
	}	
}
