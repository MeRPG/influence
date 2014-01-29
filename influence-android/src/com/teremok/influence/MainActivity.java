package com.teremok.influence;

import android.os.Bundle;

import android.widget.RelativeLayout;
import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;

import java.util.Locale;

public class MainActivity extends AndroidApplication {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        AndroidApplicationConfiguration cfg = new AndroidApplicationConfiguration();
        cfg.useGL20 = false;
        cfg.useWakelock = true;
        cfg.hideStatusBar = false;
        RelativeLayout layout = new RelativeLayout(this);

        Locale locale = getResources().getConfiguration().locale;

        layout.addView(initializeForView(new Influence(locale), cfg));
        setContentView(layout);
    }
}