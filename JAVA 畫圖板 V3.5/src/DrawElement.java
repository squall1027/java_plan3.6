import java.awt.*;
import java.awt.geom.*;
import java.io.Serializable;
//繪製元素
class DrawElement implements Serializable 
{
	boolean fillOrNot=false;
	int firstX,firstY,lastX,lastY;
	Color color=Color.BLACK;
	Shape element;
	boolean word=false;
	String string=""; //用來存放當前字體
	Font font=new Font("Serif",Font.PLAIN,30);
	Float s=1f; //設置畫筆粗細，默認值為1F
	//直線
	public DrawElement(int firstx,int firsty,int lastx,int lasty,Color penColor,Float rough)
	{
		word=false;
		firstX=firstx;
		firstY=firsty;
		lastX=lastx;
		lastY=lasty;
		Line2D line=new Line2D.Double(firstX,firstY,lastX,lastY);
		element=line;
		color=penColor;
		s=rough;
	}
	//矩形
	public DrawElement(int firstx,int firsty,int lastx,int lasty,boolean fill,Color penColor,Float rough)
	{
		word=false;
		firstX=firstx;
		firstY=firsty;
		lastX=lastx;
		lastY=lasty;
		Rectangle2D rect=new Rectangle2D.Double(Math.min(firstX,lastX),Math.min(firstY,lastY),Math.abs(lastX-firstX),Math.abs(lastY-firstY));
		element=rect;
		fillOrNot=fill;
		color=penColor;
		s=rough;
	}
	//橢圓
	public DrawElement(Point2D point,int lastx,int lasty,boolean fill,Color penColor,Float rough)
	{
		word=false;
		firstX=(int)point.getX();
		firstY=(int)point.getY();
		lastX=lastx;
		lastY=lasty;
		Rectangle2D rect=new Rectangle2D.Double(Math.min(firstX,lastX),Math.min(firstY,lastY),Math.abs(lastX-firstX),Math.abs(lastY-firstY));
		Ellipse2D ellipse=new Ellipse2D.Double();
		ellipse.setFrame(rect);
		element=ellipse;
		fillOrNot=fill;
		color=penColor;
		s=rough;
	}
	//文字
	public DrawElement(String str,int x,int y,Color penColor,Font f)
	{
		word=true;
		firstX=x;
		firstY=y;
		string=str;
		color=penColor;
		font=f;
	}
	public void drawElement(Graphics2D g2)
	{
		g2.setColor(color);
		if(word==false)
		{
			if(fillOrNot==false)
			{

				if(s==1f)
				{
					g2.setStroke(new BasicStroke(1f));
				}
				else if(s==4f)
				{
					g2.setStroke(new BasicStroke(4f));
				}
				else if(s==8f)
				{
					g2.setStroke(new BasicStroke(8f));
				}
				g2.draw(element);
			}
			else if(fillOrNot==true)
			{
				g2.fill(element);
			}
		}
		else if(word==true)
		{
			g2.setFont(font);
			g2.drawString(string,firstX,firstY);
		}
	}
}
