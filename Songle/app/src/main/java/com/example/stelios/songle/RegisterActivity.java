package com.example.stelios.songle;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatTextView;
import android.view.KeyEvent;
import android.view.View;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;


public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {

    private final AppCompatActivity activity = RegisterActivity.this;

    // FOR SCROLL VIEW
    private NestedScrollView nestedScrollView;

    // FOR NAME, EMAIL, PASSWORD AND PASSWORD CONFIRMATION TEXT INPUT LAYOUTS
    private TextInputLayout textInputLayoutName;
    private TextInputLayout textInputLayoutEmail;
    private TextInputLayout textInputLayoutPassword;
    private TextInputLayout textInputLayoutConfirmPassword;

    // FOR NAME, EMAIL, PASSWORD AND PASSWORD CONFIRMATION TEXT INPUT
    private TextInputEditText textInputEditTextName;
    private TextInputEditText textInputEditTextEmail;
    private TextInputEditText textInputEditTextPassword;
    private TextInputEditText textInputEditTextConfirmPassword;

    // FOR REGISTER BUTTON
    private AppCompatButton appCompatButtonRegister;

    // FOR ALREADY MEMBER TEXT (BUTTON)
    private AppCompatTextView appCompatTextViewLoginLink;

    // FOR VALIDATION USE
    private InputValidation inputValidation;

    // FOR DATABASE USE
    private DatabaseHelper databaseHelper;
    private User user;

    /*************************** ON CREATE *********************************************************/
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        // Set content view of activity_register.xml
        setContentView(R.layout.activity_register);

        // Initialise views, listeners and objects
        initViews();
        initListeners();
        initObjects();
    }

    /************************ INITIALISE VIEWS *****************************************************/
    private void initViews(){

        // Nested Scroll View
        nestedScrollView = (NestedScrollView) findViewById(R.id.nestedScrollView);

        // Name, Email, Password and Confirm Password Input Text Layout Views
        textInputLayoutName = (TextInputLayout) findViewById(R.id.textInputLayoutName);
        textInputLayoutEmail = (TextInputLayout) findViewById(R.id.textInputLayoutEmail);
        textInputLayoutPassword = (TextInputLayout) findViewById(R.id.textInputLayoutPassword);
        textInputLayoutConfirmPassword = (TextInputLayout) findViewById(R.id.textInputLayoutConfirmPassword);

        // Name, Email, Password and Confirm Password Input Text Views
        textInputEditTextName = (TextInputEditText) findViewById(R.id.textInputEditTextName);
        textInputEditTextEmail = (TextInputEditText) findViewById(R.id.textInputEditTextEmail);
        textInputEditTextPassword = (TextInputEditText) findViewById(R.id.textInputEditTextPassword);
        textInputEditTextConfirmPassword = (TextInputEditText) findViewById(R.id.textInputEditTextConfirmPassword);

        // For Register Button View
        appCompatButtonRegister = (AppCompatButton) findViewById(R.id.appCompatButtonRegister);

        // For Already Member Text (Button) View
        appCompatTextViewLoginLink = (AppCompatTextView) findViewById(R.id.appCompatTextViewLoginLink);
    }

    /************************ INITIALISE LISTENERS *************************************************/
    private void initListeners(){

        // Initialise listeners for two buttons ("Register" and "Already Member")
        appCompatButtonRegister.setOnClickListener(this);
        appCompatTextViewLoginLink.setOnClickListener(this);
    }

    /************************ INITIALISE OBJECTS ***************************************************/
    private void initObjects(){
        inputValidation = new InputValidation(activity);
        databaseHelper = new DatabaseHelper(activity);
        user = new User();
    }

    /*********************** ON CLICK **************************************************************/
    @Override
    public void onClick(View v){
        switch (v.getId()){

            // Case where user clicks on Register Button
            case R.id.appCompatButtonRegister:

                // Try to add data to Database
                postDataToSQLite();
                break;

            // Case where user clicks on Already Member
            case R.id.appCompatTextViewLoginLink:

                // Send user to Login Activity
                Intent login = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(login);
                finish();
                break;
        }
    }

    /************************ POST DATA TO SQLITE **************************************************/
    private void postDataToSQLite(){

        // If name field is empty, inform user
        if (!inputValidation.isInputEditTextFilled(textInputEditTextName, textInputLayoutName, getString(R.string.error_message_name))) {
            return;
        }

        // If email field is empty, inform user
        if (!inputValidation.isInputEditTextFilled(textInputEditTextEmail, textInputLayoutEmail, getString(R.string.error_message_email))) {
            return;
        }

        // If email field is not valid, inform user
        if (!inputValidation.isInputEditTextEmail(textInputEditTextEmail, textInputLayoutEmail, getString(R.string.error_message_email))) {
            return;
        }

        // If password field is empty, inform user
        if (!inputValidation.isInputEditTextFilled(textInputEditTextPassword, textInputLayoutPassword, getString(R.string.error_message_password))) {
            return;
        }

        // If password and password confirmation do not match, inform user
        if (!inputValidation.isInputEditTextMatches(textInputEditTextPassword, textInputEditTextConfirmPassword,
                textInputLayoutConfirmPassword, getString(R.string.error_password_match))) {
            return;
        }

        // If the parsed email address does not belong to other user in database
        if (!databaseHelper.checkUser(textInputEditTextEmail.getText().toString().trim())) {

            // Add user's Name, Email and Password to database
            user.setName(textInputEditTextName.getText().toString().trim());
            user.setEmail(textInputEditTextEmail.getText().toString().trim());
            user.setPassword(textInputEditTextPassword.getText().toString().trim());

            // Set user's current playing difficulty to default 3 -> "Normal"
            user.setDifficulty(3);

            // Set user's current playing song to the first one
            user.setSong(1);

            // Set user score to 0
            user.setScore(0);

            // Set user's collected words to empty list
            List<String> removedMarkers = new ArrayList<>();
            removedMarkers.add(" ");
            Gson gson = new Gson();
            String inputString = gson.toJson(removedMarkers);
            user.setJson(inputString);

            // Set user distance walked to 0
            user.setWalked(0);

            // Set user coins walked to 0
            user.setCoins(0);

            // Set music on background to enabled
            user.setMusic(1);

            // Set is hint used to 'No hint used'
            user.setHint(0);

            // Add user to database
            databaseHelper.addUser(user);

            // Snack Bar to show success message that record saved successfully
            Snackbar.make(nestedScrollView, getString(R.string.success_message), Snackbar.LENGTH_LONG).show();
            emptyInputEditText();
        }
        // If the parsed email address belongs to other user in database
        else {

            // Snack Bar to show error message that record already exists
            Snackbar.make(nestedScrollView, getString(R.string.error_email_exists), Snackbar.LENGTH_LONG).show();
        }

    }

    // Empty Input by user fields
    private void emptyInputEditText(){
        textInputEditTextName.setText(null);
        textInputEditTextEmail.setText(null);
        textInputEditTextPassword.setText(null);
        textInputEditTextConfirmPassword.setText(null);
    }

    /*********************** ON PHONE BACK BUTTON PRESS ********************************************/
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)  {
        if (keyCode == KeyEvent.KEYCODE_BACK ) {

            // Take user to Login Activity
            Intent login = new Intent(getApplicationContext(), LoginActivity.class);
            startActivity(login);
            finish();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

}
