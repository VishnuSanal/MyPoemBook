package phone.vishnu.mypoembook.fragment;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.ListResult;
import com.google.firebase.storage.StorageReference;
import com.yalantis.ucrop.UCrop;

import java.io.File;
import java.util.ArrayList;

import phone.vishnu.mypoembook.R;
import phone.vishnu.mypoembook.adapter.RecyclerViewAdapter;
import phone.vishnu.mypoembook.helper.ExportHelper;
import phone.vishnu.mypoembook.helper.SharedPreferenceHelper;

import static phone.vishnu.mypoembook.activity.MainActivity.PICK_IMAGE_ID;

public class BackgroundFragment extends Fragment {

    private RecyclerView recyclerView;
    private RecyclerViewAdapter adapter;
    private ProgressBar bgDialog;
    private Button buttonPlainColor, buttonFromGallery;

    public BackgroundFragment() {
    }

    public static BackgroundFragment newInstance() {
        return new BackgroundFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View inflate = inflater.inflate(R.layout.fragment_background, container, false);
        setUpRecyclerView(inflate);
        bgDialog = inflate.findViewById(R.id.imagePickRecyclerViewProgressBar);
        buttonPlainColor = inflate.findViewById(R.id.backgroundPlainColorButton);
        buttonFromGallery = inflate.findViewById(R.id.backgroundFromGalleryButton);
        return inflate;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        buttonPlainColor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requireActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.backgroungConstraintLayout, ColorFragment.newInstance(0), "ColorPick").addToBackStack(null).commit();
            }
        });

        buttonFromGallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                startActivityForResult(intent, PICK_IMAGE_ID);
            }
        });
    }

    private void setUpRecyclerView(View inflate) {
        recyclerView = inflate.findViewById(R.id.imagePickRecyclerView);
        adapter = new RecyclerViewAdapter(requireContext());
        recyclerView.setAdapter(adapter);

        recyclerView.setHasFixedSize(true);

        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReference().child("images");

        storageRef.listAll().addOnSuccessListener(new OnSuccessListener<ListResult>() {
            @Override
            public void onSuccess(ListResult listResult) {
                final ArrayList<Uri> list = new ArrayList<>();

                for (StorageReference item : listResult.getItems()) {

                    item.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            list.add(uri);
                            if (adapter != null && getContext() != null) {
                                adapter = new RecyclerViewAdapter(requireContext(), list);
                                recyclerView.setAdapter(adapter);

                                RecyclerView.LayoutManager layoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL) {
                                    @Override
                                    public void onLayoutCompleted(RecyclerView.State state) {
                                        super.onLayoutCompleted(state);
                                        if (state.getItemCount() >= 2)
                                            bgDialog.setVisibility(View.GONE);

                                    }
                                };
                                recyclerView.setLayoutManager(layoutManager);

                                adapter.notifyDataSetChanged();
                            }
                        }
                    });
                }
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK && data != null) {

            String filePath = new ExportHelper(requireContext()).getBGPath();

            if (requestCode == PICK_IMAGE_ID)
                UCrop.of(data.getData(), Uri.fromFile(new File(filePath)))
                        .withAspectRatio(9, 16)
                        .withMaxResultSize(1080, 1920)
                        .start(requireActivity());

            else if (requestCode == UCrop.REQUEST_CROP) {
                new SharedPreferenceHelper(requireContext()).setBackgroundPath(filePath);
                Toast.makeText(requireContext(), "Background Set", Toast.LENGTH_SHORT).show();
            }

        } else Toast.makeText(requireContext(), "Error...", Toast.LENGTH_SHORT).show();
    }
}