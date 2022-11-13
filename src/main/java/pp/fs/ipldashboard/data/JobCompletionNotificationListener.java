package pp.fs.ipldashboard.data;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.listener.JobExecutionListenerSupport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pp.fs.ipldashboard.model.Team;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;
import java.util.HashMap;
import java.util.Map;

@Component
public class JobCompletionNotificationListener extends JobExecutionListenerSupport {

    private static final Logger log = LoggerFactory.getLogger(JobCompletionNotificationListener.class);

    private final EntityManager em;

    @Autowired
    public JobCompletionNotificationListener(EntityManager em) {
        this.em = em;
    }

    @Override
    @Transactional
    public void afterJob(JobExecution jobExecution) {
        if (jobExecution.getStatus() == BatchStatus.COMPLETED) {
            log.info("!!! JOB FINISHED! Time to verify the results");

            Map<String, Team> map = new HashMap<>();

            em.createQuery("select m.team1, count(m) from Match m group by m.team1", Object[].class)
                    .getResultList()
                    .stream()
                    .map(e -> new Team((String)e[0], (long) e[1]))
                    .forEach(team -> map.put(team.getTeamName(), team));

            em.createQuery("select m.team2, count(m) from Match m group by m.team2", Object[].class)
                    .getResultList()
                    .forEach(team -> {
                        Team t = map.get((String) team[0]);
                        t.setTotalMatches(t.getTotalMatches() + (long) team[1]);
                    });

            em.createQuery("select m.matchWinner, count(m) from Match m group by m.matchWinner", Object[].class)
                    .getResultList()
                    .forEach(e -> {
                        Team team = map.get((String) e[0]);
                        if (team != null)
                            team.setTotalWins((long) e[1]);
                    });

            map.values().forEach(em::persist);

           /* jdbcTemplate.query("SELECT team1, team2 FROM match",
                    (rs, row) -> "Team-1 : " + rs.getString(1) + " , Team-2 : " + rs.getString(2)
            ).forEach(str -> System.out.println(str));*/
        }
    }
}
