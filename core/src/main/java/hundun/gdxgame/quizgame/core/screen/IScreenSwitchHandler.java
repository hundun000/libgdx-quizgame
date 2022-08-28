package hundun.gdxgame.quizgame.core.screen;

import hundun.quizlib.prototype.match.MatchConfig;

/**
 * 可一览跳转Screen前要准备好哪些参数。该Handler将调用尽可能多的跳转Screen有关的组件。
 * @author hundun
 * Created on 2022/09/01
 */
public interface IScreenSwitchHandler {
    
    void intoQuizPlayScreen(MatchConfig matchConfig);
    void intoTeamScreen(boolean load);
}
