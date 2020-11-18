package phone.vishnu.mypoembook.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Canvas;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.yalantis.ucrop.UCrop;

import java.io.File;
import java.util.List;
import java.util.Objects;

import it.xabaras.android.recyclerview.swipedecorator.RecyclerViewSwipeDecorator;
import phone.vishnu.mypoembook.R;
import phone.vishnu.mypoembook.adapter.PoemRecyclerViewAdapter;
import phone.vishnu.mypoembook.fragment.AddEditFragment;
import phone.vishnu.mypoembook.fragment.CreateFragment;
import phone.vishnu.mypoembook.fragment.PresetListFragment;
import phone.vishnu.mypoembook.helper.ExportHelper;
import phone.vishnu.mypoembook.helper.SharedPreferenceHelper;
import phone.vishnu.mypoembook.model.Poem;
import phone.vishnu.mypoembook.viewmodel.PoemViewModel;

public class MainActivity extends AppCompatActivity {
    public static final String ID_EXTRA = "phone.vishnu.mypoembook.ID";
    public static final String TITLE_EXTRA = "phone.vishnu.mypoembook.TITLE";
    public static final String DESCRIPTION_EXTRA = "phone.vishnu.mypoembook.DESCRIPTION";
    public static final int PICK_IMAGE_ID = 22;

    private PoemViewModel poemViewModel;
    private PoemRecyclerViewAdapter adapter;
    private FloatingActionButton addMainFAB, addDesignPresetFAB, addPoemFAB;
    private TextView addDesignPresetHintTV, addPoemHintTV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        addMainFAB = findViewById(R.id.addMainFAB);
        addDesignPresetFAB = findViewById(R.id.addDesignPresetMiniFAB);
        addPoemFAB = findViewById(R.id.addPoemMiniFAB);

        addDesignPresetHintTV = findViewById(R.id.addDesignPresetHintTV);
        addPoemHintTV = findViewById(R.id.addPoemHintTV);

        animateInitTasks();

        addMainFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addMainFAB.animate().rotationBy(180);
                if (addDesignPresetFAB.getAlpha() == 0f && addPoemFAB.getAlpha() == 0f) {
                    openFABMenu();
                } else {
                    closeFABMenu();
                }
            }
        });

        addPoemFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                closeFABMenu();
                getSupportFragmentManager().beginTransaction().replace(R.id.container, AddEditFragment.newInstance(), "Add").addToBackStack(null).commit();
                setVisibility(false);
            }
        });

        addDesignPresetFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                closeFABMenu();
                getSupportFragmentManager().beginTransaction().replace(R.id.container, CreateFragment.newInstance(), "Create").addToBackStack(null).commit();
                setVisibility(false);
            }
        });

        setUpRecyclerView();
    }

    private void closeFABMenu() {
        addDesignPresetFAB.animate().translationY(0).alpha(0f);
        addDesignPresetHintTV.animate().translationY(0).alpha(0f);

        addPoemFAB.animate().translationY(0).alpha(0f);
        addPoemHintTV.animate().translationY(0).alpha(0f);
    }

    private void openFABMenu() {
        int i = 120;
        addDesignPresetFAB.animate().translationYBy(-i).alpha(1f);
        addDesignPresetHintTV.animate().translationYBy(-i).alpha(1f);

        addPoemFAB.animate().translationYBy(-i * 2).alpha(1f);
        addPoemHintTV.animate().translationYBy(-i * 2).alpha(1f);
    }

    private void animateInitTasks() {
        addDesignPresetHintTV.setAlpha(0f);
        addDesignPresetHintTV.setY(0);

        addPoemHintTV.setAlpha(0f);
        addPoemHintTV.setY(0);

        addDesignPresetFAB.setAlpha(0f);
        addDesignPresetFAB.setY(0);

        addPoemFAB.setAlpha(0f);
        addPoemFAB.setY(0);
    }

    private void setUpRecyclerView() {
        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);

        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));

        adapter = new PoemRecyclerViewAdapter();
        recyclerView.setAdapter(adapter);

        poemViewModel = new ViewModelProvider(this,
                new ViewModelProvider.AndroidViewModelFactory(getApplication())).get(PoemViewModel.class);
        poemViewModel.getAllPoems().observe(this, new Observer<List<Poem>>() {
            @Override
            public void onChanged(List<Poem> poems) {
                if (poems.size() == 0) {
                    findViewById(R.id.recyclerViewEmptyHintIV).setVisibility(View.VISIBLE);
                    findViewById(R.id.recyclerViewEmptyHintTV).setVisibility(View.VISIBLE);
                    openFABMenu();
                } else {
                    findViewById(R.id.recyclerViewEmptyHintIV).setVisibility(View.GONE);
                    findViewById(R.id.recyclerViewEmptyHintTV).setVisibility(View.GONE);
                }
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

        adapter.setOnItemClickListener(new PoemRecyclerViewAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Poem poem, int id) {
                if (id == R.id.todoEditIV) {
                    getSupportFragmentManager().beginTransaction().replace(R.id.container, AddEditFragment.newInstance(poem), "Edit").addToBackStack(null).commit();
                    setVisibility(false);
                } else if (id == R.id.todoGoIV) {
                    getSupportFragmentManager().beginTransaction().replace(R.id.container, PresetListFragment.newInstance(poem), "PresetList").addToBackStack(null).commit();
                    setVisibility(false);
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        if (getSupportFragmentManager().getBackStackEntryCount() > 0) {

            if (getSupportFragmentManager().getFragments().contains(getSupportFragmentManager().findFragmentByTag("ColorPick"))) {
                super.onBackPressed();
                return;
            }

            for (Fragment fragment : getSupportFragmentManager().getFragments()) {
                if (Objects.equals(fragment.getTag(), "Create") || Objects.equals(fragment.getTag(), "Edit") || Objects.equals(fragment.getTag(), "Add") || Objects.equals(fragment.getTag(), "PresetList")) {
                    setVisibility(true);
                    super.onBackPressed();
                    break;
                } else if (Objects.equals(fragment.getTag(), "ColorPick")) {
                    super.onBackPressed();
                    break;
                }
            }
        } else
            super.onBackPressed();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK && data != null) {

            String filePath = new ExportHelper(this).getBGPath();

            if (requestCode == PICK_IMAGE_ID)

                UCrop.of(data.getData(), Uri.fromFile(new File(filePath)))
                        .withAspectRatio(9, 16)
                        .withMaxResultSize(1080, 1920)
                        .start(this);

            else if (requestCode == UCrop.REQUEST_CROP) {
                new SharedPreferenceHelper(this).setBackgroundPath(filePath);
                Toast.makeText(this, "Background Set", Toast.LENGTH_SHORT).show();
            }
        } else Toast.makeText(this, "Error...", Toast.LENGTH_SHORT).show();

    }

    public void setVisibility(boolean makeVisible) {
        if (makeVisible) {
            Objects.requireNonNull(getSupportActionBar()).show();
            addMainFAB.setVisibility(View.VISIBLE);
            addPoemFAB.setVisibility(View.VISIBLE);
            addPoemHintTV.setVisibility(View.VISIBLE);
            addDesignPresetFAB.setVisibility(View.VISIBLE);
            addDesignPresetHintTV.setVisibility(View.VISIBLE);
        } else {
            Objects.requireNonNull(getSupportActionBar()).hide();
            addMainFAB.setVisibility(View.GONE);
            addPoemFAB.setVisibility(View.GONE);
            addPoemHintTV.setVisibility(View.GONE);
            addDesignPresetFAB.setVisibility(View.GONE);
            addDesignPresetHintTV.setVisibility(View.GONE);
        }
    }
}