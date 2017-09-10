package org.hdj.tocamtmini.test;

import org.hdj.tocamtmini.core.MyTocamt;

public class Main {
    public static void main(String[] args) {
        MyTocamt tocamt = new MyTocamt(8080);
        tocamt.start();
    }
}
