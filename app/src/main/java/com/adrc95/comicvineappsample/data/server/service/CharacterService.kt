package com.adrc95.comicvineappsample.data.server.service

import com.adrc95.comicvineappsample.data.server.model.CharacterDataResponse
import com.adrc95.comicvineappsample.data.server.model.CharacterDetailResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface CharacterService {

    @GET("characters/")
    suspend fun getCharacters(
        @Query("limit") limit: Int,
        @Query("offset") offset: Int,
        @Query("field_list") fieldList: String = LIST_FIELDS,
        @Query("sort") sort: String = "name:asc"
    ): CharacterDataResponse

    @GET("character/4005-{id}/")
    suspend fun getCharacter(
        @Path("id") id: Long,
        @Query("field_list") fieldList: String = DETAIL_FIELDS,
    ): CharacterDetailResponse

    companion object {
        private const val LIST_FIELDS =
            "id,name,deck,image"
        private const val DETAIL_FIELDS =
            "id,name,deck,description,site_detail_url,api_detail_url,image"
    }
}
