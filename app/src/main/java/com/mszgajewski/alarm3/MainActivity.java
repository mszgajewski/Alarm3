package com.mszgajewski.alarm3;

import androidx.appcompat.app.AppCompatActivity;
import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Toast;
import com.mszgajewski.alarm3.databinding.ActivityMainBinding;
import java.util.Calendar;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding binding;
    private AlarmReceiver alarmReceiver;
    private int mYear, mMonth, mDay, mHour, mMinute;
    private int mHourRepeat, mMinuteRepeat;

    @SuppressLint("DefaultLocale")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        alarmReceiver = new AlarmReceiver();
        Calendar calendar = Calendar.getInstance();
        mYear = calendar.get(Calendar.YEAR);
        mMonth = calendar.get(Calendar.MONTH);
        mDay = calendar.get(Calendar.DAY_OF_MONTH);
        mHour = calendar.get(Calendar.HOUR_OF_DAY);
        mMinute = calendar.get(Calendar.MINUTE);
        mHourRepeat = mHour;
        mMinuteRepeat = mMinute;

        binding.ibOnceDate.setOnClickListener(v -> {
            DatePickerDialog datePickerDialog = new DatePickerDialog(MainActivity.this, (view, year, month, dayOfMonth) -> {
                binding.tvOnceDate.setText(String.format("%04d-%02d-%02d", year,month + 1, dayOfMonth));
                mYear = year;
                mMonth = month;
                mDay = dayOfMonth;
            }, mYear, mMonth, mDay);
            datePickerDialog.show();
        });

        binding.ibOnceTime.setOnClickListener(v -> {
            TimePickerDialog timePickerDialog = new TimePickerDialog(MainActivity.this, (view, hourOfDay, minute) -> {
                binding.tvOnceTime.setText(String.format("%02d:%02d", hourOfDay, minute));
                mHour = hourOfDay;
                mMinute = minute;
            },mHour, mMinute,true);
            timePickerDialog.show();
        });

        binding.ibRepeatingTime.setOnClickListener(v -> {
            TimePickerDialog timePickerDialog = new TimePickerDialog(MainActivity.this, (view, hourOfDay, minute) -> {
                binding.tvRepeatingTime.setText(String.format("%02d:%02d", hourOfDay, minute));
                mHourRepeat = hourOfDay;
                mMinuteRepeat = minute;
            },mHourRepeat, mMinuteRepeat,true);
            timePickerDialog.show();
        });

        binding.btnSetOnceAlarm.setOnClickListener(v -> {
            if (binding.tvOnceDate.getText().toString().equalsIgnoreCase("")){
                Toast.makeText(MainActivity.this, "Brak daty", Toast.LENGTH_SHORT).show();
            } else if (binding.tvOnceTime.getText().toString().equalsIgnoreCase("")) {
                Toast.makeText(MainActivity.this, "Brak godziny", Toast.LENGTH_SHORT).show();
            } else if (TextUtils.isEmpty(binding.etOnceMessage.getText().toString())) {
                binding.etOnceMessage.setError("Treść nie może być pusta");
            } else {
                alarmReceiver.setOneTimeAlarm(MainActivity.this, AlarmReceiver.TYPE_ONE_TIME,
                    binding.tvOnceDate.getText().toString(),
                    binding.tvOnceTime.getText().toString(),
                    binding.etOnceMessage.getText().toString());
            }
        });

        binding.btnSetRepeatingAlarm.setOnClickListener(v -> {
             if (binding.tvRepeatingTime.getText().toString().equalsIgnoreCase("")) {
                Toast.makeText(MainActivity.this, "Brak godziny", Toast.LENGTH_SHORT).show();
            } else if (TextUtils.isEmpty(binding.etRepeatingMessage.getText().toString())) {
                binding.etRepeatingMessage.setError("Treść nie może być pusta");
            } else {
                alarmReceiver.setRepeatingAlarm(MainActivity.this, AlarmReceiver.TYPE_REPEATING,
                    binding.tvRepeatingTime.getText().toString(),
                    binding.etRepeatingMessage.getText().toString());
            }
        });

        binding.btnCancelRepeatingAlarm.setOnClickListener(v -> {
            if (alarmReceiver.isAlarmSet(MainActivity.this, AlarmReceiver.TYPE_REPEATING)){
                binding.tvRepeatingTime.setText("");
                binding.etRepeatingMessage.setText("");
                alarmReceiver.cancelAlarm(MainActivity.this,AlarmReceiver.TYPE_REPEATING);
            }
        });

        binding.btnCancelOnceAlarm.setOnClickListener(v -> {
            if (alarmReceiver.isAlarmSet(MainActivity.this, AlarmReceiver.TYPE_ONE_TIME)){
                binding.tvOnceDate.setText("");
                binding.tvOnceTime.setText("");
                binding.etOnceMessage.setText("");
                alarmReceiver.cancelAlarm(MainActivity.this,AlarmReceiver.TYPE_ONE_TIME);
            }
        });
    }
}