package phone.vishnu.mypoembook.fragment;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.material.textfield.TextInputEditText;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;

import phone.vishnu.mypoembook.R;
import phone.vishnu.mypoembook.helper.SharedPreferenceHelper;
import phone.vishnu.mypoembook.model.CreateOptions;

public class ReviewFragment extends Fragment {

    private SharedPreferenceHelper sharedPreferenceHelper;
    private TextInputEditText presetNameTIE;
    private Button saveButton, cancelButton;

    public ReviewFragment() {
    }

    public static ReviewFragment newInstance() {
        return new ReviewFragment();
    }

    public void hideKeyboard(Activity activity) {
        InputMethodManager inputManager = (InputMethodManager) activity
                .getSystemService(Context.INPUT_METHOD_SERVICE);

        // check if no view has focus:
        View currentFocusedView = activity.getCurrentFocus();
        if (currentFocusedView != null) {
            inputManager.hideSoftInputFromWindow(currentFocusedView.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View inflate = inflater.inflate(R.layout.fragment_review, container, false);

        presetNameTIE = inflate.findViewById(R.id.presetNameTIE);

        saveButton = inflate.findViewById(R.id.reviewPresetAddButton);
        cancelButton = inflate.findViewById(R.id.reviewPresetCancelButton);

        sharedPreferenceHelper = new SharedPreferenceHelper(requireContext());

        return inflate;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        presetNameTIE.requestFocus();

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideKeyboard(requireActivity());
                requireActivity().onBackPressed();
            }
        });

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ProgressDialog progressDialog = ProgressDialog.show(requireContext(), "", "Please Wait...");

                String presetName = presetNameTIE.getText().toString().trim();

                Gson gson = new Gson();

                ArrayList<String> presetArrayList;

                if (sharedPreferenceHelper.getPresetArrayString() == null)
                    presetArrayList = new ArrayList<>();
                else
                    presetArrayList = gson.fromJson(sharedPreferenceHelper.getPresetArrayString(),
                            new TypeToken<ArrayList<String>>() {
                            }.getType());

                if (presetName.isEmpty() || presetArrayList.contains(presetName)) {
                    if (presetName.isEmpty()) {
                        presetNameTIE.setError("Field Empty");
                        presetNameTIE.requestFocus();
                    } else if (presetArrayList.contains(presetName)) {
                        presetNameTIE.setError("Preset Name Already Exists");
                        presetNameTIE.requestFocus();
                    }
                    progressDialog.dismiss();
                } else {
                    if (sharedPreferenceHelper.getFontPath() != null && sharedPreferenceHelper.getBackgroundPath() != null && sharedPreferenceHelper.getCardColorPreference() != null) {

                        CreateOptions createOptions = new CreateOptions(
                                presetName,
                                sharedPreferenceHelper.getFontPath(),
                                sharedPreferenceHelper.getBackgroundPath(),
                                sharedPreferenceHelper.getCardColorPreference()
                        );
                        sharedPreferenceHelper.resetSharedPreferences();

                        presetArrayList.add(createOptions.getName());
                        sharedPreferenceHelper.setPresetArrayString(gson.toJson(presetArrayList));

                        ArrayList<CreateOptions> createOptionsArrayList;
                        if (sharedPreferenceHelper.getCreateOptionsArrayString() == null)
                            createOptionsArrayList = new ArrayList<>();
                        else
                            createOptionsArrayList = gson.fromJson(sharedPreferenceHelper.getCreateOptionsArrayString(),
                                    new TypeToken<ArrayList<CreateOptions>>() {
                                    }.getType());

                        createOptionsArrayList.add(createOptions);
                        sharedPreferenceHelper.setCreateOptionsArrayString(gson.toJson(createOptionsArrayList));

                        progressDialog.dismiss();
                        hideKeyboard(requireActivity());
                        requireActivity().onBackPressed();
                    } else {
                        if (sharedPreferenceHelper.getFontPath() == null)
                            Toast.makeText(requireContext(), "Please select a Font", Toast.LENGTH_SHORT).show();
                        else if (sharedPreferenceHelper.getCardColorPreference() == null)
                            Toast.makeText(requireContext(), "Please select a Card Colour", Toast.LENGTH_SHORT).show();
                        else if (sharedPreferenceHelper.getBackgroundPath() == null)
                            Toast.makeText(requireContext(), "Please select a Background Image", Toast.LENGTH_SHORT).show();
                    }
                    progressDialog.dismiss();
                }
            }
        });

    }
}