package com.napertyhryhorenko.widgetyzadania;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private static final String KEY_ROOM_ERR = "key_room_err";
    private static final String KEY_CATERING_ERR = "key_catering_err";
    private static final String KEY_TERMS_ERR = "key_terms_err";

    private EditText etEventName;
    private EditText etParticipants;
    private Spinner spRoom;
    private RadioGroup rgCatering;
    private CheckBox cbTerms;

    private TextView tvRoomError;
    private TextView tvCateringError;
    private TextView tvTermsError;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        etEventName = findViewById(R.id.etEventName);
        etParticipants = findViewById(R.id.etParticipants);
        spRoom = findViewById(R.id.spRoom);
        rgCatering = findViewById(R.id.rgCatering);
        cbTerms = findViewById(R.id.cbTerms);

        tvRoomError = findViewById(R.id.tvRoomError);
        tvCateringError = findViewById(R.id.tvCateringError);
        tvTermsError = findViewById(R.id.tvTermsError);

        Button btnSave = findViewById(R.id.btnSave);
        Button btnClear = findViewById(R.id.btnClear);

        if (savedInstanceState != null) {
            restoreErrorTextView(tvRoomError, savedInstanceState.getString(KEY_ROOM_ERR));
            restoreErrorTextView(tvCateringError, savedInstanceState.getString(KEY_CATERING_ERR));
            restoreErrorTextView(tvTermsError, savedInstanceState.getString(KEY_TERMS_ERR));
        }

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validateAndSave();
            }
        });

        btnClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clearForm();
            }
        });
    }

    private void validateAndSave() {
        boolean isValid = true;

        String eventName = etEventName.getText().toString().trim();
        if (eventName.isEmpty()) {
            etEventName.setError(getString(R.string.err_event_empty));
            isValid = false;
        } else if (eventName.length() < 3) {
            etEventName.setError(getString(R.string.err_event_short));
            isValid = false;
        } else {
            etEventName.setError(null);
        }

        String participantsStr = etParticipants.getText().toString().trim();
        int participants = 0;
        if (participantsStr.isEmpty()) {
            etParticipants.setError(getString(R.string.err_participants_empty));
            isValid = false;
        } else {
            try {
                participants = Integer.parseInt(participantsStr);
                if (participants < 1 || participants > 50) {
                    etParticipants.setError(getString(R.string.err_participants_range));
                    isValid = false;
                } else {
                    etParticipants.setError(null);
                }
            } catch (NumberFormatException e) {
                etParticipants.setError(getString(R.string.err_participants_format));
                isValid = false;
            }
        }

        if (spRoom.getSelectedItemPosition() == 0) {
            showErrorTextView(tvRoomError, getString(R.string.err_room_required));
            isValid = false;
        } else {
            hideErrorTextView(tvRoomError);
        }

        int selectedCateringId = rgCatering.getCheckedRadioButtonId();
        if (selectedCateringId == -1) {
            showErrorTextView(tvCateringError, getString(R.string.err_catering_required));
            isValid = false;
        } else {
            hideErrorTextView(tvCateringError);
        }

        if (!cbTerms.isChecked()) {
            showErrorTextView(tvTermsError, getString(R.string.err_terms_required));
            isValid = false;
        } else {
            hideErrorTextView(tvTermsError);
        }

        if (isValid) {
            String selectedRoom = spRoom.getSelectedItem().toString();
            RadioButton selectedRadio = findViewById(selectedCateringId);
            String selectedCatering = selectedRadio.getText().toString();

            String summary = getString(
                    R.string.toast_summary_format,
                    eventName,
                    participants,
                    selectedRoom,
                    selectedCatering
            );

            Toast.makeText(this, summary, Toast.LENGTH_LONG).show();
        }
    }

    private void clearForm() {
        etEventName.setText("");
        etParticipants.setText("");
        spRoom.setSelection(0);
        rgCatering.clearCheck();
        cbTerms.setChecked(false);

        etEventName.setError(null);
        etParticipants.setError(null);
        hideErrorTextView(tvRoomError);
        hideErrorTextView(tvCateringError);
        hideErrorTextView(tvTermsError);

        etEventName.requestFocus();
    }

    private void showErrorTextView(TextView tv, String message) {
        tv.setText(message);
        tv.setVisibility(View.VISIBLE);
    }

    private void hideErrorTextView(TextView tv) {
        tv.setText("");
        tv.setVisibility(View.GONE);
    }

    private void restoreErrorTextView(TextView tv, String savedMessage) {
        if (savedMessage != null && !savedMessage.isEmpty()) {
            showErrorTextView(tv, savedMessage);
        }
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        if (tvRoomError.getVisibility() == View.VISIBLE) {
            outState.putString(KEY_ROOM_ERR, tvRoomError.getText().toString());
        }
        if (tvCateringError.getVisibility() == View.VISIBLE) {
            outState.putString(KEY_CATERING_ERR, tvCateringError.getText().toString());
        }
        if (tvTermsError.getVisibility() == View.VISIBLE) {
            outState.putString(KEY_TERMS_ERR, tvTermsError.getText().toString());
        }
    }
}