package Benchmark;

import list.*;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Random;

public class Benchmark {

    static class Result {
        String impl;
        String method;
        int n;
        long timeNs;

        Result(String impl, String method, int n, long timeNs) {
            this.impl = impl;
            this.method = method;
            this.n = n;
            this.timeNs = timeNs;
        }

        @Override
        public String toString() {
            double ms = timeNs / 1_000_000.0;
            return String.format("%s,%s,%d,%d,%.3f", impl, method, n, timeNs, ms);
        }
    }

    static final int[] SIZES = {100, 1_000, 10_000, 100_000, 1_000_000};
    static final int WARMUP = 3;
    static final int ITERATIONS = 5;

    public static void main(String[] args) throws Exception {
        java.util.List<Result> results = new ArrayList<>();

        results.addAll(benchmarkPushFront());
        results.addAll(benchmarkPushBack());
        results.addAll(benchmarkPopFront());
        results.addAll(benchmarkPopBack());
        results.addAll(benchmarkFind());
        results.addAll(benchmarkAddBefore());
        results.addAll(benchmarkErase());

        saveResultsToCSV("results_part1.csv", results);
        printResults(results);
    }

    static java.util.List<Result> benchmarkPushFront() {
        System.out.println("\n=== BENCHMARK: pushFront (esperado: O(1)) ===");
        java.util.List<Result> results = new ArrayList<>();

        for (int n : SIZES) {
            {
                List<Integer> list = new SinglyLinkedList<>();
                long avgNs = measureAvg(() -> {
                    for (int i = 0; i < n; i++) {
                        list.pushFront(i);
                    }
                });
                results.add(new Result("SinglyLinkedList", "pushFront", n, avgNs));
            }

            {
                List<Integer> list = new SinglyLinkedListWithTail<>();
                long avgNs = measureAvg(() -> {
                    for (int i = 0; i < n; i++) {
                        list.pushFront(i);
                    }
                });
                results.add(new Result("SinglyLinkedListWithTail", "pushFront", n, avgNs));
            }

            {
                List<Integer> list = new DoublyLinkedList<>();
                long avgNs = measureAvg(() -> {
                    for (int i = 0; i < n; i++) {
                        list.pushFront(i);
                    }
                });
                results.add(new Result("DoublyLinkedList", "pushFront", n, avgNs));
            }

            {
                List<Integer> list = new DoublyLinkedListWithTail<>();
                long avgNs = measureAvg(() -> {
                    for (int i = 0; i < n; i++) {
                        list.pushFront(i);
                    }
                });
                results.add(new Result("DoublyLinkedListWithTail", "pushFront", n, avgNs));
            }
        }

        return results;
    }

    static java.util.List<Result> benchmarkPushBack() {
        System.out.println("\n=== BENCHMARK: pushBack (esperado: O(n) vs O(1)) ===");
        java.util.List<Result> results = new ArrayList<>();

        for (int n : SIZES) {
            {
                List<Integer> list = new SinglyLinkedList<>();
                long avgNs = measureAvg(() -> {
                    for (int i = 0; i < n; i++) {
                        list.pushBack(i);
                    }
                });
                results.add(new Result("SinglyLinkedList", "pushBack", n, avgNs));
            }

            {
                List<Integer> list = new SinglyLinkedListWithTail<>();
                long avgNs = measureAvg(() -> {
                    for (int i = 0; i < n; i++) {
                        list.pushBack(i);
                    }
                });
                results.add(new Result("SinglyLinkedListWithTail", "pushBack", n, avgNs));
            }

            {
                List<Integer> list = new DoublyLinkedList<>();
                long avgNs = measureAvg(() -> {
                    for (int i = 0; i < n; i++) {
                        list.pushBack(i);
                    }
                });
                results.add(new Result("DoublyLinkedList", "pushBack", n, avgNs));
            }

            {
                List<Integer> list = new DoublyLinkedListWithTail<>();
                long avgNs = measureAvg(() -> {
                    for (int i = 0; i < n; i++) {
                        list.pushBack(i);
                    }
                });
                results.add(new Result("DoublyLinkedListWithTail", "pushBack", n, avgNs));
            }
        }

        return results;
    }

