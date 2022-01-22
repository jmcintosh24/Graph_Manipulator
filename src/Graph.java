/**
 * Holds the representation of a graph using an adjacency matrix. The class contains a variety of methods that
 * perform operations on the graph to discover different attributes (Is it connected, The Minimum Spanning Tree, etc).
 * Course: COMP 2100
 * Assignment: Project 4
 *
 * @author Anthony Howe, Jacob McIntosh
 * @version 1.0, 12/2/2021
 */
import java.util.Scanner;

public class Graph {
    private int[][] edges; //the edge connections in the graph
    private int numNodes; //number of nodes in graph

    /**
     * Constructor that creates a graph with the number of nodes given. The nodes are given no connections in the
     * adjacency matrix.
     *
     * @param numNodes the number of nodes in the graph
     */
    public Graph(int numNodes) {
        edges = new int[numNodes][numNodes];
    }

    /**
     * Constructor that reads in information about a graph using a given scanner. The information is then used
     * to create a graph using the adjacency matrix.
     *
     * @param scanner the scanner for the text file
     */
    public Graph(Scanner scanner) {
        numNodes = scanner.nextInt();
        edges = new int[numNodes][numNodes];
        for (int i = 0; i < edges.length; i++) {
            int numEdges = scanner.nextInt();
            int index = 0;
            while (index < numEdges) {
                edges[i][scanner.nextInt()] = scanner.nextInt();
                index++;
            }
        }
    }

    /**
     * Returns the number of nodes in the graph.
     *
     * @return number of nodes
     */
    public int getNodes() {
        return edges.length;
    }

    /**
     * Returns whether or not the graph is connected.
     *
     * @return true if connected, false otherwise
     */
    public boolean isConnected() {
        int[] array = depthFirstSearch(0);
        return array[array.length - 1] == edges.length;
    }


    public Graph minSpanTree() {
        boolean[] S = new boolean[edges.length];
        S[0] = true;
        Graph mst = new Graph(numNodes);

        for (int iterations = 0; iterations < edges.length - 1; iterations++) {  //iterate through to find the shortest edge in V that is connected to S
            int closest = Integer.MAX_VALUE;
            int startNode = 0;
            int endNode = 0;


            for (int i = 0; i < edges.length; i++) {
                if (S[i]) {
                    for (int j = 0; j < edges.length; j++) {
                        if (!S[j] && edges[i][j] != 0 && edges[i][j] < closest) {
                            closest = edges[i][j];
                            startNode = i;
                            endNode = j;
                        }
                    }
                }
            }
            mst.edges[startNode][endNode] = closest;
            mst.edges[endNode][startNode] = closest;
            S[endNode] = true;
        }
        return mst;
    }

    /**
     * Helper method that prints out the adjacency matrix.
     */
    public void printMatrix() {
        System.out.println(edges.length);
        String line = "";
        int count = 0;
        for (int i = 0; i < edges.length; i++) {
            for (int j = 0; j < edges[0].length; j++) {
                if (edges[i][j] != 0) {
                    count++;
                    line += j + " " + edges[i][j] + " ";
                }
            }
            System.out.println(count + " " + line);
            line = "";
            count = 0;
        }
    }

    private int[] depthFirstSearch(int node) {
        int[] number = new int[edges.length + 1]; //+1 is placeholder that allows us to track what has been visited
        depthFirstSearch(node, number);
        return number;
    }

    private void depthFirstSearch(int node, int[] number) {
        number[edges.length]++;
        number[node] = number[edges.length];
        for (int i = 0; i < edges.length; i++) {
            if (edges[node][i] != 0 && number[i] == 0) {
                depthFirstSearch(i, number);
            }
        }
    }

    public void shortestPath(int node, int[] distances, int[] predecessors) {
        final int nodes = edges.length;
        boolean[] S = new boolean[nodes];
        for (int i = 0; i < distances.length; ++i) {
            distances[i] = Integer.MAX_VALUE;
        }
        distances[node] = 0;
        for (int i = 0; i < nodes; i++) {
            int closestDistance = Integer.MAX_VALUE;
            int closestNode = -1;
            for (int j = 0; j < nodes; ++j) {
                if (distances[j] < closestDistance && !S[j]) {
                    closestDistance = distances[j];
                    closestNode = j;
                }
            }
            if (closestNode == -1) {
                return;
            }
            for (int j = 0; j < nodes; ++j) {
                if (edges[closestNode][j] != 0 && !S[j] && distances[j] > closestDistance + edges[closestNode][j]) {
                    distances[j] = closestDistance + edges[closestNode][j];
                    predecessors[j] = closestNode;
                }
            }
            S[closestNode] = true;
        }
    }

    public boolean isCompletelyConnected() {
        final int nodes = edges.length;
        for (int i = 0; i < nodes; ++i) {
            for (int j = i + 1; j < nodes; ++j) {
                if (edges[i][j] == 0) {
                    return false;
                }
            }
        }
        return true;
    }

    public boolean triangleInequalitySatisfied() {
        final int nodes = edges.length;

        for (int i = 0; i < nodes; ++i) {
            for (int j = i + 1; j < nodes; ++j) {
                for (int k = 0; k < nodes; k++) {
                    if (k != i && k != j && edges[i][j] > edges[i][k] + edges[k][j]) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    public void makeMetric() {
        for (int i = 0; i < edges.length; ++i) {
            int[] distances = new int[edges.length];
            int[] predecessors = new int[edges.length];
            shortestPath(i, distances, predecessors);
            for (int j = 0; j < edges.length; ++j) {
                edges[i][j] = distances[j];
                edges[j][i] = distances[j];
            }
        }
    }

    public int[] TSP() {
        int[] path = new int[edges.length + 1]; //path is 1 space longer than path, tracks distance
        int[] bestPath = new int[edges.length + 1];
        bestPath[edges.length] = Integer.MAX_VALUE;
        boolean[] visited = new boolean[edges.length];
        TSP(0, path, 0, bestPath, visited);
        return bestPath;
    }

    private void TSP(int node, int[] path, int step, int[] bestPath, boolean[] visited) {
        if (step == edges.length && node == path[0]) {
            if (path[edges.length] < bestPath[edges.length]) {
                for (int i = 0; i < path.length; ++i) {
                    bestPath[i] = path[i];
                }
            }
        } else if (!visited[node]) {
            path[step] = node;
            visited[node] = true;
            for (int i = 0; i < edges.length; ++i) {
                if (edges[node][i] != 0) {
                    path[edges.length] += edges[node][i];
                    TSP(i, path, step + 1, bestPath, visited);
                    path[edges.length] -= edges[node][i];
                }
            }
            visited[node] = false;
        }
    }

    public int[] approximateTSP() {
        Graph MST = minSpanTree();
        int[] dfsArray = MST.depthFirstSearch(0);
        int[] tour = new int[dfsArray.length];
        final int nodes = edges.length;
        for (int i = 0; i < nodes; i++) {
            tour[dfsArray[i] - 1] = i;
        }
        int weight = 0;
        for (int i = 0; i < nodes - 1; i++) {
            weight += edges[tour[i]][tour[i + 1]];
        }
        weight += edges[tour[nodes - 1]][tour[0]];
        tour[tour.length - 1] = weight;
        return tour;
    }
}
