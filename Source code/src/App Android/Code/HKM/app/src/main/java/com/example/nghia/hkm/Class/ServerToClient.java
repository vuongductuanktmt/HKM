package com.example.nghia.hkm.Class;

import org.bson.Document;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by huu21 on 25/04/2017.
 */

public class ServerToClient {
    public static Document getDataProducts(String data) {
        Document product = Document.parse(data);
        return product;
    }

    @SuppressWarnings("unchecked")
    public static List<Document> getDataListProducts(String data) {
        Document product = Document.parse(data);
        List<Document> courses = (List<Document>) product.get("data");
        return courses;
    }
    public static List<Document> getListDocument(String key,Document documents){
        List<Document> courses = (List<Document>) documents.get(key);
        return courses;
    }
    public static List<Products> GetListArrayProducts(String data) {
        List<Products> list = new ArrayList<Products>();
        List<Document> documents = new ArrayList<>();
        documents = getDataListProducts(data);
        Products p;
        for (Document document : documents) {
            p = new Products(document.getString("__Title__"), document.getString("__LinkTitle__"),
                    document.getString("__HomePage__"), document.getString("__LinkImage__"),
                    document.getLong("__ViewCount__"), document.getLong("__ViewCount__"),
                    document.getString("__CurrentPrice__"), document.getString("__OldPrice__"),
                    document.getDate("__DateInsert__"), document.getBoolean("__Delete__"),
                    document.getString("__Status__"),

                    null);
            list.add(p);
        }

        return list;
    }

    public static ArrayList<CategoryParents> GetListArrayCategoryParents(String data) {
        ArrayList<CategoryParents> list = new ArrayList<CategoryParents>();
        List<Document> documents = new ArrayList<>();
        documents = getDataListProducts(data);
        CategoryParents p;
        for (Document document : documents) {
            p = new CategoryParents(document.getString("__CategoryParentName__"));
            list.add(p);
        }

        return list;
    }
}