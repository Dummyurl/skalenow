package co.skalenow.newrequirments;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.text.DecimalFormat;
import java.util.ArrayList;

import co.skalenow.Holder;
import co.skalenow.JSONParser;
import co.skalenow.MainActivity;
import co.skalenow.Order;
import co.skalenow.R;
import co.skalenow.SplashPage;
import co.skalenow.dragwithzoom.DragZoomActivity;

/**
 * Created by ADMIN on 30-Mar-17.
 */

public class HomeMore extends Fragment implements View.OnClickListener {

    Button btnviewcart;
    Fragment fragment;
    FragmentManager fm;
    FragmentTransaction ft;
    Spinner spspineritems, spspinerweight;
    Bitmap myBitmap;
    ImageView img_product;
    ImageView imgdot0, imgdot1, imgdot2, imgdot3, imgdot4;
    String Pname, Pcode, Pweight, PimageUrl, Pid, Discription;//Pimage
    TextView txt_product, txt_p_gram, txtdescription;
    int Quantity = 1;
    String SQuantity;
    EditText edtitems, edtweight;
    Button convert, converttoitems;
    JSONArray imagelist = null;
    String Image = "";
    ArrayList<Holder> list = new ArrayList<Holder>();
    ImageView imgzoom, imgtakepicture, imgaddtocart;
    SharedPreferences sharedpreferences;
    public static final String MyPREFERENCES = "MyPrefs";
    int LoginId;
    String Weight, weighttype, WeightQuantity;
    LinearLayout lltop;
    boolean convertbutton = false;
    byte[] Url, originalUrl;
Button btnback;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        View view = inflater.inflate(R.layout.home_more_page, container, false);
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        btnviewcart = (Button) view.findViewById(R.id.btnviewcart);
        spspineritems = (Spinner) view.findViewById(R.id.spspineritems);
        spspinerweight = (Spinner) view.findViewById(R.id.spspinerweight);
        img_product = (ImageView) view.findViewById(R.id.img_product);
        txt_product = (TextView) view.findViewById(R.id.txt_product);
        txt_p_gram = (TextView) view.findViewById(R.id.txt_p_gram);
        txtdescription = (TextView) view.findViewById(R.id.txtdescription);
        edtitems = (EditText) view.findViewById(R.id.edtitems);
        edtweight = (EditText) view.findViewById(R.id.edtweight);
        convert = (Button) view.findViewById(R.id.convert);
        converttoitems = (Button) view.findViewById(R.id.convertweight);
        imgdot0 = (ImageView) view.findViewById(R.id.imgdot0);
        imgdot1 = (ImageView) view.findViewById(R.id.imgdot1);
        imgdot2 = (ImageView) view.findViewById(R.id.imgdot2);
        imgdot3 = (ImageView) view.findViewById(R.id.imgdot3);
        imgdot4 = (ImageView) view.findViewById(R.id.imgdot4);
        imgzoom = (ImageView) view.findViewById(R.id.img_zoom);
        imgtakepicture = (ImageView) view.findViewById(R.id.imgtakepicture);
        imgaddtocart = (ImageView) view.findViewById(R.id.imgaddtocart);
        lltop = (LinearLayout) view.findViewById(R.id.lltop);
        btnback=(Button)view.findViewById(R.id.btnback);
        imgdot0.setOnClickListener(this);
        imgdot1.setOnClickListener(this);
        imgdot2.setOnClickListener(this);
        imgdot3.setOnClickListener(this);
        imgdot4.setOnClickListener(this);
        convert.setOnClickListener(this);
        converttoitems.setOnClickListener(this);
        imgzoom.setOnClickListener(this);
        imgtakepicture.setOnClickListener(this);
        imgaddtocart.setOnClickListener(this);
        lltop.setOnClickListener(this);
        btnback.setOnClickListener(this);
        Pname = getArguments().getString("pname");
        Pcode = getArguments().getString("pcode");
        Pweight = getArguments().getString("pweight");
        PimageUrl = getArguments().getString("pimage");
//        Pimage = getArguments().getString("Pimage");
        Url = originalUrl = getArguments().getByteArray("imageurl");
        Pid = getArguments().getString("pid");
        Discription = getArguments().getString("description");
        txtdescription.setText(Discription);
        txt_p_gram.setText("(" + Pweight + " gm)");
        txt_product.setText(Pname + " (" + Pcode + ")");

//        byte[] encodeByte = Base64.decode(Pimage, Base64.DEFAULT);
        Bitmap bitmap = BitmapFactory.decodeByteArray(originalUrl, 0, originalUrl.length);
        img_product.setImageBitmap(bitmap);
        imgdot0.setBackgroundResource(R.drawable.selectdot);
        imgdot1.setBackgroundResource(R.drawable.unselectdot);
        imgdot2.setBackgroundResource(R.drawable.unselectdot);
        imgdot3.setBackgroundResource(R.drawable.unselectdot);
        imgdot4.setBackgroundResource(R.drawable.unselectdot);
        ArrayAdapter<CharSequence> Adapter1 = ArrayAdapter
                .createFromResource(getActivity(), R.array.productitems,
                        android.R.layout.simple_spinner_item);

