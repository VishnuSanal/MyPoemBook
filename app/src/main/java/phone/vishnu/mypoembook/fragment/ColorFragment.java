package phone.vishnu.mypoembook.fragment;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.os.Bundle;
import android.util.DisplayMetrics;
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
import phone.vishnu.mypoembook.helper.ExportHelper;
import phone.vishnu.mypoembook.helper.SharedPreferenceHelper;

public class ColorFragment extends Fragment {

    private ColorAdapter colorAdapter;
    private GridView gridView;

    public ColorFragment() {
    }

    public static ColorFragment newInstance(int COLOR_REQ_CODE) {
        Bundle args = new Bundle();
        args.putInt("ColorRequestCode", COLOR_REQ_CODE);
        ColorFragment fragment = new ColorFragment();
        fragment.setArguments(args);
        return fragment;
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

        final int colorRequestCode = getArguments().getInt("ColorRequestCode");

        final SharedPreferenceHelper sharedPreferenceHelper = new SharedPreferenceHelper(requireContext());
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                String colorString = colorAdapter.getColor(position);

                if (colorRequestCode == 1) {

                    sharedPreferenceHelper.setCardColorPreference(colorString);
                    Toast.makeText(requireContext(), "Accent Colour Set...", Toast.LENGTH_LONG).show();


                } else if (colorRequestCode == 0) {

                    DisplayMetrics metrics = new DisplayMetrics();
                    metrics.widthPixels = 1080;
                    metrics.heightPixels = 1920;

                    Bitmap image = Bitmap.createBitmap(metrics.widthPixels, metrics.heightPixels, Bitmap.Config.ARGB_8888);
                    Canvas canvas = new Canvas(image);
                    canvas.drawColor(Color.parseColor(colorString));

                    new ExportHelper(requireContext()).exportBackgroundImage(image);

                    Toast.makeText(requireContext(), "Background Set...", Toast.LENGTH_LONG).show();

                    requireActivity().onBackPressed();
                }
            }
        });
    }
}