package cn.flaty;

import com.google.common.util.concurrent.Striped;
import org.apache.commons.lang3.RandomUtils;
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
@Threads(8)
@Fork(3)
@Warmup(iterations = 1)
@Measurement(iterations = 2)
@State(Scope.Benchmark)
public class StripedBenchmark {


    private static Striped<Lock> lockStriped = Striped.lock(1024);

    private static Lock lock = new ReentrantLock();

    @Benchmark
    public void lock() throws InterruptedException {
        Lock l = lockStriped.get(RandomUtils.nextInt());
        l.lock();
        try {
            Thread.sleep(RandomUtils.nextInt(1, 10));
        } finally {
            l.unlock();
        }
    }

    @Benchmark
    public void aqsLock() throws InterruptedException {
        lock.lock();
        try {
            Thread.sleep(RandomUtils.nextInt(1, 10));
        } finally {
            lock.unlock();
        }
    }

    @Benchmark
    public void syncLock() throws InterruptedException {
        synchronized (StripedBenchmark.class) {
            Thread.sleep(RandomUtils.nextInt(1, 10));
        }
    }


    public static void main(String[] args) throws Exception {
        Options opt = new OptionsBuilder().include(StripedBenchmark.class.getSimpleName()).build();
        new Runner(opt).run();
        System.in.read();
    }

}

