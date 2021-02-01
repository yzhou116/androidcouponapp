package com.cvte.taobaounion.model;

import com.cvte.taobaounion.model.domain.Categories;
import com.cvte.taobaounion.model.domain.HomePagerContent;
import com.cvte.taobaounion.model.domain.OnSaleContent;
import com.cvte.taobaounion.model.domain.SearchContent;
import com.cvte.taobaounion.model.domain.SearchRecommend;
import com.cvte.taobaounion.model.domain.SelectPageCategory;
import com.cvte.taobaounion.model.domain.SelectedContent;
import com.cvte.taobaounion.model.domain.TicketResult;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;
import retrofit2.http.Url;


public interface Api {

    @GET("discovery/categories")
    Call<Categories> getCategories();

    @GET
    Call<HomePagerContent> getHomePagerContent(@Url String url);

    @POST("tpwd")
    Call<TicketResult> getTicket(@Body TicketPramas ticketPramas);

    @GET("recommend/categories")
    Call<SelectPageCategory> getSelectPageCategory();

    @GET
    Call<SelectedContent> getSelectContent(@Url String url);

    @GET
    Call<OnSaleContent> getOnSalePageContent(@Url String url);

    @GET("search/recommend")
    Call<SearchRecommend> getRecommendWord();

    @GET("search")
    Call<SearchContent> doSearch(@Query("page") int page, @Query("keyword") String keyWord);

}
