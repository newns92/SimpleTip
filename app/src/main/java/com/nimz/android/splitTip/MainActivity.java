package com.nimz.android.splitTip;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.Selection;
import android.text.TextUtils;
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
    private TextView overall_total_bill_text_view;
    private TextView total_per_person_text_view;
/********************************************************
********************************************************/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /* Inflate the Main Activity XML layout */
        setContentView(R.layout.activity_main);

        /* Find the Views w/in the Main Activity */
        startingBillAmount = (EditText)findViewById(R.id.starting_bill_input);
            startingBillAmount.setSelection(startingBillAmount.getText().length());
        tipPercentSlider = (SeekBar)findViewById(R.id.tip_percent_slider);
        tipPercentDisplay = (TextView)findViewById(R.id.tip_percent_text_view);
        tipDollarAmountDisplay = (TextView)findViewById(R.id.tip_dollar_amount_text_view);
        partySizeSlider = (SeekBar)findViewById(R.id.party_size_slider);
        partySizeDisplay = (TextView)findViewById(R.id.party_size_text_view);
        overall_total_bill_text_view = (TextView)findViewById(R.id.overall_total_bill_text_view);
        total_per_person_text_view = (TextView)findViewById(R.id.total_per_person_text_view);

        /* Dynamically display final bill overall and per person as (and after) user enters a
            starting bill amount */
        startingBillAmount.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                /* Set the starting bill and tip amounts to $0.00 when user clears the EditText
                    - Fixes crashing bug when EditText is empty */
                if (s.length() == 0 || TextUtils.isEmpty(s)) {
                    startingBillAmount.setText(getString(R.string.starting_bill_hint));
                    displayTipNumbers();
                    displayFinalBill(tipPercent,partySize);
                    startingBillAmount.setSelection(startingBillAmount.getText().length());
                } else {
                    /* Convert user input into US currency values */
                    if (!s.toString().matches("^\\$(\\d{1,3}(\\,\\d{3})*|(\\d+))(\\.\\d{2})?$")) {
                        String userInput = "" + s.toString().replaceAll("[^\\d]", "");
                        StringBuilder cashAmountBuilder = new StringBuilder(userInput);

                        while (cashAmountBuilder.length() > 3 && cashAmountBuilder.charAt(0) == '0') {
                            cashAmountBuilder.deleteCharAt(0);
                        }
                        while (cashAmountBuilder.length() < 3) {
                            cashAmountBuilder.insert(0, '0');
                        }
                        cashAmountBuilder.insert(cashAmountBuilder.length() - 2, '.');

                        startingBillAmount.removeTextChangedListener(this);
                        startingBillAmount.setText(cashAmountBuilder.toString());

                        startingBillAmount.setTextKeepState("$" + cashAmountBuilder.toString());
                        Selection.setSelection(startingBillAmount.getText(), cashAmountBuilder.toString().length() + 1);

                        startingBillAmount.addTextChangedListener(this);
                        /* Display new amounts in TextViews */
                        displayTipNumbers();
                        displayFinalBill(tipPercent,partySize);
                        startingBillAmount.setSelection(startingBillAmount.getText().length());
                    }
                }
            }
            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        /* Set OnSeekBarChangeListener to change the value of the tip % as the slider is used */
        tipPercentSlider.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                /* Set starting bill to $0.00 if user attempts to move the slider before entering a
                    starting bill amount
                        - Fixes crashing bug when EditText is empty */
                if (startingBillAmount.length() == 0) {
                    startingBillAmount.setText(getString(R.string.starting_bill_hint));
                    displayTipPercent(progress);
                } else {
                    displayTipPercent(progress);
                }
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });

        /* Set OnSeekBarChangeListener to change the value of the party size as the slider is used */
        partySizeSlider.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                /* Set starting bill to $0.00 if user attempts to move the slider before entering a
                    starting bill amount
                        - Fixes crashing bug when EditText is empty */
                if (startingBillAmount.length() == 0) {
                    startingBillAmount.setText(getString(R.string.starting_bill_hint));
                    displayPartySize(progress);
                } else {
                    displayPartySize(progress);
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
/*********************************************************
 *                      METHODS                          *
 *********************************************************/
    /* Check if it is 1st time user is running app and display quick, simple instructions if so*/
//    public void checkFirstRun() {
//        boolean isFirstRun = getSharedPreferences("PREFERENCE", MODE_PRIVATE).getBoolean("isFirstRun", true);
//        if (isFirstRun){
//            // Display the dialog
//            new AlertDialog.Builder(this).setTitle("First Run")
//                    .setMessage("This only pops up once")
//                    .setNeutralButton("OK", null).show();
//
//            getSharedPreferences("PREFERENCE", MODE_PRIVATE)
//                    .edit()
//                    .putBoolean("isFirstRun", false)
//                    .apply();
//        }
//    }

    /* Set the tip amount, final overall and per person bill amounts as the tip slider is moved */
    public void displayTipPercent(int progress) {
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
        startingBillAmount.setSelection(startingBillAmount.getText().length());
    }

    /* Set the party size, final overall and per person bill amounts as the party slider is moved */
    public void displayPartySize(int progress) {
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
        startingBillAmount.setSelection(startingBillAmount.getText().length());
    }

    /** Display the final Overall Bill and final Bill Per Person **/
    public void displayFinalBill(float tip, int party) {
        /* Get the text from the EditText, convert to String, then to A Double */
        double tempFinalBillAmount = Double.parseDouble(startingBillAmount.getText().toString().replace("$" ,""));
        /* Calculate the final bill as the starting bill plus the tip amount */
        double finalBillAmount = (tempFinalBillAmount + (tempFinalBillAmount * (tip/100)));
        /* Create the final overall bill text and display it in the appropriate TextView */
        String overall_total_bill_text = (getString(R.string.dollar_notation) +
                (String.valueOf(String.format("%.2f",finalBillAmount))));
        overall_total_bill_text_view.setText(overall_total_bill_text);

        /* Get the text from the EditText, convert to String, then to A Double */
        double tempTotalPerPerson = Double.parseDouble(startingBillAmount.getText().toString().replace("$" ,""));
        /* Calculate the final per person bill as the overall bill divided by party size*/
        double totalPerPerson = ((tempTotalPerPerson + (tempTotalPerPerson * (tip/100)))/party);
        /* Create the final per person bill text and display it in the appropriate TextView */
        String total_per_person_text = (getString(R.string.dollar_notation) +
                (String.valueOf(String.format("%.2f",totalPerPerson))));
        total_per_person_text_view.setText(total_per_person_text);
        startingBillAmount.setSelection(startingBillAmount.getText().length());

    }

    /* Displays the dollar amount of the tip based on the current bill amount and tip % **/
    public void displayTipNumbers() {
        String tipPercentText = String.valueOf(tipPercent) + getString(R.string.percentage);
        tipPercentDisplay.setText(tipPercentText);
        double tempFinalBillAmount = Double.parseDouble(startingBillAmount.getText().toString().replace("$" ,""));
        String tipDollarAmount_text = (getString(R.string.dollar_notation_tip) +
                (String.valueOf(String.format("%.2f",(tempFinalBillAmount*tipPercent/100))))
                + getString(R.string.dollar_notation_tip_ending));
        tipDollarAmountDisplay.setText(tipDollarAmount_text);
        startingBillAmount.setSelection(startingBillAmount.getText().length());
    }

    /** Resets the app to its initial state*/
    public void clearAll(View view) {
        startingBillAmount.setText("");
        startingBillAmount.setSelection(startingBillAmount.getText().length());
        tipPercentSlider.setProgress(15);
        tipPercent = tipPercentSlider.getProgress();
        partySizeSlider.setProgress(2);
        partySize = partySizeSlider.getProgress();
        displayFinalBill(tipPercent,partySize);
    }
}
