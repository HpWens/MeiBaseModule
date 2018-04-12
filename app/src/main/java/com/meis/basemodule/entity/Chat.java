package com.meis.basemodule.entity;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by YoKeyword on 16/6/30.
 */
public class Chat implements Parcelable {

    public int id;
    public String name;
    public String message;
    public long time;
    public int avatar;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Chat chat = (Chat) o;

        return id == chat.id;
    }

    @Override
    public int hashCode() {
        return id;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeString(this.name);
        dest.writeString(this.message);
        dest.writeLong(this.time);
        dest.writeInt(this.avatar);
    }

    public Chat() {
    }

    protected Chat(Parcel in) {
        this.id = in.readInt();
        this.name = in.readString();
        this.message = in.readString();
        this.time = in.readLong();
        this.avatar = in.readInt();
    }

    public static final Parcelable.Creator<Chat> CREATOR = new Parcelable.Creator<Chat>() {
        @Override
        public Chat createFromParcel(Parcel source) {
            return new Chat(source);
        }

        @Override
        public Chat[] newArray(int size) {
            return new Chat[size];
        }
    };
}
