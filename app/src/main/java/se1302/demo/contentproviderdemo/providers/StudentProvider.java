package se1302.demo.contentproviderdemo.providers;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;

import java.util.ArrayList;

import se1302.demo.contentproviderdemo.helpers.BaseHelper;
import se1302.demo.contentproviderdemo.models.TableModel;
import se1302.demo.contentproviderdemo.models.ColumnModel;

public class StudentProvider extends ContentProvider {
    private static final String DATABASENAME = "StudyManagement.db";
    private static final String PROVIDER_NAME = "se1302.demo.contentproviderdemo.providers.StudentProvider";
    public static final Uri CONTENT_URI = Uri.parse("content://" + PROVIDER_NAME + "/students");
    private static final UriMatcher uriMatcher;
    private TableModel table;
    private SQLiteDatabase db;

    static {
        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(PROVIDER_NAME, "students", 1);
        uriMatcher.addURI(PROVIDER_NAME, "students/#", 2);
    }

    public StudentProvider() {
        ArrayList<ColumnModel> columns = new ArrayList<>();
        columns.add(new ColumnModel("id", "text", true, true));
        columns.add(new ColumnModel("name", "text", false, true));
        columns.add(new ColumnModel("core", "integer", false, true));
        columns.add(new ColumnModel("isGraduate", "bit", false, true));
        this.table = new TableModel("Students", columns);
    }

    @Override
    public boolean onCreate() {
        Context context = getContext();
        this.db = new BaseHelper(context, this.DATABASENAME, 1, this.table).getReadableDatabase();
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
        qb.setTables(table.getName());

        switch (uriMatcher.match(uri)) {
            case 2:
                qb.appendWhere(table.getPrimaryKeyColumnName() + "=" + uri.getPathSegments().get(1));
                break;

            default:
        }
        Cursor c = qb.query(db, projection, selection,
                selectionArgs, null, null, sortOrder);
        c.setNotificationUri(getContext().getContentResolver(), uri);
        return c;
    }

    @Override
    public String getType(Uri uri) {
        switch (uriMatcher.match(uri)) {
            case 1:
                return "vnd.android.cursor.dir/vnd.se1302.demo.providers";
            case 2:
                return "vnd.android.cursor.item/vnd.se1302.demo.providers";
            default:
                return null;
        }
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        long row = this.db.insert(this.table.getName(), null, values);
        if (row > 0) {
            Uri newUri = ContentUris.withAppendedId(CONTENT_URI, row);
            getContext().getContentResolver().notifyChange(uri, null);
            return newUri;
        }
        throw new SQLException("Failed to add a record into " + uri);
    }


    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        int count = 0;
        switch (uriMatcher.match(uri)) {
            case 1:
                count = db.delete(this.table.getName(), selection, selectionArgs);
                break;

            case 2:
                String id = uri.getPathSegments().get(1);
                count = db.delete(this.table.getName(), table.getPrimaryKeyColumnName() + " = " + id +
                        (!TextUtils.isEmpty(selection) ? " AND (" + selection + ')' : ""), selectionArgs);
                break;
            default:
                throw new IllegalArgumentException("Unknown URI " + uri);
        }

        getContext().getContentResolver().notifyChange(uri, null);
        return count;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        int count = 0;
        switch (uriMatcher.match(uri)) {
            case 1:
                count = db.update(table.getName(), values, selection, selectionArgs);
                break;

            case 2:
                count = db.update(table.getName(), values,
                        table.getPrimaryKeyColumnName() + " = " + uri.getPathSegments().get(1) +
                                (!TextUtils.isEmpty(selection) ? " AND (" + selection + ')' : ""), selectionArgs);
                break;
            default:
                throw new IllegalArgumentException("Unknown URI " + uri);
        }

        getContext().getContentResolver().notifyChange(uri, null);
        return count;
    }
}
