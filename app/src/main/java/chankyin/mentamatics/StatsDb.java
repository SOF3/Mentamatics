package chankyin.mentamatics;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import lombok.Value;

public class StatsDb extends SQLiteOpenHelper{
	private final static int DB_VERSION = 1;
	private final static String QUERY_CREATE = "CREATE TABLE answers (duration REAL, completion REAL, type INTEGER, flags INTEGER)";

	private final SQLiteDatabase db = getWritableDatabase();

	StatsDb(Context ctx){
		super(ctx, "stats", null, DB_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db){
		db.execSQL(QUERY_CREATE);
	}

	@Override
	public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1){

	}

	public void resetData(){
		db.delete("answers", null, new String[0]);
	}

	public void resetData(int since){
		db.delete("answers", "completion < ?", new String[]{Integer.toString(since)});
	}

	public void incrementCorrect(long duration, int type, int flags){
		ContentValues values = new ContentValues(4);
		values.put("duration", duration);
		values.put("completion", System.currentTimeMillis());
		values.put("type", type);
		values.put("flags", flags);
		db.insert("answers", null, values);
	}

	@Value
	public static class StatSet{
		int count;
		double sum;

		public double getAverage(){
			return sum / count;
		}
	}

	public StatSet getLastStats(int lastN){
		Cursor cursor = db.query("answers", new String[]{
						"SUM(duration)",
						"COUNT(*)",
				}, "(SELECT COUNT(*) FROM answers a2 WHERE a2.completion > answers.completion) < ?",
				new String[]{Integer.toString(lastN)}, null, null, "completion DESC");
		int count = 0;
		double sum = 0;
		while(cursor.moveToNext()){
			sum = cursor.getDouble(0);
			count = cursor.getInt(1);
		}
		cursor.close();
		return new StatSet(count, sum);
	}

	public StatSet getStats(){
		Cursor cursor = db.query("answers", new String[]{
						"SUM(duration)",
						"COUNT(*)",
				}, null, null, null, null, "completion DESC");
		int count = 0;
		double sum = 0;
		while(cursor.moveToNext()){
			sum = cursor.getDouble(0);
			count = cursor.getInt(1);
		}
		cursor.close();
		return new StatSet(count, sum);
	}

	public static int numberToFlag(int number, int flag){
		int lowest = -1, highest = 32;
		for(int i = 0; i < 32; ++i){
			if((flag & (1 << i)) != 0){ // bit set
				if(lowest == -1){
					lowest = i;
				}
			}else{ // bit unset
				if(lowest != -1){
					highest = i;
					break;
				}
			}
		}
		int cap = 1 << (highest - lowest);
		if(number >= cap){
			number = cap - 1;
		}
		return number << lowest;
	}
}
