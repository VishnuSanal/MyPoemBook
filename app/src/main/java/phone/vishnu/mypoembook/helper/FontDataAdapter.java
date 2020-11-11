package phone.vishnu.mypoembook.helper;

import android.content.Context;
import android.graphics.Typeface;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.util.ArrayList;

import phone.vishnu.mypoembook.R;

public class FontDataAdapter extends ArrayAdapter<String> {

    private final LayoutInflater inflater;
    private final ArrayList<String> objects;

    public FontDataAdapter(@NonNull Context context, ArrayList<String> objects) {
        super(context, 0, objects);
        this.objects = objects;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @NonNull
    @Override
    public View getView(int position, final View convertView, @NonNull ViewGroup parent) {

        View rootView = convertView;
        final ViewHolder viewHolder;

        if (rootView == null) {
            viewHolder = new ViewHolder();
            rootView = inflater.inflate(R.layout.font_single_item, parent, false);

            rootView.setLayoutParams(new ConstraintLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));

            viewHolder.fontTV = rootView.findViewById(R.id.singleItemFontName);
            viewHolder.progressBar = rootView.findViewById(R.id.singleItemProgressBar);

            rootView.setTag(viewHolder);

            String fontString = objects.get(position).toLowerCase() + ".ttf";

            StorageReference storageReference = FirebaseStorage.getInstance().getReference().child("fonts").child(fontString);
            final File localFile = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS), "MyPoemBook");

            final File f = new File(localFile + File.separator + "." + fontString);

            if (f.exists()) {

                viewHolder.progressBar.setProgress(100);

                try {
                    Typeface face = Typeface.createFromFile(f);
                    viewHolder.fontTV.setTypeface(face);
                } catch (Exception e) {
                    //Toast.makeText(getContext(), "Something Went Wrong...", Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }
            } else {
                if (!localFile.exists())
                    localFile.mkdirs();

                storageReference.getFile(f).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                        try {
                            Typeface face = Typeface.createFromFile(f);
                            viewHolder.fontTV.setTypeface(face);
                        } catch (Exception e) {
                            //Toast.makeText(getContext(), "Something Went Wrong...", Toast.LENGTH_SHORT).show();
                            e.printStackTrace();
                        }

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        exception.printStackTrace();
                    }
                }).addOnProgressListener(new OnProgressListener<FileDownloadTask.TaskSnapshot>() {
                    @Override
                    public void onProgress(@NonNull FileDownloadTask.TaskSnapshot taskSnapshot) {
                        viewHolder.progressBar.setProgress(
                                (int) ((100.0 * taskSnapshot.getBytesTransferred()) / (taskSnapshot.getTotalByteCount()))
                        );
                    }
                });
            }
        } else {
            viewHolder = (ViewHolder) rootView.getTag();
        }

        viewHolder.fontTV.setText(objects.get(position));


        return rootView;
    }

    static class ViewHolder {
        TextView fontTV;
        ProgressBar progressBar;
    }
}