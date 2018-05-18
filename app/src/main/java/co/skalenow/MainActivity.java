package co.skalenow;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import co.skalenow.newrequirments.BrowseRatings;
import co.skalenow.newrequirments.FindProduct;
import co.skalenow.newrequirments.FindUnSyncedRecords;
import co.skalenow.newrequirments.Home;
import co.skalenow.newrequirments.ManageCustomers;
import co.skalenow.newrequirments.ProductRating;
import co.skalenow.newrequirments.SearchOrder;
import co.skalenow.newrequirments.Synchronize;
import co.skalenow.newrequirments.ViewCart;

public class MainActivity extends AppCompatActivity {

    //Defining Variables
    public static Toolbar toolbar;
    public static NavigationView navigationView;
    public static DrawerLayout drawerLayout;

    String[] title;
    String[] subtitle;
    int[] icon;
    public static ListView mDrawerList;
    public static MenuListAdapter mMenuAdapter;
    public static int mSelectedItem = 0;
    public static int add = 0;
    SharedPreferences sharedpreferences;
    public static final String MyPREFERENCES = "MyPrefs";
    Handler handler;
    Runnable myRunnable;
    ConnectionDetector cd;
    String status = "0";
    Home home = new Home();
    FindProduct fp = new FindProduct();
    BrowseRatings br = new BrowseRatings();
    FindUnSyncedRecords fr = new FindUnSyncedRecords();
    ManageCustomers mc = new ManageCustomers();
    ProductRating pr = new ProductRating();
    SearchOrder so = new SearchOrder();
    Synchronize syn = new Synchronize();
    ViewCart vc = new ViewCart();
    boolean doubleBackToExitPressedOnce = false;
    String currentpage = "";
    Fragment fragment;
    FragmentTransaction ft;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE,
                WindowManager.LayoutParams.FLAG_SECURE);
        setContentView(R.layout.activity_main);

        // Initializing Toolbar and setting it as the actionbar
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        if (toolbar != null) {

            setSupportActionBar(toolbar);
        }
//        toolbar.setLogo(R.drawable.home);
//        View logoView = getToolbarLogoIcon(toolbar);
//        logoView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                //logo clicked
////                drawerLayout.openDrawer(Gravity.START);
//                FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
//                ft.replace(R.id.frame, home);
//                ft.commit();
//                mSelectedItem = 0;
//
//
//            }
//        });
//        toolbar.setNavigationIcon(R.drawable.home);
//        title = new String[]{"Cart", "Order", "Categories", "Recents", "Featured", "All", "Settings", "Log Out"};
//        toolbar.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                drawerLayout.openDrawer(Gravity.START);
//            }
//        });
        title = new String[]{"Home", "Search Products", "View Cart", "Product Rating", "Synchronize", "Find UnSynced Records", "Search Orders", "Browse Ratings", "Manage Customers", "Log Out"};
        String text = "<font color=#008edc>Skale</font><font color=#e77511>now</font>";
//        getSupportActionBar().setTitle(Html.fromHtml(text));
        getSupportActionBar().setLogo(R.drawable.title);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
//        // Generate icon
//        icon = new int[]{R.drawable.action_about, R.drawable.action_settings, R.drawable.action_about, R.drawable.action_settings, R.drawable.action_settings, R.drawable.action_settings, R.drawable.action_settings};


        //Initializing NavigationView
        navigationView = (NavigationView) findViewById(R.id.navigation_view);
        mDrawerList = (ListView) findViewById(R.id.lst_menu_items);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer);
        mMenuAdapter = new MenuListAdapter(MainActivity.this, title);
        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        // Set the MenuListAdapter to the ListView
        mDrawerList.setAdapter(mMenuAdapter);
//        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
//        ft.replace(R.id.frame, all);
//        ft.commit();
//        mSelectedItem = 5;
        FindProduct.url = "";
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.frame, home);
        ft.commit();
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putString("page", "Home");
        editor.commit();
        mSelectedItem = 0;


