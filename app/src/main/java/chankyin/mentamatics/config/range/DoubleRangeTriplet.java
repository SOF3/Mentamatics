package chankyin.mentamatics.config.range;

public interface DoubleRangeTriplet{
	public DupletRange getHardLimit();

	public OctetRange getSoftLimit();

	public QuadretRange getValue();
}
