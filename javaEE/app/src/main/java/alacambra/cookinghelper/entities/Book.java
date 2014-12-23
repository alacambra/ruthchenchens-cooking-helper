package alacambra.cookinghelper.entities;

import javax.persistence.*;

/**
 * Created by alacambra on 23/12/14.
 */
@Entity
public class Book {

    @Id
    @GeneratedValue
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
}
