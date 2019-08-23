package Prediction;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Arrays;
import javax.swing.JFrame;
import javax.swing.JPanel;










import org.omg.CORBA_2_3.portable.InputStream;

public class main {
	public  static void main(String[] args) throws IOException 
	{
		double[] x0;
		double[][]bounds;
		double[] train,test,dataset;
		BufferedReader reader = new BufferedReader(new FileReader("C:/Users/HCJ/workspace/timeseries_prediction/src/Prediction/data.csv"));
		String line;
		double[] Adjust=new double[0];
		line=reader.readLine();
		while ((line=reader.readLine()) != null) {
			String[] fields = line.split(",");
			Adjust = Arrays.copyOf(Adjust, Adjust.length + 1);
			Adjust[Adjust.length-1]=Double.parseDouble(fields[fields.length-1]); 
			
		}
		reader.close();
		x0=new double[5];
		bounds=new double[x0.length][];
		for (int i=0;i<x0.length;i++)
		{
			x0[i]=0.1;
			bounds[i]=new double[2];
			bounds[i][0]=-10;
			bounds[i][1]= 10;
		}
		dataset=Adjust;
		int L=(int)(dataset.length*0.7);
		int L1=dataset.length-L;
		train=new double[L];
		test=new double[L1];
		for (int i=0;i<L;i++)
		{
			train[i]=dataset[i];
		}
		for (int i=0;i<L1;i++)
		{
			test[i]=dataset[i+L];
		}
		int num_particles=15, maxiter=150;
		int num_dimensions=x0.length;
		double err_best_g=-1;
		double[] pos_best_g=new double[num_dimensions];
		Particle[] swarm=new Particle[num_particles];
		for (int i=0;i<swarm.length;i++)
		{
			swarm[i]=new Particle(x0, num_dimensions, train);
		}
		int i=0;
		while(i<maxiter)
		{
			for(int j=0;j<num_particles;j++){
				swarm[j].evaluate();
				if(swarm[j].err_i<err_best_g || err_best_g==-1)
				{
					for (int k=0;k<pos_best_g.length;k++)
						pos_best_g[k]=swarm[j].position_i[k];
					err_best_g=swarm[j].err_i;
				}
			}
			for(int j=0;j<num_particles;j++){
				swarm[j].update_velocity(pos_best_g);
				swarm[j].update_position(bounds);
				
			}
			
			i++;
			System.out.println(err_best_g);
		}
		System.out.println(err_best_g);
		System.out.println(pos_best_g);
		double[] testPredictPlot=predic_psoarima(pos_best_g,L1,L-1,dataset);
		double s=0.0,s1=0;
		for (int t=0;t<L1;t++)
		{
			s+=Math.abs(test[t]-testPredictPlot[t]);
			s1+=Math.pow(test[t]-testPredictPlot[t], 2);
		}
		double RMSE=Math.sqrt(s1/L1);
		double MAE=s/L1;
		double MAPE=s/L1*100;
		System.out.println("Error:");
		System.out.println("RMSE is "+RMSE);
		System.out.println("MAP is "+MAE);
		System.out.println("MAPE is "+MAPE);
		GraphingData Draw=new GraphingData(testPredictPlot,test);
		Draw.main(testPredictPlot,test);
	}
	
	public static double[] predic_psoarima(double[] coe, int num,int no,double[] dataset)
	{
		double[] z=new double[0];
		
		for(int i=0;i<num;i++)
		{
			double val=0;
			val=dataset[no-2+i]*coe[0]+dataset[no-2+i]*coe[1]+coe[2];
			if(i>1)
				val=val+(z[i-2]-dataset[no-2+i])*coe[3]+(z[i-1]-dataset[no-1+i])*coe[4];
			z = Arrays.copyOf(z, z.length + 1);
			z[z.length-1]=val;
		}
		return z;
	}

}
