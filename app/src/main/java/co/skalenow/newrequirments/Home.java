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
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import co.skalenow.Holder;
import co.skalenow.JSONParser;
import co.skalenow.MainActivity;
import co.skalenow.R;
import co.skalenow.dragwithzoom.DragZoomActivity;

/**
 * Created by ADMIN on 30-Mar-17.
 */

public class Home extends Fragment implements View.OnClickListener {
    Spinner SortBy;
    ArrayList<Holder> list = new ArrayList<Holder>();
    ArrayList<Holder> subarraylist = new ArrayList<Holder>();
    GridView gridview;
    JSONArray productslist = null, Totalproduct = null, cartinfo = null;
    String ProductId, Name, Image, Pcode, PWeight, totalproduct, Pdescription;
    int p_length, totalproductlength;
    TextView emptylist, txttotalproduct, txtcurrentpage;
    String images;
    ImageView btnfirst, btnprev, btnnext, btnlast, btngo;
    Fragment fragment;
    FragmentTransaction ft;
    public static String Product_Id;
    SharedPreferences sharedpreferences;
    public static final String MyPREFERENCES = "MyPrefs";
    int LoginId;
    int currentpage = 1, totalpage, remainingItems, currentimage = 0;
    RadioGroup radiogroup;
    String Sortingby = "", Page = "1";
    EditText edtpage;

    public static String str_subcategories;
    Bitmap myBitmap;
    String responce_key;
    String currentImageUrl = "";
    public static String Product_Image;
    LinearLayout linearlayouttop;
    private ArrayList<ProductObject> itemsList;
    private ProgressDialog pDialog;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        View view = inflater.inflate(R.layout.home, container, false);
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        SortBy = (Spinner) view.findViewById(R.id.spReleaseDate);
        gridview = (GridView) view.findViewById(R.id.gridview);
        emptylist = (TextView) view.findViewById(R.id.emptylist);
        btnfirst = (ImageView) view.findViewById(R.id.btnfirst);
        btnprev = (ImageView) view.findViewById(R.id.btnprev);
        btnnext = (ImageView) view.findViewById(R.id.btnnext);
        btnlast = (ImageView) view.findViewById(R.id.btnlast);
        btngo = (ImageView) view.findViewById(R.id.btngo);
        txttotalproduct = (TextView) view.findViewById(R.id.txttotalproduct);
        txtcurrentpage = (TextView) view.findViewById(R.id.txtcurrentpage);
        radiogroup = (RadioGroup) view.findViewById(R.id.radiogroup);
        edtpage = (EditText) view.findViewById(R.id.edtpage);
        linearlayouttop = (LinearLayout) view.findViewById(R.id.linearlayouttop);
        btnfirst.setOnClickListener(this);
        btnprev.setOnClickListener(this);
        btnnext.setOnClickListener(this);
        btnlast.setOnClickListener(this);
        btngo.setOnClickListener(this);
        sharedpreferences = getActivity().getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
//        SharedPreferences.Editor editor = sharedpreferences.edit();
//        editor.putString("page", "Home");
//        editor.commit();
        // Create an ArrayAdapter using the string array and a default spinner
        pDialog = new ProgressDialog(getActivity());
        pDialog.setMessage("Loading...");
        pDialog.setCancelable(false);
        itemsList = new ArrayList<ProductObject>();
        ArrayAdapter<CharSequence> staticAdapter = ArrayAdapter
                .createFromResource(getActivity(), R.array.sortby,
                        android.R.layout.simple_spinner_item);

