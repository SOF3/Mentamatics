package chankyin.mentamatics.config.old;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

@Builder
@Getter
@FieldDefaults(level = AccessLevel.PRIVATE)
@Deprecated
public final class ConfigOld{
/*
	final SharedPreferences pref;

	boolean verticalAlign;

	boolean additionEnabled;
	boolean additionCarryAllowed;
	int additionUpperMinDigits;
	int additionUpperMaxDigits;
	int additionLowerMinDigits;
	int additionLowerMaxDigits;

	boolean subtractionEnabled;
	boolean subtractionBorrowAllowed;
	boolean subtractionNegativeAllowed;
	int subtractionUpperMinDigits;
	int subtractionUpperMaxDigits;
	int subtractionLowerMinDigits;
	int subtractionLowerMaxDigits;

	boolean multiplicationEnabled;

	boolean divisionEnabled;

	public void setVerticalAlign(boolean verticalAlign){
		this.verticalAlign = verticalAlign;
		editBoolean("verticalAlign", verticalAlign);
	}

	public void setAdditionEnabled(boolean additionEnabled){
		this.additionEnabled = additionEnabled;
		editBoolean("additionEnabled", additionEnabled);
	}

	public void setAdditionCarryAllowed(boolean additionCarryAllowed){
		this.additionCarryAllowed = additionCarryAllowed;
		editBoolean("additionCarryAllowed", additionCarryAllowed);
	}

	public void setAdditionUpperMinDigits(int additionUpperMinDigits){
		this.additionUpperMinDigits = additionUpperMinDigits;
		editInt("additionUpperMinDigits", additionUpperMinDigits);
	}
	public void setAdditionUpperMaxDigits(int additionUpperMaxDigits){
		this.additionUpperMaxDigits = additionUpperMaxDigits;
		editInt("additionUpperMaxDigits", additionUpperMaxDigits);
	}

	public void setAdditionLowerMinDigits(int additionLowerMinDigits){
		this.additionLowerMinDigits = additionLowerMinDigits;
		editInt("additionLowerMinDigits", additionLowerMinDigits);
	}

	public void setAdditionLowerMaxDigits(int additionLowerMaxDigits){
		this.additionLowerMaxDigits = additionLowerMaxDigits;
		editInt("additionLowerMaxDigits", additionLowerMaxDigits);
	}

	public void setSubtractionEnabled(boolean subtractionEnabled){
		this.subtractionEnabled = subtractionEnabled;
		editBoolean("subtractionEnabled", subtractionEnabled);
	}

	public void setSubtractionBorrowAllowed(boolean subtractionBorrowAllowed){
		this.subtractionBorrowAllowed = subtractionBorrowAllowed;
		editBoolean("subtractionBorrowAllowed", subtractionBorrowAllowed);
	}

	public void setSubtractionNegativeAllowed(boolean subtractionNegativeAllowed) throws ConfigException{
		if(getSubtractionUpperMinDigits() < getSubtractionLowerMinDigits() && !subtractionNegativeAllowed){

		}
		this.subtractionNegativeAllowed = subtractionNegativeAllowed;
		editBoolean("subtractionNegativeAllowed", subtractionNegativeAllowed);
	}

	public void setSubtractionUpperMinDigits(int subtractionUpperMinDigits) throws ConfigException{
		if(subtractionUpperMinDigits <= 0){
			throw new ConfigException();
		}
		if(!isSubtractionNegativeAllowed() && subtractionUpperMinDigits < getSubtractionLowerMinDigits()){
			throw new ConfigException();
		}

		this.subtractionUpperMinDigits = subtractionUpperMinDigits;
		editInt("subtractionUpperMinDigits", subtractionUpperMinDigits);
	}
	public void setSubtractionUpperMaxDigits(int subtractionUpperMaxDigits) throws ConfigException{
		if(subtractionUpperMaxDigits <= 0){
			throw new ConfigException();
		}
		if(!isSubtractionNegativeAllowed() && subtractionUpperMaxDigits < getSubtractionLowerMaxDigits()){
			throw new ConfigException();
		}

		this.subtractionUpperMaxDigits = subtractionUpperMaxDigits;
		editInt("subtractionUpperMaxDigits", subtractionUpperMaxDigits);
	}

	public void setSubtractionLowerMaxDigits(int subtractionLowerMaxDigits) throws ConfigException{
		if(subtractionLowerMaxDigits < 0){
			throw new ConfigException();
		}
		if(!isSubtractionNegativeAllowed() && subtractionLowerMaxDigits > getSubtractionUpperMaxDigits()){
			throw new ConfigException();
		}

		this.subtractionLowerMaxDigits = subtractionLowerMaxDigits;
		editInt("subtractionLowerMaxDigits", subtractionLowerMaxDigits);
	}

	public void setMultiplicationEnabled(boolean multiplicationEnabled){
		this.multiplicationEnabled = multiplicationEnabled;
		editBoolean("multiplicationEnabled", multiplicationEnabled);
	}

	public void setDivisionEnabled(boolean divisionEnabled){
		this.divisionEnabled = divisionEnabled;
		editBoolean("divisionEnabled", divisionEnabled);
	}

	private void editBoolean(String key, boolean bool){
		pref.edit()
				.putBoolean(key, bool)
				.apply();
	}

	private void editInt(String key, int i){
		pref.edit()
				.putInt(key, i)
				.apply();
	}

	public static ConfigOld create(Context ctx){
		SharedPreferences pref = ctx.getSharedPreferences(Main.PREF_SETTINGS, Context.MODE_PRIVATE);
		return ConfigOld.builder()

				.verticalAlign(pref.getBoolean("verticalAlign", false))

				.additionEnabled(pref.getBoolean("additionEnabled", true))
				.additionCarryAllowed(pref.getBoolean("additionCarryAllowed", true))
				.additionUpperMinDigits(pref.getInt("additionUpperMinDigits", 3))
				.additionUpperMaxDigits(pref.getInt("additionUpperMaxDigits", 3))
				.additionLowerMinDigits(pref.getInt("additionLowerMinDigits", 3))
				.additionLowerMaxDigits(pref.getInt("additionLowerMaxDigits", 3))

				.subtractionEnabled(pref.getBoolean("subtractionEnabled", true))
				.subtractionBorrowAllowed(pref.getBoolean("subtractionBorrowAllowed", true))
				.subtractionNegativeAllowed(pref.getBoolean("subtractionNegativeAllowed", false))
				.subtractionUpperDigits(pref.getInt("subtractionUpperMinDigits", 3))
				.subtractionLowerDigits(pref.getInt("subtractionLowerMinDigits", 2))

				.multiplicationEnabled(pref.getBoolean("multiplicationEnabled", true))

				.divisionEnabled(pref.getBoolean("divisionEnabled", true))

				.build();
	}
*/
}
