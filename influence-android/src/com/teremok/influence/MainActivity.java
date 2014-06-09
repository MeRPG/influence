package com.teremok.influence;

import android.os.Bundle;
import android.util.Log;
import android.widget.RelativeLayout;
import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.flurry.android.FlurryAgent;
import com.teremok.influence.util.FlurryHelper;
import com.teremok.influence.util.NotForPublicAccess;

import java.util.Locale;

public class MainActivity extends AndroidApplication {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        AndroidApplicationConfiguration cfg = new AndroidApplicationConfiguration();
        cfg.useWakelock = true;
        cfg.hideStatusBar = false;
        RelativeLayout layout = new RelativeLayout(this);

        Locale locale = getResources().getConfiguration().locale;

        layout.addView(initializeForView(new Influence(locale), cfg));
        setContentView(layout);
    }

    @Override
    protected void onStart() {
        super.onStart();
        FlurryAgent.onStartSession(this, NotForPublicAccess.FLURRY_CODE);

        FlurryAgent.setLogEnabled(true);
        FlurryAgent.setLogEvents(true);
        FlurryAgent.setLogLevel(Log.VERBOSE);

        FlurryHelper.enabled = true;
    }

    @Override
    protected void onStop() {
        super.onStop();
        FlurryAgent.onEndSession(this);
    }
}