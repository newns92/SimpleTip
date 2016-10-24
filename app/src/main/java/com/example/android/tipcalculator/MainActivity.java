package com.example.android.tipcalculator;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import static java.lang.Integer.valueOf;

public class MainActivity extends AppCompatActivity {

/********************************************
*               GLOBAL VARIABLES            *
*********************************************/

    private EditText startingBillAmount;
    private float tip;
    private int party;
    private SeekBar tipPercent;
    private TextView tipPercentDisplay;
    private TextView partySizeDisplay;
    private SeekBar partySize;
    private float finalBillAmount;
    private float totalPerPerson;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        startingBillAmount = (EditText)findViewById(R.id.starting_bill_input);
        tipPercent = (SeekBar)findViewById(R.id.tip_percent_slider);
        final TextView tipPercentDisplay = (TextView)findViewById(R.id.tip_percent_text_view);
        partySize = (SeekBar)findViewById(R.id.party_size_slider);
        final TextView partySizeDisplay = (TextView)findViewById(R.id.party_text_view);

        tipPercent.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                tip = valueOf(progress);
                tipPercentDisplay.setText(String.valueOf(progress));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        partySize.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                party = valueOf(progress);
                partySizeDisplay.setText(String.valueOf(progress));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }

    //increase tip by 1% and display new amount to screen
    public void incrementTip(View view) {
        //user cant have more than 100 coffees
        tip = tip + 1;
        displayTip(tip);
        displayFinalBill(tip,party);
        displayOverallFinalBill(tip,party);
    }
    //decrease tip by 1% and display new amount to screen
    public void decrementTip(View view) {
        //user can't tip less than 1%
        if (tip == 1) {
            Toast.makeText(this, "You cannot tip less than 1%!", Toast.LENGTH_SHORT).show();
            return;
        }
        tip = tip - 1;
        displayTip(tip);
        displayFinalBill(tip,party);
        displayOverallFinalBill(tip,party);
    }
    /**displays the given tip % value on the screen when buttons are pressed*/
    private void displayTip(float number) {
        //TextView tipPercentTextView = (TextView) findViewById(R.id.tip_percent_text_view);
        tipPercentDisplay.setText("" + number + "%");
    }

    //increase size of party by 1 and display new amount to screen
    public void incrementParty(View view) {
        //user cant have more than 100 coffees
        party = party + 1;
        displayParty(party);
        displayFinalBill(tip,party);
        displayOverallFinalBill(tip,party);
    }
    //decrease size of party by 1 and display new amount to screen
    public void decrementParty(View view) {
        if (party == 1) {
            Toast.makeText(this, "Your party cannot have less than 1 person!", Toast.LENGTH_SHORT).show();
            return;
        }
        party = party - 1;
        displayParty(party);
        displayFinalBill(tip,party);
        displayOverallFinalBill(tip,party);
//        calculateFinalTotal(bill,tip,party);
    }
    /**displays value of party size on the screen when buttons are pressed*/
    private void displayParty(int number) {
        TextView partyTextView = (TextView) findViewById(R.id.party_text_view);
        partyTextView.setText("" + number);
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
