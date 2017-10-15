package buchtajosef.meteorsites;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;


class MeteorListAdapter extends ArrayAdapter<MeteorData> {

    View lastSelectedView;

    MeteorListAdapter(List<MeteorData> data, Context context) {
        super(context, R.layout.row_item, data);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        MeteorData meteorData = getItem(position);
        LayoutInflater inflater = LayoutInflater.from(getContext());
        convertView = inflater.inflate(R.layout.row_item, parent, false);

        TextView txtName = (TextView) convertView.findViewById(R.id.rowName);
        TextView txtMass = (TextView) convertView.findViewById(R.id.rowMass);
        TextView txtId = (TextView) convertView.findViewById(R.id.rowId);

        //keep information about selected meteor even if it is not on screen
        if (meteorData.isSelected()) {
            convertView.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.colorPrimary));
            lastSelectedView = convertView;
        }

        txtName.setText(meteorData.getName());
        txtMass.setText(String.valueOf(meteorData.getMass()));
        txtId.setText(meteorData.getId());
        return convertView;
    }

    //change color of last selected meteor back to default
    void resetLastSelectedView () {
        if (lastSelectedView != null) {
            lastSelectedView.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.colorPrimaryDark));
            lastSelectedView = null;
        }
    }
}
