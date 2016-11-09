package com.example.android.tipcalculator;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import static java.lang.Integer.valueOf;

public class MainActivity extends AppCompatActivity {

/*********************************************************
*               GLOBAL VARIABLES                         *
*********************************************************/

    private EditText startingBillAmount;
    public int tipPercent = 15;
    public int partySize = 2;
    private SeekBar tipPercentSlider;
    private TextView tipPercentDisplay;
    private TextView tipDollarAmountDisplay;
    private TextView partySizeDisplay;
    private SeekBar partySizeSlider;
    private double tempPercentDollarAmount;
    private double tempFinalBillAmount;
    private double finalBillAmount;
    private double tempTotalPerPerson;
    private double totalPerPerson;
    private TextView overall_total_bill_text_view;
    private TextView total_per_person_text_view;

/********************************************************
********************************************************/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /* For icon display in app bar TO BE ADDED LATER */
//        getSupportActionBar().setDisplayShowHomeEnabled(true);
//        getSupportActionBar().setLogo(R.drawable.ic_launcher);
//        getSupportActionBar().setDisplayUseLogoEnabled(true);
        /* Inflate main activity XML layout */
        setContentView(R.layout.activity_main);

        /* Find the Views w/in main activity */
        startingBillAmount = (EditText)findViewById(R.id.starting_bill_input);
        tipPercentSlider = (SeekBar)findViewById(R.id.tip_percent_slider);
        tipPercentDisplay = (TextView)findViewById(R.id.tip_percent_text_view);
        tipDollarAmountDisplay = (TextView)findViewById(R.id.tip_dollar_amount_text_view);
        partySizeSlider = (SeekBar)findViewById(R.id.party_size_slider);
        partySizeDisplay = (TextView)findViewById(R.id.party_size_text_view);
        overall_total_bill_text_view = (TextView)findViewById(R.id.overall_total_bill_text_view);
        total_per_person_text_view = (TextView)findViewById(R.id.total_per_person_text_view);

        /* Dynamically display final bill overall + bill per person after user enters starting bill */
        startingBillAmount.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                /* set bill and tip amounts to $0 when user clears the EditText (Fixes crashing bug) */
                if (s.length() == 0) {
                    startingBillAmount.setText("0.00");
                } else {
                    displayTipNumbers();
                    displayFinalBill(tipPercent,partySize);
                }
            }
            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        /* Set OnSeekBarChangeListener to change the value of the tip % when the slider is used */
        tipPercentSlider.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (startingBillAmount.length() == 0) {
                    /* set default starting bill Amount = $0.00 to prevent crash if user moves
                            progress before before entering bill amount)
                     */
                    startingBillAmount.setText("0.00");
                    /* set value of tip % = current value on the seekbar + display it in the TextView*/
                    tipPercent = valueOf(progress);
                    /* If the user attempts to make tip percent < 1, give error message */
                    if (tipPercent < 1) {
                        Toast.makeText(MainActivity.this, "Tip percent can't be less than 1%!",
                                Toast.LENGTH_SHORT).show();
                        tipPercent = 1;
                        displayTipNumbers();
                        displayFinalBill(tipPercent, partySize);
                    } else {
                        displayTipNumbers();
                        displayFinalBill(tipPercent, partySize);
                    }
                } else {
                        /* set value of tip % = current value on the seekbar + display it in the TextView*/
                    tipPercent = valueOf(progress);
                    /* If the user attempts to make tip percent < 1, give error message */
                    if (tipPercent < 1) {
                        Toast.makeText(MainActivity.this, "Tip percent can't be less than 1%!",
                                Toast.LENGTH_SHORT).show();
                        tipPercent = 1;
                        displayTipNumbers();
                        displayFinalBill(tipPercent, partySize);
                    } else {
                        displayTipNumbers();
                        displayFinalBill(tipPercent, partySize);
                    }
                }
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });

        /* Set OnSeekBarChangeListener to change the value of the party size when the slider is used */
        partySizeSlider.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (startingBillAmount.length() == 0) {
                    /* set default starting bill Amount = $0.00 to prevent crash if user moves
                            progress before before entering bill amount)
                     */
                    startingBillAmount.setText("0.00");
                    /* set value of party size = current value on the seekbar + display it in the TextView*/
                    partySize = valueOf(progress);
                    /* If the user attempts to make party size < 1, give error message */
                    if (partySize < 1) {
                        Toast.makeText(MainActivity.this, "You can't have less than 1 person!",
                                Toast.LENGTH_SHORT).show();
                        partySize = 1;
                        partySizeDisplay.setText(String.valueOf(partySize));
                    } else {
                        partySizeDisplay.setText(String.valueOf(partySize));
                        displayFinalBill(tipPercent, partySize);
                    }
                } else {
                    /* set value of party size = current value on the seekbar + display it in the TextView*/
                    partySize = valueOf(progress);
                    /* If the user attempts to make party size < 1, give error message */
                    if (partySize < 1) {
                        Toast.makeText(MainActivity.this, "You can't have less than 1 person!",
                                Toast.LENGTH_SHORT).show();
                        partySize = 1;
                        partySizeDisplay.setText(String.valueOf(partySize));
                    } else {
                        partySizeDisplay.setText(String.valueOf(partySize));
                        displayFinalBill(tipPercent, partySize);
                    }
                }
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });
    }

    public void displayFinalBill(float tip, int party) {
        tempFinalBillAmount = Double.parseDouble(startingBillAmount.getText().toString());
        finalBillAmount = (tempFinalBillAmount + (tempFinalBillAmount * (tip/100)));
        overall_total_bill_text_view.setText("$ " + (String.valueOf(String.format("%.2f",
                finalBillAmount))));

        tempTotalPerPerson = Double.parseDouble(startingBillAmount.getText().toString());
        totalPerPerson = ((tempTotalPerPerson + (tempTotalPerPerson * (tip/100)))/party);
        total_per_person_text_view.setText("$ " + (String.valueOf(String.format("%.2f",
                totalPerPerson))));
    }

    public void displayTipNumbers() {
        tipPercentDisplay.setText(String.valueOf(tipPercent)+" %");
        tempPercentDollarAmount = Double.parseDouble(startingBillAmount.getText().toString());
        tipDollarAmountDisplay.setText("($ " + (String.valueOf(String.format("%.2f",
                (tempFinalBillAmount*(Double.valueOf(tipPercent)/100))) + ")")));
    }

    /** This method resets initial state of the app*/
    public void clearAll(View view) {
        startingBillAmount.setText("");
        tipPercentSlider.setProgress(15);
        tipPercent = valueOf(tipPercentSlider.getProgress());
        partySizeSlider.setProgress(2);
        partySize = valueOf(partySizeSlider.getProgress());
        displayFinalBill(tipPercent,partySize);
    }
}
