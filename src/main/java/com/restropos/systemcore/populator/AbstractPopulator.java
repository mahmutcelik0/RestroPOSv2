package com.restropos.systemcore.populator;

public abstract class AbstractPopulator <Source,Target>{
    protected abstract Target populate(Source source, Target target);

    public Target populate(Source source) {
        return populate(source, generateTarget());
    }

    public abstract Target generateTarget();
}
