package co.skalenow.newrequirments;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import co.skalenow.Holder;
import co.skalenow.JSONParser;
import co.skalenow.MainActivity;
import co.skalenow.R;
import co.skalenow.SplashPage;

/**
 * Created by ADMIN on 24-Mar-17.
 */

public class ViewCart extends Fragment implements View.OnClickListener {
    SharedPreferences sharedpreferences;

    public static final String MyPREFERENCES = "MyPrefs";
    ListView cartlist;
    JSONArray productcart = null;
    ArrayList<Holder> arraylist = new ArrayList<Holder>();
    String Product_id, Product_name, Product_price, Product_quantity, Product_image, Weight;
    Bitmap myBitmap, bitmap;
    int length, LoginId, Order_Count;
    String Remarks = "";
    Double Product_totalprice, Totalprice = 0.00;
    TextView txttotalprice;
    TextView btnupdate, btnnext;
    ArrayList<String> UrlarrayforUpdate = new ArrayList<String>();
    String productquantity, productid;
    String UrlarrayupdateString, Url;
    View view;
    Fragment fragment;
    FragmentTransaction ft;
//    Boolean check;
//    String page;
    RelativeLayout idcart, emptytext, rlfirst, rlinnerfooter;
    Button btnshop;
    //	TextView emptytext;
    public static Double Total_Price = 0.0;
    EditText edtremark;
    Button btnremark;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        view = inflater.inflate(R.layout.cart, container, false);
        txttotalprice = (TextView) view.findViewById(R.id.txttotalprice);
        btnupdate = (TextView) view.findViewById(R.id.btnupdatecart);
        btnnext = (TextView) view.findViewById(R.id.btnnext);
        btnshop = (Button) view.findViewById(R.id.btnshop);
        btnupdate.setOnClickListener(this);
        btnnext.setOnClickListener(this);
        cartlist = (ListView) view.findViewById(R.id.cartlist);
        idcart = (RelativeLayout) view.findViewById(R.id.rl1);
        rlfirst = (RelativeLayout) view.findViewById(R.id.rlfirst);
        emptytext = (RelativeLayout) view.findViewById(R.id.txtempty);
        rlinnerfooter = (RelativeLayout) view.findViewById(R.id.rlinnerfooter);
        edtremark = (EditText) view.findViewById(R.id.edtremark);
        btnremark = (Button) view.findViewById(R.id.btnupdateremarks);
        rlfirst.setVisibility(View.GONE);
//		idcart.setVisibility(View.GONE);
        //img=(ImageView)view.findViewById(R.id.banner);
        sharedpreferences = getActivity().getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        LoginId = sharedpreferences.getInt("LoginId", 0);
        Remarks = sharedpreferences.getString("remarks", "");
        edtremark.setText(Remarks);
//        sharedpreferences = getActivity().getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
//        SharedPreferences.Editor editor = sharedpreferences.edit();
//        editor.putBoolean("Signinpage", false);
//        check = sharedpreferences.getBoolean("Signinpage", false);
//        page = sharedpreferences.getString("page", "");

