/*
*    This file is part of GPSLogger for Android.
*
*    GPSLogger for Android is free software: you can redistribute it and/or modify
*    it under the terms of the GNU General Public License as published by
*    the Free Software Foundation, either version 2 of the License, or
*    (at your option) any later version.
*
*    GPSLogger for Android is distributed in the hope that it will be useful,
*    but WITHOUT ANY WARRANTY; without even the implied warranty of
*    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
*    GNU General Public License for more details.
*
*    You should have received a copy of the GNU General Public License
*    along with GPSLogger for Android.  If not, see <http://www.gnu.org/licenses/>.
*/

//https://www.dropbox.com/developers/start/setup#android

package com.mendhak.gpslogger.senders.dropbox;

import android.content.Intent;
import android.os.Bundle;
import android.preference.Preference;
<<<<<<< HEAD:GPSLogger/src/com/mendhak/gpslogger/senders/dropbox/DropBoxAuthorizationActivity.java

import com.actionbarsherlock.app.SherlockPreferenceActivity;
import com.actionbarsherlock.view.MenuItem;
=======
import android.preference.PreferenceActivity;
import android.view.MenuItem;
>>>>>>> upstream/master:gpslogger/src/main/java/com/mendhak/gpslogger/senders/dropbox/DropBoxAuthorizationActivity.java
import com.mendhak.gpslogger.GpsMainActivity;
import com.mendhak.gpslogger.R;
import com.mendhak.gpslogger.common.Utilities;
import org.slf4j.LoggerFactory;

public class DropBoxAuthorizationActivity extends PreferenceActivity {

    private static final org.slf4j.Logger tracer = LoggerFactory.getLogger(DropBoxAuthorizationActivity.class.getSimpleName());
    DropBoxHelper helper;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActionBar().setDisplayHomeAsUpEnabled(true);

        addPreferencesFromResource(R.xml.dropboxsettings);

        Preference pref = findPreference("dropbox_resetauth");

        helper = new DropBoxHelper(getApplicationContext(), null);

        if (helper.IsLinked()) {
            pref.setTitle(R.string.dropbox_unauthorize);
            pref.setSummary(R.string.dropbox_unauthorize_description);
        } else {
            pref.setTitle(R.string.dropbox_authorize);
            pref.setSummary(R.string.dropbox_authorize_description);
        }

        pref.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                // This logs you out if you're logged in, or vice versa
                if (helper.IsLinked()) {
                    helper.UnLink();
                    startActivity(new Intent(getApplicationContext(), GpsMainActivity.class));
                    finish();
                } else {
                    try {
                        helper.StartAuthentication(DropBoxAuthorizationActivity.this);
                    } catch (Exception e) {
                        tracer.error("DropBoxAuthorizationActivity.onPreferenceClick", e);
                    }
                }

                return true;
            }
        });

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                Intent intent = new Intent(this, GpsMainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);

                break;
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    protected void onResume() {
        super.onResume();

        try {
            if (helper.FinishAuthorization()) {
                startActivity(new Intent(getApplicationContext(), GpsMainActivity.class));
                finish();
            }
        } catch (Exception e) {
            Utilities.MsgBox(getString(R.string.error), getString(R.string.dropbox_couldnotauthorize),
                    DropBoxAuthorizationActivity.this);
            tracer.error("DropBoxAuthorizationActivity.onResume", e);
        }

    }


}
