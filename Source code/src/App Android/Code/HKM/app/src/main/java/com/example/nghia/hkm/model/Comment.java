package com.example.nghia.hkm.model;
//Parcelable thường được sử dụng để gửi dữ liệu (dạng Object)
// giữa các activity với nhau thông qua Bunble gửi cùng Intent.
import android.os.Parcel;
import android.os.Parcelable;
public class Comment implements Parcelable {
    User mCmtUser;
    String mCmtContent;

    public Comment(User mCmtUser, String mCmtContent) {
        this.mCmtUser = mCmtUser;
        this.mCmtContent = mCmtContent;
    }

    public User getmCmtUser() {
        return mCmtUser;
    }

    public void setmCmtUser(User mCmtUser) {
        this.mCmtUser = mCmtUser;
    }

    public String getmCmtContent() {
        return mCmtContent;
    }

    public void setmCmtContent(String mCmtContent) {
        this.mCmtContent = mCmtContent;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    //Write data to Parcel
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeSerializable(mCmtUser); //Write a generic serializable object in to a Parcel
        parcel.writeString(mCmtContent); //Write a string value into the parcel at the current dataPosition(), growing dataCapacity() if needed.
    }

    private Comment(Parcel in) {
        mCmtUser = (User) in.readSerializable();//Read and return a new Serializable object from the parcel
        mCmtContent = in.readString();//Read a string value from the parcel at the current dataPosition().
    }

    public static final Creator<Comment> CREATOR = new Creator<Comment>() {
        @Override
        public Comment createFromParcel(Parcel source) {
            return new Comment(source);
        }

        @Override
        public Comment[] newArray(int size) {
            return new Comment[size];
        }
    };
}
