package com.example.employeedb;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseManager extends SQLiteOpenHelper{

    private static final String DATABASE_NAME = "EmployeesDatabase";
    private static final int DATABASE_VERSION = 1;
    private static final String TABLE_NAME = "employees";
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_NAME = "name";
    private static final String COLUMN_DEPT = "department";
    private static final String COLUMN_JOIN_DATE = "joiningDate";
    private static final String COLUMN_SALARY = "salary";

    DatabaseManager(Context context){
        super(context, DATABASE_NAME, null,DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String sql = "CREATE TABLE " + TABLE_NAME + "(\n" +
                "   " + COLUMN_ID + "INTEGER NOT NULL CONSTRAINT employees_pk PRIMARY KEY AUTOINCREMENT , \n" +
                "   " + COLUMN_NAME + " varchar(200) NOT NULL, \n" +
                "   " + COLUMN_DEPT + " varchar(200) NOT NULL, \n" +
                "   " + COLUMN_JOIN_DATE + "datetime NOT NULL, \n" +
                "   " + COLUMN_SALARY + "double NOT NULL\n" +
                ");";

        // Executing the string to create the table\
        sqLiteDatabase.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        String sql = "DROP TABLE IF EXISTS " + TABLE_NAME + ";";
        sqLiteDatabase.execSQL(sql);
        onCreate(sqLiteDatabase);

    }

    boolean addEmployee(String name, String dept, String joiningdate, double salary){
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_NAME,name);
        contentValues.put(COLUMN_DEPT,dept);
        contentValues.put(COLUMN_JOIN_DATE,joiningdate);
        contentValues.put(COLUMN_SALARY,salary);
        SQLiteDatabase db = getWritableDatabase();
        return db.insert(TABLE_NAME,null, contentValues) != -1;
    }

    Cursor getAllEmployees(){
        SQLiteDatabase db = getReadableDatabase();
        return db.rawQuery("SELECT * FROM " + TABLE_NAME, null);
    }

    boolean updateEmployee(int id,String name, String dept, double salary){
        SQLiteDatabase db = getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_NAME,name);
        contentValues.put(COLUMN_DEPT,dept);
        contentValues.put(COLUMN_SALARY,salary);
        return db.update(TABLE_NAME,contentValues,COLUMN_ID + "=?", new String[]{String.valueOf(id)}) == 1;

    }

    boolean deleteEmployee(int id){
        SQLiteDatabase db = getWritableDatabase();
        return db.delete(TABLE_NAME,COLUMN_ID + "=?", new String[]{String.valueOf(id)}) == 1;
    }

}
