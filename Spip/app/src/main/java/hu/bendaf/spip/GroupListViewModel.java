package hu.bendaf.spip;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import java.util.List;

import hu.bendaf.spip.data.GroupEntry;
import hu.bendaf.spip.data.PersonEntry;
import hu.bendaf.spip.data.SpipRepository;

/**
 * Created by bendaf on 2018. 08. 27. Spip.
 */
public class GroupListViewModel extends AndroidViewModel { //TODO review usages, maybe separate to activities.
    private SpipRepository mRepository;
    private LiveData<List<GroupEntry>> mGroups;

    public GroupListViewModel(@NonNull Application app) {
        super(app);
        this.mRepository = SpipRepository.getInstance(app.getApplicationContext());
        mGroups = mRepository.getGroups();
    }

    public LiveData<List<GroupEntry>> getGroupList() {
        return mGroups;
    }

    public LiveData<List<PersonEntry>> getPersons(long id) {
        return mRepository.getGroupParticipants(id);
    }

    public LiveData<GroupEntry> getGroupById(long groupId) {
        return mRepository.getGroupById(groupId);
    }
}
