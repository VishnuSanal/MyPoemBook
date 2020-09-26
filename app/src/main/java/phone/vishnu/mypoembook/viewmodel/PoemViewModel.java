package phone.vishnu.mypoembook.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

import phone.vishnu.mypoembook.model.Poem;
import phone.vishnu.mypoembook.repository.PoemRepository;

public class PoemViewModel extends AndroidViewModel {

    private PoemRepository repository;
    private LiveData<List<Poem>> allPoems;

    public PoemViewModel(@NonNull Application application) {
        super(application);
        repository = new PoemRepository(application);
        allPoems = repository.getAllPoems();
    }

    public void insert(Poem poem) {
        repository.insertPoem(poem);
    }

    public void delete(Poem poem) {
        repository.deletePoem(poem);
    }

    public void update(Poem poem) {
        repository.updatePoem(poem);
    }

    public LiveData<List<Poem>> getAllPoems() {
        return allPoems;
    }
}