        // Specify the layout to use when the list of choices appears
        staticAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // Apply the adapter to the spinner
        SortBy.setAdapter(staticAdapter);
        SortBy.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
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
//        btnviewcart.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                fragment = new ViewCart();
//                ft = getActivity().getSupportFragmentManager().beginTransaction();
//                ft.add(R.id.frame, fragment);
//                ft.addToBackStack("add" + MainActivity.add);
//                ft.commit();
//                MainActivity.add++;
//            }
//        });
        radiogroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.radioasc) {
                    if (FindProduct.url.equalsIgnoreCase("")) {
                        Sortingby = "?sortby=date&sort=asc";
                        Page = "&page=" + currentpage;
                    } else {
                        Page = "&page=1";
                        Sortingby = FindProduct.url + "&sortby=date&sort=asc" + Page;
                    }
                    currentpage = 1;
                    btnfirst.setBackgroundResource(R.drawable.firsth);
                    btnprev.setBackgroundResource(R.drawable.prev);
                    btnnext.setBackgroundResource(R.drawable.next);
                    btnlast.setBackgroundResource(R.drawable.last);
//                    new ProductProgressBar().execute();
                    getItems();
                } else if (checkedId == R.id.radiodesc) {
                    if (FindProduct.url.equalsIgnoreCase("")) {
                        Sortingby = "?sortby=date&sort=desc";
                        Page = "&page=1";
                    } else {
                        Page = "&page=" + currentpage;
                        Sortingby = FindProduct.url + "&sortby=date&sort=desc" + Page;
                    }
                    btnfirst.setBackgroundResource(R.drawable.firsth);
                    btnprev.setBackgroundResource(R.drawable.prev);
                    btnnext.setBackgroundResource(R.drawable.next);
                    btnlast.setBackgroundResource(R.drawable.last);
                    currentpage = 1;
//                    new ProductProgressBar().execute();
                    getItems();
                }

            }
        });
        if (FindProduct.url.equalsIgnoreCase("")) {
            Sortingby = "?sortby=date&sort=asc";
            Page = "&page=" + currentpage;
        } else {
            Page = "&page=" + currentpage;
            Sortingby = FindProduct.url + "&sortby=date&sort=asc" + Page;
        }
        btnfirst.setBackgroundResource(R.drawable.firsth);
        btnprev.setBackgroundResource(R.drawable.prev);
        btnnext.setBackgroundResource(R.drawable.next);
        btnlast.setBackgroundResource(R.drawable.last);
//        new ProductProgressBar().execute();
        getItems();
        return view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnfirst:
                currentpage = 1;
//                completeUrl = SearchP.url + "&page=" + currentpage;?
                Page = "&page=" + currentpage;
                btnfirst.setBackgroundResource(R.drawable.firsth);
                btnprev.setBackgroundResource(R.drawable.prev);
                btnnext.setBackgroundResource(R.drawable.next);
                btnlast.setBackgroundResource(R.drawable.last);
//                new ProductProgressBar().execute();
                getItems();
                break;
            case R.id.btnprev:
                currentpage = currentpage - 1;
//                completeUrl = SearchP.url + "&page=" + currentpage;
                btnprev.setBackgroundResource(R.drawable.prevh);
                btnfirst.setBackgroundResource(R.drawable.first);
                btnnext.setBackgroundResource(R.drawable.next);
                btnlast.setBackgroundResource(R.drawable.last);
                Page = "&page=" + currentpage;
//                new ProductProgressBar().execute();
                getItems();
                break;
            case R.id.btnnext:
                if (currentpage == totalpage) {
                    Toast.makeText(getActivity(), "It's last Page", Toast.LENGTH_LONG).show();
                } else {
                    currentpage = currentpage + 1;
//                    completeUrl = SearchP.url + "&page=" + currentpage;
                    btnnext.setBackgroundResource(R.drawable.nexth);
                    btnfirst.setBackgroundResource(R.drawable.first);
                    btnprev.setBackgroundResource(R.drawable.prev);
                    btnlast.setBackgroundResource(R.drawable.last);
                    Page = "&page=" + currentpage;
//                    new ProductProgressBar().execute();
                    getItems();
                }
                break;
            case R.id.btnlast:
                if (currentpage == totalpage) {
                    Toast.makeText(getActivity(), "It's last Page", Toast.LENGTH_LONG).show();
                } else {
                    currentpage = totalpage;
//                    completeUrl = SearchP.url + "&page=" + currentpage;
                    btnlast.setBackgroundResource(R.drawable.lasth);
                    btnfirst.setBackgroundResource(R.drawable.first);
                    btnprev.setBackgroundResource(R.drawable.prev);
                    btnnext.setBackgroundResource(R.drawable.next);

                    Page = "&page=" + currentpage;
//                    new ProductProgressBar().execute();
                    getItems();
                }

                break;
            case R.id.btngo:
                if (edtpage.getText().toString().equalsIgnoreCase("")) {
                    Toast.makeText(getActivity(), "Please fill page number", Toast.LENGTH_LONG).show();
//                    currentpage = Integer.parseInt(edtpage.getText().toString());
                } else {

                    if (Integer.parseInt(edtpage.getText().toString()) <= totalpage) {
                        currentpage = Integer.parseInt(edtpage.getText().toString());
                        if (currentpage == 1) {
                            btnlast.setBackgroundResource(R.drawable.last);
                            btnfirst.setBackgroundResource(R.drawable.firsth);
                            btnprev.setBackgroundResource(R.drawable.prev);
                            btnnext.setBackgroundResource(R.drawable.next);
                        } else if (currentpage == totalpage) {
                            btnlast.setBackgroundResource(R.drawable.lasth);
                            btnfirst.setBackgroundResource(R.drawable.first);
                            btnprev.setBackgroundResource(R.drawable.prev);
                            btnnext.setBackgroundResource(R.drawable.next);
                        }
//                        completeUrl = SearchP.url + "&page=" + currentpage;
                        Page = "&page=" + currentpage;
//                        new ProductProgressBar().execute();
                        getItems();
                    } else {
                        Toast.makeText(getActivity(), "Page not found", Toast.LENGTH_LONG).show();
                    }
//                    completeUrl = SearchP.url + "&page=1";
                }

