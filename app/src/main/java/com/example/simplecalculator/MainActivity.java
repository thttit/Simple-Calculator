package com.example.simplecalculator;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    TextView result, solution;
    MaterialButton btn_c, btn_ce, btn_open, btn_close, btn_dot;
    MaterialButton btn_1, btn_2, btn_3, btn_4, btn_5, btn_6, btn_7, btn_8, btn_9;
    MaterialButton btn_plus, btn_subtrac, btn_multi, btn_divide, btn_equal;

    ArrayList<String> result_2 = new ArrayList<String>();
    ArrayList<String> solution_2 = new ArrayList<String>();

    Button btn_his;

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
        assignID(btn_equal, R.id.btn_equal);
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
                    intent.putExtra("result_2", result_2);
                    intent.putExtra("solution_2", solution_2);
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
            double val = eval(data);
            result.setText(String.valueOf(val));
            result_2.add(String.valueOf(val));
            solution_2.add(data);
            return;
        }

        if(button_text.equals("C")){
            data = data.substring(0, data.length()-1);
            result.setText("0");
        } else {
            data = data + button_text;
        }
        solution.setText(data);

    }

    public static double eval(final String str) {
        return new Object() {
            int pos = -1, ch;

            void nextChar() {
                ch = (++pos < str.length()) ? str.charAt(pos) : -1;
            }

            boolean eat(int charToEat) {
                while (ch == ' ') nextChar();
                if (ch == charToEat) {
                    nextChar();
                    return true;
                }
                return false;
            }

            double parse() {
                nextChar();
                double x = parseExpression();
                if (pos < str.length()) throw new RuntimeException("Unexpected: " + (char) ch);
                return x;
            }

            // Grammar:
            // expression = term | expression + term | expression - term
            // term = factor | term * factor | term / factor
            // factor = + factor | - factor | ( expression )
            //        | number | functionName factor | factor ^ factor

            double parseExpression() {
                double x = parseTerm();
                for (; ; ) {
                    if (eat('+')) x += parseTerm(); // addition
                    else if (eat('-')) x -= parseTerm(); // subtraction
                    else return x;
                }
            }

            double parseTerm() {
                double x = parseFactor();
                for (; ; ) {
                    if (eat('x')) x *= parseFactor(); // multiplication
                    else if (eat('/')) x /= parseFactor(); // division
                    else return x;
                }
            }

            double parseFactor() {
                if (eat('+')) return parseFactor(); // unary plus
                if (eat('-')) return -parseFactor(); // unary minus

                double x;
                int startPos = this.pos;
                if (eat('(')) { // parentheses
                    x = parseExpression();
                    eat(')');
                } else if ((ch >= '0' && ch <= '9') || ch == '.') { // numbers
                    while ((ch >= '0' && ch <= '9') || ch == '.') nextChar();
                    x = Double.parseDouble(str.substring(startPos, this.pos));
                } else if (ch >= 'a' && ch <= 'z') { // functions
                    while (ch >= 'a' && ch <= 'z') nextChar();
                    String func = str.substring(startPos, this.pos);
                    x = parseFactor();
                    if (func.equals("sqrt")) x = Math.sqrt(x);
                    else if (func.equals("sin")) x = Math.sin(Math.toRadians(x));
                    else if (func.equals("cos")) x = Math.cos(Math.toRadians(x));
                    else if (func.equals("tan")) x = Math.tan(Math.toRadians(x));
                    else if (func.equals("log")) x = Math.log10(x);
                    else if (func.equals("ln")) x = Math.log(x);
                    else throw new RuntimeException("Unknown function: " + func);
                } else {
                    throw new RuntimeException("Unexpected: " + (char) ch);
                }

                if (eat('^')) x = Math.pow(x, parseFactor()); // exponentiation

                return x;
            }
        }.parse();

    }
    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        if (result.getText().toString() != null)
            outState.putString("re", result.getText().toString());

        if (solution.getText().toString()!=null)
            outState.putString("sol", solution.getText().toString());

        if (result_2 != null)
            outState.putStringArrayList("results", result_2);

        if (solution_2 != null)
            outState.putStringArrayList("solutions", solution_2);
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        if (savedInstanceState.get("re")!=null)
            result.setText(savedInstanceState.get("re").toString());
        if (savedInstanceState.get("sol")!=null)
            solution.setText(savedInstanceState.get("sol").toString());

        if (savedInstanceState.get("results")!= null)
            result_2 = savedInstanceState.getStringArrayList("results");

        if (savedInstanceState.get("solutions")!= null)
            solution_2 = savedInstanceState.getStringArrayList("solutions");
    }

}