        sharedpreferences = getActivity().getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putString("page", "ViewCart");
        editor.commit();
        MainActivity.mSelectedItem=2;
        MainActivity.mMenuAdapter.notifyDataSetChanged();
        btnshop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fragment = new Home();
                ft = getActivity().getSupportFragmentManager().beginTransaction();
                ft.add(R.id.frame, fragment);
                ft.addToBackStack("add" + MainActivity.add);
                ft.commit();
                MainActivity.add++;
//				MainActivity.navigationView.getMenu().getItem(3).setChecked(true);
            }
        });
        btnremark.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences.Editor editor = sharedpreferences.edit();
                editor.putString("remarks", edtremark.getText().toString());
                editor.commit();
            }
        });
        new OrderProgressBar().execute();
        editor.commit();
        return view;
    }

    public class OrderProgressBar extends AsyncTask<String, String, String> {

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

            arraylist.clear();
            Totalprice = 0.0;
            Product_totalprice = 0.0;
            JSONParser jParser = new JSONParser();
            //http://www.puyangan.com/api/cart.php?user_id=67
            JSONObject json = jParser.getJSONFromUrl(getResources().getString(R.string.url) + "/api/cart.php?user_id=" + LoginId + "&device_id=" + SplashPage.DeviceId);
            try {
                //http://www.puyangan.com/api/category.php?cid=178
                // Getting Array of Employee
                productcart = json.getJSONArray("productcart");
                length = productcart.length();
                Order_Count = length;
                // looping of List
                for (int i = 0; i < length; i++) {
                    JSONObject c = productcart.getJSONObject(i);
                    //					Bitmap myBitmap = null;
                    InputStream input = null;

                    Product_id = c.getString("product_id");
                    Product_name = c.getString("product_name");
                    Product_price = c.getString("product_price");
                    Product_quantity = c.getString("product_quantity");
                    Weight = c.getString("product_weight");
                    Product_image = c.getString("product_image");
                    Holder h = new Holder();
                    if (Product_image.equalsIgnoreCase("")) {

                    } else {

                        URL url = new URL(Product_image);
                        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                        connection.setDoInput(true);
                        connection.setInstanceFollowRedirects(false);
                        connection.setRequestMethod("GET");
                        connection.connect();
                        input = connection.getInputStream();

                        BitmapFactory.Options opts = new BitmapFactory.Options();
                        // opts.inJustDecodeBounds = true;
                        opts.inSampleSize = 4;
                        myBitmap = BitmapFactory.decodeStream(input, null, opts);
                        //						Bitmap scaled = Bitmap.createScaledBitmap(myBitmap, 200, 200, true);
                        //myBitmap.recycle();

                        //						Bitmap myBitmap = BitmapFactory.decodeStream(input);
                        h.setBitmap(myBitmap);
                        connection.disconnect();
                    }
                    h.setProduct_id(Product_id);
                    h.setProduct_name(Product_name);
                    h.setProduct_price(Product_price);
                    h.setProduct_quantity(Product_quantity);
                    h.setPWeight(Weight);
                    h.setProduct_image(Product_image);
                    Product_totalprice = Double.parseDouble(Product_quantity) * Double.parseDouble(Product_price);
                    h.setProduct_total_price(Product_totalprice);
                    arraylist.add(h);

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
            cartlist.setAdapter(new MyCustomAdapter(getActivity(), arraylist));
            ListUtils.setDynamicHeight(cartlist);
            if (length == 0) {
                idcart.setVisibility(View.GONE);
                emptytext.setVisibility(View.VISIBLE);
                rlfirst.setVisibility(View.GONE);
                rlinnerfooter.setVisibility(View.GONE);
            } else {
                idcart.setVisibility(View.VISIBLE);
                emptytext.setVisibility(View.GONE);
                rlfirst.setVisibility(View.VISIBLE);
                rlinnerfooter.setVisibility(View.VISIBLE);
            }

            //			txttotalprice.setText("$"+Total_Price);
            if (Totalprice == 0.0) {
                txttotalprice.setText("Total $" + Totalprice);
                Total_Price = Totalprice;
            } else {
                if (Totalprice.toString().substring(Totalprice.toString().indexOf("."), Totalprice.toString().length()).length() > 2) {
                    txttotalprice.setText("Total $" + Totalprice.toString().substring(0, Totalprice.toString().indexOf(".") + 3));
                    Total_Price = Double.parseDouble(Totalprice.toString().substring(0, Totalprice.toString().indexOf(".") + 3));
                } else {
                    txttotalprice.setText("Total $" + Totalprice.toString().substring(0, Totalprice.toString().indexOf(".") + 2));
                    Total_Price = Double.parseDouble(Totalprice.toString().substring(0, Totalprice.toString().indexOf(".") + 2));
                }


            }
//			idcart.setVisibility(View.VISIBLE);
        }

    }

    public static class ListUtils {
        public static void setDynamicHeight(ListView orderlist) {
            ListAdapter mListAdapter = orderlist.getAdapter();
            if (mListAdapter == null) {
                // when adapter is null
                return;
            }
            int height = 0;
            int desiredWidth = View.MeasureSpec.makeMeasureSpec(orderlist.getWidth(), View.MeasureSpec.UNSPECIFIED);
            for (int i = 0; i < mListAdapter.getCount(); i++) {
                View listItem = mListAdapter.getView(i, null, orderlist);
                listItem.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED);
                height += listItem.getMeasuredHeight();
            }
            ViewGroup.LayoutParams params = orderlist.getLayoutParams();
            params.height = height;//+ (orderlist.getHeight() * (mListAdapter.getCount() - 1));
            orderlist.setLayoutParams(params);
            orderlist.requestLayout();
        }
    }

    class MyCustomAdapter extends BaseAdapter {

        LayoutInflater inflater;
        ArrayList<Holder> list;

        public MyCustomAdapter(FragmentActivity fragmentActivity, ArrayList<Holder> list) {
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
            TextView productname, weight,quan_value;
            ImageView productImage;
            EditText p_remark;
//            Button btnincrease, btndecrease;
            ImageView btndelete;
        }

        @Override
        public long getItemId(int paramInt) {
            return paramInt;
        }

        @Override
        public View getView(final int paramInt, View paramView, ViewGroup paramViewGroup) {

            final MyCustomAdapter.ViewHolder holder;
            if (paramView == null) {
                paramView = inflater.inflate(R.layout.cartitem, paramViewGroup, false);
                holder = new MyCustomAdapter.ViewHolder();

                holder.productname = (TextView) paramView.findViewById(R.id.txtproductname);
//                holder.btnincrease = (Button) paramView.findViewById(R.id.btnincrease);
//                holder.btndecrease = (Button) paramView.findViewById(R.id.btndecrease);
                holder.btndelete = (ImageView) paramView.findViewById(R.id.btndelete);
                holder.quan_value = (TextView) paramView.findViewById(R.id.edtquantity);
                holder.p_remark = (EditText) paramView.findViewById(R.id.p_remark);
                holder.productImage = (ImageView) paramView.findViewById(R.id.img);
                holder.weight = (TextView) paramView.findViewById(R.id.txtweight);
                paramView.setTag(holder);
            } else {
                holder = (MyCustomAdapter.ViewHolder) paramView.getTag();
            }

            final Holder h = list.get(paramInt);
            String pname = h.getProduct_name();
            holder.productname.setText(pname);

            String pprice = h.getProduct_price();

            String pquantity = h.getProduct_quantity();
            holder.quan_value.setText(pquantity);
            holder.weight.setText(h.getPWeight()+" gm");
            Double total = h.getProduct_total_price();

            Totalprice = Totalprice + total;

//            holder.btnincrease.setText(">");
//            holder.btndecrease.setText("<");
            bitmap = h.getBitmap();

            if (h.getProduct_image().equalsIgnoreCase("")) {
                holder.productImage.setBackgroundResource(R.drawable.header);
            } else {
                //				holder.productImage.setImageBitmap(getCircleBitmap(bitmap));
                holder.productImage.setImageBitmap(bitmap);
            }
            holder.quan_value.setTag(paramInt);
            holder.quan_value.addTextChangedListener(new TextWatcher() {
                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    // TODO Auto-generated method stub
                    System.out.println("ONtext changed " + new String(s.toString()));
                }

                @Override
                public void beforeTextChanged(CharSequence s, int start, int count,
                                              int after) {
                    // TODO Auto-generated method stub
                    System.out.println("beforeTextChanged " + new String(s.toString()));
                }

                @Override
                public void afterTextChanged(Editable s) {
                    // TODO Auto-generated method stub
                    System.out.println("afterTextChanged " + new String(s.toString()));
                    h.setProduct_quantity(new String(s.toString()));
                }
            });
            holder.btndelete.setTag(paramInt);
            holder.btndelete.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    // TODO Auto-generated method stub
                    int pos1 = (Integer) v.getTag();
                    Holder h1 = (Holder) list.get(pos1);

                    Product_id = h1.getProduct_id();
                    new DeleteProgress().execute(Product_id);
                }
            });

