package com.ishan.nytimessearch.fragments;


import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;

import com.ishan.nytimessearch.R;
import com.ishan.nytimessearch.activities.FilterSearchFragment;
import com.ishan.nytimessearch.utils.Constants;

import java.util.Calendar;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 */
public class DatePickerFragment extends DialogFragment implements View.OnClickListener{
//    @Bind(R.id.dpBeginDate) DatePicker dp;
    DatePicker dp;
    Button saveBtn;

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.btPickDate){
            DateEditedDialogListener listener = (DateEditedDialogListener) getActivity().getSupportFragmentManager().findFragmentByTag("Search Options");
            listener.onFinishDateDialog(dp.getMonth(),dp.getDayOfMonth(),dp.getYear());
            dismiss();
        }
    }

    public interface DateEditedDialogListener {
        void onFinishDateDialog(int mm, int dd, int yy);
    }


    public DatePickerFragment() {
        // Required empty public constructor
    }

    public static DatePickerFragment newInstance(String title){
        DatePickerFragment fragment = new DatePickerFragment();
        Bundle args = new Bundle();
        args.putString("title", title);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_date_picker, container);
        dp = (DatePicker) view.findViewById(R.id.dpBeginDate);
        saveBtn = (Button) view.findViewById(R.id.btPickDate);
        saveBtn.setOnClickListener(this);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // Get field from view

        dp = (DatePicker) view.findViewById(R.id.dpBeginDate);
        String title = getArguments().getString("title", "Enter Name");

        getDialog().setTitle(title);

    }



//    @Override
//    public Dialog onCreateDialog(Bundle savedInstanceState) {
//
//        // Use the current time as the default values for the picker
//        final Calendar c = Calendar.getInstance();
//        int year = c.get(Calendar.YEAR);
//        int month = c.get(Calendar.MONTH);
//        int day = c.get(Calendar.DAY_OF_MONTH);
//        // Activity needs to implement this interface
//
//        DatePickerDialog.OnDateSetListener listener = (DatePickerDialog.OnDateSetListener) getActivity();
//        // Create a new instance of TimePickerDialog and return it
//        return new DatePickerDialog(getActivity(), listener, year, month, day);
//
//    }

}