    static java.util.List<Result> benchmarkPopFront() {
        System.out.println("\n=== BENCHMARK: popFront (esperado: O(1)) ===");
        java.util.List<Result> results = new ArrayList<>();

        for (int n : SIZES) {
            {
                long avgNs = 0;
                for (int iter = 0; iter < ITERATIONS; iter++) {
                    List<Integer> list = new SinglyLinkedList<>();
                    for (int i = 0; i < n; i++) {
                        list.pushFront(i);
                    }
                    long start = System.nanoTime();
                    for (int i = 0; i < n; i++) {
                        list.popFront();
                    }
                    avgNs += (System.nanoTime() - start);
                }
                avgNs /= ITERATIONS;
                results.add(new Result("SinglyLinkedList", "popFront", n, avgNs));
            }

            {
                long avgNs = 0;
                for (int iter = 0; iter < ITERATIONS; iter++) {
                    List<Integer> list = new SinglyLinkedListWithTail<>();
                    for (int i = 0; i < n; i++) {
                        list.pushFront(i);
                    }
                    long start = System.nanoTime();
                    for (int i = 0; i < n; i++) {
                        list.popFront();
                    }
                    avgNs += (System.nanoTime() - start);
                }
                avgNs /= ITERATIONS;
                results.add(new Result("SinglyLinkedListWithTail", "popFront", n, avgNs));
            }

            {
                long avgNs = 0;
                for (int iter = 0; iter < ITERATIONS; iter++) {
                    List<Integer> list = new DoublyLinkedList<>();
                    for (int i = 0; i < n; i++) {
                        list.pushFront(i);
                    }
                    long start = System.nanoTime();
                    for (int i = 0; i < n; i++) {
                        list.popFront();
                    }
                    avgNs += (System.nanoTime() - start);
                }
                avgNs /= ITERATIONS;
                results.add(new Result("DoublyLinkedList", "popFront", n, avgNs));
            }

            {
                long avgNs = 0;
                for (int iter = 0; iter < ITERATIONS; iter++) {
                    List<Integer> list = new DoublyLinkedListWithTail<>();
                    for (int i = 0; i < n; i++) {
                        list.pushFront(i);
                    }
                    long start = System.nanoTime();
                    for (int i = 0; i < n; i++) {
                        list.popFront();
                    }
                    avgNs += (System.nanoTime() - start);
                }
                avgNs /= ITERATIONS;
                results.add(new Result("DoublyLinkedListWithTail", "popFront", n, avgNs));
            }
        }

        return results;
    }

    static java.util.List<Result> benchmarkPopBack() {
        System.out.println("\n=== BENCHMARK: popBack (esperado: O(n) vs O(1)) ===");
        java.util.List<Result> results = new ArrayList<>();

        for (int n : SIZES) {
            {
                long avgNs = 0;
                for (int iter = 0; iter < ITERATIONS; iter++) {
                    List<Integer> list = new SinglyLinkedList<>();
                    for (int i = 0; i < n; i++) {
                        list.pushBack(i);
                    }
                    long start = System.nanoTime();
                    for (int i = 0; i < n; i++) {
                        list.popBack();
                    }
                    avgNs += (System.nanoTime() - start);
                }
                avgNs /= ITERATIONS;
                results.add(new Result("SinglyLinkedList", "popBack", n, avgNs));
            }

            {
                long avgNs = 0;
                for (int iter = 0; iter < ITERATIONS; iter++) {
                    List<Integer> list = new SinglyLinkedListWithTail<>();
                    for (int i = 0; i < n; i++) {
                        list.pushBack(i);
                    }
                    long start = System.nanoTime();
                    for (int i = 0; i < n; i++) {
                        list.popBack();
                    }
                    avgNs += (System.nanoTime() - start);
                }
                avgNs /= ITERATIONS;
                results.add(new Result("SinglyLinkedListWithTail", "popBack", n, avgNs));
            }

            {
                long avgNs = 0;
                for (int iter = 0; iter < ITERATIONS; iter++) {
                    List<Integer> list = new DoublyLinkedList<>();
                    for (int i = 0; i < n; i++) {
                        list.pushBack(i);
                    }
                    long start = System.nanoTime();
                    for (int i = 0; i < n; i++) {
                        list.popBack();
                    }
                    avgNs += (System.nanoTime() - start);
                }
                avgNs /= ITERATIONS;
                results.add(new Result("DoublyLinkedList", "popBack", n, avgNs));
            }

            {
                long avgNs = 0;
                for (int iter = 0; iter < ITERATIONS; iter++) {
                    List<Integer> list = new DoublyLinkedListWithTail<>();
                    for (int i = 0; i < n; i++) {
                        list.pushBack(i);
                    }
                    long start = System.nanoTime();
                    for (int i = 0; i < n; i++) {
                        list.popBack();
                    }
                    avgNs += (System.nanoTime() - start);
                }
                avgNs /= ITERATIONS;
                results.add(new Result("DoublyLinkedListWithTail", "popBack", n, avgNs));
            }
        }

        return results;
    }

