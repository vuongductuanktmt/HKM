package App.HKM.Data;

import java.sql.Date;

public class Event {
private String __Title__;
private String __Href__;
private String __HomePage__;
private String __LinkImage__;
private long __ViewCount__;
private long __LoveCount__;
private java.util.Date __DateInsert__;
private boolean __Delete__;
public Event(String __Title__, String __Href__, String __HomePage__, String __LinkImage__, long __ViewCount__,
		long __LoveCount__, java.util.Date __DateInsert__, boolean __Delete__) {
	super();
	this.__Title__ = __Title__;
	this.__Href__ = __Href__;
	this.__HomePage__ = __HomePage__;
	this.__LinkImage__ = __LinkImage__;
	this.__ViewCount__ = __ViewCount__;
	this.__LoveCount__ = __LoveCount__;
	this.__DateInsert__ = __DateInsert__;
	this.__Delete__ = __Delete__;
}
public String get__Title__() {
	return __Title__;
}
public void set__Title__(String __Title__) {
	this.__Title__ = __Title__;
}
public String get__Href__() {
	return __Href__;
}
public void set__Href__(String __Href__) {
	this.__Href__ = __Href__;
}
public String get__LinkImage__() {
	return __LinkImage__;
}
public void set__LinkImage__(String __LinkImage__) {
	this.__LinkImage__ = __LinkImage__;
}
public String get__HomePage__() {
	return __HomePage__;
}
public void set__HomePage__(String __HomePage__) {
	this.__HomePage__ = __HomePage__;
}
public long get__ViewCount__() {
	return __ViewCount__;
}
public void set__ViewCount__(long __ViewCount__) {
	this.__ViewCount__ = __ViewCount__;
}
public long get__LoveCount__() {
	return __LoveCount__;
}
public void set__LoveCount__(long __LoveCount__) {
	this.__LoveCount__ = __LoveCount__;
}
public java.util.Date get__DateInsert__() {
	return __DateInsert__;
}
public void set__DateInsert__(java.util.Date __DateInsert__) {
	this.__DateInsert__ = __DateInsert__;
}
public boolean is__Delete__() {
	return __Delete__;
}
public void set__Delete__(boolean __Delete__) {
	this.__Delete__ = __Delete__;
}
}
