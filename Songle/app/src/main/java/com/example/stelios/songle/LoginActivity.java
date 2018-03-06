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
import android.view.View;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private final AppCompatActivity activity = LoginActivity.this;

    // FOR SCROLL VIEW
    private NestedScrollView nestedScrollView;

    // FOR EMAIL INPUT TEXT LAYOUT
    private TextInputLayout textInputLayoutEmail;

    // FOR PASSWORD INPUT TEXT LAYOUT
    private TextInputLayout textInputLayoutPassword;

    // FOR EMAIL INPUT TEXT
    private TextInputEditText textInputEditTextEmail;

    // FOR PASSWORD INPUT TEXT
    private TextInputEditText textInputEditTextPassword;

    // FOR LOGIN BUTTON
    private AppCompatButton appCompatButtonLogin;

    // FOR REGISTERING COMPAT TEXT
    private AppCompatTextView textViewLinkRegister;

    // FOR VALIDATION USE
    private InputValidation inputValidation;

    // FOR DATABASE USE
    private DatabaseHelper databaseHelper;

    // FOR TESTING
    private String verificationTesting;

    /************************ ON CREATE ************************************************************/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Set content view of activity_login.xml
        setContentView(R.layout.activity_login);

        // Initialise views, listeners and objects
        initViews();
        initListeners();
        initObjects();

    }

    /************************ INITIALISE VIEWS *****************************************************/
    private void initViews() {

        // Nested Scroll View
        nestedScrollView = (NestedScrollView) findViewById(R.id.nestedScrollView);

        // Email and Password Input Text Layout Views
        textInputLayoutEmail = (TextInputLayout) findViewById(R.id.textInputLayoutEmail);
        textInputLayoutPassword = (TextInputLayout) findViewById(R.id.textInputLayoutPassword);

        // Email and Password Input Text Views
        textInputEditTextEmail = (TextInputEditText) findViewById(R.id.textInputEditTextEmail);
        textInputEditTextPassword = (TextInputEditText) findViewById(R.id.textInputEditTextPassword);

        // Login Button View
        appCompatButtonLogin = (AppCompatButton) findViewById(R.id.appCompatButtonLogin);

        // Register Text (Button) View
        textViewLinkRegister = (AppCompatTextView) findViewById(R.id.textViewLinkRegister);
    }

    /************************ INITIALISE LISTENERS *************************************************/
    private void initListeners() {

        // Login and Register Buttons Listeners
        appCompatButtonLogin.setOnClickListener(this);
        textViewLinkRegister.setOnClickListener(this);
    }

    /************************ INITIALISE OBJECTS ***************************************************/
    private void initObjects() {
        databaseHelper = new DatabaseHelper(activity);
        inputValidation = new InputValidation(activity);
    }

    /*********************** ON CLICK **************************************************************/
    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            // On Login Button Click
            case R.id.appCompatButtonLogin:

                // Verify text parsed
                verifyFromSQLite();
                break;

            // On Register Click
            case R.id.textViewLinkRegister:

                // Take user to Register Activity
                Intent intentRegister = new Intent(getApplicationContext(), RegisterActivity.class);
                startActivity(intentRegister);
                break;
        }
    }

    /******************** VERIFY FROM SQLITE *******************************************************/

    private void verifyFromSQLite() {

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

        // If login details are correct, by checking email and password on database
        if (databaseHelper.checkUser(textInputEditTextEmail.getText().toString().trim()
                , textInputEditTextPassword.getText().toString().trim())) {

            // Take user to Users Activity (Main Screen)
            Intent accountsIntent = new Intent(activity, UsersActivity.class);

            // Take user's email, name and current playing difficulty for future use
            accountsIntent.putExtra("EMAIL", textInputEditTextEmail.getText().toString().trim());
            accountsIntent.putExtra("NAME", databaseHelper.getNameByEmail(textInputEditTextEmail.getText().toString().trim()));
            accountsIntent.putExtra("DIFFICULTY", databaseHelper.getDifficultyByEmail(textInputEditTextEmail.getText().toString().trim()));

            // Empty Input Fields
            emptyInputEditText();
            startActivity(accountsIntent);

        }

        // If login details are wrong
        else {

            // Inform the user that login details are wrong
            Snackbar.make(nestedScrollView, getString(R.string.error_valid_email_password), Snackbar.LENGTH_LONG).show();

           // verificationTesting = "Wrong User, not in Database";

        }
    }

    // Empty Input Fields (Email and Password)
    private void emptyInputEditText() {
        textInputEditTextEmail.setText(null);
        textInputEditTextPassword.setText(null);
    }
}

