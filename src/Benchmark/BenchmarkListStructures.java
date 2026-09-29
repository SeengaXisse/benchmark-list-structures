package Benchmark;

import list.*;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Random;

/**
 * FASE 1: MEDICIÓN DE DATOS - SOLO RECOLECTA TIEMPOS
 * ====================================================
 * Este programa ÚNICAMENTE mide el tiempo de ejecución.
 * NO realiza graficación alguna (ver graficarResultados.m después).
 * 
 * Tamaños probados: 10, 100, 1_000, 10_000, 100_000, 1_000_000
 * Salidas: results_list_part1.csv, results_list_part2.csv, results_list_part3.csv
 */

public class BenchmarkListStructures {

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
            double us = timeNs / 1_000.0;  // Microsegundos para mejor visualización
            return String.format("%s,%s,%d,%d,%.3f", impl, method, n, timeNs, us);
        }
    }

    // Tamaños según el requisito: 10, 100, 10^4, 10^6, 10^8
    // Nota: 10^8 puede ser muy lento para SinglyLinkedList (O(n²) en algunos métodos)
    static final int[] SIZES = {10, 100, 1_000, 10_000, 100_000, 1_000_000};
    static final int WARMUP = 2;
    static final int ITERATIONS = 5;

    public static void main(String[] args) throws Exception {
        long startTotal = System.currentTimeMillis();
        
        // PARTE 1: Métodos básicos List (O(1) y O(n))
        System.out.println("📊 PARTE 1: Benchmarking List Methods (pushFront, popFront, pushBack, popBack, find)...");
        java.util.List<Result> resultsPart1 = new ArrayList<>();
        resultsPart1.addAll(benchmarkPushFront());
        resultsPart1.addAll(benchmarkPopFront());
        resultsPart1.addAll(benchmarkPushBack());
        resultsPart1.addAll(benchmarkPopBack());
        resultsPart1.addAll(benchmarkFind());
        saveResultsToCSV("results_list_part1.csv", resultsPart1);
        
        // PARTE 2: Métodos con posición (AddBefore, AddAfter, Erase)
        System.out.println("📊 PARTE 2: Benchmarking Position-based Methods (addBefore, addAfter, erase)...");
        java.util.List<Result> resultsPart2 = new ArrayList<>();
        resultsPart2.addAll(benchmarkAddBefore());
        resultsPart2.addAll(benchmarkAddAfter());
        resultsPart2.addAll(benchmarkErase());
        saveResultsToCSV("results_list_part2.csv", resultsPart2);
        
        // PARTE 3: Stack y Queue
        System.out.println("📊 PARTE 3: Benchmarking Stack and Queue...");
        java.util.List<Result> resultsPart3 = new ArrayList<>();
        resultsPart3.addAll(benchmarkStackPush());
        resultsPart3.addAll(benchmarkStackPop());
        resultsPart3.addAll(benchmarkQueueEnqueue());
        resultsPart3.addAll(benchmarkQueueDequeue());
        saveResultsToCSV("results_stack_queue.csv", resultsPart3);
        
        long endTotal = System.currentTimeMillis();
        System.out.println("\n✅ MEDICIÓN COMPLETADA EN " + (endTotal - startTotal) + " ms");
        System.out.println("📁 Archivos generados:");
        System.out.println("   - results_list_part1.csv (pushFront, popFront, pushBack, popBack, find)");
        System.out.println("   - results_list_part2.csv (addBefore, addAfter, erase)");
        System.out.println("   - results_stack_queue.csv (Stack push/pop, Queue enqueue/dequeue)");
        System.out.println("\n⚠️  SIGUIENTE PASO: Ejecutar graficarResultados.m en MATLAB");
    }

    // ==================== BENCHMARKS LISTA ====================

    static java.util.List<Result> benchmarkPushFront() {
        System.out.println("\n⏱️  PushFront (esperado: O(1))");
        java.util.List<Result> results = new ArrayList<>();

        for (int n : SIZES) {
            List<Integer> sll = new SinglyLinkedList<>();
            long time1 = measureAvg(() -> {
                for (int i = 0; i < n; i++) sll.pushFront(i);
            });
            results.add(new Result("SinglyLinkedList", "pushFront", n, time1));
            System.out.print(".");

            List<Integer> sllwt = new SinglyLinkedListWithTail<>();
            long time2 = measureAvg(() -> {
                for (int i = 0; i < n; i++) sllwt.pushFront(i);
            });
            results.add(new Result("SinglyLinkedListWithTail", "pushFront", n, time2));
            System.out.print(".");

            List<Integer> dll = new DoublyLinkedList<>();
            long time3 = measureAvg(() -> {
                for (int i = 0; i < n; i++) dll.pushFront(i);
            });
            results.add(new Result("DoublyLinkedList", "pushFront", n, time3));
            System.out.print(".");

            List<Integer> dllwt = new DoublyLinkedListWithTail<>();
            long time4 = measureAvg(() -> {
                for (int i = 0; i < n; i++) dllwt.pushFront(i);
            });
            results.add(new Result("DoublyLinkedListWithTail", "pushFront", n, time4));
            System.out.print(".");
        }
        System.out.println(" ✓");
        return results;
    }

    static java.util.List<Result> benchmarkPopFront() {
        System.out.println("⏱️  PopFront (esperado: O(1))");
        java.util.List<Result> results = new ArrayList<>();

        for (int n : SIZES) {
            long time1 = benchmarkPopFrontImpl(new SinglyLinkedList<>(), n);
            results.add(new Result("SinglyLinkedList", "popFront", n, time1));
            System.out.print(".");

            long time2 = benchmarkPopFrontImpl(new SinglyLinkedListWithTail<>(), n);
            results.add(new Result("SinglyLinkedListWithTail", "popFront", n, time2));
            System.out.print(".");

            long time3 = benchmarkPopFrontImpl(new DoublyLinkedList<>(), n);
            results.add(new Result("DoublyLinkedList", "popFront", n, time3));
            System.out.print(".");

            long time4 = benchmarkPopFrontImpl(new DoublyLinkedListWithTail<>(), n);
            results.add(new Result("DoublyLinkedListWithTail", "popFront", n, time4));
            System.out.print(".");
        }
        System.out.println(" ✓");
        return results;
    }

    static long benchmarkPopFrontImpl(List<Integer> list, int n) {
        for (int i = 0; i < n; i++) list.pushFront(i);
        long avgNs = 0;
        for (int iter = 0; iter < ITERATIONS; iter++) {
            long start = System.nanoTime();
            for (int i = 0; i < n; i++) list.popFront();
            avgNs += (System.nanoTime() - start);
        }
        return avgNs / ITERATIONS;
    }

    static java.util.List<Result> benchmarkPushBack() {
        System.out.println("⏱️  PushBack (esperado: O(n) vs O(1) con tail)");
        java.util.List<Result> results = new ArrayList<>();

        for (int n : SIZES) {
            List<Integer> sll = new SinglyLinkedList<>();
            long time1 = measureAvg(() -> {
                for (int i = 0; i < n; i++) sll.pushBack(i);
            });
            results.add(new Result("SinglyLinkedList", "pushBack", n, time1));
            System.out.print(".");

            List<Integer> sllwt = new SinglyLinkedListWithTail<>();
            long time2 = measureAvg(() -> {
                for (int i = 0; i < n; i++) sllwt.pushBack(i);
            });
            results.add(new Result("SinglyLinkedListWithTail", "pushBack", n, time2));
            System.out.print(".");

            List<Integer> dll = new DoublyLinkedList<>();
            long time3 = measureAvg(() -> {
                for (int i = 0; i < n; i++) dll.pushBack(i);
            });
            results.add(new Result("DoublyLinkedList", "pushBack", n, time3));
            System.out.print(".");

            List<Integer> dllwt = new DoublyLinkedListWithTail<>();
            long time4 = measureAvg(() -> {
                for (int i = 0; i < n; i++) dllwt.pushBack(i);
            });
            results.add(new Result("DoublyLinkedListWithTail", "pushBack", n, time4));
            System.out.print(".");
        }
        System.out.println(" ✓");
        return results;
    }

    static java.util.List<Result> benchmarkPopBack() {
        System.out.println("⏱️  PopBack (esperado: O(n) vs O(1) con tail y doubly)");
        java.util.List<Result> results = new ArrayList<>();

        for (int n : SIZES) {
            long time1 = benchmarkPopBackImpl(new SinglyLinkedList<>(), n);
            results.add(new Result("SinglyLinkedList", "popBack", n, time1));
            System.out.print(".");

            long time2 = benchmarkPopBackImpl(new SinglyLinkedListWithTail<>(), n);
            results.add(new Result("SinglyLinkedListWithTail", "popBack", n, time2));
            System.out.print(".");

            long time3 = benchmarkPopBackImpl(new DoublyLinkedList<>(), n);
            results.add(new Result("DoublyLinkedList", "popBack", n, time3));
            System.out.print(".");

            long time4 = benchmarkPopBackImpl(new DoublyLinkedListWithTail<>(), n);
            results.add(new Result("DoublyLinkedListWithTail", "popBack", n, time4));
            System.out.print(".");
        }
        System.out.println(" ✓");
        return results;
    }

    static long benchmarkPopBackImpl(List<Integer> list, int n) {
        for (int i = 0; i < n; i++) list.pushBack(i);
        long avgNs = 0;
        for (int iter = 0; iter < ITERATIONS; iter++) {
            long start = System.nanoTime();
            for (int i = 0; i < n; i++) list.popBack();
            avgNs += (System.nanoTime() - start);
        }
        return avgNs / ITERATIONS;
    }

    static java.util.List<Result> benchmarkFind() {
        System.out.println("⏱️  Find (esperado: O(n))");
        java.util.List<Result> results = new ArrayList<>();
        Random rand = new Random(42);

        for (int n : SIZES) {
            List<Integer> sll = new SinglyLinkedList<>();
            for (int i = 0; i < n; i++) sll.pushBack(i);
            long time1 = measureAvg(() -> {
                for (int i = 0; i < 10; i++) sll.find(rand.nextInt(n));
            });
            results.add(new Result("SinglyLinkedList", "find", n, time1));
            System.out.print(".");

            List<Integer> sllwt = new SinglyLinkedListWithTail<>();
            for (int i = 0; i < n; i++) sllwt.pushBack(i);
            long time2 = measureAvg(() -> {
                for (int i = 0; i < 10; i++) sllwt.find(rand.nextInt(n));
            });
            results.add(new Result("SinglyLinkedListWithTail", "find", n, time2));
            System.out.print(".");

            List<Integer> dll = new DoublyLinkedList<>();
            for (int i = 0; i < n; i++) dll.pushBack(i);
            long time3 = measureAvg(() -> {
                for (int i = 0; i < 10; i++) dll.find(rand.nextInt(n));
            });
            results.add(new Result("DoublyLinkedList", "find", n, time3));
            System.out.print(".");

            List<Integer> dllwt = new DoublyLinkedListWithTail<>();
            for (int i = 0; i < n; i++) dllwt.pushBack(i);
            long time4 = measureAvg(() -> {
                for (int i = 0; i < 10; i++) dllwt.find(rand.nextInt(n));
            });
            results.add(new Result("DoublyLinkedListWithTail", "find", n, time4));
            System.out.print(".");
        }
        System.out.println(" ✓");
        return results;
    }

    static java.util.List<Result> benchmarkAddBefore() {
        System.out.println("⏱️  AddBefore (esperado: O(n) vs O(1) con doubly)");
        java.util.List<Result> results = new ArrayList<>();

        for (int n : SIZES) {
            long time1 = benchmarkAddBeforeImpl(new SinglyLinkedList<>(), n);
            results.add(new Result("SinglyLinkedList", "addBefore", n, time1));
            System.out.print(".");

            long time2 = benchmarkAddBeforeImpl(new SinglyLinkedListWithTail<>(), n);
            results.add(new Result("SinglyLinkedListWithTail", "addBefore", n, time2));
            System.out.print(".");

            long time3 = benchmarkAddBeforeImpl(new DoublyLinkedList<>(), n);
            results.add(new Result("DoublyLinkedList", "addBefore", n, time3));
            System.out.print(".");

            long time4 = benchmarkAddBeforeImpl(new DoublyLinkedListWithTail<>(), n);
            results.add(new Result("DoublyLinkedListWithTail", "addBefore", n, time4));
            System.out.print(".");
        }
        System.out.println(" ✓");
        return results;
    }

    static long benchmarkAddBeforeImpl(List<Integer> list, int n) {
        Position<Integer> pos = list.pushBack(0);
        for (int i = 1; i < n; i++) list.pushBack(i);
        long avgNs = 0;
        for (int iter = 0; iter < ITERATIONS; iter++) {
            long start = System.nanoTime();
            for (int i = 0; i < Math.min(100, n); i++) {
                list.addBefore(pos, -1);
            }
            avgNs += (System.nanoTime() - start);
        }
        return avgNs / ITERATIONS;
    }

    static java.util.List<Result> benchmarkAddAfter() {
        System.out.println("⏱️  AddAfter (esperado: O(1) para all)");
        java.util.List<Result> results = new ArrayList<>();

        for (int n : SIZES) {
            long time1 = benchmarkAddAfterImpl(new SinglyLinkedList<>(), n);
            results.add(new Result("SinglyLinkedList", "addAfter", n, time1));
            System.out.print(".");

            long time2 = benchmarkAddAfterImpl(new SinglyLinkedListWithTail<>(), n);
            results.add(new Result("SinglyLinkedListWithTail", "addAfter", n, time2));
            System.out.print(".");

            long time3 = benchmarkAddAfterImpl(new DoublyLinkedList<>(), n);
            results.add(new Result("DoublyLinkedList", "addAfter", n, time3));
            System.out.print(".");

            long time4 = benchmarkAddAfterImpl(new DoublyLinkedListWithTail<>(), n);
            results.add(new Result("DoublyLinkedListWithTail", "addAfter", n, time4));
            System.out.print(".");
        }
        System.out.println(" ✓");
        return results;
    }

    static long benchmarkAddAfterImpl(List<Integer> list, int n) {
        Position<Integer> pos = list.pushBack(0);
        for (int i = 1; i < n; i++) list.pushBack(i);
        long avgNs = 0;
        for (int iter = 0; iter < ITERATIONS; iter++) {
            long start = System.nanoTime();
            for (int i = 0; i < Math.min(100, n); i++) {
                list.addAfter(pos, -1);
            }
            avgNs += (System.nanoTime() - start);
        }
        return avgNs / ITERATIONS;
    }

    static java.util.List<Result> benchmarkErase() {
        System.out.println("⏱️  Erase (esperado: O(n) vs O(1) con doubly)");
        java.util.List<Result> results = new ArrayList<>();

        for (int n : SIZES) {
            long time1 = benchmarkEraseImpl(new SinglyLinkedList<>(), n);
            results.add(new Result("SinglyLinkedList", "erase", n, time1));
            System.out.print(".");

            long time2 = benchmarkEraseImpl(new SinglyLinkedListWithTail<>(), n);
            results.add(new Result("SinglyLinkedListWithTail", "erase", n, time2));
            System.out.print(".");

            long time3 = benchmarkEraseImpl(new DoublyLinkedList<>(), n);
            results.add(new Result("DoublyLinkedList", "erase", n, time3));
            System.out.print(".");

            long time4 = benchmarkEraseImpl(new DoublyLinkedListWithTail<>(), n);
            results.add(new Result("DoublyLinkedListWithTail", "erase", n, time4));
            System.out.print(".");
        }
        System.out.println(" ✓");
        return results;
    }

    static long benchmarkEraseImpl(List<Integer> list, int n) {
        java.util.List<Position<Integer>> positions = new ArrayList<>();
        for (int i = 0; i < n; i++) positions.add(list.pushBack(i));
        long avgNs = 0;
        for (int iter = 0; iter < ITERATIONS; iter++) {
            long start = System.nanoTime();
            for (int i = 0; i < Math.min(100, n); i++) {
                list.erase(positions.get(i));
            }
            avgNs += (System.nanoTime() - start);
        }
        return avgNs / ITERATIONS;
    }

    // ==================== BENCHMARKS STACK ====================

    static java.util.List<Result> benchmarkStackPush() {
        System.out.println("⏱️  Stack.push() (esperado: O(1) amortizado)");
        java.util.List<Result> results = new ArrayList<>();

        for (int n : SIZES) {
            MyStack<Integer> stack = new MyStack<>();
            long time = measureAvg(() -> {
                for (int i = 0; i < n; i++) stack.push(i);
            });
            results.add(new Result("MyStack", "push", n, time));
            System.out.print(".");
        }
        System.out.println(" ✓");
        return results;
    }

    static java.util.List<Result> benchmarkStackPop() {
        System.out.println("⏱️  Stack.pop() (esperado: O(1))");
        java.util.List<Result> results = new ArrayList<>();

        for (int n : SIZES) {
            MyStack<Integer> stack = new MyStack<>();
            for (int i = 0; i < n; i++) stack.push(i);
            long time = measureAvg(() -> {
                for (int i = 0; i < n; i++) stack.pop();
            });
            results.add(new Result("MyStack", "pop", n, time));
            System.out.print(".");
        }
        System.out.println(" ✓");
        return results;
    }

    static java.util.List<Result> benchmarkQueueEnqueue() {
        System.out.println("⏱️  Queue.enqueue() (esperado: O(1) amortizado)");
        java.util.List<Result> results = new ArrayList<>();

        for (int n : SIZES) {
            MyQueue<Integer> queue = new MyQueue<>();
            long time = measureAvg(() -> {
                for (int i = 0; i < n; i++) queue.enqueue(i);
            });
            results.add(new Result("MyQueue", "enqueue", n, time));
            System.out.print(".");
        }
        System.out.println(" ✓");
        return results;
    }

    static java.util.List<Result> benchmarkQueueDequeue() {
        System.out.println("⏱️  Queue.dequeue() (esperado: O(1))");
        java.util.List<Result> results = new ArrayList<>();

        for (int n : SIZES) {
            MyQueue<Integer> queue = new MyQueue<>();
            for (int i = 0; i < n; i++) queue.enqueue(i);
            long time = measureAvg(() -> {
                for (int i = 0; i < n; i++) queue.dequeue();
            });
            results.add(new Result("MyQueue", "dequeue", n, time));
            System.out.print(".");
        }
        System.out.println(" ✓");
        return results;
    }

    // ==================== UTILIDADES ====================

    static long measureAvg(Runnable task) {
        // Warmup
        for (int i = 0; i < WARMUP; i++) task.run();

        // Mediciones reales
        long totalNs = 0;
        for (int i = 0; i < ITERATIONS; i++) {
            System.gc();
            long start = System.nanoTime();
            task.run();
            totalNs += (System.nanoTime() - start);
        }
        return totalNs / ITERATIONS;
    }

    static void saveResultsToCSV(String filename, java.util.List<Result> results) throws Exception {
        try (PrintWriter pw = new PrintWriter(filename)) {
            pw.println("Implementación,Método,n,TiempoNs,TiempoUs");
            results.forEach(pw::println);
        }
        System.out.println("✅ Guardado: " + filename + " (" + results.size() + " mediciones)");
    }
}
