package hundun.quizlib.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import hundun.quizlib.context.IQuizComponent;
import hundun.quizlib.context.QuizComponentContext;
import hundun.quizlib.exception.NotFoundException;
import hundun.quizlib.exception.QuizgameException;
import hundun.quizlib.model.domain.TeamRuntimeModel;
import hundun.quizlib.prototype.RolePrototype;
import hundun.quizlib.prototype.TeamPrototype;

public class TeamService implements IQuizComponent {
    
    
    RoleSkillService roleSkillService;
    
    @Override
    public void postConstruct(QuizComponentContext context) {
        this.roleSkillService = context.getRoleSkillService();
    }
    
	private Map<String, TeamPrototype> teamPrototypes = new HashMap<>();
	
	public void registerTeam(String teamName, List<String> pickTagNames, List<String> banTagNames, String roleName) throws QuizgameException {
        
	    RolePrototype rolePrototype = roleName != null ? roleSkillService.getRole(roleName) : null;

	    TeamPrototype teamPrototype = new TeamPrototype(teamName, pickTagNames, banTagNames, rolePrototype);
	    
	    
	    teamPrototypes.put(teamName, teamPrototype); 


    }

	
	public void updateTeam(TeamPrototype teamPrototype) throws NotFoundException {
	    if (!existTeam(teamPrototype.getName())) {
            throw new NotFoundException(TeamRuntimeModel.class.getSimpleName(), teamPrototype.getName());
        }
	    teamPrototypes.put(teamPrototype.getName(), teamPrototype);
    }
	
	
	
	
	
	public TeamPrototype getTeam(String name) throws NotFoundException {

		if (!existTeam(name)) {
			throw new NotFoundException(TeamRuntimeModel.class.getSimpleName(), name);
		}
		return teamPrototypes.get(name);
	}
	
	public List<TeamPrototype> listTeams() {
        return new ArrayList<>(teamPrototypes.values());
    }
	
	public boolean existTeam(String name) {
        return teamPrototypes.containsKey(name);
    }


    

}
