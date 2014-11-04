package appname.gui;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Line2D;
import java.util.ArrayList;
import appname.Util;
/**
 * Created by yusiang on 11/4/14.
 */
public class ClockPanel extends JPanel {

	public ClockPanel(){
		setBackground(Color.BLACK);
	}

    @Override
    public void paintComponent(Graphics g){//Repaint every second.
        Graphics2D g2 = (Graphics2D) g;
	    g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        super.paintComponent(g2);
	    g2.setColor(Color.WHITE);
        //this.setSize(Util.safeLongToInt(Math.round(2*size)),Util.safeLongToInt(Math.round(2*size)));
        {//Setup the clock face markings
            for(int i=0; i<60;i++){
                double theta = Util.map(i, 0, 60, 2 * Math.PI, 0);
                int startX=Util.safeLongToInt(Math.round(Util.PolarToCartesianX(theta, i % 5 == 0 ? 0.95f * size : 0.98f * size)+size));
                int startY=Util.safeLongToInt(Math.round(Util.PolarToCartesianY(theta, i % 5 == 0 ? 0.95f * size : 0.98f * size)+size));
                int endX  =Util.safeLongToInt(Math.round(Util.PolarToCartesianX(theta, size)+size));
                int endY  =Util.safeLongToInt(Math.round(Util.PolarToCartesianY(theta, size)+size));
                g2.drawLine(startX,startY,endX,endY);

                if(i%5==0){
                    int posX = Util.safeLongToInt(Math.round(Util.PolarToCartesianX(theta, 0.75*size) + -8 + size));
                    int posY = Util.safeLongToInt(Math.round(Util.PolarToCartesianY(theta, 0.75*size) +  4 + size));
                    String s= ""+(i==0?12+"":(i/5<10?" "+i/5:i/5)) ;
                    g2.drawString(s,posX ,posY );

                }
            }
            {
                //Second hand
                double secondAngle = Util.map(Util.getSecond(), 0, 60, 2 * Math.PI, 0);
                double secondX = size+ Util.PolarToCartesianX(secondAngle, size*secondHandLength);
                double secondY = size+ Util.PolarToCartesianY(secondAngle, size * secondHandLength);
                g2.setStroke(new BasicStroke(3));
                //System.out.println(secondAngle+","+Util.getSecond());
                g2.draw(new Line2D.Double(size, size, secondX, secondY));
            }
            {
                //Minute hand
                double minuteAngle = Util.map(60*Util.getMinute()+Util.getSecond(), 0, 60*60, 2 * Math.PI, 0);
                double minuteX = size+ Util.PolarToCartesianX(minuteAngle, size*minuteHandLength);
                double minuteY = size+ Util.PolarToCartesianY(minuteAngle, size*minuteHandLength);
                g2.setStroke(new BasicStroke(4));
                //System.out.println(secondAngle+","+Util.getSecond());
                g2.draw(new Line2D.Double(size, size, minuteX, minuteY));
            }
            {
                //Hour hand
                double hourAngle = Util.map(60*60*Util.getHour()+60*Util.getMinute()+Util.getSecond(), 0, 12*60*60, 2 * Math.PI, 0);
                double hourX = size+ Util.PolarToCartesianX(hourAngle, size*hourHandLength);
                double hourY = size+ Util.PolarToCartesianY(hourAngle, size*hourHandLength);
                g2.setStroke(new BasicStroke(4));
                //System.out.println(secondAngle+","+Util.getSecond());
                g2.draw(new Line2D.Double(size, size, hourX, hourY));
            }
        }


    }

    void setSize(double sz){
        size=sz;
    }

	//double size=Math.min(this.getHeight(),this.getWidth())/2;
	double size=250;
	static final double secondHandLength =7/8f;
	static double minuteHandLength =5/8f;
	static double hourHandLength   =3/8f;
}
