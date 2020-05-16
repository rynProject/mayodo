package com.mayodo.news.retrofit;
import com.google.gson.JsonElement;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;
public interface ApiService {

    String BASEURL = "http://mayodo.id/";

    /*getAllLatestNews used for all latest news list*/
    @GET("wp-json/wp/v2/posts?_embed")
    Call<JsonElement> getAllLatestNews(@Query("categories") String catID,@Query("page") int per_page);

    /*category() used for fetch main category list*/
    @GET("wp-json/wp/v2/categories?per_page=100&_embed")
    Call<JsonElement> category();

    /*getFeatureNews() used for featuresnews list*/
    @GET("wp-json/wp/v2/posts?_embed")
    Call<JsonElement> getFeatureNews();

    /*getNewsByCatID used for fetch news using category id*/
    @GET("wp-json/wp/v2/posts?_embed")
    Call<JsonElement> getNewsByCatID(@Query("categories") String catID);

    /*search used for search all project content*/
    @GET("wp-json/wp/v2/posts/?_embed")
    Call<JsonElement> search(@Query("search") String world);

    /*getNewsByNewsID used for get news detais*/
    @GET("wp-json/wp/v2/posts/{NewsId}?_embed")
    Call<JsonElement> getNewsByNewsID(@Path("NewsId") String newsID);

    /*getAllComment used for view all comments*/
    @GET("wp-json/wp/v2/comments")
    Call<JsonElement> getAllComment(@Query("post") String postId);

    /*addComment used for add comment in news details*/
    @FormUrlEncoded
    @POST("api/comment.php")
    Call<JsonElement> addComment(@Field("post_id") String postId,
                                 @Field("comment_author") String commentAuthor,
                                 @Field("comment_author_email") String commentAuthorEmail,
                                 @Field("comment_content") String commentContent,
                                 @Field("user_id") String userId);

    /*contactus used for add contact details */
    @FormUrlEncoded
    @POST("api/contact_us.php")
    Call<JsonElement> contactus(@Field("name") String name, @Field("email") String email, @Field("message") String message);

    @GET("wp-json/appsetting/v1/appcolor")
    Call<JsonElement> getAppColor();

    @GET("wp-json/appsetting/v1/logo")
    Call<JsonElement> getAppLogo();

    @GET("wp-json/appsetting/v1/textbanner")
    Call<JsonElement> getAdsVisibility();

}

