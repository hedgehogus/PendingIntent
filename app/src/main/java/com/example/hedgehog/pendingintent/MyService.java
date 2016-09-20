package com.example.hedgehog.pendingintent;

import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import java.util.concurrent.TimeUnit;

public class MyService extends Service {

    public static final String LOG_TAG = "asdf";

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        /// !!! USING PARCELABLE
        PendingIntent pi = intent.getParcelableExtra(MainActivity.PARAM_PINTENT);
        //навпаки в Pending Intent вкладаєм Intent(див в MyThread)

        Thread thread = new MyThread(pi);
        thread.start();

        return START_STICKY;
    }

    class MyThread extends Thread {
        PendingIntent pi;

        public MyThread ( PendingIntent pi){
            this.pi = pi;
        }

        public void run() {
            Intent intent;
            intent = new Intent().putExtra(MainActivity.PARAM_RESULT, MainActivity.WAITING);
            try {
                pi.send(getApplicationContext(), MainActivity.STATUS_START, intent );
            } catch (PendingIntent.CanceledException e) {
                e.printStackTrace();
            }
            for (int i = 0; i < 10; i++) {
                try {
                    TimeUnit.SECONDS.sleep(1);
                } catch (InterruptedException e) {

                }
                Log.d(LOG_TAG, " " + i);
            }
            intent = new Intent().putExtra(MainActivity.PARAM_RESULT, MainActivity.RECIEVED);
            try {
                pi.send(getApplicationContext(), MainActivity.STATUS_FINISH, intent);
            } catch (PendingIntent.CanceledException e) {
                e.printStackTrace();
            }

            stopSelf();
        }
    }

}
