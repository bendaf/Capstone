package hu.bendaf.spip.data;

import android.content.Context;
import android.util.Log;

import java.util.Calendar;
import java.util.Date;

import hu.bendaf.spip.AppExecutors;

/**
 * Created by bendaf on 2018. 08. 25. Spip.
 */
@SuppressWarnings("WeakerAccess") public class SpipRepository {
    private static final String LOG_TAG = SpipRepository.class.getSimpleName();
    private static SpipRepository sInstance;
    private final SpipDao mSpipDao;
    private final AppExecutors mExecutors;

    private SpipRepository(SpipDao spipDao, AppExecutors executors) {
        mSpipDao = spipDao;
        mExecutors = executors;
    }

    public synchronized static SpipRepository getInstance(Context context, AppExecutors executors) {
        Log.d(LOG_TAG, "Getting the repository");
        if(sInstance == null) {
            sInstance = new SpipRepository(SpipDatabase.getInstance(context).spipDao(), executors);
            Log.d(LOG_TAG, "Made new repository");
        }
        return sInstance;
    }

    public void addGroup(final String name, final String mainCurrency, final String description, final Date date) {
        mExecutors.diskIO().execute(new Runnable() {
            @Override public void run() {
                mSpipDao.addGroup(new GroupEntry(0, name, description, date, mainCurrency));
            }
        });
    }

    public void addGroup(String name) {
        addGroup(name, null, null, Calendar.getInstance().getTime());
    }

}
