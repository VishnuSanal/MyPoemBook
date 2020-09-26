package phone.vishnu.mypoembook.model;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity()
public class Poem {

    @PrimaryKey(autoGenerate = true)
    private int id;

    private String title, description;

    public Poem() {
    }

    public Poem(int id, String title, String description) {
        this.id = id;
        this.title = title;
        this.description = description;
    }

    public Poem(String title, String description) {
        this.title = title;
        this.description = description;
    }

    @Override
    public String toString() {
        return "Poem{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                '}';
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
