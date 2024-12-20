package com.hsbc.vo;

import org.springframework.stereotype.Service;

import java.io.Serializable;

@Service
public class RuleResultVo implements Serializable {
    private boolean isHit;

    public boolean isHit() {
        return isHit;
    }

    public void setHit(boolean hit) {
        isHit = hit;
    }
}
