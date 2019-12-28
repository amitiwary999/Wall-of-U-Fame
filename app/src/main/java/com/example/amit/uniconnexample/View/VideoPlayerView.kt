package com.example.amit.uniconnexample.View

import android.content.Context
import android.net.Uri
import android.util.AttributeSet
import android.view.View
import android.widget.FrameLayout
import android.widget.ImageButton
import com.example.amit.uniconnexample.R
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
import kotlinx.android.synthetic.main.video_player_view.view.*
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info

/**
 * Created by Meera on 28,December,2019
 */
class VideoPlayerView : FrameLayout,AnkoLogger {
    var simpleExoPlayer: SimpleExoPlayer? = null
    var play: ImageButton? = null
    var dataSourceFactory: DataSource.Factory? = null

    var loadControl: LoadControl = DefaultLoadControl.Builder().setBufferDurationsMs(
            15 * 1000,  // Min buffer size
            30 * 1000,  // Max buffer size
            500,  // Min playback time buffered before starting video
            100).createDefaultLoadControl()


    constructor(context: Context) : this(context, null){
        initialize()
    }
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0){
        //initialize()
    }
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        //initialize()
    }

    private fun initialize(){
        inflate(context, R.layout.video_player_view, this)
        play = findViewById(R.id.exo_play)
        simpleExoPlayer = ExoPlayerFactory.newSimpleInstance(context, DefaultRenderersFactory(context), DefaultTrackSelector(), loadControl)

        simpleExoPlayer!!.addListener(eventListener)
        player_view.player = simpleExoPlayer
        val bandwidthMeter: BandwidthMeter = DefaultBandwidthMeter()
        dataSourceFactory = DefaultDataSourceFactory(context, Util.getUserAgent(context, "mediaPlayerSample"), bandwidthMeter as TransferListener)
    }

    fun setData(uri: Uri, autoPlay: Boolean?) {
        simpleExoPlayer!!.playWhenReady = autoPlay!!
        val defaultExtractorsFactory = DefaultExtractorsFactory()
        defaultExtractorsFactory.setFragmentedMp4ExtractorFlags(FragmentedMp4Extractor.FLAG_WORKAROUND_IGNORE_EDIT_LISTS)
        val mediaSource = ExtractorMediaSource.Factory(dataSourceFactory).createMediaSource(uri)
        simpleExoPlayer!!.prepare(mediaSource)
        play!!.setOnClickListener { view: View? -> simpleExoPlayer!!.playWhenReady = true }
    }


    fun pause() {
        if (simpleExoPlayer != null) {
            simpleExoPlayer!!.playWhenReady = false
        }
        //playerView.pause();
    }

    fun cleanup() {
        if (simpleExoPlayer != null) {
            simpleExoPlayer!!.release()
        }
        //playerView.cleanup();
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