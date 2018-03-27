package com.kota_app.poipoi;

import android.Manifest;
import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.location.Location;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.FusedLocationProviderApi;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.Projection;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.VisibleRegion;
import com.google.firebase.auth.FirebaseAuth;
import com.google.maps.android.clustering.ClusterManager;

import java.util.Arrays;
import java.util.Locale;

import uk.co.deanwild.materialshowcaseview.MaterialShowcaseSequence;
import uk.co.deanwild.materialshowcaseview.ShowcaseConfig;

import static android.support.v4.app.AppLaunchChecker.onActivityCreate;

public class ScrollingActivity extends AppCompatActivity implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener, Animator.AnimatorListener {
    private GoogleMap mMap;
    private FirebaseAuth auth = FirebaseAuth.getInstance();
    private AdView adView;
    private CoordinatorLayout coordinatorLayout;
    private BottomSheetBehavior bottomSheetBehavior;
    private BottomSheetBehavior bottomSheetBehavior_select;
    private BottomSheetBehavior bottomSheetBehavior_notice;
    private LinearLayout bottomSheet;
    private LinearLayout bottomSheet_select;
    private LinearLayout bottomSheet_notice;
    private Toolbar toolbar;
    private final int REQUEST_PERMISSION = 1000;
    private Marker workm = null;
    private GoogleApiClient client;
    private LocationRequest request;
    private FusedLocationProviderApi api;
    private Marker marker[] = new Marker[1000];
    private String data_get;
    private float mTmpSlideOffset;
    private CoordinatorLayout.LayoutParams params;
    private FloatingActionButton fab;
    private FloatingActionButton fab2;
    private ConstraintLayout head_select;
    private ConstraintLayout head_add;
    private TextView textView_loc;
    private Button button_combustibles;
    private Button button_incombustibles;
    private Button button_pet;
    private Button button_can;
    private Button button_login;
    private ImageButton button_popup;
    private ImageView imageView;
    private TextView textView_pull;
    private TextView textView_name;
    private TextView textView_contribution;
    private Integer count_select = 0;
    public LatLng home = new LatLng(35.720045, 139.643646);
    private String workm_id[] = new String[1];
    private Integer count_marker[] = new Integer[1];
    private android.app.DialogFragment dialog = new Dialog_add();
    private android.app.DialogFragment dialog_filter = new dialog_filter();
    private android.app.DialogFragment dialog_notlogin = new Dialog_notlogin();
    private android.app.DialogFragment dialog_userinfo = new Dialog_userinfo();
    private android.app.DialogFragment dialog_changeinfo = new Change_info();
    private Boolean anim_change = false;
    private int state = 0;
    public boolean all = true;
    private boolean combustibles = false;
    private boolean incombustibles = false;
    private boolean pet = false;
    private boolean can = false;
    private boolean animate_up = false;
    private boolean animate_down = false;
    private CardView search_card;
    private boolean get_place = false;
    private int padding_in_px;
    private int padding_in_px_select;
    private int padding_in_px_searchbox;
    private boolean card_show = true;
    private static final int RC_SIGN_IN = 123;
    private static final String SHOWCASE_ID = "sequence example";
    private ClusterManager<MyItem> mClusterManager;


