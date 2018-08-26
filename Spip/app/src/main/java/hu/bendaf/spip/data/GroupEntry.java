package hu.bendaf.spip.data;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

import java.util.Date;

/**
 * Created by bendaf on 2018. 08. 25. Spip.
 */

@Entity(tableName = "groups")
public class GroupEntry {

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
}
