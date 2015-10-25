package vr.midterm;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.text.TextUtils;

/**
 * Created by sec on 2015-10-25.
 */
public class WordInfoProvider extends ContentProvider{

    static final Uri CONTENT_URI = Uri.parse("content://vr.midterm.GermanDictionary/word/*");
    static final int GETALL = 1;
    static final int GETONE = 2;

    static final UriMatcher matcher;
    static {
        matcher = new UriMatcher(UriMatcher.NO_MATCH);
        matcher.addURI("vr.midterm.GermanDictionary", "word", GETALL);
        matcher.addURI("vr.midterm.GermanDictionary", "word/*", GETONE);
    }

    SQLiteDatabase mDB;
    class TestDBHelper extends SQLiteOpenHelper {
        public TestDBHelper(Context c){
            super(c, "test.db", null, 1);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL("CREATE TABLE IF NOT EXISTS word "
                    + "(_id INTEGER PRIMARY KEY AUTOINCREMENT, german TEXT, korean TEXT, wordclass INT, gender INT);");

        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL("DROP TABLE IF EXISTS word;");
            onCreate(db);
        }
    }
    @Override
    public boolean onCreate() {
        TestDBHelper helper = new TestDBHelper(getContext());
        mDB = helper.getWritableDatabase();

        return (mDB != null);
    }

    @Nullable
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        String sql;
        sql = "SELECT * FROM word";
        if (matcher.match(uri) == GETONE) {
            sql += selection;
        }
        sql += ";";
        Cursor cur = mDB.rawQuery(sql, null);
        return cur;
    }

    @Nullable
    @Override
    public String getType(Uri uri) {
        switch(matcher.match(uri)){
            case GETALL:
                return "vnd.android.cursor.dir/vnd.vr.word";
            case GETONE:
                return "vnd.android.cursor.item/vnd.vr.word";
        }
        return null;
    }

    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues values) {
        long row = mDB.insert("word", null, values);
        if (row > 0) {
            Uri notiuri = ContentUris.withAppendedId(CONTENT_URI, row);
            getContext().getContentResolver().notifyChange(notiuri, null);
            return notiuri;
        }
        return null;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        int cnt = 0;
        switch(matcher.match(uri)){
            case GETALL:
                cnt = mDB.delete("word", selection, selectionArgs);
                break;
            case GETONE:
                String where = "german = '" + uri.getPathSegments().get(1) + "'";
                if (TextUtils.isEmpty(selection) == false) {
                    where += " AND " + selection;
                }
                cnt = mDB.delete("word", where, selectionArgs);
                break;
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return cnt;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        int cnt = 0;
        switch (matcher.match(uri)) {
            case GETALL:
                cnt = mDB.update("word", values, selection, selectionArgs);
                break;
            case GETONE:
                String where = "german = '" + uri.getPathSegments().get(1) + "'";
                if (TextUtils.isEmpty(selection) == false) {
                    where += " AND " + selection;
                }
                cnt = mDB.update("word", values, where, selectionArgs);
                break;
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return cnt;
    }
}
