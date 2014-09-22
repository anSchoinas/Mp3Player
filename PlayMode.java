import java.io.*;
import javazoom.jl.player.advanced.*;
import javazoom.jl.decoder.*;
import javazoom.jl.player.Player;  

//Class for Mp3 Playback 

class PlayMode extends  PlaybackListener{  
	private String filename;  
    public AdvancedPlayer player; 
	private FileInputStream fis;
	private Thread runner=null;
 	private int i = 0;
	public PlaybackListener pbl;
	private BufferedInputStream bis;
	
	public PlayMode(FileInputStream fis,String filename) throws JavaLayerException{		
		this.filename = filename;
		this.fis=fis;
	}  
  
	public void play (final int playbackStartPoint, final int playbacStopPoint){
        	final Runnable r1 = new Runnable(){
			public void run(){
				while(!Thread.currentThread().isInterrupted()){
					try {  
				            bis = new BufferedInputStream(fis);  
				            player = new AdvancedPlayer(bis);
							player.setPlayBackListener(pbl = new PlaybackListener() {
							@Override
							public void playbackStarted(PlaybackEvent playbackEvent){
								System.out.println("playbackStarted()");
							}
							public void playbackFinished(PlaybackEvent event) {
								System.out.println("Playback finished");
								runner.interrupt();
								return;
    						}
							});
				            player.play(playbackStartPoint,playbacStopPoint);  
     				  	}  
				        catch (Exception e) {   
       				 	    System.out.println("Problem playing file " + filename+e);
							return;
        				}  
        				}
				}
					};
			 runner = new Thread(r1);
        	 runner.start();
			try{
			runner.join();
			}catch(InterruptedException ire){
			System.out.println(ire);
			}
       	}    	
	
   	public void stop() {
		player.stop();
		try{
		fis.close();
		bis.close();
		}catch(IOException ioe){
		System.out.println(ioe);
		}
		return;
		
	}	
}
