package cn.ac.iscas.dmo.connector.jdbc.model;

import java.util.List;

/**
 * @author zhuquanwen
 * @version 1.0
 * @date 2024/9/29 9:33
 */

public class A {
    private Integer c1;
    private String c2;

    private List<B> bs;

    public Integer getC1() {
        return c1;
    }

    public void setC1(Integer c1) {
        this.c1 = c1;
    }

    public String getC2() {
        return c2;
    }

    public void setC2(String c2) {
        this.c2 = c2;
    }

    public List<B> getBs() {
        return bs;
    }

    public void setBs(List<B> bs) {
        this.bs = bs;
    }
}