//            holder.btnincrease.setTag(paramInt);
//            holder.btnincrease.setOnClickListener(new View.OnClickListener() {
//
//                @Override
//                public void onClick(View v) {
//                    // TODO Auto-generated method stub
//
//                    String a = holder.quan_value.getText().toString();
//
//                    holder.quan_value.setText(String.valueOf(Integer.parseInt(a) + 1));
//
//                    h.setProduct_quantity("" + (Integer.parseInt(a) + 1));
//
//                    //					 ViewHolder tagHolder = (ViewHolder) v.getTag();
//                    //
//                    //					    // TODO Auto-generated method stub
//                    //					    Log.i("Edit Button Clicked", "**********");
//                    //					   /* Toast.makeText(context, "Edit button Clicked",
//                    //					      Toast.LENGTH_LONG).show();*/
//                    //
//                    //					    int mValue = Integer.parseInt(tagHolder.textAddress.getText().toString());
//                    //					    mValue--;
//                    //					    if(mValue < 0)
//                    //					    {
//                    //					        System.out.println("not valid");
//                    //					    }
//                    //					    else
//                    //					    {
//                    //					        tagHolder.textAddress.setText( ""+mValue );
//                    //					    }
//                }
//            });
//
//
//            holder.btndecrease.setTag(paramInt);
//            holder.btndecrease.setOnClickListener(new View.OnClickListener() {
//
//                @Override
//                public void onClick(View v) {
//                    // TODO Auto-generated method stub
//                    String a = holder.quan_value.getText().toString();
//
//                    holder.quan_value.setText(String.valueOf(Integer.parseInt(a) - 1));
//
//                    h.setProduct_quantity("" + (Integer.parseInt(a) - 1));
//                }
//            });


            return paramView;
        }
    }

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        switch (v.getId()) {
            case R.id.btnupdatecart:
                //http://www.puyangan.com/api/updatecart.php?user_id=67&product_id[]=703&p_qunty[]=5
                UrlarrayforUpdate.clear();
                for (int j = 0; j < length; j++) {

                    final Holder h = arraylist.get(j);
                    productquantity = h.getProduct_quantity();
                    productid = h.getProduct_id();

                    UrlarrayforUpdate.add("product_id[]=" + productid + "&p_qunty[]=" + productquantity);

                }
                arraylist.clear();
                //			ListUtils.setDynamicHeight(cartlist);

                new UpdateProgress().execute();


                break;
            case R.id.btnnext:

                fragment = new OrderPreferences();
                ft = getActivity().getSupportFragmentManager().beginTransaction();
                ft.add(R.id.frame, fragment);
                ft.addToBackStack("add" + MainActivity.add);
                ft.commit();
                MainActivity.add++;
                break;
        }
    }

    public class UpdateProgress extends AsyncTask<String, String, String> {
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

            arraylist.clear();
            Totalprice = 0.0;
            Product_totalprice = 0.0;
            String a = UrlarrayforUpdate.toString().substring(1, UrlarrayforUpdate.toString().length() - 1).trim();
            UrlarrayupdateString = a.replace(",", "&").trim();
            UrlarrayupdateString = UrlarrayupdateString.replace(" ", "");

            Url = "/updatecart.php?user_id=52&" + UrlarrayupdateString;
            Log.e("updateurl", UrlarrayupdateString);
            JSONParser jParser = new JSONParser();
            //http://www.puyangan.com/api/cart.php?user_id=67
            JSONObject json = jParser.getJSONFromUrl(getResources().getString(R.string.url) + "/api/updatecart.php?user_id=" + LoginId + "&" + UrlarrayupdateString + "&device_id=" + SplashPage.DeviceId);
            try {
                //http://www.puyangan.com/api/category.php?cid=178
                // Getting Array of Employee
                productcart = json.getJSONArray("productcart");
                length = productcart.length();
                Order_Count = length;
                // looping of List
                for (int i = 0; i < length; i++) {
                    JSONObject c = productcart.getJSONObject(i);
                    //					Bitmap myBitmap = null;
                    InputStream input = null;

                    Product_id = c.getString("product_id");
                    Product_name = c.getString("product_name");
                    Product_price = c.getString("product_price");
                    Product_quantity = c.getString("product_quantity");
                    Product_image = c.getString("product_image");
                    Weight = c.getString("product_weight");
                    Holder h = new Holder();
                    if (Product_image.equalsIgnoreCase("")) {

                    } else {

                        URL url = new URL(Product_image);
                        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                        connection.setDoInput(true);
                        connection.setInstanceFollowRedirects(false);
                        connection.setRequestMethod("GET");
                        connection.connect();
                        input = connection.getInputStream();

                        BitmapFactory.Options opts = new BitmapFactory.Options();
                        // opts.inJustDecodeBounds = true;
                        opts.inSampleSize = 4;
                        myBitmap = BitmapFactory.decodeStream(input, null, opts);
                        //						Bitmap scaled = Bitmap.createScaledBitmap(myBitmap, 200, 200, true);
                        //myBitmap.recycle();

                        //						Bitmap myBitmap = BitmapFactory.decodeStream(input);
                        h.setBitmap(myBitmap);
                        connection.disconnect();
                    }
                    h.setProduct_id(Product_id);
                    h.setProduct_name(Product_name);
                    h.setProduct_price(Product_price);
                    h.setProduct_quantity(Product_quantity);
                    h.setProduct_image(Product_image);
                    h.setPWeight(Weight);
                    Product_totalprice = Double.parseDouble(Product_quantity) * Double.parseDouble(Product_price);
                    h.setProduct_total_price(Product_totalprice);
                    arraylist.add(h);

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
            cartlist.setAdapter(new MyCustomAdapter(getActivity(), arraylist));
            ListUtils.setDynamicHeight(cartlist);
            //			txttotalprice.setText("$"+Total_Price);
            txttotalprice.setText("$" + Totalprice);
            //			new OrderProgressBar().execute();
        }


    }

    public class DeleteProgress extends AsyncTask<String, String, String> {

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
            arraylist.clear();
            Totalprice = 0.0;
            Product_totalprice = 0.0;
            JSONParser jParser = new JSONParser();
            //http://www.puyangan.com/api/deletecart.php?user_id=29&product_id[]=703
            JSONObject json = jParser.getJSONFromUrl(getResources().getString(R.string.url) + "/api/deletecart.php?user_id=" + LoginId + "&product_id[]=" + Product_id + "&device_id=" + SplashPage.DeviceId);
            try {
                //http://www.puyangan.com/api/category.php?cid=178
                // Getting Array of Employee
                productcart = json.getJSONArray("productcart");
                length = productcart.length();
                Order_Count = length;
                // looping of List
                for (int i = 0; i < length; i++) {
                    JSONObject c = productcart.getJSONObject(i);
                    //					Bitmap myBitmap = null;
                    InputStream input = null;

                    Product_id = c.getString("product_id");
                    Product_name = c.getString("product_name");
                    Product_price = c.getString("product_price");
                    Product_quantity = c.getString("product_quantity");
                    Product_image = c.getString("product_image");
                    Weight = c.getString("product_weight");
                    Holder h = new Holder();
                    if (Product_image.equalsIgnoreCase("")) {

                    } else {

                        URL url = new URL(Product_image);
                        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                        connection.setDoInput(true);
                        connection.setInstanceFollowRedirects(false);
                        connection.setRequestMethod("GET");
                        connection.connect();
                        input = connection.getInputStream();

                        BitmapFactory.Options opts = new BitmapFactory.Options();
                        // opts.inJustDecodeBounds = true;
                        opts.inSampleSize = 4;
                        myBitmap = BitmapFactory.decodeStream(input, null, opts);
                        //						Bitmap scaled = Bitmap.createScaledBitmap(myBitmap, 200, 200, true);
                        //myBitmap.recycle();

                        //						Bitmap myBitmap = BitmapFactory.decodeStream(input);
                        h.setBitmap(myBitmap);
                        connection.disconnect();
                    }
                    h.setProduct_id(Product_id);
                    h.setProduct_name(Product_name);
                    h.setProduct_price(Product_price);
                    h.setProduct_quantity(Product_quantity);
                    h.setProduct_image(Product_image);
                    h.setPWeight(Weight);
                    Product_totalprice = Double.parseDouble(Product_quantity) * Double.parseDouble(Product_price);
                    h.setProduct_total_price(Product_totalprice);
                    arraylist.add(h);

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
            cartlist.setAdapter(new MyCustomAdapter(getActivity(), arraylist));
            ListUtils.setDynamicHeight(cartlist);
            //			txttotalprice.setText("$"+Total_Price);
            txttotalprice.setText("$" + Totalprice);
            //			new OrderProgressBar().execute();
            if (length == 0) {
                idcart.setVisibility(View.GONE);
                emptytext.setVisibility(View.VISIBLE);
                rlfirst.setVisibility(View.GONE);
                rlinnerfooter.setVisibility(View.GONE);
            } else {
                idcart.setVisibility(View.VISIBLE);
                emptytext.setVisibility(View.GONE);
                rlfirst.setVisibility(View.VISIBLE);
                rlinnerfooter.setVisibility(View.VISIBLE);
            }
        }
    }
}
