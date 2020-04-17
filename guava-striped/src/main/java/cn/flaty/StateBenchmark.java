package cn.flaty;

import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author flaty
 * @date 2020-4-17
 */
@BenchmarkMode({Mode.Throughput})
@OutputTimeUnit(TimeUnit.SECONDS)
@Threads(4)
//@Warmup(iterations = 1)
@Measurement(iterations = 2,batchSize = 1000)
@State(Scope.Benchmark)
public class StateBenchmark {


    private int i = 0;
    private Lock lock = new ReentrantLock();


//    @Benchmark
//    public void syncLock() throws InterruptedException {
//        synchronized (StateBenchmark.class) {
//            i++;
//        }
//        System.out.println("syncLock sum:" + i);
//    }

    @Benchmark
    public void aqsLock() throws InterruptedException {
        lock.lock();
        try {
            i++;
        } finally {
            lock.unlock();
        }
        System.out.println("aqsLock sum:" + i);
    }


    public static void main(String[] args) throws Exception {
        Options opt = new OptionsBuilder().include(StateBenchmark.class.getSimpleName()).build();
        new Runner(opt).run();
        System.in.read();
    }

}

