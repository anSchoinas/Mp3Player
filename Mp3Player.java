/***********************/
/((*****Mp3Player*******/
/***********************/
/****Andreas Schoinas***/
/***********************/


import java.io.*;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.util.*;
import java.util.regex.*;
import javazoom.spi.mpeg.sampled.file.*;
import java.awt.*;
import java.awt.dnd.*;
import java.awt.event.*;
import javax.swing.*;
import javax.sound.sampled.*;
import javax.swing.event.*;
import java.net.*;
import org.tritonus.share.sampled.TAudioFormat;
import org.tritonus.share.sampled.file.TAudioFileFormat;
import java.util.concurrent.TimeUnit;
import javax.swing.filechooser.*;
import javax.swing.table.*;
import javax.swing.border.*;


public class Mp3Player{
	
	public Mp3Player(){
		generateUI testplayer = new generateUI();
	}
	public static void main(String args[]){
		Mp3Player player = new Mp3Player();
	}
}
 
 	
class generateUI extends JFrame implements ActionListener{
	private static final long serialVersionUID = 6975520272697552027L;
	GridBagLayout gridbag = new GridBagLayout();
	JFrame frame = new JFrame("Schoinas Mp3 Player");
	public int inRowToPlay=0;
	private int i=0;
	private int j=0;
	private int veccount;
	private int curval;
	private int status=0;
	public long millis;
	private long stad;
	public Long duration;
	private PlayMode mp3;
	private Thread increasethr;
	private Thread play1;
	private Thread play2;
	private Thread pauseSong;
	private Thread pauseProgress;
	private FileInputStream fis;
	private AudioFileFormat baseFileFormat;
	private Vector<Vector> songs;
	private Vector<String> plLabel;
	private Vector<String> column;
	private Vector<String> temp;
	private Vector<File> dirlist=new Vector<File>(15,1);
	private final JScrollPane scroll;
	private final JPanel topleft = new JPanel();
	private final JPanel topright = new JPanel();
	private final BgPanel bgtest;
	private JToolBar topBar = new JToolBar();
	private JButton play;
	private JButton stop;
	private JButton forward;
	private JButton next;
	public JProgressBar playing;
	private JMenuBar menus= new JMenuBar();
/***********************
/*****Menu Items*******
/***********************/
	JMenuItem j1= new JMenuItem("OpenFile");
	JMenuItem j2= new JMenuItem("Open Playlist");
	JMenuItem j4= new JMenuItem("Save Playlist");
	JMenuItem j5= new JMenuItem("Exit");
	JMenuItem j6= new JMenuItem("Preferences");
	JMenuItem j7= new JMenuItem("Language Settings");
	JMenuItem j8= new JMenuItem("Appearance Settings");
	JMenuItem j9= new JMenuItem("Maker");
/***********************
/*****Crate Labels******
/***********************/
	JLabel timer=new JLabel("0:00",SwingConstants.CENTER);
	JLabel title = new JLabel("TItle: ---",SwingConstants.RIGHT);
	JLabel artist = new JLabel("Artist: ---",SwingConstants.RIGHT);
	JLabel album = new JLabel("Album: ---",SwingConstants.RIGHT);
	JLabel info = new JLabel("Artist Info:",SwingConstants.CENTER);
	JLabel statlabel = new JLabel("Status: Idle");
/***********************
/****Generate Colors****
/***********************/
	Color vlack = Color.decode("#000000");
	Color laxani = Color.decode("#00FF00");
	Color yeloou = Color.decode("#FFF948");
	Color orang=Color.decode("#FF5112");
	Color blou=Color.decode("#1616C4");
	public Color myColor =yeloou;
	
	ImageIcon apple = new ImageIcon("img/apple.png");
	ImageIcon thunder = new ImageIcon("img/thunder.png");
	ImageIcon steer = new ImageIcon("img/steer.png");
	public ImageIcon myIcon = thunder;
/*********************	
/*****Constructor*****
/*********************/	
	public generateUI(){
		GridBagConstraints constraints;
		frame.setSize(800, 400);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		

/***********************
/*****Create Menus******
/***********************/
		menus = new JMenuBar();
		JMenu file = new JMenu("File");
		JMenu options = new JMenu("Options");
		JMenu about= new JMenu("About");
		file.add(j1);
		file.add(j2);
		file.add(j4);
		file.add(j5);
		options.add(j6);
		options.add(j7);
		options.add(j8);
		about.add(j9);
		menus.add(file);
		menus.add(options);
		menus.add(about);
		

/***********************
/*****JProgessBar*******
/***********************/
		playing= new JProgressBar(0,100);
		playing.setValue(0);
		playing.setStringPainted(true);
/***********************
/*****Create Icons******
/***********************/
		final ImageIcon playIcon=new ImageIcon("img/play.png");
		final ImageIcon pauseIcon=new ImageIcon("img/pause.png");
		final ImageIcon stopIcon=new ImageIcon("img/stop.png");
		final ImageIcon forwardIcon=new ImageIcon("img/step.png");
		final ImageIcon nextIcon=new ImageIcon("img/next.png");
		try {
       		 frame.setIconImage(ImageIO.read(new File("img/logo.png")));
    		} catch (IOException e) {
      		e.printStackTrace();
    		}
/***********************
/*****Create Buttons****
/***********************/
		play = new JButton(playIcon);
		play.setBorder(BorderFactory.createLineBorder(Color.black));
		stop = new JButton(stopIcon);
		stop.setBorder(BorderFactory.createLineBorder(Color.black));
		forward = new JButton(forwardIcon);
		forward.setBorder(BorderFactory.createLineBorder(Color.black));
		next = new JButton(nextIcon);
		next.setBorder(BorderFactory.createLineBorder(Color.black));
/*************************
/*****Finalize topleft****
/*************************/
		statlabel.setAlignmentX(LEFT_ALIGNMENT);
		topBar.add(play);
		topBar.add(stop);
		topBar.add(forward);
		topBar.add(next);
		topBar.setBackground(myColor);
		topBar.setFloatable(false);
		//stop.addActionListener(this);
		forward.addActionListener(this);
		topleft.setBackground(myColor);
		BoxLayout boxy = new BoxLayout(topleft, BoxLayout.Y_AXIS);
		frame.setJMenuBar(menus);
		topleft.add(playing);	
		topleft.add(topBar);
		topleft.add(statlabel);
		topleft.setLayout(boxy);
		topleft.setBorder(BorderFactory.createMatteBorder(25,25,0,0,myIcon));
/*******************************	
/******Top And Right Panel******
/*******************************/
		topright.setBackground(myColor);
		topright.setSize(100,800);
		BoxLayout flow2 = new BoxLayout(topright, BoxLayout.Y_AXIS);
		topright.setLayout(flow2);
		topright.add(timer);
		topright.add(title);
		topright.add(artist);
		topright.add(album);
		topright.setBorder(BorderFactory.createMatteBorder(25,25,0,25,myIcon));
		//topright.setMinimumSize(new Dimension(320,80));
/******************************
/*******BotLeft Panel**********
/******************************/
		final JEditorPane jedpane = new JEditorPane();
		jedpane.setEditable(false);
		jedpane.setOpaque(false);
		jedpane.setText("Loading Artist Biographical Info...");
		final ImageIcon albumIcon = new ImageIcon("img/dummy_album.png","Album image not Available");
		final JLabel imgpanel = new JLabel(albumIcon,SwingConstants.LEFT);
		imgpanel.setToolTipText("Album Image not Available");
		bgtest = new BgPanel();
		bgtest.add(imgpanel);
		final JSplitPane splitpanel = new JSplitPane(JSplitPane.VERTICAL_SPLIT,bgtest,jedpane);
		splitpanel.setBackground(myColor);
		Border bltitle=BorderFactory.createMatteBorder(0,25,25,0,myIcon);
		bltitle = BorderFactory.createTitledBorder(bltitle,"Album cover");
		splitpanel.setBorder(bltitle);
/******************************
/****Dhmiourgia Playlist*******
/******************************/
		songs=new Vector<Vector>(15,1);
		column = new Vector<String>(3);
		plLabel = new Vector<String>(3);
		for(i=0;i<12;i++){
		songs.insertElementAt(null, i);
		}
		i=0;
		plLabel.add(0,"#");
		plLabel.add(1,"Song Title");
		plLabel.add(2,"Duration");
		final JTable list= new JTable(songs,plLabel);
		list.setEnabled(true);
		list.setGridColor(vlack);
		list.setSelectionBackground(laxani);
		list.setRowSelectionAllowed(true);
		list.setShowVerticalLines(true);
		list.setShowHorizontalLines(false);
		list.addColumnSelectionInterval(0,2);
		list.setPreferredScrollableViewportSize(
			new Dimension(200,100));
		list.setBackground(myColor);
		list.setAutoResizeMode( JTable.AUTO_RESIZE_ALL_COLUMNS );
		int colwidth=20;
		TableColumn columnA = list.getColumn("#");		
		columnA.setMinWidth(colwidth);
		columnA.setMaxWidth(colwidth);
		TableColumn columnB = list.getColumn("Song Title");
		columnB.setMinWidth(9*colwidth);
		columnB.setMaxWidth(10*colwidth);
		TableColumn columnC = list.getColumn("Duration");
		columnC.setMinWidth(2*colwidth);
		columnC.setMaxWidth(2*colwidth);
		list.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
		//list.setDragEnabled(true); 
    	scroll = new JScrollPane(list);
		scroll.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		scroll.setBorder(BorderFactory.createMatteBorder(0,25,20,25,myIcon));
		list.setFillsViewportHeight(true);
		scroll.getViewport().add(list);
/****************************
/***** Eventlisteners********
/****************************/
		play.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent evt){
				switch(status){
				//Periptwsh pou path8ei to play
				case 0: 
				if (dirlist.isEmpty()){
					return;
				}else{
					curval =playing.getValue();
					j=curval;
					temp =new Vector<String>();
					temp=(songs.get(inRowToPlay));
					temp.set(0,">");
					songs.set(inRowToPlay,temp);
					final File file = dirlist.get(inRowToPlay);
					try {	
						baseFileFormat = new MpegAudioFileReader().getAudioFileFormat(file);
						Map properties = baseFileFormat.properties();
						millis= (((Long) properties.get("duration")).longValue()) / 1000;
					}
					catch (Exception e){
					System.out.println(e);
					}
					final String onoma = new String(file.getAbsolutePath().substring(file.getAbsolutePath().lastIndexOf("\\")+1)); 
					//Thread gia anaparagwgh mp3
					final Runnable run1 = new Runnable(){
						public void run(){
							try{						
								fis= new FileInputStream(file.getAbsolutePath());
								mp3 = new PlayMode(fis,onoma);
								mp3.play( (int) (curval /0.028),(int)millis);
							}
							catch(Exception e){
								System.out.println(e);
							}
						}
					};
					//Thread gia eyresh info apo to last.fm
					final Runnable dataandinfo = new Runnable(){
						public void run(){	
							JLabel cover= new JLabel();
							String biog = new String();
							play.setIcon(pauseIcon);
							title.setText("Now Playing: "+onoma);
							statlabel.setText("Playing");
							artist.setText("Artist: Retrieving info...");
							album.setText("Album: Retrieving info...");
							frame.repaint();
							getInfo currentsong = new getInfo();
							try{
							String[] ret=currentsong.songSearch(onoma.replace(".mp3", ""));
							artist.setText("Artist: "+ret[0]);
							album.setText("Album: "+ret[1]);
							String perma = ret[2].replace("/64s/ ","/126s/");
							perma=perma.replace("/34/","/126/");
							String albumlink =perma.replace("medium","large");
							URL imgURL =new URL(albumlink);
							if (imgURL != null) {
							BufferedImage img = ImageIO.read(imgURL);
					        ImageIcon albumcover = new ImageIcon(img);
							cover=new JLabel(albumcover,SwingConstants.LEFT);
							bgtest.removeAll();
							bgtest.add(cover);
							cover.setToolTipText(ret[1]);
							} else {
						        	System.err.println("Couldn't find file: " + ret[2]);
								}
							if (ret[3].equals("null")){
								biog = "Error retrieving artist bio...";
							}
							else{
								String biography=currentsong.artistBio(ret[3]);
								if (biography.equals("Problem retrieving file....")){
									biog=biography;
								} else{
								getHtml biohtml = new getHtml();
								biog =biohtml.asda(ret[0],biography);
								}
							}
							JEditorPane biogl = new JEditorPane("html",biog);
							JScrollPane edscroll= new JScrollPane(biogl);
							edscroll.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
							edscroll.setBorder(BorderFactory.createLineBorder(Color.black));
							edscroll.getViewport().add(biogl);
							splitpanel.setTopComponent(null);
							splitpanel.setBottomComponent(null);
							splitpanel.setTopComponent(bgtest);
							splitpanel.setBottomComponent(edscroll);
							frame.repaint();
							
						        
							}
							catch(Exception murle){
							System.out.println("oopsie"+murle);
							}
						}
					};
					//Thread gia progressbar
					final Runnable increase = new Runnable() {
						public void run(){
							playing.setMaximum((int)millis/1000);
							while(j<=(int)millis/1000){
								timer.setText(j/60+":"+String.format("%02d",j%60));
								playing.setValue(j);
								try{
									Thread.sleep(1000);
								} catch (InterruptedException e) {
									System.out.println("darn"+e); 
									return;
								}
								j+=1;	
							}
							
							try{
							play1.join();
							veccount=dirlist.size();
							status=0;
							playing.setValue(0);
							temp.set(0,"");
							songs.set(inRowToPlay,temp);
							if (inRowToPlay<veccount-1){
							inRowToPlay ++;
							play.doClick();
							}else{								
							play.setIcon(playIcon);
							inRowToPlay=0;
							frame.repaint();
							}
							}catch (Exception e){
							System.out.println(e);
							}		
						}
					};
				
					increasethr = new Thread(increase);
			  		play1 = new Thread(run1);
					play2 = new Thread(dataandinfo);
					increasethr.setPriority(Thread.MAX_PRIORITY);
	        		play1.setPriority(Thread.MAX_PRIORITY);
					play2.setPriority(Thread.MAX_PRIORITY);
					play1.start();
			  		play2.start();
					increasethr.start();
					status=1;
					frame.repaint();			
					break; 
				}
				//Periptwsh pou path8ei to pause
			  	case 1: play.setIcon(playIcon);
						statlabel.setText("Song Paused");
						curval=playing.getValue();
						mp3.stop();
						play1.interrupt();
						play2.interrupt();
						increasethr.interrupt();
						try{
							fis.close();
						}catch (Exception e){
							System.out.println(e);
						}
						playing.setValue(curval);
						mp3=null;
						frame.repaint();
						status=0;
						break;
		  		}
			}
		});	
		
		forward.addActionListener(new ActionListener(){
			public void actionPerformed (ActionEvent evt){
				curval=playing.getValue();
				mp3.stop();
				play1.interrupt();
				play2.interrupt();
				increasethr.interrupt();
				try{
				fis.close();
				}catch (Exception e){
				System.out.println(e);
				}
				playing.setValue(curval+(int)(millis*5/100000));
				status=0;	
				play.doClick();
			}
			});
			
		next.addActionListener(new ActionListener(){
			public void actionPerformed (ActionEvent evt){
				mp3.stop();
				play1.interrupt();
				play2.interrupt();
				increasethr.interrupt();
				try{
				fis.close();
				}catch (Exception e){
				System.out.println(e);
				}
				temp.set(0,"");
				veccount=dirlist.size();
				if(inRowToPlay<veccount-1){
				inRowToPlay ++;
				}else{
				inRowToPlay=0;
				}
				playing.setValue(0);
				status=0;	
				frame.repaint();
				play.doClick();
			}
			});
			
		list.addMouseListener(new MouseListener(){
			public void mouseClicked(MouseEvent event){
				if (event.getClickCount() == 2) {
				System.out.println("double clicked");
				}
			}
			public void mouseEntered(MouseEvent event){
			}
			
			public void mousePressed(MouseEvent event){
			}
			
			public void mouseReleased(MouseEvent event){
			}
			
			public void mouseExited(MouseEvent event){
			}
			});
		j1.addActionListener(new ActionListener(){
			public void actionPerformed (ActionEvent evt){
				JFileChooser c = new JFileChooser();
				FileNameExtensionFilter filter = new FileNameExtensionFilter("mp3 Audio Files","mp3");
				c.setFileFilter(filter);
				int rval = c.showOpenDialog(generateUI.this);
				if(rval==JFileChooser.APPROVE_OPTION){
			//add to list	
					File file = c.getSelectedFile();
					dirlist.add(file);
					try {	
					AudioFileFormat baseFileFormat = new MpegAudioFileReader().getAudioFileFormat(file);
					Map properties = baseFileFormat.properties();
					stad= (((Long) properties.get("duration")).longValue()) / 1000000;
					}
					catch (Exception e){
					System.out.println(e);
					}
					column = new Vector<String>();
					column.add("");
					column.add(c.getSelectedFile().getAbsolutePath().substring(file.getAbsolutePath().lastIndexOf("\\")+1).replace(".mp3",""));
					column.add(stad/60+":"+String.format("%02d",stad%60));
					songs.setElementAt(column,dirlist.size());
					//songs.add(column);
					list.repaint();
				}
				
			}
		});

		j2.addActionListener(new ActionListener(){
			public void actionPerformed (ActionEvent evt){
				JFileChooser c = new JFileChooser();
				FileNameExtensionFilter filter = new FileNameExtensionFilter("mp3 Audio Files","mp3");
				c.setFileFilter(filter);
				int rval = c.showOpenDialog(generateUI.this);
				if(rval==JFileChooser.APPROVE_OPTION){
			//add to list
			
				}
				
			}
		});
	
		j4.addActionListener(new ActionListener(){
		public void actionPerformed(ActionEvent evt) {
	   		JFileChooser c = new JFileChooser();
      // Demonstrate "Save" dialog:
	   		int rVal = c.showSaveDialog(generateUI.this);
	      		if (rVal == JFileChooser.APPROVE_OPTION) {
	      		}
		}
		});
		j5.addActionListener(this);
		j8.addActionListener(new ActionListener(){
			public void actionPerformed (ActionEvent evt){
				JFrame prefframe = new JFrame("Appearance Settings");
				final JPanel prefwind = new JPanel();
				prefframe.setSize(400,200);
				prefwind.setBorder(BorderFactory.createMatteBorder(35,35,35,35,myIcon));
				prefwind.setBackground(myColor);
				prefframe.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
				BoxLayout katheta = new BoxLayout(prefwind,BoxLayout.Y_AXIS);
				prefwind.setLayout(katheta);
				JLabel select = new JLabel("Select the Player's Theme",SwingConstants.CENTER);
				select.setMinimumSize(new Dimension(400,50));
				select.setBorder(BorderFactory.createLoweredBevelBorder());
				//JButton ok = new JButton("Select");
				String themes[] ={"Sun","Navy", "Fruit"};
				JComboBox<String> themelist = new JComboBox<>(themes);
				themelist.setMaximumSize(new Dimension(200,20));
				prefwind.add(select);
				prefwind.add(Box.createRigidArea(new Dimension(400,20)));
				prefwind.add(themelist);
				//prefwind.add(ok);
				pack();
				themelist.addActionListener(new ActionListener(){
					public void actionPerformed(ActionEvent jcbe){
						JComboBox cb =(JComboBox)jcbe.getSource();
						String selection = (String) cb.getSelectedItem();
						if(selection.equals("Sun")){
							myColor=yeloou;
							myIcon=thunder;
						}
						else if(selection.equals("Navy")){
							myColor=blou;
							myIcon=steer;
						}
						else if(selection.equals("Fruit")){
							myColor=orang;
							myIcon=apple;
						}
					
						scroll.setBorder(BorderFactory.createMatteBorder(20,20,20,20,myIcon));
						topleft.setBorder(BorderFactory.createMatteBorder(25,25,0,0,myIcon));
						topright.setBorder(BorderFactory.createMatteBorder(25,25,0,25,myIcon));
						splitpanel.setBorder(BorderFactory.createMatteBorder(25,25,25,0,myIcon));
						prefwind.setBorder(BorderFactory.createMatteBorder(35,35,35,35,myIcon));
						list.setBackground(myColor);
						topBar.setBackground(myColor);
						topleft.setBackground(myColor);
						topright.setBackground(myColor);
						splitpanel.setBackground(myColor);
						prefwind.setBackground(myColor);
						frame.repaint();
						prefwind.repaint();
					}
				});
				prefwind.setVisible(true);
				prefframe.add(prefwind);
				prefframe.setVisible(true);
			}
		});
		j9.addActionListener(this);
		
		stop.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent evt){
				play.setIcon(playIcon);
				statlabel.setText("Song Stopped");
				j=Integer.MAX_VALUE;
				increasethr.interrupt();
				playing.setValue(0);
				timer.setText("0:00");
				status=0;
				frame.repaint();
				mp3.stop();	
			}
		});		
		
