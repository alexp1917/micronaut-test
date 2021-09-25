package io.micronaut.test.extensions.junit5;

import org.junit.platform.launcher.TestExecutionListener;
import org.junit.platform.launcher.TestPlan;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.ServiceLoader;


public class MicronautJunitTestListener implements TestExecutionListener {

    private static final List<Runnable> planStarted = new ArrayList<>();
    private static final List<Runnable> planFinished = new ArrayList<>();

    public void addStartedCallback(Runnable cb) {
        planStarted.add(cb);
    }

    public void addFinishedCallback(Runnable cb) {
        planFinished.add(cb);
    }

    @Override
    public void testPlanExecutionStarted(TestPlan testPlan) {
        planStarted.forEach(Runnable::run);
    }

    @Override
    public void testPlanExecutionFinished(TestPlan testPlan) {
        planFinished.forEach(Runnable::run);
    }

    static Optional<MicronautJunitTestListener> micronautListener() {
        for (org.junit.platform.launcher.TestExecutionListener listener :
                ServiceLoader.load(org.junit.platform.launcher.TestExecutionListener.class)) {
            if (!MicronautJunitTestListener.class.equals(Optional.ofNullable(listener)
                    .map(Object::getClass)
                    .orElse(null)))
                continue;
            return Optional.of((MicronautJunitTestListener) listener);
        }
        return Optional.empty();
    }
}
