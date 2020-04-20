/*
    This example gleaned from the following URL:
    https://www.techotopia.com/index.php/An_Android_SQLite_Database_Tutorial

    Begin by adding a second new class to the project. The
    class will be named MyDBHandler and is sub-classed from the
    Android SQLiteOpenHelper class.

    We will add the constructor, along with the onCreate() and
    onUpgrade() methods. Since the handler will be required to
    add, query and delete data on behalf of the activity component,
    corresponding methods will also need to be added to the class.
 */

package com.example.sqlitedbproject01;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class MyDBHandler extends SQLiteOpenHelper {
    //  Database version.  Increment if any
    //  additions/deletions made
    private static final int DATABASE_VERSION = 1;

    //  User-defined name for database*
    private static final String DATABASE_NAME = "myProduct.db";

    //  User-defined name for table
    public static final String TABLE_PRODUCTS = "products";

    //  Table fields, id, productname, quantity
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_PRODUCTNAME = "productname";
    public static final String COLUMN_QUANTITY = "quantity";

    //  Constructor
    public MyDBHandler(Context context, String name,
                       CursorFactory factory, int version) {
        super(context, DATABASE_NAME, factory, DATABASE_VERSION);
    }

    /*
        The onCreate() method needs to be implemented so
        that the products table is created when the database
        is first created.

        This involves constructing a SQL CREATE statement
        containing instructions to create a new table with
        the appropriate columns and then passing that through
        to the execSQL() method of the SQLiteDatabase object
        passed as an argument to onCreate()
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        //  Create table query
        String CREATE_PRODUCTS_TABLE =
                "CREATE TABLE " +
                TABLE_PRODUCTS + "(" +
                COLUMN_ID +
                " INTEGER PRIMARY KEY," +
                COLUMN_PRODUCTNAME +
                " TEXT," +
                COLUMN_QUANTITY +
                " INTEGER" + ")";

        //  Execute create table query
        db.execSQL(CREATE_PRODUCTS_TABLE);
    }

    /*
        The onUpgrade() method is called when the handler is
        invoked with a greater database version number from
        the one previously used.

        The exact steps to be performed in this instance will
        be application specific, so for the purposes of this
        example we will simply remove the old database and
        create a new one.
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //  Drop products table if it currently exists
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PRODUCTS);

        //  Call onCreate to recreate table
        onCreate(db);
    }

    /*
        The method to insert database records will be named addProduct()
        and will take as an argument an instance of our Product data model
        class.

        A ContentValues object will be created in the body of the method
        and primed with key-value pairs for the data columns extracted from
        the Product object.

        Next, a reference to the database will be obtained via a call to
        getWritableDatabase() followed by a call to the insert() method of
        the returned database object.
     */
    public void addProduct(Product product) {
        //  Create new ContentValues object
        //  https://developer.android.com/reference/android/content/ContentValues
        ContentValues values = new ContentValues();

        //  A ContentValues object works something like an intent,
        //  in that it uses key-value pairs for get/set.
        values.put(COLUMN_PRODUCTNAME, product.get_productname());
        values.put(COLUMN_QUANTITY, product.get_quantity());

        //  Call getWritableDatabase() method as we are going to
        //  attempt to change the database.
        SQLiteDatabase db = this.getWritableDatabase();

        //  Insert record into Products table
        //
        //  As far as the nullColumnHack below is concerned:
        //  https://developer.android.com/reference/android/database/sqlite/SQLiteDatabase
        //
        //  String: optional; may be null. SQL doesn't allow inserting
        //  a completely empty row without naming at least one column
        //  name. If your provided values is empty, no column names are
        //  known and an empty row can't be inserted. If not set to null,
        //  the nullColumnHack parameter provides the name of nullable
        //  column name to explicitly insert a NULL into in the case where
        //  your values is empty.
        db.insert(TABLE_PRODUCTS, null, values);

        //  Close database connection
        db.close();
    }

    /*
        The method to query the database will be named findProduct()
        and will take as an argument a String object containing the
        name of the product to be located. Using this string, a SQL
        SELECT statement will be constructed to find all matching
        records in the table.

        For the purposes of this example, only the first match will
        then be returned, contained within a new instance of our
        Product data model class.
     */
    public Product findProduct(String productname) {
        //  Create find product SELECT query
        String query = "Select * FROM " + TABLE_PRODUCTS + " WHERE " +
                        COLUMN_PRODUCTNAME + " =  \"" + productname + "\"";

        //  Call getWritableDatabase() method as we are going to
        //  attempt to change the database.
        SQLiteDatabase db = this.getWritableDatabase();

        //  Execute this as a rawQuery(), which returns an
        //  object of the Cursor type
        Cursor cursor = db.rawQuery(query, null);

        //  Instantiate a new Product object
        Product product = new Product();

        //  If one or more records have been returned,
        //  move to first record.  "Copy" the id,
        //  productname, and quantity into the new
        //  product object
        if (cursor.moveToFirst())
        {
            cursor.moveToFirst();
            product.set_id(Integer.parseInt(cursor.getString(0)));
            product.set_productname(cursor.getString(1));
            product.set_quantity(Integer.parseInt(cursor.getString(2)));
            cursor.close();
        }
        //  No matching product found
        else
        {
            product = null;
        }

        //  Close the database connection
        db.close();

        return product;
    }

    /*
        The deletion method will be named deleteProduct() and
        will accept as an argument the entry to be deleted in the
        form of a Product object.

        The method will use a SQL SELECT statement to search for
        the entry based on the product name and, if located, delete
        it from the table.

        The success or failure of the deletion will be reflected in
        a Boolean return value
     */
    public boolean deleteProduct(String productname) {
        //  Value returning true if delete succeeded and false otherwise
        boolean result = false;

        //  Attempt to find record to delete
        String query = "Select * FROM " + TABLE_PRODUCTS + " WHERE " +
                        COLUMN_PRODUCTNAME + " =  \"" + productname + "\"";

        //  Call getWritableDatabase() method as we are going to
        //  attempt to change the database.
        SQLiteDatabase db = this.getWritableDatabase();

        //  Create rawQuery() and store result in Cursor variable
        Cursor cursor = db.rawQuery(query, null);

        //  Instantiate a new Product object
        Product product = new Product();

        //  Attempt to delete the associated record
        if (cursor.moveToFirst())
        {
            //  Going to columnIndex 0 as this is the primary key ID field
            product.set_id(Integer.parseInt(cursor.getString(0)));

            //  Do the delete
            db.delete(TABLE_PRODUCTS, COLUMN_ID + " = ?",
                        new String[] { String.valueOf(product.get_id()) });
            cursor.close();
            result = true;
        }

        //  Close database connection
        db.close();

        return result;
    }

}
