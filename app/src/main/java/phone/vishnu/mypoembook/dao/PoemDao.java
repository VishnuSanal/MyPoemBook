package phone.vishnu.mypoembook.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import phone.vishnu.mypoembook.model.Poem;

@Dao
public interface PoemDao {

    @Insert
    void insert(Poem poem);

    @Update
    void update(Poem poem);

    @Delete
    void delete(Poem poem);

    @Query("SELECT * FROM Poem order by id desc")
    LiveData<List<Poem>> getAllPoems();
}
