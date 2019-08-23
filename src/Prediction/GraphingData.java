package Prediction;

import java.awt.*;
import java.awt.font.FontRenderContext;
import java.awt.font.LineMetrics;
import java.awt.geom.*;

import javax.swing.*;
 
public class GraphingData extends JPanel {
    public double[] Pre_data;
    public double[] test;
    final int PAD = 20;
    public GraphingData(double[] pre_data,double[] _test)
    {
    	Pre_data=pre_data;
    	test=_test;
    }
    
    protected void paintComponent(Graphics g) {
    	super.paintComponent(g);
        Graphics2D g2 = (Graphics2D)g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                            RenderingHints.VALUE_ANTIALIAS_ON);
        int w = getWidth();
        int h = getHeight();
        // Draw ordinate.
        g2.draw(new Line2D.Double(PAD, PAD, PAD, h-PAD));
        // Draw abcissa.
        g2.draw(new Line2D.Double(PAD, h-PAD, w-PAD, h-PAD));
        // Draw labels.
        Font font = g2.getFont();
        FontRenderContext frc = g2.getFontRenderContext();
        LineMetrics lm = font.getLineMetrics("0", frc);
        float sh = lm.getAscent() + lm.getDescent();
        // Ordinate label.
        String s = "stock price";
        float sy = PAD + ((h - 2*PAD) - s.length()*sh)/2 + lm.getAscent();
        for(int i = 0; i < s.length(); i++) {
            String letter = String.valueOf(s.charAt(i));
            float sw = (float)font.getStringBounds(letter, frc).getWidth();
            float sx = (PAD - sw)/2;
            g2.drawString(letter, sx, sy);
            sy += sh;
        }
        // Abcissa label.
        s = "time";
        sy = h - PAD + (PAD - sh)/2 + lm.getAscent();
        float sw = (float)font.getStringBounds(s, frc).getWidth();
        float sx = (w - sw)/2;
        g2.drawString(s, sx, sy);
        // Draw lines.
        double xInc = (double)(w - 2*PAD)/(Pre_data.length-1);
        double scale = (double)(h - 2*PAD)/getMax();
        g2.setPaint(Color.green.darker());
        for(int i = 0; i < Pre_data.length-1; i++) {
            double x1 = PAD + i*xInc;
            double y1 = h - PAD - scale*Pre_data[i];
            double x2 = PAD + (i+1)*xInc;
            double y2 = h - PAD - scale*Pre_data[i+1];
            g2.setStroke(new BasicStroke(3));
            g2.draw(new Line2D.Double(x1, y1, x2, y2));
        }
        g.setFont(new Font("TimesRoman", Font.BOLD, 24));
        g2.drawString("--- Prediction data", 550, 70);
        // Mark data points.
        g2.setPaint(Color.red);
        for(int i = 0; i < test.length-1; i++) {
        	double x1 = PAD + i*xInc;
            double y1 = h - PAD - scale*test[i];
            double x2 = PAD + (i+1)*xInc;
            double y2 = h - PAD - scale*test[i+1];
            g2.setStroke(new BasicStroke(3));
            g2.draw(new Line2D.Double(x1, y1, x2, y2));
//            double x = PAD + i*xInc;
//            double y = h - PAD - scale*test[i];
//            g2.fill(new Ellipse2D.Double(x-2, y-2, 4, 4));
        }
        g2.drawString("--- Real data ", 550, 100);
        
    }
 
    private double getMax() {
    	double max = -Integer.MAX_VALUE;
        for(int i = 0; i < Pre_data.length; i++) {
            if(Pre_data[i] > max)
                max = Pre_data[i];
        }
        for(int i = 0; i < test.length; i++) {
            if(test[i] > max)
                max = test[i];
        }
        return max;
    }
 
    public void main(double[] Pre_data,double[] test) {
        JFrame f = new JFrame();
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.add(new GraphingData(Pre_data,test));
        f.setSize(800,600);
        f.setLocation(200,200);
        f.setVisible(true);
    }
}
