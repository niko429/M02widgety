package com.napertyhryhorenko.widgetyzadania;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "CYKL_ZYCIA";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.d(TAG, "onCreate() - tworzenie aktywności");

        Button btnFinish = findViewById(R.id.btnFinish);
        btnFinish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "Kliknięto przycisk Zamknij -> wywołuję finish()");
                finish();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d(TAG, "onStart() - aktywność staje się widoczna");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "onResume() - aktywność na pierwszym planie (interakcja z użytkownikiem)");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d(TAG, "onPause() - aktywność traci fokus (częściowo zasłonięta lub pauzowana)");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d(TAG, "onStop() - aktywność całkowicie niewidoczna (w tle)");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy() - niszczenie aktywności i zwalnianie zasobów");
    }
}