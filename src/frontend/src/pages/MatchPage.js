import { React, useEffect, useState } from 'react';
import {useParams} from 'react-router-dom';
import {MatchDetailCard} from "../components/MatchDetailCard";

export const MatchPage = () => {

    const [matches, setMatches] = useState([]);
    const {teamName, year} = useParams();

    useEffect(() => {
       const fetchResult = async () => {
         const response = await fetch(`http://localhost:8080/team/${teamName}/matches?year=${year}`);
         const res = await response.json();
         setMatches(res);
         console.log(res);
       };
       fetchResult();
    });

    return (
        <div className="MatchPage">
            <h1>Match Page : {teamName}</h1>
            {matches.length > 0 ?
                matches.map(match => <MatchDetailCard key={match.id} teamName={teamName} match={match}/>)
            : <h3>No info available</h3>}
        </div>
    );
};
