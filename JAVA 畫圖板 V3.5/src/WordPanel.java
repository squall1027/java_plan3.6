import java.awt.*;
import javax.swing.*;

//字體功能
class WordPanel extends JPanel
{
	Font panelFont=new Font("Serif",Font.PLAIN,30);
	public void paintComponent(Graphics g)
	{
		super.paintComponent(g);
		Graphics2D g2=(Graphics2D)g;
		g2.setFont(panelFont);
		g2.drawString("預覽文字: Example 範例",50,50);  //預覽文字
	}
}