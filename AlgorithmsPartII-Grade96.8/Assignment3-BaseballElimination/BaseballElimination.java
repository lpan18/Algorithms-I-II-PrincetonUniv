import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.FlowNetwork;
import edu.princeton.cs.algs4.FlowEdge;
import edu.princeton.cs.algs4.FordFulkerson;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class BaseballElimination {
    private final Map<String, Integer> teamToId;
    private final int[] wins;
    private final int[] losses;
    private final int[] remaining;
    private final int[][] against;
    private int maxWins;
    private String maxWinsTeam;

    // create a baseball division from given filename in format specified below
    public BaseballElimination(String filename) {
        In in = new In(filename);
        int n = in.readInt();
        wins = new int[n];
        losses = new int[n];
        remaining = new int[n];
        against = new int[n][n];
        teamToId = new HashMap<String, Integer>();
        maxWins = -1;
        while (!in.isEmpty()) {
            for (int i = 0; i < n; i++) {
                String team = in.readString();
                teamToId.put(team, i);
                wins[i] = in.readInt();
                losses[i] = in.readInt();
                remaining[i] = in.readInt();
                for (int j = 0; j < n; j++) {
                    against[i][j] = in.readInt();
                }
                if (wins[i] > maxWins) {
                    maxWins = wins[i];
                    maxWinsTeam = team;
                }
            }
        }
    }

    // number of teams
    public int numberOfTeams() {
        return teamToId.size();
    }

    // all teams
    public Iterable<String> teams() {
        return teamToId.keySet();
    }

    // number of wins for given team
    public int wins(String team) {
        return wins[getTeamIndex(team)];
    }

    //      number of losses for given team
    public int losses(String team) {
        return losses[getTeamIndex(team)];
    }

    // number of remaining games for given team
    public int remaining(String team) {
        return remaining[getTeamIndex(team)];
    }

    // number of remaining games between team1 and team2
    public int against(String team1, String team2) {
        return against[getTeamIndex(team1)][getTeamIndex(team2)];
    }

    // is given team eliminated?
    public boolean isEliminated(String team) {
        int id = getTeamIndex(team);
        if (trivialElimination(id)) {
            return true;
        }
        BuildFF g = buildFFFor(id);
        for (FlowEdge e : g.network.adj(g.source)) {
            if (e.flow() < e.capacity()) {
                return true;
            }
        }
        return false;
    }

    private boolean trivialElimination(int id) {
        if (wins[id] + remaining[id] < maxWins) {
            return true;
        }
        return false;
    }

    // subset R of teams that eliminates given team; null if not eliminated
    public Iterable<String> certificateOfElimination(String team) {
        int id = getTeamIndex(team);
        Set<String> subsetR = new HashSet<String>();
        if (isEliminated(team)) {
            if (trivialElimination(id)) {
                subsetR.add(maxWinsTeam);
            }
            BuildFF g = buildFFFor(id);
            for (String t : teams()) {
                if (g.ff.inCut(teamToId.get(t))) {
                    subsetR.add(t);
                }
            }
            return subsetR;
        }
        return null;

    }

    private int getTeamIndex(String team) {
        if (team == null||teamToId.get(team) == null) {
            throw new IllegalArgumentException("team is null or no such team!");
        }
        return teamToId.get(team);
    }

    private class BuildFF {
        FordFulkerson ff;
        FlowNetwork network;
        int source;

        private BuildFF(FordFulkerson ff, FlowNetwork network, int source) {
            this.ff = ff;
            this.network = network;
            this.source = source;
        }
    }

    private BuildFF buildFFFor(int x) {
        int n = numberOfTeams();
        int source = n;
        int sink = n + 1;
        int gameVertices = n + 2;
        Set<FlowEdge> edges = new HashSet<FlowEdge>();
        if (wins[x] + remaining[x] >= maxWins) {
            for (int i = 0; i < n; i++) {
                if (i == x) {
                    continue;
                }
                for (int j = i + 1; j < n; j++) {
                    if (j == x || against[i][j] == 0) {
                        continue;
                    }
                    edges.add(new FlowEdge(source, gameVertices, against[i][j]));
                    edges.add(new FlowEdge(gameVertices, i, Double.POSITIVE_INFINITY));
                    edges.add(new FlowEdge(gameVertices, j, Double.POSITIVE_INFINITY));
                    gameVertices++;
                }
                edges.add(new FlowEdge(i, sink, wins[x] + remaining[x] - wins[i]));
            }
        }
        FlowNetwork network = new FlowNetwork(gameVertices);
        for (FlowEdge e : edges) {
            network.addEdge(e);
        }
        //StdOut.println(network);
        FordFulkerson ff = new FordFulkerson(network, source, sink);
        return new BuildFF(ff, network, source);
    }

    public static void main(String[] args) {
        BaseballElimination division = new BaseballElimination(args[0]);
        division.buildFFFor(2);
        StdOut.println(division.isEliminated("Princeton") );
        for (String team : division.teams()) {
            if (division.isEliminated(team)) {
                StdOut.print(team + " is eliminated by the subset R = { ");
                for (String t : division.certificateOfElimination(team)) {
                    StdOut.print(t + " ");
                }
                StdOut.println("}");
            } else {
                StdOut.println(team + " is not eliminated");
            }
        }
    }
}