package com.example.amit.uniconnexample.View

import android.content.Context
import android.net.Uri
import android.widget.ImageButton
import androidx.lifecycle.LifecycleObserver
import com.google.android.exoplayer2.*
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory
import com.google.android.exoplayer2.extractor.mp4.FragmentedMp4Extractor
import com.google.android.exoplayer2.source.ExtractorMediaSource
import com.google.android.exoplayer2.source.TrackGroupArray
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector
import com.google.android.exoplayer2.trackselection.TrackSelectionArray
import com.google.android.exoplayer2.ui.PlayerView
import com.google.android.exoplayer2.upstream.*
import com.google.android.exoplayer2.util.Util

/**
 * Created by Meera on 01,January,2020
 */
class VideoPlayerViewRecycler(context: Context): LifecycleObserver {
    var simpleExoPlayer: SimpleExoPlayer? = null
    var play: ImageButton? = null
    var dataSourceFactory: DataSource.Factory? = null
    var loadControl: LoadControl
    init {
        loadControl = DefaultLoadControl.Builder().setBufferDurationsMs(
                15 * 1000,  // Min buffer size
                30 * 1000,  // Max buffer size
                500,  // Min playback time buffered before starting video
                100).createDefaultLoadControl()
        simpleExoPlayer = ExoPlayerFactory.newSimpleInstance(context, DefaultRenderersFactory(context), DefaultTrackSelector(), loadControl)
        val bandwidthMeter: BandwidthMeter = DefaultBandwidthMeter()
        dataSourceFactory = DefaultDataSourceFactory(context, Util.getUserAgent(context, "mediaPlayerSample"), bandwidthMeter as TransferListener)
        val defaultExtractorsFactory = DefaultExtractorsFactory()
        defaultExtractorsFactory.setFragmentedMp4ExtractorFlags(FragmentedMp4Extractor.FLAG_WORKAROUND_IGNORE_EDIT_LISTS)
    }

    fun setData(uri: Uri?, autoPlay: Boolean, playerView:PlayerView){
        simpleExoPlayer!!.addListener(eventListener)
        playerView.player = simpleExoPlayer
        simpleExoPlayer!!.playWhenReady = autoPlay!!
        val mediaSource = ExtractorMediaSource.Factory(dataSourceFactory).createMediaSource(uri)
        simpleExoPlayer!!.prepare(mediaSource)
    }

    var eventListener: Player.EventListener = object : Player.EventListener {
        override fun onTimelineChanged(timeline: Timeline, manifest: Any?, reason: Int) {}
        override fun onTracksChanged(trackGroups: TrackGroupArray, trackSelections: TrackSelectionArray) {}
        override fun onLoadingChanged(isLoading: Boolean) {}
        override fun onPlayerStateChanged(playWhenReady: Boolean, playbackState: Int) {
            when (playbackState) {
                Player.STATE_ENDED -> {
                    simpleExoPlayer!!.seekTo(0)
                    simpleExoPlayer!!.playWhenReady = false
                }
                Player.STATE_READY -> {
                }
                Player.STATE_BUFFERING -> {
                }
                Player.STATE_IDLE -> {
                }
            }
        }

        override fun onRepeatModeChanged(repeatMode: Int) {}
        override fun onShuffleModeEnabledChanged(shuffleModeEnabled: Boolean) {}
        override fun onPlayerError(error: ExoPlaybackException) {}
        override fun onPositionDiscontinuity(reason: Int) {}
        override fun onPlaybackParametersChanged(playbackParameters: PlaybackParameters) {}
        override fun onSeekProcessed() {}
    }
}