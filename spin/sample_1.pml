// https://spinroot.com/spin/Man/Manual.html

byte state = 1;

proctype A() {
    end: atomic {
        (state==1) -> state = state+1;
    }
}

proctype B() {
    end: atomic {
        (state==1) -> state = state-1;
    }
}

init {
    run A();
    run B();
}
