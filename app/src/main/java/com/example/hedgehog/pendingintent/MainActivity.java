package com.example.hedgehog.pendingintent;

import android.app.PendingIntent;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    TextView textView;
    Button button;

    public final static String WAITING = "Waiting for result";
    public final static String RECIEVED = "Result reseived";
    public final static String PARAM_PINTENT = "pendingIntent";
    public final static String PARAM_RESULT = "pendingIntent";

    public final static int STATUS_START = 100;
    public final static int STATUS_FINISH = 200;

    public static final String LOG_TAG = "asdf";

    final int TASK1_CODE = 1;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textView = (TextView) findViewById(R.id.textView);
        button = (Button) findViewById(R.id.button);

        button.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        PendingIntent pi;
        Intent intent;

        textView.setText(WAITING);
        button.setClickable(false);

        // Создаем PendingIntent для Task1
        pi = createPendingResult(TASK1_CODE, new Intent(), 0);
        // Создаем Intent для вызова сервиса, кладем туда параметр времени
        // и созданный PendingIntent
        intent = new Intent(this, MyService.class).putExtra(PARAM_PINTENT, pi);
        // стартуем сервис
        startService(intent);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == STATUS_START) {
            switch (requestCode) {
                case TASK1_CODE:
                    Log.d (LOG_TAG, "Start");
                    break;
            }
        }

        // Ловим сообщения об окончании задач
        if (resultCode == STATUS_FINISH) {
            int result = data.getIntExtra(PARAM_RESULT, 0);
            switch (requestCode) {
                case TASK1_CODE:
                    Log.d (LOG_TAG, "STOP");
                    button.setClickable(true);
                    break;
            }

            textView.setText(data.getStringExtra(PARAM_RESULT));
        }
    }
}
