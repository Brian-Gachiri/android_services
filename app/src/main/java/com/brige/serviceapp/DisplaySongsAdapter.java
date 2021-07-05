package com.brige.serviceapp;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import static com.brige.serviceapp.MainActivity.CHANNEL_ID;

public class DisplaySongsAdapter extends RecyclerView.Adapter<DisplaySongsAdapter.ViewHolder> {

    private ArrayList<Song> songsList ;
    private Context mContext;
    Boolean playBack = false;

    public DisplaySongsAdapter(Context context, ArrayList<Song> songsList) {
        this.songsList = songsList;
        this.mContext = context;
    }

    @NonNull
    @Override
    public DisplaySongsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.song_list_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DisplaySongsAdapter.ViewHolder holder, int position) {
        Song song = songsList.get(position);

        holder.songName.setText(song.getName());
        holder.mCurrentPosition = position;
        holder.name = song.getName();

    }

    @Override
    public int getItemCount() {
        return songsList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView songName;
        String name;
        public int mCurrentPosition;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            songName = itemView.findViewById(R.id.song_title);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    long songID = songsList.get(mCurrentPosition).getLong();
                    playSong(songID);
                }
            });



        }

    }

    public void stopSong(long id){
        Intent startIntent = new Intent(mContext, MyService.class);
        mContext.stopService(startIntent);
        playBack = false;

    }

    public void playSong(long id){
        if (playBack){
            stopSong(id);
        }
        Intent startIntent = new Intent(mContext, MyService.class);
        startIntent.putExtra("longID", id);
        mContext.startService(startIntent);

        playBack = true;
        showNotification();

    }

    public void showNotification(){
        // Create an explicit intent for an Activity in your app
        Intent intent = new Intent(mContext, MainActivity.class);

        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(mContext, (int) System.currentTimeMillis(), intent, 0);

        Intent stopAction = new Intent(mContext,MyBroadcastReceiver.class);
        PendingIntent stoppendingIntent = PendingIntent.getBroadcast(mContext, (int) System.currentTimeMillis(), stopAction, PendingIntent.FLAG_CANCEL_CURRENT);

        Intent playAudio = new Intent(mContext, MyService.class);
        intent.putExtra("longID",songsList.get(0).getLong());
        PendingIntent playAudioIntent = PendingIntent.getService(mContext,(int) System.currentTimeMillis(), playAudio, 0 );

        NotificationCompat.Builder builder = new NotificationCompat.Builder(mContext, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_music_note)
                .setContentTitle("My notification")
                .setContentText("Hello World!")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                // Set the intent that will fire when the user taps the notification
                .addAction(R.drawable.ic_mic_white, "Stop",
                       stoppendingIntent)
                .addAction(R.drawable.ic_mic_white, "Play",
                        playAudioIntent)
                .setContentIntent(pendingIntent);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(mContext);

// notificationId is a unique int for each notification that you must define
        notificationManager.notify(1, builder.build());
    }
}
