package com.hisivasankar.flicksbuddy;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.test.AndroidTestCase;
import android.util.Log;

import com.hisivasankar.flicksbuddy.contracts.FlickContract;
import com.hisivasankar.flicksbuddy.persistance.FlickDBHelper;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Created by I308944 on 2/20/2016.
 */
public class FlickDatabaseTest extends AndroidTestCase {
    public static final String LOG_TAG = FlickDatabaseTest.class.getSimpleName();

    public FlickDatabaseTest() {
        super();
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();

    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
        this.mContext.deleteDatabase(FlickDBHelper.DATABASE_NAME);
    }

    public void testDBCreated() {
        Set<String> tables = new HashSet<>();
        tables.add(FlickContract.FavouriteEntry.TABLE_NAME);

        FlickDBHelper dbHelper = new FlickDBHelper(this.mContext);

        SQLiteDatabase db = dbHelper.getWritableDatabase();

        assertTrue("DB is not open", db.isOpen());

        Cursor cursor = db.rawQuery("SELECT name FROM sqlite_master WHERE type='table'", null);

        assertTrue("No table found", cursor.moveToFirst());

        do {
            tables.remove(cursor.getString(0));
        } while (cursor.moveToNext());

        assertTrue("Tables are not created in DB", tables.isEmpty());

        assertEquals("All tables are created in DB", true, tables.isEmpty());

        validateData(dbHelper.getReadableDatabase(), 5);
    }

    public void testInsert() {
        ContentValues contentValues = getTestRow();

        FlickDBHelper dbHelper = new FlickDBHelper(this.mContext);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        long rowID = db.insert(FlickContract.FavouriteEntry.TABLE_NAME, null, contentValues);

        if (rowID > 0) {
            Log.d(LOG_TAG, "Inserted successfully : " + rowID);
        } else {
            Log.d(LOG_TAG, "Can't insert data into table");
        }

        assertTrue("Can't insert row into table", rowID != -1);

        //validateData(dbHelper.getReadableDatabase(), rowID);

    }

    public void validateData(SQLiteDatabase db, long rowID) {

        ContentValues values = getTestRow();

        Cursor cursor = db.query(FlickContract.FavouriteEntry.TABLE_NAME, null, null, null, null, null, null);

        assertTrue("No Record found", cursor.moveToFirst());

        validateRecord(cursor, values);
    }

    private void validateRecord(Cursor cursor, ContentValues expectedValues) {

        Set<Map.Entry<String, Object>> valueSet = expectedValues.valueSet();

        for (Map.Entry<String, Object> entry : valueSet) {
            String colName = entry.getKey();
            Log.d(LOG_TAG, colName);
            int colIndex = cursor.getColumnIndex(colName);
            assertTrue("No column found", colIndex != -1);

            String value = cursor.getString(colIndex);

            Log.d(LOG_TAG, value);

            assertEquals("Value doesn't not match", entry.getValue().toString(), value);

        }

    }

    private ContentValues getTestRow() {
        ContentValues contentValues = new ContentValues();
        contentValues.put(FlickContract.FavouriteEntry.COLUMN_MOVIE_ID, 12);
        contentValues.put(FlickContract.FavouriteEntry.COLUMN_TITLE, "Dada");
        contentValues.put(FlickContract.FavouriteEntry.COLUMN_OVERVIEW, "Dudu dede lala lele haha so funny");
        contentValues.put(FlickContract.FavouriteEntry.COLUMN_POSTER_PATH, "dummy path");
        contentValues.put(FlickContract.FavouriteEntry.COLUMN_RELEASE_DATE, "12-01-2016");
        contentValues.put(FlickContract.FavouriteEntry.COLUMN_VOTE_AVERAGE, 8);
        return contentValues;
    }
}
