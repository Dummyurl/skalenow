package co.skalenow.newrequirments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import co.skalenow.R;

/**
 * Created by ADMIN on 24-Mar-17.
 */

public class BrowseRatings extends Fragment {
    TextView txt;
    SharedPreferences sharedpreferences;
    public static final String MyPREFERENCES = "MyPrefs";
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        View view = inflater.inflate(R.layout.commingsoon, container, false);
        txt=(TextView)view.findViewById(R.id.txt);
        txt.setText("Browse Rating");
        sharedpreferences = getActivity().getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putString("page", "BrowseRating");
        editor.commit();
        return view;
    }
}