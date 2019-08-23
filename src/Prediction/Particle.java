package Prediction;

import java.util.Arrays;

public class Particle {
	public double[] position_i;
	public double[] velocity_i;
	public double[] post_best_i;
	public double err_best_i=-1;
	public double err_i=-1;
	public double[] train;
	public Particle(double[] x0, int num_dimensions,double[] _train){
		position_i=new double[0];
		velocity_i=new double[0];
		train=_train;
		for (int i=0;i<num_dimensions;i++){
			velocity_i = Arrays.copyOf(velocity_i, velocity_i.length + 1);
			double val=Math.random();
			val=(val-0.5)*2;
			velocity_i[velocity_i.length-1]=val;
			position_i = Arrays.copyOf(position_i, position_i.length + 1);
			position_i[position_i.length-1]=x0[i];
		}
		post_best_i=new double[position_i.length];
	}
	public  double CostFunction(double[] coe){
		double sum=0;
		int n=train.length;
		double[] tdata=new double[0];
		for(int i=0;i<n-2;i++)
		{
			double val=train[i]*coe[0]+train[i+1]*coe[1]+coe[2];
			if(i>1)
				val=val+(tdata[i-2]-train[i])*coe[3]+(tdata[i-1]-train[i+1])*coe[4];
			tdata = Arrays.copyOf(tdata, tdata.length + 1);
			tdata[tdata.length-1]=val;
			sum=sum+(tdata[i]-train[i+2])*(tdata[i]-train[i+2])/(n-2);
		}
		return sum;
	}
	public void evaluate(){
		err_i=CostFunction(position_i);
		if (err_i < err_best_i || err_best_i == -1){
			for(int i=0;i<post_best_i.length;i++){
				post_best_i[i] = position_i[i];
			}
			err_best_i = err_i;
		}
	}
	public void update_velocity(double[] post_best_g){
		double w=0.5,c1=1,c2=2;
		for(int i=0;i< post_best_i.length;i++)
		{
			double r1=Math.random();
			double r2=Math.random();
			double vel_cognitive=c1*r1*(post_best_i[i]-position_i[i]);
			double vel_social=c2*r2*(post_best_g[i]-position_i[i]);
			velocity_i[i]=w*velocity_i[i]+vel_cognitive+vel_social;
		}
		
	}
	public void update_position(double[][] bounds){
		
		for(int i=0;i< position_i.length;i++)
		{
			position_i[i]+=velocity_i[i];
			if(position_i[i]>bounds[i][1])
				position_i[i]=bounds[i][1];
			if(position_i[i]<bounds[i][0])
				position_i[i]=bounds[i][0];
		}
		
	}
}
