package org.mightyfrog.android.s4fd.rules;

import com.raizlabs.android.dbflow.config.FlowManager;

import org.junit.rules.TestRule;
import org.junit.runner.Description;
import org.junit.runners.model.Statement;
import org.robolectric.RuntimeEnvironment;

/**
 * @author Shigehiro Soejima
 */
public class InitDBFlow implements TestRule {

    @Override
    public Statement apply(final Statement base, Description description) {
        return new Statement() {
            @Override
            public void evaluate() throws Throwable {
                FlowManager.init(RuntimeEnvironment.application);
                try {
                    base.evaluate();
                } finally {
                    FlowManager.destroy();
                }
            }
        };
    }
}