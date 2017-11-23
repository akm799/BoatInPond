package uk.co.akm.test.sim.boatinpond;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    private TextView textView;
    private Button commandButton;
    private FunkyTimer funkyTimer;
    private RenderRunnable renderRunnable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textView = findViewById(R.id.number_txt);
        commandButton = findViewById(R.id.command_btn);

        renderRunnable = new RenderRunnable(textView);
    }

    void renderNumber(String number) {
        renderRunnable.setText(number);
        runOnUiThread(renderRunnable);
    }

    public void onCommand(View view) {
        if (funkyTimer != null && funkyTimer.isRunning()) {
            funkyTimer.terminate();
            funkyTimer = null;
            commandButton.setText("Start");
        } else {
            textView.setText("0");
            commandButton.setText("Stop");
            funkyTimer = new FunkyTimer(this);
            funkyTimer.initiate();
        }
    }

    @Override
    public void onPause() {
        super.onPause();

        if (funkyTimer != null && funkyTimer.isRunning()) {
            funkyTimer.terminate();
            funkyTimer = null;
        }
     }

    private static final class RenderRunnable implements Runnable {
        private final TextView textView;

        private String text;

        RenderRunnable(TextView textView) {
            this.textView = textView;
        }

        void setText(String text) {
            this.text = text;
        }

        @Override
        public void run() {
            textView.setText(text);
        }
    }

    private static final class FunkyTimer extends Thread {
        private final MainActivity parent;

        private int number;
        private boolean running;

        FunkyTimer(MainActivity parent) {
            this.parent = parent;
        }

        void initiate() {
            running = true;
            start();
        }

        boolean isRunning() {
            return running;
        }

        @Override
        public void run() {
            while (running) {
                try {
                    sleep(1000);
                } catch (InterruptedException ie) {
                    running = false;
                }

                if (running) {
                    number++;
                    parent.renderNumber(Integer.toString(number));
                }
            }

            Log.d("FunkyTimer", "FunkyTimer finished.");
        }

        void terminate() {
            running = false;
            interrupt();
        }
    }
}
