package chankyin.mentamatics.config;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.preference.PreferenceScreen;
import android.widget.Toast;
import chankyin.mentamatics.Main;
import chankyin.mentamatics.R;
import chankyin.mentamatics.config.range.QuadretRange;

import static chankyin.mentamatics.TestUtils.debug;

public class Config{
	private final ConfigEntries entries;
	private final Context context;
	private final SharedPreferences prefs;

	public Config(ConfigEntries entries, Context context){
		this.entries = entries;
		this.context = context;
		prefs = PreferenceManager.getDefaultSharedPreferences(context);
	}

	public PreferenceScreen toPrefScreen(final PreferenceFragment fragment){
		PreferenceScreen screen = fragment.getPreferenceManager().createPreferenceScreen(fragment.getActivity());
		fragment.setPreferenceScreen(screen);
		for(ConfigElement element : entries.getChildren().values()){
			element.addPreferenceTo(fragment, screen, prefs);
		}
		Preference pref = new Preference(fragment.getActivity());
		pref.setKey("resetStats");
		pref.setTitle(R.string.cfg_special_resetStats);
		pref.setSummary(R.string.cfg_special_resetStats_summary);
		pref.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener(){
			@Override
			public boolean onPreferenceClick(Preference preference){
				new AlertDialog.Builder(fragment.getActivity())
						.setTitle(R.string.cfgmsg_special_resetStats_title)
						.setMessage(R.string.cfgmsg_special_resetStats_message)
						.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener(){
							@Override
							public void onClick(DialogInterface dialog, int which){
								Main.getInstance(fragment.getActivity()).getStatsDb().resetData(); // TODO resetData(since)
								Toast.makeText(fragment.getActivity(), R.string.cfgmsg_special_resetStats_completion, Toast.LENGTH_SHORT).show();
							}
						})
						.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener(){
							@Override
							public void onClick(DialogInterface dialog, int which){
								Toast.makeText(fragment.getActivity(), R.string.cfgmsg_special_resetStats_cancelled, Toast.LENGTH_SHORT).show();
							}
						})
						.show();
				return true;
			}
		});
		screen.addPreference(pref);

		return screen;
	}

	public boolean getBoolean(String key){
		ConfigEntry entry = entries.getEntry(key);
		if(entry.type != ConfigEntryType.bool){
			throw new ClassCastException();
		}
		return prefs.getBoolean("Config:" + key, (boolean) entry.defaultValue);
	}

	public int getInt(String key){
		ConfigEntry entry = entries.getEntry(key);
		if(entry.type != ConfigEntryType.integer){
			throw new ClassCastException();
		}
		return prefs.getInt("Config:" + key, (int) entry.defaultValue);
	}

	public int[] getIntRange(String key){
		ConfigEntry entry = entries.getEntry(key);
		if(entry.type != ConfigEntryType.integerRange){
			throw new ClassCastException();
		}
		String out = prefs.getString("Config:" + key, null);
		if(out == null){
			return (int[]) entry.defaultValue;
		}else{
			return (int[]) ConfigEntryType.integerRange.fromString(out);
		}
	}

	public QuadretRange getIntDoubleRange(String key){
		ConfigEntry entry = entries.getEntry(key);
		if(entry.type != ConfigEntryType.integerDoubleRange){
			throw new ClassCastException("Expected integerDoubleRange for " + key + ", got " + entry.type.name());
		}
		String rangeString = prefs.getString("Config:" + key, null);
		debug("getIntDoubleRange: rangeString = %s", rangeString);
		if(rangeString == null){
			return (QuadretRange) entry.defaultValue;
		}else{
			return (QuadretRange) ConfigEntryType.integerDoubleRange.fromString(rangeString);
		}
	}
}
