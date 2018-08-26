package hu.bendaf.spip;

import android.arch.lifecycle.Observer;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.ads.AdRequest;

import java.util.ArrayList;
import java.util.List;

import hu.bendaf.spip.data.GroupEntry;
import hu.bendaf.spip.data.SpipDatabase;
import hu.bendaf.spip.databinding.ActivityMainBinding;
import hu.bendaf.spip.databinding.GroupListItemBinding;
import hu.bendaf.spip.utils.FirebaseUtils;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private ActivityMainBinding mBinding;
    private GroupListAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);

        FirebaseUtils.getInstance(this).logToFirebase(null, "app started");
        mAdapter = new GroupListAdapter();
        mBinding.rvGroups.setAdapter(mAdapter);

        SpipDatabase.getInstance(this).spipDao().getGroups().observe(this, new Observer<List<GroupEntry>>() {
            @Override public void onChanged(@Nullable List<GroupEntry> groupEntries) {
                mAdapter.updateGroups(groupEntries);
            }
        });

        mBinding.fabAddGroup.setOnClickListener(this);

        mBinding.adView.loadAd(new AdRequest.Builder().build());
    }

    @Override
    public void onClick(View v) {
        if(v.getTag() != null && v.getTag() instanceof Long) {
            FirebaseUtils.getInstance(this).logToFirebase(v.getTag().toString(), "Group opened");

            Intent groupBalanceActivity = new Intent(MainActivity.this, GroupBalanceActivity.class);
            groupBalanceActivity.putExtra(GroupBalanceActivity.EXTRA_GROUP_ID, (Long) v.getTag());
            startActivity(groupBalanceActivity);
        } else {
            FirebaseUtils.getInstance(this).logToFirebase(null, "Create group clicked");

            Intent addGroupActivity = new Intent(this, AddGroupActivity.class);
            startActivity(addGroupActivity);
        }
    }

    class GroupListAdapter extends RecyclerView.Adapter<GroupListAdapter.SimpleVH> {

        private List<GroupEntry> mGroups = new ArrayList<>();

        @NonNull @Override
        public SimpleVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            return new SimpleVH((GroupListItemBinding) DataBindingUtil.inflate(inflater,
                    R.layout.group_list_item, parent, false));
        }

        @Override public int getItemCount() {
            return mGroups.size();
        }

        @Override public void onBindViewHolder(@NonNull SimpleVH holder, int position) {
            holder.mBinding.setGroupEntry(mGroups.get(position));
            holder.mBinding.getRoot().setTag(mGroups.get(position).getId());
        }

        public void updateGroups(List<GroupEntry> groups) {
            this.mGroups = groups;
            notifyDataSetChanged();
        }

        class SimpleVH extends RecyclerView.ViewHolder {

            private GroupListItemBinding mBinding;

            SimpleVH(final GroupListItemBinding itemBinding) {
                super(itemBinding.getRoot());
                mBinding = itemBinding;
                mBinding.getRoot().setOnClickListener(MainActivity.this);
            }
        }
    }

}