    static java.util.List<Result> benchmarkFind() {
        System.out.println("\n=== BENCHMARK: find (esperado: O(n)) ===");
        java.util.List<Result> results = new ArrayList<>();
        Random rand = new Random(42);

        for (int n : SIZES) {
            {
                List<Integer> list = new SinglyLinkedList<>();
                for (int i = 0; i < n; i++) {
                    list.pushBack(i);
                }
                long avgNs = measureAvg(() -> {
                    for (int i = 0; i < 10; i++) {
                        int target = rand.nextInt(n);
                        list.find(target);
                    }
                });
                results.add(new Result("SinglyLinkedList", "find", n, avgNs));
            }

            {
                List<Integer> list = new SinglyLinkedListWithTail<>();
                for (int i = 0; i < n; i++) {
                    list.pushBack(i);
                }
                long avgNs = measureAvg(() -> {
                    for (int i = 0; i < 10; i++) {
                        int target = rand.nextInt(n);
                        list.find(target);
                    }
                });
                results.add(new Result("SinglyLinkedListWithTail", "find", n, avgNs));
            }

            {
                List<Integer> list = new DoublyLinkedList<>();
                for (int i = 0; i < n; i++) {
                    list.pushBack(i);
                }
                long avgNs = measureAvg(() -> {
                    for (int i = 0; i < 10; i++) {
                        int target = rand.nextInt(n);
                        list.find(target);
                    }
                });
                results.add(new Result("DoublyLinkedList", "find", n, avgNs));
            }

            {
                List<Integer> list = new DoublyLinkedListWithTail<>();
                for (int i = 0; i < n; i++) {
                    list.pushBack(i);
                }
                long avgNs = measureAvg(() -> {
                    for (int i = 0; i < 10; i++) {
                        int target = rand.nextInt(n);
                        list.find(target);
                    }
                });
                results.add(new Result("DoublyLinkedListWithTail", "find", n, avgNs));
            }
        }

        return results;
    }

    static java.util.List<Result> benchmarkAddBefore() {
        System.out.println("\n=== BENCHMARK: addBefore (esperado: O(n) vs O(1)) ===");
        java.util.List<Result> results = new ArrayList<>();
        @SuppressWarnings("unused")
        Random rand = new Random(42);

        for (int n : SIZES) {
            {
                long avgNs = 0;
                for (int iter = 0; iter < ITERATIONS; iter++) {
                    List<Integer> list = new SinglyLinkedList<>();
                    Position<Integer> pos = list.pushBack(0);
                    for (int i = 1; i < n; i++) {
                        list.pushBack(i);
                    }
                    long start = System.nanoTime();
                    for (int i = 0; i < 100; i++) {
                        list.addBefore(pos, -1);
                    }
                    avgNs += (System.nanoTime() - start);
                }
                avgNs /= ITERATIONS;
                results.add(new Result("SinglyLinkedList", "addBefore", n, avgNs));
            }

            {
                long avgNs = 0;
                for (int iter = 0; iter < ITERATIONS; iter++) {
                    List<Integer> list = new SinglyLinkedListWithTail<>();
                    Position<Integer> pos = list.pushBack(0);
                    for (int i = 1; i < n; i++) {
                        list.pushBack(i);
                    }
                    long start = System.nanoTime();
                    for (int i = 0; i < 100; i++) {
                        list.addBefore(pos, -1);
                    }
                    avgNs += (System.nanoTime() - start);
                }
                avgNs /= ITERATIONS;
                results.add(new Result("SinglyLinkedListWithTail", "addBefore", n, avgNs));
            }

            {
                long avgNs = 0;
                for (int iter = 0; iter < ITERATIONS; iter++) {
                    List<Integer> list = new DoublyLinkedList<>();
                    Position<Integer> pos = list.pushBack(0);
                    for (int i = 1; i < n; i++) {
                        list.pushBack(i);
                    }
                    long start = System.nanoTime();
                    for (int i = 0; i < 100; i++) {
                        list.addBefore(pos, -1);
                    }
                    avgNs += (System.nanoTime() - start);
                }
                avgNs /= ITERATIONS;
                results.add(new Result("DoublyLinkedList", "addBefore", n, avgNs));
            }

            {
                long avgNs = 0;
                for (int iter = 0; iter < ITERATIONS; iter++) {
                    List<Integer> list = new DoublyLinkedListWithTail<>();
                    Position<Integer> pos = list.pushBack(0);
                    for (int i = 1; i < n; i++) {
                        list.pushBack(i);
                    }
                    long start = System.nanoTime();
                    for (int i = 0; i < 100; i++) {
                        list.addBefore(pos, -1);
                    }
                    avgNs += (System.nanoTime() - start);
                }
                avgNs /= ITERATIONS;
                results.add(new Result("DoublyLinkedListWithTail", "addBefore", n, avgNs));
            }
        }

        return results;
    }

