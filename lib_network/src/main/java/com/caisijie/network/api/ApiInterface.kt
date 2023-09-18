package com.caisijie.network.api

import com.caisijie.common.model.ArticleList
import com.caisijie.common.model.Banner
import com.caisijie.common.model.CategoryItem
import com.caisijie.common.model.HotSearch
import com.caisijie.common.model.ProjectSubList
import com.caisijie.common.model.ProjectTabItem
import com.caisijie.common.model.SystemList
import com.caisijie.common.model.User
import com.caisijie.network.repository.BaseRepository
import com.caisijie.network.response.BaseResponse
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

/**
 * @author caisijie
 * @date 2023/9/18 11:41
 * @desc API接口，统一封装Retrofit所需要的接口
 */
interface ApiInterface {
    /**
     * 首页轮播图
     */
    @GET("/banner/json")
    suspend fun getHomeBanner(): BaseResponse<MutableList<Banner>>?

    /**
     * 首页资讯
     * @param page 页码
     * @param pageSize 每页数量
     */
    @GET("/article/list/{page}/json")
    suspend fun getHomeList(
        @Path("page") page: Int,
        @Query("page_size") pageSize: Int
    ): BaseResponse<ArticleList>?

    /**
     * 首页项目
     */
    @GET("/project/tree/json")
    suspend fun getProjectTab(): BaseResponse<MutableList<ProjectTabItem>>?

    /**
     * 项目二级列表
     * @param page 分页数量
     * @param cid 项目分类的id
     */
    @GET("/project/list/{page}/json")
    suspend fun getProjectList(
        @Path("page") page: Int,
        @Query("cid") cid: Int
    ): BaseResponse<ProjectSubList>?

    /**
     * 分类列表
     */
    @GET("/navi/json")
    suspend fun getCategoryData(): BaseResponse<MutableList<CategoryItem>>?

    /**
     * 获取体系列表
     */
    @GET("/tree/json")
    suspend fun getSystemList(): BaseResponse<MutableList<SystemList>>?

    /**
     * 项目二级列表
     * @param page 分页数量
     * @param cid 项目分类的id
     */
    @GET("/article/list/{page}/json")
    suspend fun getArticleList(
        @Path("page") page: Int,
        @Query("cid") cid: Int
    ): BaseResponse<ArticleList?>?

    /**
     * 登录
     * @param username 用户名
     * @param password 密码
     */
    //@Field 注解必须与 @FormUrlEncoded 注解一起使用，以指示请求的数据将以表单编码形式进行传输。
    @FormUrlEncoded
    @POST("/user/login")
    suspend fun login(
        //@Field 注解用于指定表单字段的参数，用于将数据作为表单字段发送到服务器。
        // 常用于 POST 请求中，以表单形式提交数据。
        @Field("username") username: String,
        @Field("password") password: String,
    ): BaseResponse<User?>?

    /**
     * 注册
     * @param username 用户名
     * @param password 密码
     * @param repassword 确认密码
     */
    @FormUrlEncoded
    @POST("/user/register")
    suspend fun register(
        @Field("username") username: String,
        @Field("password") password: String,
        @Field("repassword") repassword: String
    ): BaseResponse<User?>?

    /**
     * 退出登录
     */
    @GET("/user/logout/json")
    suspend fun logout(): BaseResponse<Any?>?

    /**
     * 我的收藏列表
     * @param page 页码
     */
    @GET("/lg/collect/list/{page}/json")
    suspend fun getCollectList(
        @Path("page") page: Int
    ): BaseResponse<ArticleList?>?

    /**
     * 收藏站内文章
     * @param id 文章id
     */
    @POST("lg/collect/{id}/json")
    suspend fun collectArticle(
        @Path("id") id: Int
    ): BaseResponse<Any?>?

    /**
     * 收藏站外文章
     * @param title 标题
     * @param author 作者
     * @param link 链接
     */
    @POST("lg/collect/add/json")
    @FormUrlEncoded
    suspend fun collectOutsizeArticle(
        @Field("title") title: String,
        @Field("author") author: String,
        @Field("link") link: String
    ): BaseResponse<Any>?

    /**
     * 文章列表中取消收藏文章
     * @param id
     */
    @POST("lg/uncollect_originId/{id}/json")
    suspend fun cancelCollectArticle(
        @Path("id") id: Int
    ): BaseResponse<Any>?

    /**
     * 收藏列表中取消收藏文章
     * @param id
     * @param originId 收藏之前那篇文章本身的id
     */
    @POST("lg/uncollect/{id}/json")
    @FormUrlEncoded
    suspend fun cancelMyCollect(
        @Path("id") id: Int,
        @Field("originId") originId: Int = -1
    ): BaseResponse<Any>?

    /**
     * 搜索结果
     * @param page 页码
     * @param keyword 关键词，支持多个，空格分开
     */
    @POST("article/query/{page}/json")
    @FormUrlEncoded
    suspend fun searchResult(
        @Path("page") page: Int,
        @Field("k") keyWord: String
    ): BaseResponse<ArticleList>?

    /**
     * 搜索热词
     */
    @GET("hotkey/json")
    suspend fun getHotSearchData(): BaseResponse<MutableList<HotSearch>>
}
