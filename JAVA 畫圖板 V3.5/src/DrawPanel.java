import javax.swing.*;
import java.awt.*;
import java.awt.geom.*;
import java.util.*;

//畫圖面板類，用來畫圖
class DrawPanel extends JPanel
{
	Image image=null;
	Vector panelElements=new Vector();
	int cutRecord=0;
	public void paintComponent(Graphics g)
	{
		super.paintComponent(g);
		Graphics2D g2=(Graphics2D)g; //定義畫筆
		if(image!=null)
		{
			g2.drawImage(image,0,0,this);
		}
		int j=cutRecord<panelElements.size()?cutRecord:panelElements.size();
		System.out.println("j="+j);
		for(int i=0;i<j;i++)
		{
			((DrawElement)(panelElements.elementAt(i))).drawElement(g2);
		}
	}
}

