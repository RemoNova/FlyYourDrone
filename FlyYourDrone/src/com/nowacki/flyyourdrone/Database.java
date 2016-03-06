package com.nowacki.flyyourdrone;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.location.Location;
import android.text.format.Time;
import com.readystatesoftware.sqliteasset.SQLiteAssetHelper;

public class Database extends SQLiteAssetHelper {

	 private static final String DATABASE_NAME = "DroneDatabase";
	    private static final int DATABASE_VERSION = 1;

	    public Database(Context context) {
	        super(context, DATABASE_NAME, null, DATABASE_VERSION);  
	    }
	    
	    public Cursor getOptions() {

            SQLiteDatabase db = getReadableDatabase();
            SQLiteQueryBuilder qb = new SQLiteQueryBuilder();

            String [] columns = {"options_id","EAngle","LedAnim","MaxAltitiude","RSpeed","VSpeed"};
            String sqlTables = "options";

            qb.setTables(sqlTables);
            Cursor c = qb.query(db, columns, null, null, null, null, null);

            c.moveToFirst();
            return c;

    }
	    
	    /*public Cursor getLocationsData() {

            SQLiteDatabase db = getReadableDatabase();
            SQLiteQueryBuilder qb = new SQLiteQueryBuilder();

            String [] columns = {"coordinates_id","latitiude","longitiude","height","coordinates_or_num","flights_id"};
            String sqlTables = "coordinates";

            qb.setTables(sqlTables);
            Cursor c = qb.query(db, columns, null, null, null, null, null);

            c.moveToFirst();
            return c;
            */
	    
	    public Cursor getFlightsData() {

            SQLiteDatabase db = getReadableDatabase();
            SQLiteQueryBuilder qb = new SQLiteQueryBuilder();

            String [] columns = {"flights_id","max_height","flight_date","flight_time"};
            String sqlTables = "flights";

            qb.setTables(sqlTables);
            Cursor c = qb.query(db, columns, null, null, null, null, null);

            c.moveToFirst();
            return c;

    }
	    
	    public Cursor getFlightsData(int flightId) {

            SQLiteDatabase db = getReadableDatabase();
            SQLiteQueryBuilder qb = new SQLiteQueryBuilder();

            String [] columns = {"flights_id","max_height","flight_date","flight_time"};
            String sqlTables = "flights";

            qb.setTables(sqlTables);
            Cursor c = qb.query(db, columns, "flights_id="+flightId, null, null, null, null);

            c.moveToFirst();
            return c;

    }
	    
	    public int getLastFlightNumber() {

            SQLiteDatabase db = getReadableDatabase();
            SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
            int flightId;

            String [] columns = {"flights_id","max_height","flight_date","flight_time"};
            String sqlTables = "flights";

            qb.setTables(sqlTables);
            Cursor c = qb.query(db, columns, null, null, null, null, null);

            c.moveToLast();
            flightId = Integer.valueOf(c.getString(0));
            return flightId;

    }
	    
	    public void insertFlightsData(double maxHeight)
	    {
	    	SQLiteDatabase db = getReadableDatabase();
            
            String table = "flights";
            
            Time currentTime = new Time(Time.getCurrentTimezone()) ;
            currentTime.setToNow();
            //currentTime.format("%k:%M:%S");
            
			String dateString = String.valueOf(currentTime.monthDay)+"/"+String.valueOf(currentTime.month+1)+"/"+String.valueOf(currentTime.year);
			String timeString = String.valueOf(currentTime.hour)+":"+String.valueOf(currentTime.minute)+":"+String.valueOf(currentTime.second);
            ContentValues values = new ContentValues();
            values.put("flight_date", dateString);
            values.put("flight_time", timeString);
            values.put("max_height", maxHeight);
            
            
			db.insert(table, null, values);
	    }
	    
	    public Cursor getLocationsData(int flightId) {

            SQLiteDatabase db = getReadableDatabase();
            SQLiteQueryBuilder qb = new SQLiteQueryBuilder();

            String [] columns = {"coordinates_id","latitiude","longitiude","height","flights_id"};
            String sqlTables = "coordinates";

            qb.setTables(sqlTables);
            Cursor c = qb.query(db, columns, "flights_id="+flightId, null, null, null, null);

            c.moveToFirst();
            return c;

	    }

	    
	    public void insertLocation(Location location, int flightId, double height)
	    {
	    	SQLiteDatabase db = getReadableDatabase();
            
            String table = "coordinates";
            ContentValues values = new ContentValues();
            values.put("latitiude", String.valueOf(location.getLatitude()));
            values.put("longitiude", String.valueOf(location.getLongitude()));
            values.put("height", height);
            values.put("flights_id", flightId);
            
            
			db.insert(table, null, values);
	    }
	    
	    
	    public void updateSteeringLedAnim(boolean taken_steeringLedAnim)
	    {
	    	SQLiteDatabase db = getReadableDatabase();
	    	String table = "options";
	    	
	    	ContentValues values = new ContentValues();
	    	values.put("LedAnim", String.valueOf(taken_steeringLedAnim));
	    	db.update(table, values, "options_id=1", null);
	    }
	    
	    public void updateVeriticalSpeed(int Speed)
	    {
	    	SQLiteDatabase db = getReadableDatabase();
	    	String table = "options";
	    	
	    	ContentValues values = new ContentValues();
	    	values.put("VSpeed", Speed);
	    	db.update(table, values, "options_id=1", null);
	    }
	    
	    public void updateRotationSpeed(int Speed)
	    {
	    	SQLiteDatabase db = getReadableDatabase();
	    	String table = "options";
	    	
	    	ContentValues values = new ContentValues();
	    	values.put("RSpeed", Speed);
	    	db.update(table, values, "options_id=1", null);
	    }
	    
	    public void updateMaxAltitiude(int Altitiude)
	    {
	    	SQLiteDatabase db = getReadableDatabase();
	    	String table = "options";
	    	
	    	ContentValues values = new ContentValues();
	    	values.put("MaxAltitiude", Altitiude);
	    	db.update(table, values, "options_id=1", null);
	    }
	    
	    public void updateEulerAngle(double Euler)
	    {
	    	SQLiteDatabase db = getReadableDatabase();
	    	String table = "options";
	    	
	    	ContentValues values = new ContentValues();
	    	values.put("EAngle", Euler);
	    	db.update(table, values, "options_id=1", null);
	    }
}
