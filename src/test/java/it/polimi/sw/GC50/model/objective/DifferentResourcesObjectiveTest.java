package it.polimi.sw.GC50.model.objective;

import it.polimi.sw.GC50.model.card.Resource;
import org.junit.jupiter.api.Test;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

class DifferentResourcesObjectiveTest {
    @Test
    void DifferentResourcesObjective(){
        Set<Resource> x = new HashSet<>();
        x.add(Resource.PLANT);
        DifferentResourcesObjective differentResourcesObjective = new DifferentResourcesObjective(x);
        assertEquals(x,differentResourcesObjective.getTargetResources());
    }

    @Test
    void checkCondition() {

    }
}