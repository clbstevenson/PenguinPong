package playful;

import java.util.Random;

public class JR {

	public static void main(String[] args) {
		Random r = new Random();

		int a = r.nextInt(5000);
		int b = r.nextInt(5000);
		System.out.println(a + " + " + b);
		try {
			Thread.sleep(30000);
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println(" = " + (a + b));
	}
	//738 + 3597 = 4335
	//3335

}
