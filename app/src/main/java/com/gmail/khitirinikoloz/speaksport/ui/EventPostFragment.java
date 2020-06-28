package com.gmail.khitirinikoloz.speaksport.ui;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.PopupWindow;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.gmail.khitirinikoloz.speaksport.R;
import com.gmail.khitirinikoloz.speaksport.model.EventPost;
import com.gmail.khitirinikoloz.speaksport.model.Post;
import com.gmail.khitirinikoloz.speaksport.ui.home.HomeViewModel;
import com.google.android.gms.common.api.Status;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

import static android.app.Activity.RESULT_OK;
import static com.google.android.libraries.places.widget.AutocompleteActivity.RESULT_ERROR;

public class EventPostFragment extends Fragment {

    private static final String LOG_TAG = EventPostFragment.class.getSimpleName();
    //temporarily put here
    private static final String PLACES_API_KEY = "AIzaSyDDbHknnRztUW7cbqdGHD2yaWLdSIPI6b0";
    private static final int AUTOCOMPLETE_REQUEST_CODE = 1;
    private TextInputEditText titleEditText;
    private TextInputEditText descriptionEditText;
    private TextInputEditText startTimeEditText;
    private TextInputLayout startTimeLayout;
    private TextInputEditText endTimeEditText;
    private TextInputLayout endTimeLayout;
    private TextInputEditText locationEditText;
    private TextInputEditText topicEditText;
    private PopupWindow popupWindow;
    private View popUpView;
    private List<TextInputEditText> requiredFields;

    private final Calendar startDateTime = Calendar.getInstance();
    private final Calendar endDateTime = Calendar.getInstance();

    private NewPostActivity newPostActivity;
    private HomeViewModel homeViewModel;

    public EventPostFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(
                R.layout.fragment_event_post, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //enclosing activity
        newPostActivity = (NewPostActivity) requireActivity();

        Places.initialize(newPostActivity.getApplicationContext(), PLACES_API_KEY);
        Places.createClient(newPostActivity);

        //temporary bridge between this fragment and homeFragment to display posts.(is not correct)
        homeViewModel = new ViewModelProvider(newPostActivity).get(HomeViewModel.class);

        titleEditText = view.findViewById(R.id.title_edittext);
        descriptionEditText = view.findViewById(R.id.description_edittext);

        startTimeEditText = view.findViewById(R.id.time_start_edittext);
        startTimeLayout = view.findViewById(R.id.time_start_layout);

        endTimeEditText = view.findViewById(R.id.time_end_edittext);
        endTimeLayout = view.findViewById(R.id.time_end_layout);

        locationEditText = view.findViewById(R.id.location_edittext);

        topicEditText = view.findViewById(R.id.topic_edittext);
        Button saveButton = view.findViewById(R.id.save_post_button);

        //took out location edittext to reduce PLACES API calls.
        requiredFields = new ArrayList<>(Arrays.asList
                (titleEditText, startTimeEditText, endTimeEditText, topicEditText));

        startTimeEditText.setOnClickListener(v -> selectEventDateTimeStart());
        endTimeEditText.setOnClickListener(v -> selectEventDateTimeEnd());
        locationEditText.setOnClickListener(v -> {
            List<Place.Field> fields = Arrays.asList(Place.Field.ID, Place.Field.NAME, Place.Field.ADDRESS);
            //start the autocomplete intent
            Intent intent = new Autocomplete.IntentBuilder(
                    AutocompleteActivityMode.OVERLAY, fields
            ).build(newPostActivity);

            startActivityForResult(intent, AUTOCOMPLETE_REQUEST_CODE);
        });

        topicEditText.setOnClickListener(v -> {
            newPostActivity.setUpPopupWindow(topicEditText);
            popUpView = newPostActivity.getPopUpWindowView();
            popupWindow = newPostActivity.getPopupWindow();
            popupWindow.showAtLocation(popUpView, Gravity.CENTER, 0, 0);
        });

        saveButton.setOnClickListener(v -> saveEvent());
    }

