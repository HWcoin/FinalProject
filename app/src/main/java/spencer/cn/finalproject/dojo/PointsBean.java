package spencer.cn.finalproject.dojo;

/**
 * Created by Administrator on 2017/5/5.
 */

public class PointsBean {
    private int integral;
    private int seq;

    public PointsBean() {
    }

    public PointsBean(int integral, int seq) {
        this.integral = integral;
        this.seq = seq;
    }

    public int getIntegral() {
        return integral;
    }

    public void setIntegral(int integral) {
        this.integral = integral;
    }

    public int getSeq() {
        return seq;
    }

    public void setSeq(int seq) {
        this.seq = seq;
    }
}
