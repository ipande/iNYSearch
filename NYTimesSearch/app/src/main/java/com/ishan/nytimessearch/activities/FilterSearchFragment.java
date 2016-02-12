package com.ishan.nytimessearch.activities;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.ishan.nytimessearch.R;
import com.ishan.nytimessearch.fragments.DatePickerFragment;
import com.ishan.nytimessearch.utils.Constants;

import java.util.Calendar;

import butterknife.Bind;
import butterknife.ButterKnife;

import static com.ishan.nytimessearch.utils.Constants.APP_NAME;

/**
 * A simple {@link Fragment} subclass.
 */
public class FilterSearchFragment extends DialogFragment implements DatePickerFragment.DateEditedDialogListener{
    @Bind(R.id.etDate) EditText etDate;
    private CheckBox artCheckBox;
    private CheckBox fnsCheckBox;
    private CheckBox sportsCheckBox;
    private Spinner sortOrderSpinner;
    private String sortOrder;

    public FilterSearchFragment() {
        // Required empty public constructor
    }

    public static FilterSearchFragment newInstance(String title){
        FilterSearchFragment fragment = new FilterSearchFragment();
        Bundle args = new Bundle();
        args.putString("title", title);
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_filter_search, container);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // Get field from view
        Calendar cal = Calendar.getInstance();

        int day = cal.get(Calendar.DATE);
        int month = cal.get(Calendar.MONTH);
        Log.d(Constants.APP_NAME, "month: "+month);
        int year = cal.get(Calendar.YEAR);


        String currDate = Integer.toString(month) + " / " + Integer.toString(day) + " / " + Integer.toString(year);

        etDate.setText(currDate);

        // Fetch arguments from bundle and set title
        String title = getArguments().getString("title", "Enter Name");

        getDialog().setTitle(title);

        // Show soft keyboard automatically and request focus to field
        etDate.requestFocus();

    }

    public String dateToString(int mm, int dd, int yy){
        return Integer.toString(mm) + " / " + Integer.toString(dd) + " / " + Integer.toString(yy);
    }

    @Override
    public void onFinishDateDialog(int mm, int dd, int yy) {

        Log.d(APP_NAME,"hmmmmmm "+mm +" dd "+dd + " yy "+yy);
        etDate.setText(dateToString(mm,dd,yy));
    }
}
