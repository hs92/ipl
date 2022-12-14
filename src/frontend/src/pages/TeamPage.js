import { React, useEffect, useState } from 'react';
import {useParams} from 'react-router-dom';
import {MatchDetailCard} from "../components/MatchDetailCard";
import {MatchSmallCard} from "../components/MatchSmallCard";
import './TeamPage.scss';
import { PieChart } from 'react-minimal-pie-chart';
import {Link} from "react-router-dom";

export const TeamPage = () => {

    const endYear = process.env.REACT_APP_DATA_END_YEAR;
    const [team, setTeam] = useState({matches: []});
    const {teamName} = useParams();

    useEffect(() => {
        const fetchMatches = async () => {
            const response = await fetch(`/team/${teamName}`);
            const data = await response.json();
            setTeam(data);
        };
        fetchMatches();
    },[teamName]);

    if (!team || !team.teamName) return <h1>Team name not found</h1>;
    return (
        <div className="TeamPage">
            <div className="team-name-section">
                <h1 className="team-name">{team.teamName}</h1>
            </div>
            <div className="win-loss-section">
                Wins / Losses
                <PieChart
                    data={[
                        { title: 'Wins', value: team.totalWins, color: '#4da375' },
                        { title: 'Losses', value: team.totalMatches - team.totalWins, color: '#a34d5d' }
                    ]}
                />
            </div>
            <div className="match-detail-section">
                <h3>Latest matches</h3>
                <MatchDetailCard match={team.matches[0]} teamName={team.teamName} />
            </div>
            {team?.matches?.slice(1).map(match => <MatchSmallCard key={match.id} teamName={team.teamName} match={match}/>)}
            <div className={'more-link'}>
                <Link to={`/teams/${teamName}/matches/${endYear}`}>More ></Link>
            </div>
        </div>
    );
};
