package com.karpool.karpl_passenger;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Point;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.app.LoaderManager.LoaderCallbacks;

import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;

import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.TextUtils;
import android.view.Display;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Space;
import android.widget.TextView;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;

import static android.Manifest.permission.READ_CONTACTS;
import android.widget.CheckBox;
import android.widget.Toast;


/**
 * Handles logging in and signing up
 */
public class LoginActivity extends AppCompatActivity implements LoaderCallbacks<Cursor> {


    /**
     * Id to identity READ_CONTACTS permission request.
     */
    private static final int REQUEST_READ_CONTACTS = 0;


    /**
     * Keep track of the login task to ensure we can cancel it if requested.
     */

    private final static String KEY_USER = "userID";

    // UI references.

    private View mProgressView;
    private View mLoginFormView;
    private View mRegisterFormView;
    private Space progressSpace;
    private ImageView logoImage;
    private Button signInPrompt, registerPrompt, signInButton, registerButton;
    private AutoCompleteTextView signInName, registerEmail, registerPhone;
    private EditText signInPassword, registerName, registerPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);

        // if the user is already signed in, skip this and open the main activity
        if (prefs.getString("userID", null) != null) {
            Intent I = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(I);
            finish();
        }


        signInPrompt = findViewById(R.id.signInPrompt);
        registerPrompt = findViewById(R.id.registerPrompt);
        signInName = findViewById(R.id.signInName);
        signInPassword = findViewById(R.id.signInPassword);
        signInButton = findViewById(R.id.signInButton);
        registerPrompt = findViewById(R.id.registerPrompt);
        registerEmail = findViewById(R.id.registerEmail);
        registerName = findViewById(R.id.registerName);
        registerPhone = findViewById(R.id.registerPhone);
        registerPassword = findViewById(R.id.registerPassword);
        registerButton = findViewById(R.id.registerButton);


        // Handling registration or log in options

        signInPrompt.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                setElementVisibility("signIn", false);

            }
        });
        registerPrompt.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                setElementVisibility("register", false);


            }
        });


        // Set up the login form.

        populateAutoComplete();

        signInPassword.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == EditorInfo.IME_ACTION_DONE || id == EditorInfo.IME_NULL) {
                    attemptLogin();
                    return true;
                }
                return false;
            }
        });

        signInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptLogin();
            }
        });


        registerPassword.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == EditorInfo.IME_ACTION_DONE || id == EditorInfo.IME_NULL) {
                    attemptRegistration();
                    return true;
                }
                return false;
            }
        });

        registerButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                    attemptRegistration();
            }
        });


        mLoginFormView = findViewById(R.id.login_form);
        mRegisterFormView = findViewById(R.id.register_form);
        mProgressView = findViewById(R.id.login_progress);
        logoImage = findViewById(R.id.logoImage);
        progressSpace = findViewById(R.id.progressSpace);


    }


    private void populateAutoComplete() {
        if (!mayRequestContacts()) {
            return;
        }

        getLoaderManager().initLoader(0, null, this);
    }

    private boolean mayRequestContacts() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return true;
        }
        if (checkSelfPermission(READ_CONTACTS) == PackageManager.PERMISSION_GRANTED) {
            return true;
        }
        if (shouldShowRequestPermissionRationale(READ_CONTACTS)) {
            Snackbar.make(signInName, R.string.permission_rationale, Snackbar.LENGTH_INDEFINITE)
                    .setAction(android.R.string.ok, new View.OnClickListener() {
                        @Override
                        @TargetApi(Build.VERSION_CODES.M)
                        public void onClick(View v) {
                            requestPermissions(new String[]{READ_CONTACTS}, REQUEST_READ_CONTACTS);
                        }
                    });
        } else {
            requestPermissions(new String[]{READ_CONTACTS}, REQUEST_READ_CONTACTS);
        }
        return false;
    }

    /**
     * Callback received when a permissions request has been completed.
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode == REQUEST_READ_CONTACTS) {
            if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                populateAutoComplete();
            }
        }
    }


    /**
     * Attempts to sign in the account specified by the login form.
     * If there are form errors such as an empty field, no log in attempt is made
     */
    private void attemptLogin() {

        // Reset errors.
        signInName.setError(null);
        signInPassword.setError(null);

        // Store values at the time of the login attempt.
        String username = signInName.getText().toString();
        String password = signInPassword.getText().toString();

        boolean cancel = false;
        View focusView = null;


        if (TextUtils.isEmpty(password)) {
            signInPassword.setError(getString(R.string.error_field_required));
            focusView = signInPassword;
            cancel = true;
        }

        // Check for a valid email address.
        if (TextUtils.isEmpty(username)) {
            signInName.setError(getString(R.string.error_field_required));
            focusView = signInName;
            cancel = true;
        }


        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
            showSignInProgress(true);
            userLogInTask(username, password);
        }
    }


    /**
     * Attempts to register the account specified by the register form.
     * If there are form errors such as missing fields, no register attempt is made.
     */
    private void attemptRegistration() {


        // Reset errors.
        registerName.setError(null);
        registerEmail.setError(null);
        registerPhone.setError(null);
        registerPassword.setError(null);


        // Store values at the time of the registration attempt.
        String email = registerEmail.getText().toString();
        String name = registerName.getText().toString();
        String phoneNumber = registerPhone.getText().toString();
        String password = registerPassword.getText().toString();

        boolean cancel = false;
        View focusView = null;

        if (TextUtils.isEmpty(name)) {
            registerName.setError(getString(R.string.error_field_required));
            focusView = registerName;
            cancel = true;
        }

        if (TextUtils.isEmpty(email)) {
            registerEmail.setError(getString(R.string.error_field_required));
            focusView = registerEmail;
            cancel = true;
        }


        if (TextUtils.isEmpty(phoneNumber)) {
            registerPhone.setError(getString(R.string.error_field_required));
            focusView = registerPhone;
            cancel = true;
        }

        if (TextUtils.isEmpty(password)) {
            registerPassword.setError(getString(R.string.error_field_required));
            focusView = registerPassword;
            cancel = true;
        }




        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user registration attempt.
            showRegistrationProgress(true);
            userRegisterTask(email, name, password, phoneNumber);
        }
    }



    /**
     * Shows the progress UI and hides the login form.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showSignInProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            // added by me
            logoImage.setVisibility(show ? View.GONE : View.VISIBLE);
            progressSpace.setVisibility(show ? View.VISIBLE : View.GONE);
            mLoginFormView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(mLoginFormView.getWindowToken(), 0);
            // MOVING PROGRESS BAR TO THE MIDDLE

            Display display = getWindowManager().getDefaultDisplay();
            Point size = new Point();
            display.getSize(size);
            int height = size.y;
            progressSpace.getLayoutParams().height = (int) (height / 2.5);
            progressSpace.setVisibility(show ? View.VISIBLE : View.GONE);
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mProgressView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);

                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            progressSpace.setVisibility(show ? View.VISIBLE : View.GONE);
            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            logoImage.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }


    /**
     * Shows the progress UI and hides the registration form.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showRegistrationProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            mRegisterFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            // added by me
            logoImage.setVisibility(show ? View.GONE : View.VISIBLE);
            progressSpace.setVisibility(show ? View.VISIBLE : View.GONE);
            mRegisterFormView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mRegisterFormView.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(mRegisterFormView.getWindowToken(), 0);
            // MOVING PROGRESS BAR TO THE MIDDLE

            Display display = getWindowManager().getDefaultDisplay();
            Point size = new Point();
            display.getSize(size);
            int height = size.y;
            progressSpace.getLayoutParams().height = (int) (height / 2.5);
            progressSpace.setVisibility(show ? View.VISIBLE : View.GONE);
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mProgressView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);

                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            progressSpace.setVisibility(show ? View.VISIBLE : View.GONE);
            mRegisterFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            logoImage.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }


    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        return new CursorLoader(this,
                // Retrieve data rows for the device user's 'profile' contact.
                Uri.withAppendedPath(ContactsContract.Profile.CONTENT_URI,
                        ContactsContract.Contacts.Data.CONTENT_DIRECTORY), ProfileQuery.PROJECTION,

                // Select only email addresses.
                ContactsContract.Contacts.Data.MIMETYPE +
                        " = ?", new String[]{ContactsContract.CommonDataKinds.Email
                .CONTENT_ITEM_TYPE},

                // Show primary email addresses first. Note that there won't be
                // a primary email address if the user hasn't specified one.
                ContactsContract.Contacts.Data.IS_PRIMARY + " DESC");
    }

    @Override
    public void onLoadFinished(Loader<Cursor> cursorLoader, Cursor cursor) {
        List<String> emails = new ArrayList<>();
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            emails.add(cursor.getString(ProfileQuery.ADDRESS));
            cursor.moveToNext();
        }

        addEmailsToAutoComplete(emails);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> cursorLoader) {

    }

    private void addEmailsToAutoComplete(List<String> emailAddressCollection) {
        //Create adapter to tell the AutoCompleteTextView what to show in its dropdown list.
        ArrayAdapter<String> adapter =
                new ArrayAdapter<>(LoginActivity.this,
                        android.R.layout.simple_dropdown_item_1line, emailAddressCollection);

        signInName.setAdapter(adapter);
    }


    private interface ProfileQuery {
        String[] PROJECTION = {
                ContactsContract.CommonDataKinds.Email.ADDRESS,
                ContactsContract.CommonDataKinds.Email.IS_PRIMARY,
        };

        int ADDRESS = 0;
        int IS_PRIMARY = 1;
    }


    /**
     * Attempts to authorize the user account and log in through an asynchronous task
     * @param mName the username
     * @param mPassword the password
     */
    public void userLogInTask(String mName, String mPassword) {

        final String userID = mName;

        HttpUtils.get("passengers/auth/" + mName + "/" + mPassword, new RequestParams(), new JsonHttpResponseHandler() {

            @Override
            public void onFinish() {
                showSignInProgress(false);

            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {
                    if (response.getBoolean("response") == true) {
                        showSignInProgress(false);
                        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(LoginActivity.this);
                        prefs.edit().putString(KEY_USER, userID).commit(); // adding userID to preferences for future use
                        Intent I = new Intent(LoginActivity.this, MainActivity.class);
                        startActivity(I);
                        finish();
                    } else {
                        showSignInProgress(false);
                        Toast.makeText(LoginActivity.this, response.getString("error"),
                                Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject
                    errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
                showSignInProgress(false);
                Toast.makeText(LoginActivity.this, "There was a network error, try again later.", Toast.LENGTH_LONG).show(); // generic network error

            }
        });
    }


    /**
     * Attempts to register the user account through an asynchronous task
     * @param mEmail user email
     * @param mName username
     * @param mPassword user password
     * @param mPhone user phone number
     */
    public void userRegisterTask(String mEmail, String mName, String mPassword, String mPhone) {


        HttpUtils.post("passengers/" + mName + "/" + mEmail + "/" + mPassword + "/" + mPhone + "/false", new RequestParams(), new JsonHttpResponseHandler() {
            @Override
            public void onFinish() {
                showRegistrationProgress(false);

            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {
                    showRegistrationProgress(false);
                    if (!response.isNull("name")) {
                        setElementVisibility("register", true);
                    } else {
                        showRegistrationProgress(false);
                        Toast.makeText(LoginActivity.this, response.getString("error"), Toast.LENGTH_LONG).show(); // log in error

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject
                    errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
                Toast.makeText(LoginActivity.this, "There was a network error, try again later.", Toast.LENGTH_LONG).show(); // generic network error
                showRegistrationProgress(false);


            }
        });




    }


    // This is added to return the main page when you are in the process of signing up/registering
    @Override
    public void onBackPressed() {
        if (signInButton.getVisibility() == View.VISIBLE) {
            setElementVisibility("signIn", true);
        } else if (registerButton.getVisibility() == View.VISIBLE) {
            setElementVisibility("register", true);
        } else {
            super.onBackPressed();
        }
    }


    /**
     * This method handles the visibility of elements on the logIn activity.
     *
     * @param mode    signIn or Registration depends on the user's button selection
     * @param reverse This is to complete the opposite action when the back button is pressed, i.e, go back to main page
     */
    private void setElementVisibility(String mode, Boolean reverse) {
        if (mode == "signIn") {
            if (!reverse) {
                registerPrompt.setVisibility(View.GONE);
                signInPrompt.setVisibility(View.GONE);
                mRegisterFormView.setVisibility(View.GONE);
                mLoginFormView.setVisibility(View.VISIBLE);
                signInName.setVisibility(View.VISIBLE);
                signInPassword.setVisibility(View.VISIBLE);
                signInButton.setVisibility(View.VISIBLE);
            } else {
                registerPrompt.setVisibility(View.VISIBLE);
                signInPrompt.setVisibility(View.VISIBLE);
                mLoginFormView.setVisibility(View.GONE);
                mRegisterFormView.setVisibility(View.GONE);
                signInName.setVisibility(View.GONE);
                signInName.setText("");
                signInName.setError(null);
                signInPassword.setVisibility(View.GONE);
                signInPassword.setText("");
                signInPassword.setError(null);
                signInButton.setVisibility(View.GONE);
            }
        } else if (mode == "register") {
            if (!reverse) {
                registerPrompt.setVisibility(View.GONE);
                signInPrompt.setVisibility(View.GONE);
                mLoginFormView.setVisibility(View.GONE);
                mRegisterFormView.setVisibility(View.VISIBLE);
                registerName.setVisibility(View.VISIBLE);
                registerEmail.setVisibility(View.VISIBLE);
                registerPassword.setVisibility(View.VISIBLE);
                registerPhone.setVisibility(View.VISIBLE);
                registerButton.setVisibility(View.VISIBLE);
            } else {
                registerPrompt.setVisibility(View.VISIBLE);
                signInPrompt.setVisibility(View.VISIBLE);
                mLoginFormView.setVisibility(View.GONE);
                mRegisterFormView.setVisibility(View.GONE);
                registerName.setVisibility(View.GONE);
                registerName.setText("");
                registerName.setError(null);
                registerEmail.setVisibility(View.GONE);
                registerEmail.setText("");
                registerEmail.setError(null);
                registerPassword.setVisibility(View.GONE);
                registerPassword.setText("");
                registerPassword.setError(null);
                registerPhone.setVisibility(View.GONE);
                registerPhone.setText("");
                registerPhone.setError(null);
                registerButton.setVisibility(View.GONE);

            }
        }

    }
}


