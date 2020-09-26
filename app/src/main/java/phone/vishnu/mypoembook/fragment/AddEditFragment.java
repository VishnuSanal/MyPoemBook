package phone.vishnu.mypoembook.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.textfield.TextInputEditText;

import java.util.Objects;

import phone.vishnu.mypoembook.R;
import phone.vishnu.mypoembook.activity.MainActivity;
import phone.vishnu.mypoembook.model.Poem;
import phone.vishnu.mypoembook.viewmodel.PoemViewModel;

import static phone.vishnu.mypoembook.activity.MainActivity.DESCRIPTION_EXTRA;
import static phone.vishnu.mypoembook.activity.MainActivity.ID_EXTRA;
import static phone.vishnu.mypoembook.activity.MainActivity.TITLE_EXTRA;

public class AddEditFragment extends Fragment {

    private Button saveButton, cancelButton;
    private TextInputEditText titleTIE, descriptionTIE;
    private boolean isEdit;

    public AddEditFragment() {
    }

    public static AddEditFragment newInstance() {
        return new AddEditFragment();
    }

    public static AddEditFragment newInstance(Poem poem) {
        Bundle args = new Bundle();

        args.putInt(ID_EXTRA, poem.getId());
        args.putString(TITLE_EXTRA, poem.getTitle());
        args.putString(DESCRIPTION_EXTRA, poem.getDescription());

        AddEditFragment fragment = new AddEditFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View inflate = inflater.inflate(R.layout.fragment_add_edit, container, false);

        titleTIE = inflate.findViewById(R.id.addTitleTIE);
        descriptionTIE = inflate.findViewById(R.id.addDescriptionTIE);
        saveButton = inflate.findViewById(R.id.addSaveButton);
        cancelButton = inflate.findViewById(R.id.addCancelButton);

        if (getArguments() != null) {

            isEdit = true;

            ((ImageView) inflate.findViewById(R.id.addNewIcon)).setImageResource(R.drawable.ic_edit);
            ((ImageView) inflate.findViewById(R.id.addNewIcon)).setColorFilter(getResources().getColor(android.R.color.background_light));

            titleTIE.setText(getArguments().getString(TITLE_EXTRA));
            descriptionTIE.setText(getArguments().getString(DESCRIPTION_EXTRA));
        }

        return inflate;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String title = titleTIE.getText().toString().trim();
                String description = descriptionTIE.getText().toString().trim();

                if (isValid(title, description)) {

                    if (isEdit)
                        sendData(new Poem(Objects.requireNonNull(getArguments()).getInt(ID_EXTRA), title, description));
                    else
                        sendData(new Poem(title, description));

                    Objects.requireNonNull(getView()).getRootView().clearFocus();
                    Objects.requireNonNull((MainActivity) Objects.requireNonNull(getActivity())).setVisibility(true);
                    Objects.requireNonNull((MainActivity) Objects.requireNonNull(getActivity())).onBackPressed();
                }
            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Objects.requireNonNull(getView()).getRootView().clearFocus();
                Objects.requireNonNull((MainActivity) Objects.requireNonNull(getActivity())).setVisibility(true);
                Objects.requireNonNull((MainActivity) Objects.requireNonNull(getActivity())).onBackPressed();

            }
        });
    }

    private void sendData(Poem poem) {
        PoemViewModel poemViewModel = new ViewModelProvider(this,
                new ViewModelProvider.AndroidViewModelFactory(Objects.requireNonNull(getActivity()).getApplication())).get(PoemViewModel.class);

        if (isEdit)
            poemViewModel.update(poem);
        else
            poemViewModel.insert(poem);
    }

    private boolean isValid(String title, String description) {

        if (title.isEmpty() || description.isEmpty()) {

            if (title.isEmpty())
                titleTIE.setError("Field Empty");

            else if (description.isEmpty())
                descriptionTIE.setError("Field Empty");

            return false;
        }
        return true;
    }
}