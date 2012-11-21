
package ar.com.aleatoria.akrasia.util;

import android.os.Parcel;
import android.os.Parcelable;

public class JsonParcelable implements Parcelable {
    private final String json;

    public String getJson() {
        return json;
    }

    public JsonParcelable(String json) {
        this.json = json;
    }

    public JsonParcelable(Parcel arg0) {
        json = arg0.readString();
    }

    @Override
    public int describeContents() {
        return this.hashCode();
    }

    @Override
    public void writeToParcel(Parcel dest, int arg1) {
        dest.writeString(json);

    }

    @Override
    public String toString() {
        return json;
    }

    public static final Parcelable.Creator<JsonParcelable> CREATOR = new Parcelable.Creator<JsonParcelable>() {
        @Override
        public JsonParcelable createFromParcel(Parcel arg0) {
            return new JsonParcelable(arg0);
        }

        @Override
        public JsonParcelable[] newArray(int arg0) {
            return new JsonParcelable[arg0];
        }
    };
}
