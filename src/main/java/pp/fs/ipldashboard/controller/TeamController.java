package pp.fs.ipldashboard.controller;

import org.springframework.web.bind.annotation.*;
import pp.fs.ipldashboard.model.Match;
import pp.fs.ipldashboard.model.Team;
import pp.fs.ipldashboard.repository.MatchRepository;
import pp.fs.ipldashboard.repository.TeamRepository;

import java.time.LocalDate;
import java.util.List;

@RestController
@CrossOrigin
public class TeamController {

    private final TeamRepository teamRepository;
    private final MatchRepository matchRepository;

    public TeamController(TeamRepository teamRepository, MatchRepository matchRepository) {
        this.teamRepository = teamRepository;
        this.matchRepository = matchRepository;
    }

    @GetMapping("/team/{teamName}")
    public Team getTeam(@PathVariable String teamName) {
        Team team = teamRepository.findByTeamName(teamName);
        team.setMatches(matchRepository.getLatestMatchByTeamName(teamName, 5));
        return team;
    }

    @GetMapping("/team/{teamName}/matches")
    public List<Match> getMatches(@PathVariable String teamName, @RequestParam int year) {
        LocalDate startDate = LocalDate.of(year, 1, 1);
        LocalDate endDate = LocalDate.of(year + 1, 1, 1);
        List<Match> matches = matchRepository.getMatchesByTeamBetweenDates(
                teamName, startDate, endDate);
        return matches;
    }
}
