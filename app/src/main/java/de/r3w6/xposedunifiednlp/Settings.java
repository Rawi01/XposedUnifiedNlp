package de.r3w6.xposedunifiednlp;

import android.app.Activity;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;


public class Settings extends Activity {

    private List<CheckStep> steps = new ArrayList<>();
    private TextView logTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        logTextView = (TextView) findViewById(R.id.textView);

        final Button button = (Button) findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                logTextView.setText("");
                startCheck();
            }
        });

        initCheckSteps();
    }

    private void initCheckSteps() {
        steps.add(new PackageCheckStep(this));
        steps.add(new NetworkLocationCheckStep(this));
        steps.add(new LocationCheckStep(this));
    }

    private void startCheck() {
        final Button button = (Button) findViewById(R.id.button);
        button.setEnabled(false);
        button.setText(R.string.button_check_settings_checking);
        AsyncTask<CheckStep, CheckStep, Boolean> checkWorkingTask = new AsyncTask<CheckStep, CheckStep, Boolean>() {
            @Override
            protected Boolean doInBackground(CheckStep... steps) {

                for (CheckStep step : steps) {
                    step.setState(CheckStep.StepState.RUNNING);
                    //publishProgress(step);
                    step.runStep();
                    publishProgress(step);
                    if(step.getState() == CheckStep.StepState.FAIL) {
                        return false;
                    }

                }
                return true;
            }

            @Override
            protected void onProgressUpdate(CheckStep... values) {
                CheckStep step = values[0];
                if(step.getState() == CheckStep.StepState.SUCCESS) {
                    Spannable toAppend = new SpannableString("Check step \"" + step.getName() + "\" finished successful.\n");
                    toAppend.setSpan(new ForegroundColorSpan(Color.GREEN), 0, toAppend.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    logTextView.append(toAppend);
                } else {
                    Spannable toAppend = new SpannableString("Check step \"" + step.getName() + "\" failed.\n" + step.getSolution());
                    toAppend.setSpan(new ForegroundColorSpan(Color.RED), 0, toAppend.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    logTextView.append(toAppend);
                }

                Log.d("CheckWorkingTask", values[0].getName() + ": " + values[0].getState());
            }

            @Override
            protected void onPostExecute(Boolean success) {
                if(success)
                    logTextView.append("Everything seems to be fine");

                button.setText(R.string.button_check_settings);
                button.setEnabled(true);
            }
        };
        checkWorkingTask.executeOnExecutor(Executors.newSingleThreadExecutor(), steps.toArray(new CheckStep[steps.size()]));
    }

}
