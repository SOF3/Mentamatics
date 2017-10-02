package chankyin.mentamatics.config.range;

public interface DoubleRangeTriplet{
	DupletRange getHardLimit();

	OctetRange getSoftLimit();

	QuadretRange getValue();
}
