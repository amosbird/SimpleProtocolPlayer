/*
 * Copyright (C) 2011 The Android Open Source Project
 * Copyright (C) 2014 kaytat
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.kaytat.simpleprotocolplayer;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.view.KeyEvent;
import android.util.Log;

import androidx.core.content.ContextCompat;

/**
 * Receives broadcasted intents. In particular, we are interested in the
 * android.media.AUDIO_BECOMING_NOISY and android.intent.action.MEDIA_BUTTON intents, which is
 * broadcast, for example, when the user disconnects the headphones. This class works because we are
 * declaring it in a &lt;receiver&gt; tag in AndroidManifest.xml.
 */
public class MusicIntentReceiver extends BroadcastReceiver {
    static final String TAG = "MusicIntentReceiver";
    public static final String ACTION_START = "com.kaytat.simpleprotocolplayer.action.START";

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals(android.media.AudioManager.ACTION_AUDIO_BECOMING_NOISY)) {
            Log.i(TAG, "onReceive - headphones disconnected.  Stopping");
            // send an intent to our MusicService to telling it to pause the audio
			ContextCompat.startForegroundService(context, new Intent(context, MusicService.class).setAction(MusicService.ACTION_STOP));

        } else if (intent.getAction().equals(Intent.ACTION_MEDIA_BUTTON)) {
            KeyEvent keyEvent = (KeyEvent) intent.getExtras().get(Intent.EXTRA_KEY_EVENT);
            if (keyEvent.getAction() != KeyEvent.ACTION_DOWN)
                return;

            switch (keyEvent.getKeyCode()) {
                case KeyEvent.KEYCODE_MEDIA_STOP:
                    Log.i(TAG, "onReceive - media button stop");
                    ContextCompat.startForegroundService(context, new Intent(context, MusicService.class).setAction(MusicService.ACTION_STOP));
                    break;
            }
        } else if (intent.getAction().equals(ACTION_START)) {
            MusicService.start(context, intent);
        }
    }
}
