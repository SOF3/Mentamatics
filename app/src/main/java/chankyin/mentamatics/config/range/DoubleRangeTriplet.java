package chankyin.mentamatics.config.range;

public interface DoubleRangeTriplet{
	DupletRange getHardLimit();

	OctetRange getSoftLimit();

	QuartetRange getValue();
}
