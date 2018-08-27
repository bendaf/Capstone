package hu.bendaf.spip.data;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.os.Parcel;
import android.os.Parcelable;

import java.util.Date;

/**
 * Created by bendaf on 2018. 08. 25. Spip.
 */

@Entity(tableName = "groups")
public class GroupEntry implements Parcelable {

    @PrimaryKey(autoGenerate = true)
    private long id;
    private String name;
    private String description;
    @ColumnInfo(name = "created_at")
    private Date createdAt;
    @ColumnInfo(name = "main_currency")
    private String mainCurrency;

    public GroupEntry(long id, String name, String description, Date createdAt, String mainCurrency) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.createdAt = createdAt;
        this.mainCurrency = mainCurrency;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public String getMainCurrency() {
        return mainCurrency;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setMainCurrency(String mainCurrency) {
        this.mainCurrency = mainCurrency;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    @Override public int describeContents() {
        return 0;
    }

    @Override public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(this.id);
        dest.writeString(this.name);
        dest.writeString(this.description);
        dest.writeLong(this.createdAt != null ? this.createdAt.getTime() : -1);
        dest.writeString(this.mainCurrency);
    }

    protected GroupEntry(Parcel in) {
        this.id = in.readLong();
        this.name = in.readString();
        this.description = in.readString();
        long tmpCreatedAt = in.readLong();
        this.createdAt = tmpCreatedAt == -1 ? null : new Date(tmpCreatedAt);
        this.mainCurrency = in.readString();
    }

    public static final Parcelable.Creator<GroupEntry> CREATOR = new Parcelable.Creator<GroupEntry>() {
        @Override public GroupEntry createFromParcel(Parcel source) {
            return new GroupEntry(source);
        }

        @Override public GroupEntry[] newArray(int size) {
            return new GroupEntry[size];
        }
    };
}
