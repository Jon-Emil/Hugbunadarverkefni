package is.hi.hbv501g.hbv1.extras;

import java.util.List;

/**
 * This is a simple wrapper class for the search criteria in our API
 */

public class SearchCriteria {
    private String title;
    private Float minPrice;
    private Float maxPrice;
    private String releaseDateFrom;
    private String releaseDateTo;
    private String developer;
    private String publisher;
    private List<String> genres;

    public SearchCriteria(String title, Float minPrice, Float maxPrice, String releaseDateFrom, String releaseDateTo, String developer, String publisher, List<String> genres) {
        this.title = title;
        this.minPrice = minPrice;
        this.maxPrice = maxPrice;
        this.releaseDateFrom = releaseDateFrom;
        this.releaseDateTo = releaseDateTo;
        this.developer = developer;
        this.publisher = publisher;
        this.genres = genres;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Float getMinPrice() {
        return minPrice;
    }

    public void setMinPrice(Float minPrice) {
        this.minPrice = minPrice;
    }

    public Float getMaxPrice() {
        return maxPrice;
    }

    public void setMaxPrice(Float maxPrice) {
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

    public List<String> getGenres() {
        return genres;
    }

    public void setGenres(List<String> genres) {
        this.genres = genres;
    }
}
