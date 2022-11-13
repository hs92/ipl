package pp.fs.ipldashboard.data;

import pp.fs.ipldashboard.model.Match;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.batch.item.ItemProcessor;

import java.time.LocalDate;

public class MatchDataProcessor implements ItemProcessor<MatchInput, Match> {

    private static final Logger log = LoggerFactory.getLogger(MatchDataProcessor.class);

    @Override
    public Match process(final MatchInput matchInput) throws Exception {
        String firstInnings, secondInnings;
        if ("bat".equals(matchInput.getToss_decision())) {
            firstInnings = matchInput.getToss_winner();
            secondInnings = matchInput.getToss_winner().equals(matchInput.getTeam1())
                    ? matchInput.getTeam2() : matchInput.getTeam1();
        } else {
            secondInnings = matchInput.getToss_winner();
            firstInnings = matchInput.getToss_winner().equals(matchInput.getTeam1())
                    ? matchInput.getTeam2() : matchInput.getTeam1();
        }
        Match match = new Match(Long.parseLong(matchInput.getId()), matchInput.getCity(),
                LocalDate.parse(matchInput.getDate()), matchInput.getPlayer_of_match(), matchInput.getVenue(),
                firstInnings, secondInnings, matchInput.getToss_winner(), matchInput.getToss_decision(),
                matchInput.getWinner(), matchInput.getResult(), matchInput.getResult_margin(),
                matchInput.getUmpire1(), matchInput.getUmpire2());
        return match;
    }
}
