package org.mercycorps.translationcards.media;

import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import java.io.FileInputStream;
import java.io.IOException;

/**
 * Created by njimenez and pdale on 2/18/16.
 */
public class CardAudioClickListener implements View.OnClickListener {
    private static final String TAG = "CardAudioClickListener";
    private String filename;
    private final ProgressBar progressBar;
    private MediaPlayerManager lastMediaPlayerManager;

    public CardAudioClickListener(String audioFilename, ProgressBar progressBar,
                                  MediaPlayerManager lastMediaPlayerManager) {
        this.filename = audioFilename;
        this.progressBar = progressBar;
        this.lastMediaPlayerManager = lastMediaPlayerManager;
    }

    @Override
    public void onClick(View v) {
        stop();
        try {
            lastMediaPlayerManager.play(new FileInputStream(filename).getFD(), progressBar);
        } catch (IOException e) {
            Log.d(TAG, "Error preparing audio.");
            throw new IllegalArgumentException(e);
        }
    }

    public void stop() {
        if (lastMediaPlayerManager != null) {
            lastMediaPlayerManager.stop();
        }
    }
}
