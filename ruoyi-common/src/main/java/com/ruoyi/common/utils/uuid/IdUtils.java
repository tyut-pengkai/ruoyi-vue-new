package com.ruoyi.common.utils.uuid;

import java.lang.management.ManagementFactory;
import java.net.InetAddress;
import java.net.NetworkInterface;

/**
 * ID生成器工具类
 *
 * @author ruoyi
 */
public class IdUtils
{
    private static final SnowFlake SNOW_FLAKE = new SnowFlake();

    /**
     * 获取随机UUID
     *
     * @return 随机UUID
     */
    public static String randomUUID()
    {
        return UUID.randomUUID().toString();
    }

    /**
     * 简化的UUID，去掉了横线
     *
     * @return 简化的UUID，去掉了横线
     */
    public static String simpleUUID()
    {
        return UUID.randomUUID().toString(true);
    }

    /**
     * 获取随机UUID，使用性能更好的ThreadLocalRandom生成UUID
     *
     * @return 随机UUID
     */
    public static String fastUUID()
    {
        return UUID.fastUUID().toString();
    }

    /**
     * 简化的UUID，去掉了横线，使用性能更好的ThreadLocalRandom生成UUID
     *
     * @return 简化的UUID，去掉了横线
     */
    public static String fastSimpleUUID()
    {
        return UUID.fastUUID().toString(true);
    }

    /**
     * 获取雪花算法生成的ID
     *
     * @return 雪花算法ID
     */
    public static long snowflakeId()
    {
        return SNOW_FLAKE.nextId();
    }

    /**
     * 获取雪花算法生成的ID字符串
     *
     * @return 雪花算法ID字符串
     */
    public static String snowflakeIdString()
    {
        return String.valueOf(SNOW_FLAKE.nextId());
    }
}

/**
 * 雪花算法实现
 */
class SnowFlake {
    // 起始的时间戳（可自定义调整）
    private final static long START_STAMP = 1609459200000L; // 2021-01-01 00:00:00

    // 每一部分占用的位数
    private final static long SEQUENCE_BIT = 12; // 序列号占用的位数
    private final static long MACHINE_BIT = 5;   // 机器标识占用的位数
    private final static long DATACENTER_BIT = 5; // 数据中心占用的位数

    // 每一部分的最大值
    private final static long MAX_DATACENTER_NUM = ~(-1L << DATACENTER_BIT);
    private final static long MAX_MACHINE_NUM = ~(-1L << MACHINE_BIT);
    private final static long MAX_SEQUENCE = ~(-1L << SEQUENCE_BIT);

    // 每一部分向左的位移
    private final static long MACHINE_LEFT = SEQUENCE_BIT;
    private final static long DATACENTER_LEFT = SEQUENCE_BIT + MACHINE_BIT;
    private final static long TIMESTAMP_LEFT = DATACENTER_LEFT + DATACENTER_BIT;

    private long datacenterId;    // 数据中心ID
    private long machineId;       // 机器ID
    private long sequence = 0L;   // 序列号
    private long lastStamp = -1L; // 上一次时间戳

    public SnowFlake() {
        this.datacenterId = getDatacenterId();
        this.machineId = getMachineId();
    }

    public SnowFlake(long datacenterId, long machineId) {
        if (datacenterId > MAX_DATACENTER_NUM || datacenterId < 0) {
            throw new IllegalArgumentException("datacenterId can't be greater than " + MAX_DATACENTER_NUM + " or less than 0");
        }
        if (machineId > MAX_MACHINE_NUM || machineId < 0) {
            throw new IllegalArgumentException("machineId can't be greater than " + MAX_MACHINE_NUM + " or less than 0");
        }
        this.datacenterId = datacenterId;
        this.machineId = machineId;
    }

    /**
     * 产生下一个ID
     */
    public synchronized long nextId() {
        long currStamp = getNewStamp();
        if (currStamp < lastStamp) {
            throw new RuntimeException("Clock moved backwards. Refusing to generate id");
        }

        if (currStamp == lastStamp) {
            // 相同毫秒内，序列号自增
            sequence = (sequence + 1) & MAX_SEQUENCE;
            // 同一毫秒的序列数已经达到最大
            if (sequence == 0L) {
                currStamp = getNextMill();
            }
        } else {
            // 不同毫秒内，序列号置为0
            sequence = 0L;
        }

        lastStamp = currStamp;

        return (currStamp - START_STAMP) << TIMESTAMP_LEFT // 时间戳部分
                | datacenterId << DATACENTER_LEFT          // 数据中心部分
                | machineId << MACHINE_LEFT                // 机器标识部分
                | sequence;                                // 序列号部分
    }

    private long getNextMill() {
        long mill = getNewStamp();
        while (mill <= lastStamp) {
            mill = getNewStamp();
        }
        return mill;
    }

    private long getNewStamp() {
        return System.currentTimeMillis();
    }

    /**
     * 获取数据中心ID（自动生成）
     */
    private long getDatacenterId() {
        try {
            InetAddress ip = InetAddress.getLocalHost();
            NetworkInterface network = NetworkInterface.getByInetAddress(ip);
            long id;
            if (network == null) {
                id = 1;
            } else {
                byte[] mac = network.getHardwareAddress();
                if (mac == null) {
                    id = 1;
                } else {
                    id = ((0x000000FF & (long) mac[mac.length - 1]) |
                            (0x0000FF00 & (((long) mac[mac.length - 2]) << 8))) >> 6;
                    id = id % (MAX_DATACENTER_NUM + 1);
                }
            }
            return id;
        } catch (Exception e) {
            return 1L;
        }
    }

    /**
     * 获取机器ID（自动生成）
     */
    private long getMachineId() {
        try {
            String name = ManagementFactory.getRuntimeMXBean().getName();
            if (name != null && !name.isEmpty()) {
                // 获取进程ID
                return Long.parseLong(name.split("@")[0]);
            }
        } catch (Exception e) {
            // ignore
        }
        return 0L;
    }
}