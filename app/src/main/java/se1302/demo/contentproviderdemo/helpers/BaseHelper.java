package se1302.demo.contentproviderdemo.helpers;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import se1302.demo.contentproviderdemo.models.TableModel;
import se1302.demo.contentproviderdemo.services.UltilService;

public class BaseHelper extends SQLiteOpenHelper {
    private TableModel table;
    private Context context;
    public BaseHelper(Context context, String name, int version, TableModel table) {
        super(context, name, null, version);
        this.context = context;
        this.table = table;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        try {
            UltilService service = new UltilService(context);
            boolean dbExists = service.checkDB(this.getDatabaseName());
            if (dbExists) {
                db.execSQL(getSQLCreateAllTables());
            }
        } catch (Exception e) {
            Log.d("BaseHelper", "Error at OnCreate " + e.getMessage());
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(getSQLDeleteAllTables());
        onCreate(db);
    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }

    private String getSQLDeleteAllTables() {
        return "DROP TABLE IF EXISTS " + table.getColumns();
    }

    private String getSQLCreateAllTables() {
        return "CREATE TABLE IF NOT EXISTS " + table.toString();
    }
}