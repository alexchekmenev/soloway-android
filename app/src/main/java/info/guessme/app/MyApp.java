package info.guessme.app;

import android.app.Application;
import android.support.annotation.NonNull;
import android.util.Log;

import ru.yandex.speechkit.Error;
import ru.yandex.speechkit.OnlineVocalizer;
import ru.yandex.speechkit.SpeechKit;
import ru.yandex.speechkit.Synthesis;
import ru.yandex.speechkit.Vocalizer;
import ru.yandex.speechkit.VocalizerListener;

/**
 * Created by creed on 07.07.18.
 */

public class MyApp extends Application  implements VocalizerListener {

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d("GUESSME", "app.onCreate()");
    }

    @Override
    public void onSynthesisDone(@NonNull Vocalizer vocalizer) {
        Log.d("GUESSME", "onSynthesisDone");
        vocalizer.play();
    }

    @Override
    public void onPartialSynthesis(@NonNull Vocalizer vocalizer, @NonNull Synthesis synthesis) {
        Log.d("GUESSME", "onSynthesisDone");
    }

    @Override
    public void onPlayingBegin(@NonNull Vocalizer vocalizer) {
        Log.d("GUESSME", "onPlayingBegin");
    }

    @Override
    public void onPlayingDone(@NonNull Vocalizer vocalizer) {
        Log.d("GUESSME", "onPlayingDone");
    }

    @Override
    public void onVocalizerError(@NonNull Vocalizer vocalizer, @NonNull Error error) {
        Log.e("GUESSME", error.getMessage());
    }
}
