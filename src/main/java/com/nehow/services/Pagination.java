package com.nehow.services;

import com.nehow.models.HotelAvailability;
import com.nehow.models.HotelSearchResponse;
import net.sf.json.JSONObject;

import java.util.Arrays;
import java.util.List;

/**
 * Created by Igbalajobi Jamiu Okunade on 4/21/17.
 */
public class Pagination {

    /** Member identifier for the current page number */
    private int currentPageNo;

    /** Member identifier for the current start page number in the page navigation */
    private int currentStartPageNo;

    /** Member identifier for the number of elements on a page */
    private int elementsPerPage;

    /** Member identifier for the number of pages you have in the navigation (i.e 2 to  11 or 3 to 12 etc.) */
    private int pageNumberInNavigation;

    public JSONObject query;

    private HotelSearchResponse hotelSearchResponse;

    public Pagination() {
    }

    public Pagination(HotelSearchResponse hotelSearchResponse) {
        this.hotelSearchResponse = hotelSearchResponse;
    }

    public JSONObject getPaginateObject(){
        JSONObject query = new JSONObject();
        int from = ((currentPageNo - 1) * elementsPerPage);
        query.put("start", from);
        query.put("length", elementsPerPage);
        this.query = query;
        return this.query;
    }

    public void setCurrentPageNo(int currentPageNo) {
        this.currentPageNo = currentPageNo;
    }

    public void setElementsPerPage(int elementsPerPage) {
        this.elementsPerPage = elementsPerPage;
    }
}
