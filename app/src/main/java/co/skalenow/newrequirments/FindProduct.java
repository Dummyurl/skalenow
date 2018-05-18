package co.skalenow.newrequirments;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;

import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import co.skalenow.MainActivity;
import co.skalenow.R;

/**
 * Created by ADMIN on 29-Mar-17.
 */

public class FindProduct extends Fragment implements View.OnClickListener {
    Button btnsearchProduct, btnviewcart;
    Fragment fragment;
    FragmentTransaction ft;
    EditText edt_code, edt_name, edt_weightfrom, edt_weightto, edt_reldatefrom, edt_reldateto, edtdateclear;
    String Code = "", Name = "", Weightfrom = "", Weightto = "", Datefrom = "", Dateto = "";
    public static String url = "";
    private DatePickerDialog datefrom, dateto;
    private SimpleDateFormat dateFormatter;
    SharedPreferences sharedpreferences;
    public static final String MyPREFERENCES = "MyPrefs";
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        View view = inflater.inflate(R.layout.findproduct, container, false);
        btnsearchProduct = (Button) view.findViewById(R.id.btnfindproduct);
        btnviewcart = (Button) view.findViewById(R.id.btnviewcart);
        edt_code = (EditText) view.findViewById(R.id.edt_code);
        edt_name = (EditText) view.findViewById(R.id.edt_name);
        edt_weightfrom = (EditText) view.findViewById(R.id.edtweightfrom);
        edt_weightto = (EditText) view.findViewById(R.id.edtweightto);
        edt_reldatefrom = (EditText) view.findViewById(R.id.edtreldatefrom);
        edt_reldateto = (EditText) view.findViewById(R.id.edtreldateto);
        edtdateclear = (EditText) view.findViewById(R.id.edtcleardate);
        dateFormatter = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
        edt_reldatefrom.setInputType(InputType.TYPE_NULL);
        edt_reldateto.setInputType(InputType.TYPE_NULL);
        edtdateclear.setInputType(InputType.TYPE_NULL);
        edtdateclear.setOnClickListener(this);
        btnviewcart.setOnClickListener(this);
        btnsearchProduct.setOnClickListener(this);

        sharedpreferences = getActivity().getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putString("page", "SearchProduct");
        editor.commit();

        Calendar newCalendar = Calendar.getInstance();
        datefrom = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {

            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                edt_reldatefrom.setText(dateFormatter.format(newDate.getTime()));
            }

        }, newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));
        dateto = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {

            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                edt_reldateto.setText(dateFormatter.format(newDate.getTime()));
            }

        }, newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));
        edt_reldatefrom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                datefrom.show();
            }
        });
        edt_reldatefrom.setOnFocusChangeListener(new View.OnFocusChangeListener() {

            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    // Always use a TextKeyListener when clearing a TextView to prevent android
                    // warnings in the log
                    datefrom.show();
                }
            }
        });
        edt_reldatefrom.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) {
                // put the code of save Database here
                edt_reldatefrom.setError(null);
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
        });
        edt_reldateto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dateto.show();
            }
        });
        edt_reldateto.setOnFocusChangeListener(new View.OnFocusChangeListener() {

            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    // Always use a TextKeyListener when clearing a TextView to prevent android
                    // warnings in the log
                    dateto.show();
                }
            }
        });
        edt_reldateto.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) {
                // put the code of save Database here
                edt_reldateto.setError(null);
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
        });

        return view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.edtcleardate:
                edt_reldatefrom.setText("");
                edt_reldateto.setText("");
                break;
            case R.id.btnviewcart:
                fragment = new ViewCart();
                ft = getActivity().getSupportFragmentManager().beginTransaction();
                ft.add(R.id.frame, fragment);
                ft.addToBackStack("add" + MainActivity.add);
                ft.commit();
                MainActivity.add++;
                break;
            case R.id.btnfindproduct:
                Code = edt_code.getText().toString();
                Name = edt_name.getText().toString();
                Weightfrom = edt_weightfrom.getText().toString();
                Weightto = edt_weightto.getText().toString();
                Datefrom = edt_reldatefrom.getText().toString();
                Dateto = edt_reldateto.getText().toString();
                if (Datefrom.equalsIgnoreCase("Select a Date")) {
                    Datefrom = "";
                }
                if (Dateto.equalsIgnoreCase("Select a Date")) {
                    Dateto = "";
                }
                url = "?pname=" + URLEncoder.encode(Name) + "&pcode=" + URLEncoder.encode(Code) + "&weightfrom=" + URLEncoder.encode(Weightfrom) + "&weightto=" + URLEncoder.encode(Weightto) + "&datefrom=" + URLEncoder.encode(Datefrom) + "&dateto=" + URLEncoder.encode(Dateto);
                fragment = new Home();
                ft = getActivity().getSupportFragmentManager().beginTransaction();
                ft.add(R.id.frame, fragment);
                ft.addToBackStack("add" + MainActivity.add);
                ft.commit();
                MainActivity.add++;
                break;
        }
    }
}