/******************************
/*****Dhmiourgia Interface*****
/******************************/
		addComponent(frame.getContentPane(),topleft,0,0,12,5,10,0,GridBagConstraints.BOTH,GridBagConstraints.NORTHWEST);
		addComponent(frame.getContentPane(),scroll,13,4,13,36,40,100,GridBagConstraints.BOTH,GridBagConstraints.WEST);
		addComponent(frame.getContentPane(),topright,13,0,10,4,60,0,GridBagConstraints.HORIZONTAL,GridBagConstraints.NORTHWEST);
		addComponent(frame.getContentPane(),splitpanel,0,5,13,36,60,100,GridBagConstraints.BOTH,GridBagConstraints.EAST);
		frame.setVisible(true);
}
	

	private void addComponent(Container pane, Component component, int gridx, int gridy, int gridwidth, int gridheight, int weightx, int weighty,int fill,int anchor){
		pane.setLayout(gridbag);
		GridBagConstraints constraints = new GridBagConstraints();
		constraints.gridx=gridx;
		constraints.gridy=gridy;
		constraints.gridwidth=gridwidth;
		constraints.gridheight=gridheight;
		constraints.weightx=weightx;
		constraints.weighty=weighty;
		constraints.fill=fill;
		constraints.anchor=anchor;
		constraints.ipadx=10;
		constraints.ipady=10;
		gridbag.setConstraints(component,constraints);
		pane.add(component);	
		pane.repaint();
	}

	public void actionPerformed(ActionEvent evt){
			Object source = evt.getSource();				
				
			if (source==j5){
				System.exit(0);
			}
			if (source==j9){
				makerInfo andreas = new makerInfo();
			}
		}  
}
