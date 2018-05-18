package co.skalenow.newrequirments;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import co.skalenow.CheckOut;
import co.skalenow.MainActivity;
import co.skalenow.R;

/**
 * Created by ADMIN on 30-Mar-17.
 */

public class OrderPreferences extends Fragment implements View.OnClickListener {

    Button btnconfirm, btndefault,btnviewcart;
    Fragment fragment;
    FragmentManager fm;
    FragmentTransaction ft;
    Spinner spspiner1,spspiner2,spspiner3,spspiner4,spspiner5;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        View view = inflater.inflate(R.layout.orderpreferences, container, false);
        btnconfirm = (Button) view.findViewById(R.id.btnconfirm);
        btndefault = (Button) view.findViewById(R.id.btndefault);
        btnviewcart= (Button) view.findViewById(R.id.btndefault);
        spspiner1 = (Spinner) view.findViewById(R.id.spspiner1);
        spspiner2 = (Spinner) view.findViewById(R.id.spspiner2);
        spspiner3 = (Spinner) view.findViewById(R.id.spspiner3);
        spspiner4 = (Spinner) view.findViewById(R.id.spspiner4);
        spspiner5 = (Spinner) view.findViewById(R.id.spspiner5);

        btnconfirm.setOnClickListener(this);
        btndefault.setOnClickListener(this);

        ArrayAdapter<CharSequence> Adapter1 = ArrayAdapter
                .createFromResource(getActivity(), R.array.gold,
                        android.R.layout.simple_spinner_item);

        // Specify the layout to use when the list of choices appears
        Adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spspiner1.setAdapter(Adapter1);
        spspiner1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                Log.v("item", (String) parent.getItemAtPosition(position));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // TODO Auto-generated method stub
            }
        });
        ArrayAdapter<CharSequence> Adapter2 = ArrayAdapter
                .createFromResource(getActivity(), R.array.purity,
                        android.R.layout.simple_spinner_item);

        // Specify the layout to use when the list of choices appears
        Adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spspiner2.setAdapter(Adapter2);
        spspiner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                Log.v("item", (String) parent.getItemAtPosition(position));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // TODO Auto-generated method stub
            }
        });
        ArrayAdapter<CharSequence> Adapter3 = ArrayAdapter
                .createFromResource(getActivity(), R.array.babysize,
                        android.R.layout.simple_spinner_item);

        // Specify the layout to use when the list of choices appears
        Adapter3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spspiner3.setAdapter(Adapter3);
        spspiner3.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                Log.v("item", (String) parent.getItemAtPosition(position));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // TODO Auto-generated method stub
            }
        });
        ArrayAdapter<CharSequence> Adapter4 = ArrayAdapter
                .createFromResource(getActivity(), R.array.backchailong,
                        android.R.layout.simple_spinner_item);

        // Specify the layout to use when the list of choices appears
        Adapter4.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spspiner4.setAdapter(Adapter3);
        spspiner4.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                Log.v("item", (String) parent.getItemAtPosition(position));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // TODO Auto-generated method stub
            }
        });
        ArrayAdapter<CharSequence> Adapter5 = ArrayAdapter
                .createFromResource(getActivity(), R.array.backchainshort,
                        android.R.layout.simple_spinner_item);

        // Specify the layout to use when the list of choices appears
        Adapter5.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spspiner5.setAdapter(Adapter4);
        spspiner4.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                Log.v("item", (String) parent.getItemAtPosition(position));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // TODO Auto-generated method stub
            }
        });
        btnviewcart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fragment = new ViewCart();
                ft = getActivity().getSupportFragmentManager().beginTransaction();
                ft.add(R.id.frame, fragment);
                ft.addToBackStack("add" + MainActivity.add);
                ft.commit();
                MainActivity.add++;
            }
        });
        return view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnconfirm:
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setMessage("Are you sure to confirm this order. All items will be move from the Shopping Cart to the Order. Are you sure?")
                        .setCancelable(false)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {

                                fragment = new CheckOut();
                                ft = getActivity().getSupportFragmentManager().beginTransaction();
                                ft.add(R.id.frame, fragment);
                                ft.addToBackStack("add" + MainActivity.add);
                                ft.commit();
                                MainActivity.add++;
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });
                AlertDialog alert = builder.create();
                alert.show();

                break;
            case R.id.btndefault:
                break;
        }
    }
}