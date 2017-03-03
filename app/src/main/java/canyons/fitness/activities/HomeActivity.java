package canyons.fitness.activities;

import android.annotation.TargetApi;
import android.content.Intent;
import android.content.IntentSender;
import android.os.AsyncTask;
import android.os.Build;
import android.support.design.internal.NavigationMenu;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.transition.Explode;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.github.aakira.expandablelayout.ExpandableRelativeLayout;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.Scopes;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Scope;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.fitness.Fitness;
import com.google.android.gms.fitness.data.DataPoint;
import com.google.android.gms.fitness.data.DataSet;
import com.google.android.gms.fitness.data.DataSource;
import com.google.android.gms.fitness.data.DataType;
import com.google.android.gms.fitness.data.Field;
import com.google.android.gms.fitness.data.Value;
import com.google.android.gms.fitness.request.DataReadRequest;
import com.google.android.gms.fitness.request.DataSourcesRequest;
import com.google.android.gms.fitness.request.OnDataPointListener;
import com.google.android.gms.fitness.request.SensorRequest;
import com.google.android.gms.fitness.result.DailyTotalResult;
import com.google.android.gms.fitness.result.DataSourcesResult;
import com.google.firebase.auth.FirebaseAuth;

import org.honorato.multistatetogglebutton.MultiStateToggleButton;
import org.honorato.multistatetogglebutton.ToggleButton;
import org.w3c.dom.Text;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

import canyons.fitness.R;
import canyons.fitness.models.ActivityObject;
import canyons.fitness.models.FriendObject;
import canyons.fitness.models.UserDetailsObject;
import canyons.fitness.utilities.Constants;
import canyons.fitness.utilities.Settings;
import canyons.fitness.utilities.Utility;
import canyons.fitness.views.FriendRVAdapter;
import canyons.fitness.views.RecyclerViewAdapter;
import io.github.yavski.fabspeeddial.FabSpeedDial;
import io.github.yavski.fabspeeddial.SimpleMenuListenerAdapter;

