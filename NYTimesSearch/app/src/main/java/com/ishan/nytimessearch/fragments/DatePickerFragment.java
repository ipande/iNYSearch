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

        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // Get field from view
        dp = (DatePicker) view.findViewById(R.id.dpBeginDate);
        saveBtn = (Button) view.findViewById(R.id.btPickDate);
        saveBtn.setOnClickListener(this);

        String title = getArguments().getString("title", "Enter Name");
        getDialog().setTitle(title);

    }

}