    static java.util.List<Result> benchmarkErase() {
        System.out.println("\n=== BENCHMARK: erase (esperado: O(n) vs O(1)) ===");
        java.util.List<Result> results = new ArrayList<>();

        for (int n : SIZES) {
            {
                long avgNs = 0;
                for (int iter = 0; iter < ITERATIONS; iter++) {
                    List<Integer> list = new SinglyLinkedList<>();
                    java.util.List<Position<Integer>> positions = new ArrayList<>();
                    for (int i = 0; i < n; i++) {
                        positions.add(list.pushBack(i));
                    }
                    long start = System.nanoTime();
                    for (int i = 0; i < Math.min(100, n); i++) {
                        list.erase(positions.get(i));
                    }
                    avgNs += (System.nanoTime() - start);
                }
                avgNs /= ITERATIONS;
                results.add(new Result("SinglyLinkedList", "erase", n, avgNs));
            }

            {
                long avgNs = 0;
                for (int iter = 0; iter < ITERATIONS; iter++) {
                    List<Integer> list = new SinglyLinkedListWithTail<>();
                    java.util.List<Position<Integer>> positions = new ArrayList<>();
                    for (int i = 0; i < n; i++) {
                        positions.add(list.pushBack(i));
                    }
                    long start = System.nanoTime();
                    for (int i = 0; i < Math.min(100, n); i++) {
                        list.erase(positions.get(i));
                    }
                    avgNs += (System.nanoTime() - start);
                }
                avgNs /= ITERATIONS;
                results.add(new Result("SinglyLinkedListWithTail", "erase", n, avgNs));
            }

            {
                long avgNs = 0;
                for (int iter = 0; iter < ITERATIONS; iter++) {
                    List<Integer> list = new DoublyLinkedList<>();
                    java.util.List<Position<Integer>> positions = new ArrayList<>();
                    for (int i = 0; i < n; i++) {
                        positions.add(list.pushBack(i));
                    }
                    long start = System.nanoTime();
                    for (int i = 0; i < Math.min(100, n); i++) {
                        list.erase(positions.get(i));
                    }
                    avgNs += (System.nanoTime() - start);
                }
                avgNs /= ITERATIONS;
                results.add(new Result("DoublyLinkedList", "erase", n, avgNs));
            }

            {
                long avgNs = 0;
                for (int iter = 0; iter < ITERATIONS; iter++) {
                    List<Integer> list = new DoublyLinkedListWithTail<>();
                    java.util.List<Position<Integer>> positions = new ArrayList<>();
                    for (int i = 0; i < n; i++) {
                        positions.add(list.pushBack(i));
                    }
                    long start = System.nanoTime();
                    for (int i = 0; i < Math.min(100, n); i++) {
                        list.erase(positions.get(i));
                    }
                    avgNs += (System.nanoTime() - start);
                }
                avgNs /= ITERATIONS;
                results.add(new Result("DoublyLinkedListWithTail", "erase", n, avgNs));
            }
        }

        return results;
    }

    static long measureAvg(Runnable task) {
        for (int i = 0; i < WARMUP; i++) {
            task.run();
        }

        long totalNs = 0;
        for (int i = 0; i < ITERATIONS; i++) {
            System.gc();
            long start = System.nanoTime();
            task.run();
            totalNs += (System.nanoTime() - start);
        }
        return totalNs / ITERATIONS;
    }

    static void printResults(java.util.List<Result> results) {
        System.out.println("\n\n=== RESUMEN DE RESULTADOS (CSV formato) ===");
        System.out.println("Implementación,Método,n,TiempoNs,TiempoMs");
        results.forEach(System.out::println);
    }

    static void saveResultsToCSV(String filename, java.util.List<Result> results) throws Exception {
        try (PrintWriter pw = new PrintWriter(filename)) {
            pw.println("Implementación,Método,n,TiempoNs,TiempoMs");
            results.forEach(pw::println);
        }
        System.out.println("\n✓ Resultados guardados en: " + filename);
    }
}