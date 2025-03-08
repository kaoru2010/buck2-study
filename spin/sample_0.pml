// https://spinroot.com/spin/Man/Manual.html

	byte state = 1;

	proctype A()
	{	byte tmp;
		(state==1) -> tmp = state; tmp = tmp+1; state = tmp
	}

	proctype B()
	{	byte tmp;
		(state==1) -> tmp = state; tmp = tmp-1; state = tmp
	}

	init
	{	run A(); run B()
	}
