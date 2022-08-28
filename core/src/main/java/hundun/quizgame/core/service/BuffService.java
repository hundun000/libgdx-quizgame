package hundun.quizgame.core.service;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import hundun.quizgame.core.exception.NotFoundException;
import hundun.quizgame.core.model.domain.buff.BuffRuntimeModel;
import hundun.quizgame.core.prototype.buff.BuffPrototype;


/**
 * @author hundun
 * Created on 2020/05/25
 */
public class BuffService {
    
    private  Map<String, BuffPrototype> buffPrototypes = new HashMap<>();
    
    public BuffService() {
        
        
    }
    
    public void registerBuffPrototype(BuffPrototype prototype) {
        buffPrototypes.put(prototype.getName(), prototype);
    }
    
    
    public BuffRuntimeModel generateRunTimeBuff(String buffPrototypeName, int duration) throws NotFoundException {
        if (!buffPrototypes.containsKey(buffPrototypeName)) {
            throw new NotFoundException("Buff", buffPrototypeName);
        }
        return new BuffRuntimeModel(buffPrototypes.get(buffPrototypeName), duration);
    }
    
    public Collection<BuffPrototype> listBuffModels() {
        return buffPrototypes.values();
    }

}
