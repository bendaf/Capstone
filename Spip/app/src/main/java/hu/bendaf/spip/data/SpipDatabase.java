package hu.bendaf.spip.data;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.TypeConverters;
import android.content.Context;

/**
 * Created by bendaf on 2018. 08. 25. Spip.
 */
@Database(entities = {GroupEntry.class, PersonEntry.class, ExpenseEntry.class, PaidConnection.class,
        SpentConnection.class}, version = 1, exportSchema = false)
@TypeConverters(DateConverter.class)
public abstract class SpipDatabase extends RoomDatabase {
    private static final String DATABASE_NAME = "spip";
    private static SpipDatabase sInstance;

    public static synchronized SpipDatabase getInstance(Context context) {
        if(sInstance == null) {
            sInstance = Room.databaseBuilder(context.getApplicationContext(), SpipDatabase.class,
                    SpipDatabase.DATABASE_NAME).build();
        }
        return sInstance;
    }

    public abstract SpipDao spipDao();
}
