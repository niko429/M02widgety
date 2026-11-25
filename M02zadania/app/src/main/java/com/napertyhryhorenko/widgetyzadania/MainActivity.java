package com.napertyhryhorenko.widgetyzadania;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Powiązanie widoków z kodem
        EditText etName = findViewById(R.id.etName);
        CheckBox cbConsent = findViewById(R.id.cbConsent);
        SwitchCompat swNewsletter = findViewById(R.id.swNewsletter);
        RadioGroup rgSize = findViewById(R.id.rgSize);
        SeekBar sbVolume = findViewById(R.id.sbVolume);
        Button btnSubmit = findViewById(R.id.btnSubmit);
        TextView tvSummary = findViewById(R.id.tvSummary);

        // Obsługa kliknięcia przycisku
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 1. Odczyt z EditText (jeśli puste, wpisujemy "Brak")
                String name = etName.getText().toString().trim();
                if (name.isEmpty()) {
                    name = "Brak";
                }

                // 2. Odczyt z CheckBox
                String consent = cbConsent.isChecked() ? "tak" : "nie";

                // 3. Odczyt ze Switch
                String pushStatus = swNewsletter.isChecked() ? "włączone" : "wyłączone";

                // 4. Odczyt z RadioGroup (sprawdzamy, który RadioButton jest zaznaczony)
                int selectedRadioId = rgSize.getCheckedRadioButtonId();
                String size = "nie wybrano";
                if (selectedRadioId != -1) {
                    RadioButton selectedRadioButton = findViewById(selectedRadioId);
                    size = selectedRadioButton.getText().toString();
                }

                // 5. Odczyt z SeekBar
                int volume = sbVolume.getProgress();

                // Złożenie tekstu i wyświetlenie w TextView
                String resultText = "Imię: " + name +
                        ", zgoda: " + consent +
                        ", powiadomienia: " + pushStatus +
                        ", rozmiar: " + size +
                        ", głośność: " + volume;

                tvSummary.setText(resultText);
            }
        });
    }
}