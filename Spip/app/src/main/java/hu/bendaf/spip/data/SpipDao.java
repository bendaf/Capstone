package hu.bendaf.spip.data;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

/**
 * Created by bendaf on 2018. 08. 25. Spip.
 */

@SuppressWarnings("unused")
@Dao
public interface SpipDao {

    /**
     * Selects all groups from the database.
     *
     * @return The selected {@link GroupEntry} groups.
     */
    @Query("SELECT * FROM groups")
    LiveData<List<GroupEntry>> getGroups();

    /**
     * Selects all persons for a group
     *
     * @param groupId The id of the {@link GroupEntry}
     *
     * @return The {@link PersonEntry} persons belonging to the given {@link GroupEntry}.
     */
    @Query("SELECT * FROM persons WHERE group_id=:groupId")
    LiveData<List<PersonEntry>> getPersons(long groupId);

    /**
     * Selects all expenses for a group
     *
     * @param groupId the id of the {@link GroupEntry}
     *
     * @return The list of {@link ExpenseEntry} belonging to the given {@link GroupEntry}.
     */
    @Query("SELECT * FROM expenses WHERE group_id=:groupId")
    LiveData<List<ExpenseEntry>> getExpenses(long groupId);

    /**
     * Get a list of {@link PersonEntry} who have paid an {@link ExpenseEntry}.
     *
     * @param expenseId The {@link ExpenseEntry} in which we are interested in.
     *
     * @return A {@link LiveData} of {@link List} of {@link PersonEntry} who paid.
     */
    @Query("SELECT persons.id, persons.name, persons.added_at, persons.group_id " +
                   "FROM persons INNER JOIN paid ON persons.id = paid.person_id WHERE paid.expense_id=:expenseId")
    LiveData<List<PersonEntry>> getPaidBy(long expenseId);

    /**
     * Get a list of {@link PersonEntry} who have spent an {@link ExpenseEntry}.
     *
     * @param expenseId The {@link ExpenseEntry} in which we are interested in.
     *
     * @return A {@link LiveData} of {@link List} of {@link PersonEntry} who spent the expense.
     */
    @Query("SELECT persons.id, persons.name, persons.added_at, persons.group_id " +
                   "FROM persons INNER JOIN spent ON persons.id = spent.person_id WHERE spent.expense_id=:expenseId")
    LiveData<List<PersonEntry>> getPaidTo(long expenseId);

    @Query("SELECT * from groups WHERE groups.id=:groupId")
    LiveData<GroupEntry> getGroup(long groupId);

    @Query("SELECT * FROM expenses WHERE expenses.id=:expenseId")
    LiveData<ExpenseEntry> getExpense(long expenseId);

    @Insert long addGroup(GroupEntry group);

    @Update void updateGroup(GroupEntry groupEntry);

    @Delete void deleteGroup(GroupEntry groupEntry);


    @Insert(onConflict = OnConflictStrategy.IGNORE)
    List<Long> addPerson(PersonEntry... person);

    @Update void updatePerson(PersonEntry person);

    @Delete void deletePerson(PersonEntry person);


    @Insert long addExpense(ExpenseEntry expense);

    @Update void updateExpense(ExpenseEntry expense);

    @Delete void deleteExpense(ExpenseEntry expense);


    @Insert long addPaid(PaidConnection paid);

    @Update void updatePaid(PaidConnection paid);

    @Delete void deletePaid(PaidConnection paid);


    @Insert long addSpent(SpentConnection spent);

    @Update void updateSpent(SpentConnection spent);

    @Delete void deleteSpent(SpentConnection spent);
}
