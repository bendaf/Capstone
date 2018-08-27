package hu.bendaf.spip;

import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import java.util.List;

import hu.bendaf.spip.data.GroupEntry;
import hu.bendaf.spip.data.SpipRepository;

/**
 * Created by bendaf on 2018. 08. 27. Spip.
 */
public class GroupsWidgetAdapter extends RemoteViewsService {
    @Override public RemoteViewsFactory onGetViewFactory(Intent intent) {

        return new GroupWidgetFactory(getApplicationContext());
    }

    private class GroupWidgetFactory implements RemoteViewsFactory {

        private Context mContext;
        private List<GroupEntry> mGroups;

        GroupWidgetFactory(Context mContext) {
            this.mContext = mContext;
        }

        @Override public void onCreate() {

        }

        @Override public void onDataSetChanged() {
            mGroups = SpipRepository.getInstance(mContext).loadGroupsForWidget();
        }

        @Override public void onDestroy() {

        }

        @Override public int getCount() {
            return (mGroups == null) ? 0 : mGroups.size();
        }

        @Override public RemoteViews getViewAt(int position) {
            if(mGroups == null || mGroups.size() <= position) return null;
            RemoteViews rv = new RemoteViews(getPackageName(), R.layout.group_list_item);
            rv.setTextViewText(R.id.tv_group_name, mGroups.get(position).getName());
            return rv;
        }

        @Override public RemoteViews getLoadingView() {
            return null;
        }

        @Override public int getViewTypeCount() {
            return 1;
        }

        @Override public long getItemId(int position) {
            return position;
        }

        @Override public boolean hasStableIds() {
            return true;
        }
    }
}
