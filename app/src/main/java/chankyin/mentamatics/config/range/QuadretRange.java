package chankyin.mentamatics.config.range;

import lombok.EqualsAndHashCode;

@EqualsAndHashCode
public class QuadretRange{
	public int upperMin;
	public int upperMax;
	public int lowerMin;
	public int lowerMax;

	public QuadretRange(int[] array){
		upperMin = array[0];
		upperMax = array[1];
		lowerMin = array[2];
		lowerMax = array[3];
	}

	public int[] toArray(){
		return new int[]{
				upperMin,
				upperMax,
				lowerMin,
				lowerMax
		};
	}

	@Override
	public String toString(){
		return upperMin + "," + upperMax + ";" + lowerMin + "," + lowerMax;
	}
}
