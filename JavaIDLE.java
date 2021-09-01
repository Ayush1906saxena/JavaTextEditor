import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import java.io.*;
class JavaIDLE extends JFrame
{
  TextField tb,tb1;
  JTextArea ta,ta1;
  JScrollPane p1,p2;
  JButton [] bt=new JButton[5];
  String dst,path,file,tatext="Type your code here or click browse to open a program";
  public JavaIDLE()
  {
	super("Untitled - JavaIDE");
	setSize(700,550);
	setLayout(null);
	setLocationRelativeTo(null);
	setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	getContentPane().setBackground(new Color(200,200,255));
	setResizable(false);
	Font f=new Font("Arial",Font.PLAIN,16);
	String [] str={"Save","Browse","Compile","Run","New"};
	for(int i=0;i<bt.length;i++)
	{
	  bt[i]=new JButton(str[i]);
	  add(bt[i]);
	  bt[i].addActionListener(new TaskListener());
	}
  	bt[0].setBounds(260,40,80,28);//save button
	bt[4].setBounds(345,40,80,28);//new button
	ta=new JTextArea();
	p1=new JScrollPane(ta);
	p1.setBounds(50,70,600,230);
	add(p1);
	KeyTrap kt=new KeyTrap();
	ta.addKeyListener(kt);
	ta.addFocusListener(new ResetListener());
	ta.setText(tatext);
	ta1=new JTextArea();
	p2=new JScrollPane(ta1);
	p2.setBounds(50,350,600,100);
	ta1.setEditable(false);
	add(p2);
	ta.setFont(f);ta1.setFont(f);
	tb=new TextField();
	tb.setBounds(100,310,200,27);
	add(tb);
	tb.setEditable(false);
	tb1=new TextField();
	tb1.setBounds(100,460,200,27);
	add(tb1);
	tb1.addKeyListener(kt);
	tb.addKeyListener(kt);
	bt[1].setBounds(320,310,80,27);//browse button
	bt[2].setBounds(420,310,80,27);//compile button
	bt[3].setBounds(320,460,80,27);//run button
	addWindowListener(new CloseWindow());
	bt[0].setEnabled(false);
	bt[3].setEnabled(false);
	bt[2].setEnabled(false);
	setVisible(true);
  }
  class ResetListener extends FocusAdapter
  {
    public void focusGained(FocusEvent fe)
    {
	if(fe.getSource()==ta)
	{
	  if(ta.getText().equals(tatext))
	    ta.setText("");
	}
     }
  }
  class TaskListener implements ActionListener
  {
    public void actionPerformed(ActionEvent evt)
    {
      try
      {
	if(evt.getSource()==bt[0])//save button
	{
 	  if(getTitle().equals("Untitled - JavaIDE"))
	  {
	     FileDialog fd=new FileDialog(JavaIDLE.this);
	     fd.setMode(1);
	     fd.setVisible(true);
	     file=fd.getFile();
	     dst=fd.getDirectory();
	     path=dst+file;
	     if(file!=null)
	     {
	       PrintWriter pw=new PrintWriter(new FileWriter(path));
	       pw.write(ta.getText());
	       pw.flush();
	       setTitle(path+" - JavaIDLE");
	       tb.setText(path);
	     }
	  }
	  else
	  {
	       PrintWriter pw=new PrintWriter(new FileWriter(path));
	       pw.write(ta.getText());
	       pw.flush();
	  }
	  bt[0].setEnabled(false);
 	  bt[2].setEnabled(true);
	} 
	else if(evt.getSource()==bt[1])//browse button
	{
	  
	  FileDialog fd=new FileDialog(JavaIDLE.this);
	  fd.setVisible(true);
	  file=fd.getFile();
	  if(file!=null)
	  {
	    dst=fd.getDirectory();
	    path=dst+file;
	    BufferedReader br=new BufferedReader(new FileReader(path));
	    ta.setText("");
	    tb.setText(path);
	    bt[2].setEnabled(true);
	    setTitle(path+" - JavaIDLE");
	    String str=br.readLine();
	    while(str!=null)
	    {
	        ta.append(str+"\n");
		str=br.readLine();
	    }	
	   }
	} 
  	else if(evt.getSource()==bt[2])//compile button
	{
	  Runtime rt=Runtime.getRuntime();
	  Process p=rt.exec("javac -d "+dst+" "+tb.getText());
	  InputStream is=p.getErrorStream();
	  BufferedReader br=new BufferedReader(new InputStreamReader(is));
	  String str=br.readLine();
	  if(str==null)
	  {
		ta1.setText("Compiled successfully...");
		runp();
	  }
	  else
	  {
		ta1.setText("");
		while(str!=null)
		{
		  int i=str.lastIndexOf('\\');
		  str=str.substring(i!=-1?i+1:0);
		  ta1.append(str+"\n");
		  str=br.readLine();   
		}
	  }
	  p.destroy();
	}
	else if(evt.getSource()==bt[3])//run button
	{
	  Runtime rt=Runtime.getRuntime();
	  Process p=rt.exec("java -classpath "+dst+" "+tb1.getText());
	  InputStream is=p.getInputStream();
	  int x=is.read();
	  if(x==-1)
	  {
	    is=p.getErrorStream();
	    x=is.read();
	  }
	  String emsg="";
	  while(x!=-1)
	  {
	   emsg=emsg+(char)x;
	   x=is.read();
	  }
	  ta1.setText(emsg);
	  p.destroy();
	}
	if(evt.getSource()==bt[4])//new button
	{
	  ta.setText("");
	  ta1.setText("");
	  setTitle("Untitled - JavaIDE");
	  tb.setText("");
	  tb1.setText("");
	  path="";
	  file=null;
	  bt[0].setEnabled(false);
	  bt[3].setEnabled(false);
	  bt[2].setEnabled(false);
	  ta.setText(tatext);
	}
      }
      catch(Exception ex)
      {
	System.out.println(ex);
      }
    }
  }
  void runp()
  {
	String ifile=file.substring(0,file.lastIndexOf("."));
	tb1.setText(ifile);
        	bt[3].setEnabled(true);
  }
  class KeyTrap extends KeyAdapter
  {
    public void keyPressed(KeyEvent ke)
    {
	if(ke.getSource()==ta)
	{
	  bt[0].setEnabled(true);
	  if(ta.getText().equals(tatext))
	    ta.setText("");
	}
    }
    public void keyReleased(KeyEvent ke)
    {
	if(ke.getSource()==tb1 && tb1.getText().length()>0)
	 bt[3].setEnabled(true);
	else
	 bt[3].setEnabled(false);
	if(ke.getSource()==tb && tb.getText().length()>0)
	 bt[2].setEnabled(true);
	else
	 bt[2].setEnabled(false);
    }
  }  
  public static void main(String []s)
  {
	JFrame.setDefaultLookAndFeelDecorated(true);
	new JavaIDLE();
  }
}
class CloseWindow extends WindowAdapter
{
    public void windowClosing(WindowEvent we)
    {
	System.exit(0);
    }
}

