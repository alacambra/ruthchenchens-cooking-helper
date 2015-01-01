package alacambra.cookinghelper.entities;

import javax.persistence.*;
import static alacambra.cookinghelper.entities.QueryKeys.*;

/**
 * Created by alacambra on 23/12/14.
 */
@Entity
@NamedQueries({
        @NamedQuery(name = getBookByIsbn , query = "from Book where isbn = :isbn")
})
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    Integer id;

    @Column(unique = true)
    String isbn;

    String title;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    @Override
    public String toString() {
        return title;
    }
}
