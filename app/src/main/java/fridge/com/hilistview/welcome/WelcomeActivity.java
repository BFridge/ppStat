package fridge.com.hilistview.welcome;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import fridge.com.hilistview.R;
import fridge.com.hilistview.main.MainActivity;
import fridge.com.hilistview.scroller.ScrollerActivity;
import fridge.com.hilistview.viewpage.ViewPagerActivity;

public class WelcomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wel_come);
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
    }

    public void toScroller(View view){
        Intent intent = new Intent(this, ScrollerActivity.class);
        startActivity(intent);
    }

    public void toListView(View view){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    public void toViewPager(View view){
        Intent intent = new Intent(this, ViewPagerActivity.class);
        startActivity(intent);
    }

}