//                new ProductProgressBar().execute();
                break;
        }
    }

    public void getItems() {
        showpDialog();
        itemsList.clear();
        CustomRequest jsonReq = new CustomRequest(Request.Method.POST, getResources().getString(R.string.url) + "/api/searchproducts.php" + Sortingby + Page, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            Iterator<String> iter = response.keys();
                            while (iter.hasNext()) {
                                responce_key = iter.next();
                                if (responce_key.equalsIgnoreCase("products")) {
                                    break;
                                }

                            }
                            if (responce_key.equalsIgnoreCase("products")) {
                                productslist = response.getJSONArray("products");
                                Totalproduct = response.getJSONArray("totalproducts");
                                cartinfo = response.getJSONArray("cartinfo");

                                totalproductlength = Totalproduct.length();
                                for (int i = 0; i < totalproductlength; i++) {
                                    JSONObject ob = Totalproduct.getJSONObject(i);
                                    totalproduct = ob.getString("total");
                                }


                                int arrayLength = productslist.length();
                                p_length = productslist.length();
                                if (arrayLength > 0) {

                                    for (int i = 0; i < productslist.length(); i++) {

                                        JSONObject questionObj = (JSONObject) productslist.get(i);

                                        ProductObject question = new ProductObject(questionObj);

                                        itemsList.add(question);
                                    }
                                }
                            }
                        } catch (JSONException e) {

                            e.printStackTrace();

                        } finally {
                            loadingComplete();
                            hidepDialog();
//                            Toast.makeText(getApplicationContext(), response.toString(), Toast.LENGTH_SHORT).show();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
//                Toast.makeText(getActivity(), "hgjhk", Toast.LENGTH_SHORT).show();
                hidepDialog();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                sharedpreferences = getActivity().getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
                Map<String, String> params = new HashMap<String, String>();
//                params.put("Sortingby", Long.toString(1));
                return params;
            }
        };

        App.getInstance().addToRequestQueue(jsonReq);
    }

    public class ProductProgressBar extends AsyncTask<String, String, String> {

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
            list.clear();
            JSONParser jParser = new JSONParser();
            JSONObject json = jParser.getJSONFromUrl(getResources().getString(R.string.url) + "/api/searchproducts.php" + Sortingby + Page);
            try {
                //http://www.puyangan.com/api/category.php?cid=178
                // Getting Array of Employee
                Iterator<String> iter = json.keys();
                while (iter.hasNext()) {
                    responce_key = iter.next();
                    if (responce_key.equalsIgnoreCase("products")) {
                        break;
                    }

                }
                if (responce_key.equalsIgnoreCase("products")) {


                    productslist = json.getJSONArray("products");
                    Totalproduct = json.getJSONArray("totalproducts");
                    cartinfo = json.getJSONArray("cartinfo");

                    totalproductlength = Totalproduct.length();
                    for (int i = 0; i < totalproductlength; i++) {
                        JSONObject ob = Totalproduct.getJSONObject(i);
                        totalproduct = ob.getString("total");
                    }
                    p_length = productslist.length();
                    // looping of List
                    for (int i = 0; i < p_length; i++) {
                        JSONObject c = productslist.getJSONObject(i);
                        //					Bitmap myBitmap = null;

                        InputStream input = null;
                        ProductId = c.getString("id");
                        Name = c.getString("name");
                        Image = c.getString("image");
                        Pcode = c.getString("pcode");
                        PWeight = c.getString("weight");
                        Pdescription = c.getString("dscptn");
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
                            opts.inSampleSize = 4;
                            Bitmap myBitmap = BitmapFactory.decodeStream(input, null, opts);
                            //						Bitmap scaled = Bitmap.createScaledBitmap(myBitmap, 200, 200, true);
                            //myBitmap.recycle();

                            //						Bitmap myBitmap = BitmapFactory.decodeStream(input);
                            h.setBitmap(myBitmap);
                            connection.disconnect();
                        }


                        h.setId(ProductId);
                        h.setName(Name);
                        h.setPcode(Pcode);
                        h.setWeight(PWeight);
                        h.setImage(Image);
                        h.setDescription(Pdescription);
                        list.add(h);
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            } catch (IOException e) {

            }
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            // TODO Auto-generated method stub
            super.onPostExecute(result);
            pdia.dismiss();
            pdia = null;
            if (totalproduct != null) {
                totalpage = Integer.parseInt(totalproduct) / 10;
                remainingItems = Integer.parseInt(totalproduct) % 10;
                if (remainingItems > 0) {
                    totalpage = totalpage + 1;
                }
                edtpage.setText("" + currentpage);
                txttotalproduct.setText("Products:" + totalproduct);
                txtcurrentpage.setText(currentpage + " of " + totalpage);
                //			ExpandableListAdapter	listAdapter = new com.puyangan.ExpandableListAdapter(getActivity(), listDataHeader, listDataChild);

                // setting list adapter
                //			expListView.setAdapter(listAdapter);
//                gridview.setAdapter(new MyCustomAdapter(getActivity(), list));
                //			ListUtils.setDynamicHeight(gridview);
                if (p_length == 0) {
                    emptylist.setVisibility(View.VISIBLE);
                    gridview.setVisibility(View.GONE);
                    linearlayouttop.setVisibility(View.GONE);
                } else {
                    emptylist.setVisibility(View.GONE);
                    gridview.setVisibility(View.VISIBLE);
                    linearlayouttop.setVisibility(View.VISIBLE);
                }
                if (totalpage == 1) {
                    btnprev.setClickable(false);
                    btnnext.setClickable(false);
                    btnlast.setClickable(false);
                } else if (totalpage == 2) {
                    if (currentpage == 1) {
                        btnprev.setClickable(false);
                        btnnext.setClickable(true);
                        btnlast.setClickable(true);
                        btnfirst.setClickable(true);
                    } else {
                        btnprev.setClickable(true);
                        btnnext.setClickable(false);
                        btnlast.setClickable(false);
                        btnfirst.setClickable(true);
                    }
                } else if (currentpage > 2) {
                    if (currentpage == 1) {
                        btnprev.setClickable(false);
                        btnnext.setClickable(true);
                        btnlast.setClickable(true);
                        btnfirst.setClickable(true);
                    } else {
                        btnprev.setClickable(true);
                        btnnext.setClickable(true);
                        btnlast.setClickable(true);
                        btnfirst.setClickable(true);
                    }
                }
            } else {
                if (p_length == 0) {
                    emptylist.setVisibility(View.VISIBLE);
                    gridview.setVisibility(View.GONE);
                    linearlayouttop.setVisibility(View.GONE);
                } else {
                    emptylist.setVisibility(View.GONE);
                    gridview.setVisibility(View.VISIBLE);
                    linearlayouttop.setVisibility(View.VISIBLE);
                }
            }
        }
    }

    public void loadingComplete() {

        if (totalproduct != null) {
            totalpage = Integer.parseInt(totalproduct) / 10;
            remainingItems = Integer.parseInt(totalproduct) % 10;
            if (remainingItems > 0) {
                totalpage = totalpage + 1;
            }
            edtpage.setText("" + currentpage);
            txttotalproduct.setText("Products:" + totalproduct);
            txtcurrentpage.setText(currentpage + " of " + totalpage);
            //			ExpandableListAdapter	listAdapter = new com.puyangan.ExpandableListAdapter(getActivity(), listDataHeader, listDataChild);

            // setting list adapter
            //			expListView.setAdapter(listAdapter);
//                gridview.setAdapter(new MyCustomAdapter(getActivity(), list));
            //			ListUtils.setDynamicHeight(gridview);
            gridview.setAdapter(new MyCustomAdapter(getActivity(), itemsList));
            if (p_length == 0) {
                emptylist.setVisibility(View.VISIBLE);
                gridview.setVisibility(View.GONE);
                linearlayouttop.setVisibility(View.GONE);
            } else {
                emptylist.setVisibility(View.GONE);
                gridview.setVisibility(View.VISIBLE);
                linearlayouttop.setVisibility(View.VISIBLE);
            }
            if (totalpage == 1) {
                btnprev.setClickable(false);
                btnnext.setClickable(false);
                btnlast.setClickable(false);
            } else if (totalpage == 2) {
                if (currentpage == 1) {
                    btnprev.setClickable(false);
                    btnnext.setClickable(true);
                    btnlast.setClickable(true);
                    btnfirst.setClickable(true);
                } else {
                    btnprev.setClickable(true);
                    btnnext.setClickable(false);
                    btnlast.setClickable(false);
                    btnfirst.setClickable(true);
                }
            } else if (currentpage > 2) {
                if (currentpage == 1) {
                    btnprev.setClickable(false);
                    btnnext.setClickable(true);
                    btnlast.setClickable(true);
                    btnfirst.setClickable(true);
                } else {
                    btnprev.setClickable(true);
                    btnnext.setClickable(true);
                    btnlast.setClickable(true);
                    btnfirst.setClickable(true);
                }
            }
        } else {
            if (p_length == 0) {
                emptylist.setVisibility(View.VISIBLE);
                gridview.setVisibility(View.GONE);
                linearlayouttop.setVisibility(View.GONE);
            } else {
                emptylist.setVisibility(View.GONE);
                gridview.setVisibility(View.VISIBLE);
                linearlayouttop.setVisibility(View.VISIBLE);
            }
        }
    }

    class MyCustomAdapter extends BaseAdapter {

        LayoutInflater inflater;
        ArrayList<ProductObject> list;

        public MyCustomAdapter(FragmentActivity fragmentActivity, ArrayList<ProductObject> list) {
            inflater = LayoutInflater.from(fragmentActivity);
            this.list = list;
        }

        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public Object getItem(int paramInt) {
            return paramInt;
        }


        class ViewHolder {
            TextView txt_product, txt_p_gram;
            ImageView imgzoom, img_product, imgtakepicture, imgmore;
        }

        @Override
        public long getItemId(int paramInt) {
            return paramInt;
        }

        @Override
        public View getView(final int paramInt, View paramView, ViewGroup paramViewGroup) {

            final ViewHolder holder;
            if (paramView == null) {
                paramView = inflater.inflate(R.layout.homelistitems, paramViewGroup, false);
                holder = new ViewHolder();

                holder.txt_product = (TextView) paramView.findViewById(R.id.txt_product);
                holder.txt_p_gram = (TextView) paramView.findViewById(R.id.txt_p_gram);
                holder.img_product = (ImageView) paramView.findViewById(R.id.img_product);
                holder.imgzoom = (ImageView) paramView.findViewById(R.id.img_zoom);
                holder.imgtakepicture = (ImageView) paramView.findViewById(R.id.imgtakepicture);
                holder.imgmore = (ImageView) paramView.findViewById(R.id.imgmore);
                paramView.setTag(holder);
            } else {
                holder = (MyCustomAdapter.ViewHolder) paramView.getTag();
            }

//            Holder h = list.get(paramInt);
            final ProductObject h = list.get(paramInt);
            String name = h.getProduct_name();
            String pcode = h.getProduct_code();
            holder.txt_product.setText(name + " (" + pcode + ")");
            holder.txt_p_gram.setText("(" + h.getProduct_weight() + " gms)");
            myBitmap = h.getBitmap();
//            Image = h.getProduct_image();
            if (h.getProduct_image().equalsIgnoreCase("")) {
                holder.img_product.setBackgroundResource(R.drawable.header);
            } else {
                if (myBitmap != null) {
                    holder.img_product.setImageBitmap(myBitmap);
                } else {
                    Picasso.with(getActivity())
                            .load(h.getProduct_image()) // thumbnail url goes here
//                        .placeholder(R.drawable.launchicon)
                            .resize(100, 100)
//                        .transform(blurTransformation)
                            .into(holder.img_product, new Callback() {
                                @Override
                                public void onSuccess() {
                                    Picasso.with(getActivity())
                                            .load(h.getProduct_image()) // image url goes here
                                            .resize(100, 100)
                                            .placeholder(holder.img_product.getDrawable())
                                            .into(holder.img_product);
                                }

                                @Override
                                public void onError() {
                                }
                            });
                }
            }
            holder.imgzoom.setTag(paramInt);
            holder.imgzoom.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos1 = (Integer) v.getTag();
//                    Holder h1 = (Holder) list.get(pos1);
                    ProductObject h1 = list.get(pos1);
                    if (h1.getBitmap() != null) {
                        Intent in = new Intent(getActivity(), ImageZoom.class);
                        ByteArrayOutputStream baos = new ByteArrayOutputStream();
                        h1.getBitmap().compress(Bitmap.CompressFormat.PNG, 100, baos);
                        byte[] b = baos.toByteArray();
//                    String temp = Base64.encodeToString(b, Base64.DEFAULT);
//                    in.putExtra("imageurl", temp);
//                    in.putExtra("imageurl", h1.getImage());
                        in.putExtra("imageurl", b);
                        startActivity(in);
                    }
                }
            });
            holder.imgtakepicture.setTag(paramInt);
            holder.imgtakepicture.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos1 = (Integer) v.getTag();
