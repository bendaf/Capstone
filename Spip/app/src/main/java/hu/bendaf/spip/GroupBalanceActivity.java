package hu.bendaf.spip;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import hu.bendaf.spip.databinding.ActivityGroupBalanceBinding;

public class GroupBalanceActivity extends AppCompatActivity {

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    private SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private long mGroupId = -1;
    public static String EXTRA_GROUP_ID = "groupId";
    private ActivityGroupBalanceBinding mBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_group_balance);

        setSupportActionBar(mBinding.toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mBinding.container.setAdapter(mSectionsPagerAdapter);

        mBinding.container.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(mBinding.tabs));
        mBinding.tabs.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(mBinding.container));

        mBinding.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                AsyncTask<Void, Void, Boolean> a = new MyAsyncTask(view, GroupBalanceActivity.this);
                a.execute();
            }
        });

        if(getIntent().hasExtra(EXTRA_GROUP_ID)) {
            mGroupId = getIntent().getLongExtra(EXTRA_GROUP_ID, -1);
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_group_balance, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if(id == R.id.action_edit) {
            Intent editGroupActivity = new Intent(GroupBalanceActivity.this, AddGroupActivity.class);
            editGroupActivity.putExtra(EXTRA_GROUP_ID, mGroupId);
            startActivity(editGroupActivity);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    static class MyAsyncTask extends AsyncTask<Void, Void, Boolean> {
        private ProgressDialog pd;
        @SuppressLint("StaticFieldLeak") private View view;
        @SuppressLint("StaticFieldLeak") Context mContext;

        MyAsyncTask(View v, Context c) {
            this.view = v;
            mContext = c;
            pd = new ProgressDialog(mContext);
        }

        @Override protected void onPreExecute() {
            pd.setMessage(mContext.getResources().getString(R.string.text_loading));
            pd.show();
        }

        @Override protected void onPostExecute(Boolean aVoid) {
            if(pd.isShowing()) {
                pd.dismiss();
            }
            Snackbar.make(view, R.string.text_task_done, Snackbar.LENGTH_LONG).show();
        }

        @Override protected Boolean doInBackground(Void... voids) {
            try {
                Thread.sleep(3000);
            } catch(InterruptedException e) {
                e.printStackTrace();
                return false;
            }
            return true;
        }
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        public PlaceholderFragment() {
        }

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_group_balance, container, false);
//            TextView textView = (TextView) rootView.findViewById(R.id.section_label);
//            textView.setText(getString(R.string.section_format, getArguments().getInt(ARG_SECTION_NUMBER)));
            return rootView;
        }
    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            return PlaceholderFragment.newInstance(position + 1);
        }

        @Override
        public int getCount() {
            return 2;
        }
    }
}
