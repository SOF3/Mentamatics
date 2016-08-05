package chankyin.mentamatics.config.range;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode
@AllArgsConstructor
public class OctetRange{
	public int upperMinMin;
	public int upperMinMax;
	public int upperMaxMin;
	public int upperMaxMax;

	public int lowerMinMin;
	public int lowerMinMax;
	public int lowerMaxMin;
	public int lowerMaxMax;
}
