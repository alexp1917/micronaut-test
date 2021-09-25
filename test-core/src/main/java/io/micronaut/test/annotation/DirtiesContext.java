package io.micronaut.test.annotation;

import java.lang.annotation.*;


/**
 * If this annotation is present it means that this test class mutates the
 * application context. The context cannot be reused in other tests and must
 * be recreated.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Inherited
@Documented
public @interface DirtiesContext {
    /**
     * When to restart the context (before or after the test class runs).
     *
     * @return the restart timing (before, after, etc)
     */
    Timing timing() default Timing.AFTER;

    enum Timing {
        BEFORE() {
            public boolean before() {
                return true;
            }

            public boolean after() {
                return false;
            }
        },
        AFTER() {
            public boolean before() {
                return false;
            }

            public boolean after() {
                return true;
            }
        },
        ;

        /**
         * @return true when the test should be cleared before the test class
         */
        public abstract boolean before();
        /**
         * @return true when the test should be cleared after the test class
         */
        public abstract boolean after();
    }
}
