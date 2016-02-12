package com.ishan.nytimessearch.activities;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;

import com.ishan.nytimessearch.R;
import com.ishan.nytimessearch.fragments.DatePickerFragment;
import com.ishan.nytimessearch.utils.Constants;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import butterknife.Bind;
import butterknife.ButterKnife;

import static com.ishan.nytimessearch.utils.Constants.APP_NAME;

/**
 * A simple {@link Fragment} subclass.
 */
public class FilterSearchFragment extends DialogFragment implements DatePickerFragment.DateEditedDialogListener{
    @Bind(R.id.etDate) EditText etDate;
    @Bind(R.id.cbArts) CheckBox artCheckBox;
    @Bind(R.id.cbFnS) CheckBox fnsCheckBox;
    @Bind(R.id.cbSports) CheckBox sportsCheckBox;
    @Bind(R.id.spinnerSortOrder) Spinner sortOrderSpinner;
    @Bind(R.id.buttonSaveFilters)Button saveButton;
    private String sortOrder;
    private String beginDate = null;

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

    public interface FiltersEditedDialogListener {
        void onFinishFilters(String sortedOrder, boolean artChecked, boolean fnsCheckBoxChecked, boolean sportsCheckBoxChecked, String beginDate);
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

        // Create an ArrayAdapter using the string array and a default spinner
        ArrayAdapter<CharSequence> staticAdapter = ArrayAdapter
                .createFromResource(getActivity().getApplicationContext(), R.array.sort_order_array,
                        R.layout.spinner_item);
        staticAdapter
                .setDropDownViewResource(R.layout.spinner_item);
        // Apply the adapter to the spinner
        sortOrderSpinner.setAdapter(staticAdapter);




        String currDate = Integer.toString(month) + " / " + Integer.toString(day) + " / " + Integer.toString(year);

        etDate.setText(currDate);

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(v.getId() == R.id.buttonSaveFilters){
                    Log.d(APP_NAME,"sortOrder "+sortOrderSpinner.getSelectedItem());
                    FiltersEditedDialogListener listener = (FiltersEditedDialogListener) getActivity();
                    listener.onFinishFilters(sortOrderSpinner.getSelectedItem().toString(),artCheckBox.isChecked(),
                            fnsCheckBox.isChecked(),sportsCheckBox.isChecked(),beginDate);
                    dismiss();
                }

            }
        });



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
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        Calendar cal = Calendar.getInstance();
        cal.set(yy,mm,dd);
        beginDate = sdf.format(cal.getTime());
        etDate.setText(dateToString(mm,dd,yy));
    }
}
