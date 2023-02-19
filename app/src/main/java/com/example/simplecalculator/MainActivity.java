package com.example.simplecalculator;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;

import org.mozilla.javascript.Context;
import org.mozilla.javascript.Scriptable;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    TextView result, solution;
    MaterialButton btn_c, btn_ce, btn_open, btn_close, btn_dot;
    MaterialButton btn_1, btn_2, btn_3, btn_4, btn_5, btn_6, btn_7, btn_8, btn_9;
    MaterialButton btn_plus, btn_subtrac, btn_multi, btn_divide;

    ArrayList<String> result_2 = new ArrayList<String>();
    ArrayList<String> solution_2 = new ArrayList<String>();

    MaterialButton btn_his;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        result = findViewById(R.id.result);
        solution = findViewById(R.id.solution);
        btn_his = findViewById(R.id.btn_his);

        assignID(btn_c, R.id.btn_c);
        assignID(btn_ce, R.id.btn_ce);
        assignID(btn_open, R.id.btn_open);
        assignID(btn_close, R.id.btn_close);
        assignID(btn_dot, R.id.btn_dot);
        assignID(btn_1, R.id.btn_1);
        assignID(btn_2, R.id.btn_2);
        assignID(btn_3, R.id.btn_3);
        assignID(btn_4, R.id.btn_4);
        assignID(btn_5, R.id.btn_5);
        assignID(btn_6, R.id.btn_6);
        assignID(btn_7, R.id.btn_7);
        assignID(btn_8, R.id.btn_8);
        assignID(btn_9, R.id.btn_9);
        assignID(btn_plus, R.id.btn_plus);
        assignID(btn_subtrac, R.id.btn_subtrac);
        assignID(btn_multi, R.id.btn_multi);
        assignID(btn_divide, R.id.btn_divide);

        btn_his.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), history.class);
                if (result_2 != null || solution_2 != null){
                    intent.putExtra("result", result_2);
                    intent.putExtra("solution", solution_2);
                }
                startActivity(intent);
            }
        });
    }

    void assignID(MaterialButton btn, int id){
        btn = findViewById(id);
        btn.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        MaterialButton button = (MaterialButton) view;
        String button_text = button.getText().toString();
        String data = solution.getText().toString();

        if(button_text.equals("CE")){
            solution.setText("");
            result.setText("0");
            return;
        }
        if(button_text.equals("=")){
            solution.setText(result.getText());
            solution_2.add(solution.getText().toString());
            return;
        }
        if(button_text.equals("C")){
            data = data.substring(0, data.length()-1);
            result.setText("0");
        } else {
            data = data + button_text;
        }
        solution.setText(data);

        String final_result = getResult(data);
        if(!final_result.equals("Error")){
            result.setText(final_result);
        }
    }
    String getResult(String data)
    {
        try{
            Context context = Context.enter();
            context.setOptimizationLevel(-1);
            Scriptable scriptable = context.initSafeStandardObjects();
            String final_result = context.evaluateString(scriptable, data, "Javascript",1,null).toString();
            if(final_result.endsWith(".0")){
                final_result = final_result.replace(".0","");
            }
            return final_result;
        } catch (Exception e){
            return "Error";
        }
    }
    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        if (result.getText().toString()!=null)
            outState.putString("result", result.getText().toString());

        if (solution.getText().toString()!=null)
            outState.putString("solution", solution.getText().toString());

        if (result_2 != null)
            outState.putStringArrayList("results", result_2);

        if (solution_2 != null)
            outState.putStringArrayList("solutions", solution_2);
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        if (savedInstanceState.get("result")!=null)
            result.setText(savedInstanceState.get("result").toString());
        if (savedInstanceState.get("solution")!=null)
            solution.setText(savedInstanceState.get("solution").toString());

        if (savedInstanceState.get("results")!= null)
            result_2 = savedInstanceState.getStringArrayList("results");

        if (savedInstanceState.get("solutions")!= null)
            solution_2 = savedInstanceState.getStringArrayList("solutions");
    }

}