public class HomeActivity extends AppCompatActivity  implements
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener {

    private static final int REQUEST_OAUTH = 1;
    private static final String AUTH_PENDING = "auth_state_pending";
    private boolean authInProgress = false;
    private GoogleApiClient mApiClient;
    RecyclerView homeRecycler;
    RecyclerViewAdapter activityAdapter;
    FriendRVAdapter friendAdapter;
    private List<ActivityObject> activityObjectList = new ArrayList<>();
    private List<UserDetailsObject> friendList = new ArrayList<>();
    private List<String> mobileNumbers = new ArrayList<>();
   // private List<FriendDetailsObject> friendDetailsObjectList = new ArrayList<>();
    private GridLayoutManager gridLayoutManager;




    FabSpeedDial fabSpeedDial;


    //Fields form the add channel section
    EditText etAddFriendID;
    Button btAddFriendDone;
    TextView tvAddFriendCancel;
    ExpandableRelativeLayout expandableLinearLayoutHome;
    TextView homeCalorietv, homeDistancetv, homeStepstv;

    //Toggle button to switch to public or my channels
    MultiStateToggleButton multiStateToggleButton;

    //Firebase variables
    Firebase firebase;
    FirebaseAuth mAuth;
    boolean friendFeed = false;
    String steps="0", calories="0", distance="0";
    UserDetailsObject myUserObj;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        if (savedInstanceState != null) {

            authInProgress = savedInstanceState.getBoolean(AUTH_PENDING);
        }


        Firebase.setAndroidContext(this);
        firebase = new Firebase(Constants.BASE_URL+Constants.USERS_MAP);
        mAuth = FirebaseAuth.getInstance();
        Log.d("mauth",mAuth.getCurrentUser().getUid());

        Log.d("date",Utility.getDate());
        Log.d("authinpr",authInProgress+"");
        mApiClient = new GoogleApiClient.Builder(this)
                .addApi(Fitness.SENSORS_API)
                .addApi(Fitness.HISTORY_API)
                .addScope(new Scope(Scopes.FITNESS_LOCATION_READ))
                .addScope(new Scope(Scopes.FITNESS_ACTIVITY_READ_WRITE))
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .enableAutoManage(this,0,this)
                .build();




        etAddFriendID = (EditText)findViewById(R.id.homer_add_friend_number);
        tvAddFriendCancel = (TextView)findViewById(R.id.home_add_friend_cancel);
        btAddFriendDone = (Button)findViewById(R.id.home_add_friend_done);
        multiStateToggleButton  = (MultiStateToggleButton)findViewById(R.id.home_switch_feed);
        homeRecycler = (RecyclerView)findViewById(R.id.home_recycler);
        homeStepstv= (TextView)findViewById(R.id.home_tv_bmi);
        homeDistancetv = (TextView)findViewById(R.id.home_tv_weight);
        homeCalorietv=(TextView)findViewById(R.id.home_tv_height);

        expandableLinearLayoutHome = (ExpandableRelativeLayout)findViewById(R.id.expandableLayoutHome);
        fabSpeedDial = (FabSpeedDial)findViewById(R.id.fab_speed_dial);
        multiStateToggleButton = (MultiStateToggleButton)findViewById(R.id.home_switch_feed);

        myUserObj = new UserDetailsObject();

        //Setting toggle to channel details by default
        multiStateToggleButton.setValue(0);


        //adding listener to toggle switch
        multiStateToggleButton.setOnValueChangedListener(new ToggleButton.OnValueChangedListener() {
            @Override
            public void onValueChanged(int value) {
                if(value==0){
                    friendFeed = false;
                }else{
                    friendFeed = true;
                }
                resetRecycerFetchData(value);
            }
        });


        activityAdapter = new RecyclerViewAdapter(getApplicationContext(),activityObjectList);
        friendAdapter = new FriendRVAdapter(getApplicationContext(), friendList, new clickListener() {
            @Override
            public void onClick(int position) {

                ActivityOptionsCompat activityOptionsCompat= ActivityOptionsCompat.makeSceneTransitionAnimation(HomeActivity.this);
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                    Explode explode = null;

                    explode = new Explode();
                    explode.setDuration(500);

                    getWindow().setExitTransition(explode);
                    getWindow().setEnterTransition(explode);


                }
                Intent intent;
                intent= new Intent(HomeActivity.this,ComparisonActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.putExtra("name",friendList.get(position).getName());
                intent.putExtra("age",friendList.get(position).getAge());
                intent.putExtra("sex",friendList.get(position).getSex());
                intent.putExtra("height1",friendList.get(position).getHeight());
                intent.putExtra("weight1",friendList.get(position).getWeight());
                intent.putExtra("bmi1",Utility.getBMI(
                        Float.parseFloat(friendList.get(position).getWeight()),
                        Float.parseFloat(friendList.get(position).getHeight())));
                intent.putExtra("height2",myUserObj.getHeight());
                intent.putExtra("weight2",myUserObj.getWeight());
                intent.putExtra("bmi2",myUserObj.getBMI());

                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {

                    startActivity(intent,activityOptionsCompat.toBundle());}else{
                    startActivity(intent);
                }
            }
        });
        gridLayoutManager = new GridLayoutManager(getApplicationContext(),2);
        homeRecycler.setLayoutManager(gridLayoutManager);

        homeRecycler.setAdapter(activityAdapter);


        //Cancel adding of channel
        tvAddFriendCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                expandableLinearLayoutHome.collapse();
            }
        });


        // Check the existence of channel and add it to the user's channel database
        btAddFriendDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (etAddFriendID.getText().toString().trim().isEmpty()
                       ) {
                    Utility.showSnack(getApplicationContext(),btAddFriendDone,Utility.FIELD_EMPTY);

                } else {

                    FriendObject obj = new FriendObject(etAddFriendID.getText().toString().trim(),"");


                    String pushID = firebase.child(mAuth.getCurrentUser().getUid()).child("friends").push().getKey();
                    obj.setPushID(pushID);

                    firebase.child(mAuth.getCurrentUser().getUid()).child("friends").child(pushID).setValue(obj);
                }
                etAddFriendID.setText("");
                expandableLinearLayoutHome.collapse();
                Utility.showSnack(getApplicationContext(),fabSpeedDial,Utility.DONE);

                }

        });


        fabSpeedDial.setMenuListener(new SimpleMenuListenerAdapter() {

            @Override
            public boolean onMenuItemSelected(MenuItem menuItem) {
                //TODO: Start some activity

                ActivityOptionsCompat activityOptionsCompat= ActivityOptionsCompat.makeSceneTransitionAnimation(HomeActivity.this);
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                    Explode explode = null;

                    explode = new Explode();
                    explode.setDuration(500);

                    getWindow().setExitTransition(explode);
                    getWindow().setEnterTransition(explode);

                }
                Intent intent;

                switch (menuItem.getItemId())
                {


                    case R.id.action_add_channel:

                        expandableLinearLayoutHome.expand();
                        break;

                    case R.id.action_sign_out:
                        mAuth.signOut();
                        Settings.clearSharedPreferences(getApplicationContext());
                        intent= new Intent(HomeActivity.this,MainActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {

                            startActivity(intent,activityOptionsCompat.toBundle());}else{
                            startActivity(intent);
                        }
                        finish();
                        break;

                }
                return false;
            }
        });

        firebase.child(mAuth.getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.d("data",dataSnapshot.toString());
                myUserObj.setHeight(dataSnapshot.child("height").getValue().toString().trim());
                myUserObj.setWeight(dataSnapshot.child("weight").getValue().toString().trim());
                myUserObj.setBMI(Utility.getBMI(
                       Float.parseFloat(dataSnapshot.child("weight").getValue().toString().trim()),
                        Float.parseFloat(dataSnapshot.child("height").getValue().toString().trim())

                ));

            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });


        if(Settings.getSharedPreference(getApplicationContext(),"date").equals("")|| (!Settings.getSharedPreference(getApplicationContext(),"date").equals(Utility.getDate()))) {
            Log.d("pushing","new activity");
            pushNewActivity();
        }
        resetRecycerFetchData(0);

    }



    public interface  clickListener{
        public void onClick(int position);
    }
    private void resetRecycerFetchData(int value) {

        activityObjectList.clear();
        friendList.clear();

        friendAdapter.notifyDataSetChanged();
        activityAdapter.notifyDataSetChanged();

        //if value =0 fetch user's personal activities
        if(value==0){

            Firebase firebaseChild =firebase.child(mAuth.getCurrentUser().getUid()).child("activities");


            firebaseChild.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    findViewById(R.id.progressIndicator).setVisibility(View.VISIBLE);
                    if(multiStateToggleButton.getValue()==0) {
                        activityObjectList.clear();
                        friendList.clear();
                        mobileNumbers.clear();
                        //friendDetailsObjectList.clear();
                        activityAdapter.notifyDataSetChanged();
                        for (DataSnapshot activityItem : dataSnapshot.getChildren()) {
                            ActivityObject activityObj = new ActivityObject();
                            activityObj.setCalories(activityItem.child("calories").getValue().toString());
                            activityObj.setDate(activityItem.child("date").getValue().toString());
                            activityObj.setDistance(activityItem.child("distance").getValue().toString());
                            activityObj.setSteps(activityItem.child("steps").getValue().toString());

                             if (!activityObjectList.contains(activityObj)) {
                                activityObjectList.add(activityObj);
                                Log.d("steps",activityObj.getSteps().toString());
                            }

                        }


                    }
                    activityAdapter.notifyDataSetChanged();
                    homeRecycler.setAdapter(activityAdapter);
                    findViewById(R.id.progressIndicator).setVisibility(View.GONE);

                }

                @Override
                public void onCancelled(FirebaseError firebaseError) {

                }
            });

        }

        //else fetch friend list
        else if(value==1){

            Firebase firebaseChild =firebase.child(mAuth.getCurrentUser().getUid()).child("friends");


            firebaseChild.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    findViewById(R.id.progressIndicator).setVisibility(View.VISIBLE);
                    if(multiStateToggleButton.getValue()==1) {
                        activityObjectList.clear();
                        friendList.clear();
                        mobileNumbers.clear();
                        friendAdapter.notifyDataSetChanged();
                        for (DataSnapshot friendItem : dataSnapshot.getChildren()) {
                            String mobile =friendItem.child("mobile").getValue().toString().trim();

                            if (!mobileNumbers.contains(mobile)) {
                                mobileNumbers.add(mobile);
                                Log.d("mobile",mobile);
                            }

                        }


                    }


                    firebase.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {


                            for (DataSnapshot userItem : dataSnapshot.getChildren()) {
                                if(mobileNumbers.contains(userItem.child("mobile").getValue().toString())){
                                    UserDetailsObject  userObj = new UserDetailsObject();
                                    userObj.setMobile(userItem.child("mobile").getValue().toString());
                                    userObj.setSex(userItem.child("sex").getValue().toString());
                                    userObj.setPushID("");
                                    userObj.setAge(userItem.child("age").getValue().toString());
                                    userObj.setBMI(Utility.getBMI(
                                            Float.parseFloat(userItem.child("weight").getValue().toString().trim()),
                                            Float.parseFloat(userItem.child("height").getValue().toString().trim())

                                    ));
                                    userObj.setHeight(userItem.child("height").getValue().toString());
                                    userObj.setWeight(userItem.child("weight").getValue().toString());
                                    userObj.setName(userItem.child("name").getValue().toString());


                                if (!friendList.contains(userObj)) {
                                    friendList.add(userObj);
                                    Log.d("mob",userObj.getMobile().toString());
                                }

                            }

                        }

                            friendAdapter.notifyDataSetChanged();
                            homeRecycler.setAdapter(friendAdapter);

                        }

                        @Override
                        public void onCancelled(FirebaseError firebaseError) {

                        }
                    });
                    findViewById(R.id.progressIndicator).setVisibility(View.GONE);

                }

                @Override
                public void onCancelled(FirebaseError firebaseError) {

                }
            });
        }
    }



    @Override
    protected void onStart() {
        super.onStart();
        mApiClient.connect();
    }
    @Override
    public void onConnected(Bundle bundle) {

        Log.d("connected","true");

        Timer feedTimer= new Timer();
        feedTimer.scheduleAtFixedRate(new TimerTask() {

            @Override
            public void run() {
                Log.d("Updater","Updating feed");
                new displayData().execute();
            }}, 0, 3000);

//
//        DataSourcesRequest dataSourceRequest = new DataSourcesRequest.Builder()
//                .setDataTypes( DataType.TYPE_STEP_COUNT_DELTA , DataType.TYPE_DISTANCE_DELTA)
//                .setDataSourceTypes(DataSource.TYPE_DERIVED )
//                .build();
//
//        ResultCallback<DataSourcesResult> dataSourcesResultCallback = new ResultCallback<DataSourcesResult>() {
//            @Override
//            public void onResult(DataSourcesResult dataSourcesResult) {
//                for( DataSource dataSource : dataSourcesResult.getDataSources() ) {
//                    if (DataType.TYPE_STEP_COUNT_DELTA.equals(dataSource.getDataType())) {
//                        registerFitnessDataListener(dataSource, DataType.TYPE_STEP_COUNT_DELTA);
//                        Log.d("calling", "registercouny");
//                    } else if (DataType.TYPE_DISTANCE_DELTA.equals(dataSource.getDataType())) {
//                        registerFitnessDataListener(dataSource, DataType.TYPE_DISTANCE_DELTA);
//                        Log.d("calling", "registerdis");
//
//                    }
//                }
//            }
//        };
//        Fitness.SensorsApi.findDataSources(mApiClient, dataSourceRequest)
//                .setResultCallback(dataSourcesResultCallback);
//


    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Log.d("conn","failed");
        if( !authInProgress ) {
            try {
                authInProgress = true;
                connectionResult.startResolutionForResult( HomeActivity.this, REQUEST_OAUTH );
            } catch(IntentSender.SendIntentException e ) {

            }
        } else {
            Log.d( "GoogleFit", "authInProgress" );
        }
    }

