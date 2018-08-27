package hu.bendaf.spip;

import android.arch.lifecycle.Observer;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

import hu.bendaf.spip.data.GroupEntry;
import hu.bendaf.spip.data.PersonEntry;
import hu.bendaf.spip.data.SpipRepository;
import hu.bendaf.spip.databinding.ActivityAddGroupBinding;
import hu.bendaf.spip.databinding.PersonListItemBinding;
import hu.bendaf.spip.utils.FirebaseUtils;

public class AddGroupActivity extends AppCompatActivity {

    private static final String EXTRA_PARTICIPANTS = "participants";
    private static final String EXTRA_GROUP = "group";
    ActivityAddGroupBinding mBinding;
    private ParticipantListAdapter mAdapter;
    private GroupEntry mGroupEntry = new GroupEntry(0, null, null, null, null);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_add_group);
        setSupportActionBar(mBinding.toolbar);

        //noinspection ConstantConditions
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        assert mBinding.content != null;
        mAdapter = new ParticipantListAdapter(new ArrayList<PersonEntry>());
        if(getIntent().hasExtra(GroupBalanceActivity.EXTRA_GROUP_ID)) {
            long groupId = getIntent().getLongExtra(GroupBalanceActivity.EXTRA_GROUP_ID, -1);
            SpipRepository.getInstance(this).getGroupById(groupId).observe(AddGroupActivity.this,
                    new Observer<GroupEntry>() {
                        @Override public void onChanged(@Nullable GroupEntry groupEntry) {
                            mGroupEntry = groupEntry;
                            mBinding.setGroupEntry(groupEntry);
                        }
                    });
            SpipRepository.getInstance(this).getGroupParticipants(groupId).observe(this,
                    new Observer<List<PersonEntry>>() {
                        @Override public void onChanged(@Nullable List<PersonEntry> personEntries) {
                            mAdapter = new ParticipantListAdapter(personEntries);
                            mBinding.content.rvParticipants.setAdapter(mAdapter);
                        }
                    });
        } else {
            if(savedInstanceState != null) {
                if(savedInstanceState.getParcelableArrayList(EXTRA_PARTICIPANTS) != null) {
                    mAdapter = new ParticipantListAdapter(savedInstanceState.<PersonEntry>getParcelableArrayList(EXTRA_PARTICIPANTS));
                }
                if(savedInstanceState.getParcelable(EXTRA_GROUP) != null) {
                    mGroupEntry = savedInstanceState.getParcelable(EXTRA_GROUP);
                }
            }
            mBinding.setGroupEntry(mGroupEntry);
            mBinding.content.rvParticipants.setAdapter(mAdapter);
        }
        List<String> currencies = Arrays.asList(getResources().getStringArray(R.array.list_currencies));
        mBinding.content.spCurrency.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item,
                currencies));

        mBinding.content.etNewPerson.setImeActionLabel(getString(R.string.label_add), KeyEvent.KEYCODE_ENTER);
        mBinding.content.etNewPerson.setOnKeyListener(new View.OnKeyListener() {
            @Override public boolean onKey(View v, int keyCode, KeyEvent event) {
                if((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    PersonEntry newPerson = new PersonEntry(0, ((EditText) v).getText().toString(),
                            Calendar.getInstance().getTime(), 0);
                    mAdapter.addPerson(newPerson);
                    ((EditText) v).setText("");
                    return true;
                }
                return false;
            }
        });

    }

    @Override public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_done, menu);
        return true;
    }

    @Override public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case R.id.action_menu_done:
                assert mBinding.content != null;
                if(mBinding.content.etName.getText().length() > 0 && mAdapter.getItemCount() > 1) {
                    if(mGroupEntry == null || mGroupEntry.getCreatedAt() == null) {
                        createGroup();
                    } else {
                        updateGroup();
                    }
                }
                finish();
                return true;
        }
        return false;
    }

    @Override protected void onSaveInstanceState(Bundle outState) {
        outState.putParcelableArrayList(EXTRA_PARTICIPANTS, (ArrayList<? extends Parcelable>) mAdapter.mPeople);
        outState.putParcelable(EXTRA_GROUP, mGroupEntry);
        super.onSaveInstanceState(outState);
    }

    private void updateGroup() {
        assert mBinding.content != null;
        mGroupEntry.setName(mBinding.content.etName.getText().toString());
        mGroupEntry.setDescription(mBinding.content.etDescription.getText().toString());
        mGroupEntry.setMainCurrency(mBinding.content.spCurrency.getSelectedItem().toString());
        SpipRepository.getInstance(this).updateGroup(mGroupEntry, mAdapter.mPeople);

        FirebaseUtils.getInstance(this).logToFirebase(mGroupEntry.getId().toString(), "Group updated");
    }

    private void createGroup() {
        assert mBinding.content != null;
        if(mGroupEntry == null) { //TODO - is this a valid use case?
            mGroupEntry = new GroupEntry(0, mBinding.content.etName.getText().toString(),
                    mBinding.content.etDescription.getText().toString(), Calendar.getInstance().getTime(),
                    mBinding.content.spCurrency.getSelectedItem().toString());
        }
        mGroupEntry.setCreatedAt(Calendar.getInstance().getTime());
        SpipRepository.getInstance(this).addGroupWithPeople(mGroupEntry, mAdapter.mPeople);

        FirebaseUtils.getInstance(this).logToFirebase(null, "Group created");
    }

    class ParticipantListAdapter extends RecyclerView.Adapter<ParticipantListAdapter.SimpleVH> {

        private List<PersonEntry> mPeople;

        ParticipantListAdapter(List<PersonEntry> mPeople) {
            this.mPeople = mPeople;
        }

        @NonNull @Override
        public SimpleVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            return new SimpleVH((PersonListItemBinding) DataBindingUtil.inflate(inflater,
                    R.layout.person_list_item, parent, false));
        }

        @Override public int getItemCount() {
            return mPeople.size();
        }

        @Override public void onBindViewHolder(@NonNull SimpleVH holder, int position) {
            holder.mBinding.setPersonEntry(mPeople.get(position));
            holder.mBinding.getRoot().setTag(mPeople.get(position).getId());
        }

        public void addPerson(PersonEntry person) {
            mPeople.add(person);
            notifyDataSetChanged();
        }

        class SimpleVH extends RecyclerView.ViewHolder {

            private PersonListItemBinding mBinding;

            SimpleVH(final PersonListItemBinding itemBinding) {
                super(itemBinding.getRoot());
                mBinding = itemBinding;
            }
        }
    }
}
