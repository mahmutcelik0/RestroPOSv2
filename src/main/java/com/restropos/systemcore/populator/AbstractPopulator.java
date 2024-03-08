package com.restropos.systemcore.populator;

import java.util.List;
import java.util.stream.Collectors;

public abstract class AbstractPopulator <Source,Target>{
    protected abstract Target populate(Source source, Target target);

    public Target populate(Source source) {
        return populate(source, generateTarget());
    }

    public abstract Target generateTarget();

    public List<Target> populateAll(List<Source> sourceList){
        return sourceList.stream().map(this::populate).toList();
    }
}
