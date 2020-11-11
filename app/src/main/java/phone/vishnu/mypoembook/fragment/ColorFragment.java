package phone.vishnu.mypoembook.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import phone.vishnu.mypoembook.R;
import phone.vishnu.mypoembook.helper.ColorAdapter;
import phone.vishnu.mypoembook.helper.SharedPreferenceHelper;

public class ColorFragment extends Fragment {

    private ColorAdapter colorAdapter;
    private GridView gridView;

    public ColorFragment() {
    }

    public static ColorFragment newInstance() {
        return new ColorFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View i = inflater.inflate(R.layout.fragment_color, container, false);

        gridView = i.findViewById(R.id.gridView);
        colorAdapter = new ColorAdapter(requireContext());
        gridView.setAdapter(colorAdapter);

        return i;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        final SharedPreferenceHelper sharedPreferenceHelper = new SharedPreferenceHelper(requireContext());
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                String colorString = colorAdapter.getColor(position);

                sharedPreferenceHelper.setCardColorPreference(colorString);
                Toast.makeText(requireContext(), "Accent Colour Set...", Toast.LENGTH_LONG).show();

            }
        });
    }
}