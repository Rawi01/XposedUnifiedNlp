package de.r3w6.xposedunifiednlp;

import android.util.Log;

/**
 * Created on 13.02.2015.
 */
public abstract class CheckStep {
    public CheckStep(String name) {
        this.name = name;
    }

    protected abstract void runStep();

    private String name;
    private StepState state = StepState.WAIT;
    private String solution;

    public String getName() {
        return name;
    }

    public StepState getState() {
        return state;
    }

    protected void setState(StepState state) {
        Log.i("CheckStep", getName() + "s state changed to " + state);
        this.state = state;
    }

    public void setSolution(String solution) {
        this.solution = solution;
    }
    public String getSolution() {
        if(solution == null)
            return "No known solution";
        return solution;
    }

    public enum StepState {
        WAIT,
        SUCCESS,
        FAIL,
        RUNNING, SKIPPED
    }
}
