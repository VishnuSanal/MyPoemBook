package phone.vishnu.mypoembook.database;

import android.content.Context;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import phone.vishnu.mypoembook.dao.PoemDao;
import phone.vishnu.mypoembook.model.Poem;


@Database(entities = {Poem.class}, version = 1)
public abstract class PoemDatabase extends RoomDatabase {

    private static PoemDatabase instance;
    private static RoomDatabase.Callback callback = new Callback() {
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);
            new PopulateDBAsyncTask(instance).execute();
        }
    };

    public static synchronized PoemDatabase getInstance(Context context) {

        if (instance == null) {

            instance = Room.databaseBuilder(context.getApplicationContext(), PoemDatabase.class, "poem_database")
                    .fallbackToDestructiveMigration()
                    .addCallback(callback)
                    .build();
        }

        return instance;
    }

    public abstract PoemDao poemDao();

    private static class PopulateDBAsyncTask extends AsyncTask<Void, Void, Void> {
        private PoemDao poemDao;

        public PopulateDBAsyncTask(PoemDatabase database) {
            this.poemDao = database.poemDao();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            return null;
        }
    }
}
