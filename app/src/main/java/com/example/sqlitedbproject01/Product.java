package com.example.sqlitedbproject01;

public class Product {
    private int _id;
    private String _productname;
    private int _quantity;

    //  No-Arg Constructor
    public Product() {
    }

    //  Full-Arg Constructor
    public Product(int _id, String _productname, int _quantity) {
        this._id = _id;
        this._productname = _productname;
        this._quantity = _quantity;
    }

    //  Two-Arg Constructor
    public Product(String _productname, int _quantity) {
        this._productname = _productname;
        this._quantity = _quantity;
    }

    //  Getters and setters
    //  _id getter
    public int get_id() {
        return _id;
    }

    //  _id setter
    public void set_id(int _id) {
        this._id = _id;
    }

    //  _productname getter
    public String get_productname() {
        return _productname;
    }

    //  _productname setter
    public void set_productname(String _productname) {
        this._productname = _productname;
    }

    //  _quantity getter
    public int get_quantity() {
        return _quantity;
    }

    //  _quantity setter
    public void set_quantity(int _quantity) {
        this._quantity = _quantity;
    }
}

