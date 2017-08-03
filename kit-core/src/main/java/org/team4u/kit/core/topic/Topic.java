package org.team4u.kit.core.topic;


import java.io.Closeable;

public interface Topic<M> extends Closeable {

    void publish(M m);

    String register(Subscriber<M> listener);

    void unregister(String id);

    void unregisterAll();

    interface Subscriber<M> {

        void onMessage(M message);
    }
}