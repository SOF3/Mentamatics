package chankyin.mentamatics.config;

import android.content.Context;
import android.content.res.XmlResourceParser;
import android.support.annotation.StringRes;
import chankyin.mentamatics.Main;
import chankyin.mentamatics.R;
import lombok.SneakyThrows;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;

import static org.xmlpull.v1.XmlPullParser.*;

public class ConfigParser{
	private final Context context;
	private XmlResourceParser parser;

	private ConfigEntries entries;
	private ConfigGroup currentGroup = null;
	private ConfigEntry.Builder currentEntryBuilder = null;

	private BuildState currentState = BuildState.NOT_BUILDING;

	@SneakyThrows(XmlPullParserException.class)
	public static ConfigEntries parse(Context context){
		return new ConfigParser(context).doParse();
	}

	public ConfigParser(Context context){
		this.context = context;
	}

	@SneakyThrows(IOException.class)
	public ConfigEntries doParse() throws XmlPullParserException{
		parser = context.getResources().getXml(R.xml.config_entries);
		entries = new ConfigEntries();

		while(true){
			if(!handleEvent(parser.getEventType())){
				break;
			}
			parser.next();
		}

		parser.close();

		return entries;
	}

	private boolean handleEvent(int eventType) throws XmlPullParserException{
		switch(eventType){
			case START_DOCUMENT:
				return true;
			case START_TAG:
				return startTag();
			case END_TAG:
				if("EntryType".equals(parser.getName()) ||
						"EntrySummary".equals(parser.getName()) ||
						"EntrySummaryPositive".equals(parser.getName()) ||
						"DefaultValue".equals(parser.getName())){
					currentState = BuildState.NOT_BUILDING;
					return true;
				}
				if("ConfigEntry".equals(parser.getName())){
					currentState = BuildState.NOT_BUILDING;
					ConfigEntry entry = currentEntryBuilder.build();
					currentGroup.addChild(entry);
					currentEntryBuilder = null;
					return true;
				}
				if("ConfigGroup".equals(parser.getName())){
					currentGroup = currentGroup.parent;
					return true;
				}
				if(!"ConfigEntries".equals(parser.getName())){
					throw new XmlPullParserException("Wrong end tag: " + parser.getName());
				}
			case END_DOCUMENT:
				return false;
			case TEXT:
				if(currentState == BuildState.ENTRY_TYPE){
					readEntryTypeText();
					return true;
				}
				if(currentState == BuildState.ENTRY_SUMMARY){
					readEntrySummaryText(false);
					return true;
				}
				if(currentState == BuildState.ENTRY_SUMMARY_POSITIVE){
					readEntrySummaryText(true);
				}
				if(currentState == BuildState.DEFAULT_VALUE){
					readDefaultValueText();
					return true;
				}
				return true;
		}
		throw new AssertionError();
	}

	private boolean startTag() throws XmlPullParserException{
		if("ConfigEntries".equals(parser.getName())){
			startConfigEntriesTag();
			return true;
		}
		if("ConfigGroup".equals(parser.getName())){
			startConfigGroupTag();
			return true;
		}
		if("ConfigEntry".equals(parser.getName())){
			startConfigEntryTag();
			return true;
		}
		if("EntryType".equals(parser.getName())){
			if(currentEntryBuilder != null){
				currentState = BuildState.ENTRY_TYPE;
			}
			return true;
		}
		if("EntrySummary".equals(parser.getName())){
			if(currentEntryBuilder != null){
				currentState = BuildState.ENTRY_SUMMARY;
			}
			return true;
		}
		if("EntrySummaryPositive".equals(parser.getName())){
			if(currentEntryBuilder != null){
				currentState = BuildState.ENTRY_SUMMARY_POSITIVE;
			}
			return true;
		}
		if("DefaultValue".equals(parser.getName())){
			if(currentEntryBuilder != null){
				currentState = BuildState.DEFAULT_VALUE;
			}
			return true;
		}
		throw new XmlPullParserException("Unknown tag");
	}

	private void startConfigEntriesTag(){
		currentGroup = entries;
	}

	private void startConfigGroupTag() throws XmlPullParserException{
		if(currentGroup == null){
			throw new XmlPullParserException("Wrong base tag");
		}
		String groupId = parser.getAttributeValue(null, "id");
		@StringRes int nameResId = Main.getStringIdentifier(context, parser.getAttributeValue(null, "name"));
		boolean header = parser.getAttributeBooleanValue(null, "header", false);
		ConfigGroup group = new ConfigGroup(groupId, nameResId, currentGroup, header);
		currentGroup.addChild(group);
		currentGroup = group;
	}

	private void startConfigEntryTag() throws XmlPullParserException{
		if(currentGroup == null){
			throw new XmlPullParserException("Wrong base tag");
		}
		String entryId = parser.getAttributeValue(null, "id");
		@StringRes int nameResId = Main.getStringIdentifier(context, parser.getAttributeValue(null, "name"));

		boolean groupToggle = parser.getAttributeBooleanValue(null, "groupToggle", false);

		currentEntryBuilder = ConfigEntry.build(entryId, nameResId, currentGroup);
		if(groupToggle){
			currentEntryBuilder.groupToggle(true);
		}
	}

	private void readEntryTypeText() throws XmlPullParserException{
		String typeName = parser.getText();
		String[] query;
		int index = typeName.indexOf('?');
		if(index != -1){
			query = typeName.substring(index + 1).split(",");
			typeName = typeName.substring(0, index);
		}else{
			query = new String[0];
		}
		ConfigEntryType type = ConfigEntryType.valueOf(typeName);
		if(type == null){
			throw new XmlPullParserException("Unknown element type " + typeName);
		}
		currentEntryBuilder.type(type).typeArgs(query);
	}

	private void readEntrySummaryText(boolean positive){
		@StringRes int summaryResId = Main.getStringIdentifier(context, parser.getText());
		if(positive){
			currentEntryBuilder.summaryPositive(summaryResId);
		}else{
			currentEntryBuilder.summary(summaryResId);
		}
	}

	private void readDefaultValueText(){
		currentEntryBuilder.defaultValue(parser.getText());
	}

	enum BuildState{
		NOT_BUILDING,
		ENTRY_TYPE,
		ENTRY_SUMMARY,
		ENTRY_SUMMARY_POSITIVE,
		DEFAULT_VALUE
	}
}
