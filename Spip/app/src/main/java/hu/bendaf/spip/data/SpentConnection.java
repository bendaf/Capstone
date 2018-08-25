package hu.bendaf.spip.data;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.Index;

/**
 * Created by bendaf on 2018. 08. 25. Spip.
 */

@Entity(tableName = "spent",
        primaryKeys = {"person_id", "expense_id"},
        indices = {@Index(value = {"person_id"}), @Index(value = {"expense_id"})},
        foreignKeys = {@ForeignKey(entity = PersonEntry.class, parentColumns = "id", childColumns = "person_id"),
                @ForeignKey(entity = ExpenseEntry.class, parentColumns = "id", childColumns = "expense_id")})
public class SpentConnection {

    @ColumnInfo(name = "person_id")
    private long personId;

    @ColumnInfo(name = "expense_id")
    private long expenseId;

    private int weight;

    public SpentConnection(long personId, long expenseId, int weight) {
        this.personId = personId;
        this.expenseId = expenseId;
        this.weight = weight;
    }

    public long getPersonId() {
        return personId;
    }

    public long getExpenseId() {
        return expenseId;
    }

    public int getWeight() {
        return weight;
    }
}
