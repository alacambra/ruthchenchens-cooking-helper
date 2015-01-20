package alacambra.cookinghelper.search;

import alacambra.cookinghelper.boundary.PaginationHelper;

import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.faces.model.DataModel;
import javax.faces.model.ListDataModel;
import javax.inject.Named;
import javax.persistence.criteria.CriteriaQuery;
import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by alacambra on 1/20/15.
 */
@Named("simpleSearch")
@SessionScoped
public class SimpleSearchController implements Serializable{

    private PaginationHelper pagination;
    private DataModel items = null;

    private String searchString;
    private Long searchId;

    private SearchBy searchBy;
    private boolean paginating = false;

    @EJB
    private SimpleSearchFacade facade;

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

            CriteriaQuery cq;

            if(searchBy == SearchBy.ALL){
                cq =  facade.getCriteria(true, searchString);
            }else{
                cq = facade.getCriteria(true, searchBy, searchId);
            }
            return getFacade().count(cq);
        }

        @Override
        public DataModel createPageDataModel() {

            CriteriaQuery cq;

            if(searchBy == SearchBy.ALL){
                cq =  facade.getCriteria(false, searchString);
            }else{
                cq = facade.getCriteria(false, searchBy, searchId);
            }

            return new ListDataModel(getFacade().findRange(
                    new int[]{pagination.getPageFirstItem(),
                            pagination.getPageFirstItem() + pagination.getPageSize()}, cq));
        }

    }

    public Long getSearchId() {
        return searchId;
    }

    public void setSearchId(Long searchId) {
        this.searchId = searchId;
    }

    public String getSearchString() {
        return searchString;
    }

    public void setSearchString(String searchString) {
        this.searchString = searchString;
    }

    public void setSearchBy(SearchBy searchBy) {
        this.searchBy = searchBy;
        recreateModel();
        recreatePagination();
        paginating = true;
    }

    public String searchAll() {
        this.setSearchBy(SearchBy.ALL);
        return "/search/simplesearch";
    }

    public SearchBy getSearchBy() {
        return searchBy;
    }

//    public void searchTypeChanged(){
//        recreateModel();
//        recreatePagination();
//        paginating = true;
//    }

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
        return "simplesearch";
    }

    public String previous() {
        getPagination().previousPage();
        recreateModel();
        return "simplesearch";
    }

    public SimpleSearchFacade getFacade() {
        return facade;
    }

}
