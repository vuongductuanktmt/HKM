package com.example.nghia.hkm.Class;

/**
 * Created by huu21 on 25/04/2017.
 */

import org.json.JSONException;
import org.json.JSONObject;
public class ClientToServer {
    private String __TokenUser__;
    private String __Authorities__;
    private String __Request__;
    private String __Table__;
    private String __Var__;
    private String __Value__;
    private String __Search__;
    private int __PageSize__;
    private int __PageNumber__;
    /*
     * dùng để get_all_data, get_category_parent, get_history_user, register_user
     */
    public ClientToServer(String __TokenUser__, String __Authorities__, String __Request__, String __Table__) {
        super();
        this.__TokenUser__ = __TokenUser__;
        this.__Authorities__ = __Authorities__;
        this.__Request__ = __Request__;
        this.__Table__ = __Table__;
    }
    /*
     * dùng để get_top_most, create_history
     */
    public ClientToServer(String __TokenUser__, String __Authorities__, String __Request__, String __Table__,
                          String __Var__, String __Value__) {
        super();
        this.__TokenUser__ = __TokenUser__;
        this.__Authorities__ = __Authorities__;
        this.__Request__ = __Request__;
        this.__Table__ = __Table__;
        this.__Var__ = __Var__;
        this.__Value__ = __Value__;
    }
    /*
     * dùng để get_pagination
     */

    public ClientToServer(String __TokenUser__, String __Authorities__, String __Request__, String __Table__,
                             String __Var__, String __Search__, int __PageSize__, int __PageNumber__) {
        super();
        this.__TokenUser__ = __TokenUser__;
        this.__Authorities__ = __Authorities__;
        this.__Request__ = __Request__;
        this.__Table__ = __Table__;
        this.__Var__ = __Var__;
        this.__Search__ = __Search__;
        this.__PageSize__ = __PageSize__;
        this.__PageNumber__ = __PageNumber__;
    }
    public String request(){
        JSONObject value = new JSONObject();
        try {
            value.put("__TokenUser__", this.__TokenUser__);
            value.put("__Authorities__", this.__Authorities__);
            value.put("__Request__", this.__Request__);
            value.put("__Table__", this.__Table__);
            value.put("__Var__", this.__Var__);
            value.put("__Value__", this.__Value__);
            value.put("__Search__", this.__Search__);
            value.put("__PageSize__", this.__PageSize__);
            value.put("__PageNumber__", this.__PageNumber__);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return value.toString();
    }

}