//    OnDataPointListener listener = new OnDataPointListener() {
//        @Override
//        public void onDataPoint(DataPoint dataPoint) {
//            Log.d("data","datapoint");
//            for( final Field field : dataPoint.getDataType().getFields() ) {
//                final Value value = dataPoint.getValue( field );
//                runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//                        Log.d("values","steps:"+steps+"distance"+distance+"value"+value+field.getName());
//
//                        if(field.getName().equals("steps"))
//                        {
//
//                            steps = value.toString();
//                            homeStepstv.setText(steps);
//
//
//                        }else if(field.getName().equals("distance")&&Float.parseFloat(value.toString())>Float.parseFloat(distance)){
//
//                            distance = value.toString().substring(0,5);
//                            homeDistancetv.setText(distance);
//                        }
//                        firebase.
//                                child(mAuth.getCurrentUser().getUid()).
//                                child("activities").
//                                child(Settings.getSharedPreference(getApplicationContext(),"pushID")).
//                                child(field.getName()).setValue(value+"");
//                      //  Toast.makeText(getApplicationContext(), "Field: " + field.getName() + " Value: " + value, Toast.LENGTH_SHORT).show();
//                    }
//                });
//            }
//        }
//    };


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if( requestCode == REQUEST_OAUTH ) {
            authInProgress = false;
            if( resultCode == RESULT_OK ) {
                if( !mApiClient.isConnecting() && !mApiClient.isConnected() ) {
                    mApiClient.connect();
                }
            } else if( resultCode == RESULT_CANCELED ) {
                Log.d( "GoogleFit", "RESULT_CANCELED" );
            }
        } else {
            Log.d("GoogleFit", "requestCode NOT request_oauth");
        }
    }

