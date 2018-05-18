package co.skalenow;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class SplashPage extends AppCompatActivity {
    ImageView imglock;
    String status = "0";
    public static String DeviceId = "";
    Toolbar toolbar;
    RelativeLayout rlinput, rlsubmit, rlmessage;
    String Username = "", Userpass = "";
    EditText edtname, edtpass;
    SharedPreferences sharedpreferences;
    public static final String MyPREFERENCES = "MyPrefs";
    int LoginId;
TextView txtlogoname;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE,
                WindowManager.LayoutParams.FLAG_SECURE);
        setContentView(R.layout.activity_splash_page);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        rlinput = (RelativeLayout) findViewById(R.id.rlinput);
        rlsubmit = (RelativeLayout) findViewById(R.id.rlsubmit);
        rlmessage = (RelativeLayout) findViewById(R.id.rlmessage);
        edtname = (EditText) findViewById(R.id.edtname);
        edtpass = (EditText) findViewById(R.id.edtpass);
        rlinput.setVisibility(View.GONE);
        rlsubmit.setVisibility(View.GONE);
        rlmessage.setVisibility(View.GONE);
        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        if (toolbar != null) {

            setSupportActionBar(toolbar);
        }
        DeviceId = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        LoginId = sharedpreferences.getInt("LoginId", 0);
        txtlogoname=(TextView)findViewById(R.id.txtlogoname);
//        if (LoginId != 0) {
//            Intent in = new Intent(SplashPage.this, MainActivity.class);
//            startActivity(in);
//            SplashPage.this.finish();
//        }
        String text = "<font color=#008edc><b>Skale</b></font><font color=#e77511><i>now</i></font>";
        txtlogoname.setText(Html.fromHtml(text));
//        txtlogoname.setText(Html.fromHtml("Multiple style inside android textview: bold text: <b>bold</b>, italic text: <i>italic</i>, small font: <small>small text</small>, " +
//                "font color: <font color=\"red\">Text Color</font>, " +
//                "font color with bold text: <font color=\"red\"><b>Bold with font color</b></font>"));
        imglock = (ImageView) findViewById(R.id.imglock);
        imglock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ConnectionDetector cd = new ConnectionDetector(getApplicationContext());
                if (!cd.isConnectingToInternet()) {

                    Toast.makeText(getApplicationContext(), "Please check your Internet connection", Toast.LENGTH_SHORT).show();
                } else {
                    new Progress().execute();
                }

            }
        });
        edtname.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) {
                // put the code of save Database here
                edtname.setError(null);
                rlmessage.setVisibility(View.GONE);
            }
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
        });
        edtpass.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) {
                // put the code of save Database here
                edtpass.setError(null);
                rlmessage.setVisibility(View.GONE);
            }
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
        });
        rlsubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Username = edtname.getText().toString();
                Userpass = edtpass.getText().toString();
                rlmessage.setVisibility(View.GONE);
                if (Username.replace(" ", "").equalsIgnoreCase("")) {
                    edtname.requestFocus();
                    edtname.setText("");
                    edtname.setError("fill user name");
                } else {
                    if (Userpass.replace(" ", "").equalsIgnoreCase("")) {
                        edtpass.requestFocus();
                        edtpass.setText("");
                        edtpass.setError("fill password");
                    } else {
                        if ((Username.equalsIgnoreCase("admin")) & (Userpass.equalsIgnoreCase("123"))) {
                            SharedPreferences.Editor editor = sharedpreferences.edit();
                            editor.putInt("LoginId", Integer.parseInt("1"));
                            editor.commit();
                            Intent in = new Intent(SplashPage.this, MainActivity.class);
                            startActivity(in);
                            SplashPage.this.finish();
                        } else {
                            rlmessage.setVisibility(View.VISIBLE);
                        }
                    }
                }

            }
        });
        edtname.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) {
                // put the code of save Database here
                edtname.setError(null);
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
        });
        edtpass.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) {
                // put the code of save Database here
                edtpass.setError(null);
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
        });
//        getSupportActionBar().setTitle(Html.fromHtml(text));
        getSupportActionBar().setLogo(R.drawable.title);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        edtname.setCustomSelectionActionModeCallback(new ActionMode.Callback() {

            public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
                return false;
            }

            public void onDestroyActionMode(ActionMode mode) {
            }

            public boolean onCreateActionMode(ActionMode mode, Menu menu) {
                return false;
            }

            public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
                return false;
            }
        });
        edtname.setLongClickable(false);
        edtname.setTextIsSelectable(false);
        edtpass.setCustomSelectionActionModeCallback(new ActionMode.Callback() {

            public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
                return false;
            }

            public void onDestroyActionMode(ActionMode mode) {
            }

            public boolean onCreateActionMode(ActionMode mode, Menu menu) {
                return false;
            }

            public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
                return false;
            }
        });
        edtpass.setLongClickable(false);
        edtpass.setTextIsSelectable(false);

    }

    public class Progress extends AsyncTask<String, String, String> {
        private ProgressDialog pdia;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pdia = new ProgressDialog(SplashPage.this);
            pdia.setMessage("Loading...");
            pdia.show();
            pdia.setCancelable(false);
        }

        @Override
        protected String doInBackground(String... strings) {
            JSONParser jParser = new JSONParser();
            // getting JSON string from URL//fname,lname,email,pwd,pwd2,zipcode,month,day,subscriber      checkuserlogin=1,user_login=emailid,user_pass=password
            JSONObject json = jParser.getJSONFromUrl("http://www.puyangan.com/security-poc/signup.php?deviceid="+DeviceId);
            try {

                JSONArray successArray = json.getJSONArray("info");
                for (int i = 0; i < successArray.length(); i++) {
                    JSONObject registerObj = (JSONObject) successArray.get(i);
                    if (registerObj.has("status")) {
                        status = registerObj.getString("status");
                    }
                }
            } catch (JSONException e) {

                e.printStackTrace();

            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            pdia.dismiss();
            pdia = null;
            if (status.equalsIgnoreCase("0")) {
                AlertDialog alertDialog = new AlertDialog.Builder(SplashPage.this).create();
                alertDialog.setCancelable(false);
                alertDialog.setTitle("Warning!");
                alertDialog.setMessage("You are not authorized to access this app.Please contact the Owner.");
                alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        SplashPage.this.finish();
                    }
                });
                alertDialog.show();
            } else if (status.equalsIgnoreCase("1")) {
                imglock.setVisibility(View.GONE);
                rlinput.setVisibility(View.VISIBLE);
                rlsubmit.setVisibility(View.VISIBLE);
            }
        }
    }
}
