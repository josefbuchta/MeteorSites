package buchtajosef.meteorsites;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import org.json.JSONArray;
import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class MeteorListFragment extends ListFragment {

    OnMeteorSelectedListener callback;
    List<MeteorData> dataList = new ArrayList<>();
    View clickedItem;
    int clickedItemPosition;

    interface OnMeteorSelectedListener{
        void onMeteorSelected(MeteorData mData);
    }

    public MeteorListFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_meteor_list, container, false);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        //load data from JSON file
        loadData();
        //get interface to main activity
        try {
            callback = (OnMeteorSelectedListener) context;
        } catch (ClassCastException e){
            throw new RuntimeException(context.toString() + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        //restore view after rotation
        if (savedInstanceState != null) {
            clickedItemPosition = savedInstanceState.getInt("position");
            dataList.get(clickedItemPosition).setSelected(true);
        }
        loadAdapter();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        callback = null;
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        ((MeteorListAdapter)getListAdapter()).resetLastSelectedView();
        if (clickedItem != null)
            clickedItem.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.colorPrimaryDark));
        clickedItem = v;
        clickedItem.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.colorPrimary));
        dataList.get(clickedItemPosition).setSelected(false);
        clickedItemPosition = position;
        dataList.get(clickedItemPosition).setSelected(true);

        callback.onMeteorSelected(dataList.get(position));
    }

    private String getDataFromFile () {
        try {
            FileInputStream in = new FileInputStream(new File(getContext().getFilesDir(), MainActivity.dataFileName));
            //BufferedReader reader = new BufferedReader(new InputStreamReader(in));
            int size = in.available();
            byte[] buffer = new byte[size];
            in.read(buffer);
            in.close();
            return new String(buffer, "UTF-8");
        } catch (Exception e) {
            return null;
        }
    }

    public void loadData() {
        try {
            //JSONObject jsonObject = new JSONObject(getDataFromFile());
            String data = getDataFromFile();
            if (data != null) {
                dataList.clear();
                JSONArray jsonArray = new JSONArray(data);
                for (int i = 0; i < jsonArray.length(); i++) {
                    //dataList.add(jsonArray.getJSONObject(i).getString("name"));
                    JSONArray jsonCoordinates = jsonArray.getJSONObject(i).optJSONObject("geolocation").optJSONArray("coordinates");
                    double[] coordinates = {jsonCoordinates.optDouble(0), jsonCoordinates.optDouble(1)};
                    dataList.add(new MeteorData(
                            jsonArray.getJSONObject(i).optInt("id"),
                            jsonArray.getJSONObject(i).optString("name"),
                            jsonArray.getJSONObject(i).optString("nameType"),
                            jsonArray.getJSONObject(i).optString("recClass"),
                            jsonArray.getJSONObject(i).optString("fall"),
                            jsonArray.getJSONObject(i).optString("year"),
                            jsonArray.getJSONObject(i).optString("reclat"),
                            jsonArray.getJSONObject(i).optString("reclong"),
                            jsonArray.getJSONObject(i).optInt("mass"),
                            coordinates
                    ));
                }
                Collections.sort(dataList, new MeteorMassComparator());
                Collections.reverse(dataList);
            }
        } catch (Exception e) {}
    }

    public void loadAdapter() {
        setListAdapter(new MeteorListAdapter(dataList, getContext()));
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("position", clickedItemPosition);
    }
}
