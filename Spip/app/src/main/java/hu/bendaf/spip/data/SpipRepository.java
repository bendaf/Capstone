package hu.bendaf.spip.data;

import android.arch.lifecycle.LiveData;
import android.content.Context;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

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

    public synchronized static SpipRepository getInstance(Context context) {
        if(sInstance == null) {
            sInstance = new SpipRepository(SpipDatabase.getInstance(context).spipDao(), AppExecutors.getInstance());
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

    public void addGroup(final String name, final String mainCurrency, final String description) {
        addGroup(name, mainCurrency, description, Calendar.getInstance().getTime());
    }

    public void addGroup(String name) {
        addGroup(name, null, null);
    }

    public void addGroupWithPeople(final GroupEntry groupEntry, final List<PersonEntry> people) {
        mExecutors.diskIO().execute(new Runnable() {
            @Override public void run() {
                long id = mSpipDao.addGroup(groupEntry);
                for(PersonEntry p : people) {
                    p.setGroupId(id);
                    mSpipDao.addPerson(p);
                }
            }
        });
    }

    public LiveData<GroupEntry> getGroupById(long groupId) {
        return mSpipDao.getGroup(groupId);
    }

    public void updateGroup(final GroupEntry groupEntry, final List<PersonEntry> people) {
        mExecutors.diskIO().execute(new Runnable() {
            @Override public void run() {
                mSpipDao.updateGroup(groupEntry);
                for(PersonEntry p : people) {
                    p.setGroupId(groupEntry.getId());
                    mSpipDao.addPerson(p);
                }
            }
        });
    }

    public LiveData<List<PersonEntry>> getGroupParticipants(Long mGroupId) {
        return mSpipDao.getPersons(mGroupId);
    }

    public List<GroupEntry> loadGroupsForWidget() {
        return mSpipDao.loadGroupsForWidget();
    }
}