        // Specify the layout to use when the list of choices appears
        Adapter1.setDropDownViewResource(R.layout.spinner_row);
        spspineritems.setAdapter(Adapter1);
        spspineritems.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                WeightQuantity = parent.getItemAtPosition(position).toString();

                if (WeightQuantity.equalsIgnoreCase("Custom")) {
                    if (!convertbutton) {
                        edtitems.setVisibility(View.VISIBLE);
                        edtitems.setText("");
                        Quantity = 0;
                        edtweight.setText("" + Quantity * Float.parseFloat(Pweight));
                        convert.setVisibility(View.GONE);
                        spspinerweight.setSelection(0);
                        convertbutton = false;
                    } else {
//                        edtweight.setText("" + Quantity * Float.parseFloat(Pweight));
                        convertbutton = false;
                        edtitems.setVisibility(View.VISIBLE);
                        edtitems.setText(""+Quantity);
                        if(weighttype.equalsIgnoreCase("gm"))
                        {
                            spspinerweight.setSelection(1);
                        }
                        else if(weighttype.equalsIgnoreCase("kg"))
                        {
                            spspinerweight.setSelection(2);
                        }

                    }


//                    edtitems.setVisibility(View.VISIBLE);
//                    edtitems.setText("");
//                    Quantity = 0;
//                    edtweight.setText("" + Quantity * Float.parseFloat(Pweight));
//                    convert.setVisibility(View.GONE);
//                    spspinerweight.setSelection(0);

                } else {
                    if (!convertbutton)
                    {
                        edtitems.setVisibility(View.GONE);
                        Quantity = Integer.parseInt(WeightQuantity);
                        edtweight.setText("" + Quantity * Float.parseFloat(Pweight));
                        spspinerweight.setSelection(1);
                        convert.setVisibility(View.GONE);
                    }
                    else
                    {
                        Quantity = Integer.parseInt(WeightQuantity);
//                        edtweight.setText("" + Quantity * Float.parseFloat(Pweight));
                        convertbutton = false;
                        edtitems.setVisibility(View.GONE);
                        spspinerweight.setSelection(1);
                        convert.setVisibility(View.GONE);
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // TODO Auto-generated method stub
            }
        });

        ArrayAdapter<CharSequence> Adapter2 = ArrayAdapter
                .createFromResource(getActivity(), R.array.weighttype,
                        android.R.layout.simple_spinner_item);

        // Specify the layout to use when the list of choices appears
        Adapter2.setDropDownViewResource(R.layout.spinner_row);
        spspinerweight.setAdapter(Adapter2);
        spspinerweight.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                weighttype = parent.getItemAtPosition(position).toString();
                if (weighttype.equalsIgnoreCase("gm")) {
                    if (WeightQuantity.equalsIgnoreCase("Custom")) {
                        if (edtitems.getText().toString().equalsIgnoreCase("")) {
                            Toast.makeText(getActivity(), "fill number of items", Toast.LENGTH_LONG).show();
                        } else {
                            DecimalFormat df = new DecimalFormat("#");
                            df.setMaximumFractionDigits(4);
                            Quantity = Integer.parseInt(edtitems.getText().toString());
                            edtweight.setText("" + String.format("%.2f",(Integer.parseInt(edtitems.getText().toString()) * Float.parseFloat(Pweight))));
                        }

                    } else {
                        DecimalFormat df = new DecimalFormat("#");
                        df.setMaximumFractionDigits(8);
                        edtweight.setText("" + Quantity * Float.parseFloat(Pweight));
                    }
                } else if (weighttype.equalsIgnoreCase("kg")) {
                    if (WeightQuantity.equalsIgnoreCase("Custom")) {
                        if (edtitems.getText().toString().equalsIgnoreCase("")) {
                            Toast.makeText(getActivity(), "fill number of items", Toast.LENGTH_LONG).show();
                        } else {
                            DecimalFormat df = new DecimalFormat("#");
                            df.setMaximumFractionDigits(8);
                            Quantity = Integer.parseInt(edtitems.getText().toString());
                            edtweight.setText("" + String.format("%.6f",(Quantity * Float.parseFloat(Pweight)) / 1000));
                        }

                    } else {
                        DecimalFormat df = new DecimalFormat("#");
                        df.setMaximumFractionDigits(8);


//                        System.out.println(df.format(myvalue));

                        edtweight.setText("" + df.format((Quantity * Double.parseDouble(Pweight)) / 1000));
                    }
                } else {
                    edtweight.setText("0.0");
                }
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

        new Progress().execute();