    @Override
    public void onPause() {
        if (popupWindow != null && popupWindow.isShowing())
            popupWindow.dismiss();
        super.onPause();
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == AUTOCOMPLETE_REQUEST_CODE && data != null) {
            if (resultCode == RESULT_OK) {
                final Place place = Autocomplete.getPlaceFromIntent(data);
                String address = place.getName() + "\n" + place.getAddress();
                locationEditText.setText(address);
                locationEditText.setError(null);
            } else if (resultCode == RESULT_ERROR) {
                final Status status = Autocomplete.getStatusFromIntent(data);
                Log.e(LOG_TAG, "Fetching a place failed: " + status.getStatusMessage());
            }
        }
    }

    private void saveEvent() {
        if (!allFieldsSet())
            return;

        final String title = String.valueOf(titleEditText.getText());
        final String description = String.valueOf(descriptionEditText.getText());
        //values for event startTime and endTime are stored in Calendar variables.
        final String location = String.valueOf(locationEditText.getText());
        final String topic = String.valueOf(topicEditText.getText());

        final Post newPost = new EventPost(title, description, startDateTime, endDateTime,
                location, topic);

        //temporarily
        homeViewModel.setPost(newPost);
        newPostActivity.finish();
    }

    private boolean allFieldsSet() {
        boolean isSet = true;
        for (TextInputEditText editText : requiredFields) {
            if (TextUtils.getTrimmedLength(editText.getText()) == 0) {
                editText.setError("Field not set");
                isSet = false;
            }
        }

        if (startTimeLayout.isErrorEnabled() || endTimeLayout.isErrorEnabled())
            isSet = false;

        return isSet;
    }

    //might need refactoring in the future, does the job for now.
    private void selectEventDateTimeStart() {
        final TimePickerDialog.OnTimeSetListener timeSetListener = (view, hour, minute) -> {
            startDateTime.set(Calendar.HOUR_OF_DAY, hour);
            startDateTime.set(Calendar.MINUTE, minute);

            startTimeEditText.setError(null); //clear the editText error if set

            if (!TextUtils.isEmpty(endTimeEditText.getText()) && startDateTime.compareTo(endDateTime) > 0) {
                startTimeLayout.setError("Start time can't be after End");
            } else {
                startTimeLayout.setErrorEnabled(false);
                endTimeLayout.setErrorEnabled(false);
            }

            DateFormat dateFormat = DateFormat.getDateTimeInstance(DateFormat.MEDIUM, DateFormat.SHORT);
            startTimeEditText.setText(dateFormat.format(startDateTime.getTime()));

            endTimeEditText.setEnabled(true);
        };

        final DatePickerDialog.OnDateSetListener dateSetListener = (view, year, month, dayOfMonth) -> {
            startDateTime.set(Calendar.YEAR, year);
            startDateTime.set(Calendar.MONTH, month);
            startDateTime.set(Calendar.DAY_OF_MONTH, dayOfMonth);

            this.createTimePickerDialog(timeSetListener);
        };

        this.createDatePickerDialog(dateSetListener);
    }

    private void selectEventDateTimeEnd() {
        final TimePickerDialog.OnTimeSetListener timeSetListener = (view, hour, minute) -> {
            endDateTime.set(Calendar.HOUR_OF_DAY, hour);
            endDateTime.set(Calendar.MINUTE, minute);

            endTimeEditText.setError(null);

            if (endDateTime.compareTo(startDateTime) < 0)
                endTimeLayout.setError("End time can't be before Start");
            else {
                endTimeLayout.setErrorEnabled(false);
                startTimeLayout.setErrorEnabled(false);
            }

            DateFormat dateFormat = DateFormat.getDateTimeInstance(DateFormat.MEDIUM, DateFormat.SHORT);
            endTimeEditText.setText(dateFormat.format(endDateTime.getTime()));
        };

        final DatePickerDialog.OnDateSetListener dateSetListener = (view, year, month, dayOfMonth) -> {
            endDateTime.set(Calendar.YEAR, year);
            endDateTime.set(Calendar.MONTH, month);
            endDateTime.set(Calendar.DAY_OF_MONTH, dayOfMonth);

            this.createTimePickerDialog(timeSetListener);
        };

        this.createDatePickerDialog(dateSetListener);
    }


    private void createDatePickerDialog(@NonNull DatePickerDialog.OnDateSetListener dateSetListener) {
        final Calendar date = Calendar.getInstance();

        final DatePickerDialog datePicker = new DatePickerDialog(newPostActivity, dateSetListener,
                date.get(Calendar.YEAR),
                date.get(Calendar.MONTH),
                date.get(Calendar.DAY_OF_MONTH));
        datePicker.getDatePicker().setMinDate(System.currentTimeMillis());
        datePicker.show();
    }


    private void createTimePickerDialog(@NonNull TimePickerDialog.OnTimeSetListener timeSetListener) {
        final Calendar time = Calendar.getInstance();

        new TimePickerDialog(newPostActivity, timeSetListener,
                time.get(Calendar.HOUR_OF_DAY),
                time.get(Calendar.MINUTE), false).show();
    }
}
