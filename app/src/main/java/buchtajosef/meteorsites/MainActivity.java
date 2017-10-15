package buchtajosef.meteorsites;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import java.util.List;

public class MainActivity extends AppCompatActivity implements MeteorListFragment.OnMeteorSelectedListener, DetailFragment.OnMeteorRequestedListener{

    static final String dataURL = "https://data.nasa.gov/resource/y77d-th95.json?$$app_token=2GKpG4inx5YREBpPWHzPe1OY1&$where=year>='2011-01-01T00:00:00'";
    static final String dataFileName = "data.json";
    static final String receiverFilter = "DOWNLOAD_FINISHED";
    MeteorData mData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //start periodic updates
        PeriodicDownload pd = new PeriodicDownload();
        pd.setAlarm(getApplicationContext());

        //create list fragment if not in large screen
        if (savedInstanceState == null && findViewById(R.id.container) != null) {
            MeteorListFragment meteorListFragment = new MeteorListFragment();
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, meteorListFragment)
                    .commit();
        }
    }

    @Override
    protected void onResume () {
        registerReceiver(broadcastReceiver, new IntentFilter(receiverFilter));
        super.onResume();
    }

    @Override
    protected void onPause () {
        unregisterReceiver(broadcastReceiver);
        super.onPause();
    }

    //when user selects meteor update or created detail fragment
    public void onMeteorSelected(MeteorData meteorData) {
        mData = meteorData;
        DetailFragment detail = (DetailFragment) getSupportFragmentManager().findFragmentById(R.id.fragmentMeteorDetail);
        if (detail == null) {
            DetailFragment detailFragment = new DetailFragment();
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, detailFragment)
                    .addToBackStack(null)
                    .commit();
        } else {
            detail.setMarker(mData);
        }
    }

    //send selected meteor to detail fragment
    public MeteorData onMeteorRequested () {
        return mData;
    }

    //if app is running and download finishes reload new data
    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            List<Fragment> fragmentList = getSupportFragmentManager().getFragments();
            for (Fragment f : fragmentList) {
                if (f instanceof MeteorListFragment) {
                    ((MeteorListFragment) f).loadData();
                    ((MeteorListFragment) f).loadAdapter();
                }
            }
        }
    };
}
