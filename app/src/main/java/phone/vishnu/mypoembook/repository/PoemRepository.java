package phone.vishnu.mypoembook.repository;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import java.util.List;

import phone.vishnu.mypoembook.dao.PoemDao;
import phone.vishnu.mypoembook.database.PoemDatabase;
import phone.vishnu.mypoembook.model.Poem;

public class PoemRepository {

    private PoemDao poemDao;
    private LiveData<List<Poem>> poemsList;

    public PoemRepository(Application application) {
        PoemDatabase database = PoemDatabase.getInstance(application);
        poemDao = database.poemDao();
        poemsList = poemDao.getAllPoems();
    }

    public void insertPoem(Poem poem) {
        new InsertPoemAsyncTask(poemDao).execute(poem);
    }

    public void updatePoem(Poem poem) {
        new UpdatePoemAsyncTask(poemDao).execute(poem);
    }

    public void deletePoem(Poem poem) {
        new DeletePoemAsyncTask(poemDao).execute(poem);
    }

    public LiveData<List<Poem>> getAllPoems() {
        return poemsList;
    }

    private static class InsertPoemAsyncTask extends AsyncTask<Poem, Void, Void> {
        private PoemDao poemDao;

        public InsertPoemAsyncTask(PoemDao poemDao) {
            this.poemDao = poemDao;
        }

        @Override
        protected Void doInBackground(Poem... poems) {
            poemDao.insert(poems[0]);
            return null;
        }
    }

    private static class UpdatePoemAsyncTask extends AsyncTask<Poem, Void, Void> {
        private PoemDao poemDao;

        public UpdatePoemAsyncTask(PoemDao poemDao) {
            this.poemDao = poemDao;
        }

        @Override
        protected Void doInBackground(Poem... poems) {
            poemDao.update(poems[0]);
            return null;
        }
    }

    private static class DeletePoemAsyncTask extends AsyncTask<Poem, Void, Void> {
        private PoemDao poemDao;

        public DeletePoemAsyncTask(PoemDao poemDao) {
            this.poemDao = poemDao;
        }

        @Override
        protected Void doInBackground(Poem... poems) {
            poemDao.delete(poems[0]);
            return null;
        }
    }
}
