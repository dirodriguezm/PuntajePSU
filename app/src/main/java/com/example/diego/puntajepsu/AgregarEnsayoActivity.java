package com.example.diego.puntajepsu;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.Calendar;

public class AgregarEnsayoActivity extends AppCompatActivity implements CalendarView.OnDateChangeListener, Spinner.OnItemSelectedListener{
    private CalendarView calendarView;
    private Spinner spinner;
    private EditText editText;
    private FloatingActionButton aceptar;
    private FloatingActionButton cancelar;

    private Calendar calendar;
    private String ensayo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agregar_ensayo);
        calendarView = findViewById(R.id.calendarView);
        calendarView.setOnDateChangeListener(this);
        spinner = findViewById(R.id.spinner3);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.ensayos, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);
        editText = findViewById(R.id.editText);
        calendar = Calendar.getInstance();
        calendarView.setDate(calendar.getTimeInMillis());
        aceptar = findViewById(R.id.floatingActionButton3);
        cancelar = findViewById(R.id.floatingActionButton4);
    }

    @Override
    public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
        calendar.set(Calendar.YEAR,year);
        calendar.set(Calendar.MONTH,month);
        calendar.set(Calendar.DAY_OF_MONTH,dayOfMonth);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        this.ensayo = (String) parent.getItemAtPosition(position);
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    public void aceptar(View v){
        editText.setError(null);
        if(! TextUtils.isEmpty(editText.getText().toString())) {
            Intent intent = new Intent(this, MainActivity.class);
            intent.putExtra("fecha", calendar);
            intent.putExtra("ensayo", ensayo);
            intent.putExtra("puntaje", Integer.parseInt(String.valueOf(editText.getText())));
            startActivity(intent);
        }
        else{
            editText.setError("Falta agregar puntaje");
            editText.requestFocus();
        }
    }

    public void cancelar(View v){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}
