package alacambra.cookinghelper.category;

import javax.persistence.*;

/**
 * Created by alacambra on 23/12/14.
 */
@Entity
@NamedQueries({
        @NamedQuery(name = "category.getByName", query = "select c from Category as c where c.name = :name")
})
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    Integer id;

    @Column(unique = true)
    String name;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }
}

