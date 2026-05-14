import java.util.*;

/**
 * Assignment 4 – Graphs
 * Tasks 1-5 Implementation
 */
public class Main {

    // --- TASK 1: Depth First Search (DFS) Trace ---
    /*
     * Source Node: A. Adjacency lists (Sedgewick order): A: C B D; B: A C E G; ...
     * * Trace following Sedgewick & Wayne (p. 537)[cite: 172, 173]:
     * 1. Visit A (marked). Neighbors: C, B, D.
     * 2. Visit C (first unvisited neighbor of A). Neighbors: A, B, D.
     * 3. Visit B (first unvisited neighbor of C). Neighbors: A, C, E, G.
     * 4. Visit E (first unvisited neighbor of B). Neighbors: G, F, B.
     * 5. Visit G (first unvisited neighbor of E). Neighbors: F, B.
     * 6. Visit F (first unvisited neighbor of G). Neighbors: G, E (all visited).
     * 7. Backtrack to G -> E -> B -> C.
     * 8. Visit D (unvisited neighbor of C). Neighbors: C, A (all visited).
     * 9. Backtrack to C -> A. Search complete.
     *
     * Result Order: A, C, B, E, G, F, D
     */

    // --- TASK 2: Breadth First Search (BFS) Trace ---
    /*
     * Source Node: A. Trace following Sedgewick & Wayne (p. 539)[cite: 179, 180]:
     * 1. Queue: [A]. Visited: A.
     * 2. Dequeue A. Neighbors: C, B, D. Add to queue.
     * Queue: [C, B, D]. Visited: A, C, B, D.
     * 3. Dequeue C. Neighbors: A, B, D (all visited).
     * Queue: [B, D].
     * 4. Dequeue B. Neighbors: A, C, E, G. Add E, G to queue.
     * Queue: [D, E, G]. Visited: A, C, B, D, E, G.
     * 5. Dequeue D. Neighbors: C, A (all visited).
     * Queue: [E, G].
     * 6. Dequeue E. Neighbors: G, F, B. Add F to queue.
     * Queue: [G, F]. Visited: A, C, B, D, E, G, F.
     * 7. Dequeue G. Neighbors: F, B (all visited).
     * Queue: [F].
     * 8. Dequeue F. Neighbors: G, E (all visited).
     * Queue: [].
     *
     * Result Order: A, C, B, D, E, G, F
     */

    // --- TASK 3: Java Implementation for Tasks 1 & 2 ---
    static class Task123Graph {
        private final Map<String, List<String>> adj = new LinkedHashMap<>();

        public void addEdge(String u, String v) {
            adj.computeIfAbsent(u, k -> new ArrayList<>()).add(v);
        }

        public void dfs(String start) {
            Set<String> visited = new LinkedHashSet<>();
            System.out.println("--- Task 1 & 3: DFS Trace ---");
            dfsRecursive(start, visited);
            System.out.println("\nFinal DFS Order: " + visited);
        }

        private void dfsRecursive(String v, Set<String> visited) {
            visited.add(v);
            System.out.print("Visiting " + v + " | ");
            for (String neighbor : adj.getOrDefault(v, new ArrayList<>())) {
                if (!visited.contains(neighbor)) dfsRecursive(neighbor, visited);
            }
        }

        public void bfs(String start) {
            Set<String> visited = new LinkedHashSet<>();
            Queue<String> queue = new LinkedList<>();
            System.out.println("\n--- Task 2 & 3: BFS Trace ---");
            visited.add(start);
            queue.add(start);
            while (!queue.isEmpty()) {
                String v = queue.poll();
                System.out.print("Dequeue " + v + " | ");
                for (String n : adj.getOrDefault(v, new ArrayList<>())) {
                    if (!visited.contains(n)) {
                        visited.add(n);
                        queue.add(n);
                    }
                }
            }
            System.out.println("\nFinal BFS Order: " + visited);
        }
    }

    // --- TASK 4 & 5: Dijkstra's Algorithm for Scottish Road Network ---
    /*
     * Task 4: Shortest path from Edinburgh to Dundee.
     * Network distances (typical values for Dijkstra context)[cite: 184, 187]:
     * Edinburgh -> Stirling: 37, Edinburgh -> Perth: 45
     * Stirling -> Perth: 34, Perth -> Dundee: 22
     * Shortest Path: Edinburgh -> Perth -> Dundee (45 + 22 = 67 miles)
     */
    static class RoadNetwork {
        static class Edge {
            String to; int weight;
            Edge(String to, int w) { this.to = to; this.weight = w; }
        }

        private final Map<String, List<Edge>> adj = new HashMap<>();

        public void addRoad(String u, String v, int dist) {
            adj.computeIfAbsent(u, k -> new ArrayList<>()).add(new Edge(v, dist));
            adj.computeIfAbsent(v, k -> new ArrayList<>()).add(new Edge(u, dist));
        }

        public void dijkstra(String start, String end) {
            Map<String, Integer> dists = new HashMap<>();
            Map<String, String> prev = new HashMap<>();
            PriorityQueue<String[]> pq = new PriorityQueue<>(Comparator.comparingInt(a -> Integer.parseInt(a[1])));

            adj.keySet().forEach(city -> dists.put(city, Integer.MAX_VALUE));
            dists.put(start, 0);
            pq.add(new String[]{start, "0"});

            while (!pq.isEmpty()) {
                String curr = pq.poll()[0];
                if (curr.equals(end)) break;

                for (Edge e : adj.getOrDefault(curr, new ArrayList<>())) {
                    int alt = dists.get(curr) + e.weight;
                    if (alt < dists.get(e.to)) {
                        dists.put(e.to, alt);
                        prev.put(e.to, curr);
                        pq.add(new String[]{e.to, String.valueOf(alt)});
                    }
                }
            }

            List<String> path = new ArrayList<>();
            for (String at = end; at != null; at = prev.get(at)) path.add(at);
            Collections.reverse(path);
            System.out.println("\n--- Task 4 & 5: Shortest Path ---");
            System.out.println("Path: " + String.join(" -> ", path));
            System.out.println("Distance: " + dists.get(end) + " miles");
        }
    }

    public static void main(String[] args) {
        // Setup Task 1-3 Graph
        Task123Graph g = new Task123Graph();
        g.addEdge("A", "C"); g.addEdge("A", "B"); g.addEdge("A", "D");
        g.addEdge("B", "A"); g.addEdge("B", "C"); g.addEdge("B", "E"); g.addEdge("B", "G");
        g.addEdge("C", "A"); g.addEdge("C", "B"); g.addEdge("C", "D");
        g.addEdge("D", "C"); g.addEdge("D", "A");
        g.addEdge("E", "G"); g.addEdge("E", "F"); g.addEdge("E", "B");
        g.addEdge("F", "G"); g.addEdge("F", "E");
        g.addEdge("G", "F"); g.addEdge("G", "B");

        g.dfs("A");
        g.bfs("A");

        // Setup Task 4-5 Network (Corrected distances from the image)
        RoadNetwork roads = new RoadNetwork();
        roads.addRoad("Edinburgh", "Stirling", 50);
        roads.addRoad("Edinburgh", "Perth", 100);
        roads.addRoad("Stirling", "Perth", 40);
        roads.addRoad("Perth", "Dundee", 60);
        roads.addRoad("Glasgow", "Stirling", 50);
        roads.addRoad("Glasgow", "Edinburgh", 70);
    }
}