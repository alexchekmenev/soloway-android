package info.guessme.app;

import android.app.Application;
import android.os.Build;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import java.util.Locale;

import ru.yandex.speechkit.Emotion;
import ru.yandex.speechkit.Error;
import ru.yandex.speechkit.Language;
import ru.yandex.speechkit.OnlineVocalizer;
import ru.yandex.speechkit.SpeechKit;
import ru.yandex.speechkit.Synthesis;
import ru.yandex.speechkit.Vocalizer;
import ru.yandex.speechkit.VocalizerListener;
import ru.yandex.speechkit.Voice;

public class DrawerActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, VocalizerListener {

    private OnlineVocalizer vocalizer;

    private TextToSpeech TTS;
    private boolean ttsEnabled = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drawer);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        try {
            SpeechKit.getInstance().init(getApplicationContext(), "4232644e-517d-4896-85c9-857eba6589cb");
        } catch (Exception e) {
            e.printStackTrace();
        }

        vocalizer = new OnlineVocalizer.Builder(Language.RUSSIAN, this)
                .setEmotion(Emotion.GOOD)
                .setVoice(Voice.ZAHAR)
                .build(); // 1
        vocalizer.prepare(); // 2


        TTS = new TextToSpeech(this, new TextToSpeech.OnInitListener() {
            @Override public void onInit(int initStatus) {
                if (initStatus == TextToSpeech.SUCCESS) {
                    if (TTS.isLanguageAvailable(new Locale(Locale.getDefault().getLanguage()))
                            == TextToSpeech.LANG_AVAILABLE) {
                        TTS.setLanguage(new Locale(Locale.getDefault().getLanguage()));
                    } else {
                        TTS.setLanguage(Locale.US);
                    }
                    TTS.setPitch(1.3f);
                    TTS.setSpeechRate(0.7f);
                    ttsEnabled = true;
                } else if (initStatus == TextToSpeech.ERROR) {
                    Toast.makeText(DrawerActivity.this, "Ошибка", Toast.LENGTH_LONG).show();
                    ttsEnabled = false;
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.drawer, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
//            playVoice();
            String s = "Создание удобных и креативных сервисов в помощь туристам. Поиск достопримечательностей неподалеку от местоположения пользователя с отображением рейтинга. Оптимальный маршрут по нескольким точкам интереса.";
            speak(s);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void playVoice() {
        Log.d("GUESSME", "playVoice");
        vocalizer.synthesize("Погода на завтра", Vocalizer.TextSynthesizingMode.APPEND); // 3
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

    public void speak(String text) {
        if (!ttsEnabled) return;
        String utteranceId = this.hashCode() + "";
        TTS.speak(text, TextToSpeech.QUEUE_FLUSH, null, utteranceId);
    }
}
