package phone.vishnu.mypoembook.fragment;

import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.ListResult;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

import phone.vishnu.mypoembook.R;
import phone.vishnu.mypoembook.adapter.RecyclerViewAdapter;

public class BackgroundFragment extends Fragment {

    private RecyclerView recyclerView;
    private RecyclerViewAdapter adapter;
    private ProgressBar bgDialog;

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
        return inflate;
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
}