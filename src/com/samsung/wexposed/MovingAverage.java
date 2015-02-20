package com.samsung.wexposed;


public class MovingAverage {
    private double curAvg;
    private int curIndex;
    private long[] window;
    private boolean full;

    public MovingAverage(int ws) {
        if (ws <= 1) return;    //the window size cannot be smaller than 2
        curAvg = 0;
        window = new long[ws];
        for(int i=0; i<ws; i++) window[i] = 0;    //initialize the array with all zeros
        curIndex = 0;
        full = false;
    }

    public double next(long num) {
        int w1 = curIndex;        // curIndex is equal to current window size if not full
        if(full) w1 = window.length;
        
        long tmp = add(num);
        
        int w2 = curIndex;
        if(full) w2 = window.length;

//       System.out.println("tmp = " + tmp + ", num = " + num + ", w1 = " + w1 + ", w2 = " + w2 + ", full = " + full  + ", curIndex = " + curIndex);
        curAvg = curAvg * w1 / w2 + (num - tmp) / w2;

        
//        System.out.println("post_cur_avg = " + cur_avg);
        return curAvg;
    }
    
    //@param num: the number to be added to the current window
    //@return: if the window is full, the least recently added number will be returned; otherwise 0.
    private long add(long num){
        long ret = window[curIndex];
        window[curIndex++] = num;
        if(curIndex == window.length){
            full = true;
            curIndex = 0;
        }        
        return ret;
    }
}
