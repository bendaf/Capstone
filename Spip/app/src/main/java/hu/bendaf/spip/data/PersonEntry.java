package hu.bendaf.spip.data;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;
import android.os.Parcel;
import android.os.Parcelable;

import java.util.Date;

import static android.arch.persistence.room.ForeignKey.CASCADE;

/**
 * Created by bendaf on 2018. 08. 25. Spip.
 */

@Entity(tableName = "persons",
        indices = {@Index(value = {"id"}), @Index(value = {"group_id"})},
        foreignKeys = @ForeignKey(entity = GroupEntry.class, parentColumns = "id", childColumns = "group_id",
                                  onDelete = CASCADE))
public class PersonEntry implements Parcelable {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    private long id;
    private String name;
    @ColumnInfo(name = "added_at")
    private Date addedAt;

    @ColumnInfo(name = "group_id")
    private long groupId;

    public PersonEntry(long id, String name, Date addedAt, long groupId) {
        this.id = id;
        this.name = name;
        this.addedAt = addedAt;
        this.groupId = groupId;
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Date getAddedAt() {
        return addedAt;
    }

    public long getGroupId() {
        return groupId;
    }

    public void setGroupId(long groupId) {
        this.groupId = groupId;
    }

    @Override public int describeContents() {
        return 0;
    }

    @Override public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(this.id);
        dest.writeString(this.name);
        dest.writeLong(this.addedAt != null ? this.addedAt.getTime() : -1);
        dest.writeLong(this.groupId);
    }

    protected PersonEntry(Parcel in) {
        this.id = in.readLong();
        this.name = in.readString();
        long tmpAddedAt = in.readLong();
        this.addedAt = tmpAddedAt == -1 ? null : new Date(tmpAddedAt);
        this.groupId = in.readLong();
    }

    public static final Parcelable.Creator<PersonEntry> CREATOR = new Parcelable.Creator<PersonEntry>() {
        @Override public PersonEntry createFromParcel(Parcel source) {
            return new PersonEntry(source);
        }

        @Override public PersonEntry[] newArray(int size) {
            return new PersonEntry[size];
        }
    };
}
