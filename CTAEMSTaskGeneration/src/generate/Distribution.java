package generate;

public class Distribution {

	public static double Normal (double mean, double stddev, double value)
	{
	    return ((1.0 / (stddev * Math.sqrt(2.0 * Math.PI))) *
	            Math.pow(Math.E, ((- (value - mean) * (value - mean)) /
	            (2.0 * stddev * stddev))));
	}
	
	public static double Exponential (double origin, double sigma6,
            double value)
	{
		double b = Math.abs((sigma6 - origin) / 6.0);
		double answer = 0.0;
		answer = (1.0 / b) * Math.pow (Math.E,  - (value - origin) / b);
		return (answer);
	}
	
	public static double Laplace (double mean, double stddev, double value)
	{
	    double x = Math.abs (value - mean);
	    double b = stddev;
	    return ((1.0 / (2.0 * b)) * Math.pow(Math.E, - x / b));
	}
	
	public static double Poisson (double lambda, double k) {
		double lk = Math.pow(lambda, k);
		double enl = Math.pow(Math.E, -lambda);
		double kf = 1;
		for (int i = 1; i <= k; i++) {
		   kf *= i;
		}
		return lk*enl / kf;
	}
	
	public static double UniformRandom(double min, double max) {
		return (Math.random() * (max - min)) + min;
	}
}
