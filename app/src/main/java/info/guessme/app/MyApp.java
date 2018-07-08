package info.guessme.app;

import android.app.Application;
import android.support.annotation.NonNull;
import android.util.Log;


/**
 * Created by creed on 07.07.18.
 */

public class MyApp extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d("GUESSME", "app.onCreate()");
    }

}
