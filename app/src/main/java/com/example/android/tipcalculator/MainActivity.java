package com.example.android.tipcalculator;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
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
    private int tipPercent = 15;
    private int partySize = 2;
    private SeekBar tipPercentSlider;
    private TextView tipPercentDisplay;
    private TextView partySizeDisplay;
    private SeekBar partySizeSlider;
    private float finalBillAmount;
    private float totalPerPerson;
    private TextView overall_total_bill_text_view;
    private TextView total_per_person_text_view;

/********************************************************
********************************************************/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /* Inflate main activity XML layout */
        setContentView(R.layout.activity_main);

        /* Find the Views w/in main activity */
        startingBillAmount = (EditText)findViewById(R.id.starting_bill_input);
        tipPercentSlider = (SeekBar)findViewById(R.id.tip_percent_slider);
        final TextView tipPercentDisplay = (TextView)findViewById(R.id.tip_percent_text_view);
        partySizeSlider = (SeekBar)findViewById(R.id.party_size_slider);
        final TextView partySizeDisplay = (TextView)findViewById(R.id.party_size_text_view);
//        final TextView overallFinalBill = (TextView)findViewById(R.id.overall_total_bill_text_view);

        /* Dynamically display final bill overall + per person after user enters starting bill */
        startingBillAmount.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                /* sets bill amount to $0 when user clears the EditText (Fixes crashing bug) */
                if (s.length() == 0) {
                    startingBillAmount.setText("0.00");
                } else {
                    displayFinalBill(tipPercent,partySize);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
//                displayFinalBill(tipPercent,partySize);
            }
        });

        /* Set OnSeekBarChangeListener to change the value of the tip % when the slider is used */
        tipPercentSlider.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                /* set value of tip % = current value on the seekbar + display it in the TextView*/
                tipPercent = valueOf(progress);
                /* If the user attempts to make tip percent < 1, give error message */
                if (tipPercent < 1) {
                    Toast.makeText(MainActivity.this, "Tip percent can't be less than 1%!",
                            Toast.LENGTH_SHORT).show();
                    tipPercent = 1;
                    tipPercentDisplay.setText(String.valueOf(tipPercent)+" %");
                } else {
                    tipPercentDisplay.setText(String.valueOf(tipPercent) + " %");
                    displayFinalBill(tipPercent, partySize);
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
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });
    }

    private void displayFinalBill(float tip, int party) {

        TextView totalBillOverall = (TextView) findViewById(R.id.overall_total_bill_text_view);
        double tempBill = Double.parseDouble(startingBillAmount.getText().toString());
        double finalBill = (tempBill + (tempBill * (tip/100)));
        totalBillOverall.setText("$ " + (String.valueOf(String.format("%.2f", finalBill))));

        TextView totalBillPerPerson = (TextView) findViewById(R.id.total_per_person_text_view);
        double billPerPerson = Double.parseDouble(startingBillAmount.getText().toString());
        double finalBillPerPerson = ((billPerPerson + (billPerPerson * (tip/100)))/party);
        totalBillPerPerson.setText("$ " + (String.valueOf(String.format("%.2f", finalBillPerPerson))));

    }
}
