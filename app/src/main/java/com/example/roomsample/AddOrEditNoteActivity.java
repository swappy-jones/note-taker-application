package com.example.roomsample;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;
import com.oshi.libsearchtoolbar.SearchAnimationToolbar;

import java.sql.Time;
import java.util.Calendar;
import java.util.Date;
import java.util.IllegalFormatCodePointException;

public class AddOrEditNoteActivity extends AppCompatActivity {
    private Intent intent;
    private NoteEntity noteEntity;
    private EditText edtTitle,edtDescription;
    private UpdateNoteViewModel updateNoteViewModel;
    private View app_bar;
    private boolean isNoteSaved = false;
    private int selectedYear,selectedMonth,selectedDayOfMonth;
    private int selectedHour,selectedMinutes;
    private boolean isAlarmSet;
    private Menu currentMenu;
    private String dateOfMonth,monthOfYear,year,hour,minute;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_or_edit_note);
        manageToolbar();
        edtTitle = findViewById(R.id.edtTitle);
        edtDescription = findViewById(R.id.edtDescription);
    }

    private void populateValues() {
        edtTitle.setText(noteEntity.getTitle());
        edtDescription.setText(noteEntity.getDescription());
        isAlarmSet = noteEntity.isAlarmSet();
        String arr[] = noteEntity.getAlarmDate().split(" ");
        dateOfMonth = arr[0];
        monthOfYear = arr[1];
        year = arr[2];
        hour = arr[3];
        minute = arr[4];
    }

    @Override
    protected void onPause() {
        super.onPause();
        insertOrUpdateNotes();
    }

    private void manageToolbar() {
        if (app_bar==null){
            app_bar = findViewById(R.id.header);
            Toolbar toolbar = app_bar.findViewById(R.id.includeToolbar);
            /*toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.arrow_left));*/
            setSupportActionBar(toolbar);
            ActionBar supportActionBar = getSupportActionBar();
            supportActionBar.setDisplayShowTitleEnabled(false);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.arrow_left);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        intent = getIntent();
        updateNoteViewModel = ViewModelProviders.of(this).get(UpdateNoteViewModel.class);
        if (intent.hasExtra(ExtraUtil.EDIT_NOTE)){
            noteEntity = (NoteEntity) intent.getSerializableExtra(ExtraUtil.EDIT_NOTE);
            populateValues();
        }
        getMenuInflater().inflate(R.menu.header_menu,menu);
        currentMenu = menu;
        menu.findItem(R.id.action_search).setVisible(false);
        menu.findItem(R.id.action_cancel).setVisible(false);
        if (noteEntity!=null){
            if (noteEntity.isAlarmSet()){
                menu.findItem(R.id.action_alarm).setVisible(false);
            }else{
                menu.findItem(R.id.action_cancel_alarm).setVisible(false);
            }
        }else {
            menu.findItem(R.id.action_cancel_alarm).setVisible(false);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_save:
                insertOrUpdateNotes();
                InputMethodManager imm = (InputMethodManager)
                        getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(edtTitle.getWindowToken(),0);
                Snackbar.make(findViewById(android.R.id.content),"Saved",Snackbar.LENGTH_SHORT).show();
                return true;
            case android.R.id.home:
                insertOrUpdateNotes();
                finish();
                return true;
            case R.id.action_alarm:
                imm = (InputMethodManager)
                        getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(edtTitle.getWindowToken(),0);
                startAlarmPicker();
                return true;
            case R.id.action_cancel_alarm:
                showDialogForAlarmCancellation();
                return true;
            case R.id.action_pin:
                Toast.makeText(this,"Pinned",Toast.LENGTH_SHORT).show();
                return true;

        }
        return true;
    }

    private void showDialogForAlarmCancellation() {
        android.app.AlertDialog.Builder builder;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            builder = new android.app.AlertDialog.Builder(this, android.R.style.Theme_Material_Dialog_Alert);
        } else {
            builder = new android.app.AlertDialog.Builder(this);
        }
