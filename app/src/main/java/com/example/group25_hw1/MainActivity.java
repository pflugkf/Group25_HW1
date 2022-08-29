package com.example.group25_hw1;
/**
 * Assignment #: Homework 1
 * File name: Group25_HW1 --- MainActivity.java
 * Name: Kristin Pflug
 */


import androidx.annotation.LongDef;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    SeekBar tipScroller;
    TextView customTipVal;
    EditText billTextBox;
    RadioGroup tipRadioGroup;
    RadioGroup splitRadioGroup;
    TextView tipTotal;
    TextView total;
    TextView splitBillTotal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tipScroller = findViewById(R.id.custom_tip_slider);
        customTipVal = findViewById(R.id.custom_tip_slider_value);
        billTextBox = findViewById(R.id.bill_total_textbox);
        tipRadioGroup = findViewById(R.id.tip_percentage_choices);
        splitRadioGroup = findViewById(R.id.split_by_choices);
        tipTotal = findViewById(R.id.tip_value);
        total = findViewById(R.id.total_value);
        splitBillTotal = findViewById(R.id.total_per_person_value);

        
        tipScroller.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean b) {
                customTipVal.setText(progress + "%");
                if(tipRadioGroup.getCheckedRadioButtonId() == findViewById(R.id.custom_tip_radiobutton).getId()) {
                    double bill = Double.parseDouble(billTextBox.getText().toString());
                    double tip = getTipAmount(bill, tipRadioGroup.getCheckedRadioButtonId());
                    changeTipAndTotal(bill, tip);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        billTextBox.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                try{
                    String userInput = billTextBox.getText().toString();

                    if(!userInput.equals("")){
                        double bill = Double.parseDouble(userInput);
                        int tipChoice = tipRadioGroup.getCheckedRadioButtonId();
                        int numPeople = splitRadioGroup.getCheckedRadioButtonId();
                        double tip = getTipAmount(bill, tipChoice);
                        changeTipAndTotal(bill, tip);
                        changeSplitTotal(bill+tip, numPeople);

                    } else {
                        tipTotal.setText("$0.0");
                        total.setText("$0.0");
                        splitBillTotal.setText("$0.0");
                    }
                } catch (NumberFormatException nfe) {
                    Toast.makeText(MainActivity.this,getString(R.string.nfeErrorText), Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        tipRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                if(!billTextBox.getText().toString().equals("")) {
                    double tip = getTipAmount(Double.parseDouble(billTextBox.getText().toString()), i);
                    changeTipAndTotal(Double.parseDouble(billTextBox.getText().toString()), tip);
                }
            }
        });

        splitRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                if(!billTextBox.getText().toString().equals("")) {
                    double bill = Double.parseDouble(billTextBox.getText().toString());
                    double billWithTip = bill + getTipAmount(bill, tipRadioGroup.getCheckedRadioButtonId());
                    changeSplitTotal(billWithTip, i);
                }
            }
        });

        findViewById(R.id.clear_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tipRadioGroup.check(findViewById(R.id.ten_percent_tip_radiobutton).getId());
                tipScroller.setProgress(40);
                tipTotal.setText("$0.0");
                total.setText("$0.0");
                splitRadioGroup.check(findViewById(R.id.one_person_radiobutton).getId());
                tipScroller.setProgress(40);
                billTextBox.setText("");
            }

        });
    }

    private double getTipAmount(double billAmount, int tipChoice){
        double tip = 0.0;
        if (tipChoice == R.id.ten_percent_tip_radiobutton) {
            tip=billAmount*0.1;

        } else if (tipChoice == R.id.fifteen_percent_tip_radiobutton) {
            tip=billAmount*0.15;

        } else if (tipChoice == R.id.eighteen_percent_radiobutton) {
            tip=billAmount*0.18;

        } else if (tipChoice == R.id.custom_tip_radiobutton) {
            double customTip = tipScroller.getProgress() * 0.01;
            tip=billAmount*customTip;
        }
        return tip;
    }

    private void changeTipAndTotal(double bill, double tip){
        tipTotal.setText(String.format("$%.1f", tip));
        double billWithTip = (bill+tip);
        total.setText(String.format("$%.1f", billWithTip));
        changeSplitTotal(billWithTip, splitRadioGroup.getCheckedRadioButtonId());
    }

    private void changeSplitTotal(double billWithTip, int splitBy){
        double splitBill = 0.0;
        if (splitBy == R.id.one_person_radiobutton) {
            splitBill = billWithTip;
        } else if (splitBy == R.id.two_people_radiobutton) {
            splitBill = billWithTip/2;
        } else if (splitBy == R.id.three_person_radiobutton) {
            splitBill = billWithTip/3;
        } else if (splitBy == R.id.four_people_radiobutton) {
            splitBill = billWithTip/4;
        }
        splitBillTotal.setText(String.format("$%.1f", splitBill));
    }
}