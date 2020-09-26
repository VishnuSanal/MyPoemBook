package phone.vishnu.mypoembook.activity;

import android.graphics.Canvas;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;
import java.util.Objects;

import it.xabaras.android.recyclerview.swipedecorator.RecyclerViewSwipeDecorator;
import phone.vishnu.mypoembook.R;
import phone.vishnu.mypoembook.adapter.RecyclerViewAdapter;
import phone.vishnu.mypoembook.fragment.AddEditFragment;
import phone.vishnu.mypoembook.fragment.CreateFragment;
import phone.vishnu.mypoembook.model.Poem;
import phone.vishnu.mypoembook.viewmodel.PoemViewModel;

public class MainActivity extends AppCompatActivity {

    public static final String ID_EXTRA = "phone.vishnu.mypoembook.ID";
    public static final String TITLE_EXTRA = "phone.vishnu.mypoembook.TITLE";
    public static final String DESCRIPTION_EXTRA = "phone.vishnu.mypoembook.DESCRIPTION";

    private PoemViewModel poemViewModel;
    private RecyclerViewAdapter adapter;
    private FloatingActionButton floatingActionButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        Toolbar toolbar = findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);

        floatingActionButton = findViewById(R.id.addFAB);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getSupportFragmentManager().beginTransaction().replace(R.id.container, AddEditFragment.newInstance()).addToBackStack(null).commit();
                setVisibility(false);
            }
        });

        setUpRecyclerView();
    }

    private void setUpRecyclerView() {
        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);

        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));

        adapter = new RecyclerViewAdapter();
        recyclerView.setAdapter(adapter);

        poemViewModel = new ViewModelProvider(this,
                new ViewModelProvider.AndroidViewModelFactory(getApplication())).get(PoemViewModel.class);
        poemViewModel.getAllPoems().observe(this, new Observer<List<Poem>>() {
            @Override
            public void onChanged(List<Poem> poems) {
                adapter.submitList(poems);
            }
        });

        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {

                new RecyclerViewSwipeDecorator.Builder(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
                        .addBackgroundColor(ContextCompat.getColor(MainActivity.this, R.color.redColor))
                        .addActionIcon(R.drawable.ic_delete)
                        .create()
                        .decorate();

                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
            }

            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull final RecyclerView.ViewHolder viewHolder, int direction) {

                poemViewModel.delete(adapter.getPoem(viewHolder.getAdapterPosition()));
            }
        }).attachToRecyclerView(recyclerView);

        adapter.setOnItemClickListener(new RecyclerViewAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Poem poem, int id) {

                switch (id) {
                    case R.id.todoEditIV: {

                        getSupportFragmentManager().beginTransaction().replace(R.id.container, AddEditFragment.newInstance(poem)).addToBackStack(null).commit();
                        setVisibility(false);
                        break;
                    }
                    case R.id.todoGoIV: {
                        getSupportFragmentManager().beginTransaction().replace(R.id.container, CreateFragment.newInstance(poem)).addToBackStack(null).commit();
                        break;
                    }

                    default: {
                        break;
                    }
                }

            }
        });
    }

    public void setVisibility(boolean makeVisible) {
        if (makeVisible) {
            Objects.requireNonNull(getSupportActionBar()).show();
            floatingActionButton.setVisibility(View.VISIBLE);

//            recyclerView.requestFocus();
        } else {
            Objects.requireNonNull(getSupportActionBar()).hide();
            floatingActionButton.setVisibility(View.GONE);
        }
    }
}