//        drawerLayout.openDrawer(Gravity.START);
        //Setting Navigation View Item Selected Listener to handle the item click of the navigation menu
        mDrawerList.setBackgroundColor(Color.BLACK);
        mDrawerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                // Locate Position
                SharedPreferences.Editor editor = sharedpreferences.edit();
                sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
                currentpage = sharedpreferences.getString("page", "");
                switch (position) {
                    case 0:
                        if (currentpage.equalsIgnoreCase("Home")) {

                        } else {
                            home= new Home();
                            FindProduct.url = "";
                            ft.replace(R.id.frame, home);
                            editor.putString("page", "Home");
                            editor.commit();
                        }
                        break;
                    case 1:
                        if (currentpage.equalsIgnoreCase("SearchProduct")) {

                        } else {
                            ft.replace(R.id.frame, fp);
                            editor.putString("page", "SearchProduct");
                            editor.commit();
                        }
                        break;
                    case 2:
                        if (currentpage.equalsIgnoreCase("ViewCart")) {

                        } else {
                            ft.replace(R.id.frame, vc);
                            editor.putString("page", "ViewCart");
                            editor.commit();
                        }
                        break;

                    case 3:
                        if (currentpage.equalsIgnoreCase("ProductRating")) {

                        } else {
                            ft.replace(R.id.frame, pr);
                            editor.putString("page", "ProductRating");
                            editor.commit();
                        }
                        break;
                    case 4:
                        if (currentpage.equalsIgnoreCase("Synchronize")) {

                        } else {
                            ft.replace(R.id.frame, syn);
                            editor.putString("page", "Synchronize");
                            editor.commit();
                        }
                        break;
                    case 5:
                        if (currentpage.equalsIgnoreCase("UnSynchronize")) {

                        } else {
                            ft.replace(R.id.frame, fr);
                            editor.putString("page", "UnSynchronize");
                            editor.commit();
                        }
                        break;

                    case 6:
                        if (currentpage.equalsIgnoreCase("SearchOrder")) {

                        } else {
                            ft.replace(R.id.frame, so);
                            editor.putString("page", "SearchOrder");
                            editor.commit();
                        }

                        break;
                    case 7:
                        if (currentpage.equalsIgnoreCase("BrowseRating")) {

                        } else {
                            editor.putString("page", "BrowseRating");
                            editor.commit();
                            ft.replace(R.id.frame, br);
                        }
                        break;
                    case 8:
                        if (currentpage.equalsIgnoreCase("ManageCustomer")) {

                        } else {
                            editor.putString("page", "ManageCustomer");
                            editor.commit();
                            ft.replace(R.id.frame, mc);
                        }
                        break;

                    case 9:
                        Intent in = new Intent(MainActivity.this, SplashPage.class);
                        startActivity(in);
                        MainActivity.this.finish();
                        editor.putInt("LoginId", Integer.parseInt("0"));
                        editor.putString("page", "");
                        editor.commit();
                        break;

                }

                ft.commit();
//                mDrawerList.setBackgroundColor(getResources().getColor(R.color.SelectedColor));
                mSelectedItem = position;
                mMenuAdapter.notifyDataSetChanged();
