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

/*********************************************************
*               GLOBAL VARIABLES                         *
*********************************************************/

    private EditText startingBillAmount;
    private float tipPercent;
    private int partySize;
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
        final TextView partySizeDisplay = (TextView)findViewById(R.id.party_text_view);

        /* Set OnSeekBarChangeListener to change the value of the tip % when the slider is used */
        tipPercentSlider.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                /* set value of tip % = current value on the seekbar + display it in the TextView*/
                tipPercent = valueOf(progress);
                tipPercentDisplay.setText(String.valueOf(progress));
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
        tipPercent = tipPercent + 1;
        displayTip(tipPercent);
        displayFinalBill(tipPercent,partySize);
        displayOverallFinalBill(tipPercent,partySize);
    }
    //decrease tip by 1% and display new amount to screen
    public void decrementTip(View view) {
        //user can't tip less than 1%
        if (tipPercent == 1) {Toast.makeText(this, "You cannot tip less than 1%!",
                Toast.LENGTH_SHORT).show();
            return;
        }
        tipPercent = tipPercent - 1;
        displayTip(tipPercent);
        displayFinalBill(tipPercent,partySize);
        displayOverallFinalBill(tipPercent,partySize);
    }
    /**displays the given tip % value on the screen when buttons are pressed*/
    private void displayTip(float number) {
        //TextView tipPercentTextView = (TextView) findViewById(R.id.tip_percent_text_view);
        tipPercentDisplay.setText("" + number + "%");
    }

    //increase size of party by 1 and display new amount to screen
    public void incrementParty(View view) {
        //user cant have more than 100 coffees
        partySize = partySize + 1;
        displayParty(partySize);
        displayFinalBill(tipPercent,partySize);
        displayOverallFinalBill(tipPercent,partySize);
    }
    //decrease size of party by 1 and display new amount to screen
    public void decrementParty(View view) {
        if (partySize == 1) {
            Toast.makeText(this, "Your party cannot have less than 1 person!", Toast.LENGTH_SHORT).show();
            return;
        }
        partySize = partySize - 1;
        displayParty(partySize);
        displayFinalBill(tipPercent,partySize);
        displayOverallFinalBill(tipPercent,partySize);
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
