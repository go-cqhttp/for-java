package com.zhuangxv.bot.core.component;

/**
 * @author xiaoxu
 * @since 2020/10/14
 **/
public class SnowFlakeIdGenerator implements IdGenerator {

    private final SnowFlake snowFlake;

    public SnowFlakeIdGenerator() {
        this.snowFlake = new SnowFlake(0);
    }

    @Override
    public Long createId() {
        return this.snowFlake.nextId();
    }

    @Override
    public String createStrId() {
        return String.valueOf(this.snowFlake.nextId());
    }

    public static class SnowFlake {

        //2020-10-24 00:00:00
        private static final long startTimestamp = 1603123200000L;

        private static final long workerIdLength = 15L;
        private static final long sequenceLength = 7L;

        private static final long maxWorkerId = ~(-1L << workerIdLength);
        private static final long maxSequence = ~(-1L << sequenceLength);

        private static final long workerIdOffset = sequenceLength;
        private static final long timestampOffset = sequenceLength + workerIdLength;

        private final long workerId;
        private long sequence = 0L;
        private long lastTimestamp = -1L;
        private long sequenceExpand = 0L;

        public SnowFlake(long workerId) {
            if (workerId > maxWorkerId || workerId < 0) {
                throw new IllegalArgumentException(String.format("WorkIdd必须取0-%d区间的值", maxWorkerId));
            }
            this.workerId = workerId;
        }

        public synchronized long nextId() {
            long timestamp = timeGen();
            if (timestamp < lastTimestamp) {
                throw new RuntimeException(String.format("时钟回拨，拒绝生成ID，回拨值：%d", lastTimestamp - timestamp));
            }
            if (timestamp == lastTimestamp) {
                sequence = ++sequence & maxSequence;
                if (sequence == 0L) {
                    timestamp = tilNextMillis();
                }
            } else {
                sequence = ++sequenceExpand & 1L;
            }
            lastTimestamp = timestamp;
            return (timestamp - startTimestamp) << timestampOffset
                    | workerId << workerIdOffset
                    | sequence;
        }

        private long tilNextMillis() {
            long timestamp = timeGen();
            while (timestamp <= lastTimestamp) {
                timestamp = timeGen();
            }
            return timestamp;
        }

        private long timeGen() {
            return System.currentTimeMillis();
        }
    }

}
