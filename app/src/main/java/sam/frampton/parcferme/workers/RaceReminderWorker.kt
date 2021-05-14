package sam.frampton.parcferme.workers

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import android.os.Bundle
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.navigation.NavArgument
import androidx.navigation.NavDeepLinkBuilder
import androidx.work.Worker
import androidx.work.WorkerParameters
import sam.frampton.parcferme.R
import sam.frampton.parcferme.fragments.RaceDetailFragmentArgs
import sam.frampton.parcferme.fragments.RaceDetailFragmentDirections
import sam.frampton.parcferme.fragments.RaceListFragmentArgs

class RaceReminderWorker(val context: Context, workerParams: WorkerParameters) :
    Worker(context, workerParams) {

    companion object {
        const val KEY_RACE_SEASON = "RACE_SEASON"
        const val KEY_RACE_NAME = "RACE_NAME"
        const val KEY_RACE_TIME = "RACE_TIME"
    }

    override fun doWork(): Result {
        val channelId = context.getString(R.string.channel_id_race_reminders)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = context.getString(R.string.channel_name_race_reminders)
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(channelId, name, importance)
            val notificationManager: NotificationManager =
                context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }

        val bundle = Bundle()
        bundle.putInt("season", inputData.getInt(KEY_RACE_SEASON, -1))
        val pendingIntent =
            NavDeepLinkBuilder(context)
                .setGraph(R.navigation.nav_graph)
                .setDestination(R.id.raceListFragment)
                .setArguments(bundle)
                .createPendingIntent()

        val notification = NotificationCompat.Builder(context, channelId)
            .setSmallIcon(R.drawable.ic_race)
            .setContentTitle(inputData.getString(KEY_RACE_NAME))
            .setContentText(inputData.getString(KEY_RACE_TIME))
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .build()

        NotificationManagerCompat.from(context).notify(0, notification)
        return Result.success()
    }
}