//                    Holder h1 = (Holder) list.get(pos1);
                    ProductObject h1 = list.get(pos1);
                    if (h1.getBitmap() != null) {
                        Intent in = new Intent(getActivity(), DragZoomActivity.class);

                        ByteArrayOutputStream baos = new ByteArrayOutputStream();
                        h1.getBitmap().compress(Bitmap.CompressFormat.PNG, 100, baos);
                        byte[] b = baos.toByteArray();
//                    String temp = Base64.encodeToString(b, Base64.DEFAULT);
//                    Bundle args = new Bundle();
//                    args.putString("imageurl", temp);
//                    in.putExtras(args);
//                    in.putExtra("imageurl", temp);
                        in.putExtra("imageurl", b);
                        startActivity(in);
                    }
                }
            });
            holder.imgmore.setTag(paramInt);
            holder.imgmore.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos1 = (Integer) v.getTag();
//                    Holder h1 = (Holder) list.get(pos1);
                    ProductObject h1 = list.get(pos1);
                    if (h1.getBitmap() != null) {
                        fragment = new HomeMore();
                        Bundle args = new Bundle();
                        args.putString("pname", h1.getProduct_name());
                        args.putString("pcode", h1.getProduct_code());
                        args.putString("pweight", h1.getProduct_weight());
                        args.putString("pimage", h1.getProduct_image());
                        args.putString("pid", h1.getProduct_id());
                        args.putString("description", h1.getProduct_description());
                        ByteArrayOutputStream baos = new ByteArrayOutputStream();
                        h1.getBitmap().compress(Bitmap.CompressFormat.PNG, 100, baos);
                        byte[] b = baos.toByteArray();
//                    String temp = Base64.encodeToString(b, Base64.DEFAULT);
//                    args.putString("Pimage", temp);
                        args.putByteArray("imageurl", b);
                        fragment.setArguments(args);
                        ft = getActivity().getSupportFragmentManager().beginTransaction();
                        ft.add(R.id.frame, fragment);
                        ft.addToBackStack("add" + MainActivity.add);
                        ft.commit();
                        MainActivity.add++;
                    }

                }
            });

            holder.img_product.setTag(paramInt);
            holder.img_product.setOnClickListener(new View.OnClickListener() {

                @SuppressWarnings("deprecation")
                @Override
                public void onClick(View vv) {
                    // TODO Auto-generated method stub

                    int pos1 = (Integer) vv.getTag();
//                    Holder h1 = (Holder) list.get(pos1);
                    ProductObject h1 = list.get(pos1);
                    if (h1.getBitmap() != null) {
                        fragment = new HomeMore();
                        Bundle args = new Bundle();
                        args.putString("pname", h1.getProduct_name());
                        args.putString("pcode", h1.getProduct_code());
                        args.putString("pweight", h1.getProduct_weight());
                        args.putString("pimage", h1.getProduct_image());
                        args.putString("pid", h1.getProduct_id());
                        args.putString("description", h1.getProduct_description());
                        ByteArrayOutputStream baos = new ByteArrayOutputStream();
                        h1.getBitmap().compress(Bitmap.CompressFormat.PNG, 100, baos);
                        byte[] b = baos.toByteArray();
//                    String temp = Base64.encodeToString(b, Base64.DEFAULT);
//                    args.putString("Pimage", temp);
                        args.putByteArray("imageurl", b);
                        fragment.setArguments(args);
                        ft = getActivity().getSupportFragmentManager().beginTransaction();
                        ft.add(R.id.frame, fragment);
                        ft.addToBackStack("add" + MainActivity.add);
                        ft.commit();
                        MainActivity.add++;
                    }
                }
            });
            return paramView;
        }
    }

    protected void showpDialog() {

        if (!pDialog.isShowing())
            pDialog.show();
    }

    protected void hidepDialog() {

        if (pDialog.isShowing())
            pDialog.dismiss();
    }
}