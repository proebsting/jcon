package rts;

class iUtil {



//  sort(x) -- sort java array x of vValues, using x[i].compare().

static void sort(vValue x[])
{
    qsort(x, 0, x.length - 1);
}



// qsort(x, l, u) -- sort x[l] through x[u] inclusive, using x[i].compare().
//
// based on Jon Bentley, Programming Pearls, CACM 27:4 (Apr 1984) p.289

private static void qsort(vValue x[], int l, int u)
{
    int d, i, r, tr, llow;
    vValue t, v;

    if (l >= u) {
    	return;
    }

    r = l + (int) ((u - l) * Math.random());
    swap(x, l, r);
    t = x[l];
    tr = t.rank();

    llow = l;
    for (i = l + 1; i <= u; i++) {
	v = x[i];
	d = v.rank() - tr;
	if (d == 0) {
	    d = v.compareTo(t);
	}
    	if (d < 0) {
	    llow++;
	    swap(x, llow, i);
	}
    }
    swap(x, l, llow);
    qsort(x, l, llow-1);
    qsort(x, llow+1, u);
}


// swap(x, i, j) -- exchange x[i] and x[j].

private static void swap(vValue x[], int i, int j)
{
    vValue o = x[i];
    x[i] = x[j];
    x[j] = o;
}
    
} // class iUtil
