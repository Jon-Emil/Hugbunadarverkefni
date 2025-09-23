package is.hi.hbv501g.hbv1.extras;

import java.util.List;

/**
 * This is a simple wrapper class for the search criteria in our API
 */

public class SearchCriteria {
    private String title;
    private float minPrice;
    private float maxPrice;
    private String releaseDateFrom;
    private String releaseDateTo;
    private String developer;
    private String publisher;

    // we need to implement Genre Constructor, GETTERs and SETTERs will be semi implemented meanwhile
    // private genres List<Genre>;

    public SearchCriteria(String title, float minPrice, float maxPrice, String releaseDateFrom, String releaseDateTo, String developer, String publisher) {
        this.title = title;
        this.minPrice = minPrice;
        this.maxPrice = maxPrice;
        this.releaseDateFrom = releaseDateFrom;
        this.releaseDateTo = releaseDateTo;
        this.developer = developer;
        this.publisher = publisher;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public float getMinPrice() {
        return minPrice;
    }

    public void setMinPrice(float minPrice) {
        this.minPrice = minPrice;
    }

    public float getMaxPrice() {
        return maxPrice;
    }

    public void setMaxPrice(float maxPrice) {
        this.maxPrice = maxPrice;
    }

    public String getReleaseDateFrom() {
        return releaseDateFrom;
    }

    public void setReleaseDateFrom(String releaseDateFrom) {
        this.releaseDateFrom = releaseDateFrom;
    }

    public String getReleaseDateTo() {
        return releaseDateTo;
    }

    public void setReleaseDateTo(String releaseDateTo) {
        this.releaseDateTo = releaseDateTo;
    }

    public String getDeveloper() {
        return developer;
    }

    public void setDeveloper(String developer) {
        this.developer = developer;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }
}
