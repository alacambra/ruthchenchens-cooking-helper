package alacambra.cookinghelper.entities;

import java.util.Set;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToMany;

/**
 * Created by alacambra on 23/12/14.
 */
@Entity
public class IngredientCluster {
    @Id
    @GeneratedValue
    Integer id;

    @ManyToMany
    Set<Ingredient> ingredients;

    public Set<Ingredient> getIngredient() {
        return ingredients;
    }

    public void setIngredient(Set<Ingredient> ingredient) {
        this.ingredients = ingredient;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
}
