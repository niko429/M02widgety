package com.napertyhryhorenko.widgetyzadania;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private static final String KEY_RESULT_TEXT = "saved_result_text";

    private RadioGroup rgUnits;
    private EditText etWeight;
    private EditText etHeight;
    private TextView tvResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        rgUnits = findViewById(R.id.rgUnits);
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

        updateHints();

        rgUnits.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                updateHints();
                etWeight.setError(null);
                etHeight.setError(null);
            }
        });

        etWeight.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                validateWeightLive(s.toString().trim());
            }
        });

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

    private boolean isMetric() {
        return rgUnits.getCheckedRadioButtonId() == R.id.rbMetric;
    }

    private void updateHints() {
        if (isMetric()) {
            etWeight.setHint("Waga w kg (20 - 300)");
            etHeight.setHint("Wzrost w cm (100 - 250)");
        } else {
            etWeight.setHint("Waga w lb (44 - 660)");
            etHeight.setHint("Wzrost w in (39 - 98)");
        }
    }

    private void validateWeightLive(String text) {
        if (text.isEmpty()) {
            etWeight.setError(null);
            return;
        }

        double minWeight = isMetric() ? 20.0 : 44.0;
        double maxWeight = isMetric() ? 300.0 : 660.0;
        String unit = isMetric() ? "kg" : "lb";

        try {
            double weight = Double.parseDouble(text);
            if (weight < minWeight || weight > maxWeight) {
                etWeight.setError(String.format(Locale.US, "Waga musi być z zakresu %.0f - %.0f %s", minWeight, maxWeight, unit));
            } else {
                etWeight.setError(null);
            }
        } catch (NumberFormatException e) {
            etWeight.setError("Niepoprawny format liczby");
        }
    }

    private void calculateBmi() {
        String weightStr = etWeight.getText().toString().trim();
        String heightStr = etHeight.getText().toString().trim();
        boolean isValid = true;
        boolean metric = isMetric();

        double minW = metric ? 20.0 : 44.0;
        double maxW = metric ? 300.0 : 660.0;
        String unitW = metric ? "kg" : "lb";

        double minH = metric ? 100.0 : 39.0;
        double maxH = metric ? 250.0 : 98.0;
        String unitH = metric ? "cm" : "in";

        double weight = 0;
        if (weightStr.isEmpty()) {
            etWeight.setError("Podaj wagę");
            isValid = false;
        } else {
            try {
                weight = Double.parseDouble(weightStr);
                if (weight < minW || weight > maxW) {
                    etWeight.setError(String.format(Locale.US, "Waga musi być z zakresu %.0f - %.0f %s", minW, maxW, unitW));
                    isValid = false;
                }
            } catch (NumberFormatException e) {
                etWeight.setError("Niepoprawna liczba");
                isValid = false;
            }
        }

        double height = 0;
        if (heightStr.isEmpty()) {
            etHeight.setError("Podaj wzrost");
            isValid = false;
        } else {
            try {
                height = Double.parseDouble(heightStr);
                if (height < minH || height > maxH) {
                    etHeight.setError(String.format(Locale.US, "Wzrost musi być z zakresu %.0f - %.0f %s", minH, maxH, unitH));
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
        double bmi;
        if (metric) {
            double heightM = height / 100.0;
            bmi = weight / (heightM * heightM);
        } else {
            bmi = (weight / (height * height)) * 703.0;
        }

        // 4. Kategoria BMI
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

        String systemLabel = metric ? "kg/cm" : "lb/in";
        String resultText = String.format(Locale.US, "Twoje BMI (%s): %.2f\nKategoria: %s", systemLabel, bmi, category);
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