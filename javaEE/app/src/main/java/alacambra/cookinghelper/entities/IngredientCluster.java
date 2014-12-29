package alacambra.cookinghelper.entities;

import java.util.Set;
import javax.persistence.*;

/**
 * Created by alacambra on 23/12/14.
 */
@Entity
//@NamedQueries({
//        @NamedQuery(name = "ingredientCluster.getByIsbn", query = "from IngredientCluster where isbn = :isbn")
//})
public class IngredientCluster {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
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