//        imgdot1.setBackgroundResource(R.drawable.greendot);
//        imgdot2.setBackgroundResource(R.drawable.blackdot);
//        imgdot3.setBackgroundResource(R.drawable.blackdot);
//        imgdot4.setBackgroundResource(R.drawable.blackdot);
        edtitems.setCustomSelectionActionModeCallback(new ActionMode.Callback() {

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
        edtitems.setLongClickable(false);
        edtitems.setTextIsSelectable(false);
        edtweight.setCustomSelectionActionModeCallback(new ActionMode.Callback() {

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
        edtweight.setLongClickable(false);
        edtweight.setTextIsSelectable(false);
        return view;
    }

    @Override
    public void onClick(View v) {
        byte[] encodeByte;
        Holder h;
        byte[] b;
        ByteArrayOutputStream baos;
        switch (v.getId()) {

            case R.id.btnviewcart:
                fragment = new Order();
                ft = getActivity().getSupportFragmentManager().beginTransaction();
                ft.add(R.id.frame, fragment);
                ft.addToBackStack("add" + MainActivity.add);
                ft.commit();
                MainActivity.add++;
                break;
            case R.id.imgdot0:
//                byte[] encodeBytes = Base64.decode(getArguments().getString("Pimage"), Base64.DEFAULT);
//                Bitmap bitmap = BitmapFactory.decodeByteArray(encodeBytes, 0, encodeBytes.length);
                Bitmap bitmap = BitmapFactory.decodeByteArray(originalUrl, 0, originalUrl.length);
                img_product.setImageBitmap(bitmap);
                // ++++++++++++++++++++++for current image zoom+++++++++++++++++
                baos = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
                b = baos.toByteArray();
                Url = b;
                // ++++++++++++++++++++++for current image zoom+++++++++++++++++
                imgdot0.setBackgroundResource(R.drawable.selectdot);
                imgdot1.setBackgroundResource(R.drawable.unselectdot);
                imgdot2.setBackgroundResource(R.drawable.unselectdot);
                imgdot3.setBackgroundResource(R.drawable.unselectdot);
                imgdot4.setBackgroundResource(R.drawable.unselectdot);

                break;
            case R.id.imgdot1:
                if (list.size() > 0) {
                    h = list.get(0);
                    // ++++++++++++++++++++++for current image zoom+++++++++++++++++
                    baos = new ByteArrayOutputStream();
                    h.getBitmap().compress(Bitmap.CompressFormat.PNG, 100, baos);
                    b = baos.toByteArray();
                    Url = b;
                    // ++++++++++++++++++++++for current image zoom+++++++++++++++++
                    img_product.setImageBitmap(h.getBitmap());
                    imgdot0.setBackgroundResource(R.drawable.unselectdot);
                    imgdot1.setBackgroundResource(R.drawable.selectdot);
                    imgdot2.setBackgroundResource(R.drawable.unselectdot);
                    imgdot3.setBackgroundResource(R.drawable.unselectdot);
                    imgdot4.setBackgroundResource(R.drawable.unselectdot);
                }
                break;
            case R.id.imgdot2:
//                encodeByte = Base64.decode(arraylist.get(1), Base64.DEFAULT);
//                myBitmap = BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
//                img_product.setImageBitmap(myBitmap);
                if (list.size() > 1) {
                    h = list.get(1);
                    // ++++++++++++++++++++++for current image zoom+++++++++++++++++
                    baos = new ByteArrayOutputStream();
                    h.getBitmap().compress(Bitmap.CompressFormat.PNG, 100, baos);
                    b = baos.toByteArray();
                    Url = b;
                    // ++++++++++++++++++++++for current image zoom+++++++++++++++++
                    img_product.setImageBitmap(h.getBitmap());
                    imgdot0.setBackgroundResource(R.drawable.unselectdot);
                    imgdot1.setBackgroundResource(R.drawable.unselectdot);
                    imgdot2.setBackgroundResource(R.drawable.selectdot);
                    imgdot3.setBackgroundResource(R.drawable.unselectdot);
                    imgdot4.setBackgroundResource(R.drawable.unselectdot);
                }
                break;
            case R.id.imgdot3:
//                encodeByte = Base64.decode(arraylist.get(2), Base64.DEFAULT);
//                myBitmap = BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
//                img_product.setImageBitmap(myBitmap);
                if (list.size() > 2) {
                    h = list.get(2);
                    // ++++++++++++++++++++++for current image zoom+++++++++++++++++
                    baos = new ByteArrayOutputStream();
                    h.getBitmap().compress(Bitmap.CompressFormat.PNG, 100, baos);
                    b = baos.toByteArray();
                    Url = b;
                    // ++++++++++++++++++++++for current image zoom+++++++++++++++++
                    img_product.setImageBitmap(h.getBitmap());
                    imgdot0.setBackgroundResource(R.drawable.unselectdot);
                    imgdot1.setBackgroundResource(R.drawable.unselectdot);
                    imgdot2.setBackgroundResource(R.drawable.unselectdot);
                    imgdot3.setBackgroundResource(R.drawable.selectdot);
                    imgdot4.setBackgroundResource(R.drawable.unselectdot);
                }
                break;
            case R.id.imgdot4:
//                encodeByte = Base64.decode(arraylist.get(3), Base64.DEFAULT);
//                myBitmap = BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
//                img_product.setImageBitmap(myBitmap);
                if (list.size() > 3) {
                    h = list.get(3);
                    // ++++++++++++++++++++++for current image zoom+++++++++++++++++
                    baos = new ByteArrayOutputStream();
                    h.getBitmap().compress(Bitmap.CompressFormat.PNG, 100, baos);
                    b = baos.toByteArray();
                    Url = b;
                    // ++++++++++++++++++++++for current image zoom+++++++++++++++++
                    img_product.setImageBitmap(h.getBitmap());
                    imgdot0.setBackgroundResource(R.drawable.unselectdot);
                    imgdot1.setBackgroundResource(R.drawable.unselectdot);
                    imgdot2.setBackgroundResource(R.drawable.unselectdot);
                    imgdot3.setBackgroundResource(R.drawable.unselectdot);
                    imgdot4.setBackgroundResource(R.drawable.selectdot);
                }
                break;
            case R.id.convert:
                edtweight.setText("" + Integer.parseInt(edtitems.getText().toString()) * Float.parseFloat(Pweight));
                spspinerweight.setSelection(1);
                break;
            case R.id.convertweight:
//                edtitems.setVisibility(View.GONE);
//                weighttype="gm";
//                spspinerweight.setSelection(1);
                   if (weighttype.equalsIgnoreCase("gm")) {
                    if (edtweight.getText().toString().equalsIgnoreCase("")) {
                        Toast.makeText(getActivity(), "fill weight", Toast.LENGTH_LONG).show();
                    } else {
                        convertbutton = true;
                        DecimalFormat df = new DecimalFormat("#");
                        df.setMaximumFractionDigits(8);
                        Double weight = Double.parseDouble(edtweight.getText().toString());
                        weight = Double.parseDouble(String.format("%.2f", Double.parseDouble(edtweight.getText().toString())));
//                    float itemquantity=weight%Float.parseFloat(Pweight);
//                        Double remender = weight - (Math.floor(weight / Double.parseDouble(Pweight)) * Double.parseDouble(Pweight));

                        BigDecimal remender = BigDecimal.valueOf(weight).remainder(BigDecimal.valueOf(Double.parseDouble(Pweight)));


//                        int quant = (int) (weight / Double.parseDouble(Pweight));
//                        int quant = BigDecimal.valueOf(weight).divide(BigDecimal.valueOf(Double.parseDouble(Pweight))).intValueExact();

                        BigDecimal d = BigDecimal.valueOf(weight).divide(BigDecimal.valueOf(Double.parseDouble(Pweight)), 2, RoundingMode.HALF_UP);
                        int quant = Integer.valueOf(d.intValue());
//                        if (remender > 0) {
//                        if (remender.compareTo(BigDecimal.ZERO) > 0) {
//                            Toast.makeText(getActivity(), "fill correct weight", Toast.LENGTH_LONG).show();
//                        } else {
                            edtitems.setVisibility(View.GONE);
                            Quantity = quant;
                            if (quant == 1) {
                                spspineritems.setSelection(0);
                            } else if (quant == 2) {
                                spspineritems.setSelection(1);
                            } else if (quant == 3) {
                                spspineritems.setSelection(2);
                            } else if (quant == 4) {
                                spspineritems.setSelection(3);
                            } else {
                                spspineritems.setSelection(4);
                                edtitems.setText("" + quant);
                                edtitems.setVisibility(View.VISIBLE);

                            }

//                        }
                    }

                } else if (weighttype.equalsIgnoreCase("kg")) {
                    if (edtweight.getText().toString().equalsIgnoreCase("")) {
                        Toast.makeText(getActivity(), "fill weight", Toast.LENGTH_LONG).show();
                    } else {
                        convertbutton = true;

                        DecimalFormat df = new DecimalFormat("#");
                        df.setMaximumFractionDigits(8);

                        Double weight = Double.parseDouble(edtweight.getText().toString());
                        weight = Double.parseDouble(String.format("%.6f", Double.parseDouble(edtweight.getText().toString()) * 1000));
//                    float itemquantity=weight%Float.parseFloat(Pweight);
//                        Double remender = weight - (Math.floor(weight / Double.parseDouble(Pweight)) * Double.parseDouble(Pweight));
                        BigDecimal remender = BigDecimal.valueOf(weight).remainder(BigDecimal.valueOf(Double.parseDouble(Pweight)));

//                        int quant = (int) (weight / Double.parseDouble(Pweight));
//                        int quant = BigDecimal.valueOf(weight).divide(BigDecimal.valueOf(Double.parseDouble(Pweight))).intValueExact();
                        BigDecimal d = BigDecimal.valueOf(weight).divide(BigDecimal.valueOf(Double.parseDouble(Pweight)), 2, RoundingMode.HALF_UP);
                        int quant = Integer.valueOf(d.intValue());
//
//                        if (remender > 0) {
//                        if (remender.compareTo(BigDecimal.ZERO) > 0) {
//                            Toast.makeText(getActivity(), "fill correct weight", Toast.LENGTH_LONG).show();
//                        } else {
                            edtitems.setVisibility(View.GONE);
                            Quantity = quant;
                            if (quant == 1) {
                                spspineritems.setSelection(0);
                            } else if (quant == 2) {
                                spspineritems.setSelection(1);
                            } else if (quant == 3) {
                                spspineritems.setSelection(2);
                            } else if (quant == 4) {
                                spspineritems.setSelection(3);
                            } else {
                                spspineritems.setSelection(4);
                                edtitems.setText("" + quant);
                                edtitems.setVisibility(View.VISIBLE);

                            }

//                        }
                    }
                } else {
                    Toast.makeText(getActivity(), "Please select weight type", Toast.LENGTH_LONG).show();
                }
//                else {
//                    if (edtweight.getText().toString().equalsIgnoreCase("")) {
//                        Toast.makeText(getActivity(), "fill weight", Toast.LENGTH_LONG).show();
//                    } else {
//                        convertbutton = true;
//                        Double weight = Double.parseDouble(edtweight.getText().toString());
//                        weight = weight * 1000;
////                    float itemquantity=weight%Float.parseFloat(Pweight);
//                        Double itemquantity = (weight - (Math.floor(weight / Double.parseDouble(Pweight)) * Double.parseDouble(Pweight)));
//
//                        int quant = (int) (weight / Double.parseDouble(Pweight));
//
//
//                        if (itemquantity > 0) {
//                            Toast.makeText(getActivity(), "fill correct weight", Toast.LENGTH_LONG).show();
//                        } else {
//                            Quantity = quant;
//                            if (quant == 1) {
//                                spspineritems.setSelection(0);
//                            } else if (quant == 2) {
//                                spspineritems.setSelection(1);
//                            } else if (quant == 3) {
//                                spspineritems.setSelection(2);
//                            } else if (quant == 4) {
//                                spspineritems.setSelection(3);
//                            } else {
//                                spspineritems.setSelection(4);
//                                edtitems.setText("" + quant);
//                                edtitems.setVisibility(View.VISIBLE);
//                            }
//                        }
//                    }
////                    float weight = Float.parseFloat(edtweight.getText().toString());
////                    float itemquantity = weight % (Float.parseFloat(Pweight) / 1000);
////                    int quant = (int) (weight / Float.parseFloat(Pweight));
//
//
//                }


//                edtitems.setText("" + Integer.parseInt(edtitems.getText().toString()) * Float.parseFloat(Pweight));
//                spspinerweight.setSelection(0);
                break;
            case R.id.img_zoom:
                Intent in = new Intent(getActivity(), ImageZoom.class);
//                in.putExtra("imageurl", Pimage);
                in.putExtra("imageurl", Url);
                startActivity(in);
                break;
            case R.id.imgtakepicture:
                Intent inn = new Intent(getActivity(), DragZoomActivity.class);
//                inn.putExtra("imageurl", Pimage);
                inn.putExtra("imageurl", Url);
                startActivity(inn);
                break;
            case R.id.imgaddtocart:
                sharedpreferences = getActivity().getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
                LoginId = sharedpreferences.getInt("LoginId", 0);
                Double weight;
                BigDecimal remender;
                DecimalFormat df = new DecimalFormat("#");
                df.setMaximumFractionDigits(8);
                if (weighttype.equalsIgnoreCase("gm")) {
//                    weight = Double.parseDouble(edtweight.getText().toString());
//                    remender = weight - (Math.floor(weight / Double.parseDouble(Pweight)) * Double.parseDouble(Pweight));
                    weight = Double.parseDouble(String.format("%.2f", Double.parseDouble(edtweight.getText().toString())));
//                    float itemquantity=weight%Float.parseFloat(Pweight);
//                        Double remender = weight - (Math.floor(weight / Double.parseDouble(Pweight)) * Double.parseDouble(Pweight));
                    remender = BigDecimal.valueOf(weight).remainder(BigDecimal.valueOf(Double.parseDouble(Pweight)));

//                        int quant = (int) (weight / Double.parseDouble(Pweight));
                } else {
//                    weight = Double.parseDouble(df.format(Double.parseDouble(edtweight.getText().toString()) * 1000));
//                    remender = weight - (Math.floor(weight / Double.parseDouble(Pweight)) * Double.parseDouble(Pweight));
                    weight = Double.parseDouble(String.format("%.6f", Double.parseDouble(edtweight.getText().toString()) * 1000));
//                    float itemquantity=weight%Float.parseFloat(Pweight);
//                        Double remender = weight - (Math.floor(weight / Double.parseDouble(Pweight)) * Double.parseDouble(Pweight));
                    remender = BigDecimal.valueOf(weight).remainder(BigDecimal.valueOf(Double.parseDouble(Pweight)));

//                        int quant = (int) (weight / Double.parseDouble(Pweight));
                }
//                if (remender > 0) {
//                if (remender.compareTo(BigDecimal.ZERO) > 0) {
//                    Toast.makeText(getActivity(), "fill correct weight", Toast.LENGTH_LONG).show();
//                } else {
//                    Quantity = (int) (weight / Float.parseFloat(Pweight));
//                    Quantity = BigDecimal.valueOf(weight).divide(BigDecimal.valueOf(Double.parseDouble(Pweight))).intValueExact();
                    BigDecimal d = BigDecimal.valueOf(weight).divide(BigDecimal.valueOf(Double.parseDouble(Pweight)), 2, RoundingMode.HALF_UP);
                    Quantity = Integer.valueOf(d.intValue());
                    SQuantity = Integer.toString(Quantity);
                    Weight = "" + Quantity * Double.parseDouble(Pweight);
                    new AddToCartProgress().execute();
//                }
                break;
            case R.id.lltop:
                break;
            case R.id.btnback:
                FragmentManager manager = getActivity().getSupportFragmentManager();
                manager.popBackStack();
                break;
        }
    }

    public class AddToCartProgress extends AsyncTask<String, String, String> {


        private ProgressDialog pdia;

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
            pdia = new ProgressDialog(getActivity());
            pdia.setMessage("Loading...");
            pdia.show();
            pdia.setCancelable(false);
        }

        @Override
        protected String doInBackground(String... params) {
            // TODO Auto-generated method stub


            JSONParser jParser = new JSONParser();

            JSONObject json = new JSONObject();
            json = jParser.getJSONFromUrl(getResources().getString(R.string.url) + "/api/addtocart.php?user_id=" + LoginId + "&product_id=" + URLEncoder.encode(Pid) + "&qunt=" + URLEncoder.encode(SQuantity) + "&device_id=" + URLEncoder.encode(SplashPage.DeviceId) + "&wght=" + URLEncoder.encode(Weight));

            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            // TODO Auto-generated method stub
            super.onPostExecute(result);
            pdia.dismiss();
            pdia = null;
            Toast.makeText(getActivity(), "Product added to cart", Toast.LENGTH_LONG).show();
        }
    }

    public class Progress extends AsyncTask<String, String, String> {
        private ProgressDialog pdia;

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
            pdia = new ProgressDialog(getActivity());
            pdia.setMessage("Loading...");
            pdia.show();
            pdia.setCancelable(false);
        }

        @Override
        protected String doInBackground(String... params) {
            JSONParser jParser = new JSONParser();
            JSONObject json = jParser.getJSONFromUrl(getResources().getString(R.string.url) + "/api/images.php?pid=" + Pid);
            try {

                imagelist = json.getJSONArray("images");
                for (int i = 0; i < imagelist.length(); i++) {
                    JSONObject c = imagelist.getJSONObject(i);
                    InputStream input = null;
                    Image = c.getString("img");
                    Holder h = new Holder();
                    if (Image.equalsIgnoreCase("")) {

                    } else {

                        URL url = new URL(Image);
                        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                        connection.setDoInput(true);
                        connection.setInstanceFollowRedirects(false);
                        connection.setRequestMethod("GET");
                        connection.connect();
                        input = connection.getInputStream();

                        BitmapFactory.Options opts = new BitmapFactory.Options();
                        // opts.inJustDecodeBounds = true;
                        opts.inSampleSize = 2;
                        Bitmap myBitmap = BitmapFactory.decodeStream(input, null, opts);
                        //						Bitmap scaled = Bitmap.createScaledBitmap(myBitmap, 200, 200, true);
                        //myBitmap.recycle();

                        //						Bitmap myBitmap = BitmapFactory.decodeStream(input);
                        h.setBitmap(myBitmap);
                        connection.disconnect();
                    }
                    h.setImage(Image);
                    list.add(h);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            } catch (IOException e) {

            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            pdia.dismiss();
            pdia = null;

        }
    }
}