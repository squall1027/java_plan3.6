import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;
import java.util.Vector;
import java.io.*;
import javax.swing.*;
public class DrawGraphic extends JFrame   //主類，擴展了JFrame類，用來生成DrawGraphi主介面
{
	Container con;
	DrawPanel panel; //定義面板
	JPanel labelPanel; //定義標籤面板
	JToolBar buttonPanel; //定義按鈕面板
	JMenuBar bar; //攔
	JMenu file,tool,draw,color,word,help;
	JMenu file_edit;
	JMenuItem file_new,file_open,file_save,file_edit_undo,file_edit_recover,file_edit_clear,file_exit;
	JMenu tool_pencil;
	JMenuItem tool_brush,tool_pencil_small,tool_pencil_middle,tool_pencil_big,tool_eraser;
	JMenu draw_rect,draw_ellipse;
	JMenuItem draw_rect_white,draw_rect_color,draw_ellipse_white,draw_ellipse_color;
	JMenuItem draw_line,draw_word;
	JMenu color_draw,color_fill;
	JMenuItem color_draw_black,color_draw_red,color_draw_yellow,color_draw_green,color_draw_blue,color_draw_panel,
		color_fill_black,color_fill_red,color_fill_yellow,color_fill_green,color_fill_blue,color_fill_panel;
	JMenuItem word_set,help_about;
	//JMenuItem help
	JLabel label1,label2,label3,label4,label5,operate,quick;
	JButton New,open,save,pencil,line,rect,ellipse,words,eraser,undo,recover,clear;
	int choice=1;  //打開畫板後默認選擇筆的值
	//1.小筆;11,中筆12;大筆2.線;3.矩形4.橢圓;5:橡皮擦;6:文字;7:清空
	//1:xi_pencil;11:zhong_pencil;12:da_pencil;2:line;3:rect;4:ellipse;5:eraser;6:word;7:clear
	int x,y,firstX,firstY,lastX,lastY,beginX=0,beginY=0;
	Color drawColor=Color.BLACK,fillColor=Color.BLACK;
	boolean fill=false,did=false,saved=false;
	int pencilRough=0;   //鉛筆的默認粗值
	//pencilRough:5,9;eraser:32;
	//draw_rectangle_rough:1f,4f,8f
	String string=""; //用來存放當前字體
	//Font font=new Font("Serif",Font.) 
	//int wordX,wordY; 
	Vector elements=new Vector();
	Vector lineRecords=new Vector();
	ObjectInputStream input;
	ObjectOutputStream output;
	String name="Serif";
	int style=Font.PLAIN;
	int size=30;
	Font f=new Font(name,style,size);
	Float rough=1f;
	Stroke s=new BasicStroke(1f);
	Image nowPicture;
	public DrawGraphic()  //畫板的主要功能
	{
		super("JPaint  V3.6");
		con=this.getContentPane();
		//GridLayout g=new GridLayout(1,2);
		//con.setLayout(g);
		bar=new JMenuBar();//定義菜單欄
		operate=new JLabel("     操作欄   ");
		bar.add(operate);
		//創建 各種基本操作欄的按鈕
		file=new JMenu("文件");
		file_new=new JMenuItem("新建");
		file_new.addActionListener(new NewFileListener());
		file.add(file_new);
		file_open=new JMenuItem("打開");
		file_open.addActionListener(new OpenFileListener());
		file.add(file_open);
		file_save=new JMenuItem("保存");
		file_save.addActionListener(new SaveFileListener());
		file.add(file_save);
		file_edit=new JMenu("編輯");
		file_edit_undo=new JMenuItem("撤消");
		file_edit_undo.addActionListener(new DealUndo());
		file_edit.add(file_edit_undo);
		file_edit_recover=new JMenuItem("恢復");
		file_edit_recover.addActionListener(new DealRecover());
		file_edit.add(file_edit_recover);
		file_edit_clear=new JMenuItem("清空");
		file_edit_clear.addActionListener(new DealButton());
		file_edit.add(file_edit_clear);
		file.add(file_edit);
		file_exit=new JMenuItem("退出");
		file_exit.addActionListener(new ExitFileListener());
		file.add(file_exit);
		bar.add(file);
		tool=new JMenu("工具");
		tool_pencil=new JMenu("鉛筆");
		tool_pencil_small=new JMenuItem("小");
		tool_pencil_small.addActionListener(new DealButton());
		tool_pencil.add(tool_pencil_small);
		tool_pencil_middle=new JMenuItem("中");
		tool_pencil_middle.addActionListener(new DealButton());
		tool_pencil.add(tool_pencil_middle);
		tool_pencil_big=new JMenuItem("大");
		tool_pencil_big.addActionListener(new DealButton());
		tool_pencil.add(tool_pencil_big);
		tool.add(tool_pencil);
		tool_brush=new JMenuItem("麥克筆");
		tool_brush.addActionListener(new DealButton());
		tool.add(tool_brush);
		tool_eraser=new JMenuItem("橡皮擦");
		tool_eraser.addActionListener(new DealButton());
		tool.add(tool_eraser);
		bar.add(tool);
		draw=new JMenu("繪製");
		draw_line=new JMenuItem("直線");
		draw_line.addActionListener(new DealButton());
		draw.add(draw_line);
		draw_rect=new JMenu("矩形");
		draw_rect_white=new JMenuItem("不填充");
		draw_rect_white.addActionListener(new DealButton());
		draw_rect.add(draw_rect_white);
		draw_rect_color=new JMenuItem("使用當前填充顏色");
		draw_rect_color.addActionListener(new DealButton());
		draw_rect.add(draw_rect_color);
		draw.add(draw_rect);
		draw_ellipse=new JMenu("橢圓形");
		draw_ellipse_white=new JMenuItem("不填充");
		draw_ellipse_white.addActionListener(new DealButton());
		draw_ellipse.add(draw_ellipse_white);
		draw_ellipse_color=new JMenuItem("使用當前填充顏色");
		draw_ellipse_color.addActionListener(new DealButton());
		draw_ellipse.add(draw_ellipse_color);
		draw.add(draw_ellipse);
		draw_word=new JMenuItem("文字");
		draw_word.addActionListener(new ShowWordDialog());
		draw.add(draw_word);
		bar.add(draw);
		color=new JMenu("顏色");
		color_draw=new JMenu("畫筆顏色");
		color_draw_black=new JMenuItem("黑");
		color_draw_black.addActionListener(new DealButton());
		color_draw.add(color_draw_black);
		color_draw_red=new JMenuItem("紅");
		color_draw_red.addActionListener(new DealButton());
		color_draw.add(color_draw_red);
		color_draw_yellow=new JMenuItem("黃");
		color_draw_yellow.addActionListener(new DealButton());
		color_draw.add(color_draw_yellow);
		color_draw_green=new JMenuItem("綠");
		color_draw_green.addActionListener(new DealButton());
		color_draw.add(color_draw_green);
		color_draw_blue=new JMenuItem("藍");
		color_draw_blue.addActionListener(new DealButton());
		color_draw.add(color_draw_blue);
		color_draw_panel=new JMenuItem("調色板");
		color_draw_panel.addActionListener(new ChooseColor());
		color_draw.add(color_draw_panel);
		color.add(color_draw);
		color_fill=new JMenu("填充顏色");
		color_fill_black=new JMenuItem("黑");
		color_fill_black.addActionListener(new DealButton());
		color_fill.add(color_fill_black);
		color_fill_red=new JMenuItem("紅");
		color_fill_red.addActionListener(new DealButton());
		color_fill.add(color_fill_red);
		color_fill_yellow=new JMenuItem("黃");
		color_fill_yellow.addActionListener(new DealButton());
		color_fill.add(color_fill_yellow);
		color_fill_green=new JMenuItem("綠");
		color_fill_green.addActionListener(new DealButton());
		color_fill.add(color_fill_green);
		color_fill_blue=new JMenuItem("藍");
		color_fill_blue.addActionListener(new DealButton());
		color_fill.add(color_fill_blue);
		color_fill_panel=new JMenuItem("調色板");
		color_fill_panel.addActionListener(new ChooseColor());
		color_fill.add(color_fill_panel);
		color.add(color_fill);
		bar.add(color);
		word=new JMenu("字體");
		word_set=new JMenuItem("改變字體");
		word_set.addActionListener(new DrawFont());
		word.add(word_set);
		bar.add(word);
		help=new JMenu("幫助");
		help_about=new JMenuItem("關於...");
		help_about.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				JOptionPane.showMessageDialog(panel,"這裡你們填寫補充資料","使用說明",JOptionPane.PLAIN_MESSAGE);
			}
		});
		help.add(help_about);
		bar.add(help);
		this.setJMenuBar(bar);
		//buttonPanel=new JToolBar(JToolBar.VERTICAL);
		buttonPanel=new JToolBar(JToolBar.HORIZONTAL);
		quick=new JLabel("快捷欄  "); 
		buttonPanel.add(quick);
		//創建各種基本快捷欄的按鈕
		New=new JButton("新建");
		New.addActionListener(new NewFileListener());
		buttonPanel.add(New);
		open=new JButton("打開");
		open.addActionListener(new OpenFileListener());
		buttonPanel.add(open);
		save=new JButton("保存");
		save.addActionListener(new SaveFileListener());
		buttonPanel.add(save);
		pencil=new JButton("鉛筆");
		pencil.addActionListener(new DealButton());
		buttonPanel.add(pencil);
		line=new JButton("直線");
		line.addActionListener(new DealButton());
		buttonPanel.add(line);
		rect=new JButton("矩形");
		rect.addActionListener(new DealButton());
		buttonPanel.add(rect);
		ellipse=new JButton("橢圓");
		ellipse.addActionListener(new DealButton());
		buttonPanel.add(ellipse);
		words=new JButton("文字");
		words.addActionListener(new ShowWordDialog());
		buttonPanel.add(words);
		eraser=new JButton("橡皮擦");
		eraser.addActionListener(new DealButton());
		buttonPanel.add(eraser);
		undo=new JButton("撤消");
		undo.addActionListener(new DealUndo());
		buttonPanel.add(undo);
		recover=new JButton("恢復");
		recover.addActionListener(new DealRecover());
		buttonPanel.add(recover);
		clear=new JButton("清空");
		clear.addActionListener(new DealButton());
		buttonPanel.add(clear);
		con.add(buttonPanel,BorderLayout.NORTH);
		panel=new DrawPanel();
		panel.addMouseMotionListener(new PanelMouseMotion());
		panel.addMouseListener(new PanelMouse());
		panel.setBackground(Color.WHITE);
		con.add(panel,BorderLayout.CENTER);
		labelPanel=new JPanel();//顯示鼠標狀態的提示條
		GridLayout g=new GridLayout(5,1);
		labelPanel.setLayout(g);
		label1=new JLabel("當前鼠標位置:x=0,y=0");
		labelPanel.add(label1);
		label2=new JLabel("當前畫筆顏色:黑");
		labelPanel.add(label2);
		label3=new JLabel("當前填充顏色:黑");
		labelPanel.add(label3);
		label4=new JLabel("當前畫筆大小:小");
		labelPanel.add(label4);
		label5=new JLabel("當前字體:\"襯線體\",常規,30");
		labelPanel.add(label5);
		con.add(labelPanel,BorderLayout.SOUTH);
		setVisible(true);
		setSize(700,700); //畫圖區域大小
		this.setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		this.addWindowListener(new WindowAdapter()
		{
			public void windowClosing(WindowEvent e)
			{
				if(did==true && saved==false)
				{
					int result=JOptionPane.showConfirmDialog(panel,"您是否要儲存對 未命名 所做的變更?","Java 畫圖",JOptionPane.YES_NO_CANCEL_OPTION,JOptionPane.PLAIN_MESSAGE);
					if(result==0)
					{
						//save 的功能
						saveFileListener();
						saved=true;
						System.exit(0);
					}
					else if(result==1)
					{
						System.exit(0);
					}
					else if(result==2)
					{
						System.out.println("cancel");
					}
				}
				else
				{
					System.exit(0);
				}
			}
		});
	}
	class DealButton implements ActionListener
	{
		public void actionPerformed(ActionEvent e)//如果被觸發，則調用choice函數段
		{	//如果當前選擇的按鈕是隨筆畫，則進行下面的操作
			if((e.getSource()==pencil && pencilRough==0) || e.getSource()==tool_pencil_small)
			{
				choice=1;
				pencilRough=0;
				label4.setText("當前畫筆大小:小");
				panel.setCursor(Cursor.getPredefinedCursor(Cursor.CROSSHAIR_CURSOR));
			}
			else if(e.getSource()==pencil && pencilRough==5)
			{
				choice=11;
				panel.setCursor(Cursor.getPredefinedCursor(Cursor.CROSSHAIR_CURSOR));
			}
			else if(e.getSource()==pencil && pencilRough==9)
			{
				choice=12;
				panel.setCursor(Cursor.getPredefinedCursor(Cursor.CROSSHAIR_CURSOR));
			}
			else if(e.getSource()==tool_pencil_middle)
			{
				choice=11;
				pencilRough=5;
				label4.setText("當前畫筆大小:中");
				panel.setCursor(Cursor.getPredefinedCursor(Cursor.CROSSHAIR_CURSOR));
			}
			else if(e.getSource()==tool_pencil_big)
			{
				choice=12;
				pencilRough=9;
				label4.setText("當前畫筆大小:大");
				panel.setCursor(Cursor.getPredefinedCursor(Cursor.CROSSHAIR_CURSOR));
			}
			else if(e.getSource()==tool_brush)
			{
				choice=12;
				pencilRough=20;
				label4.setText("當前畫筆大小:極大");
				panel.setCursor(Cursor.getPredefinedCursor(Cursor.CROSSHAIR_CURSOR));
			}
			else if(e.getSource()==line || e.getSource()==draw_line)
			{
				choice=2;
				panel.setCursor(Cursor.getPredefinedCursor(Cursor.CROSSHAIR_CURSOR));
			}
			else if(e.getSource()==rect || e.getSource()==draw_rect_white)
			{
				choice=3;
				fill=false;
				panel.setCursor(Cursor.getPredefinedCursor(Cursor.CROSSHAIR_CURSOR));
			}
			else if(e.getSource()==draw_rect_color)
			{
				choice=3;
				fill=true;
				panel.setCursor(Cursor.getPredefinedCursor(Cursor.CROSSHAIR_CURSOR));
			}
			else if(e.getSource()==ellipse || e.getSource()==draw_ellipse_white)
			{
				choice=4;
				fill=false;
				panel.setCursor(Cursor.getPredefinedCursor(Cursor.CROSSHAIR_CURSOR));
			}
			else if(e.getSource()==draw_ellipse_color)
			{
				choice=4;
				fill=true;
				panel.setCursor(Cursor.getPredefinedCursor(Cursor.CROSSHAIR_CURSOR));
			}
			else if(e.getSource()==tool_eraser || e.getSource()==eraser)
			{
				choice=5;
				String url="image/eraser.png";
				Toolkit tk=Toolkit.getDefaultToolkit();
				Image image=new ImageIcon(url).getImage();
				Cursor cursor=tk.createCustomCursor(image, new Point(16,16),"norm");
				panel.setCursor(cursor);
			}
			else if(e.getSource()==color_draw_black)
			{
				drawColor=Color.BLACK;
				label2.setText("當前畫筆顏色:黑");
			}
			else if(e.getSource()==color_draw_red)
			{
				drawColor=Color.RED;
				label2.setText("當前畫筆顏色:紅");
			}
			else if(e.getSource()==color_draw_yellow)
			{
				drawColor=Color.YELLOW;
				label2.setText("當前畫筆顏色:黃");
			}
			else if(e.getSource()==color_draw_green)
			{
				drawColor=Color.GREEN;
				label2.setText("當前畫筆顏色:綠");
			}
			else if(e.getSource()==color_draw_blue)
			{
				drawColor=Color.BLUE;
				label2.setText("當前畫筆顏色:藍");
			}
			else if(e.getSource()==color_fill_black)
			{
				fillColor=Color.BLACK;
				label3.setText("當前填充顏色:黑");
			}
			else if(e.getSource()==color_fill_red)
			{
				fillColor=Color.RED;
				label3.setText("當前填充顏色:紅");
			}
			else if(e.getSource()==color_fill_yellow)
			{
				fillColor=Color.YELLOW;
				label3.setText("當前填充顏色:黃");
			}
			else if(e.getSource()==color_fill_green)
			{
				fillColor=Color.GREEN;
				label3.setText("當前填充顏色:綠");
			}
			else if(e.getSource()==color_fill_blue)
			{
				fillColor=Color.BLUE;
				label3.setText("當前填充顏色:藍");
			}
			else if(e.getSource()==clear || e.getSource()==file_edit_clear)
			{
				Graphics g=panel.getGraphics();
				Graphics2D g2=(Graphics2D)g;
				Rectangle2D rect = new Rectangle2D.Double(0,0,panel.getWidth(),panel.getHeight());
				g2.setColor(Color.WHITE);
				g2.fill(rect);
				nowPicture=null;
				panel.image=null;
				panel.cutRecord=0;
				did=false;
				elements.clear();
			}
		}
	}
	class PanelMouse extends MouseAdapter
	{
		int minIndex,maxIndex;
		public void mousePressed(MouseEvent e)
		{
			firstX=e.getX();
			firstY=e.getY();
			beginX=e.getX();
			beginY=e.getY();
			if(choice==1)
			{
				DrawElement lineElement=new DrawElement(beginX,beginY,e.getX(),e.getY(),drawColor,1f);
				//elements.addElement(lineElement);
				elements.add(panel.cutRecord,lineElement);
				panel.panelElements=elements;
				minIndex=panel.cutRecord;
				panel.cutRecord++;
				repaint();//將有關值設置為初始狀態，並且重畫
				did=true;
			}
			else if(choice==11 || choice==12)
			{
				DrawElement ellipseElement=new DrawElement(new Point2D.Double(x,y),x+pencilRough,y+pencilRough,true,drawColor,1f);
				//elements.addElement(ellipseElement);
				elements.add(panel.cutRecord,ellipseElement);
				panel.panelElements=elements;
				minIndex=panel.cutRecord;
				panel.cutRecord++;
				repaint();//將有關值設置為初始狀態，並且重畫
				did=true;
			}
			else if(choice==2)
			{
				if(pencilRough==0)
				{
					rough=1f;
				}
				else if(pencilRough==5)
				{
					rough=4f;
				}
				else if(pencilRough==9)
				{
					rough=8f;
				}
				DrawElement lineElement=new DrawElement(firstX,firstY,e.getX(),e.getY(),drawColor,rough);
				//elements.addElement(lineElement);
				elements.add(panel.cutRecord, lineElement);
				panel.panelElements=elements;
				panel.cutRecord++;
				repaint();//將有關值設置為初始狀態，並且重畫
				did=true;
			}
			else if(choice==3)
			{
				if(fill==false)
				{
					if(pencilRough==0)
					{
						rough=1f;
					}
					else if(pencilRough==5)
					{
						rough=4f;
					}
					else if(pencilRough==9)
					{
						rough=8f;
					}
					DrawElement rectElement=new DrawElement(firstX,firstY,e.getX(),e.getY(),false,drawColor,rough);
					//elements.addElement(rectElement);
					elements.add(panel.cutRecord, rectElement);
					panel.panelElements=elements;
					panel.cutRecord++;
					repaint();//將有關值設置為初始狀態，並且重畫
					did=true;
				}
				else if(fill==true)
				{
					DrawElement rectElement=new DrawElement(firstX,firstY,e.getX(),e.getY(),true,fillColor,1f);
					//elements.addElement(rectElement);
					elements.add(panel.cutRecord, rectElement);
					panel.panelElements=elements;
					panel.cutRecord++;
					repaint();//將有關值設置為初始狀態，並且重畫
					did=true;
				}
			}
			else if(choice==4)
			{
				if(fill==false)
				{
					if(pencilRough==0)
					{
						rough=1f;
					}
					else if(pencilRough==5)
					{
						rough=4f;
					}
					else if(pencilRough==9)
					{
						rough=8f;
					}
					DrawElement ellipseElement=new DrawElement(new Point2D.Double(firstX,firstY),e.getX(),e.getY(),false,drawColor,rough);
					//elements.addElement(ellipseElement);
					elements.add(panel.cutRecord,ellipseElement);
					panel.panelElements=elements;
					panel.cutRecord++;
					repaint();//將有關值設置為初始狀態，並且重畫
					did=true;
				}
				else if(fill=true)
				{
					DrawElement ellipseElement=new DrawElement(new Point2D.Double(firstX,firstY),e.getX(),e.getY(),true,fillColor,1f);
					//elements.addElement(ellipseElement);
					elements.add(panel.cutRecord,ellipseElement);
					panel.panelElements=elements;
					panel.cutRecord++;
					repaint();//將有關值設置為初始狀態，並且重畫
					did=true;
				}
			}
		}
		public void mouseReleased(MouseEvent e)
		{
			saved=false;
			lastX=e.getX();
			lastY=e.getY();
			if(choice==1)
			{
				DrawElement lineElement=new DrawElement(lastX,lastY,e.getX(),e.getY(),drawColor,1f);
				//elements.addElement(lineElement);
				elements.add(panel.cutRecord,lineElement);
				panel.panelElements=elements;
				maxIndex=panel.cutRecord;
				LineRecord record=new LineRecord(minIndex,maxIndex);
				lineRecords.add(record);
				panel.cutRecord++;
				repaint();//將有關值設置為初始狀態，並且重畫
				did=true;
			}
			else if(choice==11 || choice==12)
			{
				DrawElement ellipseElement=new DrawElement(new Point2D.Double(x,y),x+pencilRough,y+pencilRough,true,drawColor,1f);
				//elements.addElement(ellipseElement);
				elements.add(panel.cutRecord,ellipseElement);
				panel.panelElements=elements;
				maxIndex=panel.cutRecord;
				LineRecord record=new LineRecord(minIndex,maxIndex);
				lineRecords.add(record);
				panel.cutRecord++;
				repaint();//將有關值設置為初始狀態，並且重畫
				did=true;
			}
			else if(choice==2)
			{
				DrawElement lineElement=new DrawElement(firstX,firstY,lastX,lastY,drawColor,rough);
				elements.set(panel.cutRecord-1,lineElement);
				panel.panelElements=elements;
				repaint();//將有關值設置為初始狀態，並且重畫
				did=true;
			}
			else if(choice==3)
			{
				if(fill==false)
				{
					DrawElement rectElement=new DrawElement(firstX,firstY,lastX,lastY,false,drawColor,rough);
					elements.set(panel.cutRecord-1,rectElement);
					panel.panelElements=elements;
					repaint();//將有關值設置為初始狀態，並且重畫
					did=true;
				}
				else if(fill==true)
				{
					DrawElement rectElement=new DrawElement(firstX,firstY,lastX,lastY,true,fillColor,1f);
					elements.set(panel.cutRecord-1,rectElement);
					panel.panelElements=elements;
					repaint();//將有關值設置為初始狀態，並且重畫
					did=true;
				}
			}
			else if(choice==4)
			{
				if(fill==false)
				{
					DrawElement ellipseElement=new DrawElement(new Point2D.Double(firstX,firstY),lastX,lastY,false,drawColor,rough);
					elements.set(panel.cutRecord-1,ellipseElement);
					panel.panelElements=elements;
					repaint();//將有關值設置為初始狀態，並且重畫
					did=true;
				}
				else if(fill==true)
				{
					DrawElement ellipseElement=new DrawElement(new Point2D.Double(firstX,firstY),lastX,lastY,true,fillColor,1f);
					elements.set(panel.cutRecord-1,ellipseElement);
					panel.panelElements=elements;
					repaint();//將有關值設置為初始狀態，並且重畫
					did=true;
				}
			}
		}
		public void mouseExited(MouseEvent e)
		{
			setCursor(Cursor.getDefaultCursor());
		}
		public void mouseClicked(MouseEvent e)
		{
			saved=false;
			int x=e.getX();
			int y=e.getY();
			DrawElement wordElement=new DrawElement(string,x,y,drawColor,f);
			//elements.addElement(wordElement);
			elements.add(panel.cutRecord,wordElement);
			panel.panelElements=elements;
			panel.cutRecord++;
			repaint();//將有關值設置為初始狀態，並且重畫
			did=true;
		}
	}
	
	//鼠標事件PanelMouseMotio類，繼承了MouseMotionAdapter，用來完成鼠標相應事件操作
	class PanelMouseMotion extends MouseMotionAdapter
	{
		public void mouseDragged(MouseEvent e)
		{
			int x=e.getX();
			int y=e.getY();
			saved=false;
			label1.setText("當前鼠標位置:x="+x+",y="+y);
			if(choice==1)
			{
				DrawElement lineElement=new DrawElement(beginX,beginY,e.getX(),e.getY(),drawColor,1f);
				//elements.addElement(lineElement);
				elements.add(panel.cutRecord,lineElement);
				panel.panelElements=elements;
				panel.cutRecord++;
				repaint();//將有關值設置為初始狀態，並且重畫
				did=true;
				beginX=e.getX();
				beginY=e.getY();
			}
			else if(choice==11 || choice==12)
			{
				DrawElement ellipseElement=new DrawElement(new Point2D.Double(x,y),x+pencilRough,y+pencilRough,true,drawColor,1f);
				//elements.addElement(ellipseElement);
				elements.add(panel.cutRecord,ellipseElement);
				panel.panelElements=elements;
				panel.cutRecord++;
				repaint();//將有關值設置為初始狀態，並且重畫
				did=true;
			}
			else if(choice==2)
			{
				DrawElement lineElement=new DrawElement(firstX,firstY,e.getX(),e.getY(),drawColor,rough);
				elements.set(panel.cutRecord-1,lineElement);
				panel.panelElements=elements;
				repaint();//將有關值設置為初始狀態，並且重畫
				did=true;
			}
			else if(choice==3)
			{
				if(fill==false)
				{
					DrawElement rectElement=new DrawElement(firstX,firstY,e.getX(),e.getY(),false,drawColor,rough);
					elements.set(panel.cutRecord-1,rectElement);
					panel.panelElements=elements;
					repaint();//將有關值設置為初始狀態，並且重畫
					did=true;
				}
				else if(fill==true)
				{
					DrawElement rectElement=new DrawElement(firstX,firstY,e.getX(),e.getY(),true,fillColor,1f);
					elements.set(panel.cutRecord-1,rectElement);
					panel.panelElements=elements;
					repaint();//將有關值設置為初始狀態，並且重畫
					did=true;
				}
			}
			else if(choice==4)
			{
				if(fill==false)
				{
					DrawElement ellipseElement=new DrawElement(new Point2D.Double(firstX,firstY),e.getX(),e.getY(),false,drawColor,rough);
					elements.set(panel.cutRecord-1,ellipseElement);
					panel.panelElements=elements;
					repaint();//將有關值設置為初始狀態，並且重畫
					did=true;
				}
				else if(fill==true)
				{
					DrawElement ellipseElement=new DrawElement(new Point2D.Double(firstX,firstY),e.getX(),e.getY(),true,fillColor,1f);
					elements.set(panel.cutRecord-1,ellipseElement);
					panel.panelElements=elements;
					repaint();//將有關值設置為初始狀態，並且重畫
					did=true;
				}
			}
			else if(choice==5)
			{
				DrawElement ellipseElement=new DrawElement(new Point2D.Double(x,y),x+32,y+32,true,Color.WHITE,1f);
				//elements.addElement(ellipseElement);
				elements.add(panel.cutRecord,ellipseElement);
				panel.panelElements=elements;
				panel.cutRecord++;
				repaint();//將有關值設置為初始狀態，並且重畫
				did=true;
			}
		}
		public void mouseMoved(MouseEvent e)
		{
			x=e.getX();
			y=e.getY();
			setCursor(Cursor.getPredefinedCursor(Cursor.CROSSHAIR_CURSOR));
			label1.setText("當前鼠標位置:x="+x+",y="+y);
		}
	}
	class ShowWordDialog implements ActionListener
	{
		public void actionPerformed(ActionEvent e)
		{
			choice=6;
			string=JOptionPane.showInputDialog(panel,"請輸入文字:","輸入",JOptionPane.PLAIN_MESSAGE);
			if(string==null)
			{
				string="";
			}
		}
	}
	class ChooseColor implements ActionListener
	{
		public void actionPerformed(ActionEvent e)
		{
			if(e.getSource()==color_draw_panel)
			{
				drawColor=JColorChooser.showDialog(panel,"選擇鉛筆顏色",Color.BLACK);
				label2.setText("當前畫筆顏色:來自調色板");
			}
			else if(e.getSource()==color_fill_panel)
			{
				fillColor=JColorChooser.showDialog(panel,"選擇填充顏色",Color.WHITE);
				label3.setText("當前填充顏色:來自調色板");
			}
		}
	}
	class SaveFileListener implements ActionListener
	{
		public void actionPerformed(ActionEvent e)
		{
			saveFileListener();
			saved=true; //如果被觸發，則保存文件
		}
	}
	
	//保存圖形文件選舉的程序段
	public void saveFileListener()
	{
		if(nowPicture!=null)
		{
			int result=JOptionPane.showConfirmDialog(panel,"只能保存為.bd格式，原圖將無法保存，您想繼續嗎?","Java 畫圖",JOptionPane.YES_NO_CANCEL_OPTION,JOptionPane.PLAIN_MESSAGE);
			if(result==1 || result==2)
			{
				return;
			}
		}
		JFileChooser fileChooser=new JFileChooser();
		fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
		int result=fileChooser.showSaveDialog(panel);
		if(result==JFileChooser.CANCEL_OPTION) return;
		File fileName=fileChooser.getSelectedFile();
		fileName.canWrite();
		if(fileName==null || fileName.getName().equals(""))
		{
			JOptionPane.showMessageDialog(fileChooser,"Invalid File Name","Invalid File Name",JOptionPane.ERROR_MESSAGE);
		}
		else
		{
			try
			{
				fileName.delete();
				String n=fileName.getName();
				int length=n.length();
				FileOutputStream fos;
				if(n.charAt(length-1)=='d' && n.charAt(length-2)=='b' && n.charAt(length-3)=='.')
				{
					fos=new FileOutputStream(fileName);
				}
				else
				{
					fos=new FileOutputStream(fileName+".bd");
				}
				output=new ObjectOutputStream(fos);
				output.writeInt(panel.cutRecord);
				System.out.println("elements count="+panel.cutRecord);
				for(int i=0;i<panel.cutRecord;i++)
				{
					output.writeObject(elements.elementAt(i));
					output.flush();//將所有圖形信息強制轉換成父類線性化存儲到文件中
				}
				output.close();
				fos.close();
			}catch(IOException ioe)
			{
				ioe.printStackTrace();
			}
		}
	}
	
	//打開一個圖形文件程序段，openFileListener函數通過建立FileInputStream對象讀入文件
	public void openFileListener()
	{
		JFileChooser fileChooser=new JFileChooser();
		fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
		int result=fileChooser.showOpenDialog(panel);
		if(result==JFileChooser.CANCEL_OPTION) return;
		File fileName=fileChooser.getSelectedFile();
		String n=fileName.getName();
		int length=n.length();
		System.out.println("name="+n+";length="+length);
		if(n.charAt(length-1)=='d' && n.charAt(length-2)=='b' && n.charAt(length-3)=='.')
		{
			try
			{
				FileInputStream fis=new FileInputStream(fileName);
				input=new ObjectInputStream(fis);
				int count=input.readInt();
				Graphics g=panel.getGraphics();
				Graphics2D g2=(Graphics2D)g;
				for(int i=0;i<count;i++)
				{
					DrawElement reDrawElement=(DrawElement)input.readObject();
					elements.addElement(reDrawElement);
				}
				panel.image=null;
				panel.panelElements=elements;
				panel.cutRecord=elements.size();
				repaint();//將有關值設置為初始狀態，並且重畫
				input.close();
				fis.close();
			}
			catch(EOFException endofFileException)
			{
				JOptionPane.showMessageDialog(panel,"文件中沒有更多的信息","未找到類錯誤",JOptionPane.ERROR_MESSAGE);
			}
			catch(ClassNotFoundException classNotFoundException)
			{
				JOptionPane.showMessageDialog(panel, "無法創建實例","文件結束錯誤",JOptionPane.ERROR_MESSAGE);
			}
			catch(IOException ioException)
			{
				JOptionPane.showMessageDialog(panel, "讀取時遇到錯誤，可能是不支持的文件格式^_^","讀取錯誤",JOptionPane.ERROR_MESSAGE);
			}
		}
		else if(fileName==null || fileName.getName().equals(""))
		{
			JOptionPane.showMessageDialog(fileChooser,"Invalid File Name","Invalid File Name",JOptionPane.ERROR_MESSAGE);
		}
		else
		{ 
	        if (fileName!= null) 
	        {  
	            Image image =Toolkit.getDefaultToolkit().getImage(fileName.getAbsolutePath()); 
	            System.out.println(fileName.getAbsolutePath());
	            if (image!= null) 
	            {  
	            	System.out.println("image!=null");
	            	nowPicture=image;
	            	panel.image=nowPicture;
	            	repaint();//將有關值設置為初始狀態，並且重畫
	            }  
	        }  
		}
	}
	class OpenFileListener implements ActionListener
	{
		public void actionPerformed(ActionEvent e)
		{
			if(did==true && saved==false)
			{
				int result=JOptionPane.showConfirmDialog(panel,"您想要保存當前繪圖嗎?","畫圖",JOptionPane.YES_NO_CANCEL_OPTION,JOptionPane.PLAIN_MESSAGE);
				if(result==0)
				{
					//save
					saveFileListener();
					saved=true;
				}
				else if(result==1)
				{
					//not save
					Graphics g=panel.getGraphics();
					Graphics2D g2=(Graphics2D)g;
					Rectangle2D rect = new Rectangle2D.Double(0,0,panel.getWidth(),panel.getHeight());
					g2.setColor(Color.WHITE);
					g2.fill(rect);
					nowPicture=null;
					did=false;
					elements.clear();
					panel.cutRecord=0;
					openFileListener();
				}
				else if(result==2)
				{
					return;
				}
			}
			else 
			{
				Graphics g=panel.getGraphics();
				Graphics2D g2=(Graphics2D)g;
				Rectangle2D rect = new Rectangle2D.Double(0,0,panel.getWidth(),panel.getHeight());
				g2.setColor(Color.WHITE);
				g2.fill(rect);
				nowPicture=null;
				did=false;
				elements.clear();
				panel.cutRecord=0;
				openFileListener();
			}
		}
	}
	
	
	class NewFileListener implements ActionListener
	{	//新建一個文件程序段
		public void actionPerformed(ActionEvent e)
		{
			//System.out.println(saved);
			if(did==true && saved==false)
			{
				int result=JOptionPane.showConfirmDialog(panel,"您想要保存當前繪圖嗎?","畫圖",JOptionPane.YES_NO_CANCEL_OPTION,JOptionPane.PLAIN_MESSAGE);
				if(result==0)
				{
					//save
					saveFileListener();
					saved=true;
					Graphics g=panel.getGraphics();
					Graphics2D g2=(Graphics2D)g;
					Rectangle2D rect = new Rectangle2D.Double(0,0,panel.getWidth(),panel.getHeight());
					g2.setColor(Color.WHITE);
					g2.fill(rect);
					did=false;
					elements.clear();
					panel.cutRecord=0;
				}
				else if(result==1)
				{
					//not save
					Graphics g=panel.getGraphics();
					Graphics2D g2=(Graphics2D)g;
					Rectangle2D rect = new Rectangle2D.Double(0,0,panel.getWidth(),panel.getHeight());
					g2.setColor(Color.WHITE);
					g2.fill(rect);
					did=false;
					elements.clear();
					panel.cutRecord=0;
				}
				else if(result==2)
				{
					return;
				}
			}
			else
			{
				Graphics g=panel.getGraphics();
				Graphics2D g2=(Graphics2D)g;
				Rectangle2D rect = new Rectangle2D.Double(0,0,panel.getWidth(),panel.getHeight());
				g2.setColor(Color.WHITE);
				g2.fill(rect);
				did=false;
				elements.clear();
				panel.cutRecord=0;
			}
		}
	}
	class ExitFileListener implements ActionListener
	{
		public void actionPerformed(ActionEvent e)
		{
			System.exit(0);
		}
	}
	class DrawFont implements ActionListener
	{
		JFrame frame;
		JLabel l1,l2,l3;
		JPanel p1,p2,p3,buttonPanel;
		WordPanel wordPanel=new WordPanel();
		JComboBox fontBox,styleBox,sizeBox;
		JButton ok,cancel;
		Font nowFont=new Font("Serif",Font.PLAIN,30);
		public void actionPerformed(ActionEvent e)
		{
			frame=new JFrame();
			Container con=frame.getContentPane();
			GridLayout g=new GridLayout(5,1);
			con.setLayout(g);
			wordPanel=new WordPanel();
			p1=new JPanel();
			l1=new JLabel("選擇字體");
			p1.add(l1);
			fontBox=new JComboBox();
			fontBox.addItem("Serif");
			fontBox.addItem("Tamoha");
			fontBox.addItem("微軟雅黑");
			fontBox.addItem("楷體");
			fontBox.addItem("黑體");
			fontBox.setSelectedItem("Serif");
			fontBox.addItemListener(new ItemListener()
			{
				public void itemStateChanged(ItemEvent e)
				{
					name=(String)fontBox.getSelectedItem();
					nowFont=new Font(name,style,size);
					wordPanel.panelFont=nowFont;
					wordPanel.repaint();//將有關值設置為初始狀態，並且重畫
				}
			});
			p1.add(fontBox);
			con.add(p1);
			p2=new JPanel();
			l2=new JLabel("選擇字形");
			p2.add(l2);
			styleBox=new JComboBox();
			styleBox.addItem("常規");
			styleBox.addItem("加粗");
			styleBox.addItem("傾斜");
			styleBox.setSelectedItem("常規");
			styleBox.addItemListener(new ItemListener()
			{
				public void itemStateChanged(ItemEvent e)
				{
					if((String)styleBox.getSelectedItem()=="常規")
					{
						style=Font.PLAIN;
						nowFont=new Font(name,style,size);
						wordPanel.panelFont=nowFont;
						wordPanel.repaint();//將有關值設置為初始狀態，並且重畫
					}
					else if((String)styleBox.getSelectedItem()=="加粗")
					{
						style=Font.BOLD;
						nowFont=new Font(name,style,size);
						wordPanel.panelFont=nowFont;
						wordPanel.repaint();//將有關值設置為初始狀態，並且重畫
					}
					else if((String)styleBox.getSelectedItem()=="傾斜")
					{
						style=Font.ITALIC;
						nowFont=new Font(name,style,size);
						wordPanel.panelFont=nowFont;
						wordPanel.repaint();//將有關值設置為初始狀態，並且重畫
					}
				}
			});
			p2.add(styleBox);
			con.add(p2);
			p3=new JPanel();
			l3=new JLabel("選擇字號");
			p3.add(l3);
			sizeBox=new JComboBox();
			sizeBox.addItem("10");
			sizeBox.addItem("20");
			sizeBox.addItem("30");
			sizeBox.addItem("40");
			sizeBox.addItem("50");
			sizeBox.setSelectedItem("30");
			sizeBox.addItemListener(new ItemListener()
			{
				public void itemStateChanged(ItemEvent e)
				{
					String temp=(String)sizeBox.getSelectedItem();
					size=Integer.parseInt(temp);
					nowFont=new Font(name,style,size);
					wordPanel.panelFont=nowFont;
					wordPanel.repaint();//將有關值設置為初始狀態，並且重畫
				}
			});
			p3.add(sizeBox);
			con.add(p3);
			con.add(wordPanel);
			buttonPanel=new JPanel();
			ok=new JButton("確定");
			ok.addActionListener(new ActionListener()
			{
				public void actionPerformed(ActionEvent e)
				{
					f=nowFont;
					frame.setVisible(false);
					if(f.isPlain()==true)
					{
						label5.setText("當前字體:\""+name+"\",常規,"+size);
					}
					else if(f.isBold()==true)
					{
						label5.setText("當前字體:\""+name+"\",加粗,"+size);
					}
					else if(f.isItalic()==true)
					{
						label5.setText("當前字體:\""+name+"\",傾斜,"+size);
					}
				}
			});
			buttonPanel.add(ok);
			cancel=new JButton("取消");
			cancel.addActionListener(new ActionListener()
			{
				public void actionPerformed(ActionEvent e)
				{
					frame.setVisible(false);
					return;
				}
			});
			buttonPanel.add(cancel);
			con.add(buttonPanel);
			frame.setSize(450,350);
			frame.setVisible(true);
			frame.setResizable(false);
			frame.setTitle("選擇字體");
		}
	}
	class DealUndo implements ActionListener  //撤消 動作
	{
		public void actionPerformed(ActionEvent e)
		{
			if(panel.cutRecord>0)
			{
				if(lineRecords.size()!=0 && panel.cutRecord-1==((LineRecord)(lineRecords.elementAt(lineRecords.size()-1))).lastIndex)
				{
					panel.cutRecord=panel.cutRecord-((LineRecord)(lineRecords.elementAt(lineRecords.size()-1))).delta;
				}
				else
				{
					panel.cutRecord--;
				}
				repaint();//將有關值設置為初始狀態，並且重畫
				did=true;
			}
		}
	}
	class DealRecover implements ActionListener  //恢復 動作 (重做的意思)
	{
		public void actionPerformed(ActionEvent e)
		{
			if(panel.cutRecord<panel.panelElements.size())
			{
				if(lineRecords.size()!=0 && panel.cutRecord==((LineRecord)(lineRecords.elementAt(lineRecords.size()-1))).firstIndex)
				{
					panel.cutRecord=panel.cutRecord+((LineRecord)(lineRecords.elementAt(lineRecords.size()-1))).delta;
				}
				else
				{
					panel.cutRecord++;
				}
				repaint();//將有關值設置為初始狀態，並且重畫
				did=true;
			}
		}
	}
	public static void main(String args[])
	{
		new DrawGraphic();
	}
}