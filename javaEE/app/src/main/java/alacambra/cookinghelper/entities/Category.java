package alacambra.cookinghelper.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

/**
 * Created by alacambra on 23/12/14.
 */
@Entity
public class Category {
    @Id
    @GeneratedValue
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
}

