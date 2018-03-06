package com.example.stelios.songle;

import android.app.Activity;
import android.content.Context;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.util.Patterns;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;


public class InputValidation {

    private Context context;

    public InputValidation(Context context){
        this.context = context;
    }


    /********************************** IS INPUT EDIT TEXT FILLED **********************************/
    // Check whether input field is filled with text
    // Return false if text field is empty
    // Return true if text filed is filled with text
    public boolean isInputEditTextFilled(TextInputEditText textInputEditText, TextInputLayout textInputLayout, String message){
        String value = textInputEditText.getText().toString().trim();
        if (value.isEmpty()){
            textInputLayout.setError(message);
            hideKeyboardFrom(textInputEditText);
            return false;
        } else {
            textInputLayout.setErrorEnabled(false);
        }
        return true;
    }

    /********************************** IS INPUT EDIT TEXT EMAIL ***********************************/
    // Check whether input email is filled and validates if it is in email format
    // Return false if text field is empty or not an email address
    // Return true if text filed is filled and is an email adress
    public boolean isInputEditTextEmail(TextInputEditText textInputEditText, TextInputLayout textInputLayout, String message){
        String value = textInputEditText.getText().toString().trim();
        if (value.isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(value).matches()){
            textInputLayout.setError(message);
            hideKeyboardFrom(textInputEditText);
            return false;
        }else {
            textInputLayout.setErrorEnabled(false);
        }
        return true;
    }

    /********************************** IS INPUT EDIT TEXT MATCHES *********************************/
    // Check whether the two inputs match each other
    // To be used when registering on password confirmation
    public boolean isInputEditTextMatches(TextInputEditText textInputEditText1, TextInputEditText textInputEditText2, TextInputLayout textInputLayout, String message ){
        String value1 = textInputEditText1.getText().toString().trim();
        String value2 = textInputEditText2.getText().toString().trim();
        if (!value1.contentEquals(value2)){
            textInputLayout.setError(message);
            hideKeyboardFrom(textInputEditText2);
            return false;
        }else {
            textInputLayout.setErrorEnabled(false);
        }
        return true;
    }

    /********************************** HIDE KEYBOARD FROM *****************************************/
    // Hide Soft Input
    private void hideKeyboardFrom(View view){
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Activity.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
    }
}
