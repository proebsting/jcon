//  vByteBuffer.java -- used for building vStrings



package rts;

public final class vByteBuffer {

    vString prefix;		// prefix string(s), if any
    byte[] data;		// byte array
    int len;			// length used;

static final int GrowthSize = 16;	// size of chunk allocated when growing



//  new vByteBuffer(n) -- make bytebuffer with room for n bytes

vByteBuffer(int n) {
    data = new byte[n];
}



//  length() -- return current length

final int length() {
    if (prefix == null) {
	return len;
    } else {
	return len + prefix.length();
    }
}



//  append(c) -- append character

final vByteBuffer append(char c) {
    if (len >= data.length) {
	if (prefix == null) {
	    prefix = vString.New(data);
	} else {
	    prefix = prefix.concat(vString.New(data));
	}
	data = new byte[GrowthSize];
	len = 0;
    }
    data[len++] = (byte) c;
    return this;
}



//  mkString() -- convert to vString

public vString mkString() {
    if (len < data.length) {
	byte[] b = new byte[len];
	System.arraycopy(data, 0, b, 0, len);
	data = b;
    }
    if (prefix == null) {
	return vString.New(data);
    } else {
	return prefix.concat(vString.New(data));
    }
}



} // class vByteBuffer
