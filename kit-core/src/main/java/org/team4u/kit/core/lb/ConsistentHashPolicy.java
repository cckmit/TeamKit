package org.team4u.kit.core.lb;

import org.team4u.kit.core.error.ExceptionUtil;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Collection;
import java.util.Iterator;
import java.util.SortedMap;
import java.util.TreeMap;

/**
 * 一致性哈希策略
 */
public class ConsistentHashPolicy {

    private SortedMap<Long, VirtualNode> ring = new TreeMap<Long, VirtualNode>();
    private MD5Hash hash = new MD5Hash();

    public ConsistentHashPolicy(Collection<PhysicalNode> pNodes, int vNodeCount) {
        for (PhysicalNode pNode : pNodes) {
            addNode(pNode, vNodeCount);
        }
    }

    public void addNode(PhysicalNode pNode, int vNodeCount) {
        int existingReplicas = getReplicas(pNode.toString());
        for (int i = 0; i < vNodeCount; i++) {
            VirtualNode vNode = new VirtualNode(pNode, i + existingReplicas);
            ring.put(hash.hash(vNode.toString()), vNode);
        }
    }

    public void removeNode(PhysicalNode pNode) {
        Iterator<Long> it = ring.keySet().iterator();
        while (it.hasNext()) {
            Long key = it.next();
            VirtualNode virtualNode = ring.get(key);
            if (virtualNode.matches(pNode.toString())) {
                it.remove();
            }
        }
    }

    public PhysicalNode getNode(String key) {
        if (ring.isEmpty()) {
            return null;
        }
        Long hashKey = hash.hash(key);
        SortedMap<Long, VirtualNode> tailMap = ring.tailMap(hashKey);
        hashKey = !tailMap.isEmpty() ? tailMap.firstKey() : ring.firstKey();
        return ring.get(hashKey).getParent();
    }

    public int getReplicas(String nodeName) {
        int replicas = 0;
        for (VirtualNode node : ring.values()) {
            if (node.matches(nodeName)) {
                replicas++;
            }
        }
        return replicas;
    }

    private static class MD5Hash {
        private MessageDigest instance;

        public MD5Hash() {
            try {
                instance = MessageDigest.getInstance("MD5");
            } catch (NoSuchAlgorithmException e) {
                throw ExceptionUtil.toRuntimeException(e);
            }
        }

        long hash(String key) {
            byte[] digest;

            synchronized (this) {
                instance.reset();
                instance.update(key.getBytes());
                digest = instance.digest();
            }

            long h = 0;
            for (int i = 0; i < 4; i++) {
                h <<= 8;
                h |= ((int) digest[i]) & 0xFF;
            }
            return h;
        }
    }

    public static class VirtualNode {
        private int replicaNumber;
        private PhysicalNode parent;

        public VirtualNode(PhysicalNode parent, int replicaNumber) {
            this.replicaNumber = replicaNumber;
            this.parent = parent;
        }

        public boolean matches(String host) {
            return parent.toString().equalsIgnoreCase(host);
        }

        @Override
        public String toString() {
            return parent.toString().toLowerCase() + ":" + replicaNumber;
        }

        public int getReplicaNumber() {
            return replicaNumber;
        }

        public PhysicalNode getParent() {
            return parent;
        }
    }

    public static class PhysicalNode {
        private String domain;
        private String ip;
        private int port;

        public PhysicalNode(String domain, String ip, int port) {
            this.domain = domain;
            this.ip = ip;
            this.port = port;
        }

        @Override
        public String toString() {
            return domain + ":" + ip + ":" + port;
        }

        public String getDomain() {
            return domain;
        }

        public String getIp() {
            return ip;
        }

        public int getPort() {
            return port;
        }
    }
}