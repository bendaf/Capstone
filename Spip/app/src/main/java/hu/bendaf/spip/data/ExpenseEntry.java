package hu.bendaf.spip.data;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;

import java.util.Date;

import static android.arch.persistence.room.ForeignKey.CASCADE;

/**
 * Created by bendaf on 2018. 08. 25. Spip.
 */

@Entity(tableName = "expenses",
        indices = {@Index(value = {"id"}), @Index(value = {"group_id"})},
        foreignKeys = @ForeignKey(entity = GroupEntry.class, parentColumns = "id", childColumns = "group_id",
                                  onDelete = CASCADE))
public class ExpenseEntry {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    private int id;
    private String name;

    private Date date;

    @ColumnInfo(name = "group_id")
    private int groupId;
    private double amount;
    private String currency;

    @ColumnInfo(name = "is_transfer")
    private boolean isTransfer;

    public ExpenseEntry(int id, String name, Date date, int groupId, double amount, String currency, boolean isTransfer) {
        this.id = id;
        this.name = name;
        this.date = date;
        this.groupId = groupId;
        this.amount = amount;
        this.currency = currency;
        this.isTransfer = isTransfer;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Date getDate() {
        return date;
    }

    public int getGroupId() {
        return groupId;
    }

    public String getCurrency() {
        return currency;
    }

    public boolean isTransfer() {
        return isTransfer;
    }

    public double getAmount() {
        return amount;
    }
}
