package phone.vishnu.mypoembook.fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.ListResult;
import com.google.firebase.storage.StorageReference;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.File;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Objects;

import phone.vishnu.mypoembook.R;
import phone.vishnu.mypoembook.helper.FontDataAdapter;
import phone.vishnu.mypoembook.helper.SharedPreferenceHelper;

public class FontFragment extends Fragment {

    private SharedPreferenceHelper sharedPreferenceHelper;
    private ListView listView;
    private FontDataAdapter fontDataAdapter;
    private ArrayList<String> fontList = new ArrayList<>();
    private ProgressBar progressBar;

    public FontFragment() {

    }

    public static FontFragment newInstance() {
        return new FontFragment();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        final String fontArrayListString = sharedPreferenceHelper.getFontArrayString();

        if (null != fontArrayListString) {
            fontList = new ArrayList<>();

            Gson gson = new Gson();
            Type type = new TypeToken<ArrayList<String>>() {
            }.getType();
            fontList = gson.fromJson(fontArrayListString, type);

//            progressBar.setVisibility(View.GONE);
            if (getActivity() != null)
                if (fontDataAdapter == null) {
                    fontDataAdapter = new FontDataAdapter(Objects.requireNonNull(getActivity()), fontList);
                    listView.setAdapter(fontDataAdapter);
                } else {
                    fontDataAdapter.clear();
                    fontDataAdapter.addAll(fontList);
                    fontDataAdapter.notifyDataSetChanged();
                }
        }

        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReference().child("fonts");
        storageRef.listAll().addOnSuccessListener(new OnSuccessListener<ListResult>() {
            @Override
            public void onSuccess(ListResult listResult) {
                fontList = new ArrayList<>();
                for (StorageReference item : listResult.getItems()) {

                    String fontString = item.getName().replace(".ttf", "");

                    fontString = fontString.toUpperCase().charAt(0) + fontString.substring(1);

//                    if (!fontList.contains(fontString))
                    fontList.add(fontString);
                }

                progressBar.setVisibility(View.GONE);

                Gson gson = new Gson();

                sharedPreferenceHelper.setFontArrayString(gson.toJson(fontList));

                if (getActivity() != null)
                    if (fontDataAdapter == null) {
                        fontDataAdapter = new FontDataAdapter(Objects.requireNonNull(getActivity()), fontList);
                        listView.setAdapter(fontDataAdapter);
                    } else {
                        fontDataAdapter.clear();
                        fontDataAdapter.addAll(fontList);
                        fontDataAdapter.notifyDataSetChanged();
                    }
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, final View view, int position, long id) {

                final ProgressDialog progressDialog = ProgressDialog.show(getActivity(), "", "Please Wait....");

                String fontString = fontList.get(position).toLowerCase() + ".ttf";
                StorageReference storageReference = FirebaseStorage.getInstance().getReference().child("fonts").child(fontString);
                final File localFile = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS), "MyPoemBook");
                final File f = new File(localFile + File.separator + "." + fontString);

                if (localFile.mkdirs()) if (f.mkdirs()) {
                    sharedPreferenceHelper.setFontPath(f.toString());

                    Toast.makeText(getActivity(), "Font Set.....", Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();

                    //                    getActivity().onBackPressed();
                } else {

                    storageReference.getFile(f).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                            if (getActivity() != null) {

                                sharedPreferenceHelper.setFontPath(f.toString());

                                Toast.makeText(getActivity(), "Font Set.....", Toast.LENGTH_SHORT).show();
//                                view.setBackgroundColor(Color.BLACK);
                                progressDialog.dismiss();

                                getActivity().onBackPressed();
                            } else {
                                progressDialog.dismiss();
                            }
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception exception) {
                            exception.printStackTrace();
                            //TODO: Check this
                            //                            FirebaseCrashlytics.getInstance().recordException(exception);
                            progressDialog.dismiss();
                            Toast.makeText(getActivity(), "Error.....", Toast.LENGTH_LONG).show();
                        }
                    });
                }
                else Toast.makeText(getActivity(), "Error.....", Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View inflate = inflater.inflate(R.layout.fragment_font, container, false);
        if (!isNetworkAvailable())
            Toast.makeText(getActivity(), "Please Connect to the Internet...", Toast.LENGTH_SHORT).show();

        progressBar = inflate.findViewById(R.id.fontProgressBar);
        listView = inflate.findViewById(R.id.fontListView);
        sharedPreferenceHelper = new SharedPreferenceHelper(getActivity());
        return inflate;
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
}