//                mDrawerList.setItemChecked(position, true);
//                for (int i = 0; i < mDrawerList.getChildCount(); i++) {
//                    if (position == i) {
//                        mDrawerList.getChildAt(i).setBackgroundColor(getResources().getColor(R.color.SelectedColor));
//                    } else {
//                        mDrawerList.getChildAt(i).setBackgroundColor(Color.TRANSPARENT);
//                    }
//                }
                // Get the title followed by the position
                getSupportActionBar().setTitle("Emerald Jewellers");
                // Close drawer
                drawerLayout.closeDrawers();
            }
        });

        // Initializing Drawer Layout and ActionBarToggle
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer);
        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.drawer_open, R.string.drawer_close) {

            @Override
            public void onDrawerClosed(View drawerView) {
                // Code here will be triggered once the drawer closes as we dont want anything to happen so we leave this blank
                super.onDrawerClosed(drawerView);
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                // Code here will be triggered once the drawer open as we dont want anything to happen so we leave this blank

                super.onDrawerOpened(drawerView);
            }
        };

        //Setting the actionbarToggle to drawer layout
        drawerLayout.setDrawerListener(actionBarDrawerToggle);

        //calling sync state is necessay or else your hamburger icon wont show up
        actionBarDrawerToggle.syncState();
        cd = new ConnectionDetector(getApplicationContext());
        handler = new Handler();
        myRunnable = new Runnable() {
            @Override
            public void run() {
                if (!cd.isConnectingToInternet()) {

                    Toast.makeText(MainActivity.this, "Please check your Internet connection", Toast.LENGTH_SHORT).show();

                } else {
                    new Progress().execute();

                }
                handler.postDelayed(this, 5 * 1000);
            }
        };
        handler.post(myRunnable);
    }

    public class Progress extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... strings) {
            JSONParser jParser = new JSONParser();
            // getting JSON string from URL//fname,lname,email,pwd,pwd2,zipcode,month,day,subscriber      checkuserlogin=1,user_login=emailid,user_pass=password
            JSONObject json = jParser.getJSONFromUrl("http://www.puyangan.com/security-poc/signup.php?deviceid=" + SplashPage.DeviceId);
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
            if (status.equalsIgnoreCase("0")) {
//                AlertDialog alertDialog = new AlertDialog.Builder(MainActivity.this).create();
//                alertDialog.setCancelable(false);
//                alertDialog.setTitle("Warning!");
//                alertDialog.setMessage("You are not authorized to access this app.Please contact the Owner.");
//                alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
//                    public void onClick(DialogInterface dialog, int which) {
//                        MainActivity.this.finish();
//                    }
//                });
//                alertDialog.show();
                Intent in = new Intent(MainActivity.this, SplashPage.class);
                startActivity(in);
                MainActivity.this.finish();
                handler.removeCallbacks(myRunnable);
            } else if (status.equalsIgnoreCase("1")) {
            }
        }
    }

    @Override
    public void onBackPressed() {
//        super.onBackPressed();
        FragmentManager manager = getSupportFragmentManager();
        if (manager.getBackStackEntryCount() > 0) {
            // If there are back-stack entries, leave the FragmentActivity
            // implementation take care of them.
            manager.popBackStack();

        } else {
            // Otherwise, ask user if he wants to leave :)
            if (doubleBackToExitPressedOnce) {
                super.onBackPressed();
                return;
            }

            this.doubleBackToExitPressedOnce = true;
            Toast.makeText(this, "Please click back again to exit", Toast.LENGTH_SHORT).show();

            new Handler().postDelayed(new Runnable() {

                @Override
                public void run() {
                    doubleBackToExitPressedOnce = false;
                }
            }, 2000);
        }
    }

    public class MenuListAdapter extends BaseAdapter {

        // Declare Variables
        Context context;
        String[] mTitle;
        //    String[] mSubTitle;
//    int[] mIcon;
        LayoutInflater inflater;
        View itemView;

        public MenuListAdapter(Context context, String[] title) {
            this.context = context;
            this.mTitle = title;
//        this.mSubTitle = subtitle;
//        this.mIcon = icon;
        }

        @Override
        public int getCount() {
            return mTitle.length;
        }

        @Override
        public Object getItem(int position) {
            return mTitle[position];
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        public View getView(int position, View convertView, ViewGroup parent) {
            // Declare Variables
            TextView txtTitle;
            TextView txtSubTitle;
            ImageView imgIcon;

            inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            itemView = inflater.inflate(R.layout.drawer_list_item, parent,
                    false);

            // Locate the TextViews in drawer_list_item.xml
            txtTitle = (TextView) itemView.findViewById(R.id.title);
            txtSubTitle = (TextView) itemView.findViewById(R.id.subtitle);

            // Locate the ImageView in drawer_list_item.xml
            imgIcon = (ImageView) itemView.findViewById(R.id.icon);

            // Set the results into TextViews
            txtTitle.setText(mTitle[position]);
//        txtSubTitle.setText(mSubTitle[position]);

            // Set the results into ImageView
//        imgIcon.setImageResource(mIcon[position]);
//            if (position == 0)
//                itemView.setBackgroundColor(context.getResources().getColor(R.color.SelectedColor));
            if (position == mSelectedItem) {
                itemView.setBackgroundColor(context.getResources().getColor(R.color.SelectedColor));
            } else {
//                itemView.setBackgroundColor(Color.BLACK);
                itemView.setBackgroundColor(context.getResources().getColor(R.color.UnSelectedColor));
            }
            return itemView;
        }
    }

    //    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        MenuInflater inflater = getMenuInflater();
//        inflater.inflate(R.menu.menu_main, menu);
//        return true;
//    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_button, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {

            case android.R.id.home: {
                finish();
                return true;
            }
            case R.id.options_menu_main_search: {
//                Toast.makeText(MainActivity.this, "Refresh App", Toast.LENGTH_LONG).show();
                fragment = new ViewCart();
                ft = getSupportFragmentManager().beginTransaction();
                ft.replace(R.id.frame, fragment);
//                ft.add(R.id.frame, fragment);
//                ft.addToBackStack("add" + MainActivity.add);
                ft.commit();
//                MainActivity.add++;
                return true;
            }
            default: {

                return super.onOptionsItemSelected(item);
            }
        }
    }

    public static View getToolbarLogoIcon(Toolbar toolbar) {
        //check if contentDescription previously was set
        boolean hadContentDescription = android.text.TextUtils.isEmpty(toolbar.getLogoDescription());
        String contentDescription = String.valueOf(!hadContentDescription ? toolbar.getLogoDescription() : "logoContentDescription");
        toolbar.setLogoDescription(contentDescription);
        ArrayList<View> potentialViews = new ArrayList<View>();
        //find the view based on it's content description, set programatically or with android:contentDescription
        toolbar.findViewsWithText(potentialViews, contentDescription, View.FIND_VIEWS_WITH_CONTENT_DESCRIPTION);
        //Nav icon is always instantiated at this point because calling setLogoDescription ensures its existence
        View logoIcon = null;
        if (potentialViews.size() > 0) {
            logoIcon = potentialViews.get(0);
        }
        //Clear content description if not previously present
        if (hadContentDescription)
            toolbar.setLogoDescription(null);
        return logoIcon;
    }
}