//        builder.setIcon(R.drawable.app_icon);
        builder.setTitle("Info")
                .setMessage("You have a reminder set for "+hour+" : "+minute+" on "+dateOfMonth+"/"+monthOfYear+"/"+year+"."+"\nDo You want to cancel the reminder?")
                .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // TODO Exit from app
                        isAlarmSet=false;
                        cancelAlarm();
                        currentMenu.findItem(R.id.action_cancel_alarm).setVisible(false);
                        currentMenu.findItem(R.id.action_alarm).setVisible(true);
                    }
                })
                .setNegativeButton("NO", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                })
                .setIcon(android.R.drawable.ic_dialog_info)
                .show();
    }

    private void startAlarmPicker() {
        final Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        DatePickerDialog datePickerDialog = new DatePickerDialog(this,mDateSetListener,year,month,day);
        datePickerDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.WHITE));
        datePickerDialog.show();
    }

    private DatePickerDialog.OnDateSetListener mDateSetListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker datePicker, int year, int month, int dayOfMonth) {
            selectedYear = year;
            selectedDayOfMonth = dayOfMonth;
            selectedMonth = month;
            AddOrEditNoteActivity.this.year = year+"";
            monthOfYear = month+"";
            dateOfMonth = dayOfMonth+"";
            final Calendar calendar = Calendar.getInstance();
            int hour = calendar.get(Calendar.HOUR);
            int minutes = calendar.get(Calendar.MINUTE);
            boolean is24HourForamt = DateFormat.is24HourFormat(AddOrEditNoteActivity.this);
            TimePickerDialog timePickerDialog = new TimePickerDialog(AddOrEditNoteActivity.this,onTimeSetListener,hour,minutes,is24HourForamt);
            timePickerDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.WHITE));
            timePickerDialog.show();
        }
    };

    private TimePickerDialog.OnTimeSetListener onTimeSetListener = new TimePickerDialog.OnTimeSetListener() {
        @Override
        public void onTimeSet(TimePicker timePicker, int hour, int minute) {
            selectedHour = hour;
            selectedMinutes = minute;
            AddOrEditNoteActivity.this.hour = hour+"";
            AddOrEditNoteActivity.this.minute = minute+"";
            Calendar calendar = Calendar.getInstance();
            calendar.set(selectedYear,selectedMonth,selectedDayOfMonth,selectedHour,selectedMinutes);
            setAlarm(calendar);
        }
    };

    private void setAlarm(Calendar calendar) {
        if (edtDescription.getText().toString().length()==0 && edtTitle.getText().toString().length()==0){
            Snackbar.make(findViewById(android.R.id.content),"Both Title and Description cannot be empty for the alarm.",Snackbar.LENGTH_SHORT).show();
            return;
        }
        isAlarmSet = true;
        insertOrUpdateNotes();
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(this,AlarmReceiver.class);
        intent.putExtra(ExtraUtil.TITLE,edtTitle.getText().toString().trim().replace("\n"," "));
        intent.putExtra(ExtraUtil.DESCRIPTION,edtDescription.getText().toString().trim().replace("\n"," "));
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this,1,intent,0);
        alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),pendingIntent);
        currentMenu.findItem(R.id.action_cancel_alarm).setVisible(true);
        currentMenu.findItem(R.id.action_alarm).setVisible(false);
        Snackbar.make(findViewById(android.R.id.content),"Alarm set",Snackbar.LENGTH_SHORT).show();
    }

    private void cancelAlarm(){
        insertOrUpdateNotes();
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(this,AlarmReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this,1,intent,0);
        alarmManager.cancel(pendingIntent);
        Snackbar.make(findViewById(android.R.id.content),"Alarm Disabled",Snackbar.LENGTH_SHORT).show();
    }

    private void insertOrUpdateNotes() {
        if (intent.hasExtra(ExtraUtil.ADD_NOTE))
        {
            noteEntity = new NoteEntity(edtTitle.getText().toString().trim(),edtDescription.getText().toString().trim());
            noteEntity.setAlarmSet(isAlarmSet);
            noteEntity.setAlarmDate(selectedDayOfMonth+" "+selectedMonth+" "+selectedYear+" "+selectedHour+" "+selectedMinutes);
            if (edtTitle.getText().toString().trim().length()>0 || edtDescription.getText().toString().trim().length()>0)
            {
                if (isNoteSaved){
                    updateNoteViewModel.update(noteEntity);
                    isNoteSaved = true;
                } else{
                    updateNoteViewModel.insert(noteEntity);
                    isNoteSaved = true;
                }
            }else{
                Snackbar.make(findViewById(android.R.id.content),"Both Title and Description cannot be empty.",Snackbar.LENGTH_SHORT).show();
            }
        }
        else{
            noteEntity.setDescription(edtDescription.getText().toString().trim());
            noteEntity.setTitle(edtTitle.getText().toString().trim());
            noteEntity.setAlarmSet(isAlarmSet);
            noteEntity.setAlarmDate(selectedDayOfMonth+" "+selectedMonth+" "+selectedYear+" "+selectedHour+" "+selectedMinutes);
            if (edtTitle.getText().toString().trim().length()>0 || edtDescription.getText().toString().trim().length()>0)
            {
                updateNoteViewModel.update(noteEntity);
                isNoteSaved = true;
            }
            else
                updateNoteViewModel.delete(noteEntity);
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
