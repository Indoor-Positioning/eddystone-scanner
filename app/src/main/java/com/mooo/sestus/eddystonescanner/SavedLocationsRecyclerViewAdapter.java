package com.mooo.sestus.eddystonescanner;

import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mooo.sestus.eddystonescanner.SavedLocationsFragment.OnListFragmentInteractionListener;
import com.mooo.sestus.eddystonescanner.dummy.DummyContent.DummyItem;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * {@link RecyclerView.Adapter} that can display a {@link DummyItem} and makes a call to the
 * specified {@link OnListFragmentInteractionListener}.
 * TODO: Replace the implementation with code for your data type.
 */
public class SavedLocationsRecyclerViewAdapter extends RecyclerView.Adapter<SavedLocationsRecyclerViewAdapter.ViewHolder> {

    private final List<String> mValues;
    private final OnListFragmentInteractionListener mListener;
    private final File appDir;

    public SavedLocationsRecyclerViewAdapter(File appDir, OnListFragmentInteractionListener listener) {
        mValues = new ArrayList<>();
        this.appDir = appDir;
        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                readLocationFromAppDir();
                notifyDataSetChanged();
            }
        });
        mListener = listener;
    }

    private void readLocationFromAppDir() {
        File[] locations = appDir.listFiles();
        for (File location : locations) {
            mValues.add(location.getName());
        }
    }

    public boolean addLocation(String location) {
        if (mValues.contains(location))
            return false;
        mValues.add(location);
        notifyItemInserted(mValues.size() - 1);
        return true;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_savedlocations, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);
        holder.mContentView.setText(mValues.get(position));

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    // Notify the active callbacks interface (the activity, if the
                    // fragment is attached to one) that an item has been selected.
                    mListener.onListFragmentInteraction(holder.mItem);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public void persistLocations() {
        for (String location: mValues) {
            try {
                new File(appDir, location).createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView mIdView;
        public final TextView mContentView;
        public String mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mIdView = (TextView) view.findViewById(R.id.id);
            mContentView = (TextView) view.findViewById(R.id.content);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mContentView.getText() + "'";
        }
    }
}
