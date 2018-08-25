package hu.bendaf.spip;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Room;
import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.IOException;
import java.util.Date;
import java.util.List;

import hu.bendaf.spip.data.GroupEntry;
import hu.bendaf.spip.data.SpipDao;
import hu.bendaf.spip.data.SpipDatabase;

/**
 * Created by bendaf on 2018. 08. 25. Spip.
 */
@RunWith(AndroidJUnit4.class)
public class GroupEntityReadWriteTest {
    private SpipDao mUserDao;
    private SpipDatabase mDb;

    @Before
    public void createDb() {
        Context context = InstrumentationRegistry.getTargetContext();
        mDb = Room.inMemoryDatabaseBuilder(context, SpipDatabase.class).build();
        mUserDao = mDb.spipDao();
    }

    @After
    public void closeDb() throws IOException {
        mDb.close();
    }

    @Test
    public void writeUserAndReadInList() throws Exception {
        GroupEntry group = new GroupEntry(0, "test", "test desc", new Date(0), "HUF");

        mUserDao.addGroup(group);
        LiveData<List<GroupEntry>> byName = mUserDao.getGroups();
//        Assert.assertFalse(byName.getValue().isEmpty());
    }
}
