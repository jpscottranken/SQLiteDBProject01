package com.example.sqlitedbproject01;

import android.app.Activity;
import android.app.ActionBar;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.os.Build;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    //  Widget declarations
    TextView idView;                //  TextView containing productID
    EditText productBox;            //  EditText containing product name
    EditText quantityBox;           //  EditText containing product quantity
    Toast t;                        //  Toast variable

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //  Widget initializations
        idView      = (TextView) findViewById(R.id.idView);
        productBox  = (EditText) findViewById(R.id.productBox);
        quantityBox = (EditText) findViewById(R.id.quantityBox);
    }

    //  Runs when Add Record button is clicked
    public void newProduct (View view) {
        //  Create instance of MyDBHandler class
        MyDBHandler dbHandler = new MyDBHandler(this, null, null, 1);

        int quantity;               //  Numeric value of product quantity EditText

        //  Check for no product name input
        if (productBox.getText().toString().equals(""))
        {
            Toast.makeText(getApplicationContext(), "No Product Name Inputted!",
                    Toast.LENGTH_LONG).show();
            productBox.requestFocus();
            return;
        }

        //  Check for no product quantity input
        if (quantityBox.getText().toString().equals(""))
        {
            Toast.makeText(getApplicationContext(), "No Quantity Inputted!",
                                                         Toast.LENGTH_LONG).show();
            quantityBox.requestFocus();
            return;
        }

        //  Quantity EditText not empty
        quantity = Integer.parseInt(quantityBox.getText().toString());

        //  Check for <= 0 product quantity input
        if (quantity <= 0)
        {
            Toast.makeText(getApplicationContext(), "Quantity Must Be Positive!",
                                                        Toast.LENGTH_LONG).show();
            quantityBox.setText("");
            quantityBox.requestFocus();
            return;
        }

        //  Product name not empty.  Product quantity positive.
        //  Instantiate new product object
        Product product = new Product(productBox.getText().toString(), quantity);

        dbHandler.addProduct(product);
        Toast.makeText(getApplicationContext(), "Record Successfully Added",
                                                     Toast.LENGTH_LONG).show();
        //  Set fields in GUI to blank
        productBox.setText("");
        quantityBox.setText("");
    }

    //  Runs when Find Record button is clicked
    public void lookupProduct (View view) {
        //  Create instance of MyDBHandler class
        MyDBHandler dbHandler = new MyDBHandler(this, null, null, 1);

        //  Call findProduct() method, passing current contents in
        //  product description EditText
        Product product = dbHandler.findProduct(productBox.getText().toString());

        //  If a match, fill up associated EditTexts with associated into
        if (product != null)
        {
            idView.setText(String.valueOf(product.get_id()));
            quantityBox.setText(String.valueOf(product.get_quantity()));
        }
        //  No match, blank out all fields and show associated Toast message
        else
        {
            idView.setText("");
            productBox.setText("");
            quantityBox.setText("");
            Toast.makeText(getApplicationContext(), "No Record Found Meeting Criteria",
                                                         Toast.LENGTH_LONG).show();
        }
    }

    //  Runs when Delete Record button is clicked
    public void removeProduct (View view) {
        //  Create instance of MyDBHandler class
        MyDBHandler dbHandler = new MyDBHandler(this, null, null, 1);

        //  Attempt to delete record with current contents in
       //   product description EditText
        boolean result = dbHandler.deleteProduct(productBox.getText().toString());

        //  Success
        if (result)
        {
            idView.setText("");
            productBox.setText("");
            quantityBox.setText("");
            Toast.makeText(getApplicationContext(), "Record Successfully Deleted",
                                                         Toast.LENGTH_LONG).show();
        }
        //  Failure
        else
        {
            Toast.makeText(getApplicationContext(), "Error Deleting Record",
                                                         Toast.LENGTH_LONG).show();
        }
    }
}
