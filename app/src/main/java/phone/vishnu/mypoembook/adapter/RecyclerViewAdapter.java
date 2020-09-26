package phone.vishnu.mypoembook.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import phone.vishnu.mypoembook.R;
import phone.vishnu.mypoembook.model.Poem;

public class RecyclerViewAdapter extends ListAdapter<Poem, RecyclerViewAdapter.PoemHolder> {

    private OnItemClickListener listener;

    public RecyclerViewAdapter() {
        super(new DiffUtil.ItemCallback<Poem>() {
            @Override
            public boolean areItemsTheSame(@NonNull Poem oldItem, @NonNull Poem newItem) {
                return oldItem.getId() == newItem.getId();
            }

            @Override
            public boolean areContentsTheSame(@NonNull Poem oldItem, @NonNull Poem newItem) {
                return oldItem.getTitle().equals(newItem.getTitle()) &&
                        oldItem.getDescription().equals(newItem.getDescription());
            }
        });
    }

    @NonNull
    @Override
    public PoemHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_item, parent, false);
        return new PoemHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull PoemHolder holder, int position) {
        Poem currentPoem = getItem(position);
        holder.titleTV.setText(currentPoem.getTitle());
        holder.descriptionTV.setText(currentPoem.getDescription());
    }


    public Poem getPoem(int position) {
        return getItem(position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    public interface OnItemClickListener {
        void onItemClick(Poem shelve, int id);
    }

    class PoemHolder extends RecyclerView.ViewHolder {
        private ImageView editIV, goIV;
        private TextView titleTV, descriptionTV;

        public PoemHolder(@NonNull View itemView) {
            super(itemView);

            titleTV = itemView.findViewById(R.id.todoTitle);
            descriptionTV = itemView.findViewById(R.id.todoDescription);

            editIV = itemView.findViewById(R.id.todoEditIV);
            editIV.setColorFilter(R.color.colorAccent);
            editIV.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null && getAdapterPosition() != RecyclerView.NO_POSITION)
                        listener.onItemClick(getItem(getAdapterPosition()), v.getId());
                }
            });

            goIV = itemView.findViewById(R.id.todoGoIV);
            goIV.setColorFilter(R.color.colorAccent);
            goIV.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null && getAdapterPosition() != RecyclerView.NO_POSITION)
                        listener.onItemClick(getItem(getAdapterPosition()), v.getId());
                }
            });

        }
    }
}
