package chankyin.mentamatics.config.ui;

import lombok.EqualsAndHashCode;

@EqualsAndHashCode
public class Quadret{
	public int upperMin;
	public int upperMax;
	public int lowerMin;
	public int lowerMax;

	public Quadret(int[] array){
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
}
