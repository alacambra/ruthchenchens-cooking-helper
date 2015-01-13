package alacambra.cookinghelper.search;

import alacambra.cookinghelper.boundary.PaginationHelper;

import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.faces.model.DataModel;
import javax.faces.model.ListDataModel;
import javax.inject.Named;
import java.io.Serializable;
import java.util.*;

/**
 * Created by alacambra on 1/4/15.
 */
@Named("search")
@SessionScoped
public class SearchRecipeController implements Serializable{

    private PaginationHelper pagination;
    private DataModel items = null;
    private String ingredients;
    private String categories;
    private SearchType searchType;
    private boolean paginating = false;

    @EJB
    private SearchRecipeFacade facade;

    public PaginationHelper getPagination() {
        if (pagination == null) {
            pagination = new Paginator(30);
        }
        return pagination;
    }

    class Paginator extends PaginationHelper{

        public Paginator(int pageSize) {
            super(pageSize);
        }

        @Override
        public int getItemsCount() {
            return getFacade().count(facade.getCriteria(true, searchType, categories, ingredients));
        }

        @Override
        public DataModel createPageDataModel() {
            return new ListDataModel(getFacade().findRange(
                    new int[]{pagination.getPageFirstItem(),
                            pagination.getPageFirstItem() + pagination.getPageSize()},
                    facade.getCriteria(false, searchType, categories, ingredients)));
        }

    }

    public String getIngredients() {
        return ingredients;
    }

    public void setIngredients(String ingredients) {
        this.ingredients = ingredients;
    }

    public String getCategories() {
        return categories;
    }

    public void setCategories(String categories) {
        this.categories = categories;
    }

    public void createCriteriaAllORs(){
        searchType = SearchType.OR;
        searchTypeChanged();
    }

    public void createCriteriaAllANDs(){
        searchType = SearchType.AND;
        searchTypeChanged();
    }

    public void createCriteriaNOTs(){
        searchType = SearchType.NOT;
        searchTypeChanged();
    }

    public void searchTypeChanged(){
        recreateModel();
        recreatePagination();
        paginating = true;
    }

    public DataModel getItems() {
        if(!paginating) {
            return new ListDataModel(new ArrayList());
        }
        if (items == null || !items.iterator().hasNext()) {
            items = getPagination().createPageDataModel();
        }
        return items;
    }

    private void recreateModel() {
        items = null;
    }

    private void recreatePagination() {
        pagination = null;
        pagination = getPagination();
    }

    public String next() {
        getPagination().nextPage();
        recreateModel();
        return "searchresults";
    }

    public String previous() {
        getPagination().previousPage();
        recreateModel();
        return "searchresults";
    }

    public SearchRecipeFacade getFacade() {
        return facade;
    }
}
