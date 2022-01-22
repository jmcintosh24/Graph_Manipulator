import java.io.File;
import java.util.Scanner;

public class Graphs {
    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);
        Scanner graphScanner = null;
        boolean fileNeeded = true;
        Scanner in = new Scanner(System.in);

        while (fileNeeded) {
            System.out.print("Enter a graph file: ");
            String stopName = input.nextLine();
            try {
                File graphFile = new File(stopName);
                graphScanner = new Scanner(graphFile);
                fileNeeded = false;
            } catch (Exception e) {
                System.out.println("That file does not exist!");
            }
        }

        Graph graph = new Graph(graphScanner);
        boolean done = false;
        while (!done) {
            System.out.print("\n1. Is Connected\n" +
                    "2. Minimum Spanning Tree\n" +
                    "3. Shortest Path\n" +
                    "4. Is Metric\n" +
                    "5. Make Metric\n" +
                    "6. Traveling Salesman Problem\n" +
                    "7. Approximate TSP\n" +
                    "8. Quit\n\n" +
                    "Make your choice (1 - 8): ");

            int userChoice = in.nextInt();
            if (userChoice == 1) {
                if (graph.isConnected())
                    System.out.println("Graph is connected.");
                else {
                    System.out.println("Graph is not connected.");
                }
            } else if (userChoice == 2) {
                if (graph.isConnected()) {
                    graph.minSpanTree().printMatrix();

                } else
                    System.out.println("Error: Graph is not connected.");
            } else if (userChoice == 3) {
                int nodeTotal = graph.getNodes();
                System.out.print("From which node would you like to find the shortest paths (0 - " + (nodeTotal - 1) + "): ");
                int nodeChoice = in.nextInt();
                int[] predecessors = new int[nodeTotal];
                int[] distances = new int[nodeTotal];
                graph.shortestPath(nodeChoice, distances, predecessors);
                for (int i = 0; i < nodeTotal; i++) {
                    if (distances[i] != Integer.MAX_VALUE) {
                        System.out.print(i + ": (" + distances[i] + ")\t");
                        printPath(nodeChoice, i, predecessors);
                        System.out.print("\n");
                    } else {
                        System.out.print(i + ": (Infinity)\n");
                    }
                }
            } else if (userChoice == 4) {
                if (!graph.isCompletelyConnected()) {
                    System.out.println("Graph is not metric: Graph is not completely connected.");
                } else if (!graph.triangleInequalitySatisfied()) {
                    System.out.println("Graph is not metric: Edges do not obey the triangle inequality.");
                } else {
                    System.out.println("Graph is metric.");
                }
            } else if (userChoice == 5) {
                if (graph.isConnected()) {
                    graph.makeMetric();
                    graph.printMatrix();
                } else {
                    System.out.println("Error: Graph is not connected.");
                }
            } else if (userChoice == 6) {
                if (!graph.isConnected()) {
                    System.out.println("Error: Graph is not connected.");
                }
                int[] path = graph.TSP();
                if (path[graph.getNodes()] == Integer.MAX_VALUE) {
                    System.out.println("Error: Graph has no tour.");
                } else {
                    System.out.print(path[graph.getNodes()] + ": ");
                    for (int i = 0; i < graph.getNodes(); ++i) {
                        System.out.print(path[i] + " -> ");
                    }
                    System.out.println(path[0]);
                }
            } else if (userChoice == 7) {
                if (graph.isCompletelyConnected() && graph.triangleInequalitySatisfied()) {
                    int[] path = graph.approximateTSP();
                    System.out.print(path[graph.getNodes()] + ": ");
                    for (int i = 0; i < graph.getNodes(); ++i) {
                        System.out.print(path[i] + " -> ");
                    }
                    System.out.println(path[0]);
                } else {
                    System.out.println("Error: Graph is not metric.");
                }
            } else if (userChoice == 8) {
                done = true;
            }
        }

    }

    /*
    Helper method that recursively prints the path to a currentNode using an array of predecessors.
     */
    private static void printPath(int nodeChoice, int currentNode, int[] predecessors) {
        if (currentNode == nodeChoice) {
            System.out.print(currentNode);
        } else {
            printPath(nodeChoice, predecessors[currentNode], predecessors);
            System.out.print(" -> " + currentNode);
        }

    }
}
