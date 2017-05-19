package com.mooo.sestus.eddystonescanner;

import android.app.ListFragment;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by mike on 5/18/17.
 */

public class SavedLocationsFragment extends ListFragment {

    SavedLocationsAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_list_locations, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        FloatingActionButton fab = (FloatingActionButton) getActivity().findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Add location not implemented yet", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        fab.setImageResource(R.drawable.ic_add_location_black);
        fab.show();
    }

    @Override
    public void onResume() {
        super.onResume();
        adapter = new SavedLocationsAdapter();
    }
}
