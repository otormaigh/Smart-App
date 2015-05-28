package ie.teamchile.smartapp.activities;

import ie.teamchile.smartapp.R;
import ie.teamchile.smartapp.retrofit.ApiRootModel;
import ie.teamchile.smartapp.retrofit.SmartApi;
import ie.teamchile.smartapp.utility.AppointmentSingleton;
import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;

import android.app.ActionBar;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class MenuInheritActivity extends AppCompatActivity {
	protected ProgressDialog pd;
    protected DrawerLayout drawerLayout;
    protected ListView drawerList;
    protected ActionBarDrawerToggle drawerToggle;
    protected SmartApi api;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.navigation_drawer_layout);

        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //getActionBar().setCustomView(R.layout.action_bar_custom);
        //getSupportActionBar().setCustomView(R.layout.action_bar_custom);

        getSupportActionBar().setDisplayOptions(android.support.v7.app.ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.action_bar_custom);

        ActionBar.LayoutParams lp = new ActionBar.LayoutParams(
                ActionBar.LayoutParams.WRAP_CONTENT,
                ActionBar.LayoutParams.WRAP_CONTENT,
                Gravity.CENTER_HORIZONTAL);

        createNavDrawer();
        initRetrofit();
    }

    protected void showProgressDialog(Context context, String message){
        pd = new ProgressDialog(context);
        pd.setMessage(message);
        pd.setCanceledOnTouchOutside(false);
        pd.setCancelable(false);
        pd.show();
    }

    protected void initRetrofit(){
        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint(SmartApi.BASE_URL)
                //.setLogLevel(RestAdapter.LogLevel.FULL)
                .build();

        api = restAdapter.create(SmartApi.class);
    }

    protected void setContentForNav(int layout){
        LayoutInflater inflater = (LayoutInflater) this
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View contentView = inflater.inflate(layout, null, false);
        drawerLayout.addView(contentView, 0);
    }

    protected void setActionBarTitle(String title){
        View v = getSupportActionBar().getCustomView();
        TextView titleTxtView = (TextView) v.findViewById(R.id.tv_action_bar);
        titleTxtView.setText(title);
    }

    protected void createNavDrawer(){
        String[] mPlanetTitles = getResources().getStringArray(R.array.nav_drawer_items);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawerList = (ListView) findViewById(R.id.left_drawer);
        drawerList.setAdapter(new ArrayAdapter<>(this,
                R.layout.drawer_item_layout, mPlanetTitles));
        drawerList.setOnItemClickListener(new DrawerItemClickListener());
        drawerToggle = new ActionBarDrawerToggle(this, drawerLayout,
                        R.string.drawer_open, R.string.drawer_close);
        drawerLayout.setDrawerListener(drawerToggle);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        drawerToggle.syncState();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (drawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void selectItem(int position) {
        Intent intent;
        drawerLayout.closeDrawer(drawerList);
        switch(position){
            case 0:         //Patient Search
                intent = new Intent(getApplicationContext(), ServiceUserSearchActivity.class);
                startActivity(intent);
                break;
            case 1:         //Book Appointment
                intent = new Intent(getApplicationContext(), AppointmentTypeSpinnerActivity.class);
                startActivity(intent);
                break;
            case 2:         //Calendar
                intent = new Intent(getApplicationContext(), CalendarActivity.class);
                startActivity(intent);
                break;
            case 3:         //Todays Appointments
                intent = new Intent(getApplicationContext(), TodayAppointmentActivity.class);
                startActivity(intent);
                break;
            case 4:         //Sync
                AppointmentSingleton.getInstance().updateLocal(this);
                break;
            case 5:         //Logout
                new AlertDialog.Builder(this)
                    .setTitle(R.string.Logout_title)
                    .setMessage(R.string.Logout_dialog_message)
                    .setNegativeButton(R.string.No, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialoginterface, int i) { }})
                    .setPositiveButton(R.string.Yes, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialoginterface, int i) {
                            Log.d("MYLOG", "Logout button pressed");
                            final Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |
                                    Intent.FLAG_ACTIVITY_CLEAR_TASK |
                                    Intent.FLAG_ACTIVITY_NEW_TASK);
                            if (ApiRootModel.getInstance().getLoginStatus() == false) {
                                startActivity(intent);
                            } else {
                                doLogout(intent);
                                pd = new ProgressDialog(MenuInheritActivity.this);
                                pd.setMessage("Logging Out");
                                pd.setCanceledOnTouchOutside(false);
                                pd.setCancelable(false);
                                pd.show();
                            }
                        }
                    }).show();
                break;
            default:
        }
    }

    private void doLogout(final Intent intent) {
        api.doLogout(
            ApiRootModel.getInstance().getLogin().getToken(),
            SmartApi.API_KEY,
            new Callback<ApiRootModel>() {
                @Override
                public void success(ApiRootModel apiRootModel, Response response) {
                    switch(response.getStatus()){
                        case 200:
                            Log.d("Retro", "in logout success 200");
                            doLogout(intent);
                            break;
                        default:
                            Log.d("Retro", "in logout success response = " + response.getStatus());
                    }
                }
                @Override
                public void failure(RetrofitError error) {
                Log.d("Retro", "in logout failure error = " + error);
                    if (error.getResponse().getStatus() == 401) {
                        Toast.makeText(getApplicationContext(),
                                "You are now logged out",
                                Toast.LENGTH_SHORT).show();
                        pd.dismiss();
                        startActivity(intent);
                    } else {
                        Toast.makeText(getApplicationContext(),
                                "There was a problem with the logout, " +
                                        "\nPlease try again.",
                                Toast.LENGTH_SHORT).show();
                        pd.dismiss();
                    }
                }
            }
        );
    }

    private class DrawerItemClickListener implements ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView parent, View view, int position, long id) {
            selectItem(position);
        }
    }
}