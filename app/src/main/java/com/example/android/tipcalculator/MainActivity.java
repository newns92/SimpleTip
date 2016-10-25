package com.example.android.tipcalculator;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;

import static java.lang.Integer.valueOf;

public class MainActivity extends AppCompatActivity {

/*********************************************************
*               GLOBAL VARIABLES                         *
*********************************************************/

    private EditText startingBillAmount;
    private float tipPercent = 15;
    private int partySize = 1;
    private SeekBar tipPercentSlider;
    private TextView tipPercentDisplay;
    private TextView partySizeDisplay;
    private SeekBar partySizeSlider;
    private float finalBillAmount;
    private float totalPerPerson;

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

        /* Dynamically display final bill overall + per person after user enters starting bill */
        startingBillAmount.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                displayOverallFinalBill(tipPercent,partySize);
                displayFinalBill(tipPercent,partySize);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        /* Set OnSeekBarChangeListener to change the value of the tip % when the slider is used */
        tipPercentSlider.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                /* set value of tip % = current value on the seekbar + display it in the TextView*/
                tipPercent = valueOf(progress);
                tipPercentDisplay.setText(String.valueOf(progress)+"%");
                //displayTip(tipPercent);
                displayFinalBill(tipPercent,partySize);
                displayOverallFinalBill(tipPercent,partySize);
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
                partySizeDisplay.setText(String.valueOf(progress));
                displayFinalBill(tipPercent,partySize);
                displayOverallFinalBill(tipPercent,partySize);
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });
    }

    /**displays the given tip % value on the screen when buttons are pressed*/
    private void displayTip(float number) {
        //TextView tipPercentTextView = (TextView) findViewById(R.id.tip_percent_text_view);
        tipPercentDisplay.setText("" + String.valueOf(number) + "%");
    }

    /**displays value of party size on the screen when buttons are pressed*/
    private void displayParty(int number) {
        TextView partyTextView = (TextView) findViewById(R.id.party_size_text_view);
        partyTextView.setText("" + String.valueOf(number));
    }

    private void displayOverallFinalBill(float tip, int party) {
        TextView totalBillTextView = (TextView) findViewById(R.id.overall_total_bill_text_view);
        EditText billInput = (EditText) findViewById(R.id.starting_bill_input);
        String tempBill = billInput.getText().toString();
        float bill = Float.parseFloat(tempBill);
        float finalBill = (bill + (bill * (tip/100)));
        totalBillTextView.setText("$" + finalBill);
    }

    private void displayFinalBill(float tip, int party) {
        TextView totalBillTextView = (TextView) findViewById(R.id.total_bill_text_view);
        EditText billInput = (EditText) findViewById(R.id.starting_bill_input);
        String tempBill = billInput.getText().toString();
        float bill = Float.parseFloat(tempBill);
        float finalBill = ((bill + (bill * (tip/100)))/party);
        totalBillTextView.setText("$" + finalBill);
    }
}
