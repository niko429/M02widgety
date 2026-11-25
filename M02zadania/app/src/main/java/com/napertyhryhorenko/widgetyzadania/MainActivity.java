package com.napertyhryhorenko.widgetyzadania;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private static final String KEY_RESULT_TEXT = "saved_result_text";

    private EditText etWeight;
    private EditText etHeight;
    private TextView tvResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        etWeight = findViewById(R.id.etWeight);
        etHeight = findViewById(R.id.etHeight);
        tvResult = findViewById(R.id.tvResult);
        Button btnCalculate = findViewById(R.id.btnCalculate);
        Button btnClear = findViewById(R.id.btnClear);

        if (savedInstanceState != null) {
            String savedResult = savedInstanceState.getString(KEY_RESULT_TEXT);
            if (savedResult != null) {
                tvResult.setText(savedResult);
            }
        }

        btnCalculate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calculateBmi();
            }
        });

        btnClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clearAll();
            }
        });
    }

    private void calculateBmi() {
        String weightStr = etWeight.getText().toString().trim();
        String heightStr = etHeight.getText().toString().trim();
        boolean isValid = true;

        double weight = 0;
        if (weightStr.isEmpty()) {
            etWeight.setError("Podaj wagę");
            isValid = false;
        } else {
            try {
                weight = Double.parseDouble(weightStr);
                if (weight < 20 || weight > 300) {
                    etWeight.setError("Waga musi być z zakresu 20 - 300 kg");
                    isValid = false;
                }
            } catch (NumberFormatException e) {
                etWeight.setError("Niepoprawna liczba");
                isValid = false;
            }
        }

        double heightCm = 0;
        if (heightStr.isEmpty()) {
            etHeight.setError("Podaj wzrost");
            isValid = false;
        } else {
            try {
                heightCm = Double.parseDouble(heightStr);
                if (heightCm < 100 || heightCm > 250) {
                    etHeight.setError("Wzrost musi być z zakresu 100 - 250 cm");
                    isValid = false;
                }
            } catch (NumberFormatException e) {
                etHeight.setError("Niepoprawna liczba");
                isValid = false;
            }
        }

        if (!isValid) {
            return;
        }

        double heightM = heightCm / 100.0;
        double bmi = weight / (heightM * heightM);

        String category;
        if (bmi < 18.5) {
            category = "Niedowaga";
        } else if (bmi < 25.0) {
            category = "Waga prawidłowa";
        } else if (bmi < 30.0) {
            category = "Nadwaga";
        } else {
            category = "Otyłość";
        }

        String resultText = String.format(Locale.US, "Twoje BMI: %.2f\nKategoria: %s", bmi, category);
        tvResult.setText(resultText);
    }

    private void clearAll() {
        etWeight.setText("");
        etHeight.setText("");

        etWeight.setError(null);
        etHeight.setError(null);

        tvResult.setText("Wpisz dane i kliknij Oblicz BMI");
        etWeight.requestFocus();
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(KEY_RESULT_TEXT, tvResult.getText().toString());
    }
}