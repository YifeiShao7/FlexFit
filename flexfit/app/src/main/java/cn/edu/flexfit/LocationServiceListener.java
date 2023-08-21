package cn.edu.flexfit;

public interface LocationServiceListener {
    void onDistanceChanged(double distance);

    void onTimeChanged(int time);
}
