package info.guessme.app;

import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.AppBarLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class ScrollingActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scrolling);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("");
        }

        AppBarLayout appBarLayout = (AppBarLayout) findViewById(R.id.app_bar);

        final Button playButton = (Button) findViewById(R.id.play_button);
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
    }
}
