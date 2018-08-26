package hu.bendaf.spip.utils;

import android.content.Context;
import android.os.Bundle;

import com.google.firebase.analytics.FirebaseAnalytics;

/**
 * Created by bendaf on 2018. 08. 26. Spip.
 */
public class FirebaseUtils {

    private FirebaseAnalytics mFirebaseAnalytics;
    private static FirebaseUtils sFirebaseUtils;

    private FirebaseUtils(Context c) {
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(c);
    }

    public static synchronized FirebaseUtils getInstance(Context c) {
        if(sFirebaseUtils == null) {
            sFirebaseUtils = new FirebaseUtils(c);
        }
        return sFirebaseUtils;
    }

    public void logToFirebase(String id, String name) {
        Bundle bundle = new Bundle();
        if(id != null) bundle.putString(FirebaseAnalytics.Param.ITEM_ID, id);
        if(name != null) bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, name);
        mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle);
    }
}
