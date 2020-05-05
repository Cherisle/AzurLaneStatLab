package com.cherisle.azurlanestatlab;

import android.content.ContentValues;
import android.content.Context;
import android.content.res.AssetManager;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteCantOpenDatabaseException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class AzurLaneDbHelper extends SQLiteOpenHelper {
    // If you change the database schema, you must increment the database version.
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "AzurLaneDB";
    private static final String TABLE_NAME = "Ships";

    public AzurLaneDbHelper(Context context)
    {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    public void onCreate(SQLiteDatabase db)
    {
        String CREATION_TABLE = "CREATE TABLE Ships ( "
                + "id INTEGER PRIMARY KEY AUTOINCREMENT, " + "name TEXT, "
                + "association TEXT, " + "classification TEXT, "
                + "rarity TEXT, " + "level TEXT, " + "chibi TEXT, "
                + "health INTEGER DEFAULT 0, " + "armor INTEGER DEFAULT 0, "
                + "reload INTEGER DEFAULT 0, " + "luck INTEGER DEFAULT 0, "
                + "firepower INTEGER DEFAULT 0, " + "torpedo INTEGER DEFAULT 0, "
                + "evasion INTEGER DEFAULT 0, " + "speed INTEGER DEFAULT 0, "
                + "antiair INTEGER DEFAULT 0, " + "aviation INTEGER DEFAULT 0, "
                + "oilconsumption INTEGER DEFAULT 0, " + "accuracy INTEGER DEFAULT 0, "
                + "antisubmarine INTEGER DEFAULT 0)";

        db.execSQL(CREATION_TABLE);
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // This database is only a cache for online data, so its upgrade policy is
        // to simply to discard the data and start over
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
    }

    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }

    public void writeCsvToDb(String csv_file, Context context)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        AssetManager manager = context.getAssets();
        InputStream inStream = null;
        try
        {
            inStream = manager.open(csv_file);
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }

        BufferedReader buffer = new BufferedReader(new InputStreamReader(inStream));
        String line = "";
        db.beginTransaction();
        try
        {
            while ((line = buffer.readLine()) != null)
            {
                Log.i("ALSL LOG","log:" + line);
                String[] col = line.split(",");
                ContentValues cv = new ContentValues(col.length);
                if(col.length == 0)
                {
                    //empty row
                    Log.i("ALSL LOG","log: EMPTY ROW READ");
                }
                else if(col.length < 6)
                {
                    Log.i("ALSL LOG","log: incomplete ship stats read");
                    cv.put("name", col[0].trim());
                    cv.put("association", col[1].trim());
                    cv.put("classification", col[2].trim());
                    cv.put("rarity", col[3].trim());
                    cv.put("level", col[4].trim());
                    cv.put("chibi", "");
                    db.insert("Ships", null, cv);
                }
                else if(col.length < 7)
                {
                    Log.i("ALSL LOG","log: incomplete ship stats read");
                    cv.put("name", col[0].trim());
                    cv.put("association", col[1].trim());
                    cv.put("classification", col[2].trim());
                    cv.put("rarity", col[3].trim());
                    cv.put("level", col[4].trim());
                    cv.put("chibi", col[5].trim());
                    db.insert("Ships", null, cv);
                }
                else
                {
                    cv.put("name", col[0].trim());
                    cv.put("association", col[1].trim());
                    cv.put("classification", col[2].trim());
                    cv.put("rarity", col[3].trim());
                    cv.put("level", col[4].replace("Level ","").trim());
                    cv.put("chibi", col[5].trim());

                    cv.put("health", col[6].trim());
                    cv.put("armor", col[7].trim());
                    cv.put("reload", col[8].trim());
                    cv.put("luck", col[9].trim());
                    cv.put("firepower", col[10].trim());
                    cv.put("torpedo", col[11].trim());
                    cv.put("evasion", col[12].trim());
                    cv.put("speed", col[13].trim());
                    cv.put("antiair", col[14].trim());
                    cv.put("aviation", col[15].trim());
                    cv.put("oilconsumption", col[16].trim());
                    cv.put("accuracy", col[17].trim());
                    cv.put("antisubmarine", col[18].trim());
                    db.insert("Ships", null, cv);
                }
            }
        } catch (IOException e)
        {
            e.printStackTrace();
        }
        db.setTransactionSuccessful();
        db.endTransaction();
    }

    public String[] getAllShipNames()
    {
        SQLiteDatabase db = this.getReadableDatabase();
        List<String> db_result = new ArrayList<>();

        // query database elements
        Cursor c = db.rawQuery("Select name from Ships group by name order by name asc;", null);

        while (c.moveToNext())
        {
            db_result.add(c.getString(c.getColumnIndex("name")));
        }
        c.close();

        String[] result = new String[db_result.size()];
        return db_result.toArray(result);
    }

    public String getShipChibiSrc(String ship_name)
    {
        SQLiteDatabase db = this.getReadableDatabase();
        String result = "";

        // query database elements
        String[] selectionArgs = {ship_name};
        Cursor c = db.rawQuery("Select chibi from Ships where name = ? group by name;", selectionArgs);

        while (c.moveToNext())
        {
           result = c.getString(c.getColumnIndex("chibi"));
        }
        c.close();

        Log.i("ALSL","url: " + result);
        return result;
    }

    public void setStatVariants(AzurLaneShip als, String ship_name)
    {
        SQLiteDatabase db = this.getReadableDatabase();
        ArrayList<String> variants = new ArrayList<>();

        // query database elements
        String[] selectionArgs = {ship_name};
        Cursor c = db.rawQuery("Select level from Ships where name = ? group by level;", selectionArgs);

        while (c.moveToNext())
        {
            if(c.getString(c.getColumnIndex("level")).contains("Base"))
            {
                variants.add(c.getString(c.getColumnIndex("level")).replaceFirst(" Stats",""));
            }
            else
            {
                variants.add(c.getString(c.getColumnIndex("level")).replaceFirst(" Retrofit","R"));
            }
        }
        c.close();

        als.setShipStatVariants(variants);
        als.setCurrentStatVariant("default");

        Log.i("ALSL","stats: " + variants.toString());
        Log.i("ALSL","stats: current variants - " + als.getCurrentStatVariant());
    }

    public void setShipData(AzurLaneShip als, String ship_name, String level)
    {
        SQLiteDatabase db = this.getReadableDatabase();

        Log.i("ALSL","stat: " + ship_name);
        Log.i("ALSL","stat: " + level);

        // query database elements
        String query_selectfrom = "Select association,classification,health,armor,reload,luck,firepower,torpedo,evasion,speed,antiair,aviation,oilconsumption,accuracy,antisubmarine from Ships ";
        String query_where = "where name = '" + ship_name + "' and level = '" + level + "';";
        String exec_sql = query_selectfrom + query_where;

        Cursor c = null;
        String association = "";
        String ship_class = "";
        int health = 0;
        int armor = 0;
        int reload = 0;
        int luck = 0;
        int firepower = 0;
        int torpedo = 0;
        int evasion = 0;
        int speed = 0;
        int antiair = 0;
        int aviation = 0;
        int oilconsumption = 0;
        int accuracy = 0;
        int antisubmarine = 0;
        try
        {
            c = db.rawQuery(exec_sql,null);
            if(c.getCount()!=0)
            {
                if (c.moveToFirst())
                {
                    association = c.getString(c.getColumnIndex("association"));
                    ship_class = c.getString(c.getColumnIndex("classification"));
                    health = c.getInt(c.getColumnIndex("health"));
                    armor = c.getInt(c.getColumnIndex("armor"));
                    reload = c.getInt(c.getColumnIndex("reload"));
                    luck = c.getInt(c.getColumnIndex("luck"));
                    firepower = c.getInt(c.getColumnIndex("firepower"));
                    torpedo = c.getInt(c.getColumnIndex("torpedo"));
                    evasion = c.getInt(c.getColumnIndex("evasion"));
                    speed = c.getInt(c.getColumnIndex("speed"));
                    antiair = c.getInt(c.getColumnIndex("antiair"));
                    aviation = c.getInt(c.getColumnIndex("aviation"));
                    oilconsumption = c.getInt(c.getColumnIndex("oilconsumption"));
                    accuracy = c.getInt(c.getColumnIndex("accuracy"));
                    antisubmarine = c.getInt(c.getColumnIndex("antisubmarine"));
                    Log.i("ALSL","stat: " + health);
                }
            }
            else
            {
                Log.i("ALSL","sql: no rows returned");
            }
            c.close();
        }
        catch(SQLException sqle)
        {
            Log.i("ALSL", "sql: " + sqle.getMessage());
        }

        als.setDisplayValues(association,ship_class,health,armor,reload,luck,firepower,torpedo,evasion,speed,antiair,aviation,oilconsumption,accuracy,antisubmarine);
    }

    public boolean checkDataBase()
    {
        SQLiteDatabase checkDB = null;
        try {
            checkDB = SQLiteDatabase.openDatabase("/data/user/0/com.cherisle.azurlanestatlab/databases/AzurLaneDB",
                    null, SQLiteDatabase.OPEN_READONLY);
            checkDB.close();
            Log.i("ALSL","db: exists - " + this.getDatabaseName() + " - " + checkDB.getPath());
        }
        catch (SQLiteException sqle)
        {
            Log.i("ALSL","db: doesnt exist yet - " + sqle.getMessage());
            // database doesn't exist yet.
        }
        return checkDB != null;
    }
}

