package com.ruoyi.common.utils;

import org.springframework.util.Assert;

import java.util.PriorityQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

public class SysCache {

    private static final ScheduledExecutorService swapExpiredPool = new ScheduledThreadPoolExecutor(10);

    private static final ReentrantLock lock = new ReentrantLock();

    private static final ConcurrentHashMap<String, Node> cache = new ConcurrentHashMap<>(1024);
    /**
     * 让过期时间最小的数据排在队列前，在清除过期数据时
     * ，只需查看缓存最近的过期数据，而不用扫描全部缓存
     *
     * @see Node#compareTo(Node)
     * @see SwapExpiredNodeWork#run()
     */
    private static final PriorityQueue<Node> expireQueue = new PriorityQueue<>(1024);

    static {
        //使用默认的线程池，每5秒清除一次过期数据
        //线程池和调用频率 最好是交给调用者去设置。
        swapExpiredPool.scheduleWithFixedDelay(new SwapExpiredNodeWork(), 5, 5, TimeUnit.SECONDS);
    }

    public static Object set(String key, Object value, long ttl) {
        Assert.isTrue(StringUtils.isNotBlank(key), "key can't be empty");
//        Assert.isTrue(ttl > 0, "ttl must greater than 0");
        if(ttl > 0) {
            long expireTime = System.currentTimeMillis() + ttl;
            Node newNode = new Node(key, value, expireTime);
            lock.lock();
            try {
                Node old = cache.put(key, newNode);
                expireQueue.add(newNode);
                //如果该key存在数据，还要从过期时间队列删除
                if (old != null) {
                    expireQueue.remove(old);
                    return old.value;
                }
                return null;
            } finally {
                lock.unlock();
            }
        }
        return null;
    }

    /**
     * 拿到的数据可能是已经过期的数据，可以再次判断一下
     * if（n.expireTime<System.currentTimeMillis()）{
     * return null;
     * }
     * 也可以直接返回整个节点Node ，交给调用者去取舍
     * <p>
     * <p>
     * 无法判断不存在该key,还是该key存的是一个null值，如果需要区分这两种情况
     * 可以定义一个全局标识，标识key不存在
     * public static final NOT_EXIST = new Object();
     * 返回值时
     * return n==null?NOT_EXIST:n.value;
     */
    public static Object get(String key) {
        Node n = cache.get(key);
        return n == null ? null : n.value;
    }

    /**
     * 删出KEY，并返回该key对应的数据
     */
    public static Object delete(String key) {
        lock.lock();
        try {
            Node n = cache.remove(key);
            if (n == null) {
                return null;
            } else {
                expireQueue.remove(n);
                return n.value;
            }
        } finally {
            lock.unlock();
        }
    }

    /**
     * 删除已经过期的数据
     */
    private static class SwapExpiredNodeWork implements Runnable {
        @Override
        public void run() {
            long now = System.currentTimeMillis();
            while (true) {
                lock.lock();
                try {
                    Node node = expireQueue.peek();
                    //没有数据了，或者数据都是没有过期的了
                    if (node == null || node.expireTime > now) {
                        return;
                    }
                    cache.remove(node.key);
                    expireQueue.poll();
                } finally {
                    lock.unlock();
                }
            }
        }
    }


    private static class Node implements Comparable<Node> {
        private String key;
        private Object value;
        private long expireTime;

        public Node(String key, Object value, long expireTime) {
            this.value = value;
            this.key = key;
            this.expireTime = expireTime;
        }

        /**
         * @see SwapExpiredNodeWork
         */
        @Override
        public int compareTo(Node o) {
            long r = this.expireTime - o.expireTime;
            if (r > 0) {
                return 1;
            }
            if (r < 0) {
                return -1;
            }
            return 0;
        }
    }

}
