package org.team4u.command.infrastructure.executor;

public class MockCommandRequest {

    private final String name;

    public MockCommandRequest(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return "MockCommandRequest{" +
                "name='" + name + '\'' +
                '}';
    }
}
