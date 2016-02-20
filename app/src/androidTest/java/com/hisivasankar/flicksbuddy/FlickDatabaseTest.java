package com.hisivasankar.flicksbuddy;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.test.AndroidTestCase;

import com.hisivasankar.flicksbuddy.contracts.FlickContract;
import com.hisivasankar.flicksbuddy.persistance.FlickDBHelper;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by I308944 on 2/20/2016.
 */
public class FlickDatabaseTest extends AndroidTestCase {
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
    }

}
