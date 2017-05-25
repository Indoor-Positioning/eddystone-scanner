package com.mooo.sestus.eddystonescanner;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;


public class AddLocationDialogFragment extends DialogFragment {

    private OnFragmentInteractionListener mListener;
    private EditText mLocationName;
    private EditText mLocationDescription;

    private boolean isLocationAdded;

    public AddLocationDialogFragment() {
        // Required empty public constructor
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        // Get the layout inflater
        LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService( Context.LAYOUT_INFLATER_SERVICE );

        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout
        View view = inflater.inflate(R.layout.fragment_new_location, null);
        final AlertDialog alertDialog = builder.setView(view)
                .setTitle("Add new location")
                // Add action buttons
                .setPositiveButton(R.string.location_add, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {

                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        AddLocationDialogFragment.this.getDialog().cancel();
                    }
                }).create();
        alertDialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialog) {
                Button button = alertDialog.getButton(AlertDialog.BUTTON_POSITIVE);
                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mLocationName = (EditText) getDialog().findViewById(R.id.location_name);
                        mLocationDescription = (EditText) getDialog().findViewById(R.id.location_description);
                        isLocationAdded = onLocationAdded(mLocationName.getText().toString(), mLocationDescription.getText().toString());
                        if (!isLocationAdded) {
                            mLocationName.setError("Location already exists");
                            alertDialog.show();
                        }
                        else
                            alertDialog.dismiss();
                    }
                });
            }
        });

        return alertDialog;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public boolean onLocationAdded(String name, String description) {
        return mListener != null && mListener.onLocationAdded(name, description);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        boolean onLocationAdded(String location, String description);
    }
}
