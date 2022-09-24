package hundun.quizlib.prototype;

import java.util.List;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @author hundun
 * Created on 2021/06/28
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
public class TeamPrototype {
    private String name;
    
    private List<String> pickTags;
    
    private List<String> banTags;
    
    private RolePrototype rolePrototype;
    
}
