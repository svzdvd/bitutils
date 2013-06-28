package it.yobibit;


public class Bits {

	public enum BitListSize {
		
		Size1(1),
		Size2(2),
		Size4(4);
		
		int size;
		int max;
		
		BitListSize(int size) {
			this.size = size;
			this.max = ((int) Math.pow(2, size)) - 1;
		}
		
		int get() {
			return size;
		}
		
		int max() {
			return max;
		}
	}

	public static int get(int bits, int pos) {
		return bits & (1 << pos);
	}	
	
	public static int set(int bits, int pos) {
		return bits | (1 << pos);
	}
	
	public static String asString(int bits) {
		StringBuilder result = new StringBuilder();
		for (int i = 0; i < 32; i++) {
			if (get(bits, i) != 0) result.append("1");
			else result.append("0");
		}
		return result.toString();
	}
}