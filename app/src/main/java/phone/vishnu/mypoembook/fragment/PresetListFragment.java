package phone.vishnu.mypoembook.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

import phone.vishnu.mypoembook.R;
import phone.vishnu.mypoembook.helper.SharedPreferenceHelper;
import phone.vishnu.mypoembook.model.Poem;

import static phone.vishnu.mypoembook.activity.MainActivity.DESCRIPTION_EXTRA;
import static phone.vishnu.mypoembook.activity.MainActivity.TITLE_EXTRA;

public class PresetListFragment extends Fragment {

    private SharedPreferenceHelper sharedPreferenceHelper;
    private ListView listView;

    public PresetListFragment() {
    }

    public static PresetListFragment newInstance(Poem poem) {
        Bundle args = new Bundle();
        args.putString(TITLE_EXTRA, poem.getTitle());
        args.putString(DESCRIPTION_EXTRA, poem.getDescription());

        PresetListFragment fragment = new PresetListFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View inflate = inflater.inflate(R.layout.fragment_preset_list, container, false);
        sharedPreferenceHelper = new SharedPreferenceHelper(requireContext());
        listView = inflate.findViewById(R.id.presetListView);

        final String presetArrayString = sharedPreferenceHelper.getPresetArrayString();

        ArrayList<String> presetList = new ArrayList<>();

        if (null != presetArrayString) {
            Gson gson = new Gson();
            Type type = new TypeToken<ArrayList<String>>() {
            }.getType();
            presetList = gson.fromJson(presetArrayString, type);
        }

        presetList.add("+ New Design Preset");

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_list_item_1, presetList);
        listView.setAdapter(arrayAdapter);

        return inflate;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (view instanceof TextView) {
                    if (((TextView) view).getText().toString().equals("+ New Design Preset")) {
                        requireActivity().getSupportFragmentManager().beginTransaction().replace(R.id.container, CreateFragment.newInstance(), "CreatePreset").addToBackStack(null).commit();
                    } else {
                        //TODO:Share
                    }
                }
            }
        });

    }
}