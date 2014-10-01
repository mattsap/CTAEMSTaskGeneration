package project;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;


public class GaussianGenerator {

	
	public static List<Float> generate(int count) {
		List<Float> ret = new ArrayList<Float>(count);
		Random r = new Random();
		
		for (int i = 0; i < count; i++) {
			float x1, x2, w, y1, y2;
			 
	        do {
	                x1 = 2.0f * r.nextFloat() - 1.0f;
	                x2 = 2.0f * r.nextFloat() - 1.0f;
	                w = x1 * x1 + x2 * x2;
	        } while ( w >= 1.0 );
	
	        w = (float) Math.sqrt( (-2.0f * Math.log( w ) ) / w );
	        y1 = x1 * w;
	        y2 = x2 * w;
	        
	        ret.add( (y1));
	        i++;
	        if (i >= count) break;
	        ret.add( (y2));
		}
		return ret;
	}
}
