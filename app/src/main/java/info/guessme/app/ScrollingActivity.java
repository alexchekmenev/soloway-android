package info.guessme.app;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.AppBarLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.MultiTransformation;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;

import java.util.Locale;

import static com.bumptech.glide.request.RequestOptions.bitmapTransform;

public class ScrollingActivity extends AppCompatActivity {

    private TextToSpeech TTS;
    private boolean ttsEnabled = false;

    private boolean isPlaying = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scrolling);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("");
        }

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
                    Toast.makeText(ScrollingActivity.this, "Ошибка", Toast.LENGTH_LONG).show();
                    ttsEnabled = false;
                }
            }
        });

        AppBarLayout appBarLayout = (AppBarLayout) findViewById(R.id.app_bar);

        final Button playButton = (Button) findViewById(R.id.play_button);

        playButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isPlaying) {
                    playButton.setText(R.string.play_button_text_playing);
                    playButton.setCompoundDrawablesWithIntrinsicBounds(R.mipmap.ic_stop_white_18, 0, 0, 0);
                    play(R.string.large_text);
                } else {
                    playButton.setText(R.string.play_button_text);
                    playButton.setCompoundDrawablesWithIntrinsicBounds(R.mipmap.ic_play_24, 0, 0, 0);
                    stop();
                }
                isPlaying = !isPlaying;
            }
        });

        final TextView textHeader = (TextView) findViewById(R.id.text_header);

        final TextView textSubHeader = (TextView) findViewById(R.id.text_sub_header);
        final ConstraintLayout placeStats = (ConstraintLayout) findViewById(R.id.place_stats);

        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                int diff = appBarLayout.getTotalScrollRange() - Math.abs(verticalOffset);
                double alpha = Math.min((1.0 * diff + 0) / appBarLayout.getTotalScrollRange(), 1.0);
//                textHeader.setAlpha(((float) alpha));
                textSubHeader.setAlpha(((float) alpha));
                placeStats.setAlpha(((float) alpha));
                playButton.setAlpha(((float) alpha));
                if (diff == 0) {
                    // Collapsed
                    textHeader.setVisibility(View.GONE);
                    textSubHeader.setVisibility(View.GONE);
                    placeStats.setVisibility(View.GONE);
                    playButton.setVisibility(View.GONE);

//                    setHeaderAsToolbarTitle(textHeader);
//                    textHeader.setAlpha(0);
//                    if ("".equals(getSupportActionBar().getTitle())) {
//                        getSupportActionBar().setDisplayShowTitleEnabled(true);
//                        if (textHeader.getText() != null) {
//                            getSupportActionBar().setTitle(textHeader.getText());
//                        } else {
//                            getSupportActionBar().setTitle(getString(R.string.header_place_name));
//                        }
//                    }
                } else {
                    // Expanded
                    textHeader.setVisibility(View.VISIBLE);
                    textSubHeader.setVisibility(View.VISIBLE);
                    placeStats.setVisibility(View.VISIBLE);
                    playButton.setVisibility(View.VISIBLE);

//                    textHeader.setAlpha(1);
//                    if (!"".equals(getSupportActionBar().getTitle())) {
//                        Log.d("GUESSME", "title is not empty");
//                        getSupportActionBar().setDisplayShowTitleEnabled(false);
//                        getSupportActionBar().setTitle("");
//                    }

                }
            }
        });

//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });

        ImageView imageView = (ImageView) findViewById(R.id.imageView);
        MultiTransformation<Bitmap> multi = new MultiTransformation<>(
                new CenterCrop()
        );
        Glide.with(this)
                .load(R.drawable.sample)
                .apply(bitmapTransform(multi))
                .into(imageView);

        Button continueButton = (Button) findViewById(R.id.continue_button);
        continueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nextPlace();
            }
        });
    }

    public void play(int resId) {
        String text = getResources().getString(resId);
        if (!ttsEnabled) return;
        String utteranceId = this.hashCode() + "";
        TTS.speak(text, TextToSpeech.QUEUE_FLUSH, null, utteranceId);
    }

    public void stop() {
        String utteranceId = this.hashCode() + "";
        TTS.stop();
    }

    private void nextPlace() {
        Log.d("GUESSME", "nextPlace");
        if (ttsEnabled) {
            TTS.stop();
            TTS.shutdown();
        }

        Intent intent = new Intent(ScrollingActivity.this, RouteActivity.class);
//        intent.setFlags(intent.getFlags() | Intent.FLAG_ACTIVITY_NO_HISTORY); // Adds the FLAG_ACTIVITY_NO_HISTORY flag
        startActivity(intent);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (ttsEnabled) {
            TTS.stop();
            TTS.shutdown();
        }
    }
}
