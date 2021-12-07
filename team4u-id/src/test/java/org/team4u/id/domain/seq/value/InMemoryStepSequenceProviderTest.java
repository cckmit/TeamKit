package org.team4u.id.domain.seq.value;


public class InMemoryStepSequenceProviderTest extends AbstractStepSequenceProviderTest {

    @Override
    protected StepSequenceProvider provider(StepSequenceProvider.Config config) {
        return new InMemoryStepSequenceProvider(config);
    }
}