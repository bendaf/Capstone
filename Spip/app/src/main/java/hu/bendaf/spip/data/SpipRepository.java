package hu.bendaf.spip.data;

import android.util.Log;

import hu.bendaf.spip.AppExecutors;

/**
 * Created by bendaf on 2018. 08. 25. Spip.
 */
public class SpipRepository {
    private static final String LOG_TAG = SpipRepository.class.getSimpleName();
    private static SpipRepository sInstance;
    private final SpipDao mSpipDao;
    private final AppExecutors mExecutors;

    private SpipRepository(SpipDao spipDao, AppExecutors executors) {
        mSpipDao = spipDao;
        mExecutors = executors;
    }

    public synchronized static SpipRepository getInstance(SpipDao weatherDao, AppExecutors executors) {
        Log.d(LOG_TAG, "Getting the repository");
        if(sInstance == null) {
            sInstance = new SpipRepository(weatherDao, executors);
            Log.d(LOG_TAG, "Made new repository");
        }
        return sInstance;
    }
}
