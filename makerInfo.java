import java.io.*;
import javax.swing.*;
import java.awt.*;
import javax.swing.event.*;
import java.awt.event.*;



class makerInfo extends JFrame implements ActionListener{
	JFrame infoframe = new JFrame("Schoinas Andreas");
	JPanel infowind = new JPanel();
	JButton ok;
	JLabel name;
	JLabel property;
	JLabel year;
	BoxLayout katheto;
	public makerInfo(){
		super();
		infoframe.setSize(400,200);
		infowind.setBorder(BorderFactory.createLineBorder(Color.red));
		infoframe.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		katheto = new BoxLayout(infowind,BoxLayout.Y_AXIS);
		infowind.setLayout(katheto);
		name = new JLabel("Creator: Schoinas Andreas",SwingConstants.CENTER);
		property = new JLabel("Student of National Technical Uniersity of Athens",SwingConstants.CENTER);
		year = new JLabel ("9th Semester   A.M:03109750",SwingConstants.CENTER);
		ok = new JButton("Ok");
		infowind.add(Box.createRigidArea(new Dimension(200,20)));
		infowind.add(name);
		infowind.add(Box.createRigidArea(new Dimension(50,20)));
		infowind.add(property);
		infowind.add(Box.createRigidArea(new Dimension(100,10)));
		infowind.add(year);
		infowind.add(Box.createRigidArea(new Dimension(200,25)));
		infowind.add(ok);
		pack();
		ok.addActionListener(this);
		infowind.setVisible(true);
		infoframe.add(infowind);
		infoframe.setVisible(true);
	}
	public void actionPerformed(ActionEvent evt){
		Object source = evt.getSource();
		if (source==ok){
			infoframe.dispose();
		}
	}
	}
