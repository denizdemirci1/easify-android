package com.easify.easify.data.service

import com.easify.easify.model.*
import retrofit2.Response
import retrofit2.http.*

/**
 * @author: deniz.demirci
 * @date: 9/4/2020
 */

interface SpotifyService {

  companion object {
    const val BASE_URL = "https://api.spotify.com/v1/"
  }

  @GET("me")
  suspend fun fetchUser(): User

  @GET("me/player/recently-played")
  suspend fun fetchRecentlyPlayed(
      @Query("type") type: String = "track",
      @Query("limit") limit: Int = 30,
      @Query("before") before: String? = null
  ): HistoryResponse

  @PUT("me/player/play")
  suspend fun play(
    @Query("device_id") deviceId: String? = null,
    @Body playObject: PlayObject
  ): Response<Unit>

  @GET("me/player/devices")
  suspend fun getDevices(): DevicesResponse

  @GET("me/player")
  suspend fun getCurrentPlayback(): CurrentPlaybackResponse?

  @GET("me/top/artists")
  suspend fun fetchTopArtists(
    @Query("time_range") timeRange: String?,
    @Query("limit") limit: Int = 20,
    @Query("offset") offset: Int,
  ): TopArtistResponse

  @GET("me/top/tracks")
  suspend fun fetchTopTracks(
    @Query("time_range") timeRange: String?,
    @Query("limit") limit: Int = 20,
    @Query("offset") offset: Int,
  ): TopTrackResponse

  @GET("me/playlists")
  suspend fun fetchPlaylists(
    @Query("limit") limit: Int = 20,
    @Query("offset") offset: Int,
  ): PlaylistResponse

  @GET("playlists/{playlist_id}/tracks")
  suspend fun fetchPlaylistTracks(
    @Path("playlist_id") playlistId: String,
    @Query("offset") offset: Int,
  ): PlaylistTracksResponse

  @HTTP(method = "DELETE", path = "playlists/{playlist_id}/tracks", hasBody = true)
  suspend fun removeTracksFromPlaylist(
    @Path("playlist_id") playlistId: String,
    @Body removeTrackObject: RemoveTrackObject
  )

  @POST("users/{user_id}/playlists")
  suspend fun createPlaylist(
    @Path("user_id") userId: String,
    @Body createPlaylistBody: CreatePlaylistBody
  ): Playlist

  @POST("playlists/{playlist_id}/tracks")
  suspend fun addTrackToPlaylist(
    @Path("playlist_id") playlistId: String,
    @Body addTrackObject: AddTrackObject
  ): SnapshotResponse

  @GET("me/following")
  suspend fun fetchFollowedArtists(
    @Query("type") type: String = "artist",
    @Query("limit") limit: Int? = 30,
    @Query("after") next: String? = null
  ): ArtistsResponse

  @PUT("me/following")
  suspend fun followArtist(
    @Query("type") type: String? = "artist",
    @Query("ids") id: String
  )

  @DELETE("me/following")
  suspend fun unfollowArtist(
    @Query("type") type: String? = "artist",
    @Query("ids") id: String
  )

  @GET("me/tracks")
  suspend fun fetchSavedTracks(
    @Query("limit") limit: Int? = 50,
    @Query("offset") offset: Int,
  ): SavedTrackResponse

  @GET("me/tracks/contains")
  suspend fun checkSavedTracks(
    @Query("ids") id: String
  ): List<Boolean>

  @PUT("me/tracks")
  suspend fun saveTracks(
    @Query("ids") id: String
  )

  @GET("search")
  suspend fun search(
    @Query("type") type: String,
    @Query("q") q: String,
    @Query("limit") limit: Int = 50
  ): SearchResponse

  @GET("audio-features/{id}")
  suspend fun fetchAudioFeatures(
    @Path("id") id: String
  ): FeaturesResponse

  @GET("recommendations")
  suspend fun fetchRecommendations(
    @Query("seed_tracks") seedTrackId: String,
    @QueryMap queries: Map<String, Float>
  ): RecommendationsResponse
}
