package hu.bendaf.spip;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
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

public class AddGroupActivity extends AppCompatActivity {

    ActivityAddGroupBinding mBinding;
    private ParticipantListAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_add_group);
        mBinding.setGroupEntry(new GroupEntry(0, null, null, null, null));
        setSupportActionBar(mBinding.toolbar);

        //noinspection ConstantConditions
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        List<String> currencies = Arrays.asList(getResources().getStringArray(R.array.list_currencies));
        assert mBinding.content != null;
        mBinding.content.spCurrency.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_spinner_item,
                currencies));

        mAdapter = new ParticipantListAdapter();
        mBinding.content.rvParticipants.setAdapter(mAdapter);

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
                if(mBinding.content.etName.getText().length() > 0 && mAdapter.getItemCount() > 0) {
                    createGroup();
                }
                finish();
                return true;
        }
        return false;
    }

    private void createGroup() {
        GroupEntry newGroup = new GroupEntry(0, mBinding.content.etName.getText().toString(),
                mBinding.content.etDescription.getText().toString(), Calendar.getInstance().getTime(),
                mBinding.content.spCurrency.getSelectedItem().toString());

        SpipRepository.getInstance(this, AppExecutors.getInstance()).addGroupWithPeople(newGroup, mAdapter.mPeople);
    }

    class ParticipantListAdapter extends RecyclerView.Adapter<ParticipantListAdapter.SimpleVH> {

        private List<PersonEntry> mPeople = new ArrayList<>();

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
