package io.qhzhou.nztrip.dto;

/**
 * Created by qianhao.zhou on 8/9/16.
 */
public final class Token {

    public Token(Role role, int id, long expireTime) {
        this.role = role;
        this.expireTime = expireTime;
        this.id = id;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public long getExpireTime() {
        return expireTime;
    }

    public void setExpireTime(long expireTime) {
        this.expireTime = expireTime;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public boolean isExpired() {
        return System.currentTimeMillis() >= expireTime;
    }

    private Role role;
    private long expireTime;
    private int id;

}