//
//
//    private void registerFitnessDataListener(DataSource dataSource, DataType dataType) {
//     //   Toast.makeText(HomeActivity.this, "registed", Toast.LENGTH_SHORT).show();
//
//        SensorRequest request = new SensorRequest.Builder()
//                .setDataSource( dataSource )
//                .setDataType( dataType )
//                .setSamplingRate( 3, TimeUnit.SECONDS )
//                .build();
//
//        Fitness.SensorsApi.add( mApiClient, request, listener )
//                .setResultCallback(new ResultCallback<Status>() {
//                    @Override
//                    public void onResult(Status status) {
//                        if (status.isSuccess()) {
//                            Log.d( "GoogleFit", "SensorApi successfully added" );
//                        }
//                    }
//                });
//    }




    @Override
    protected void onStop() {
        super.onStop();

//        Fitness.SensorsApi.remove( mApiClient, listener )
//                .setResultCallback(new ResultCallback<Status>() {
//                    @Override
//                    public void onResult(Status status) {
//                        if (status.isSuccess()) {
//                            mApiClient.disconnect();
//                        }
//                    }
//                });
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(AUTH_PENDING, authInProgress);
    }

    public  void pushNewActivity(){

        Settings.setSharedPreference(getApplicationContext(), "date", Utility.getDate());


        Log.d("dagte",Utility.getDate());
        ActivityObject obj = new ActivityObject("0","0","0",Utility.getDate());
        String pushID = firebase.child(mAuth.getCurrentUser().getUid()).child("activities").push().getKey();
        Settings.setSharedPreference(getApplicationContext(),"activityID",pushID);
        firebase.child(mAuth.getCurrentUser().getUid()).child("activities").child(pushID).setValue(obj);


    }


    public class displayData extends AsyncTask {
        @Override
        protected Object doInBackground(Object[] params) {
            DailyTotalResult resultDistance = Fitness.HistoryApi.readDailyTotal( mApiClient, DataType.TYPE_DISTANCE_DELTA ).await(1, TimeUnit.MINUTES);

            DailyTotalResult result = Fitness.HistoryApi.readDailyTotal( mApiClient, DataType.TYPE_STEP_COUNT_DELTA ).await(1, TimeUnit.MINUTES);
            DataSet dataSet = result.getTotal();
            DataSet distanceDataSet = resultDistance.getTotal();
            Log.e("History", "Data returned for Data type: " + dataSet.getDataType().getName());
            DateFormat dateFormat = DateFormat.getDateInstance();
            DateFormat timeFormat = DateFormat.getTimeInstance();

            for (DataPoint dp : dataSet.getDataPoints()) {
//                Log.e("History", "Data point:");
//                Log.e("History", "\tType: " + dp.getDataType().getName());
//                Log.e("History", "\tStart: " + dateFormat.format(dp.getStartTime(TimeUnit.MILLISECONDS)) + " " + timeFormat.format(dp.getStartTime(TimeUnit.MILLISECONDS)));
//                Log.e("History", "\tEnd: " + dateFormat.format(dp.getEndTime(TimeUnit.MILLISECONDS)) + " " + timeFormat.format(dp.getStartTime(TimeUnit.MILLISECONDS)));
                for(Field field : dp.getDataType().getFields()) {
//                    Log.e("History", "\tField: " + field.getName() +
//                            " Value: " + dp.getValue(field));
                    if(field.getName().equals("steps")){

                        steps = dp.getValue(field)+"";
                        Log.d("asnyc",steps);
                    }
                }
            }

            for (DataPoint dp : distanceDataSet.getDataPoints()) {
//                Log.e("History", "Data point:");
//                Log.e("History", "\tType: " + dp.getDataType().getName());
//                Log.e("History", "\tStart: " + dateFormat.format(dp.getStartTime(TimeUnit.MILLISECONDS)) + " " + timeFormat.format(dp.getStartTime(TimeUnit.MILLISECONDS)));
//                Log.e("History", "\tEnd: " + dateFormat.format(dp.getEndTime(TimeUnit.MILLISECONDS)) + " " + timeFormat.format(dp.getStartTime(TimeUnit.MILLISECONDS)));
                for(Field field : dp.getDataType().getFields()) {
//                    Log.e("History", "\tField: " + field.getName() +
//                            " Value: " + dp.getValue(field));
                    if(field.getName().equals("distance")){

                        distance = dp.getValue(field)+"";
                    }
                }
            }

            return null;
        }

        @Override
        protected void onPostExecute(Object o) {
            homeStepstv.setText(steps);
            float distanceCalc = Float.parseFloat(distance)/1000;
            homeDistancetv.setText(String.valueOf(distanceCalc).substring(0,6));
            homeCalorietv.setText((Float.valueOf(steps)/25)+"");

           Firebase newfirebase = new Firebase(Constants.BASE_URL);
            newfirebase.child("users").child(mAuth.getCurrentUser().getUid()).child("activities").child(Settings.getSharedPreference(getApplicationContext(),"activityID")).child("steps").setValue(steps);
            newfirebase.child("users").child(mAuth.getCurrentUser().getUid()).child("activities").child(Settings.getSharedPreference(getApplicationContext(),"activityID")).child("distance").setValue(distanceCalc);
            newfirebase.child("users").child(mAuth.getCurrentUser().getUid()).child("activities").child(Settings.getSharedPreference(getApplicationContext(),"activityID")).child("calories").setValue((Float.valueOf(steps)/25)+"");

            super.onPostExecute(o);
        }
    }


}
