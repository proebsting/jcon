//  Quantize.java -- image quantization code
//
//  A linear algorithm based on combining octtree nodes.



//  Cold-hearted orb that rules the night
//  Removes the colours from our sight
//  Red is gray, and yellow white
//  But we decide which is right
//  And which is quantization error
//
//	Jef Poskanzer, after Justin Hayward
//	(from the "ppmtopgm" man page)



package rts;

import java.awt.*;
import java.awt.image.*;
import java.util.*;




class onode {			// octtree node:
    onode parent;		// parent node
    onode children[];		// eight subnodes
    onode link;			// next node in sort bin
    int level;			// the level of this node
    int ntotal;			// total number of pixels represented
    int nhere;			// number represented here (not in children)
    long rsum, gsum, bsum;	// sums of rgb values here (not incl children)

    public onode(int n, int r, int g, int b) {
	ntotal = nhere = n;
	rsum = r * (long) n;
	gsum = g * (long) n;
	bsum = b * (long) n;
    }

    public onode() {}

} // class onode




public class Quantize {

//  configuration: n bits of resolution require a 2^(3n)-sized int array

private static final int Qres = 6;			// quantizatn resolution
private static final int Qignore = 8 - Qres;		// bits ignored
private static final int Qshades = 1 << Qres;		// shades per primary
private static final int Qmask = Qshades - 1;		// corresponding bitmask
private static final int Qbins = 1 << (3 * Qres);	// number of tally bins

private static final int Qranks = 100;			// rank bins per level



//  Quantize.toNcolors(pixels, n) -- reduce an array of RGB pixels to n colors
//
//  In the process, color resolution is truncated to Qres bits (see above).

public static void toNcolors(int[] data, int ncolors)
{
    reduce(data);
    int[] tlist = tally(data);
    int icolors = nzcount(tlist);
    onode root = mktree(tlist);
    if (icolors > ncolors) {
	prune(root, icolors - ncolors);
    }
    mkmap(tlist, root);
    remap(data, tlist);
}



//  reduce(data) -- reduce to Qres bits per primary

private static void reduce(int[] data) {
    int n = data.length;
    for (int i = 0; i < n; i++) {
	int w = data[i];
	int r = (w >> (16 + Qignore - 2 * Qres)) & (Qmask << 2 * Qres);
	int g = (w >> (8 + Qignore - Qres)) & (Qmask << Qres);
	int b = (w >> Qignore) & Qmask;
	data[i] = r | g | b;
    }
}



//  tally(data) -- return int array of counts of data values

private static int[] tally(int[] data) {
    int[] t = new int[Qbins];
    int n = data.length;
    for (int i = 0; i < n; i++) {
    	t[data[i]]++;
    }
    return t;
}



//  nzcount(data) -- count number of nonzero tally bins

private static int nzcount(int[] data) {
    int n = data.length;
    int t = 0;
    for (int i = 0; i < n; i++) {
    	if (data[i] != 0) {
	    t++;
	}
    }
    return t;
}



//  mktree(tallies) -- make octtree of onodes

private static onode mktree(int[] tlist) {
    onode root = new onode();
    int i = 0;
    for (int r = 0; r < Qshades; r++) {
	for (int g = 0; g < Qshades; g++) {
	    for (int b = 0; b < Qshades; b++) {
	    	int n = tlist[i++];
		if (n > 0) {
		    insert(root, new onode(n, r, g, b), Qres, r, g, b);
		}
	    }
	}
    }
    return root;
}



//  insert(root, node, nbits, r, g, b)

private static void insert(onode root, onode node,
			    int nbits, int r, int g, int b) {

    if (root.children == null) {
	root.children = new onode[8];
    }

    int bmask = 1 << (nbits - 1);
    int i = 0;
    if ((r & bmask) != 0) { i += 4; }
    if ((g & bmask) != 0) { i += 2; }
    if ((b & bmask) != 0) { i += 1; }

    onode o;
    if (nbits == 1) {
    	o = root.children[i] = node;
	node.parent = root;
	node.level = root.level + 1;
    } else {
    	o = root.children[i];
	if (o == null) {
	    o = root.children[i] = new onode();
	    o.parent = root;
	    o.level = root.level + 1;
	}
	insert(o, node, nbits - 1, r, g, b);
    }

    root.ntotal += node.ntotal;
}



//  prune(root, n) -- prune n nodes to reduce number of colors

private static void prune(onode root, int nprune) {
    int n = Qranks * (Qres + 1);
    onode[] a = new onode[n];
    double qmul = Qranks / Math.log(root.ntotal + 1);

    distrib(a, qmul, root);

    for (int i = 0; i < n; i++) {
    	for (onode o = a[i]; o != null; o = o.link) {
	    nprune -= rmnode(o);
	    if (nprune <= 0) {
	    	return;
	    }
	}
    }
}



//  distrib(a, qmul, root) -- add nodes to sortbins based on level & population

private static void distrib(onode[] a, double qmul, onode root) {

    if (root == null) {
    	return;
    }

    int base = Qranks * (Qres - root.level);
    int offset = (int) (qmul * Math.log(root.ntotal));
    int i = base + offset;
    root.link = a[i];
    a[i] = root;

    if (root.children != null) {
	for (i = 0; i < 8; i++) {
	    distrib(a, qmul, root.children[i]);
	}
    }
}



//  rmnode(node) -- prune node and its children, returning count pruned

static int rmnode(onode node) {
    int n = 0;
    if (node.parent == null) {
	return 0;	// pruned earlier
    }
    if (node.children != null) {
	for (int i = 0; i < 8; i++) {
	    if (node.children[i] != null) {
		n += rmnode(node.children[i]);
	    }
	}
    }
    onode p = node.parent;
    if (p.nhere > 0) {
    	n++;
    }
    p.nhere += node.nhere;
    p.rsum += node.rsum;
    p.gsum += node.gsum;
    p.bsum += node.bsum;
    for (int i = 0; i < 8; i++) {
	if (p.children[i] == node) {
	    p.children[i] = null;
	}
    }
    node.parent = null;
    return n;
}



//  mkmap(array, root) -- make mapping entries reflecting tree

private static void mkmap(int[] cmap, onode root) {
    int i = 0;
    for (int r = 0; r < Qshades; r++) {
	for (int g = 0; g < Qshades; g++) {
	    for (int b = 0; b < Qshades; b++) {
		if (cmap[i] > 0) {
		    cmap[i] = rgbspec(root, Qres, r, g, b);
		}
		i++;
	    }
	}
    }
}



//  rgbspec(root, nbits, r, g, b) -- find node in tree and return int rgb spec

private static int rgbspec(onode o, int nbits, int r, int g, int b) {

    if (nbits > 0) {
	int bmask = 1 << (nbits - 1);
	int i = 0;
	if ((r & bmask) != 0) { i += 4; }
	if ((g & bmask) != 0) { i += 2; }
	if ((b & bmask) != 0) { i += 1; }
	onode c = o.children[i];
	if (c != null) {
	    return rgbspec(c, nbits - 1, r, g, b);
	}
    }
    r = (int) (((o.rsum << Qignore) + (o.rsum >> (Qres - Qignore))) / o.nhere);
    g = (int) (((o.gsum << Qignore) + (o.gsum >> (Qres - Qignore))) / o.nhere);
    b = (int) (((o.bsum << Qignore) + (o.bsum >> (Qres - Qignore))) / o.nhere);
    return 0xFF000000 | (r << 16) | (g << 8) | b;
}



//  remap(data, cmap) -- map reduced-res specs to full spec via color map

private static void remap(int[] data, int[] cmap) {
    int n = data.length;
    for (int i = 0; i < n; i++) {
	data[i] = cmap[data[i]];
    }
}



} // class Quantize