    //permission取得済みフラグ
    private boolean isPermissionGranted=false;
    //map準備済みフラグ
    private boolean isMapReadied=false;


    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            onActivityCreate(MainActivity.getInstance()); //エラー
        } catch (Exception e){
        }
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)!= PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(ScrollingActivity.this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_PERMISSION);
        }else {
            //permission取得済みフラグをonに
            isPermissionGranted=true;
            //もしmapの準備も完了していたら現在地表示onにする
            setMyLocationEnabled();
        }

        setContentView(R.layout.activity_scrolling);
        MobileAds.initialize(this,"ca-app-pub-9318890511624941~4820466471");

        adView = (AdView) findViewById(R.id.adView);
        final AdRequest adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);



        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);getFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
        coordinatorLayout = (CoordinatorLayout) findViewById(R.id.coordinatorLayout);

        getLocation();

        bottomSheetBehavior = BottomSheetBehavior.from(findViewById(R.id.bottom_sheet));
        bottomSheetBehavior_select = BottomSheetBehavior.from(findViewById(R.id.bottom_sheet_select));
        bottomSheetBehavior_notice = BottomSheetBehavior.from(findViewById(R.id.bottom_sheet_notice));
        bottomSheet = (LinearLayout) findViewById(R.id.bottom_sheet);
        bottomSheet_select = (LinearLayout) findViewById(R.id.bottom_sheet_select);
        bottomSheet_notice = (LinearLayout) findViewById(R.id.bottom_sheet_notice);
        head_select = (ConstraintLayout) findViewById(R.id.select_head);
        head_add = (ConstraintLayout) findViewById(R.id.add_head);
        button_combustibles = (Button) findViewById(R.id.button_combustibles);
        button_incombustibles = (Button) findViewById(R.id.button_incombustibles);
        button_pet = (Button) findViewById(R.id.button_pet);
        button_can = (Button) findViewById(R.id.button_can);
        textView_loc = (TextView) findViewById(R.id.textView15);
        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab2 = (FloatingActionButton) findViewById(R.id.fab2);
        params = (CoordinatorLayout.LayoutParams) fab2.getLayoutParams();
        search_card = (CardView) findViewById(R.id.search_card);
        button_login = (Button)findViewById(R.id.button_login);
        button_popup = (ImageButton)findViewById(R.id.imageButton5);
        imageView = (ImageView) findViewById(R.id.imageView15);
        textView_pull = (TextView) findViewById(R.id.textView24);
        textView_name = (TextView) findViewById(R.id.textView_bottom_name);
        textView_contribution = (TextView) findViewById(R.id.textView_bottom_contribution);
        final float scale = getResources().getDisplayMetrics().density;
        padding_in_px = (int) (125 * scale + 0.5f);
        padding_in_px_select = (int) (70 * scale + 0.5f);
        padding_in_px_searchbox = (int) (51.5 * scale + 0.5f);

        if(auth.getCurrentUser() == null) {
            params.setAnchorId(R.id.bottom_sheet);
        }else {
            params.setAnchorId(R.id.space6);
        }
        params.anchorGravity = Gravity.TOP | Gravity.RIGHT;
        fab2.setLayoutParams(params);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("アイコンをタップで表示");
        toolbar.setNavigationIcon(R.drawable.ic_close_black_24dp);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                all = true;
                anim_change=true;
                fab2.setImageDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.ic_filter_list_black_24dp, null));
                toolbar.setVisibility(View.GONE);
                bottomSheetBehavior_select.setState(BottomSheetBehavior.STATE_HIDDEN);
                mMap.setPadding(0,padding_in_px_searchbox,0,0);
                params.setAnchorId(R.id.space6);
                params.anchorGravity = Gravity.TOP| Gravity.RIGHT;
                fab2.setLayoutParams(params);
                bottomSheet_select.setVisibility(View.GONE);
                fab2.show();
                asyncStart();
                ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(fab2, "rotation", 0f,0f);
                objectAnimator.setDuration(0);
                objectAnimator.start();
            }
        });

        View bottomSheet = findViewById(R.id.bottom_sheet);
        final View bottomSheet_select = findViewById(R.id.bottom_sheet_select);
        View bottomSheet_notice = findViewById(R.id.bottom_sheet_notice);
        BottomSheetBehavior behavior = BottomSheetBehavior.from(bottomSheet);
        BottomSheetBehavior behavior_select = BottomSheetBehavior.from(bottomSheet_select);
        BottomSheetBehavior behavior_notice = BottomSheetBehavior.from(bottomSheet_notice);
        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
        bottomSheetBehavior_select.setState(BottomSheetBehavior.STATE_HIDDEN);
        behavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override public void onStateChanged(View bottomSheet, int newState) {
                switch (newState){
                    case BottomSheetBehavior.STATE_COLLAPSED:
                        if (!card_show){
                            Animation_down();
                        }
                        if (head_add.getVisibility() == View.VISIBLE){
                            if(bottomSheet_select.getVisibility() != View.GONE){
                                fab2.setVisibility(View.INVISIBLE);
                            } else {
                                params.setAnchorId(R.id.space);
                                params.anchorGravity = Gravity.TOP | Gravity.RIGHT;
                                fab2.setLayoutParams(params);
                            }
                        } else if (head_select.getVisibility() == View.VISIBLE){
                            if(bottomSheet_select.getVisibility() != View.GONE){
                                fab2.setVisibility(View.INVISIBLE);
                            } else {
                                params.setAnchorId(R.id.space5);
                                params.anchorGravity = Gravity.TOP | Gravity.RIGHT;
                                fab2.setLayoutParams(params);
                            }
                        }

                        state=0;
                        mMap.setPadding(0,padding_in_px_searchbox,0,padding_in_px);
                        break;
                    case BottomSheetBehavior.STATE_DRAGGING:
                        params.setAnchorId(R.id.bottom_sheet);
                        params.anchorGravity = Gravity.TOP | Gravity.RIGHT;
                        fab2.setLayoutParams(params);
                        break;
                    case BottomSheetBehavior.STATE_EXPANDED:
                        if(card_show){
                            Animation_up();
                        }
                        params.setAnchorId(R.id.bottom_sheet);
                        params.anchorGravity = Gravity.TOP | Gravity.RIGHT;
                        fab2.setLayoutParams(params);
                        break;
                    case BottomSheetBehavior.STATE_HIDDEN:
                        if(auth.getCurrentUser() == null) {
                            adView.setVisibility(View.VISIBLE);
                        }
                        mMap.setPadding(0,padding_in_px_searchbox,0,0);
                        if(auth.getCurrentUser() == null) {
                            params.setAnchorId(R.id.bottom_sheet);
                        }else {
                            params.setAnchorId(R.id.space6);
                        }
                        params.anchorGravity = Gravity.TOP | Gravity.RIGHT;
                        fab2.setLayoutParams(params);

                        if (!card_show){
                            Animation_down();
                        }

                        if(bottomSheet_select.getVisibility() != View.GONE){
                            fab2.show();
                        }
                        if(workm!=null){
                            workm.remove();
                        }
                        fab.setVisibility(View.INVISIBLE);
                        break;
                    case BottomSheetBehavior.STATE_SETTLING:
                        break;
                }
            }

            @Override public void onSlide(View bottomSheet, float slideOffset) {
                /*if (slideOffset < -0.7) {
                    if (mTmpSlideOffset < slideOffset) {
                    } else {
                        if(bottomSheet_select.getVisibility() != View.GONE){
                            fab2.setVisibility(View.INVISIBLE);
                        } else {
                            params.setAnchorId(R.id.space5);
                            params.anchorGravity = Gravity.TOP | Gravity.RIGHT;
                            fab2.setLayoutParams(params);
                        }
                    }
                }*/

                if (slideOffset < -0.3) {
                    if (mTmpSlideOffset > slideOffset) {
                        if(auth.getCurrentUser() == null) {
                            params.setAnchorId(R.id.bottom_sheet);
                        }else {
                            params.setAnchorId(R.id.space6);
                        }
                        params.anchorGravity = Gravity.TOP | Gravity.RIGHT;
                        fab2.setLayoutParams(params);
                    } else {
                        if(bottomSheet_select.getVisibility() != View.GONE){
                            fab2.setVisibility(View.INVISIBLE);
                        } else {
                            params.setAnchorId(R.id.space5);
                            params.anchorGravity = Gravity.TOP | Gravity.RIGHT;
                            fab2.setLayoutParams(params);
                        }
                    }
                }

                if(slideOffset <0.8 && slideOffset > 0.7){
                    if (mTmpSlideOffset < slideOffset && !animate_up){
                        Animation_up();
                    } else if (mTmpSlideOffset > slideOffset && !animate_down) {
                        Animation_down();
                    }
                }
                mTmpSlideOffset = slideOffset;
            }
        });

        behavior_select.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override public void onStateChanged(View bottomSheet, int newState) {
                switch (newState){
                    case BottomSheetBehavior.STATE_COLLAPSED:
                        params.setAnchorId(R.id.space4);
                        params.anchorGravity = Gravity.TOP | Gravity.RIGHT;
                        fab2.setLayoutParams(params);
                        mMap.setPadding(0,padding_in_px_searchbox,0,padding_in_px_select);
                        break;
                    case BottomSheetBehavior.STATE_DRAGGING:
                        params.setAnchorId(R.id.bottom_sheet_select);
                        params.anchorGravity = Gravity.TOP | Gravity.RIGHT;
                        fab2.setLayoutParams(params);
//                        bottomSheetBehavior_select.setState(BottomSheetBehavior.STATE_COLLAPSED);
                        break;
                    case BottomSheetBehavior.STATE_EXPANDED:
                        break;
                    case BottomSheetBehavior.STATE_HIDDEN:
                        if(!anim_change){
                        ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(fab2, "rotation", 0f, -180f);
                        objectAnimator.setDuration(300);
                        objectAnimator.start();
                        }
                        anim_change=false;
                        mMap.setPadding(0,padding_in_px_searchbox,0,0);
                        break;
                    case BottomSheetBehavior.STATE_SETTLING:
                        break;
                }
            }

            @Override public void onSlide(View bottomSheet, float slideOffset) {
            }
        });

        behavior_notice.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override public void onStateChanged(View bottomSheet, int newState) {
                switch (newState){
                    case BottomSheetBehavior.STATE_COLLAPSED:
                        if (!card_show){
                            Animation_down();
                        }
                        params.setAnchorId(R.id.space6);
                        params.anchorGravity = Gravity.TOP | Gravity.RIGHT;
                        fab2.setLayoutParams(params);
                        imageView.setVisibility(View.VISIBLE);
                        textView_pull.setVisibility(View.VISIBLE);
                        textView_name.setVisibility(View.INVISIBLE);
                        textView_contribution.setVisibility(View.INVISIBLE);
                        state=0;
//                        mMap.setPadding(0,padding_in_px_searchbox,0,padding_in_px);
                        break;
                    case BottomSheetBehavior.STATE_DRAGGING:
                        params.setAnchorId(R.id.bottom_sheet_notice);
                        params.anchorGravity = Gravity.TOP | Gravity.RIGHT;
                        fab2.setLayoutParams(params);
                        break;
                    case BottomSheetBehavior.STATE_EXPANDED:
                        if(card_show){
                            Animation_up();
                        }
                        params.setAnchorId(R.id.bottom_sheet_notice);
                        params.anchorGravity = Gravity.TOP | Gravity.RIGHT;
                        fab2.setLayoutParams(params);
                        imageView.setVisibility(View.INVISIBLE);
                        textView_pull.setVisibility(View.INVISIBLE);
                        textView_name.setVisibility(View.VISIBLE);
                        textView_contribution.setVisibility(View.VISIBLE);
                        asyncStart_getuserabout();
                        asyncStart_getusermarker();
                        /*params.setAnchorId(R.id.bottom_sheet);
                        params.anchorGravity = Gravity.TOP | Gravity.RIGHT;
                        fab2.setLayoutParams(params);*/
                        break;
                    case BottomSheetBehavior.STATE_HIDDEN:
                        //mMap.setPadding(0,padding_in_px_searchbox,0,0);

                        if (!card_show){
                            Animation_down();
                        }

                        /*if(bottomSheet_select.getVisibility() != View.GONE){
                            fab2.show();
                        }
                        if(workm!=null){
                            workm.remove();
                        }
                        fab.setVisibility(View.INVISIBLE);*/
                        break;
                    case BottomSheetBehavior.STATE_SETTLING:
                        break;
                }
            }

            @Override public void onSlide(View bottomSheet, float slideOffset) {
                if (slideOffset < -0.7) {
                    /*if (mTmpSlideOffset < slideOffset) {
                    } else {
                        if(bottomSheet_select.getVisibility() != View.GONE){
                            fab2.setVisibility(View.INVISIBLE);
                        } else {
                            params.setAnchorId(R.id.space5);
                            params.anchorGravity = Gravity.TOP | Gravity.RIGHT;
                            fab2.setLayoutParams(params);
                        }
                    }*/
                }

                if(slideOffset <0.8 && slideOffset > 0.7){
                    if (mTmpSlideOffset < slideOffset && !animate_up){
                        Animation_up();
                    } else if (mTmpSlideOffset > slideOffset && !animate_down) {
                        Animation_down();
                    }
                }
                mTmpSlideOffset = slideOffset;
            }
        });

        PlaceAutocompleteFragment autocompleteFragment = (PlaceAutocompleteFragment)
                getFragmentManager().findFragmentById(R.id.place_autocomplete_fragment);

        autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place) {
                // TODO:Get info about the selected place.
                get_place=true;
                if (mMap != null) {
                    LatLng loc = place.getLatLng();
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(loc, 16f));
                }
            }

            @Override
            public void onError(Status status) {
                // TODO:Handle the error.
            }
        });
        asyncStart_getuserabout();
        if (auth != null) {
            asyncStart_getusermarker();
        }
    }

    @Override
    protected void onStart(){
        super.onStart();
        ShowcaseConfig config = new ShowcaseConfig();
        config.setDelay(500);
        MaterialShowcaseSequence sequence = new MaterialShowcaseSequence(this, SHOWCASE_ID);
        sequence.setConfig(config);
        if (auth.getCurrentUser() == null){
            button_login = (Button) findViewById(R.id.button_login);
            button_login.setVisibility(View.VISIBLE);
            adView.setVisibility(View.VISIBLE);
            bottomSheet_notice.setVisibility(View.INVISIBLE);
            sequence.addSequenceItem(fab2,"ここを押して表示するゴミ箱を捨てられるごみの種類で選択することができます","開始する");
        } else {
            adView.setVisibility(View.GONE);
            sequence.addSequenceItem(bottomSheet_notice,"ここを押してあなたへの通知と投稿したゴミ箱について確認できます","次へ");
            sequence.addSequenceItem(button_popup, "このボタンを押すと出てくるメニューでアカウントの操作ができます", "開始する");
        }
        sequence.start();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.getUiSettings().setMapToolbarEnabled(false);
        mMap.setPadding(0,padding_in_px_searchbox,0,0);
        final BitmapDescriptor icon = BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_YELLOW);

        mMap.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {
            int flag = 1;

            public void onMapLongClick(LatLng longpushLocation) {
                if (workm == null) {
                    LatLng newlocation = new LatLng(longpushLocation.latitude, longpushLocation.longitude);
                    float zoom = mMap.getCameraPosition().zoom;
                    workm = mMap.addMarker(new MarkerOptions().position(newlocation).icon(icon).draggable(true));
                    workm_id[0] = workm.getId();
                    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(newlocation, zoom));

                    // Position the map.
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(51.503186, -0.126446), 10));

                    // Initialize the manager with the context and the map.
                    // (Activity extends context, so we can pass 'this' in the constructor.)
                    mClusterManager = new ClusterManager<MyItem>(getApplicationContext(), mMap);

                    // Point the map's listeners at the listeners implemented by the cluster
                    // manager.
                    mMap.setOnCameraIdleListener(mClusterManager);
                    mMap.setOnMarkerClickListener(mClusterManager);

                    // Add cluster items (markers) to the cluster manager.
                    addItems();
                } else {
                    workm.remove();
                    LatLng newlocation = new LatLng(longpushLocation.latitude, longpushLocation.longitude);
                    float zoom = mMap.getCameraPosition().zoom;
                    workm = mMap.addMarker(new MarkerOptions().position(newlocation).icon(icon).draggable(true));
                    workm_id[0] = workm.getId();
                    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(newlocation, zoom));
                }
                ViewGroup.LayoutParams params1 =  bottomSheet.getLayoutParams();
                params1.height = padding_in_px;
                bottomSheet.setLayoutParams(params1);
                bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                fab.show();
                if(bottomSheet_select.getVisibility() != View.GONE){
                    fab2.setVisibility(View.INVISIBLE);
                } else {
                    params.setAnchorId(R.id.space);
                    params.anchorGravity = Gravity.TOP | Gravity.RIGHT;
                    fab2.setLayoutParams(params);
                }
                head_select.setVisibility(View.GONE);
                head_add.setVisibility(View.VISIBLE);
                textView_loc.setText(String.format(workm.getPosition().latitude + "," + workm.getPosition().longitude));
            }
        });

        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            public void onMapClick(LatLng point){
                    bottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
                    fab.setVisibility(View.GONE);
                if (workm!=null){
                    workm.remove();
                }
                /*head_add.setVisibility(View.GONE);
                head_select.setVisibility(View.GONE);*/
            }
        });

        mMap.setOnMarkerClickListener(new OnMarkerClickListener(){
            @Override
            public boolean onMarkerClick(Marker marker){
                adView.setVisibility(View.GONE);
                String id = marker.getId();
                if(id.equals(workm_id[0])){
                    return false;
                }
                if (workm != null){
                    workm.remove();
                }
                fab.setVisibility(View.GONE);
                ViewGroup.LayoutParams params1 =  bottomSheet.getLayoutParams();
                params1.height = ViewGroup.LayoutParams.MATCH_PARENT;
                bottomSheet.setLayoutParams(params1);
                bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                head_add.setVisibility(View.GONE);
                head_select.setVisibility(View.VISIBLE);
                if(bottomSheet_select.getVisibility() != View.GONE){
                    fab2.setVisibility(View.INVISIBLE);
                } else {
                    params.setAnchorId(R.id.space5);
                    params.anchorGravity = Gravity.TOP | Gravity.RIGHT;
                    fab2.setLayoutParams(params);
                }
                asyncStart_getinfo(marker.getId());
                return false;
            }
        });

        mMap.setOnCameraIdleListener(new GoogleMap.OnCameraIdleListener() {
            @Override
            public void onCameraIdle() {
                onGetArea();
            }
        });

        mMap.setOnMyLocationButtonClickListener(new GoogleMap.OnMyLocationButtonClickListener() {
            @Override
            public boolean onMyLocationButtonClick() {
                return false;
            }
        });
        mMap.moveCamera(CameraUpdateFactory.newLatLng(home));

        mMap.setOnMarkerDragListener(new GoogleMap.OnMarkerDragListener() {
            @Override
            public void onMarkerDragStart(Marker marker) {
            }

            @Override
            public void onMarkerDrag(Marker marker) {
            }

            @Override
            public void onMarkerDragEnd(Marker marker) {
            }
        });

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            Toast.makeText(getApplicationContext(), "位置情報の取得が不可能です", Toast.LENGTH_LONG).show();
            return;
        }
        //mapの準備終了のフラグを立てる
        isMapReadied=true;
        //permisssionも得えられていたら現在地表示onにする
        setMyLocationEnabled();
    }

    public void SheetClick(View view){
        if (head_select.getVisibility()==View.VISIBLE) {
            if (bottomSheetBehavior.getState() == BottomSheetBehavior.STATE_COLLAPSED) {
                bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
            } else if (bottomSheetBehavior.getState() == BottomSheetBehavior.STATE_EXPANDED) {
                bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
            }
        }
    }

    public void SheetClick_notice(View view){
        if (bottomSheetBehavior_notice.getState() == BottomSheetBehavior.STATE_COLLAPSED) {
            bottomSheetBehavior_notice.setState(BottomSheetBehavior.STATE_EXPANDED);
        } else if (bottomSheetBehavior_notice.getState() == BottomSheetBehavior.STATE_EXPANDED){
            bottomSheetBehavior_notice.setState(BottomSheetBehavior.STATE_COLLAPSED  );
        }
    }

    public void fabClick(View view){
        if (auth.getCurrentUser() != null) {
            Bundle args = new Bundle();
            args.putString("LatLng",String.format("%s,%s", workm.getPosition().latitude, workm.getPosition().longitude));
            dialog.setArguments(args);
            dialog.show(getFragmentManager(), "dialog_custom");
        }else{
            dialog_notlogin.show(getFragmentManager(),"dialog_custom");
        }
    }

    public void changeinfo_Click(View view){
        Bundle args = new Bundle();
//        args.putString("LatLng",String.format("%s,%s", workm.getPosition().latitude, workm.getPosition().longitude));
        dialog_changeinfo.setArguments(args);
        dialog_changeinfo.show(getFragmentManager(), "dialog_custom");
    }

    public void fab_filterClick(View view){
        if(bottomSheet_select.getVisibility() == View.GONE) {
            all = false;
            anim_change=true;
            ViewGroup.LayoutParams params1 =  bottomSheet_select.getLayoutParams();
            params1.height = padding_in_px;
            bottomSheet_select.setLayoutParams(params1);
            bottomSheet_select.setVisibility(View.VISIBLE);
            bottomSheetBehavior_select.setState(BottomSheetBehavior.STATE_COLLAPSED);
            params.setAnchorId(R.id.space4);
            params.anchorGravity = Gravity.TOP | Gravity.RIGHT;
            fab2.setLayoutParams(params);
            fab2.setImageDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.ic_keyboard_arrow_down_white_24px, null));
            if (bottomSheetBehavior.getState() == BottomSheetBehavior.STATE_COLLAPSED) {
                fab2.setVisibility(View.INVISIBLE);
            }
            toolbar.setVisibility(View.VISIBLE);
            asyncStart();
            /*ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(fab2, "rotation", 120f, 0f);
            objectAnimator.setDuration(400);
            objectAnimator.start();*/
        } else if(bottomSheetBehavior_select.getState() == BottomSheetBehavior.STATE_COLLAPSED) {
            params.setAnchorId(R.id.bottom_sheet_select);
            params.anchorGravity = Gravity.TOP | Gravity.RIGHT;
            fab2.setLayoutParams(params);
            anim_change = true;
            bottomSheetBehavior_select.setState(BottomSheetBehavior.STATE_HIDDEN);
            ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(fab2, "rotation", 0f, -180f);
            objectAnimator.setDuration(300);
            objectAnimator.start();
        } else if(bottomSheetBehavior_select.getState() == BottomSheetBehavior.STATE_HIDDEN) {
            params.setAnchorId(R.id.space4);
            params.anchorGravity = Gravity.TOP | Gravity.RIGHT;
            fab2.setLayoutParams(params);
            bottomSheetBehavior_select.setState(BottomSheetBehavior.STATE_COLLAPSED);
            ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(fab2, "rotation", 180f,360);
            objectAnimator.setDuration(300);
            objectAnimator.start();
        }
        /*Bundle args = new Bundle();
        args.putBoolean("all",all);
        args.putBoolean("combustibles",combustibles);
        args.putBoolean("incombustibles",incombustibles);
        args.putBoolean("pet",pet);
        args.putBoolean("can",can);*/
        /*dialog_filter.setArguments(args);
        dialog_filter.show(getFragmentManager(), "dialog_custom");*/
    }

    public void searchbuttonClick(View view){
        int PLACE_AUTOCOMPLETE_REQUEST_CODE = 1;
        try {
            Intent intent =
                    new PlaceAutocomplete.IntentBuilder(PlaceAutocomplete.MODE_OVERLAY)
                            .setBoundsBias(new LatLngBounds(
                                    new LatLng(20.25, 122.56),
                                    new LatLng(45.33, 153.59)))
                            .build(this);
            startActivityForResult(intent, PLACE_AUTOCOMPLETE_REQUEST_CODE);
        } catch (GooglePlayServicesRepairableException e) {
            // TODO:Handle the error.
        } catch (GooglePlayServicesNotAvailableException e) {
            // TODO:Handle the error.
        }
    }

    public void userinfo_click(View view){
        Bundle args = new Bundle();
        dialog_userinfo.setArguments(args);
        dialog_userinfo.show(getFragmentManager(), "dialog_custom");
//        startActivity(new Intent(this, ScrollingActivity2.class));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
                Place place = PlaceAutocomplete.getPlace(this, data);
                LatLng loc = place.getLatLng();
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(loc, 16f));
            } else if (resultCode == PlaceAutocomplete.RESULT_ERROR) {
                Status status = PlaceAutocomplete.getStatus(this, data);
            } else if (resultCode == RESULT_CANCELED) {
            }
        }

        if (requestCode == RC_SIGN_IN) {
            if (resultCode == RESULT_OK) {
                // user is signed in
                startActivity(new Intent(this, EnterUserName.class));
                finish();
            } else {
                Toast.makeText(this,"サインインできませんでした。",Toast.LENGTH_LONG);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == REQUEST_PERMISSION) {
            // 使用が許可された
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    return;
                }
                //permission取得済みフラグonに
                isPermissionGranted=true;
                //mapの準備も終わっていたら現在地表示onに
                setMyLocationEnabled();
                getLocation();
                return;

            }
        }
    }

    private void getLocation(){
        // 位置情報のリクエスト情報を取得
        request = LocationRequest.create()
                .setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY)
                .setInterval(1000)
                .setFastestInterval(15);
        api = LocationServices.FusedLocationApi;
        // Google Playへの接続クライアントを生成
        client = new GoogleApiClient.Builder(this)
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();
    }

    public void closebuttonClick(View view){
        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
        workm.remove();
    }

    public void popupClick(View view){
        PopupMenu popupMenu = new PopupMenu(this,button_popup);
        popupMenu.getMenuInflater().inflate(R.menu.popup, popupMenu.getMenu());
        popupMenu.show();

        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                if(menuItem.getItemId() == R.id.item01){
                    if(auth != null) {
                        startActivity(new Intent(getApplicationContext(), Setting_account.class));
                    }
                }

                if(menuItem.getItemId() == R.id.item02){
                    auth.signOut();
                    Toast.makeText(getApplication(), "ログアウトしました", Toast.LENGTH_LONG).show();
                    Animation_down();
                    bottomSheet_notice.setVisibility(View.INVISIBLE);
                    button_login.setVisibility(View.VISIBLE);
                    adView.setVisibility(View.VISIBLE);
                }
                return true;
            }
        });
    }

    public void loginClick(View view){
        if (auth.getCurrentUser() != null) {
            startActivity(new Intent(this, EnterUserName.class));
            finish();

        } else {
            startActivityForResult(
                    AuthUI.getInstance()
                            .createSignInIntentBuilder()
                            .setAvailableProviders(
                                    Arrays.asList(new AuthUI.IdpConfig.Builder(AuthUI.EMAIL_PROVIDER).build(),
                                            new AuthUI.IdpConfig.Builder(AuthUI.GOOGLE_PROVIDER).build()
                                    ))
                            .build(),
                    RC_SIGN_IN);
        }
    }

    @Override
    protected void onResume(){
        super.onResume();
        // GooglePlayへの接続
        if (client != null){
            client.connect();
        }
    }

    @Override
    public void onConnected(Bundle bundle){
        // ACCESS_FINE_LOCATIONへのパーミッションを確認
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){
            return;
        }
        //位置情報の監視を開始
        api.requestLocationUpdates(client, request, this);
    }

    //接続が中断されたときの処理
    @Override
    public void onConnectionSuspended(int i){}

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult){}

    @Override
    public void onLocationChanged(Location location) {
        if (mMap == null) {
            return;
        }
        if (!get_place) {
            LatLng current = new LatLng(location.getLatitude(), location.getLongitude());
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(current, 16f));
            onGetArea();
            client.disconnect();
            get_place = true;
        }
    }

    public void  onGetArea(){
        Projection proj = mMap.getProjection();
        VisibleRegion vRegion = proj.getVisibleRegion();
        // Latitude:緯度 Longitude:経度
        double topLatitude = vRegion.latLngBounds.northeast.latitude;
        double bottomLatitude = vRegion.latLngBounds.southwest.latitude;
        double leftLongitude = vRegion.latLngBounds.southwest.longitude;
        double rightLongitude = vRegion.latLngBounds.northeast.longitude;
        data_get = String.format("latitude=%s,%s&longitude=%s,%s", bottomLatitude, topLatitude, leftLongitude, rightLongitude);
        asyncStart();
    }

    public void asyncStart() {
        AsyncNetworkTask_getmarker task = new AsyncNetworkTask_getmarker(this,mMap,marker, workm, workm_id, count_marker, coordinatorLayout);
        final BitmapDescriptor icon = BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_YELLOW);
        if(!all){
            if(!combustibles&&!incombustibles&&!pet&&!can){
                if(workm != null && bottomSheetBehavior.getState()==BottomSheetBehavior.STATE_COLLAPSED) {
                    final LatLng position = new LatLng(workm.getPosition().latitude, workm.getPosition().longitude);
                    mMap.clear();
                    workm = mMap.addMarker(new MarkerOptions().position(position).icon(icon).draggable(true));
                }else {
                    mMap.clear();
                }
                return;
            }
            final String  data_get_seigen = data_get + String.format("&combustibles=%s&incombustibles=%s&pet=%s&can=%s", combustibles, incombustibles, pet, can);
            task.execute(data_get_seigen);
            return;
        }
        task.execute(data_get);
    }

    public void asyncStart_getinfo(String id) {
        String info_id = "";
        String localeId = "localeid=";
        Locale locale = Locale.getDefault();
        String language = locale.getLanguage();
        String country = locale.getCountry();
        if (language.equals("ja")) {
            localeId = localeId + "_ja";
        } else if ((language.equals("zh")) && (country.equals("CN"))) {
            localeId = localeId + "_zh_CHS";
        } else if ((language.equals("zh")) && ((country.equals("TW")) || (country.equals("HK")))) {
            localeId = localeId + "_zh_CHT";
        } else if ((language.equals("ko"))) {
            localeId = localeId + "_ko";
        }
        DataHelper_marker helper = new DataHelper_marker(this);
        SQLiteDatabase db = helper.getWritableDatabase();
        Cursor cs = null;
        try {
            String[] cols = {"id"};
            String[] params = {id};
            cs = db.query("marker_info", cols, "marker_id = ?", params, null, null, null);
            if (cs.moveToFirst()){
                info_id = cs.getString(0);
            } else{
                //エラー処理
            }
        } finally {
            cs.close();
            db.close();
            AsyncNetworkTask_getinfo task_getinfo = new AsyncNetworkTask_getinfo(this);
            task_getinfo.execute(localeId +  "&id="+ info_id);
        }
    }

    public void asyncStart_getusermarker(){
        AsyncNetworkTask_getusermarker task_getusermarker_lately = new AsyncNetworkTask_getusermarker(this, "lately");
        task_getusermarker_lately.execute(String.format("uid=%s&type=lately",auth.getUid()));
        AsyncNetworkTask_getusermarker task_getusermarker_popular = new AsyncNetworkTask_getusermarker(this, "popular");
        task_getusermarker_popular.execute(String.format("uid=%s&type=popular",auth.getUid()));
    }

    public void asyncStart_getuserabout(){
        final String mes = "bottom";
        AsyncNetworkTask_checkuser task = new AsyncNetworkTask_checkuser(this,mes, null, null);
        String uid = String.format("uid=%s",auth.getUid());
        task.execute(uid);
        AsyncNetworkTask_checknumber_contribution task_checknumber_contribution = new AsyncNetworkTask_checknumber_contribution(this);
        task_checknumber_contribution.execute(uid);
    }

    public void select_Click(View v){
        if(v.getId() == R.id.button_combustibles){
            if(combustibles){
                button_combustibles.setCompoundDrawablesWithIntrinsicBounds(0,R.drawable.ic_moeru_gray,0,0);
                combustibles=false;
                count_select -= 1;
            }else if(!combustibles){
                button_combustibles.setCompoundDrawablesWithIntrinsicBounds(0,R.drawable.ic_moeru,0,0);
                combustibles=true;
                count_select += 1;
            }
        }

        if(v.getId() == R.id.button_incombustibles){
            if(incombustibles){
                button_incombustibles.setCompoundDrawablesWithIntrinsicBounds(0,R.drawable.ic_moenai_gray,0,0);
                incombustibles=false;
                count_select -= 1;
            }else if(!incombustibles){
                button_incombustibles.setCompoundDrawablesWithIntrinsicBounds(0,R.drawable.ic_moenai,0,0);
                incombustibles=true;
                count_select += 1;
            }
        }

        if(v.getId() == R.id.button_pet){
            if(pet){
                button_pet.setCompoundDrawablesWithIntrinsicBounds(0,R.drawable.ic_pet_gray,0,0);
                pet=false;
                count_select -= 1;
            }else if(!pet){
                button_pet.setCompoundDrawablesWithIntrinsicBounds(0,R.drawable.ic_pet,0,0);
                pet=true;
                count_select += 1;
            }
        }

        if(v.getId() == R.id.button_can){
            if(can){
                button_can.setCompoundDrawablesWithIntrinsicBounds(0,R.drawable.ic_can_gray,0,0);
                can=false;
                count_select -= 1;
            }else if(!can){
                button_can.setCompoundDrawablesWithIntrinsicBounds(0,R.drawable.ic_can,0,0);
                can=true;
                count_select += 1;
            }
        }
        if(count_select==0){
            toolbar.setTitle("アイコンをタップで表示");
        }else {
            toolbar.setTitle(String.format("%s種表示しています", count_select));
        }
        asyncStart();
    }
    public void onReturnValue(Boolean get_all, Boolean get_combustibles, Boolean get_incombustibles, Boolean get_pet, Boolean get_can){
        all = get_all;
        combustibles = get_combustibles;
        incombustibles = get_incombustibles;
        pet = get_pet;
        can = get_can;
        asyncStart();
    }

    private void Animation_up(){
        PropertyValuesHolder vhX = PropertyValuesHolder.ofFloat( "translationY", 0f, -padding_in_px_searchbox );
        ObjectAnimator objectAnimator = ObjectAnimator.ofPropertyValuesHolder(search_card, vhX);

        // 3秒かけて実行させます
        objectAnimator.setDuration( 300 );
        objectAnimator.addListener(ScrollingActivity.this);

        // アニメーションを開始します
        objectAnimator.start();
        animate_up = true;
        card_show = false;
    }

    private void Animation_down(){
        PropertyValuesHolder vhX = PropertyValuesHolder.ofFloat( "translationY", -padding_in_px_searchbox,0f);
        ObjectAnimator objectAnimator = ObjectAnimator.ofPropertyValuesHolder(search_card, vhX);

        // 3秒かけて実行させます
        objectAnimator.setDuration( 300 );
        objectAnimator.addListener(ScrollingActivity.this);

        // アニメーションを開始します
        objectAnimator.start();
        animate_down = true;
        card_show = true;
    }

    private void addItems() {

        // Set some lat/lng coordinates to start with.
        double lat = 51.5145160;
        double lng = -0.1270060;

        // Add ten cluster items in close proximity, for purposes of this example.
        for (int i = 0; i < 10; i++) {
            double offset = i / 60d;
            lat = lat + offset;
            lng = lng + offset;
            MyItem offsetItem = new MyItem(lat, lng);
            mClusterManager.addItem(offsetItem);
        }
    }

    // アニメーション開始で呼ばれる
    @Override
    public void onAnimationStart(Animator animation) {
        Log.d("debug","onAnimationStart()");
    }

    // アニメーションがキャンセルされると呼ばれる
    @Override
    public void onAnimationCancel(Animator animation) {
        Log.d("debug","onAnimationCancel()");
    }

    // アニメーション終了時
    @Override
    public void onAnimationEnd(Animator animation) {
        animate_up=false;
        animate_down=false;
        Log.d("debug","onAnimationEnd()");
    }

    // 繰り返しでコールバックされる
    @Override
    public void onAnimationRepeat(Animator animation) {
        Log.d("debug","onAnimationRepeat()");
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
    if(keyCode == KeyEvent.KEYCODE_BACK){
        if(bottomSheetBehavior.getState() == BottomSheetBehavior.STATE_COLLAPSED || bottomSheetBehavior.getState() == BottomSheetBehavior.STATE_SETTLING){
            bottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
            if (workm != null){
                workm.remove();
            }
            return false;
        } else if (bottomSheetBehavior.getState() == BottomSheetBehavior.STATE_EXPANDED){
            bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
            return false;
        } else if (bottomSheetBehavior_notice.getState() == BottomSheetBehavior.STATE_EXPANDED){
            bottomSheetBehavior_notice.setState(BottomSheetBehavior.STATE_COLLAPSED);
            return false;
        }
        finish();
        return true;
    }else{
        finish();
        return true;
    }
    }

    /**
     * mapが取得できていて、パーミッションもあるときのみ現在地表示をonにする。
     *
     * メゾッド内でフラグisPermissionGrantedを用いてパーミッションの有無を実質調べているので@SuppressLint("MissingPermission")
     * でGoogleMap#setMyLocationEnabled(boolean)に関するAdd permission warningを消している
     */
    @SuppressLint("MissingPermission")
    private void setMyLocationEnabled(){
        if(isPermissionGranted&&isMapReadied){
            mMap.setMyLocationEnabled(true);
        }
    }

}