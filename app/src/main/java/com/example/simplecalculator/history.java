package com.example.simplecalculator;

import android.os.Bundle;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;


public class history extends AppCompatActivity {

    TextView txt_his;
    ArrayList<String> result_2 = new ArrayList<String>();
    ArrayList<String> solution_2 = new ArrayList<String>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        txt_his = findViewById(R.id.txt_his);
        Bundle extras = getIntent().getExtras();
        if (extras != null ){
            result_2 = extras.getStringArrayList("result_2");
            solution_2 = extras.getStringArrayList("solution_2");
        }
        String his = "";
        for (int i = result_2.size()-1 ; i >= 0 ; i--) {
            his = his + solution_2.get(i) + " = " + result_2.get(i) + "\n\n";
        }
        txt_his.setText(his);

    }
    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        if (txt_his.getText().toString()!=null)
            outState.putString("result", txt_his.getText().toString());

    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        if (savedInstanceState.get("result")!=null)
            txt_his.setText(savedInstanceState.get("result").toString());

    }
}
