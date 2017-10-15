package buchtajosef.meteorsites;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class DetailFragment extends Fragment implements OnMapReadyCallback {

    MapView mapView;
    GoogleMap map;
    MeteorData mData;
    Marker lastMarker;
    TextView id, name, mass, year, coordinates;
    boolean restoreState = false;

    OnMeteorRequestedListener callback;

    interface OnMeteorRequestedListener{
        MeteorData onMeteorRequested();
    }

    public DetailFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            callback = (OnMeteorRequestedListener) context;
        } catch (ClassCastException e){
            throw new RuntimeException(context.toString() + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) {
            mData = new MeteorData(
                    savedInstanceState.getInt("id"),
                    savedInstanceState.getString("name"),
                    null,
                    null,
                    null,
                    savedInstanceState.getString("year"),
                    null,
                    null,
                    savedInstanceState.getInt("mass"),
                    new double[] {savedInstanceState.getDouble("lat"), savedInstanceState.getDouble("long")}
            );
            restoreState = true;
        }
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        savedInstanceState.putInt("id", mData.getIntID());
        savedInstanceState.putString("name",  mData.getName());
        savedInstanceState.putInt("mass",  mData.getMass());
        savedInstanceState.putString("year",  mData.getYear());
        savedInstanceState.putString("coordinates",  mData.getCoordinatesText());
        savedInstanceState.putDouble("lat",  mData.getCoordinates()[0]);
        savedInstanceState.putDouble("long",  mData.getCoordinates()[1]);
        super.onSaveInstanceState(savedInstanceState);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_detail, container, false);
        if (savedInstanceState == null)
            mData = callback.onMeteorRequested();

        id = (TextView) v.findViewById(R.id.detailID);
        name = (TextView) v.findViewById(R.id.detailName);
        mass = (TextView) v.findViewById(R.id.detailMass);
        year = (TextView) v.findViewById(R.id.detailYear);
        coordinates = (TextView) v.findViewById(R.id.detailCoordinates);

        // Gets the MapView from the XML layout and creates it
        mapView = (MapView) v.findViewById(R.id.mapView);
        mapView.onCreate(savedInstanceState);
        mapView.onResume();
        mapView.getMapAsync(this);
        return v;
    }

    @Override
    public void onMapReady(GoogleMap map) {
        this.map = map;
        setMarker(mData);
    }

    public void setMarker (MeteorData data) {
        if (restoreState) {
            restoreState = false;
        } else {
            mData = data;
        }
        if (mData != null) {
            if (lastMarker != null)
                lastMarker.remove();
            MarkerOptions markerOptions = new MarkerOptions();
            LatLng markerCoordinates = new LatLng(mData.getCoordinates()[0], mData.getCoordinates()[1]);
            markerOptions.position(markerCoordinates)
                    .title(mData.getName());
            lastMarker = map.addMarker(markerOptions);
            map.moveCamera(CameraUpdateFactory.newLatLngZoom(markerCoordinates, 5));
            map.animateCamera(CameraUpdateFactory.zoomIn());
            map.animateCamera(CameraUpdateFactory.zoomTo(5), 2000, null);

            id.setText(mData.getId());
            name.setText(mData.getName());
            mass.setText(mData.getMassText());
            year.setText(mData.getYear());
            coordinates.setText(mData.getCoordinatesText());
        }
        map.getUiSettings().setZoomControlsEnabled(true);
    }


}
