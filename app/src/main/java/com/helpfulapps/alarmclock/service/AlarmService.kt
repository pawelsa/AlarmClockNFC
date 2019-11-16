package com.helpfulapps.alarmclock.service

import android.app.*
import android.content.Context
import android.content.Intent
import android.media.AudioAttributes
import android.media.AudioManager.STREAM_ALARM
import android.media.MediaPlayer
import android.net.Uri
import android.os.Build
import android.os.IBinder
import android.util.Log
import androidx.core.app.NotificationCompat
import com.helpfulapps.alarmclock.R
import com.helpfulapps.alarmclock.views.ringing_alarm.RingingAlarmActivity
import com.helpfulapps.domain.use_cases.alarm.GetAlarmUseCase
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.plusAssign
import io.reactivex.rxkotlin.subscribeBy
import org.koin.android.ext.android.inject


class AlarmService : Service() {

    private val TAG = AlarmService::class.java.simpleName

    private val getAlarmUseCase: GetAlarmUseCase by inject()

    private val mMediaPlayer: MediaPlayer by lazy {
        MediaPlayer()
    }
    private val disposable = CompositeDisposable()


    override fun onBind(intent: Intent): IBinder? = null

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {

        if (intent?.action == "STOP") {
            mMediaPlayer.stop()
            mMediaPlayer.release()
            stopSelf()
        } else {
            val alarmId = intent?.getIntExtra("ALARM_ID", -1) ?: -1

            if (alarmId != -1) {

                disposable += getAlarmUseCase(GetAlarmUseCase.Params(alarmId.toLong())).subscribeBy(
                    onSuccess = {
                        val alert = Uri.parse(it.alarm.ringtoneUrl)
                        setupMediaPlayer(alert)
                    },
                    onError = {
                        it.printStackTrace()
                        Log.e(TAG, it.message ?: "")
                    }
                )

                val notification = createNotification(alarmId)

                startForeground(1, notification)
            } else {
                stopSelf()
            }
        }
        return START_STICKY
    }

    fun setupMediaPlayer(ringtoneUri: Uri) {
        with(mMediaPlayer) {
            setDataSource(this@AlarmService, ringtoneUri)

            setAudioAttributes(
                AudioAttributes.Builder()
                    .setFlags(AudioAttributes.FLAG_AUDIBILITY_ENFORCED)
                    .setLegacyStreamType(STREAM_ALARM)
                    .setUsage(AudioAttributes.USAGE_ALARM)
                    .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                    .build()
            )
            isLooping = true
            setOnPreparedListener {
                it.start()
            }
            prepareAsync()
        }
    }

    fun createNotification(alarmId: Int): Notification {
        val fullScreenIntent = Intent(this, RingingAlarmActivity::class.java).also {
            it.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            it.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
            it.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            it.putExtra("EXIT", true)
            it.putExtra("ALARM_ID", alarmId)
        }
        val fullScreenPendingIntent = PendingIntent.getActivity(
            this, 0,
            fullScreenIntent, PendingIntent.FLAG_UPDATE_CURRENT
        )

        val notificationBuilder =
            NotificationCompat.Builder(this, "One")
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle("Incoming call $alarmId")
                .setContentText("(919) 555-1234")
                .setPriority(NotificationCompat.PRIORITY_MAX)
                .setCategory(NotificationCompat.CATEGORY_ALARM)
                .setFullScreenIntent(fullScreenPendingIntent, true)
                .setVibrate(LongArray(3) { 500L })


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            // Create the NotificationChannel
            val name = "Test channel"
            val descriptionText = "Test channel description"
            val importance = NotificationManager.IMPORTANCE_HIGH
            val mChannel = NotificationChannel("One", name, importance)
            mChannel.description = descriptionText
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            notificationManager.createNotificationChannel(mChannel)
        }

        notificationBuilder.setChannelId("One")

        return notificationBuilder.build()
    }

    override fun onDestroy() {
        mMediaPlayer.release()
        disposable.clear()
        super.onDestroy()
    }
}
